package com.trade.jobs.kline;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.JdbcTemplate;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;

public class KlineCalculateJob {

    private final Logger log = LoggerFactory.getLogger(getClass());

    @Resource
    private JdbcTemplate jdbc;

    @Resource
    private ExecutorService executorService;

    private SimpleDateFormat longSdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    private SimpleDateFormat smallSdf = new SimpleDateFormat("yyyy-MM-dd");

    private Map<Integer, Integer> timeStep = new TreeMap<>(new Comparator<Integer>() {
        @Override
        public int compare(Integer o1, Integer o2) {
            return o2.compareTo(o1);
        }
    });

    public KlineCalculateJob() {
        timeStep.put(1, 0);
        timeStep.put(3, 1);
        timeStep.put(5, 2);
        timeStep.put(15, 3);
        timeStep.put(30, 4);
        timeStep.put(1 * 60, 5);
        timeStep.put(2 * 60, 6);
        timeStep.put(4 * 60, 7);
        timeStep.put(6 * 60, 8);
        timeStep.put(12 * 60, 9);
        timeStep.put(1 * 24 * 60, 10);
        timeStep.put(3 * 24 * 60, 11);
        timeStep.put(7 * 24 * 60, 12);
    }

    public List<Integer> getFviFids() {
        return jdbc.queryForList("SELECT id fid FROM market WHERE status = 1 and trade_status = 1", Integer.class);
    }

    public void calculate(int fviFid, int mins) {
        Integer key = timeStep.get(mins);
        Map<String, Object> map = getLastUpdateTime2(fviFid, key);
        String lastUpdateTime = (String) map.get("ftime");
        if ("0".equals(lastUpdateTime)) {
            lastUpdateTime = getFirstTime(fviFid);
        }

        Date date = parseDate(lastUpdateTime, mins);
        Calendar calendar = Calendar.getInstance();
        while (true) {
            if (date.getTime() > System.currentTimeMillis()) {
                break;
            }
            String fromTime = longSdf.format(date);
            calendar.setTime(date);
            calendar.add(Calendar.MINUTE, mins);
            date = calendar.getTime();
            String endTime = longSdf.format(date);
            calculate(fviFid, key, fromTime, endTime);
        }
    }

    public Date parseDate(String source, int mins) {
        try {
            // 周期超过一天，则不要时分秒
            if (mins >= 24 * 60) {
                return smallSdf.parse(source);
            }
            Date date = longSdf.parse(source);

            Calendar calendar = Calendar.getInstance();
            calendar.setTime(new Date());
            calendar.add(Calendar.MINUTE, -mins * 800);
            Date maxShowDate = calendar.getTime();
            if (date.getTime() < maxShowDate.getTime()) {
                date = maxShowDate;
            }

            return date;
        } catch (Exception e) {
            return new Date();
        }
    }

    public void calculate(int fviFid, Integer key, String fromTime, String endTime) {
//        if (key == 10 && fviFid == 4) {
//            System.out.println("==========" + fviFid + "-" + fromTime + "-" + endTime);
//        }
        jdbc.update("REPLACE INTO kline(fid, fvi_fid, fkey, ftime, fkai, fshou, fgao, fdi, fliang) SELECT * from \n" +
                "(select fid, fvi_fid, ? fkey, ftime, fkai from fperiod WHERE fvi_fid = ? and ftime BETWEEN ? and ? limit 1) a,\n" +
                "(select fshou from fperiod WHERE fvi_fid = ? and ftime BETWEEN ? and ? ORDER BY fid desc limit 1) b,\n" +
                "(select MAX(fgao) fgao from fperiod WHERE fvi_fid = ? and ftime BETWEEN ? and ? limit 1) c,\n" +
                "(select MIN(fdi) fdi from fperiod WHERE fvi_fid = ? and ftime BETWEEN ? and ? limit 1) d,\n" +
                "(select SUM(fliang) fliang from fperiod WHERE fvi_fid = ? and ftime BETWEEN ? and ? limit 1) e", new Object[]{
                key, fviFid, fromTime, endTime,
                fviFid, fromTime, endTime,
                fviFid, fromTime, endTime,
                fviFid, fromTime, endTime,
                fviFid, fromTime, endTime
        });
    }

    public String getFirstTime(int fviFid) {
        return jdbc.queryForMap("select IFNULL(MIN(ftime), 0) ftime from fperiod WHERE fvi_fid = ? limit 1", new Object[]{fviFid}).get("ftime") + "";
    }

    public Map<String, Object> getLastUpdateTime2(int fviFid, int key) {
        return jdbc.queryForMap("select IFNULL(MAX(ftime), 0) ftime, IFNULL(MAX(fid), 0) fid from kline WHERE fvi_fid = ? and fkey = ? ORDER BY ftime desc", new Object[]{fviFid, key});
    }

    /**
     * K线计算
     * 直接读取数据库计算
     * 首次可能会有点慢
     * 但是由于数据是增量计算的所以没有问题
     * 单次计算速度在秒级
     * @throws Exception
     */
    @PostConstruct
    public void run() throws Exception {
        synchronized (this) {
            long beginTime = System.currentTimeMillis();
            log.info("计算周期K线");
            try {
                calculate();
            } catch (Exception e) {
                log.error("计算周期K线 {}", e.toString());
            }
            log.info("计算周期K线耗时: {}", (System.currentTimeMillis() - beginTime));
        }
    }

    private void calculate() throws Exception {
        List<Integer> fviFids = getFviFids();
        CountDownLatch latch = new CountDownLatch(fviFids.size());
        fviFids.forEach(fviFid -> {
            timeStep.forEach((key, val) -> {
                // 周期为1分钟的不需要计算
                if (val != 0) {
                    // 这里用多线程会有点的问题，因为获取上一次更新时间有顺序依赖，暂时去掉，有时间可以改一下
//                    executorService.submit(() -> {
                        try {
                            log.debug("计算币{}周期{}", fviFid, key);
                            calculate(fviFid, key);
                        } catch (Exception e) {
                            e.printStackTrace();
                        } finally {
                            log.debug("计算币{}周期{}完成", fviFid, key);
                            latch.countDown();
                        }
//                    });
                }
            });
        });
//        latch.await();
    }


}

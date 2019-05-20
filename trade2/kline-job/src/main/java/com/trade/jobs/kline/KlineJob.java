package com.trade.jobs.kline;

import com.alibaba.druid.util.JdbcUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import javax.sql.DataSource;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;

public class KlineJob {

    private final SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    private final Logger log = LoggerFactory.getLogger(getClass());

    @Resource
    private ExecutorService executorService;

    @Resource
    private DataSource dataSource;

    public KlineJob() {
        log.info("K线生成任务启动");
    }

    @PostConstruct
    public void run() {
        long beginTime = System.currentTimeMillis();
        log.info("生成K线数据");
        try {
            generatePeriods();
        } catch (Exception e) {
            log.error("生成K线失败 {}", e.toString());
        }
        log.info("生成K线数据耗时: {}", (System.currentTimeMillis() - beginTime));
    }

    private void generatePeriod(Object vid) {
        Timestamp startTime = (Timestamp) getStartTime(vid);
        String queryTime;
        if (startTime != null) {
            Calendar date = Calendar.getInstance();
            date.setTime(startTime);
            date.add(Calendar.MINUTE, 1);
            date.set(Calendar.SECOND, 0);
            queryTime = sdf.format(date.getTime());
        } else {
            queryTime = "1970-01-01 00:00:00";
        }

        List<Map<String, Object>> datas = getDatas(vid, queryTime);
        datas.forEach(period -> {
            String beginTime = period.get("time") + ":00";
            String endTime = null;
            try {
                Calendar date = Calendar.getInstance();
                date.setTime(sdf.parse(beginTime));
                date.add(Calendar.MINUTE, 1);
                date.add(Calendar.SECOND, -1);
                endTime = sdf.format(date.getTime());
            } catch (ParseException e) {
                e.printStackTrace();
            }
            callProdure(vid, beginTime, endTime);
        });
    }

    private void generatePeriods() throws InterruptedException {
        List<Map<String, Object>> coins = getCoinTypes();
        CountDownLatch latch = new CountDownLatch(coins.size());
        if (coins != null && coins.size() > 0) {
            coins.forEach(coin -> {
                executorService.submit(() -> {
                    try {
                        generatePeriod(coin.get("fid"));
                    } catch (Exception e) {
                        e.printStackTrace();
                    } finally {
                        latch.countDown();
                    }
                });
            });
        }
        latch.await();
    }

    private List<Map<String, Object>> getCoinTypes() {
        long beginTime = System.currentTimeMillis();
        List<Map<String, Object>> maps = executeQuery("select id as fid from market where status = 1");
        log.debug("查询币列表耗时: {}", (System.currentTimeMillis() - beginTime));
        return maps;
    }

    private List<Map<String, Object>> getDatas(Object vid, String time) {
        long beginTime = System.currentTimeMillis();
        List<Map<String, Object>> maps = executeQuery("SELECT SUBSTR(fCreateTime, 1, 16) time FROM fentrustlog WHERE FVI_type = ? AND isactive = 1 AND fCreateTime >= ? GROUP BY SUBSTR(fCreateTime, 1, 16)", vid, time);
        log.debug("查询需要币{}生成的时间段耗时: {}", vid, (System.currentTimeMillis() - beginTime));
        return maps;
    }

    private Object getStartTime(Object vid) {
        long beginTime = System.currentTimeMillis();
        List<Map<String, Object>> list = executeQuery("select ftime as startTime from fperiod WHERE fvi_fid = ? order by ftime desc limit 1", vid);
        log.debug("查询币{}最后一次更新时间耗时: {}", vid, (System.currentTimeMillis() - beginTime));
        return list != null && list.size() > 0 ? list.get(0).get("startTime") : null;
    }

    private void callProdure(Object vid, String beginTime, String endTime) {
        log.debug("call getOneMinsData({},{},{})", vid, beginTime, endTime);
        long _beginTime = System.currentTimeMillis();
        executeUpdate("call getOneMinsData(?,?,?)", vid, beginTime, endTime);
        log.debug("生成币{}一分钟K线数据耗时: {}", vid, (System.currentTimeMillis() - _beginTime));
    }

    private List<Map<String, Object>> executeQuery(String sql, Object... parameters) {
        List<Map<String, Object>> ret = null;
        try {
            ret = JdbcUtils.executeQuery(dataSource, sql, parameters);
        } catch (Exception e) {
            e.printStackTrace();
            log.error(e.toString());
        }
        return ret;
    }

    private int executeUpdate(String sql, Object... parameters) {
        int ret = 0;
        try {
            ret = JdbcUtils.executeUpdate(dataSource, sql, parameters);
        } catch (Exception e) {
            e.printStackTrace();
            log.error(e.toString());
        }
        return ret;
    }

}

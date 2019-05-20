package com.trade.jobs.kline;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.JdbcTemplate;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;

public class KlineSyncCacheJob {

    private final Logger log = LoggerFactory.getLogger(getClass());

    @Resource
    private JdbcTemplate jdbc;

    @Resource
    private JedisPool jedisPool;

    @Resource
    private ExecutorService executorService;

    private Map<Integer, Integer> timeStep = new TreeMap<>(new Comparator<Integer>() {
        @Override
        public int compare(Integer o1, Integer o2) {
            return o2.compareTo(o1);
        }
    });

    public KlineSyncCacheJob() {
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

    private List<Integer> getFviFids() {
        return jdbc.queryForList("SELECT id as fid FROM market WHERE status = 1 and trade_status = 1", Integer.class);
    }

    private String getKlineData(int fviFid, int key) {
        if (key == 0) {
            // 一分钟数据从fperiod表查询
            return getKlineDataFromPeriod(fviFid);
        }
        return getKlineDataFromBigPeriod(fviFid, key);
    }

    private String getKlineDataFromBigPeriod(int fviFid, int key) {
        List<String> list = jdbc.queryForList("select k from (\n" +
                "select ftime, CONCAT('[', UNIX_TIMESTAMP(ftime), ',0,0,', fkai, ',', fshou, ',', fgao, ',', fdi, ',', fliang, ']') k FROM kline WHERE fvi_fid = ? and fkey = ? AND fliang > 0 ORDER BY ftime desc limit 800\n" +
                ") a ORDER BY ftime", new Object[]{fviFid, key}, String.class);

        StringBuilder sb = new StringBuilder();
        sb.append("[");
        for (int i = 0; i < list.size(); i++) {
            if (i > 0) {
                sb.append(",");
            }
            sb.append(list.get(i));
        }
        sb.append("]") ;
        return sb.toString();
    }

    private String getKlineDataFromPeriod(int fviFid) {
        List<String> list = jdbc.queryForList("select k from (\n" +
                "select ftime, CONCAT('[', UNIX_TIMESTAMP(ftime), ',0,0,', fkai, ',', fshou, ',', fgao, ',', fdi, ',', fliang, ']') k FROM fperiod WHERE fvi_fid = ? AND fliang > 0 ORDER BY fid desc limit 800\n" +
                ") a ORDER BY ftime", new Object[]{fviFid}, String.class);

        StringBuilder sb = new StringBuilder();
        sb.append("[");
        for (int i = 0; i < list.size(); i++) {
            if (i > 0) {
                sb.append(",");
            }
            sb.append(list.get(i));
        }
        sb.append("]") ;
        return sb.toString();
    }

    private void saveToRedis(int fviFid, int key, String json) {
        String mkey = ("cache:kline:" + fviFid);
        String jkey = "" + key;
        try (Jedis jedis = jedisPool.getResource()) {
            jedis.hset(mkey, jkey, json);
        }
    }

    private void syncCoinKlineToRedis(int fviFid) {
        long beginTime = System.currentTimeMillis();
        timeStep.forEach((key, val) -> {
            String data = getKlineData(fviFid, val);
            saveToRedis(fviFid, val, data);
        });
        log.debug("缓存K{}线到Redis耗时: {}", fviFid, (System.currentTimeMillis() - beginTime));
    }

    /**
     * 同步K线数据到Redis
     */
    @PostConstruct
    public void run() {
        long beginTime = System.currentTimeMillis();
        log.info("缓存K线到Redis");
        try {
            cacheKlineToRedis();
        } catch (Exception e) {
            log.error("缓存K线到Redis {}", e.toString());
        }
        log.info("缓存K线到Redis耗时: {}", (System.currentTimeMillis() - beginTime));
    }

    private void cacheKlineToRedis() throws Exception {
        List<Integer> fviFids = getFviFids();
        CountDownLatch latch = new CountDownLatch(fviFids.size());
        fviFids.forEach(fviFid -> {
            executorService.submit(() -> {
                try {
                    syncCoinKlineToRedis(fviFid);
                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    latch.countDown();
                }
            });
        });
        latch.await();
    }

}

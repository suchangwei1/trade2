package com.trade.jobs.kline;

import com.alibaba.fastjson.JSON;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.Pipeline;

import javax.annotation.Resource;
import java.util.*;

public class CacheDataServiceImpl implements CacheDataService {

    private final Logger log = LoggerFactory.getLogger(getClass());

    // Redis cache Key
    public static final byte[] REDIS_CACHE_LAST = "cache:latest".getBytes();

    @Resource
    private JedisPool jedisPool;

    private HashMap<String, byte[]> latestDataCache = new HashMap<>();
    private HashMap<String, Integer> latestDataCacheCounter = new HashMap<>();

    public CacheDataServiceImpl() {
    }

    public CacheDataServiceImpl(JedisPool jedisPool) {
        this.jedisPool = jedisPool;
    }

    @Override
    public LatestDealData getLatestDealData(final int fid) {
        LatestDealData latest = null;
        try (Jedis connection = jedisPool.getResource()) {

            byte[] m = (fid + "").getBytes();
            byte[] key = REDIS_CACHE_LAST;
            byte[] bdata = connection.hget(key, m);

            if (bdata != null && bdata.length > 0) {
                latest = JSON.parseObject(bdata, LatestDealData.class);
            } else {
                latest = new LatestDealData();
                latest.setFid(fid);
            }
        } catch (Exception e) {
            log.error("getLatestDealData " + e);
        }
        return latest;
    }

    @Override
    public List<LatestDealData> getLatestDealData() {
        List<LatestDealData> list = Collections.emptyList();
        try (Jedis connection = jedisPool.getResource()) {

            byte[] key = REDIS_CACHE_LAST;
            Map<byte[], byte[]> bdataMap = connection.hgetAll(key);

            list = new ArrayList<>(bdataMap.size());
            for (Map.Entry<byte[], byte[]> entry : bdataMap.entrySet()) {
                byte[] bdata = entry.getValue();
                if (bdata != null && bdata.length > 0) {
                    list.add(JSON.parseObject(bdata, LatestDealData.class));
                }
            }
        } catch (Exception e) {
            log.error("getLatestDealData " + e);
        }
        return list;
    }

    @Override
    public void removeLatestDealData(LatestDealData latestDealData) {
        try (Jedis connection = jedisPool.getResource()) {
            connection.hdel(REDIS_CACHE_LAST, (latestDealData.getFid() + "").getBytes());
        } catch (Exception e) {
            log.error("removeLatestDealData " + e);
        }
    }

    @Override
    public void updateLatestDealData(LatestDealData data) {

        log.debug("updateLatestDealData fvid = {}, {}, {}, {}, {}, {}, {}, {}, {}, {}",
                data.getFid(),
                data.getStatus(),
                data.getFupanddown(),
                data.getFupanddownweek(),
                data.getFmarketValue(),
                data.getFentrustValue(),
                data.getVolumn(),
                data.getLowestPrize24(),
                data.getHighestPrize24(),
                data.getTotalDeal24(),
                data.getEntrustValue24());

        LatestDealData latest = getLatestDealData(data.getFid());
        latest.setFupanddown(data.getFupanddown());
        latest.setFupanddownweek(data.getFupanddownweek());
        latest.setFmarketValue(data.getFmarketValue());
        latest.setFentrustValue(data.getFentrustValue());
        latest.setVolumn(data.getVolumn());
        latest.setLowestPrize24(data.getLowestPrize24());
        latest.setHighestPrize24(data.getHighestPrize24());
        latest.setTotalDeal24(data.getTotalDeal24());
        latest.setEntrustValue24(data.getEntrustValue24());
        latest.setCoinTradeType(data.getCoinTradeType());
        latest.setGroup(data.getGroup());
        latest.setOpenTrade(data.getOpenTrade());
        latest.setHomeShow(data.isHomeShow());
        latest.setHomeOrder(data.getHomeOrder());
        latest.setTypeOrder(data.getTypeOrder());
        latest.setTotalOrder(data.getTotalOrder());
        latest.setStatus(data.getStatus());
        latest.setLastDealPrize(data.getLastDealPrize());

        String pkey = "cache:latest:" + data.getFid();
        byte[] value = JSON.toJSONBytes(latest);

        // 更新之前先对比上一次的数据，减少不必要的推送
        byte[] oldValue = latestDataCache.get(pkey);
        Integer count = latestDataCacheCounter.get(pkey);
        count = count == null ? 0 : count;

        // 最新数据缓存，如果计算后的数据跟原来的数据一致，则不推送，但是会导致一种情况就是，上一次推送没有正常到达，所以超过一定次数，即使数据重复也会推送
        if (oldValue == null || count == 5 || oldValue.equals(value)) {
            latestDataCache.put(pkey, value);
            latestDataCacheCounter.put(pkey, 0);
            updateLatestDealData(data.getFid(), pkey, value);
        } else {
            latestDataCacheCounter.put(pkey, count + 1);
        }

    }

    private void updateLatestDealData(int fid, String pkey, byte[] value) {
        try (Jedis connection = jedisPool.getResource(); Pipeline pip = connection.pipelined()) {
            byte[] m = (fid + "").getBytes();
            byte[] key = REDIS_CACHE_LAST;
            Long counter = connection.hincrBy("counter", pkey, 1);
            pip.hset(key, m, value);
            pip.publish(pkey, counter + "");
        } catch (Exception e) {
            log.error("updateLatestDealData " + e);
        }
    }

    @Override
    public void updateLatestDealDataInfo(LatestDealData latestDealData) {
        log.debug("updateLatestDealDataInfo vid = {}", latestDealData.getFid());
        try (Jedis connection = jedisPool.getResource()) {
            byte[] m = (latestDealData.getFid() + "").getBytes();
            byte[] key = REDIS_CACHE_LAST;
            byte[] data = connection.hget(key, m);
            LatestDealData latest;
            if (data != null && data.length > 0) {
                latest = JSON.parseObject(data, LatestDealData.class);
            } else {
                latest = new LatestDealData();
                latest.setFid(latestDealData.getFid());
            }
            latest.setStatus(latestDealData.getStatus());
            latest.setFisShare(latestDealData.isFisShare());
            latest.setFname(latestDealData.getFname());
            latest.setfShortName(latestDealData.getfShortName());
            latest.setFisShare(latestDealData.isFisShare());
            latest.setFname_sn(latestDealData.getFname_sn());
            latest.setFurl(latestDealData.getFurl());
            latest.setCoinTradeType(latestDealData.getCoinTradeType());
            latest.setGroup(latestDealData.getGroup());
            latest.setHomeShow(latestDealData.isHomeShow());
            connection.hset(key, m, JSON.toJSONBytes(latest));
        } catch (Exception e) {
            log.error("updateLatestDealDataInfo " + e);
        }
    }

    @Override
    public void addLatestDealData(LatestDealData latestDealData) {
        log.debug("addLatestDealData vid = {}", latestDealData.getFid());
        try (Jedis connection = jedisPool.getResource()) {
            byte[] m = (latestDealData.getFid() + "").getBytes();
            byte[] key = REDIS_CACHE_LAST;
            connection.hset(key, m, JSON.toJSONBytes(latestDealData));
        } catch (Exception e) {
            log.error("addLatestDealData " + e);
        }
    }

}

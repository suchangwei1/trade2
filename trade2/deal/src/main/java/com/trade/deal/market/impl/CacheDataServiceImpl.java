package com.trade.deal.market.impl;

import com.alibaba.fastjson.JSON;
import com.trade.deal.Enum.EntrustTypeEnum;
import com.trade.deal.core.MarketUpdater;
import com.trade.deal.market.CacheDataService;
import com.trade.deal.model.FentrustlogData;
import com.trade.deal.model.LatestDealData;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.Pipeline;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.Map;

public class CacheDataServiceImpl implements CacheDataService {

    private final Logger log = LoggerFactory.getLogger(getClass());

    // Redis cache Key
    public static final byte[] REDIS_CACHE_LAST = "cache:latest".getBytes();

    public static final String CACHE_FENTRUSTLOG = "cache:fentrustlog:";

    @Resource
    private JedisPool jedisPool;

    @Resource
 	private MarketUpdater marketUpdater;

    private HashMap<String, byte[]> latestDataCache = new HashMap<>();
    private HashMap<String, Integer> latestDataCacheCounter = new HashMap<>();

    public CacheDataServiceImpl() {
    }

    public CacheDataServiceImpl(JedisPool jedisPool) {
        this.jedisPool = jedisPool;
    }

    @Override
    public void addFentrustLogData(final FentrustlogData data) {
        marketUpdater.publish(() -> {
            log.info("addFentrustLogData fvid = {}, time = {}, price = {}, count = {}", data.getFviFid(), data.getFcreateTime(), data.getFprize(), data.getFcount());
            try (Jedis jedis = jedisPool.getResource(); Pipeline pip = jedis.pipelined()) {
                String key = CACHE_FENTRUSTLOG + data.getFviFid();
                byte[] mkey = key.getBytes();

                Long counter = jedis.hincrBy("counter", key, 1);

                pip.zadd(mkey, data.getFcreateTime().getTime(), JSON.toJSONBytes(data));
                pip.publish(key, counter + "");
            } catch (Exception e) {
                log.error("addFentrustLogData " + e);
            }
        });
    }

    @Override
    public void clearFentrustLogData(final int fviFid) {
        log.debug("clearFentrustLogData fvid = {}", fviFid);
        try (Jedis connection = jedisPool.getResource()) {
            byte[] mkey = (CACHE_FENTRUSTLOG + fviFid).getBytes();
            connection.del(mkey);
        } catch (Exception e) {
            log.error("clearFentrustLogData " + e);
        }
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
    public void updateMarking(int fid, double dealPrice, double highestBuy, double lowestSell) {
        marketUpdater.publish("real", () -> {
            log.debug("updateMarking vid = {}, dealPrice = {}, highestBuy = {}, lowestSell = {}", fid, dealPrice, highestBuy, lowestSell);
            try (Jedis connection = jedisPool.getResource(); Pipeline pip = connection.pipelined()) {
                byte[] m = (fid + "").getBytes();
                byte[] key = REDIS_CACHE_LAST;

                Map<String, String> map = new HashMap<>();
                LatestDealData latest = getLatestDealData(fid);
                if (dealPrice > 0) {
                    latest.setLastDealPrize(dealPrice);
                    if (dealPrice > latest.getHighestPrize24()) {
                        latest.setHighestPrize24(dealPrice);
                        map.put("high", String.valueOf(dealPrice));
                    } else if (dealPrice < latest.getLowestPrize24()) {
                        latest.setLowestPrize24(dealPrice);
                        map.put("low", String.valueOf(dealPrice));
                    }
                }
                latest.setHigestBuyPrize(highestBuy);
                latest.setLowestSellPrize(lowestSell);

                String pkey = "cache:latest:" + fid;
                Long counter = connection.hincrBy("counter", pkey, 1);

                pip.hset(key, m, JSON.toJSONBytes(latest));
                pip.publish(pkey, counter + "");

                // 实时价格
                map.put("buy", highestBuy + "");
                map.put("sell", lowestSell + "");
                map.put("last", latest.getLastDealPrize() + "");

                pip.hmset("cache:real:" + fid, map);

            } catch (Exception e) {
                log.error("updateMarking " + e);
            }
        });
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
        latest.setEquityType(data.getEquityType());
        latest.setOpenTrade(data.getOpenTrade());
        latest.setHomeShow(data.isHomeShow());
        latest.setHomeOrder(data.getHomeOrder());
        latest.setTypeOrder(data.getTypeOrder());
        latest.setTotalOrder(data.getTotalOrder());
        latest.setStatus(data.getStatus());
        latest.setGroup(data.getGroup());

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
            latest.setEquityType(latestDealData.getEquityType());
            latest.setHomeShow(latestDealData.isHomeShow());
            latest.setGroup(latestDealData.getGroup());
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

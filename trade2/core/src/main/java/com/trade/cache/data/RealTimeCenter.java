package com.trade.cache.data;

import com.alibaba.fastjson.JSON;
import com.trade.Enum.EntrustTypeEnum;
import com.trade.dto.FentrustlogData;
import com.trade.dto.LatestDealData;
import org.apache.commons.lang.math.NumberUtils;
import org.springframework.stereotype.Service;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPubSub;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentSkipListMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Service
public class RealTimeCenter {

    @Resource
    private JedisPool jedisPool;

    @Resource(name = "realTimeDealDataService")
    private RealTimeDealDataService realTimeDealDataService;

    // 更新行情线程
    private ExecutorService executorService = Executors.newCachedThreadPool();

    // 结构： type: vid -> price,  如： buy: 1 -> 1.2, sell: 1 -> 1.3, last: 1 -> 1.3, ...
    private Map<Type, Map<Integer, Double>> priceMap = new HashMap<>();

    // 结构： vid -> [log,log]
    private Map<Integer, Set<FentrustlogData>> entrustSuccessMap = new ConcurrentSkipListMap<>();

    // 结构：key -> entrust depth
    private Map<String, String> depthMap = new ConcurrentHashMap<>();

    // 结构：key -> entrust market depth
    private Map<Integer, String> marketMap = new ConcurrentHashMap<>();

    // 价格数据为空的时候，默认为0
    private static final Double ZERO_PRICE = 0D;

    // 行情数据为空的时候，默认为空的二维数组
    private static final String EMPTY_MARKET = "[[],[]]";

    public RealTimeCenter() {
        priceMap.put(Type.BUY, new ConcurrentHashMap<>());
        priceMap.put(Type.SELL, new ConcurrentHashMap<>());
        priceMap.put(Type.LAST, new ConcurrentHashMap<>());
        priceMap.put(Type.HIGH, new ConcurrentHashMap<>());
        priceMap.put(Type.LOW, new ConcurrentHashMap<>());
        priceMap.put(Type.VOL, new ConcurrentHashMap<>());
    }

    // 初始化，拉取最新数据；监听行情变化，更新本地缓存
    @PostConstruct
    public void init() {
        fetchFromRedis();
        subscribeRedis();
    }

    /**
     * 价格类型
     */
    public enum Type {
        BUY, SELL, LAST, HIGH, LOW, VOL;
        String value() {
            return this.name().toLowerCase();
        }
    }

    /**
     * 获取实时行情价格
     * @param type
     * @param vid
     * @return
     */
    public Double getPrice(Type type, int vid) {
        Double price = priceMap.get(type).get(vid);
        if (price == null) {
            price = ZERO_PRICE;
        }
        return price;
    }

    /**
     * 获取成交日志
     * @param vid
     * @return
     */
    public Set<FentrustlogData> getEntrustLog(int vid) {
        Set<FentrustlogData> list = entrustSuccessMap.get(vid);
        if (list == null) {
            list = Collections.emptySet();
        }
        return list;
    }

    /**
     * 获取深度挂单
     * @param key
     * @return
     */
    public String getEntrustList(String key) {
        String list = depthMap.get(key);
        if (list == null) {
            list = "[]";
        }
        return list;
    }

    /**
     * 获取行情（累积买卖单）
     * @param vid
     * @return
     */
    public String getMarketDepth(int vid) {
        String data = marketMap.get(vid);
        if (data == null) {
            data = EMPTY_MARKET;
        }
        return data;
    }

    // 首次启动先从Redis拉取一次数据
    private void fetchFromRedis() {
        List<LatestDealData> list = realTimeDealDataService.getLatestDealDataList();
        for (LatestDealData vtype : list) {
            int vid = vtype.getFid();
            for (int deep = 0; deep < 5; deep++) {
                syncDepthEntrust(String.format("%d:%d:%d", vid, EntrustTypeEnum.BUY, deep));
                syncDepthEntrust(String.format("%d:%d:%d", vid, EntrustTypeEnum.SELL, deep));
            }
            syncMarket(vid);
            syncLatestDealData(vid);
            syncEntrustSuccessMap(vid);
        }
    }

    // Redis 订阅消息处理器
    private JedisPubSub redisSub = new JedisPubSub() {
        @Override
        public void onPMessage(String pattern, String channel, String message) {
            try {
                executorService.execute(() -> {
                    if (channel.startsWith("cache:fentrustlog")) {
                        int fviFid = Integer.valueOf(channel.split(":")[2]);
                        syncEntrustSuccessMap(fviFid);
                    } else if (channel.startsWith("cache:latest")) {
                        int fviFid = Integer.valueOf(channel.split(":")[2]);
                        syncLatestDealData(fviFid);
                    }else if(channel.startsWith("kline:data")){
                        int fviFid = Integer.valueOf(channel.split(":")[2]);
                        syncLatestDealData(fviFid);
                    } else {
                        int vid = Integer.valueOf(channel.split(":")[0]);
                        syncDepthEntrust(channel);
                        syncMarket(vid);
                    }
                });
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    };

    // 订阅Redis数据变化，实时更新本地缓存
    private void subscribeRedis() {
        String[] channels = new String[]{"1*", "2*", "3*", "4*", "5*", "6*", "7*", "8*", "9*", "c*"};
        executorService.execute(() -> {
            // 如果监听Redis报异常了，则等待一会重新监听，psubscribe 方法是堵塞的，如果监听成功不会跑到下面
            while (true) {
                try (Jedis jedis = jedisPool.getResource()) {
                    jedis.psubscribe(redisSub, channels);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                try {
                    Thread.sleep(3000);
                } catch (Exception e) {

                }
            }
        });
    }

    // 同步挂单列表（不同深度）
    private void syncDepthEntrust(String key) {
        String data = getDepthEntrust(key);
        if (data != null) {
            depthMap.put(key, data);
        }
    }

    private String getDepthEntrust(String key) {
        String data;
        try (Jedis connection = jedisPool.getResource()) {
            data = connection.hget("depth", key);
        }
        return data;
    }

    // 同步最新价格
    private void syncLatestDealData(int vid) {
        // 实时价格不保存成json了，改成hash保存
        List<String> list;
        try (Jedis jedis = jedisPool.getResource()) {
            list = jedis.hmget("cache:real:" + vid, Type.BUY.value(), Type.SELL.value(), Type.LAST.value(), Type.HIGH.value(), Type.LOW.value(), Type.VOL.value());
        }

        double buy = 0D, sell = 0D, last = 0D, high = 0D, low = 0D, vol = 0D;
        if (list != null && list.size() > 0) {
            buy = NumberUtils.toDouble(list.get(0));
            sell = NumberUtils.toDouble(list.get(1));
            last = NumberUtils.toDouble(list.get(2));
            high = NumberUtils.toDouble(list.get(3));
            low = NumberUtils.toDouble(list.get(4));
            vol = NumberUtils.toDouble(list.get(5));
        }
        priceMap.get(Type.BUY).put(vid, buy);
        priceMap.get(Type.SELL).put(vid, sell);
        priceMap.get(Type.LAST).put(vid, last);
        priceMap.get(Type.HIGH).put(vid, high);
        priceMap.get(Type.LOW).put(vid, low);
        priceMap.get(Type.VOL).put(vid, vol);
    }

    // 同步最新成交日志
    private void syncEntrustSuccessMap(int id) {
        try (Jedis jedis = jedisPool.getResource()) {
            Set<FentrustlogData> set = new TreeSet<>();
            byte[] mkey = ("cache:fentrustlog:" + id).getBytes();
            Set<byte[]> bytes = jedis.zrevrange(mkey, 0, 20);
            int i = bytes.size();
            for (byte[] b : bytes) {
                FentrustlogData obj = JSON.parseObject(b, FentrustlogData.class);
                obj.setFid(i--);
                set.add(obj);
            }
            entrustSuccessMap.put(id, set);
        }
    }

    // 同步行情
    private void syncMarket(int vid) {
        try (Jedis jedis = jedisPool.getResource()) {
            String data = jedis.hget("market", String.valueOf(vid));
            if (data != null) {
                marketMap.put(vid, data);
            }
        }
    }

}

package com.trade.cache.data.impl;

import com.alibaba.fastjson.JSON;
import com.trade.Enum.VirtualCoinTypeStatusEnum;
import com.trade.cache.data.RealTimeDealDataService;
import com.trade.dto.LatestDealData;
import com.trade.util.Constants;
import org.springframework.stereotype.Service;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.*;
import java.util.concurrent.ConcurrentSkipListMap;

@Service("realTimeDealDataService")
public class RealTimeDealDataServiceImpl implements RealTimeDealDataService, Runnable {

    @Resource
    private JedisPool jedisPool;

    private List<LatestDealData> latestDealDatas;

    private Map<Integer, LatestDealData> latestDealDataMap = new ConcurrentSkipListMap<>();

    @Override
    public List<LatestDealData> getLatestDealDataList() {
        if (latestDealDatas == null) {
            return new ArrayList<>();
        }
        return latestDealDatas;
    }

    @Override
    public LatestDealData getLatestDealData(final int id) {
        LatestDealData latestDealData = latestDealDataMap.get(id);
        if (latestDealData == null) {
            latestDealData = new LatestDealData();
        }
        return latestDealData;
    }

    private Comparator<LatestDealData> latestDealDataComparator = new Comparator<LatestDealData>() {
        @Override
        public int compare(LatestDealData o1, LatestDealData o2) {
            return o1.getFid() - o2.getFid();
        }
    };

    ///////////////// 同步缓存数据 //////////////////

    @Override
    @PostConstruct
    public void run() {
        syncLatestDealData();
    }

    private void syncLatestDealData() {
        final List<LatestDealData> list = new ArrayList<>();

        Map<byte[], byte[]> map;
        try (Jedis jedis = jedisPool.getResource()) {
            map = jedis.hgetAll(Constants.REDIS_CACHE_LAST);
        }

        for (byte[] key : map.keySet()) {
            byte[] value = map.get(key);
            LatestDealData obj = JSON.parseObject(value, LatestDealData.class);
            list.add(obj);
        }

        Collections.sort(list, latestDealDataComparator);
        for (LatestDealData latestDealData : list) {
            latestDealDataMap.put(latestDealData.getFid(), latestDealData);
        }
        List<LatestDealData> validList = new ArrayList<>();
        // 只保存状态正常的
        for (LatestDealData data : list) {
            if (data.getStatus() == VirtualCoinTypeStatusEnum.Normal && data.isFisShare()) {
                validList.add(data);
            }
        }

        for (LatestDealData data : validList) {

            data.setVolumn(data.getTotalDeal24());

            double upanddown = data.getFupanddown() * 100;
            BigDecimal bd = new BigDecimal(upanddown);
            upanddown = bd.setScale(2,BigDecimal.ROUND_HALF_UP).doubleValue();
            data.setFupanddown(upanddown);

            double upanddownweek = data.getFupanddownweek() * 100;
            BigDecimal bdweek = new BigDecimal(upanddownweek);
            upanddownweek = bdweek.setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
            data.setFupanddownweek(upanddownweek);

            double entrustValue = data.getEntrustValue24();
            BigDecimal bdev = new BigDecimal(entrustValue);
            entrustValue = bdev.setScale(2,BigDecimal.ROUND_HALF_UP).doubleValue();
            data.setFentrustValue(entrustValue);
        }

        latestDealDatas = validList;
    }

}

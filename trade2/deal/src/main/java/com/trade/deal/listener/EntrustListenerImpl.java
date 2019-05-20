package com.trade.deal.listener;

import com.trade.deal.Enum.EntrustStatusEnum;
import com.trade.deal.Enum.EntrustTypeEnum;
import com.trade.deal.core.SyncTaskService;
import com.trade.deal.market.DepthCalculateQueue;
import com.trade.deal.model.FentrustData;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

import javax.annotation.Resource;
import java.util.Arrays;

public class EntrustListenerImpl implements EntrustListener {

    private final Logger log = LoggerFactory.getLogger(getClass());

    @Resource
    private DepthCalculateQueue depthCalculateQueue;

    @Resource
    private SyncTaskService syncTaskService;

    @Resource
    private JedisPool jedisPool;

    public EntrustListenerImpl() {
    }

    public EntrustListenerImpl(DepthCalculateQueue depthCalculateQueue, SyncTaskService syncTaskService, JedisPool jedisPool) {
        this.depthCalculateQueue = depthCalculateQueue;
        this.syncTaskService = syncTaskService;
        this.jedisPool = jedisPool;
    }

    @Override
    public void onCreateEntrust(FentrustData data) {
        this.depthCalculateQueue.calculateDepthEntrust(data);
        this.noticeUserUpdate(data);
        // 异步更新行情
        this.syncTaskService.execute(() -> {
            this.updateRealMarket(data);
        });
    }

    @Override
    public void onUpdateEntrust(FentrustData data) {
        data.setFstatus(EntrustStatusEnum.Cancel);
        depthCalculateQueue.calculateDepthEntrust(data);
        this.noticeUserUpdate(data);
    }

    @Override
    public void onCancelEntrust(FentrustData data) {
        this.noticeUserUpdate(data);
    }

    private void noticeUserUpdate(FentrustData data) {
        syncTaskService.execute(() -> {
            try (Jedis jedis = jedisPool.getResource()) {
                jedis.publish("user:entrust:" + data.getFviFid(), data.getFuid() + "");
            } catch (Exception e) {
                log.error("notice user entrust update error reason " + e.getLocalizedMessage());
            }
        });
    }

    private final String UPDATE_PRICE_LUA_SCRIPT = "" +
            "local val = tonumber(redis.call('hget', KEYS[1], KEYS[2]))\n" +
            "local latest = tonumber(ARGV[1])\n" +
            "if 'sell' == KEYS[2] and (not val or val == 0 or val > latest) then\n" +
            "     redis.call('hset', KEYS[1], KEYS[2], ARGV[1])\n" +
            "elseif 'buy' == KEYS[2] and (not val or val == 0 or val < latest) then\n" +
            "     redis.call('hset', KEYS[1], KEYS[2], ARGV[1])\n" +
            "end\n" +
            "return val\n";

    public void updateRealMarket(FentrustData data) {
        double value = data.getFprize();
        int fid = data.getFviFid();
        String key;
        if (data.getFentrustType() == EntrustTypeEnum.BUY) {
            key = "buy";
        } else {
            key = "sell";
        }
        try (Jedis connection = jedisPool.getResource()) {
            connection.eval(UPDATE_PRICE_LUA_SCRIPT, Arrays.asList("cache:real:" + fid, key), Arrays.asList(String.valueOf(value)));
//            connection.hset("cache:real:" + fid, key, String.valueOf(value));
        } catch (Exception e) {
            log.error("updateRealMarket ", e);
        }
    }

}

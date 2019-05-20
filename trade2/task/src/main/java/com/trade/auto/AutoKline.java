package com.trade.auto;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.trade.dto.LatestDealData;
import com.trade.mq.MessageQueueService;
import com.trade.util.Constants;
import org.springframework.beans.factory.annotation.Autowired;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.Pipeline;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * Created by Administrator on 2018/6/24 0024.
 */
public class AutoKline {
    @Resource
    private JedisPool jedisPool;

    @Autowired
    private MessageQueueService messageQueueService;



    private static Jedis jedis;

    @PostConstruct
    public void initJedis() {
        jedis= jedisPool.getResource();
    }
    /*@Scheduled(cron = "0/1 * * * * ?")*/
    public void klineData15() {
        System.out.println("####################################");
        AutoKline.sendMessge(60);
    }

    public static  void sendMessge(int time) {
        List<LatestDealData> list = getAllCoinType();
        for (LatestDealData LatestDealData : list) {
            JSONObject result = new JSONObject();
            result.put("symbol", LatestDealData.getFid());
            result.put("step", time);
            Pipeline pip = jedis.pipelined();
            pip.publish("kline:date", "000000000000000000000000000000000000000000");
            pip.publish("kline:data", result.toJSONString());
      //    messageQueueService.publish("kline:data", result.toJSONString());

        }
    }

    /**
     * 得到所有的币种
     * @return
     */
    public static List<LatestDealData> getAllCoinType() {

        final List<LatestDealData> list = new ArrayList<>();

        Map<byte[], byte[]> map=new HashMap<byte[], byte[]>();
        try  {
            map = jedis.hgetAll(Constants.REDIS_CACHE_LAST);
        }catch (Exception e){
            e.printStackTrace();
        }

        for (byte[] key : map.keySet()) {
            byte[] value = map.get(key);
            LatestDealData obj = JSON.parseObject(value, LatestDealData.class);
            list.add(obj);
        }

        return list;
    }


}

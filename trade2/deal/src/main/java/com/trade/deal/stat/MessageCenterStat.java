package com.trade.deal.stat;

import com.alibaba.fastjson.JSON;
import com.trade.deal.core.MessageCenter;
import com.trade.deal.core.TradingSystem;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by john on 16/6/12.
 */
public class MessageCenterStat implements MessageCenterStatMBean {

    @Resource
    private MessageCenter messageCenter;

    @Override
    public Map<Integer, String> getTradingSystemMap() {
        Map<Integer, TradingSystem> map = messageCenter.getSystemMap();
        Map<Integer, String> ret = new HashMap<>();
        map.forEach((k, v) -> {
            Map<String, Object> json = new HashMap<>();
            json.put("buyCount", v.getBuyCount());
            json.put("sellCount", v.getSellCount());
            json.put("count", v.getCount());
            json.put("queueCount", v.getQueueCount());
            ret.put(k, JSON.toJSONString(json));
        });
        return ret;
    }

}

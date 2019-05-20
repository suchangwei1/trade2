package com.trade.task.queue;

import com.alibaba.fastjson.JSON;
import com.trade.comm.ConstantMap;
import com.trade.model.OtcOrder;
import com.trade.model.OtcOrderLog;
import com.trade.service.front.OtcOrderLogService;
import com.trade.service.front.OtcOrderService;
import com.trade.util.DateUtils;
import com.trade.util.StringUtils;
import com.trade.util.Utils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

import java.sql.Timestamp;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class OTCQueue {
    private static final Logger log = LoggerFactory
            .getLogger(OTCQueue.class);

    @Autowired
    private JedisPool jedisPool;
    @Autowired
    private OtcOrderLogService otcOrderLogService;
    @Autowired
    private OtcOrderService otcOrderService;
    @Autowired
    private ConstantMap constantMap;

    public void work() {
        synchronized (this) {
            try (Jedis jedis = jedisPool.getResource()) {
                //多出十秒用于定时
                String nowTime = ""+ new Timestamp(new Date().getTime() - constantMap.getInt("frontOtcTime") * 60 * 1000 + 10 * 1000);
                /*待付款 - > 撤销*/
                String filter = "where status = 0 and updateTime <  '" + nowTime+"'" ;
                List<OtcOrderLog> logs = otcOrderLogService.list(0,0, filter, false);
                for (OtcOrderLog log: logs){
                    otcOrderLogService.updateCancel(log);
                    String channel = "otc:message:" + log.getOtcOrder().getFvirtualcointype().getFid() + ":" + log.getOtcOrder().getFuser().getFid() + ":" + log.getFuser().getFid();
                    jedis.publish(StringUtils.string2UTF8Bytes(channel), StringUtils.string2UTF8Bytes(JSON.toJSONString("")));
                }
                /*待放币 -> 冻结中*/
                String filter2 = "where status = 1 and updateTime <  '" + nowTime+"'" ;
                List<OtcOrderLog> logs2 = otcOrderLogService.list(0,0, filter2, false);
                for (OtcOrderLog log: logs2){
                    log.setStatus(4);//冻结
                    log.setUpdateTime(Utils.getTimestamp());
                    otcOrderLogService.updateObj(log);
                    String channel = "otc:message:" + log.getOtcOrder().getFvirtualcointype().getFid() + ":" + log.getOtcOrder().getFuser().getFid() + ":" + log.getFuser().getFid();
                    jedis.publish(StringUtils.string2UTF8Bytes(channel), StringUtils.string2UTF8Bytes(JSON.toJSONString("")));
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            String newFilter = "where status = 0 and amount - successAmount < minAmount and frozenAmount = 0";
            List<OtcOrder> otcOrders = otcOrderService.list(0,0, newFilter, false);
            for(OtcOrder otcOrder: otcOrders){
                otcOrder.setStatus(2);
                otcOrderService.updateOrderStatus(otcOrder);
            }
        }
    }

}

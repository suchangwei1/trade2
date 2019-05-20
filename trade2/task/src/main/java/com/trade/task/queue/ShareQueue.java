package com.trade.task.queue;

import com.trade.comm.ConstantMap;
import com.trade.model.*;
import com.trade.service.admin.EntrustlogService;
import com.trade.service.admin.VirtualCoinService;
import com.trade.service.front.FrontUserService;
import com.trade.service.front.MarketService;
import com.trade.service.front.SpreadLogService;
import com.trade.service.front.TradeFeesShareService;
import com.trade.util.DateUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ShareQueue {
    private static final Logger log = LoggerFactory
            .getLogger(ShareQueue.class);

    @Autowired
    private ConstantMap constantMap;
    @Autowired
    private SpreadLogService spreadLogService;
    @Autowired
    private EntrustlogService entrustlogService;
    @Autowired
    private MarketService marketService;
    @Autowired
    private VirtualCoinService virtualCoinService;

    public void work() {
        synchronized (this) {
            String[] shareStr = constantMap.getString("isTradeFeesShareOpen").split(",");
            if("1".equals(shareStr[0])){
                List<Market> markets = marketService.findAll();
                Map<Integer, Market> marketMap = new HashMap();
                for(Market market: markets){
                    marketMap.put(market.getId(), market);
                }
                Map<Integer, Fvirtualcointype> coinMap = new HashMap();
                List<Fvirtualcointype> coins = virtualCoinService.findAll();
                for(Fvirtualcointype fvirtualcointype : coins) {
                    coinMap.put(fvirtualcointype.getFid(), fvirtualcointype);
                }
                List<Map<String, Object>> list = entrustlogService.findUnsharedLogs(shareStr[1]);
                for (Map<String, Object> map: list) {
                    int uid = Integer.valueOf(map.get("uid").toString());
                    Map spreadMap = new HashMap();
                    spreadMap.put("child.fid", uid);
                    List<SpreadLog> spreadLogs = spreadLogService.findByProperties(spreadMap, null, null);
                    if(spreadLogs != null && spreadLogs.size() > 1){
                        log.error("error spread, user id is: ", uid);
                        continue;
                    }
                    Fentrustlog fentrustlog = entrustlogService.findById(Integer.valueOf(map.get("id").toString()));
                    if(spreadLogs == null || spreadLogs.size() == 0){
                        log.debug("no parent and skip");
                        fentrustlog.setIsShared(3);
                        entrustlogService.update(fentrustlog);
                        continue;
                    }
                    SpreadLog spreadLog = spreadLogs.get(0);
                    if(new Date().getTime() - spreadLog.getCreateTime().getTime() > Long.valueOf(shareStr[3]) * 60 * 60 * 24 * 1000){ //注册时间满6个月不分润
                        log.debug("overtime and skip");
                        fentrustlog.setIsShared(2);
                        entrustlogService.update(fentrustlog);
                        continue;
                    }
                    double amount = Double.valueOf(shareStr[2]) * Double.valueOf(map.get("fees").toString());
                    entrustlogService.updateShareFees(fentrustlog, map, spreadLog, marketMap, amount, coinMap);
                }
            }
        }
    }
}

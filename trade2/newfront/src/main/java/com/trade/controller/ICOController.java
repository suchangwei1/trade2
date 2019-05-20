package com.trade.controller;

import com.trade.dto.FluentHashMap;
import com.trade.model.*;
import com.trade.mq.MessageQueueService;
import com.trade.mq.QueueConstants;
import com.trade.service.admin.VirtualCoinService;
import com.trade.service.admin.VirtualWalletService;
import com.trade.service.front.FrontUserService;
import com.trade.service.front.ICORecordService;
import com.trade.service.front.ICOService;
import com.trade.service.front.ICOSwapRateService;
import com.trade.util.Utils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.*;

@RestController
@RequestMapping("/api")
public class ICOController extends BaseController{
    @Autowired
    private ICOService icoService;
    @Autowired
    private ICORecordService icoRecordService;
    @Autowired
    private ICOSwapRateService icoSwapRateService;
    @Autowired
    private FrontUserService frontUserService;
    @Autowired
    private VirtualWalletService virtualWalletService;
    @Autowired
    private VirtualCoinService virtualCoinService;
    @Autowired
    private MessageQueueService messageQueueService;

    @RequestMapping("/v1/ico")
    public Object index(@RequestParam(required = false, defaultValue = "1") int page, int pageSize) {
        List<ICO> list = this.icoService.find((page - 1) * pageSize, pageSize);
        return forSuccessResult(list,  this.icoService.count());
    }

    @RequestMapping("/v1/ico/detail")
    public Object detail(@RequestParam int id) {
        ICO ico = this.icoService.findById(id);
        if(Objects.isNull(ico) || ico.isDelete()) {
            return forFailureResult(-2);
        }

        Fuser fuser = getFuser();
        Map map = new HashMap();
        List<ICOSwapRate> supportCoins = this.icoSwapRateService.findSupportCoin(ico.getId());
        // 资金余额
        if(Objects.nonNull(fuser)) {
            Map walletMap = new HashMap();
            for(ICOSwapRate supportCoin : supportCoins) {
                Fvirtualwallet fvirtualwallet = virtualWalletService.findByUser(fuser.getFid(), supportCoin.getCoinType());
                walletMap.put((long)supportCoin.getCoinType(), fvirtualwallet.getFtotal());
            }
            map.put("walletMap", walletMap);
            map.put("haveAmount", icoRecordService.sumAmountByUser(fuser.getFid(), id));
        }

        Map rateMap = new HashMap();
        List<ICOSwapRate> curRateList = this.icoSwapRateService.findCurrent(ico.getId());
        for(ICOSwapRate swapRate : curRateList) {
            rateMap.put((long)swapRate.getCoinType(), swapRate);
        }

        Map coinMap = new HashMap();
        List<Fvirtualcointype> coins = virtualCoinService.findAll();
        for(Fvirtualcointype fvirtualcointype : coins) {
            Fvirtualcointype v = new Fvirtualcointype();
            v.setfShortName(fvirtualcointype.getfShortName());
            coinMap.put((long)fvirtualcointype.getFid(), v);
        }
        Fvirtualcointype cnyType = new Fvirtualcointype();
        cnyType.setFid(0);
        cnyType.setfShortName("CNY");
        coinMap.put(0L, cnyType);

        map.put("ico", ico);
        map.put("nowTime", Utils.getTimestamp());
        map.put("coinMap", coinMap);
        map.put("rateMap", rateMap);
        map.put("supportCoins", supportCoins);

        return forSuccessResult(map);
    }

    @RequestMapping("/v1/account/icoLogs")
    public Object record(@RequestParam(required = false, defaultValue = "1") int page, int pageSize) {
        Fuser fuser = getFuser();
        Map map = new HashMap();
        List<Map> list = icoRecordService.list(fuser.getFid(), (page - 1) * pageSize, pageSize);
        map.put("logs", list);
        map.put("coinMap",virtualCoinService.findCoinMap());
        return forSuccessResult(map,  this.icoRecordService.count(fuser.getFid()));
    }

    @RequestMapping("/v1/account/icoSupport")
    public Object support(@RequestParam int id, @RequestParam(name = "amount") double amount, @RequestParam int coinType, @RequestParam(name = "password") String tradePassword) {
        Fuser fuser = getFuser();
        ICO ico = this.icoService.findById(id);
        if(Objects.isNull(ico) || ico.isDelete()) {
            return new FluentHashMap().fluentPut("code", 101);
        }
        if(amount <= 0) {
            return new FluentHashMap().fluentPut("code", 102);
        }
        Date now = new Date();
        if(now.before(ico.getStartTime()) || now.after(ico.getEndTime())) {
            return new FluentHashMap().fluentPut("code", 103);
        }

        if(ico.getRightAmount() + ico.getSupplyAmount() >= ico.getAmount()) {
            // 已满额
            return new FluentHashMap().fluentPut("code", 105);
        }
        if(!Utils.MD5(tradePassword).equals(fuser.getFtradePassword())) {
            return new FluentHashMap().fluentPut("code", 107);
        }
        if(amount + ico.getRightAmount() + ico.getSupplyAmount() > ico.getAmount()) {
            // 超出额度
            return new FluentHashMap().fluentPut("code", 109).fluentPut("leftAmount", ico.getAmount() - ico.getRightAmount() - ico.getSupplyAmount());
        }
        if(ico.getMinBuyAmount() > 0 && amount < ico.getMinBuyAmount()) {
            // 最小购买数量限制
            return new FluentHashMap().fluentPut("code", 111).fluentPut("amount", ico.getMinBuyAmount());
        }
        if(ico.getLimitAmount() > 0) {
            // 限购
            double haveAmount = icoRecordService.sumAmountByUser(fuser.getFid(), id);
            if(haveAmount + amount > ico.getLimitAmount()) {
                // leftAmount可能为负数 <=0
                return new FluentHashMap().fluentPut("code", 113).fluentPut("leftAmount", ico.getLimitAmount() - haveAmount);
            }
        }

        ICOSwapRate icoSwapRate = icoSwapRateService.findCurrent(id, coinType);
        Date nowTime = Utils.getTimestamp();
        if(icoSwapRate.getStartTime().after(nowTime) || icoSwapRate.getEndTime().before(nowTime)){
            // 不在兑换时间内
            return new FluentHashMap().fluentPut("code", 115);
        }

        // 消费数量
        double swapAmount = Utils.getDouble(amount / icoSwapRate.getAmount(), 4);

        Fvirtualwallet fvirtualwallet = virtualWalletService.findByUser(fuser.getFid(), coinType);
        if(swapAmount > fvirtualwallet.getFtotal()) {
            // 余额不足
            return new FluentHashMap().fluentPut("code", 117);
        }

        int res = icoService.updateBuyICO(ico, icoSwapRate, amount, fuser);
        // 队列通知
        messageQueueService.publish(QueueConstants.ICO_BUY_QUEUE, res);
        return new FluentHashMap().fluentPut("code", 200);
    }

}

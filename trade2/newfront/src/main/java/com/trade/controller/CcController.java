package com.trade.controller;

import com.trade.Enum.CcLogStatusEnum;
import com.trade.Enum.CcLogTypeEnum;
import com.trade.comm.ConstantMap;
import com.trade.dto.ResultBean;
import com.trade.model.CcLog;
import com.trade.model.CcUser;
import com.trade.model.Fuser;
import com.trade.model.Fvirtualcointype;
import com.trade.service.admin.CcUserService;
import com.trade.service.front.CcLogService;
import com.trade.service.front.FrontUserService;
import com.trade.service.front.FrontVirtualCoinService;
import com.trade.util.Utils;
import org.apache.jasper.tagplugins.jstl.core.ForEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.*;

@RestController
@RequestMapping("/api")
public class CcController extends BaseController {
    @Autowired
    private FrontUserService frontUserService;
    @Autowired
    private CcLogService ccLogService;
    @Autowired
    private CcUserService ccUserService;
    @Autowired
    private ConstantMap constantMap;
    @Autowired
    private FrontVirtualCoinService frontVirtualCoinService;

    /**
     * 更新我的银行卡
     * @param request
     * @param account
     * @param code
     * @return
     */
    @RequestMapping("/v1/account/saveBank")
    public Object saveBank(HttpServletRequest request, @RequestParam() String account, @RequestParam() String code){
        Fuser fuser = getFuser();
        if(fuser.getFrealName() == null){
            forFailureResult(-2);
        }
        ResultBean res = codeLimit(fuser.getFloginName(), code);
        if (res.getCode() != SUCCESS) {
            return res;
        }
        fuser = frontUserService.findById(fuser.getFid());
        fuser.setBankAccount(account);
        frontUserService.updateFUser(fuser, request.getSession());
        return forSuccessResult();
    }

    /**
     * 查询我的cc交易记录
     * @return
     */
    @RequestMapping("/v1/account/myCcLog")
    public Object myLog(){
        List<CcLog> ccLogList = ccLogService.listMyLog(getFuser().getFid());
        lazyInitLog(ccLogList);
        return forSuccessResult(ccLogList);
    }

    private void lazyInitLog(List<CcLog> logs){
        for(CcLog c: logs){
            Fvirtualcointype coin = new Fvirtualcointype();
            coin.setfShortName(c.getCoin().getfShortName());
            c.setCoin(coin);
            c.setBuyyer(null);
            CcUser ccUser = new CcUser();
            ccUser.setContactWay(c.getSeller().getContactWay().substring(0,3) + "****" + c.getSeller().getContactWay().substring(7));
            c.setSeller(ccUser);
        }
    }

    /**
     * 查询最新cc交易记录
     * @return
     */
    @RequestMapping("/v1/ccLog")
    public Object logs( @RequestParam int size){
        List<CcLog> ccBuyLogList = ccLogService.list(0, size, "where type = " + CcLogTypeEnum.BUY.getIndex() + "and status = 1 order by id desc", true);
        List<CcLog> ccSellLogList = ccLogService.list(0, size, "where type = " +  CcLogTypeEnum.SELL.getIndex() + "and status = 1 order by id desc", true);
        lazyInitLog(ccBuyLogList);
        lazyInitLog(ccSellLogList);
        Map map = new HashMap<>();
        map.put("buy", ccBuyLogList);
        map.put("sell", ccSellLogList);
        return forSuccessResult(map);
    }

    /**
     * C2C交易
     * @return
     */
    @RequestMapping("/v1/account/ccTrade")
    public Object trade(@RequestParam() int type,
                        @RequestParam() double amount,
                        @RequestParam() int cid){
        if(amount < 100 || amount > 1000000){
            return forFailureResult(-1); //数量不能小于0
        }
        if(getFuser().getBankAccount() == null || getFuser().getBankAccount().length() < 10){
            return forFailureResult(-3);//未设置银行卡
        }
        Random random=new Random();
        List<CcUser> ccUsers = ccUserService.list(0,0,"", false);
        if(ccUsers == null || ccUsers.size() == 0){
            return forFailureResult(-2);//商户不存在
        }
        CcUser ccUser = ccUsers.get(random.nextInt(ccUsers.size()));
        if(cid != constantMap.getInt("frontCcCoinId")){
            return forFailureResult(-5);//币种不合法
        }
        Fvirtualcointype coin = frontVirtualCoinService.findFvirtualCoinById(cid);
        if(coin == null){
            return forFailureResult(-4);//币种不存在
        }
        CcLog ccLog = new CcLog(getFuser(),
                ccUser,
                amount,
                constantMap.getDouble("frontCcPrice"+type),
                CcLogStatusEnum.UN_CHECK,
                CcLogTypeEnum.get(type),
                Utils.getTimestamp(),
                Utils.getTimestamp(),
                coin);
        try{
            ccLogService.saveLog(ccLog);
            Fvirtualcointype fvirtualcointype = new Fvirtualcointype();
            fvirtualcointype.setfShortName(ccLog.getCoin().getfShortName());
            ccLog.setCoin(fvirtualcointype);
            ccLog.setBuyyer(null);
            return forSuccessResult(ccLog);
        }catch (Exception e){
            e.printStackTrace();
            return forFailureResult(-6);//余额不足
        }
    }

    /**
     * C2C交易详情
     * @return
     */
    @RequestMapping("/v1/account/ccDetail")
    public Object ccDetail(@RequestParam() int id){
        CcLog ccLog = ccLogService.findById(id);
        ccLog.setCoin(null);
        ccLog.setBuyyer(null);
        ccLog.getSeller().setId(0);
        ccLog.getSeller().setContactWay(null);
        return forSuccessResult(ccLog);
    }
}


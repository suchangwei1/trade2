package com.trade.controller;

import com.trade.comm.ConstantMap;
import com.trade.model.*;
import com.trade.service.admin.CcUserService;
import com.trade.service.front.*;
import com.trade.util.FormatUtils;
import com.trade.util.MathUtils;
import com.trade.util.StringUtils;
import com.trade.util.Utils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;

@RestController
@RequestMapping("/api")
public class OtcController extends BaseController {
    @Autowired
    private OtcAccountService otcAccountService;
    @Autowired
    private FrontVirtualCoinService frontVirtualCoinService;
    @Autowired
    private OtcOrderService otcOrderService;
    @Autowired
    private OtcOrderLogService otcOrderLogService;
    @Autowired
    private ConstantMap constantMap;
    @Autowired
    private FrontUserService userService;
    @Autowired
    CcUserService ccUserService;
    /**
     * 查询我的OTC设置
     *
     * @return
     */
    @RequestMapping("/v1/account/getOtcInfo")
    public Object getOtcInfo() {
        Fuser fuser = getFuser();
        OtcAccount account = otcAccountService.findByUserId(fuser.getFid());
        return forSuccessResult(account);
    }

    /**
     * 添加otc设置
     *
     * @return
     */
    @RequestMapping("/v1/account/saveOtcInfo")
    public Object saveOtcInfo(HttpServletRequest request,
                              HttpSession session) {
        Fuser fuser = getFuser();
        OtcAccount account = otcAccountService.findByUserId(fuser.getFid());
        String bankName = request.getParameter("bankName");
        String bankBranch = request.getParameter("bankBranch");
        String bankAccount = request.getParameter("bankAccount");
        String aliUrl = request.getParameter("aliUrl");
        String aliAccount = request.getParameter("aliAccount");
        String wechatUrl = request.getParameter("wechatUrl");
        String wechatAccount = request.getParameter("wechatAccount");
        String safeWord = request.getParameter("safeWord");
        if (fuser.getFtradePassword() == null) {
            return forFailureResult(2);
        }
        if (!fuser.getFtradePassword().equals(Utils.MD5(safeWord))) {
            return forFailureResult(3);
        }
        if (account == null) {
            account = new OtcAccount();
        }
        if (bankName != null) {
            account.setBankName(bankName);
        }
        if (bankBranch != null) {
            account.setBankBranch(bankBranch);
        }
        if (bankAccount != null) {
            account.setBankAccount(bankAccount);
        }
        if (aliUrl != null) {
            account.setAliUrl(aliUrl);
        }
        if (aliAccount != null) {
            account.setAliAccount(aliAccount);
        }
        if (wechatUrl != null) {
            account.setWechatUrl(wechatUrl);
        }
        if (wechatAccount != null) {
            account.setWechatAccount(wechatAccount);
        }
        account.setFuser(fuser);
        otcAccountService.saveAccount(account, fuser, session);
        return forSuccessResult(account);
    }

    /**
     * 获取otc币种列表
     */
    @RequestMapping("/v1/getOtcList")
    public Object getOtcList() {
        Fuser fuser=getFuser();
        List<Fvirtualcointype> coins = frontVirtualCoinService.findOtcCoin();
        int ispass=0;
        if(fuser!=null){

            if(fuser.isCanOtc()){
                ispass=1;
            }

        }

        return forSuccessResult(coins, ispass);
    }

    /**
     * 添加otc挂单
     */
    @RequestMapping("/v1/account/saveOtcOrder")
    public Object saveOtcOrder(@RequestParam double price,
                               @RequestParam double amount,
                               @RequestParam double minAmount,
                               @RequestParam double maxAmount,
                               @RequestParam String payWay,
                               @RequestParam int cid,
                               @RequestParam String safeWord,
                               @RequestParam int type) {
        amount = Utils.getDouble(amount, 4);
        price = Utils.getDouble(price, 6);
        Fuser fuser = getFuser();
        if (fuser.getfIdentityStatus() != 2) {
            return forFailureResult(-100);
        }
        if (constantMap.getInt("frontOtcOrderLimit") == 1) {
            if (!fuser.isCanOtc()) {
                fuser = userService.findById(fuser.getFid());
                if (!fuser.isCanOtc()) {
                    return forFailureResult(-2);
                }
            }
        }
        OtcAccount otcAccount = otcAccountService.findByUserId(fuser.getFid());
        String[] arr = payWay.split(",");
        if (arr.length == 0) {
            return forSuccessResult(11);
        }
        for (int i = 0; i < arr.length; i++) {
            switch (arr[i]) {
                case "0":
                    if (otcAccount == null || otcAccount.getBankAccount() == null) {
                        return forFailureResult(12);
                    }
                    break;
                case "1":
                    if (otcAccount == null || otcAccount.getAliAccount() == null) {
                        return forFailureResult(13);
                    }
                    break;
                case "2":
                    if (otcAccount == null || otcAccount.getWechatAccount() == null) {
                        return forFailureResult(14);
                    }
                    break;
                default:
                    return forFailureResult(-1);
            }
        }
        Fvirtualcointype fvirtualcointype = frontVirtualCoinService.findFvirtualCoinById(cid);
        if (constantMap.getInt("frontOtcPriceLimit") == 1) {
            if (type == 1) {
                price = fvirtualcointype.getOtcSellPrice();
            } else {
                price = fvirtualcointype.getOtcBuyPrice();
            }
        }
        if (minAmount > maxAmount) {
            return forFailureResult(5);
        }
        if (fuser.getFtradePassword() == null) {
            return forFailureResult(2);
        }
        if (!fuser.getFtradePassword().equals(Utils.MD5(safeWord))) {
            return forFailureResult(3);
        }
        if (price <= 0 || amount <= 0 || minAmount <= 0 || maxAmount <= 0) {
            return forFailureResult(5);
        }
        if (fvirtualcointype == null) {
            return forFailureResult(6);
        }
        List<OtcOrder> orders = otcOrderService.findByUserIdAndCoin(fuser.getFid(), fvirtualcointype.getFid());
        if (orders != null && orders.size() > 1) {
            return forFailureResult(7);
        }

        OtcOrder otcOrder = new OtcOrder(type, price, amount, minAmount, maxAmount, payWay, fuser, fvirtualcointype);
        try {
            otcOrderService.saveOrder(otcOrder);
            return forSuccessResult();
        } catch (Exception e) {
            e.printStackTrace();
            return forFailureResult(4);
        }
    }

    /**
     * 查询otc挂单
     */
    @RequestMapping("/v1/listOtcOrders")
    public Object listOtcOrders(@RequestParam int cid, @RequestParam(required = false) Integer type) {
        if (type == null) {
            List<OtcOrder> buyOrders = otcOrderService.list(0, 0, "where type = 0 and status = 0 and amount > successAmount + frozenAmount and fvirtualcointype.fid = " + cid + " order by price desc", false);
            List<OtcOrder> sellOrders = otcOrderService.list(0, 0, "where type = 1 and status = 0 and amount > successAmount + frozenAmount and fvirtualcointype.fid = " + cid + " order by price desc", false);
            sellOrders.addAll(buyOrders);
            return forSuccessResult(transferOrder(sellOrders));
        } else if (type == 0) {
            List<OtcOrder> buyOrders = otcOrderService.list(0, 0, "where type = 0 and status = 0 and amount > successAmount + frozenAmount and fvirtualcointype.fid = " + cid + " order by price desc", false);
            return forSuccessResult(transferOrder(buyOrders));
        } else {
            List<OtcOrder> sellOrders = otcOrderService.list(0, 0, "where type = 1 and status = 0 and amount > successAmount + frozenAmount and fvirtualcointype.fid = " + cid + " order by price asc", false);
            return forSuccessResult(transferOrder(sellOrders));
        }

    }

    private List<OtcOrder> transferOrder(List<OtcOrder> orders) {
        for (OtcOrder otcOrder : orders) {
            Fuser user = new Fuser();
            user.setFrealName(otcOrder.getFuser().getFrealName().substring(0, 1) + "**");
            user.setOtcTimes(otcOrder.getFuser().getOtcTimes());
            otcOrder.setFuser(user);
        }
        return orders;
    }

    /**
     * 查询我的otc挂单
     */
    @RequestMapping("/v1/listMyOtcOrders")
    public Object listMyOtcOrders(@RequestParam(required = false) Integer cid,
                                  @RequestParam(required = false) Integer pageSize,
                                  @RequestParam(required = false) Integer page,
                                  @RequestParam(required = false) Integer status,
                                  @RequestParam(required = false) Integer type,
                                  @RequestParam(required = false) String startDate,
                                  @RequestParam(required = false) String endDate
    ) {
        Fuser fuser = getFuser();

        if (fuser == null) {
            return forSuccessResult(new ArrayList<>(), 0);
        }
        DateFormat format1 = new SimpleDateFormat("yyyy-MM-dd");
        Date  stdate=null;
        Date  etdate=null;
        try{
              stdate=format1.parse(startDate);
              etdate=format1.parse(endDate);

        }catch (Exception e){

        }

        String filter = "";
        if (cid != null&& cid!=-1) {
            filter = "where  fvirtualcointype.fid = " + cid;
        } else {
            filter = "where 1=1";
        }
        filter += " and  fuser.fid = " + fuser.getFid();
        if (!StringUtils.isEmpty(status)&&status!=-1) {
            if (status == 3) {
                filter += " and (status = 1 or status = 2) ";
            } else {
                if (status != -1) {
                    filter += " and status = " + status + " ";
                }
            }
        }

        if (type!=null && type != -1) {
            filter += " and type = " + type + " ";
        }

        if (!StringUtils.isEmpty(stdate)) {

            filter += " and DATE_FORMAT(createTime, '%Y-%m-%d') >= '" + startDate + "'\n";

        }

        if (!StringUtils.isEmpty(etdate)) {
            filter += " and DATE_FORMAT(createTime, '%Y-%m-%d') <= '" + endDate + "'\n";
        }
        filter += "  order by id desc";
        List<OtcOrder> orders = otcOrderService.list((page - 1) * pageSize, pageSize, filter, true);
        int count = otcOrderService.count(filter);
        for (OtcOrder otcOrder : orders) {
            otcOrder.setFuser(null);
            otcOrder.setShortName(otcOrder.getFvirtualcointype().getfShortName());
            Double Amout=new Double(otcOrder.getAmount());
            otcOrder.setAmount(Double.valueOf(FormatUtils.formatBalance(Amout) ));

        }
        return forSuccessResult(orders, count);
    }

    /**
     * 撤销我的otc挂单
     */
    @RequestMapping("/v1/account/cancelMyOtcOrder")
    public Object cancelMyOtcOrder(@RequestParam int id) {
        Fuser fuser = getFuser();
        OtcOrder otcOrder = otcOrderService.findById(id);
        if (otcOrder == null || (otcOrder.getStatus() != 0 && otcOrder.getStatus() != 1)) {
            return forFailureResult(2); //已撤销或不存在
        }
        if (otcOrder.getFuser().getFid() != fuser.getFid()) {
            return forFailureResult(4); //不是自己的单子
        }
        if (otcOrder.getFrozenAmount() > 0) {
            return forFailureResult(3); //有未完成的交易
        }
        otcOrder.setStatus(2);
        try {
            otcOrderService.updateOrderStatus(otcOrder);
            return forSuccessResult();
        } catch (Exception e) {
            e.printStackTrace();
            return forFailureResult(5);
        }
    }

    /**
     * 快速OTC
     */
    @RequestMapping("/v1/account/dealOtc")
    public Object dealOtc(@RequestParam int id,
                          @RequestParam String safeWord,
                          @RequestParam(required = false) String payWay,
                          @RequestParam double amount) {
        amount = Utils.getDouble(amount, 4);
        Fuser fuser = getFuser();
        if (amount <= 0) {
            return forFailureResult(7);
        }
        if (fuser.getfIdentityStatus() != 2) {
            return forFailureResult(-100);
        }
        if (fuser.getFtradePassword() == null) {
            return forFailureResult(2);
        }
        if (!fuser.getFtradePassword().equals(Utils.MD5(safeWord))) {
            return forFailureResult(3);
        }
        OtcOrder otcOrder = otcOrderService.findById(id);
        if (otcOrder == null || otcOrder.getStatus() == 2) {
            return forFailureResult(4);
        }
        if (otcOrder.getFuser().getFid() == fuser.getFid()) {
            return forFailureResult(8);
        }
        String mergePayWay;
        OtcAccount account = otcAccountService.findByUserId(fuser.getFid());
        if (otcOrder.getType() == 0) {
            String[] arr = payWay.split(",");
            if (arr.length == 0) {
                return forFailureResult(10);
            }
            for (int i = 0; i < arr.length; i++) {
                switch (arr[i]) {
                    case "0":
                        if (account == null || account.getBankAccount() == null) {
                            return forFailureResult(12);
                        }
                        break;
                    case "1":
                        if (account == null || account.getAliAccount() == null) {
                            return forFailureResult(13);
                        }
                        break;
                    case "2":
                        if (account == null || account.getWechatAccount() == null) {
                            return forFailureResult(14);
                        }
                        break;
                    default:
                        return forFailureResult(-1);
                }
                if (otcOrder.getPayWay().indexOf(arr[i]) == -1) {
                    return forFailureResult(-1);
                }
            }
            mergePayWay = payWay;
        } else {
            mergePayWay = otcOrder.getPayWay();
        }

        if (otcOrder.getAmount() < MathUtils.add(MathUtils.add(otcOrder.getSuccessAmount(), amount), otcOrder.getFrozenAmount())) {
            return forFailureResult(5, MathUtils.subtract(otcOrder.getAmount(), MathUtils.add(otcOrder.getSuccessAmount(), otcOrder.getFrozenAmount())));
        }
        if (amount < otcOrder.getMinAmount() || amount > otcOrder.getMaxAmount()) {
            return forFailureResult(6);
        }
        OtcOrderLog log = new OtcOrderLog(otcOrder, amount, fuser, mergePayWay);
        try {
            otcOrderLogService.saveOrderLog(otcOrder, log);
            otcOrderLogService.updateSendCode(log.getOtcOrder().getFuser(), "" + log.getId());
            return forSuccessResult();
        } catch (Exception e) {
            e.printStackTrace();
            return forFailureResult(7);
        }
    }

    /**
     * 快速OTC列表
     */
    @RequestMapping("/v1/myDealOtcList")
    public Object myDealOtcList(@RequestParam(required = false ,defaultValue = "-1") Integer cid,
                                @RequestParam Integer pageSize,
                                @RequestParam Integer page,
                                @RequestParam(required = false, defaultValue = "-1") Integer status,
                                @RequestParam(required = false, defaultValue = "-1") Integer type,
                                @RequestParam(required = false) String startDate,
                                @RequestParam(required = false) String endDate
    ) {
        Fuser fuser = getFuser();
        if (fuser == null) {
            return forSuccessResult(new ArrayList<>(), 0);
        }

        DateFormat format1 = new SimpleDateFormat("yyyy-MM-dd");
        Date  stdate=null;
        Date  etdate=null;
        try{
            stdate=format1.parse(startDate);
            etdate=format1.parse(endDate);

        }catch (Exception e){

        }
        String filter = "";
        if (cid != null && cid!=-1) {
            filter = "where otcOrder.fvirtualcointype.fid = " + cid;
        } else {
            filter = "where 1=1";
        }

        if (type == 0) {
            filter += " and ((otcOrder.fuser.fid = " + fuser.getFid() + " and otcOrder.type = " + type + ") or (fuser.fid = " + fuser.getFid() + " and otcOrder.type = " + (1 - type) + "))";
        } else if (type == 1) {
            filter += " and ((otcOrder.fuser.fid = " + fuser.getFid() + " and otcOrder.type = " + type + ") or (fuser.fid = " + fuser.getFid() + " and otcOrder.type = " + (1 - type) + "))";
        } else {
            filter += " and (otcOrder.fuser.fid = " + fuser.getFid() + " or fuser.fid = " + fuser.getFid() + ")";
        }
        ////状态  0 待付款 1 待放币 2 已完成 3 已撤销 4冻结中 5 申诉中
        if (status != null&& status!=-1) {
            if (status == 0) {
                filter += " and status not in(2, 3)"; //正在处理
            } else if (status == 1) {
                filter += " and status = 2"; //已完成
            } else if (status == 2) {
                filter += " and status = 3"; //已撤销
            } else if (status == 3) {//历史单
                filter += " and status in(2, 3)";
            }else if(status == 4){
                filter += " and status =0";//0 待付款
            }else if(status == 5){
                filter += " and status =1";//1 待放币
            }else if(status == 6){
                filter += " and status =4";//冻结中
            }else if(status == 7){
                filter += " and status =5";//申述中
            }
        }

        if (!StringUtils.isEmpty(stdate)) {

            filter += " and DATE_FORMAT(createTime, '%Y-%m-%d') >= '" + startDate+ "'\n " ;

        }
        if (!StringUtils.isEmpty(etdate)) {
            filter += "and DATE_FORMAT(createTime, '%Y-%m-%d') <= '" + endDate+"'\n" ;
        }
        filter += "   order by id desc";
        List<OtcOrderLog> logs = otcOrderLogService.list((page - 1) * pageSize, pageSize, filter, true);
        for (OtcOrderLog log : logs) {
            log.getFuser().setFtradePassword(null);
            log.getFuser().setFloginPassword(null);
            log.getOtcOrder().getFuser().setFtradePassword(null);
            log.getOtcOrder().getFuser().setFloginPassword(null);
            OtcAccount otcAccount = otcAccountService.findByUserId(log.getFuser().getFid());
            log.getFuser().setAccount(otcAccount);
            OtcAccount otcAccount1 = otcAccountService.findByUserId(log.getOtcOrder().getFuser().getFid());
            log.getOtcOrder().getFuser().setAccount(otcAccount1);
            log.setShortName(log.getOtcOrder().getFvirtualcointype().getfShortName());
            Double Amout=new Double(log.getAmount());
            log.setAmount(Double.valueOf(FormatUtils.formatBalance(Amout) ));

        }
        return forSuccessResult(logs, otcOrderLogService.count(filter));
    }

    /**
     * s
     * OTC付款
     */
    @RequestMapping("/v1/account/otcPay")
    public Object otcPay(@RequestParam int id) {
        Fuser fuser = getFuser();
        OtcOrderLog otcOrderLog = otcOrderLogService.findById(id);
        if (otcOrderLog == null || otcOrderLog.getStatus() != 0) {
            return forFailureResult(2);
        }
        if (otcOrderLog.getOtcOrder().getType() == 1 && otcOrderLog.getFuser().getFid() != fuser.getFid()) {
            return forFailureResult(2);
        }
        if (otcOrderLog.getOtcOrder().getType() == 0 && otcOrderLog.getOtcOrder().getFuser().getFid() != fuser.getFid()) {
            return forFailureResult(2);
        }
        otcOrderLog.setUpdateTime(Utils.getTimestamp());
        otcOrderLog.setStatus(1);//待放币
        otcOrderLogService.updatePay(otcOrderLog);
        return forSuccessResult();
    }

    /**
     * OTC撤销
     */
    @RequestMapping("/v1/account/cancelOtcPay")
    public Object cancelOtcPay(@RequestParam int id) {
        Fuser fuser = getFuser();
        OtcOrderLog otcOrderLog = otcOrderLogService.findById(id);
        if (otcOrderLog == null || otcOrderLog.getStatus() != 0) {
            return forFailureResult(2);
        }
        if (otcOrderLog.getOtcOrder().getType() == 1 && otcOrderLog.getFuser().getFid() != fuser.getFid()) {
            return forFailureResult(2);
        }
        if (otcOrderLog.getOtcOrder().getType() == 0 && otcOrderLog.getOtcOrder().getFuser().getFid() != fuser.getFid()) {
            return forFailureResult(2);
        }
        try {
            otcOrderLogService.updateCancel(otcOrderLog);
            return forSuccessResult();
        } catch (Exception e) {
            e.printStackTrace();
            return forFailureResult(3);
        }
    }

    /**
     * OTC放币
     */
    @RequestMapping("/v1/account/otcSend")
    public Object otcSend(@RequestParam int id) {
        Fuser fuser = getFuser();
        OtcOrderLog otcOrderLog = otcOrderLogService.findById(id);
        if (otcOrderLog == null || (otcOrderLog.getStatus() != 0 && otcOrderLog.getStatus() != 1 && otcOrderLog.getStatus() != 4)) {
            return forFailureResult(2);
        }
        if (otcOrderLog.getOtcOrder().getType() == 1 && otcOrderLog.getOtcOrder().getFuser().getFid() != fuser.getFid()) {
            return forFailureResult(2);
        }
        if (otcOrderLog.getOtcOrder().getType() == 0 && otcOrderLog.getFuser().getFid() != fuser.getFid()) {
            return forFailureResult(2);
        }
        try {
            otcOrderLogService.updateSuccess(otcOrderLog);
            return forSuccessResult();
        } catch (Exception e) {
            e.printStackTrace();
            return forFailureResult(3);//用户资金异常
        }
    }

    /**
     * OTC申诉
     */
    @RequestMapping("/v1/account/otcApply")
    public Object otcApply(@RequestParam int id, @RequestParam String note) {
        Fuser fuser = getFuser();
        OtcOrderLog otcOrderLog = otcOrderLogService.findById(id);
        if (otcOrderLog.getStatus() != 4 && otcOrderLog.getStatus() != 5) {
            return forFailureResult(2);
        }
        if (fuser.getFid() != otcOrderLog.getFuser().getFid() && fuser.getFid() != otcOrderLog.getOtcOrder().getFuser().getFid()) {
            return forFailureResult(3);
        }
        otcOrderLog.setUpdateTime(Utils.getTimestamp());
        otcOrderLog.setStatus(5);//申诉
        if ((otcOrderLog.getOtcOrder().getType() == 1 && otcOrderLog.getFuser().getFid() == fuser.getFid())
                || (otcOrderLog.getOtcOrder().getType() != 1) && otcOrderLog.getOtcOrder().getFuser().getFid() == fuser.getFid()) {
            otcOrderLog.setBuyerNote(note);
        } else {
            otcOrderLog.setSllerNote(note);
        }
        otcOrderLogService.updateObj(otcOrderLog);
        return forSuccessResult();
    }
}


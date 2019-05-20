package com.trade.controller;

import com.alibaba.fastjson.JSONObject;
import com.trade.Enum.MessageStatusEnum;
import com.trade.Enum.VirtualCapitalOperationOutStatusEnum;
import com.trade.Enum.VirtualCapitalOperationTypeEnum;
import com.trade.Enum.VirtualCoinTypeStatusEnum;
import com.trade.cache.data.RealTimeDataService;
import com.trade.comm.ConstantMap;
import com.trade.dto.LatestDealData;
import com.trade.dto.ResultBean;
import com.trade.dto.SpreadDto;
import com.trade.dto.TradeFeesShareDto;
import com.trade.model.*;
import com.trade.service.admin.AdminService;
import com.trade.service.admin.FeeService;
import com.trade.service.admin.VirtualCoinService;
import com.trade.service.front.*;
import com.trade.util.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.util.HtmlUtils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.awt.geom.Rectangle2D;
import java.sql.Timestamp;
import java.util.*;

@RestController
@RequestMapping("/api")
public class AccountController extends BaseController {
    @Autowired
    private FrontUserService frontUserService;
    @Autowired
    private FrontAccountService frontAccountService;
    @Autowired
    private FrontVirtualCoinService frontVirtualCoinService;
    @Autowired
    private ConstantMap constantMap;
    @Autowired
    private FrontTradeService frontTradeService;
    @Autowired
    private FrontQuestionService frontQuestionService;
    @Autowired
    private FeeService feeService;
    @Autowired
    private RealTimeDataService realTimeDataService;

    @Autowired
    private FmessageService fmessageService;
    @Autowired
    private SpreadLogService spreadLogService;
    @Autowired
    private AdminService adminService;
    @Autowired
    private TradeFeesShareService tradeFeesShareService;
    @Autowired
    private MarketService marketService;
    @Autowired
    private VirtualCoinService virtualCoinService;


    @RequestMapping("/v1/account/values")
    public Object values(){
        Fuser fuser = getFuser();
        List<LatestDealData> dealDatas = realTimeDataService.getLatestDealDataList();
        List<LatestDealData> list = new ArrayList<>();
        String base = "USDP";
        dealDatas.forEach(latestDealData -> {
            if(base.equals(latestDealData.getGroup())){
                list.add(latestDealData);
            }
        });
        Map<String, Double> moneyMap = new HashMap();
        for(LatestDealData latestDealData: list){
            moneyMap.put(latestDealData.getfShortName(), latestDealData.getLastDealPrize());
        }
        Map<Long, Fvirtualcointype> coinMap = frontVirtualCoinService.findOnSellMap();
        Double value = 0d;
        for (Map.Entry<Long, Fvirtualcointype> entry : coinMap.entrySet()) {
            Fvirtualcointype fvirtualcointype = entry.getValue();
            Fvirtualwallet fvirtualwallet = frontUserService.findVirtualWalletByUser(fuser.getFid(), fvirtualcointype.getFid());
            if (Objects.nonNull(fvirtualwallet)) {
                double total = MathUtils.add(fvirtualwallet.getFfrozen(), fvirtualwallet.getFtotal());
                double plus = moneyMap.get(fvirtualcointype.getfShortName()) == null? 0 : moneyMap.get(fvirtualcointype.getfShortName());
                value += MathUtils.multiply(total, plus);
                if(base.equals(fvirtualcointype.getfShortName())){
                    value += total;
                }
            }
        }
        value = "en".equals(Utils.getCookie(getRequest().getCookies(), "lang")) ? value: value * 7 ;
        return forSuccessResult(FormatUtils.formatCNY(value));
    }

    @RequestMapping("/v1/account/balances")
    public Object personalAssets(){
        Fuser fuser = getFuser();

        //得到所有币种
        Map<Long, Fvirtualcointype> coinMap = frontVirtualCoinService.findOnSellMap();
        List balanceList = new ArrayList(coinMap.size());
        //得到所有交易对
        List<LatestDealData> dealDatas = realTimeDataService.getLatestDealDataList();
        List<LatestDealData> list = new ArrayList<>();
        //换算基准
        String base = "USDP";
        // 所有与USDP做交易的交易对
        dealDatas.forEach(latestDealData -> {
            if(base.equals(latestDealData.getGroup())){

                list.add(latestDealData);
            }
           /* if(base.equals(latestDealData.getfShortName())){
                list.add(latestDealData);
            }*/
        });
        //得到交易对简称 及 最新交易价格
        Map<String, Double> moneyMap = new HashMap();
        for(LatestDealData latestDealData: list){
            moneyMap.put(latestDealData.getfShortName(), latestDealData.getLastDealPrize());

        }


        for (Map.Entry<Long, Fvirtualcointype> entry : coinMap.entrySet()) {
            double valuation=0d;
            Fvirtualcointype fvirtualcointype = entry.getValue();
            Fvirtualwallet fvirtualwallet = frontUserService.findVirtualWalletByUser(fuser.getFid(), fvirtualcointype.getFid());
            if (Objects.nonNull(fvirtualwallet)) {
                Map wallet = new HashMap();
                wallet.put("id", fvirtualcointype.getFid());
                wallet.put("url", fvirtualcointype.getFurl());
                wallet.put("name", fvirtualcointype.getfShortName());
                wallet.put("isWithDraw", fvirtualcointype.isFIsWithDraw());
                wallet.put("isRecharge", fvirtualcointype.isFIsRecharge());
                wallet.put("total",  FormatUtils.formatBalance(fvirtualwallet.getFtotal()));
                wallet.put("frozen", FormatUtils.formatBalance(fvirtualwallet.getFfrozen()));
                wallet.put("num",FormatUtils.formatBalance(fvirtualwallet.getFtotal()+fvirtualwallet.getFfrozen()));//数量
                String coin = fvirtualcointype.getfShortName();

                double num = Double.valueOf(FormatUtils.formatBalance(fvirtualwallet.getFtotal()+fvirtualwallet.getFfrozen()));
                double plus = moneyMap.get(coin) == null? 0 : moneyMap.get(coin);
                valuation += MathUtils.multiply(num, plus);
                if(base.equals(coin)){
                    valuation += num;
                }

                wallet.put("enValuation", FormatUtils.formatCoin(valuation, "0.00"));
                wallet.put("cnValuation", FormatUtils.formatCoin((valuation * 7), "0.00"));
                balanceList.add(wallet);
            }
        }

        return forSuccessResult(balanceList);
    }


    @RequestMapping("/v1/account/TotalAssets")
    public Object personalTotalAssets() {
        Map<String,Object> map=new HashMap<String,Object>();
        Fuser fuser = getFuser();
        //得到所有币种
        Map<Long, Fvirtualcointype> coinMap = frontVirtualCoinService.findOnSellMap();
        List balanceList = new ArrayList(coinMap.size());
        //得到所有交易对
        List<LatestDealData> dealDatas = realTimeDataService.getLatestDealDataList();
        List<LatestDealData> list = new ArrayList<>();
        //换算基准
        String base = "USDP";
       // 所有与USDP做交易的交易对
        dealDatas.forEach(latestDealData -> {
            if(base.equals(latestDealData.getGroup())){
                list.add(latestDealData);
            }
        });
        //得到交易对简称 及 最新交易价格
        Map<String, Double> moneyMap = new HashMap();
        for(LatestDealData latestDealData: list){
            moneyMap.put(latestDealData.getfShortName(), latestDealData.getLastDealPrize());
        }

        Double value = 0d;
        for (Map.Entry<Long, Fvirtualcointype> entry : coinMap.entrySet()) {
            Fvirtualcointype fvirtualcointype = entry.getValue();
            Fvirtualwallet fvirtualwallet = frontUserService.findVirtualWalletByUser(fuser.getFid(), fvirtualcointype.getFid());
            if (Objects.nonNull(fvirtualwallet)) {
                String coin = fvirtualcointype.getfShortName();
             //   value+= moneyMap.get(coin) == null? 0 :Double.valueOf(FormatUtils.formatBalance(fvirtualwallet.getFtotal()+fvirtualwallet.getFfrozen())) ;

                double num = Double.valueOf(FormatUtils.formatBalance(fvirtualwallet.getFtotal()+fvirtualwallet.getFfrozen()));
                double plus = moneyMap.get(coin) == null? 0 : moneyMap.get(coin);
                value += MathUtils.multiply(num, plus);
                if(base.equals(coin)){
                    value += num;
                }

            }
        }
        map.put("total", "en".equals(Utils.getCookie(getRequest().getCookies(), "lang")) ?
                FormatUtils.formatCoin(value, "0.0000"):
                FormatUtils.formatCoin((value * 7), "0.0000"));

        return forSuccessResult(map);
    }

    @RequestMapping("/v1/account/getCoinAddress")
    public Object getCoinAddress(@RequestParam int symbol, @RequestParam(required = false, defaultValue = "0") boolean create) {
        Fuser fuser = getFuser();

        Fvirtualcointype fvirtualcointype = frontVirtualCoinService.findFvirtualCoinById(symbol);
        if (!fvirtualcointype.isFIsRecharge()) {
            // 不支持充提不反回
            return forFailureResult(1);
        }

        Fvirtualaddress fvirtualaddress = frontVirtualCoinService.findFvirtualaddress(fuser, fvirtualcointype);
        if (create && Objects.isNull(fvirtualaddress)) {
            fvirtualaddress = frontVirtualCoinService.updateAssignWalletAddress(fuser, fvirtualcointype);
            if (Objects.isNull(fvirtualaddress)) {
                //获取地址失败，直接从钱包获取
                BTCMessage btcMessage = new BTCMessage();
                btcMessage.setACCESS_KEY(fvirtualcointype.getFaccess_key());
                btcMessage.setIP(fvirtualcointype.getFip());
                btcMessage.setPORT(fvirtualcointype.getFport());
                btcMessage.setSECRET_KEY(fvirtualcointype.getFsecrt_key());
                if (btcMessage.getACCESS_KEY() == null
                        || btcMessage.getIP() == null
                        || btcMessage.getPORT() == null
                        || btcMessage.getSECRET_KEY() == null) {
                    return forFailureResult(2);
                }
                String name = fvirtualcointype.getfShortName().toLowerCase();
                String keyName = name + "Password";
                if(fvirtualcointype.getFaccess_key().toLowerCase().indexOf("btc") != -1 && constantMap.get(keyName) != null){
                    btcMessage.setPASSWORD(constantMap.getString(keyName));
                }
                BTCUtils btcUtils = new BTCUtils(btcMessage);
                String address;
                try {
                    address = btcUtils.getNewaddressValueForAdmin(fuser.getFid() + "");
                } catch (Exception e) {
                    return forFailureResult(3);
                }

                if (address == null || address.trim().length() == 0) {
                    return forFailureResult(4);
                } else {
                    Fvirtualaddress newAddress = new Fvirtualaddress();
                    newAddress.setFadderess(address);
                    newAddress.setFcreateTime(Utils.getTimestamp());
                    newAddress.setFuser(fuser);
                    newAddress.setFvirtualcointype(fvirtualcointype);
                    frontVirtualCoinService.saveAddress(newAddress);
                    return forSuccessResult(address);
                }
            }
        }

        return forSuccessResult(Objects.nonNull(fvirtualaddress) ? fvirtualaddress.getFadderess() : "");
    }


    @RequestMapping("/v1/account/getParamsByCoin")
    public Object getParamsByCoin(Integer id){
        Map map = new HashMap();
        Ffees ffees = this.feeService.findByCoin(id);
        map.put("min", ffees.getMinWithdraw());
        map.put("minWithdraw", ffees.getMinWithdraw());
        map.put("max", constantMap.getDouble("maxbtcWithdraw", 0));
        Fuser fuser = getFuser();
        Fvirtualwallet fvirtualwallet = frontUserService.findVirtualWalletByUser(fuser.getFid(), id);
        map.put("total", FormatUtils.formatBalance(fvirtualwallet.getFtotal()));
        map.put("frozen", FormatUtils.formatBalance(fvirtualwallet.getFfrozen()));

        map.put("feeRatio", ffees.getWithdrawRatio());
        map.put("feeAmount", ffees.getWithdraw());
        Map<Long, Fvirtualcointype> coinMap = frontVirtualCoinService.findOnSellMap();
        map.put("feeName", coinMap.get(Long.valueOf(ffees.getWithdrawFeeType())).getfShortName());
        int times = constantMap.getInt(ConstantKeys.DAY_DRAW_COIN_TIMES, 0);
        map.put("times", times);
        List<Object> addresses = frontVirtualCoinService.findFvirtualaddressInitWithdraws(fuser, id);
        map.put("addresses", addresses);
        return forSuccessResult(map);
    }

    @RequestMapping("/v1/account/getAddressByCoin")
    public Object getAddressByCoin(Integer id) {
        if(StringUtils.isEmpty(id)){
            return forFailureResult(-1);
        }
        Fuser fuser = getFuser();
        List<Object> addresses = frontVirtualCoinService.findFvirtualaddressInitWithdraws(fuser, id);
        return forSuccessResult(addresses);
    }
    @RequestMapping("/v1/account/getRegistAddressBySpread")
    public Object getRegistAddressBySpread(HttpServletRequest req)
    {
        Fuser fuser = getFuser();
        String path = req.getContextPath();
        String basePath = req.getScheme() + "://" + req.getServerName() + ":" + req.getServerPort() + path ;
        String addresses=basePath+"/user/register?inviteCode=" + fuser.getFid();
        return forSuccessResult(addresses);
    }

    @RequestMapping("/v1/account/addAddress")
    public Object addAddress(String address, String flag, Integer id, String code) {
        Fuser fuser = getFuser();
        ResultBean res = codeLimit(fuser.getFloginName(), code);
        if (SUCCESS != res.getCode()) {
            return res;
        }
        Fvirtualcointype fvirtualcointype = frontVirtualCoinService.findFvirtualCoinById(id);
        FvirtualaddressWithdraw fvirtualaddressWithdraw = new FvirtualaddressWithdraw(fvirtualcointype, address, fuser, Utils.getTimestamp(), flag);
        frontVirtualCoinService.updateFvirtualaddressWithdraw(fvirtualaddressWithdraw);
        return forSuccessResult();
    }

    @RequestMapping("/v1/account/updateFlag")
    public Object updateFlag(String flag, Integer id) {
        Fuser fuser = getFuser();
        FvirtualaddressWithdraw fvirtualaddressWithdraw = frontVirtualCoinService.findWithdrawAddressById(id);
        if (fuser.getFid() != fvirtualaddressWithdraw.getFuser().getFid()) {
            return forFailureResult(1);
        }
        fvirtualaddressWithdraw.setFlabel(flag);
        frontVirtualCoinService.updateFvirtualaddressWithdraw(fvirtualaddressWithdraw);
        return forSuccessResult();
    }

    @ResponseBody
    @RequestMapping("/v1/account/deleteAddress")
    public Object deleteAddress(Integer id) {
        Fuser fuser = getFuser();
        FvirtualaddressWithdraw f = frontVirtualCoinService.getFvirtualaddressWithdraw(id);
        if (fuser.getFid() != f.getFuser().getFid()) {
            return forFailureResult(1);
        }
        this.frontVirtualCoinService.deleteFvirtualaddressWithdraw(f);
        return forSuccessResult();
    }

    @RequestMapping("/v1/account/withdrawCoin")
    public Object withdrawCoin(@RequestParam int id, @RequestParam String address, @RequestParam double amount, @RequestParam String code, String safeWord) {
        amount = Math.abs(amount);
        Fuser fuser = this.frontUserService.findById(getFuser().getFid());

        Fvirtualcointype fvirtualcointype = this.frontVirtualCoinService.findFvirtualCoinById(id);
        Ffees ffees = feeService.findByCoin(fvirtualcointype.getFid());
        double minbtcWithdraw = ffees.getMinWithdraw() > 0 ? ffees.getMinWithdraw() : 0.0001;
        double maxbtcWithdraw = constantMap.getDouble("maxbtcWithdraw", 0);

        if (fuser.getfIdentityStatus() != 2) {
            return forFailureResult(1);
        }

        //最少提现0.1
        if (amount < minbtcWithdraw) {
            return forFailureResult(2, minbtcWithdraw);
        }
        // 最大提现
        if (amount > maxbtcWithdraw && maxbtcWithdraw > 0) {
            return forFailureResult(3, maxbtcWithdraw);
        }

        if (Objects.isNull(fvirtualcointype) || !fvirtualcointype.isFIsWithDraw() || fvirtualcointype.getFstatus() == VirtualCoinTypeStatusEnum.Abnormal) {
            // 项目不支持提现
            return forFailureResult(4);
        }

        //余额不足
        Fvirtualwallet fvirtualwallet = this.frontUserService.findVirtualWalletByUser(fuser.getFid(), fvirtualcointype.getFid());
        if (Objects.isNull(fvirtualwallet) || fvirtualwallet.getFtotal() < amount) {
            return forFailureResult(5);
        }

        // 手续费
        Fvirtualwallet feeWallet = this.frontUserService.findVirtualWalletByUser(fuser.getFid(), ffees.getWithdrawFeeType());
        if (Objects.isNull(feeWallet) || feeWallet.getFtotal() < ffees.getWithdrawFee(amount)) {
            // 您的账户没有足额的ETH，无法申请提币
            return forFailureResult(6);
        }

        // 今日提现次数限制
        int time = this.frontAccountService.getTodayVirtualCoinWithdrawTimes(fuser);
        int times = constantMap.getInt(ConstantKeys.DAY_DRAW_COIN_TIMES, 0);
        if (time >= times && times > 0) {
            return forFailureResult(7);
        }

        if (fuser.getFtradePassword() == null) {
            return forFailureResult(8);
        }

        if (!fuser.getFtradePassword().equals(Utils.MD5(safeWord))) {
            return forFailureResult(9);
        }

        ResultBean res = codeLimit(fuser.getFloginName(), code);
        if (res.getCode() != SUCCESS) {
            return res;
        }

        int res1 = this.frontVirtualCoinService.updateWithdrawBtc(address, fvirtualcointype, amount, fuser);
        return forSuccessResult(res1);
    }

    @RequestMapping("/v1/account/coinIn")
    public Object coinIn(@RequestParam int symbol, @RequestParam(defaultValue = "1") int page, int pageSize) {
        Fuser fuser = getFuser();
        Fvirtualcointype fvirtualcointype = new Fvirtualcointype();
        fvirtualcointype.setFid(symbol);
        List<Fvirtualcaptualoperation> list =
                this.frontVirtualCoinService.findFvirtualcaptualoperation(
                        fuser,
                        new int[]{VirtualCapitalOperationTypeEnum.COIN_IN},
                        null,
                        new Fvirtualcointype[]{fvirtualcointype},
                        "fid desc", (page - 1) * pageSize, pageSize);
        int totalCount = this.frontVirtualCoinService.countFvirtualcaptualoperation(fuser, new Integer[]{VirtualCapitalOperationTypeEnum.COIN_IN}, null,
                new Fvirtualcointype[]{fvirtualcointype});

        List records = new ArrayList(list.size());
        HashMap map = null;
        for (Fvirtualcaptualoperation item : list) {
            if (Objects.isNull(map)) {
                map = new HashMap();
            } else {
                map = (HashMap) map.clone();
            }
            map.put("id", item.getFid());
            map.put("amount", FormatUtils.formatBalance(item.getFamount()));
            map.put("status", item.getFstatus());
            map.put("confirmations", item.getFconfirmations());
            map.put("time", item.getFcreateTime());
            map.put("coinAddrees",item.getRecharge_virtual_address());
            records.add(map);
        }

        return forSuccessResult(records, totalCount);
    }


    @RequestMapping("/v1/account/coinOut")
    public Object coinOut(@RequestParam int symbol, @RequestParam(defaultValue = "1") int page, int pageSize) {
        Fuser fuser = getFuser();
        Fvirtualcointype fvirtualcointype = new Fvirtualcointype();
        fvirtualcointype.setFid(symbol);
        List<Fvirtualcaptualoperation> list =
                this.frontVirtualCoinService.findFvirtualcaptualoperation(
                        fuser,
                        new int[]{VirtualCapitalOperationTypeEnum.COIN_OUT},
                        null,
                        new Fvirtualcointype[]{fvirtualcointype},
                        "fid desc", (page - 1) * pageSize, pageSize);
        int totalCount = this.frontVirtualCoinService.countFvirtualcaptualoperation(fuser, new Integer[]{VirtualCapitalOperationTypeEnum.COIN_OUT}, null,
                new Fvirtualcointype[]{fvirtualcointype});

        Map<Long, Fvirtualcointype> coinMap = frontVirtualCoinService.findOnSellMap();

        List records = new ArrayList(list.size());
        HashMap map = null;
        for (Fvirtualcaptualoperation item : list) {
            if (Objects.isNull(map)) {
                map = new HashMap();
            } else {
                map = (HashMap) map.clone();
            }
            map.put("id", item.getFid());
            map.put("name", item.getFvirtualcointype().getfShortName());
            map.put("amount", FormatUtils.formatCoin(item.getFamount()));
            map.put("ffees", FormatUtils.formatCoin(item.getFfees()));
            map.put("status", item.getFstatus());
            map.put("address", item.getWithdraw_virtual_address());
            map.put("time", item.getFcreateTime());

            Fvirtualcointype feeType = coinMap.get(Long.valueOf(item.getFeeCoinType()));
            map.put("feeTypeName", Objects.nonNull(feeType) ? feeType.getfShortName() : "");
            records.add(map);
        }


        return forSuccessResult(records, totalCount);
    }

    /**
     * 重构充值提现方法 合为一个方法
     * @param symbol
     * @param page
     * @param pageSize
     * @param type "" ：全部  ，1 ：充值 ， 2： 提现
     * @return
     */
    @RequestMapping("/v1/account/billList")
    public Object billList(@RequestParam(required = false) Integer symbol, @RequestParam(defaultValue = "1") int page, int pageSize,@RequestParam(required = false,defaultValue = "-1") Integer type) {
        Fuser fuser = getFuser();
        Fvirtualcointype fvirtualcointype = null;
        if(StringUtils.isEmpty(symbol)){
            return forFailureResult(1);//交易对不能为空
        }
        fvirtualcointype=new Fvirtualcointype();
        fvirtualcointype.setFid(symbol);
        List<Fvirtualcaptualoperation> list =
                this.frontVirtualCoinService.findFvirtualcaptualoperation(
                        fuser,
                        new int[]{type},
                        null,
                        new Fvirtualcointype[]{fvirtualcointype},
                        "fid desc", (page - 1) * pageSize, pageSize);
        int totalCount = this.frontVirtualCoinService.countFvirtualcaptualoperation(fuser, new Integer[]{type}, null,
                new Fvirtualcointype[]{fvirtualcointype});
        Map<Long, Fvirtualcointype> coinMap = frontVirtualCoinService.findOnSellMap();
        List records = new ArrayList(list.size());
        HashMap map = null;
        for (Fvirtualcaptualoperation item : list) {
            if (Objects.isNull(map)) {
                map = new HashMap();
            } else {
                map = (HashMap) map.clone();
            }
            map.put("id", item.getFid());
            map.put("name", item.getFvirtualcointype().getfShortName());
            map.put("amount", FormatUtils.formatCoin(item.getFamount()));
            map.put("ffees", FormatUtils.formatCoin(item.getFfees()));
            map.put("status", item.getFstatus());
            map.put("address", item.getWithdraw_virtual_address());
            map.put("time", item.getFcreateTime());
            Fvirtualcointype feeType = coinMap.get(Long.valueOf(item.getFeeCoinType()));
            map.put("feeTypeName", Objects.nonNull(feeType) ? feeType.getfShortName() : "");
            map.put("confirmations", item.getFconfirmations());
            map.put("type",item.getFtype());
            records.add(map);

        }

        return forSuccessResult(records, totalCount);
    }

    /**
     * 正在处理充提
     *
     * @return
     */
    @RequestMapping("/v1/account/pendingCoin")
    public Object pendingCoin() {
        Fuser fuser = getFuser();
        List<Fvirtualcaptualoperation> list =
                this.frontVirtualCoinService.findFvirtualcaptualoperation(
                        fuser,
                        null,
                        new int[]{0, 1, 2},
                        null,
                        "fid desc", -1, -1);
        List records = new ArrayList(list.size());
        HashMap map = null;
        for (Fvirtualcaptualoperation item : list) {
            if (Objects.isNull(map)) {
                map = new HashMap();
            } else {
                map = (HashMap) map.clone();
            }
            map.put("id", item.getFid());
            map.put("type", item.getFtype());
            map.put("name", item.getFvirtualcointype().getfShortName());
            map.put("amount", FormatUtils.formatBalance(item.getFamount()));
            map.put("fees", FormatUtils.formatBalance(item.getFfees()));
            map.put("status", item.getFstatus());
            map.put("in_address", item.getRecharge_virtual_address());
            map.put("out_address", item.getWithdraw_virtual_address());
            map.put("time", item.getFcreateTime());
            records.add(map);
        }

        return forSuccessResult(records);
    }

    /**
     * 撤销提现
     *
     * @param id
     * @return
     */
    @RequestMapping("/v1/account/cancelWithdraw")
    public Object cancelWithdraw(@RequestParam int id) {
        Fuser fuser = getFuser();
        Fvirtualcaptualoperation fvirtualcaptualoperation = this.frontVirtualCoinService.findFvirtualcaptualoperationById(id);
        if (fvirtualcaptualoperation != null
                && fvirtualcaptualoperation.getFuser().getFid() == fuser.getFid()
                && fvirtualcaptualoperation.getFtype() == VirtualCapitalOperationTypeEnum.COIN_OUT
                && fvirtualcaptualoperation.getFstatus() == VirtualCapitalOperationOutStatusEnum.WaitForOperation
                ) {

            this.frontAccountService.updateCancelWithdrawBtc(fvirtualcaptualoperation);
            // 撤销成功
            return forSuccessResult();
        }
        // 撤销失败
        return forFailureResult(1);
    }

    @RequestMapping("/v1/account/history")
        public Object history(@RequestParam(required =false,defaultValue = "-1") Integer id,
                              @RequestParam(required =false,defaultValue = "-1") Integer type,
                              @RequestParam(required =false,defaultValue = "-1")Integer status, Integer pageSize,
                              Integer page,@RequestParam(required =false,defaultValue = "") String startDate,
                              @RequestParam(required =false,defaultValue = "") String endDate) {
        Fuser fuser = getFuser();
        HashMap map = null;

        List<Fentrust> entrusts =
                frontTradeService.findSuccessHistory(fuser.getFid(),  id, type, status, startDate, endDate, page, pageSize);
        int totalCount = frontTradeService.countSuccessHistory(fuser.getFid(),  id, type, status, null, null);
        List list = new ArrayList<>(entrusts.size());
        //Map res = new HashMap();
       // JSONObject coinList = new JSONObject();

        List<Fvirtualcointype> cointype = this.virtualCoinService.findAll();
        Map typeMap = new HashMap();
        for (Fvirtualcointype fvirtualcointype : cointype) {
            typeMap.put(fvirtualcointype.getFid(), fvirtualcointype.getfShortName());
        }

        if(entrusts!=null&&entrusts.size()!=0){
            for (Fentrust fentrust : entrusts) {
                String buy_id=null;
                String sell_id=null;
                int sellId=0;
                int buyId=0;
                if(fentrust.getMarket()!=null){
                    buy_id = String.valueOf(fentrust.getMarket().getBuyId());
                    sell_id = String.valueOf(fentrust.getMarket().getSellId());
                    sellId = fentrust.getMarket().getSellId();
                    buyId= fentrust.getMarket().getBuyId();
                }



             /*   if (coinList.get(buy_id) == null) {
                    coinList.put(buy_id, 1);
                }
                if (coinList.get(sell_id) == null) {
                    coinList.put(sell_id, 1);
                }*/
                if (Objects.isNull(map)) {
                    map = new HashMap();
                } else {
                    map = (HashMap) map.clone();
                }
                System.out.println("结果："+typeMap.get(sell_id));
                map.put("id", fentrust.getFid());
                map.put("fee", fentrust.getFfees());
                map.put("buyName", fentrust.getMarket().getBuyId());
                map.put("sellName", fentrust.getMarket().getSellId());
                map.put("tradeType",typeMap.get(sellId) + "/" + typeMap.get(buyId));//交易对
                map.put("price", FormatUtils.formatCoin(fentrust.getFprize()));
                map.put("avgPrice", FormatUtils.formatCoin(frontTradeService.avgSuccessPrice(fentrust.getFid())));
                map.put("amount", FormatUtils.formatCoin(fentrust.getFamount()));
                map.put("successAmount", FormatUtils.formatCoin(fentrust.getFsuccessAmount()));
                map.put("count", FormatUtils.formatCoin(fentrust.getFcount()));
                map.put("rightCount", FormatUtils.formatCoin(fentrust.getFcount() - fentrust.getFleftCount()));
                map.put("status", fentrust.getFstatus());
                map.put("type", fentrust.getFentrustType());
                map.put("time", fentrust.getFcreateTime());
                list.add(map);
            }
        }

       /* coinList.forEach((i, v) -> {
            Fvirtualcointype coin = frontVirtualCoinService.findFvirtualCoinById(Integer.valueOf(i));
            coinList.put(i, coin.getfShortName());
        });*/
        // res.put("history", list);
        // res.put("coinList", coinList);
        return forSuccessResult(list, totalCount);
    }

    @RequestMapping(value = "/v1/account/auth", method = RequestMethod.POST)
    public Object auth(HttpServletRequest request, @RequestParam() String no, @RequestParam() String name) throws Exception {
        if (StringUtils.isEmpty(name)) {
            return forFailureResult(1);//用户名不能为空
        }
        if (StringUtils.isEmpty(no)) {
            return forFailureResult(2);//证件号码不能为空
        }

        Fuser fuser = this.frontUserService.findById(getFuser().getFid());
        if (fuser.getFpostRealValidate() || fuser.getFhasRealValidate()) {
            return forFailureResult(3);//若已完成初级认证
        }
        List<Fuser> list = frontUserService.findUserByProperty("fidentityNo", no);
        if (!CollectionUtils.isEmpty(list)) {
            return forFailureResult(4);//证件号码已绑定
        }

        name = HtmlUtils.htmlEscape(name);
        fuser.setFidentityNo(no);
        fuser.setFrealName(name);
        fuser.setFpostRealValidate(true);
        fuser.setFhasRealValidate(true);
        fuser.setFhasRealValidateTime(Utils.getTimestamp());
        fuser.setFpostRealValidateTime(Utils.getTimestamp());
        this.frontUserService.updateFUser(fuser, request.getSession());
        return forSuccessResult();
    }

    @RequestMapping(value = "/v1/account/upload")
    public Object upload(@RequestParam(value = "img",required = false) MultipartFile multipartFile, int type) {
        if (null == multipartFile) {
            // 上传文件不存在
            return forFailureResult(1);
        } else if (multipartFile.getSize() > 3 * 1024 * 1024) {
            // 上传文件过大
            return forFailureResult(2);
        } else {
            try {
                byte[] bytes = multipartFile.getBytes();
                if (!Utils.isImage(bytes)) {
                    // 不是有效图片文件
                    return forFailureResult(3);
                } else {
                    Fuser fuser = getFuser();
                    String ext = StringUtils.getFilenameExtension(multipartFile.getOriginalFilename());
                    String filePath = Constants.IdentityPicDirectory + Utils.getRelativeFilePath(ext, bytes, fuser.getFid());
                    boolean flag = Utils.uploadFileToOss(multipartFile.getInputStream(), filePath);
                    if (flag) {
                        return forSuccessResult(filePath, type);
                    } else {
                        // 上传失败
                        return forFailureResult(4);
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            return forFailureResult(5);
        }
    }

    @RequestMapping(value = "/v1/account/uploadAuth", method = RequestMethod.POST)
    public Object uploadAuth(HttpServletRequest request, String fIdentityPath1, String fIdentityPath2, String fIdentityPath3) {
        if (!StringUtils.hasText(fIdentityPath1) || !StringUtils.hasText(fIdentityPath2) || !StringUtils.hasText(fIdentityPath3)) {
            return forFailureResult(1);
        }

        Fuser fuser = getFuser();
        fuser = frontUserService.findById(fuser.getFid());

        if (1 == fuser.getfIdentityStatus() || 2 == fuser.getfIdentityStatus()) {
            return forFailureResult(2);
        }
        fuser.setfIdentityPath(fIdentityPath1);
        fuser.setfIdentityPath2(fIdentityPath2);
        fuser.setfIdentityPath3(fIdentityPath3);
        fuser.setfIdentityStatus(1);
        fuser.setFlastUpdateTime(new Timestamp(System.currentTimeMillis()));
        frontUserService.updateFUser(fuser, request.getSession());
        return forSuccessResult();
    }


    @RequestMapping(value = "/v1/account/modSafeWord", method = RequestMethod.POST)
    public Object modSafeWord(HttpServletRequest request, String pwd, String code) {
        Fuser fuser = getFuser();

        if (fuser.getFtradePassword() != null && fuser.getFtradePassword().equals(Utils.MD5(pwd))) {
             return forFailureResult(1);
        }
        //资金安全密码不能和登录密码一致
        if( fuser.getFloginPassword().equals(Utils.MD5(pwd))){
              return forFailureResult(2);
        }

        ResultBean resultBean = codeLimit(fuser.getFloginName(), code);
        if (SUCCESS != resultBean.getCode()) {
            return resultBean;
        }

        fuser = this.frontUserService.findById(fuser.getFid());
        fuser.setFtradePassword(Utils.MD5(pwd));
        this.frontUserService.updateFUser(fuser, request.getSession());
        return forSuccessResult();
    }


    @RequestMapping(value = "/v1/account/modPassWord", method = RequestMethod.POST)
    public Object modPassWord(HttpServletRequest request, HttpServletResponse response ,String pwd, String code) {

        if(!StringUtils.hasText(pwd)){
            return forFailureResult(103);//密码为空
        }
        Fuser fuser = getFuser();

        ResultBean resultBean = codeLimit(fuser.getFloginName(), code);
        if (SUCCESS != resultBean.getCode()) {
            return resultBean;
        }
        fuser = this.frontUserService.findById(fuser.getFid());
        if(fuser.getFloginPassword().equals(Utils.MD5(pwd))){
           return forFailureResult(104);//新密码不能和旧密码一致
       }

        if(fuser.getFtradePassword()!=null&&fuser.getFtradePassword().equals(Utils.MD5(pwd))){
            return forFailureResult(105);//新密码不能和交易密码一致
        }
        fuser.setFloginPassword(Utils.MD5(pwd));
        this.frontUserService.updateFUser(fuser, request.getSession());
        ModelAndView model=new ModelAndView();
        model.setViewName("redirect:/api/v1/logout");

        return  model;
    }

    @RequestMapping(value = "/v1/account/modEmail", method = RequestMethod.POST)
    public Object modEmail(String code) {

        Fuser fuser = getFuser();

        ResultBean resultBean = codeLimit(fuser.getFemail(), code);
        if (SUCCESS != resultBean.getCode()) {
            return resultBean;
        }
        getSession().setAttribute(Constants.MOD_EMAIL_SESSION, fuser);
        return forSuccessResult();
    }

    @RequestMapping(value = "/v1/account/modEmail2", method = RequestMethod.POST)
    public Object modEmail2(HttpServletRequest request, String email, String code) {

        Fuser fuser = (Fuser) getSession().getAttribute(Constants.MOD_EMAIL_SESSION);

        if (fuser == null) {
            return forFailureResult(1);
        }

        if (frontUserService.isEmailExist(email)) {
            return forFailureResult(2);
        }

        ResultBean resultBean = codeLimit(email, code);
        if (SUCCESS != resultBean.getCode()) {
            return resultBean;
        }

        fuser = this.frontUserService.findById(fuser.getFid());
        if (fuser.getFloginName().equals(fuser.getFemail())) {
            fuser.setFloginName(email);
        }
        fuser.setFemail(email);
        this.frontUserService.updateFUser(fuser, request.getSession());
        return forSuccessResult();

    }

    @RequestMapping(value = "/v1/account/addEmail", method = RequestMethod.POST)
    public Object addEmail(HttpServletRequest request, String email, String code) {

        Fuser fuser = getFuser();

        if (frontUserService.isEmailExist(email)) {
            return forFailureResult(4); //4 账号已被注册或绑定
        }

        if (fuser.getFemail() != null) {
            return forFailureResult(1);//非法操作
        }

        ResultBean resultBean = codeLimit(email, code);
        if (SUCCESS != resultBean.getCode()) {
            return resultBean;
        }

        fuser = this.frontUserService.findById(fuser.getFid());
        if (fuser.getFloginName().equals(fuser.getFemail())) {
            fuser.setFloginName(email);
        }
        fuser.setFemail(email);
        this.frontUserService.updateFUser(fuser, request.getSession());
        return forSuccessResult();
    }

    @RequestMapping(value = "/v1/account/modMobile", method = RequestMethod.POST)
    public Object modMobile(String code) {

        Fuser fuser = getFuser();

        ResultBean resultBean = codeLimit(fuser.getFtelephone(), code);
        if (SUCCESS != resultBean.getCode()) {
            return resultBean;
        }
        getSession().setAttribute(Constants.MOD_MOBILE_SESSION, fuser);
        return forSuccessResult();
    }

    @RequestMapping(value = "/v1/account/modMobile2", method = RequestMethod.POST)
    public Object modMobile2(HttpServletRequest request, String mobile, String code, String areaCode) {

        Fuser fuser = (Fuser) getSession().getAttribute(Constants.MOD_MOBILE_SESSION);

        if (fuser == null) {
            return forFailureResult(1);
        }

        if (frontUserService.isEmailExist(mobile)) {
            return forFailureResult(2);
        }

        ResultBean resultBean = codeLimit(mobile, code);
        if (SUCCESS != resultBean.getCode()) {
            return resultBean;
        }

        fuser = this.frontUserService.findById(fuser.getFid());
        if (fuser.getFloginName().equals(fuser.getFtelephone())) {
            fuser.setFloginName(mobile);
            fuser.setFnickName(mobile);//修改昵称也为新的手机号
        }
        fuser.setFtelephone(mobile);
        fuser.setFareaCode(areaCode);
        this.frontUserService.updateFUser(fuser, request.getSession());
        return forSuccessResult();

    }

    @RequestMapping(value = "/v1/account/addMobile", method = RequestMethod.POST)
    public Object addMobile(HttpServletRequest request, String mobile, String code, String areaCode) {

        Fuser fuser = getFuser();

        if (frontUserService.isEmailExist(mobile)) {
            return forFailureResult(2);
        }

        ResultBean resultBean = codeLimit(mobile, code);
        if (SUCCESS != resultBean.getCode()) {
            return resultBean;
        }

        fuser = this.frontUserService.findById(fuser.getFid());
        if (fuser.getFloginName().equals(fuser.getFtelephone())) {
            fuser.setFloginName(mobile);
        }
        fuser.setFtelephone(mobile);
        fuser.setFareaCode(areaCode);
        this.frontUserService.updateFUser(fuser, request.getSession());
        // 邮件通知
        return forSuccessResult();
    }

    @RequestMapping("/v1/account/messages")
    public Object messages(@RequestParam(required = false, defaultValue = "1") int page, int pageSize) {
        Fuser fuser = getFuser();
        Map<String, Object> param = new HashMap<String, Object>();
        param.put("freceiver.fid", fuser.getFid());
        int totalCount = this.frontQuestionService.findFmessageByParamCount(param);
        int totalPage = totalCount / pageSize + (totalCount % pageSize == 0 ? 0 : 1);
        page = page < 1 ? 1 : page;
        page = page > totalPage ? totalCount : page;
        List<Fmessage> list = this.frontQuestionService.findFmessageByParam(param, (page - 1) * pageSize, pageSize, "fstatus asc, fcreateTime desc");
        List msgList = new ArrayList<>(list.size());
        list.forEach(e -> {
            Map map = new HashMap();
            map.put("id", e.getFid());
            map.put("title", e.getFtitle());
            map.put("time", e.getFcreateTime());
            map.put("status", e.getFstatus());
            msgList.add(map);
        });
        return forSuccessResult(msgList, totalCount);
    }

    @RequestMapping("/v1/account/delMessage")
    public Object delMessage(@RequestParam() String ids) {
        Fuser fuser = getFuser();
        String[] arr = ids.split(",");
        for (int i = 0; i < arr.length; i++) {
            fmessageService.deleteById(Integer.valueOf(arr[i]), fuser.getFid());
        }
        return forSuccessResult();
    }

    @RequestMapping("/v1/account/markMessage")
    public Object markMessage(@RequestParam() String ids) {
        Fuser fuser = getFuser();
        String[] arr = ids.split(",");
        for (int i = 0; i < arr.length; i++) {
            fmessageService.updateMarkMessage(Integer.valueOf(arr[i]), fuser.getFid());
        }
        return forSuccessResult();
    }

    @RequestMapping("/v1/account/messageDetail")
    public Object messageDetail(@RequestParam() int id) {
        Map map = new HashMap();
        Fmessage fmessage = this.frontQuestionService.findFmessageById(id);
        if (fmessage == null || fmessage.getFreceiver().getFid() != getFuser().getFid()) {
            return forFailureResult(1);
        }

        if (fmessage.getFstatus() == MessageStatusEnum.NOREAD_VALUE) {
            fmessage.setFstatus(MessageStatusEnum.READ_VALUE);
            this.frontQuestionService.updateFmessage(fmessage);
        }

        map.put("title", fmessage.getFtitle());
        map.put("content", fmessage.getFcontent());
        return forSuccessResult(map);
    }


    @RequestMapping("/v1/account/spreadLog")
    public Object spreadLog(@RequestParam() int page, @RequestParam() int pageSize) {
        Map map = new HashMap();
        Fuser fuser = getFuser();
        map.put("parent", fuser.getfIntroUser_id() != null ? fuser.getfIntroUser_id().getFloginName(): "");
        String filter = " where parent.fid = " + fuser.getFid() + " order by createTime desc ";
        List<SpreadLog> spreadLogs = spreadLogService.list((page - 1) * pageSize, pageSize, filter, true);
        List<SpreadDto> logs = new ArrayList();
        for(SpreadLog spreadLog: spreadLogs){
            SpreadDto spreadDto = new SpreadDto();
            spreadDto.setCreateTime(spreadLog.getCreateTime());
            spreadDto.setReward(FormatUtils.formatCoin(spreadLog.getParentRewardNum(), "#.########") + " " + spreadLog.getParentRewardCoin());
            spreadDto.setUid(spreadLog.getChild().getFid());//被推荐人的ID
            logs.add(spreadDto);
        }
        map.put("child", logs);
        return forSuccessResult(map, adminService.getAllCount("SpreadLog", filter));
    }

    @RequestMapping("/v1/account/spreadDetail")
    public Object spreadLog() {
        Map map = new HashMap();
        Fuser fuser = getFuser();
        List<Map<String, String>> maps = spreadLogService.listSum(fuser.getFid());
        map.put("sums", maps);
        map.put("total", adminService.getAllCount("SpreadLog", " where parent.fid = " + fuser.getFid()));
        return forSuccessResult(map);
    }


    @RequestMapping("/v1/account/shareLog")
    public Object shareLog(@RequestParam() int page, @RequestParam() int pageSize) {
        Fuser fuser = getFuser();
        String filter = " where parent.fid = " + fuser.getFid() + " order by shareTime desc ";
        List<TradeFeesShare> logs = tradeFeesShareService.list((page - 1) * pageSize, pageSize, filter, true);
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
        List<TradeFeesShareDto> tradeFeesShareDtos = new ArrayList<>();
        for(TradeFeesShare log: logs){
            TradeFeesShareDto tradeFeesShareDto = new TradeFeesShareDto();
            tradeFeesShareDto.setShareTime(log.getShareTime());
            tradeFeesShareDto.setUid(log.getChild().getFid());
            tradeFeesShareDto.setMarket(coinMap.get(marketMap.get(log.getMarket()).getSellId()).getfShortName() + "/" + coinMap.get(marketMap.get(log.getMarket()).getBuyId()).getfShortName());
            tradeFeesShareDto.setType(log.getType());
            tradeFeesShareDto.setReward(FormatUtils.formatCoin(log.getAmount(), "#.########") + " " + log.getCoin());
            tradeFeesShareDtos.add(tradeFeesShareDto);
        }
        return forSuccessResult(tradeFeesShareDtos, adminService.getAllCount("TradeFeesShare", filter));
    }

    @RequestMapping("/v1/account/shareDetail")
    public Object shareDetail() {
        Map map = new HashMap();
        Fuser fuser = getFuser();
        List<Map<String, Object>> lists = tradeFeesShareService.listSum(fuser.getFid());
        for(Map map1 : lists){
            map1.put("num", FormatUtils.formatCoin(Double.valueOf(map1.get("num").toString()), "#.########"));
        }
        map.put("sums", lists);
        List<LatestDealData> dealDatas = realTimeDataService.getLatestDealDataList();
        List<LatestDealData> list = new ArrayList<>();
        String base = "USDP";
        dealDatas.forEach(latestDealData -> {
            if(base.equals(latestDealData.getGroup())){
                list.add(latestDealData);
            }
        });
        Map<String, Double> moneyMap = new HashMap();
        for(LatestDealData latestDealData: list){
            moneyMap.put(latestDealData.getfShortName(), latestDealData.getLastDealPrize());
        }
        Double value = 0d;
        for (Map<String, Object> m: lists) {
            String coin = (String) m.get("coin");
            double num = Double.valueOf(m.get("num").toString());
            double plus = moneyMap.get(coin) == null? 0 : moneyMap.get(coin);
            value += MathUtils.multiply(num, plus);
            if(base.equals(coin)){
                value += num;
            }
        }
        map.put("total", "en".equals(Utils.getCookie(getRequest().getCookies(), "lang")) ?
                FormatUtils.formatCoin(value, "#.##"):
                FormatUtils.formatCoin((value * 7), "#.##"));
        return forSuccessResult(map);
    }

}


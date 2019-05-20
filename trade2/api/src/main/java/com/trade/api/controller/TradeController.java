package com.trade.api.controller;

import com.alibaba.fastjson.JSONObject;
import com.trade.Enum.EntrustStatusEnum;
import com.trade.Enum.EntrustTypeEnum;
import com.trade.api.APIResultCode;
import com.trade.cache.data.RealTimeDataService;
import com.trade.dto.FentrustlogData;
import com.trade.dto.LatestDealData;
import com.trade.model.*;
import com.trade.service.admin.VirtualWalletService;
import com.trade.service.front.FrontTradeService;
import com.trade.service.front.FrontUserService;
import com.trade.service.front.FrontVirtualCoinService;
import com.trade.service.front.MarketService;
import com.trade.util.DateUtils;
import com.trade.util.FormatUtils;
import com.trade.util.MarketUtils;
import com.trade.util.Utils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.*;

import static com.trade.api.APIResultCode.*;

@RestController
@RequestMapping("/api")
public class TradeController extends ApiBaseController {

    @Autowired
    private RealTimeDataService realTimeDataService;
    @Autowired
    private MarketService marketService;
    @Autowired
    private VirtualWalletService virtualWalletService;
    @Autowired
    private FrontTradeService frontTradeService;
    @Autowired
    private FrontUserService frontUserService;
    @Autowired
    private FrontVirtualCoinService frontVirtualCoinService;

    /**
     * 400/s
     * 获取zhgtrade行情
     *
     * @param symbol
     * @return
     */
    @RequestMapping(value = "/v1/ticker")
    public Object ticker(@RequestParam(value = "symbol") int symbol) {
        if (symbol <= 0) {
            return forFailureResult(APIResultCode.Code_201);
        }

        LatestDealData dealData = realTimeDataService.getLatestDealData(symbol);
        Map<String, Object> ticker = new HashMap<>();
        ticker.put("last", FormatUtils.defaultFormatCoin(dealData.getLastDealPrize()));
        ticker.put("buy", FormatUtils.defaultFormatCoin(dealData.getHigestBuyPrize()));
        ticker.put("sell", FormatUtils.defaultFormatCoin(dealData.getLowestSellPrize()));
        ticker.put("high", FormatUtils.defaultFormatCoin(dealData.getHighestPrize24()));
        ticker.put("low", FormatUtils.defaultFormatCoin(dealData.getLowestPrize24()));
        ticker.put("vol", FormatUtils.defaultFormatCoin(dealData.getVolumn()));

        return forSuccessResult(ticker);
    }

    /**
     * 400/s
     * 获取zhgtrade行情
     *
     * @return
     */
    @RequestMapping(value = "/v1/tickers")
    public Object tickers() {
        List<LatestDealData> dealDatas = realTimeDataService.getLatestDealDataList();
        List list = new ArrayList<>(dealDatas.size());

        dealDatas.forEach(dealData -> {
            Map<String, Object> ticker = new HashMap<>();
            ticker.put("symbol", dealData.getFid());
            ticker.put("last", FormatUtils.defaultFormatCoin(dealData.getLastDealPrize()));
            ticker.put("buy", FormatUtils.defaultFormatCoin(dealData.getHigestBuyPrize()));
            ticker.put("sell", FormatUtils.defaultFormatCoin(dealData.getLowestSellPrize()));
            ticker.put("high", FormatUtils.defaultFormatCoin(dealData.getHighestPrize24()));
            ticker.put("low", FormatUtils.defaultFormatCoin(dealData.getLowestPrize24()));
            ticker.put("vol", FormatUtils.defaultFormatCoin(dealData.getVolumn()));

            list.add(ticker);
        });

        return forSuccessResult(list);
    }

    /**
     * 500/s
     * 获取市场深度
     *
     * @return
     */
    @RequestMapping(value = "/v1/depth")
    public Object depth(@RequestParam(value = "symbol") int symbol,
                        @RequestParam(value = "merge", required = false, defaultValue = "0") int merge) {
        if (symbol <= 0) {
            return forFailureResult(APIResultCode.Code_201);
        }
        String sell = this.realTimeDataService.getBuyDepthMap(symbol, merge);
        String buy = this.realTimeDataService.getSellDepthMap(symbol, merge);

        JSONObject askBidJson = new JSONObject();
        askBidJson.put("bids", buy);
        askBidJson.put("asks", sell);

        return forSuccessResult(askBidJson);
    }

    /**
     * 400/s
     * 获取最近100交易信息
     *
     * @return
     */
    @RequestMapping(value = "/v1/orders")
    public Object orders(@RequestParam(value = "symbol") int symbol,
                         @RequestParam(value = "size", required = false, defaultValue = "100") int size) {
        if (symbol <= 0) {
            return forFailureResult(APIResultCode.Code_201);
        }

        if(size > 100){
            size = 100;
        }

        Set<FentrustlogData> logData = realTimeDataService.getEntrustSuccessMapLimit(symbol, size);
        List<Object> dataList = new ArrayList<>(logData.size());
        for (FentrustlogData fentrustlog : logData) {
            Map<String, Object> map = new HashMap<>(6);
            map.put("date", fentrustlog.getFcreateTime().getTime() / 1000);
            map.put("price", FormatUtils.defaultFormatCoin(fentrustlog.getFprize()));
            map.put("amount", FormatUtils.defaultFormatCoin(fentrustlog.getFamount()));
            map.put("type", fentrustlog.getfEntrustType() == EntrustTypeEnum.BUY ? "buy" : "sell");
            dataList.add(map);
        }

        return forSuccessResult(dataList);
    }

    /**
     * 400/s
     * 获取交易币种
     *
     * @return
     */
    @RequestMapping("/v1/coins")
    public Object coins(){
        List<LatestDealData> dealDatas = realTimeDataService.getLatestDealDataList();
        List<Map> data =new ArrayList<>();
        dealDatas.forEach(latestDealData -> {
            Map map = new HashMap();
            map.put("symbol",latestDealData.getFid());
            map.put("name",latestDealData.getfShortName());
            map.put("group",latestDealData.getGroup());
            data.add(map);
        });
        return forSuccessResult(data);
    }

    /**
     * 250/s
     * 获取用户信息
     *
     * @return
     */
    @RequestMapping(value = "/v1/balances")
    public Object balances() {
        int userId = getUserId();

        // 虚拟币
        List<Fvirtualwallet> wallets = virtualWalletService.list(0, 0, "where fvirtualcointype.fstatus=1 and fuser.fid=" + userId, false);
        List list = new ArrayList(wallets.size());
        HashMap map = null;
        for (Fvirtualwallet wallet : wallets) {
            if (Objects.isNull(map)) {
                map = new HashMap();
            } else {
                map = (HashMap) map.clone();
            }
            map.put("symbol", wallet.getFvirtualcointype().getFid());
            map.put("name", wallet.getFvirtualcointype().getfShortName());
            map.put("balance", FormatUtils.defaultFormatCoin(wallet.getFtotal()));
            map.put("frozen", FormatUtils.defaultFormatCoin(wallet.getFfrozen()));
            list.add(map);
        }

        return forSuccessResult(list);
    }

    /**
     * 300/s
     * @return
     */
    @RequestMapping(value = "/v1/coin_address")
    public Object walletAddress(@RequestParam(value = "symbol") int symbol) {
        if (symbol <= 0) {
            return forFailureResult(APIResultCode.Code_201);
        }

        Fvirtualcointype fvirtualcointype = frontVirtualCoinService.findFvirtualCoinById(symbol);
        if (Objects.isNull(fvirtualcointype)) {
            return forFailureResult(APIResultCode.Code_201);
        }

        if (!fvirtualcointype.isFIsRecharge()) {
            // 不能充值
            return forFailureResult(APIResultCode.Code_202);
        }

        Fuser fuser = getFuser();
        Fvirtualaddress fvirtualaddress = this.frontVirtualCoinService.findFvirtualaddress(fuser, fvirtualcointype);

        // 若没有地址 则分配地址
        if (null == fvirtualaddress) {
            try {
                fvirtualaddress = frontVirtualCoinService.updateAssignWalletAddress(fuser, fvirtualcointype);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        if (Objects.isNull(fvirtualaddress)) {
            // 没有分配到地址
            return forFailureResult(APIResultCode.Code_203);
        }

        return forSuccessResult(fvirtualaddress.getFadderess());
    }

    /**
     * 200/s
     * 挂单查询 最多100条
     *
     * @param symbol
     * @param since
     * @param type    0全部 1挂单中
     * @return
     */
    @RequestMapping(value = "/v1/trade_list")
    public Object tradeList(@RequestParam(value = "symbol") int symbol,
                            @RequestParam(value = "since", required = false, defaultValue = "0") long since,
                            @RequestParam(value = "type", defaultValue = "0") int type) {
        int userId = getUserId();

        if (symbol <= 0) {
            return forFailureResult(APIResultCode.Code_201);
        }

        Date beginDate = null;
        if (since > 0) {
            beginDate = new Date(since * 1000);
        }

        Integer[] status = 1 == type ? new Integer[]{1, 2} : null;

        List<Fentrust> fentrusts = frontTradeService.findFentrustHistory(userId, symbol, null, status, beginDate, null, 1, 100);
        List<Map<String, Object>> result = new ArrayList<>(fentrusts.size());
        for (Fentrust fentrust : fentrusts) {
            Map<String, Object> map = new HashMap<>(10);
            map.put("id", fentrust.getFid());
            map.put("datetime", DateUtils.formatDate(fentrust.getFcreateTime(), "yyyy-MM-dd HH:mm:ss"));
            map.put("amount", FormatUtils.defaultFormatCoin(fentrust.getFamount()));
            map.put("left_amount", FormatUtils.defaultFormatCoin(fentrust.getFamount() - fentrust.getFsuccessAmount()));
            map.put("price", FormatUtils.defaultFormatCoin(fentrust.getFprize()));
            map.put("status", fentrust.getFstatus());
            map.put("type", 0 == fentrust.getFentrustType() ? "buy" : "sell");
            map.put("count", FormatUtils.defaultFormatCoin(fentrust.getFcount()));
            map.put("left_count", FormatUtils.defaultFormatCoin(fentrust.getFleftCount()));
            result.add(map);
        }

        return forSuccessResult(result);
    }

    /**
     * 450/s
     * 获取用户订单
     *
     * @return
     */
    @RequestMapping(value = "/v1/trade_view")
    public Object tradeInfo(@RequestParam(value = "id") int id) {
        int userId = getUserId();
        Fentrust fentrust = frontTradeService.findFentrustById(id);
        if (userId != fentrust.getFuser().getFid()) {
            // 非法操作
            return forFailureResult(Code_401);
        }

        Map<String, Object> map = new HashMap<>();
        map.put("id", fentrust.getFid());
        map.put("datetime", DateUtils.formatDate(fentrust.getFcreateTime(), "yyyy-MM-dd HH:mm:ss"));
        map.put("amount", FormatUtils.defaultFormatCoin(fentrust.getFamount()));
        map.put("left_amount", FormatUtils.defaultFormatCoin(fentrust.getFamount() - fentrust.getFsuccessAmount()));
        map.put("price", FormatUtils.defaultFormatCoin(fentrust.getFprize()));
        map.put("status", fentrust.getFstatus());
        map.put("type", 0 == fentrust.getFentrustType() ? "buy" : "sell");
        map.put("count", FormatUtils.defaultFormatCoin(fentrust.getFcount()));
        map.put("left_count", FormatUtils.defaultFormatCoin(fentrust.getFleftCount()));

        return forSuccessResult(map);
    }

    /**
     * 200/s
     * 取消挂单
     *
     * @param id
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/v1/cancel_trade", method = RequestMethod.POST)
    public Object cancelEntrust(@RequestParam(value = "id") int id) throws Exception {
        Fuser fuser = getFuser();

        final Fentrust fentrust = this.frontTradeService.findFentrustById(id);
        if (fentrust.getFuser().getFid() != fuser.getFid()) {
            // 非法操作
            return forFailureResult(Code_401);
        }
        if (fentrust != null && (fentrust.getFstatus() == EntrustStatusEnum.Going || fentrust.getFstatus() == EntrustStatusEnum.PartDeal)) {
            /*更新挂单*/
            this.frontTradeService.updateCancelFentrust(fentrust, fuser);
            realTimeDataService.removeEntrustBuyMap(fentrust.getMarket().getId(), fentrust);
        } else {
            return forFailureResult(APIResultCode.Code_204);
        }

        return forSuccessResult(id);
    }

    /**
     * 200/s
     * 下单交易
     *
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/v1/trade", method = RequestMethod.POST)
    public Object trade(@RequestParam(value = "symbol") int symbol,
                        @RequestParam(value = "type") String type,
                        @RequestParam(value = "price") double price,
                        @RequestParam(value = "amount") double amount) throws Exception {
        Object resultBean = checkTrade(amount, price, symbol, type);
        return APIResultCode.Code_200.getCode().equals(((Map)resultBean).get("code")) ? forSuccessResult(((Map)resultBean).get("data")): resultBean;
    }

    private Object checkTrade(double tradeAmount, double tradeCnyPrice, int symbol, String type){
        Fuser sessionUser = getFuser();

        if (sessionUser == null){
            return forFailureResult(Code_401);
        }

        if(tradeAmount <= 0 || tradeCnyPrice <= 0 || (!("buy").equals(type) && !"sell".equals(type))){
            return forFailureResult(Code_1);
        }

        Market market = marketService.findById(symbol);

        if (market == null || market.getStatus() == Market.STATUS_Abnormal) {
            return  forFailureResult(Code_2);
        }

        if (market.getTradeStatus() == Market.TRADE_STATUS_Abnormal) {
            return  forFailureResult(Code_3);
        }

        if (MarketUtils.openTrade(market.getTradeTime()) == false) {
            return  forFailureResult(Code_4);
        }

        tradeAmount = Utils.getDouble(tradeAmount, market.getDecimals());
        tradeCnyPrice = Utils.getDouble(tradeCnyPrice, market.getDecimals());
        double minCount = market.getMinCount();
        double maxCount = market.getMaxCount();
        double minMoney = market.getMinMoney();
        double maxMoney = market.getMaxMoney();
        double minPrice = market.getMinPrice();
        double maxPrice = market.getMaxPrice();
        double tradeMoney = tradeAmount * tradeCnyPrice;

        if (minCount != -1 && tradeAmount < minCount) {
            return forFailureResult(Code_5, minCount);
        }

        if (maxCount != -1 && tradeAmount > maxCount) {
            return forFailureResult(Code_6, maxCount);
        }

        if (minMoney != -1 && tradeMoney < minMoney) {
            return forFailureResult(Code_7, minMoney);
        }

        if(maxMoney != -1 && tradeMoney > maxMoney){
            return forFailureResult(Code_8, maxMoney);
        }

        if(minPrice != -1 && tradeCnyPrice < minPrice){
            return forFailureResult(Code_9, minPrice);
        }

        if(maxPrice != -1 && tradeCnyPrice > maxPrice){
            return forFailureResult(Code_10, maxPrice);
        }

        Fuser fuser = this.frontUserService.findById(sessionUser.getFid());

        int id;
        if("buy".equals(type)){
            id = market.getBuyId();
        }else{
            id = market.getSellId();
        }

        Fvirtualwallet fvirtualwallet = this.frontUserService.findVirtualWalletByUser(fuser.getFid(), id);

        if (fvirtualwallet == null) {
            return forFailureResult(Code_1);
        }

        if ("buy".equals(type) && fvirtualwallet.getFtotal() < tradeMoney) {
            return  forFailureResult(Code_11);
        }

        if ("sell".equals("type") && fvirtualwallet.getFtotal() < tradeAmount) {
            return  forFailureResult(Code_11);
        }

        Fentrust fentrust;

        try {
            if("buy".equals(type)){
                fentrust = this.frontTradeService.updateEntrustBuy2(market, tradeAmount, tradeCnyPrice, fuser);
            }else{
                fentrust = this.frontTradeService.updateEntrustSell2(market, tradeAmount, tradeCnyPrice, fuser);
            }
            if(fentrust == null){
                return forFailureResult(Code_11);
            }
            frontTradeService.sendToQueue(false, symbol, fentrust);
            return forSuccessResult(fentrust.getFid());
        } catch (Exception e) {
            e.printStackTrace();
            return forFailureResult(Code_1);
        }

    }
}




















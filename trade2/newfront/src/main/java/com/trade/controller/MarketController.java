package com.trade.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.trade.Enum.EntrustStatusEnum;
import com.trade.auto.OneDayData;
import com.trade.cache.data.KlineDataService;
import com.trade.cache.data.RealTimeDataService;
import com.trade.comm.ConstantMap;
import com.trade.dto.*;
import com.trade.model.*;
import com.trade.mq.MessageQueueService;
import com.trade.service.admin.*;
import com.trade.service.front.*;
import com.trade.util.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

import javax.servlet.http.HttpServletRequest;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;


@Controller
@RequestMapping("/api/v2/market")
public class MarketController extends BaseController {
    private final Logger log = LoggerFactory.getLogger(MarketController.class);
    @Autowired
    private JedisPool jedisPool;

    @Autowired
    private RealTimeDataService realTimeDataService;
    @Autowired
    private FrontVirtualCoinService frontVirtualCoinService;
    @Autowired
    private SystemArgsService systemArgsService;
    @Autowired
    private FrontUserService frontUserService;
    @Autowired
    private FrontTradeService frontTradeService;
    @Autowired
    private VirtualCoinService virtualCoinService;
    @Autowired
    private LimittradeService limittradeService;
    @Autowired
    private FrontOthersService frontOthersService;
    @Autowired
    private KlineDataService klineDataService;
    @Autowired
    private EntrustService entrustService;
    @Autowired
    private MarketService marketService;

    @Autowired
    private OneDayData oneDayData;
    @Autowired
    private ConstantMap constantMap;
    @Autowired
    private VirtualWalletService virtualWalletService;
    @Autowired
    private FuserCointypeService fuserCointypeService;
    private Comparator<LatestDealData> comparator = (f1, f2) -> {
        //0总是在非0的后面
        if(f1.getTypeOrder() == 0 && f2.getTypeOrder() != 0){
            return 1;
        }else if(f1.getTypeOrder() != 0 && f2.getTypeOrder() == 0){
            return -1;
        } else {
            return Integer.valueOf(f1.getTypeOrder()).compareTo(f2.getTypeOrder());
        }
    };

    private static final int NUMBER_PER_PAGE = 10;

    /**
     * 实时行情
     *
     * @param id
     * @return
     */
    @RequestMapping("/real")
    @ResponseBody
    public Map<String, Object> real(@RequestParam("symbol") int id) {
        Map<String, Object> map = new HashMap<>();
        map.put("last", String.valueOf(this.realTimeDataService.getLatestDealPrize(id)));
        map.put("buy", String.valueOf(this.realTimeDataService.getHighestBuyPrize(id)));
        map.put("sell", String.valueOf(this.realTimeDataService.getLowestSellPrize(id)));
        map.put("high", String.valueOf(this.oneDayData.getHighest(id)));
        map.put("low", String.valueOf(this.oneDayData.getLowest(id)));
        map.put("vol", String.valueOf(this.oneDayData.getTotal(id)));
        return map;
    }

    private ResultBean checkTrade(HttpServletRequest request, double tradeAmount, double tradeCnyPrice, int symbol, String tradePwd, int type,String language){
        Fuser sessionUser = getSessionUser(request);

        if (sessionUser == null){
            return forFailureResult(401);
        }

        if(tradeAmount <= 0 || tradeCnyPrice <= 0 || (type != 1 && type != 0)){
            return forFailureResult(-1);
        }

        Market market = marketService.findById(symbol);

        if (market == null || market.getStatus() == Market.STATUS_Abnormal) {
            return  forFailureResult(-2);//该交易对已被禁用
        }

        if (market.getTradeStatus() == Market.TRADE_STATUS_Abnormal) {
            return  forFailureResult(-3);//该交易对已暂停交易
        }

        if (MarketUtils.openTrade(market.getTradeTime()) == false) {
            return  forFailureResult(-4);//不在交易时间内
        }
        Fuser fuser = this.frontUserService.findById(sessionUser.getFid());
        if (isNeedTradePassword(request)) {
            if (tradePwd == null || tradePwd.trim().length() == 0) {
                return forFailureResult(-1);//交易密码不能为空
            }
            if (fuser.getFtradePassword() == null) {
                return forFailureResult(-12);//您未设置交易密码
            }
            if (!Utils.MD5(tradePwd).equals(fuser.getFtradePassword())) {
                return forFailureResult(-13);//交易密码错误
            }
        }
        tradeAmount = Utils.getDouble(tradeAmount, market.getDecimals());
        tradeCnyPrice = Utils.getDouble(tradeCnyPrice, market.getDecimals());
        double minCount = market.getMinCount();//最小交易量
        double maxCount = market.getMaxCount();//最大交易量
        double minMoney = market.getMinMoney();//最小交易额
        double maxMoney = market.getMaxMoney();//最大交易额
        double minPrice = market.getMinPrice();//最小交易价
        double maxPrice = market.getMaxPrice();//最大交易价
        double tradeMoney = tradeAmount * tradeCnyPrice;

        if (minCount != -1 && tradeAmount < minCount) {
            return forFailureResult(-5, minCount);//交易数量不能小于minCount
        }

        if (maxCount != -1 && tradeAmount > maxCount) {
            return forFailureResult(-6, maxCount);// 交易数量不能大于maxCount
        }

        if(minPrice != -1 && tradeCnyPrice < minPrice){
            return forFailureResult(-9, minPrice);//交易价格不能小于minPrice
        }
        if(maxPrice != -1 && tradeCnyPrice > maxPrice){
            return forFailureResult(-10, maxPrice);//交易价格不能大于maxPrice
        }
        if (minMoney != -1 && tradeMoney < minMoney) {
            return forFailureResult(-7, minMoney);//交易总金额不能小于minMoney
        }

        if(maxMoney != -1 && tradeMoney > maxMoney){
            return forFailureResult(-8, maxMoney);//交易总金额不能大于maxMoney
        }

        int id;
        if(type == 0){
            id = market.getBuyId();
        }else{
            id = market.getSellId();
        }

        Fvirtualwallet fvirtualwallet = this.frontUserService.findVirtualWalletByUser(fuser.getFid(), id);

        if (fvirtualwallet == null) {
            return forFailureResult(-3); //交易已暂停，开放请关注公告
        }

        if (type == 0 && fvirtualwallet.getFtotal() < tradeMoney) {
            return  forFailureResult(-11);
        }

        if (type == 1 && fvirtualwallet.getFtotal() < tradeAmount) {
            return  forFailureResult(-11);
        }

        Fentrust fentrust;

        try {
            if(type == 0){
                fentrust = this.frontTradeService.updateEntrustBuy2(market, tradeAmount, tradeCnyPrice, fuser);
            }else{
                fentrust = this.frontTradeService.updateEntrustSell2(market, tradeAmount, tradeCnyPrice, fuser);
            }
            if(fentrust == null){
                return forFailureResult(-11);
            }
            frontTradeService.sendToQueue(false, symbol, fentrust);
            setNoNeedPassword(request);
            return forSuccessResult(fentrust.getFid());
        } catch (Exception e) {
            e.printStackTrace();
            return forFailureResult(-1);
        }
    }

    /**
     * 买入
     * @param request
     * @param symbol
     * @param tradeAmount
     * @param tradeCnyPrice
     * @param tradePwd
     * @return
     * @throws Exception
     */
    @ResponseBody
    @RequestMapping(value = "/buyBtcSubmit", method = RequestMethod.POST)
    public Object buyBtcSubmit(
            HttpServletRequest request,
            @RequestParam() final int symbol,//币种
            @RequestParam() double tradeAmount,//数量
            @RequestParam() double tradeCnyPrice,//单价
            @RequestParam(required = false, defaultValue = "0") String tradePwd,
            @RequestParam(required = false, defaultValue = "0") String language
    ) throws Exception {
        return checkTrade(request, tradeAmount, tradeCnyPrice, symbol, tradePwd, 0,language);
    }

    /**
     * 卖出
     * @param request
     * @param symbol
     * @param tradeAmount
     * @param tradeCnyPrice
     * @param tradePwd
     * @return
     * @throws Exception
     */
    @ResponseBody
    @RequestMapping(value = "/sellBtcSubmit", method = RequestMethod.POST)
    public Object sellBtcSubmit(
            HttpServletRequest request,
            @RequestParam() final int symbol,//币种
            @RequestParam() double tradeAmount,//数量
            @RequestParam() double tradeCnyPrice,//单价
            @RequestParam(required = false, defaultValue = "0") String tradePwd,
            @RequestParam(required = false, defaultValue = "0") String language
    ) throws Exception {
        return checkTrade(request, tradeAmount, tradeCnyPrice, symbol, tradePwd, 1,language);
    }


    @RequestMapping("/getFee")
    @ResponseBody
    public Object getFee(@RequestParam("symbol") int symbol) {
        double ffee = 0d;
        double buyFfee = 0d;
        Fuser fuser = getFuser();
        if (fuser != null && fuser.getFneedFee()) {
            Market market = marketService.findById(symbol);
            ffee = market.getSellFee();
            buyFfee = market.getBuyFee();
        }
        Map<String, Double> ret = new HashMap<>();
        ret.put("sellFee", ffee);
        ret.put("buyFee", buyFfee);
        return ret;
    }

    public Flimittrade isLimitTrade(int vid) {
        Flimittrade flimittrade = null;
        String filter = "where fvirtualcointype.fid="+vid;
        List<Flimittrade> flimittrades = this.limittradeService.list(0, 0, filter, false);
        if(flimittrades != null && flimittrades.size() >0){
            flimittrade = flimittrades.get(0);
            // 涨跌幅比例，如果设置了涨跌幅，那么限价则不生效
            if (flimittrade.getFpercent() != null && flimittrade.getFpercent() > 0) {
                String json = klineDataService.getJsonString(vid, 10);// 获取1日K线，取到昨天的收盘价
                JSONArray jsonArray = JSON.parseArray(json);
                if (jsonArray != null && jsonArray.size() > 1) {
                    JSONArray yesterdayDay = jsonArray.getJSONArray(jsonArray.size() - 2);
                    double shou = yesterdayDay.getDouble(4);
                    flimittrade.setFupprice(MathUtils.convert(MathUtils.add(shou, MathUtils.multiply(shou, flimittrade.getFpercent())), 8));
                    flimittrade.setFdownprice(MathUtils.convert(MathUtils.subtract(shou, MathUtils.multiply(shou, flimittrade.getFpercent())), 8));
                } else {
                    Fvirtualcointype fvirtualcointype = frontVirtualCoinService.findFvirtualCoinById(vid);
//                    double openPrice = fvirtualcointype.getFopenPrice();
//                    if (openPrice > 0) {
//                        flimittrade.setFupprice(MathUtils.convert(MathUtils.add(openPrice, MathUtils.multiply(openPrice, flimittrade.getFpercent())), 8));
//                        flimittrade.setFdownprice(MathUtils.convert(MathUtils.subtract(openPrice, MathUtils.multiply(openPrice, flimittrade.getFpercent())), 8));
//                    }
                }
            }
        }
        return flimittrade;
    }

    /**
     * 取消挂单
     * @param id
     * @return
     * @throws Exception
     */
    @ResponseBody
    @RequestMapping(value = "/cancelEntrust", method = RequestMethod.POST)
    public Object cancelEntrust(@RequestParam(required = false, defaultValue = "0") int id) throws Exception {

        Map<String, Object> map = new HashMap<>();
        Fuser sessionUser = getFuser();

        if (sessionUser == null) {
            map.put("isLogin", 0);
            return map;
        }

        Fuser fuser = this.frontUserService.findById(sessionUser.getFid());
        final Fentrust fentrust = this.frontTradeService.findFentrustById(id);
        if (fentrust != null && (fentrust.getFstatus() == EntrustStatusEnum.Going || fentrust.getFstatus() == EntrustStatusEnum.PartDeal) && fentrust.getFuser().getFid() == fuser.getFid()) {
            try {
                /*更新挂单*/
                this.frontTradeService.updateCancelFentrust(fentrust, fuser);
                realTimeDataService.removeEntrustBuyMap(fentrust.getMarket().getId(), fentrust);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return map;
    }

    @ResponseBody
    @RequestMapping("/refreshUserInfo")
    public Object refreshUserInfo(HttpServletRequest request, @RequestParam(required = false, defaultValue = "0") int symbol) throws Exception {
        Map<String, Object> map = new HashMap<>();

        Market market = marketService.findById(symbol);

        if (market == null) {
            return map;
        }

        map.put("symbol", symbol);
        map.put("recommendPrizesell", realTimeDataService.getHighestBuyPrize(symbol));//推荐卖出价
        map.put("recommendPrizebuy", realTimeDataService.getLowestSellPrize(symbol));//推荐买入价

        Fuser user = getSessionUser(request);

        if (user != null) {
            map.put("isLogin", 1);
            map.put("needTradePasswd", isNeedTradePassword(request));

            Fvirtualwallet virtualwallet = frontUserService.findVirtualWalletNative(user.getFid(), market.getSellId());
            Fvirtualwallet btcWallet = frontUserService.findVirtualWalletNative(user.getFid(), market.getBuyId());

            map.put("rmbtotal",  FormatUtils.formatCoin(btcWallet.getFtotal(),"0.0000"));
            map.put("rmbfrozen", FormatUtils.formatCoin(btcWallet.getFfrozen(),"0.0000"));
            map.put("vtype", "btc");

            map.put("virtotal",  FormatUtils.formatCoin(virtualwallet.getFtotal(),"0.0000"));
            map.put("virfrozen", FormatUtils.formatCoin(virtualwallet.getFfrozen(),"0.0000"));

            List entrustList = frontTradeService.getFentrustHistory(user.getFid(), symbol, 0, 5);
            List entrustListLog = frontTradeService.findFentrustHistory(user.getFid(), symbol, 0, 5);
            map.put("entrustList", entrustList);
            map.put("entrustListLog", entrustListLog);
        } else {
            map.put("isLogin", 0);
        }

        // 返回sessionid，用来做websocket权限验证
        map.put("token", request.getSession().getId());
      /*  String sessionValue = Utils.getCookie(getRequest().getCookies(), "JSESSIONID");
        log.info("首页session******************"+sessionValue);
        try (Jedis jedis = jedisPool.getResource()) {
            if(StringUtils.hasText(sessionValue)){
                jedis.set(Constants.KLINE_SESSION,sessionValue);

            }

        }*/
        return map;
    }

    /**
     * 委托/成交数据
     *
     * @param symbol
     * @param deep
     * @return
     * @throws Exception
     */
    @ResponseBody
    @RequestMapping("/marketRefresh")
    public Object marketRefresh(@RequestParam(required = false, defaultValue = "0") int symbol, @RequestParam(required = false, defaultValue = "4") int deep) throws Exception {

        Map<String, Object> map = new HashMap<>();

        Object[] successEntrusts = this.realTimeDataService.getEntrustSuccessMap(symbol).toArray();
        String buyEntrusts = this.realTimeDataService.getBuyDepthMap(symbol, deep);
        String sellEntrusts = this.realTimeDataService.getSellDepthMap(symbol, deep);

        List<List<String>> recentDealList = toStringList2(successEntrusts);

        map.put("buyDepthList", JSON.parseArray(buyEntrusts));
        map.put("sellDepthList", JSON.parseArray(sellEntrusts));
        map.put("recentDealList", recentDealList);
        map.put("symbol", symbol);

        return map;
    }

    @ResponseBody
    @RequestMapping("/depthData")
    public Object depthData(@RequestParam(required = false, defaultValue = "0") int symbol) {
        return realTimeDataService.getMarketJSON(symbol);
    }

    private List<List<String>> toStringList2(Object[] successEntrusts) {
        List<List<String>> recentDealList = new ArrayList<List<String>>();
        for (int i = 0; i < successEntrusts.length && i <= 50; i++) {
            FentrustlogData fentrust = (FentrustlogData) successEntrusts[i];
            List<String> itemList = new ArrayList<String>();
            itemList.add(FormatUtils.formatCoin(fentrust.getFprize(),"0.00000000"));
            itemList.add(FormatUtils.formatCoin(fentrust.getFcount(),"0.00000000"));
            itemList.add(String.valueOf(new SimpleDateFormat("MM-dd HH:mm:ss").format(fentrust.getFcreateTime())));
            itemList.add(String.valueOf(fentrust.getfEntrustType() + 1));
            recentDealList.add(itemList);
        }
        return recentDealList;
    }

    public boolean isNeedTradePassword(HttpServletRequest request) {
        Fuser user = getFuser();
        if (user == null) return true;
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String key = user.getFid() + "trade";
        Object obj = request.getSession().getAttribute(key);

        if (obj == null) {
            return true;
        } else {
            try {
                double hour = Double.valueOf(this.systemArgsService.getValue("tradePasswordHour"));
                double lastHour = Utils.getDouble((sdf.parse(obj.toString()).getTime() - new Date().getTime()) / 1000 / 60 / 60, 2);
                if (lastHour >= hour) {
                    request.getSession().removeAttribute(key);
                    return true;
                } else {
                    return false;
                }
            } catch (ParseException e) {
                return false;
            }
        }
    }

    public void setNoNeedPassword(HttpServletRequest request) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String key = getFuser().getFid() + "trade";
        request.getSession().setAttribute(key, sdf.format(new Date()));
    }

    @RequestMapping("/news")
    public ModelAndView dynamic(
            @RequestParam(required = false, defaultValue = "1") int currentPage,
            @RequestParam(required = false, defaultValue = "1") int symbol
    ){
        ModelAndView map = new ModelAndView();
        int total = frontOthersService.countArticleByCoinId(symbol);
        List<Farticle> list = frontOthersService.findArticleByCoinId(symbol, (currentPage - 1) * NUMBER_PER_PAGE, NUMBER_PER_PAGE);
        Fvirtualcointype fvirtualcointype = frontVirtualCoinService.findFvirtualCoinById(symbol);
        map.addObject("pageNow", currentPage);
        map.addObject("total", total);
        map.addObject("pageSize", NUMBER_PER_PAGE);
        map.addObject("list", list);
        map.addObject("fname", fvirtualcointype.getFname());
        map.addObject("symbol", symbol);
        map.setViewName("market/news");
        return map;
    }

    @RequestMapping("/orders")
    @ResponseBody
    public Object orders(@RequestParam(required = false, defaultValue = "-1") int symbol) {
        Map<String, Object> map = new HashMap<>();
        Fuser fuser = getFuser();
        if (fuser != null) {
            int uid = fuser.getFid();
            Collection list = entrustService.getOrders(uid, symbol);
            list.forEach(row -> {
                List arr = (List) row;
                arr.forEach(row2 -> {
                    Object[] rows = (Object[]) row2;
                    LatestDealData market = realTimeDataService.getLatestDealData((Integer) rows[0]);
                    rows[1] = market.getfShortName();
                    rows[2] = market.getfShortName() + " / " + market.getGroup();
                    rows[3] = market.getGroup();
                });
            });
            map.put("orders", list);
        }
        return map;
    }
    /**
     * 币币交易
     *
     * @return
     */
    @ResponseBody
    @RequestMapping("/coins")
    public Object coins(){
         List<LatestDealData> dealDatas = realTimeDataService.getLatestDealDataList();
         Fuser fuser=getFuser();
         List<FuserCointype> collectList=new ArrayList<FuserCointype>();
         if(fuser!=null){
             collectList= fuserCointypeService.findByUser(fuser.getFid(),0,false);
         }
         List<LatestDealData> immutableList = Utils.deepCopy(dealDatas);
         immutableList.sort(comparator);
         Map<String,Double> groupMap=new HashMap<String,Double>();

          String base = "USDP";
          immutableList.forEach(latestDealData -> {
                     if(base.equals(latestDealData.getGroup())){
                         groupMap.put(latestDealData.getfShortName(), latestDealData.getLastDealPrize()*7);
                       /*  if( "en".equals(Utils.getCookie(getRequest().getCookies(), "lang")) ){*/
                       /*     groupMap.put(latestDealData.getfShortName(), latestDealData.getLastDealPrize());*/
                       /*  }else{*/
                       /*    groupMap.put(latestDealData.getfShortName(), latestDealData.getLastDealPrize()*7);*/
                       /*  }*/

                     }
                 });


        Map map =new HashMap();
        Map<String, List<LatestDealData>> dataMap = new TreeMap<>();
        for (LatestDealData latestDealData:immutableList) {
            List<LatestDealData> list = dataMap.get(latestDealData.getGroup());
            if (list == null) {
                list = new ArrayList<>();
                dataMap.put(latestDealData.getGroup(), list);
            }
            if (base.equals(latestDealData.getGroup())) {
                latestDealData.setCnPrice(7);
                latestDealData.setEnPrice(latestDealData.getLastDealPrize());

                  /* if( "en".equals(Utils.getCookie(getRequest().getCookies(), "lang")) ){*/
                  /*       latestDealData.setCnPrice(latestDealData.getLastDealPrize());*/
                  /* }else{*/
                  /*       latestDealData.setCnPrice(7);*/
/*
*/
                  /* }*/
            }
            for (String name : groupMap.keySet()) {
                if (name.equals(latestDealData.getGroup())) {
                    latestDealData.setCnPrice(groupMap.get(name));
                    latestDealData.setEnPrice(groupMap.get(name) / 7);
                }
            }

            if (collectList != null && collectList.size() != 0) {

                for (FuserCointype fuserCointype : collectList) {
                    //如果用户已经选择了交易对 ，则标记一下
                    if (latestDealData.getFid() == fuserCointype.getMarket().getId()) {
                        latestDealData.setIsCollection(Constants.collectSelf);
                    }
                }
            }
            //按涨跌幅排序
            list.add(latestDealData);
            list.sort((f1, f2) -> {
                return Double.valueOf(f2.getFupanddown()).compareTo(f1.getFupanddown());

            });



        }
       /* String sessionValue = Utils.getCookie(getRequest().getCookies(), "JSESSIONID");
        log.info("首页session******************"+sessionValue);
        try (Jedis jedis = jedisPool.getResource()) {
            if(StringUtils.hasText(sessionValue)){
                jedis.set(Constants.KLINE_SESSION,sessionValue);

            }

        }*/

        map.put("dataMap", dataMap);
        return map;
    }

    /**
     * 币币交易
     *
     * @return
     */
    @ResponseBody
    @RequestMapping("/allcoins")
    public Object allcoins(){
        List<LatestDealData> vlist = realTimeDataService.getLatestDealDataList();
        List immutableList = Utils.deepCopy(vlist);
        immutableList.sort(comparator);
        return immutableList;
    }
    @ResponseBody
    @RequestMapping("/allcoinsComplete")
    public Object allcoinsComplete(){
        Fuser fuser=getFuser();
        if(fuser==null){
            return forFailureResult(401);
        }
        List<LatestDealData> vlist = realTimeDataService.getLatestDealDataList();
        List<FuserCointype> collectList= fuserCointypeService.findByUser(fuser.getFid(),0,false);
        List<LatestDealData> immutableList = Utils.deepCopy(vlist);
        immutableList.sort(comparator);
        Map<String,Double> groupMap=new HashMap<String,Double>();
         String base = "USDP";
         immutableList.forEach(latestDealData -> {
                    if(base.equals(latestDealData.getGroup())){
                           groupMap.put(latestDealData.getfShortName(), latestDealData.getLastDealPrize()*7);
                    }
                });
         
            for (LatestDealData latestDealData:immutableList) {
                    if(base.equals(latestDealData.getGroup())){
                          latestDealData.setCnPrice(7);
                          latestDealData.setEnPrice(latestDealData.getLastDealPrize())  ;
                    }
                for (String name : groupMap.keySet()) {
                   if(name.equals(latestDealData.getGroup())){
                           latestDealData.setCnPrice(groupMap.get(name));
                           latestDealData.setEnPrice(groupMap.get(name)/7);
                   }
                }
                if(collectList!=null&&collectList.size()!=0){
                        
                            for (FuserCointype fuserCointype:collectList) {                      
                                //如果用户已经选择了交易对 ，则标记一下                                            
                                if(latestDealData.getFid()==fuserCointype.getMarket().getId()){  
                                    latestDealData.setIsCollection(Constants.collectSelf);       
                                }                                                                
                            }
                }
            }
        return forSuccessResult(immutableList);
    }
    @RequestMapping("/getEthPrice")
    @ResponseBody
    public Object getEthPrice() {
        Map result = new HashMap();
        String price = this.constantMap.getEth().get("price");
        String rate = this.constantMap.getEth().get("rate");
        String last ="0";
        String newRate = "0";
        if(price != null){
            last = JSON.parseObject(JSON.parseObject(price).getString("ticker")).getString("last");
        }
        if(rate != null){
            newRate = JSON.parseObject(rate).getString("rate");
        }

        result.put("last",last);
        result.put("rate",newRate);

        return result;
    }


}

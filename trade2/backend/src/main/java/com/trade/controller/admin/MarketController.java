package com.trade.controller.admin;

import com.trade.model.Market;
import com.trade.service.front.FrontVirtualCoinService;
import com.trade.service.front.MarketService;
import com.trade.util.CollectionUtils;
import com.trade.util.Constants;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import java.util.Objects;

@Controller
public class MarketController {
    @Autowired
    private MarketService marketService;
    @Autowired
    private FrontVirtualCoinService frontVirtualCoinService;

    @RequestMapping("ssadmin/marketList")
    @RequiresPermissions("ssadmin/marketList.html")
    public Object list(Integer status, Integer tradeStatus,
            @RequestParam(defaultValue = "1", name = "pageNum") int page) {
        ModelAndView modelAndView = new ModelAndView("ssadmin/marketList");

        StringBuilder filter = new StringBuilder("where 1=1 ");
        if (Objects.nonNull(status)) {
            filter.append("and model.status = ").append(status).append(" ");
        }
        if (Objects.nonNull(tradeStatus)) {
            filter.append("and model.tradeStatus = ").append(tradeStatus).append(" ");
        }

        int count = marketService.count(filter.toString());
        if (count > 0) {
            modelAndView.addObject("list", marketService.list((page - 1) * Constants.PAGE_ITEM_COUNT_20, Constants.PAGE_ITEM_COUNT_20, filter.toString(), true));
        }

        modelAndView.addObject("coinMap", frontVirtualCoinService.listMap());
        modelAndView.addObject("numPerPage", Constants.PAGE_ITEM_COUNT_20);
        modelAndView.addObject("rel", "marketList");
        modelAndView.addObject("currentPage", page);
        modelAndView.addObject("totalCount", count);
        return modelAndView;
    }

    @RequestMapping("ssadmin/goMarketJSP")
    public ModelAndView goAboutJSP(HttpServletRequest request) throws Exception{
        String url = request.getParameter("url");
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName(url) ;
        if(request.getParameter("id") != null){
            int id = Integer.parseInt(request.getParameter("id"));
            Market market = marketService.findById(id);
            modelAndView.addObject("market", market);
        }
        modelAndView.addObject("coinMap", frontVirtualCoinService.listOnSellMap());
        return modelAndView;
    }

    @RequestMapping("ssadmin/createMarket")
//    @RequiresPermissions("ssadmin/createMarket.html")
    public Object createMarket(@RequestParam int buyId,
                               @RequestParam int sellId,
                               @RequestParam(defaultValue = "0") int decimals,
                               @RequestParam(defaultValue = "0") double buyFee,
                               @RequestParam(defaultValue = "0") double sellFee,
                               @RequestParam(defaultValue = "-1") double minCount,
                               @RequestParam(defaultValue = "-1") double maxCount,
                               @RequestParam(defaultValue = "-1") double minPrice,
                               @RequestParam(defaultValue = "-1") double maxPrice,
                               @RequestParam(defaultValue = "-1") double minMoney,
                               @RequestParam(defaultValue = "-1") double maxMoney,
                               @RequestParam String tradeTime,
                               @RequestParam(defaultValue = "0") double updown) {
        ModelAndView modelAndView = new ModelAndView("ssadmin/comm/ajaxDone");
        if (decimals < 4) {
            modelAndView.addObject("statusCode",300);
            modelAndView.addObject("message","保留小数位不能小于4");
            return modelAndView;
        }
        if (buyFee < 0 || buyFee > 100 || sellFee < 0 || sellFee > 100) {
            modelAndView.addObject("statusCode",300);
            modelAndView.addObject("message","手续费必须在0-100之间");
            return modelAndView;
        }

        if (buyId == sellId) {
            modelAndView.addObject("statusCode",300);
            modelAndView.addObject("message","买卖币种不能一样");
            return modelAndView;
        }

        if (!CollectionUtils.isEmpty(marketService.list(0, 1, "where model.buyId=" + buyId + " and model.sellId = " + sellId, true))) {
            modelAndView.addObject("statusCode",300);
            modelAndView.addObject("message","交易市场已存在");
            return modelAndView;
        }

        Market market = new Market();
        market.setBuyId(buyId);
        market.setSellId(sellId);
        market.setDecimals(decimals);
        market.setBuyFee(buyFee / 100);
        market.setSellFee(sellFee / 100);
        market.setMinCount(minCount);
        market.setMaxCount(maxCount);
        market.setMinPrice(minPrice);
        market.setMaxPrice(maxPrice);
        market.setMinMoney(minMoney);
        market.setMaxMoney(maxMoney);
        market.setTradeTime(tradeTime);
        market.setUpdown(updown / 100);
        marketService.save(market);

        modelAndView.addObject("statusCode",200);
        modelAndView.addObject("message","修改成功");
        modelAndView.addObject("callbackType","closeCurrent");
        return modelAndView;
    }

    @RequestMapping("ssadmin/updateMarket")
//    @RequiresPermissions("ssadmin/updateMarket.html")
    public Object updateMarket(@RequestParam int id,
                               @RequestParam(defaultValue = "0") double buyFee,
                               @RequestParam(defaultValue = "0") double sellFee,
                               @RequestParam(defaultValue = "0") double minCount,
                               @RequestParam(defaultValue = "0") double maxCount,
                               @RequestParam(defaultValue = "0") double minPrice,
                               @RequestParam(defaultValue = "0") double maxPrice,
                               @RequestParam(defaultValue = "0") double minMoney,
                               @RequestParam(defaultValue = "0") double maxMoney,
                               @RequestParam String tradeTime,
                               @RequestParam(defaultValue = "0") double updown) {
        ModelAndView modelAndView = new ModelAndView("ssadmin/comm/ajaxDone");
        if (buyFee < 0 || buyFee > 100 || sellFee < 0 || sellFee > 100) {
            modelAndView.addObject("statusCode",300);
            modelAndView.addObject("message","手续费必须在0-100之间");
            return modelAndView;
        }

        Market market = marketService.findById(id);
        market.setBuyFee(buyFee / 100);
        market.setSellFee(sellFee / 100);
        market.setMinCount(minCount);
        market.setMaxCount(maxCount);
        market.setMinPrice(minPrice);
        market.setMaxPrice(maxPrice);
        market.setMinMoney(minMoney);
        market.setMaxMoney(maxMoney);
        market.setTradeTime(tradeTime);
        market.setUpdown(updown / 100);
        marketService.update(market);

        modelAndView.addObject("statusCode",200);
        modelAndView.addObject("message","修改成功");
        modelAndView.addObject("callbackType","closeCurrent");
        return modelAndView;
    }

    /**
     * 启用交易市场
     *
     * @param id
     * @return
     */
    @RequestMapping("ssadmin/startMarket")
//    @RequiresPermissions("ssadmin/startMarket.html")
    public Object startMarket(@RequestParam int id) {
        Market market = marketService.findById(id);
        ModelAndView modelAndView = new ModelAndView("ssadmin/comm/ajaxDone");
        if (Market.STATUS_Normal == market.getStatus()) {
            modelAndView.addObject("statusCode",300);
            modelAndView.addObject("message","启用失败");
            return modelAndView;
        }

        market.setStatus(Market.STATUS_Normal);
        marketService.update(market);
        modelAndView.addObject("statusCode",200);
        modelAndView.addObject("message","启用成功");
        return modelAndView;
    }

    /**
     * 禁用交易市场
     *
     * @param id
     * @return
     */
    @RequestMapping("ssadmin/stopMarket")
//        @RequiresPermissions("ssadmin/stopMarket.html")
    public Object stopMarket(@RequestParam int id) {
        Market market = marketService.findById(id);
        ModelAndView modelAndView = new ModelAndView("ssadmin/comm/ajaxDone");
        if (Market.STATUS_Abnormal == market.getStatus()) {
            modelAndView.addObject("statusCode",300);
            modelAndView.addObject("message","禁用失败");
            return modelAndView;
        }

        market.setStatus(Market.STATUS_Abnormal);
        marketService.update(market);
        modelAndView.addObject("statusCode",200);
        modelAndView.addObject("message","禁用成功");
        return modelAndView;
    }

    /**
     * 启用交易市场交易
     *
     * @param id
     * @return
     */
    @RequestMapping("ssadmin/startMarketTrade")
//    @RequiresPermissions("ssadmin/startMarketTrade.html")
    public Object startMarketTrade(@RequestParam int id) {
        Market market = marketService.findById(id);
        ModelAndView modelAndView = new ModelAndView("ssadmin/comm/ajaxDone");
        if (Market.TRADE_STATUS_Normal == market.getTradeStatus()) {
            modelAndView.addObject("statusCode",300);
            modelAndView.addObject("message","启用失败");
            return modelAndView;
        }

        market.setTradeStatus(Market.TRADE_STATUS_Normal);
        marketService.update(market);
        modelAndView.addObject("statusCode",200);
        modelAndView.addObject("message","启用成功");
        return modelAndView;
    }

    /**
     * 禁用交易市场交易
     *
     * @param id
     * @return
     */
    @RequestMapping("ssadmin/stopMarketTrade")
//    @RequiresPermissions("ssadmin/stopMarketTrade.html")
    public Object stopMarketTrade(@RequestParam int id) {
        Market market = marketService.findById(id);
        ModelAndView modelAndView = new ModelAndView("ssadmin/comm/ajaxDone");
        if (Market.TRADE_STATUS_Abnormal == market.getTradeStatus()) {
            modelAndView.addObject("statusCode",300);
            modelAndView.addObject("message","禁用失败");
            return modelAndView;
        }

        market.setTradeStatus(Market.TRADE_STATUS_Abnormal);
        marketService.update(market);
        modelAndView.addObject("statusCode",200);
        modelAndView.addObject("message","禁用成功");
        return modelAndView;
    }

}




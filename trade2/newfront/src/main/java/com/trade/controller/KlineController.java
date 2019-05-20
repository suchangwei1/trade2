package com.trade.controller;

import com.trade.Enum.EntrustTypeEnum;
import com.trade.auto.KlinePeriodData;
import com.trade.cache.data.RealTimeDataService;
import com.trade.dto.FentrustData;
import com.trade.dto.FentrustlogData;
import com.trade.model.Fvirtualcointype;
import com.trade.service.front.FrontVirtualCoinService;
import com.trade.util.Utils;
import net.sf.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Controller
@RequestMapping("/api/v2/market")
public class KlineController {

    @Autowired
    private RealTimeDataService realTimeDataService;
    @Autowired
    private KlinePeriodData klinePeriodData;
    @Autowired
    private FrontVirtualCoinService frontVirtualCoinService;

    @RequestMapping("/kline")
    public ModelAndView start(@RequestParam(value = "symbol", required = false, defaultValue = "0") int symbol) throws Exception {
        ModelAndView modelAndView = new ModelAndView();
        Fvirtualcointype fvirtualcointype = this.frontVirtualCoinService.findFvirtualCoinById(symbol);

        modelAndView.addObject("fvirtualcointype", fvirtualcointype);
        modelAndView.setViewName("market/start");
        return modelAndView;
    }

    @ResponseBody
    @RequestMapping("/period")
    public String period(@RequestParam(required = true, value = "step") int step, @RequestParam(required = true, value = "symbol") int symbol) throws Exception {
        int key = 0;
        switch (step) {
            case 60:
                key = 0;
                break;
            case 60 * 3:
                key = 1;
                break;
            case 60 * 5:
                key = 2;
                break;
            case 60 * 15:
                key = 3;
                break;
            case 60 * 30:
                key = 4;
                break;
            case 60 * 60:
                key = 5;
                break;
            case 60 * 60 * 2:
                key = 6;
                break;
            case 60 * 60 * 4:
                key = 7;
                break;
            case 60 * 60 * 6:
                key = 8;
                break;
            case 60 * 60 * 12:
                key = 9;
                break;
            case 60 * 60 * 24:
                key = 10;
                break;
            case 60 * 60 * 24 * 3:
                key = 11;
                break;
            case 60 * 60 * 24 * 7:
                key = 12;
                break;
        }
        return this.klinePeriodData.getJsonString(symbol, key);
    }

    @ResponseBody
    @RequestMapping("/depth")
    public String depth(@RequestParam(required = true, value = "symbol") int symbol) throws Exception {
        JSONObject jsonObject = new JSONObject();
        jsonObject.accumulate("result", "success");

        String sell = this.realTimeDataService.getBuyDepthMap(symbol, 4);
        String buy = this.realTimeDataService.getSellDepthMap(symbol, 4);

        JSONObject askBidJson = new JSONObject();
        askBidJson.accumulate("bids", buy);
        askBidJson.accumulate("asks", sell);

        jsonObject.accumulate("return", askBidJson);
        jsonObject.accumulate("now", Utils.getTimestamp().getTime());

        return jsonObject.toString();
    }

    @ResponseBody
    @RequestMapping("/trades")
    public String trades(
            @RequestParam(required = true, value = "symbol") int symbol,
            @RequestParam(required = false, defaultValue = "0", value = "since") int since
    ) throws Exception {
        //成交记录ask/卖/bid买

        Object[] objs = this.realTimeDataService.getEntrustSuccessMap(symbol).toArray();
        StringBuffer sb = new StringBuffer();
        sb.append("[");
        int length = objs.length;
        long last = System.currentTimeMillis();
        for (int i = length - 1; i >= 0; i--) {
            FentrustlogData fentrust = (FentrustlogData) objs[i];
            if ((since == 0 && fentrust.getFcreateTime().getTime() < last)
                    || (since != 0 && since >= fentrust.getFid())
                    ) {
                continue;
            }

            JSONObject jsonObject = new JSONObject();
            jsonObject.accumulate("amount", fentrust.getFcount());
            jsonObject.accumulate("date", fentrust.getFcreateTime().getTime() / 1000L);
            jsonObject.accumulate("price", fentrust.getFprize());
            jsonObject.accumulate("tid", fentrust.getFid());
            jsonObject.accumulate("tradeType", fentrust.getfEntrustType() == EntrustTypeEnum.BUY ? "bid" : "ask");
            sb.append(jsonObject.toString());
            sb.append(",");
        }
        sb.append("]");
        return sb.toString().replace("},]", "}]");
    }


    @ResponseBody
    @RequestMapping("/designperiod")
    public String periodByDesign(
            @RequestParam(required = true, value = "begin") String begin,
            @RequestParam(required = true, value = "end") String end,
            @RequestParam(required = true, value = "step") int step,
            @RequestParam(required = true, value = "symbol") int symbol){

        int key = 0;
        switch (step) {
            case 60:
                key = 0;
                break;
            case 60 * 3:
                key = 1;
                break;
            case 60 * 5:
                key = 2;
                break;
            case 60 * 15:
                key = 3;
                break;
            case 60 * 30:
                key = 4;
                break;
            case 60 * 60:
                key = 5;
                break;
            case 60 * 60 * 2:
                key = 6;
                break;
            case 60 * 60 * 4:
                key = 7;
                break;
            case 60 * 60 * 6:
                key = 8;
                break;
            case 60 * 60 * 12:
                key = 9;
                break;
            case 60 * 60 * 24:
                key = 10;
                break;
            case 60 * 60 * 24 * 3:
                key = 11;
                break;
            case 60 * 60 * 24 * 7:
                key = 12;
                break;
        }
        return this.klinePeriodData.getJsonString(begin, end, symbol, key);

    }


}

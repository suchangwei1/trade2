package com.trade.controller.admin;

import com.alibaba.fastjson.JSON;
import com.trade.Enum.VirtualCoinTypeStatusEnum;
import com.trade.model.DateWalletStatistic;
import com.trade.model.Fvirtualcointype;
import com.trade.service.admin.DateWalletStatisticService;
import com.trade.service.admin.VirtualCoinService;
import com.trade.util.DateUtils;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Controller
@RequestMapping("/ssadmin")
public class StatisticController {
    @Autowired
    private VirtualCoinService virtualCoinService;
    @Autowired
    private DateWalletStatisticService dateWalletStatisticService;

    @RequestMapping("/dateWalletReport")
    @RequiresPermissions("ssadmin/dateWalletReport.html")
    public Object dateWalletReport(ModelMap modelMap, @RequestParam(defaultValue = "0")int coinType,
                                   @RequestParam(required = false)@DateTimeFormat(pattern = "yyyy-MM-dd")Date startDate,
                                   @RequestParam(required = false)@DateTimeFormat(pattern = "yyyy-MM-dd")Date endDate){
        if(null == startDate && null == endDate){
            endDate = DateUtils.formatDate(DateUtils.formatDate(new Date()));
            startDate = DateUtils.getDaysBefore(6);
        }

        List<DateWalletStatistic> list = dateWalletStatisticService.find(coinType, startDate, endDate);

        List dateList = new ArrayList<>(list.size());
        List balanceList = new ArrayList<>(list.size());
        List freezeList = new ArrayList<>(list.size());
        list.forEach(e -> {
            dateList.add(DateUtils.formatDate(e.getDate()));
            balanceList.add(e.getTotalBalance());
            freezeList.add(e.getTotalFreeze());
        });

        modelMap.put("coinType", coinType);
        modelMap.put("startDate", startDate);
        modelMap.put("endDate", endDate);
        modelMap.put("dateList", JSON.toJSONString(dateList));
        modelMap.put("balanceList", JSON.toJSONString(balanceList));
        modelMap.put("freezeList", JSON.toJSONString(freezeList));

        List<Fvirtualcointype> fvirtualcointypes = virtualCoinService.findByProperty("fstatus", VirtualCoinTypeStatusEnum.Normal);
        modelMap.put("types", fvirtualcointypes);

        return "ssadmin/charts/dateWalletReport";
    }

    @RequestMapping("/exportDateWalletReport")
    @RequiresPermissions("ssadmin/exportDateWalletReport.html")
    public Object exportDateWalletReport(ModelMap modelMap, @RequestParam(defaultValue = "0")int coinType,
                                   @RequestParam(required = false)@DateTimeFormat(pattern = "yyyy-MM-dd")Date startDate,
                                   @RequestParam(required = false)@DateTimeFormat(pattern = "yyyy-MM-dd")Date endDate){
        if(null == startDate && null == endDate){
            endDate = DateUtils.formatDate(DateUtils.formatDate(new Date()));
            startDate = DateUtils.getDaysBefore(6);
        }

        List<DateWalletStatistic> list = dateWalletStatisticService.find(coinType, startDate, endDate);

        if(0 == coinType){
            modelMap.put("coinName", "人民币");
        }else{
            Fvirtualcointype fvirtualcointype = virtualCoinService.findById(coinType);
            modelMap.put("coinName", fvirtualcointype.getFname());
        }

        modelMap.put("list", list);
        return "ssadmin/excel/exportDateWalletReport";
    }

}

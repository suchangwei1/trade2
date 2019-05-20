package com.trade.controller.admin;

import com.trade.helper.TurnOverHelp;
import com.trade.model.Fvirtualcointype;
import com.trade.service.admin.EntrustlogService;
import com.trade.service.admin.VirtualCoinService;
import com.trade.util.DateUtils;
import com.trade.util.StringUtils;
import com.trade.util.XlsExport;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

@Controller
@RequestMapping("/ssadmin")
public class TurnOverController {

    @Resource
    private EntrustlogService entrustlogService;
    @Resource
    private VirtualCoinService virtualCoinService;

    @RequestMapping("/turnoverReport")
    @RequiresPermissions("ssadmin/turnoverReport.html")
    public String getUpAndDown(@RequestParam(required = false) String start, @RequestParam(required = false)String end, Map<String, Object> map) throws ParseException {

        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        if(!StringUtils.hasText(start) || !StringUtils.hasText(end)){
            // 默认一个月
            end = DateUtils.formatDate(new Date(), "yyyy-MM-dd");
            Calendar calendar = Calendar.getInstance();
            calendar.add(Calendar.MONTH, -1);
            start = DateUtils.formatDate(calendar.getTime(), "yyyy-MM-dd");
        }


        List<Fvirtualcointype> types = virtualCoinService.findAll();
        List<TurnOverHelp> data = new ArrayList<TurnOverHelp>();
        for (Fvirtualcointype fvirtualcointype:types) {
            Date startTime = null;
            Date endTime = null;
            try {
                startTime = format.parse(start);
                endTime = format.parse(end);
            } catch (ParseException e) {
                e.printStackTrace();
            }
            double result = entrustlogService.getTradeCount(fvirtualcointype.getFid(), startTime, endTime);
            data.add(new TurnOverHelp(fvirtualcointype.getFname(), result));

        }

        data.sort(new Comparator<TurnOverHelp>() {
            @Override
            public int compare(TurnOverHelp o1, TurnOverHelp o2) {
                return Double.valueOf(Math.abs(o2.getAmount())).compareTo(Math.abs(o1.getAmount()));
            }
        });


        map.put("data", data);
        map.put("start", start);
        map.put("end", end);
        return "ssadmin/turnoverReport";
    }

    /**
     * 导出涨跌幅列表
     * @param
     */
    @RequestMapping(value = "/reportturnover", method = RequestMethod.GET)
    @RequiresPermissions("ssadmin/reportturnover.html")
    public void exportExcel(HttpServletResponse response, String start, String end) {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        List<Fvirtualcointype> types = virtualCoinService.findAll();
        List<TurnOverHelp> data = new ArrayList<TurnOverHelp>();
        for (Fvirtualcointype fvirtualcointype : types) {
            Date startTime = null;
            Date endTime = null;
            try {
                startTime = format.parse(start);
                endTime = format.parse(end);
            } catch (ParseException e) {
                e.printStackTrace();
            }
            double result = entrustlogService.getTradeCount(fvirtualcointype.getFid(), startTime, endTime);
            data.add(new TurnOverHelp(fvirtualcointype.getFname(), result));

        }

        data.sort(new Comparator<TurnOverHelp>() {
            @Override
            public int compare(TurnOverHelp o1, TurnOverHelp o2) {
                return Double.valueOf(Math.abs(o2.getAmount())).compareTo(Math.abs(o1.getAmount()));
            }
        });
        XlsExport xls = new XlsExport();

        // 标题

        xls.createRow(0);
        xls.setCell(0, "虚拟币");
        xls.setCell(1, "交易额");

        // 填入数据
        int row = 1;
        for (TurnOverHelp turnOverHelp : data) {
            xls.createRow(row++);
            xls.setCell(0, turnOverHelp.getName());
            xls.setCell(1, turnOverHelp.getAmount());
        }
        try {
            SimpleDateFormat format2 = new SimpleDateFormat("yyyy-MM-dd");
            response.setContentType("application/vnd.ms-excel");
            response.setHeader("Content-Disposition", "attachment;filename=" + URLEncoder.encode("交易额统计表", "utf-8") + format.format(new Date()) + ".xls");
            xls.exportXls(response);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

    }

}


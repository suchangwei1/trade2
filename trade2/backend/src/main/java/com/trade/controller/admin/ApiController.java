package com.trade.controller.admin;

import com.trade.Enum.TradeApiType;
import com.trade.Enum.UseStatus;

import com.trade.model.TradeApi;
import com.trade.service.front.ApiInvokeLogService;
import com.trade.service.front.TradeApiService;
import com.trade.util.Utils;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;

@Controller
public class ApiController extends BaseController {
    @Autowired
    private TradeApiService tradeApiService;
    @Autowired
    private ApiInvokeLogService apiInvokeLogService;

    @RequestMapping("ssadmin/apiList")
    @RequiresPermissions("ssadmin/apiList.html")
    public String apiList(HttpServletRequest request,
                          Map<String, Object> model,
                          @RequestParam(value = "keyword", required = false) String keyword,
                          @RequestParam(value = "type", required = false) Integer type,
                          @RequestParam(value = "status", required = false) Integer status,
                          @RequestParam(value = "pageNum", required = false, defaultValue = "1") int currentPage,
                          @RequestParam(value = "total", required = false, defaultValue = "0") int total,
                          @RequestParam(value = "orderField", required = false, defaultValue = "a.id")String orderField,
                          @RequestParam(value = "orderDirection", required = false, defaultValue = "desc")String orderDirection){

        List<Map<String, Object>> list = tradeApiService.list(keyword, null != type ? TradeApiType.get(type) : null, null != status ? UseStatus.get(status) : null, orderField, orderDirection, (currentPage - 1) * Utils.getNumPerPage(), Utils.getNumPerPage());

        if(0 == total || 1 == currentPage){
            total = tradeApiService.count(keyword, null != type ? TradeApiType.get(type) : null, null != status ? UseStatus.get(status) : null);
        }

        model.put("list", list);
        model.put("keyword", keyword);
        model.put("type", type);
        model.put("status", status);
        model.put("currentPage", currentPage);
        model.put("total", total);
        model.put("orderField", orderField);
        model.put("orderDirection", orderDirection);
        model.put("numPerPage", Utils.getNumPerPage());

        return "ssadmin/apiList";
    }

    @RequestMapping("ssadmin/updateApi")
    @RequiresPermissions(value = {"ssadmin/updateApi.html?status=0", "ssadmin/updateApi.html?status=1"})
    public String update(HttpServletRequest request,
                         Map<String, Object> map,
                         @RequestParam(value = "id")int id,
                         @RequestParam(value = "status")int status){

        TradeApi tradeApi = tradeApiService.findById(id);
        tradeApi.setStatus(UseStatus.get(status));
        tradeApiService.update(tradeApi);

        map.put("statusCode", 200);
        map.put("message", "修改成功");

        return "ssadmin/comm/ajaxDone";
    }

    @RequestMapping("ssadmin/lockAllApi")
    @RequiresPermissions("ssadmin/lockAllApi.html")
    public String lockAll(HttpServletRequest request,
                         Map<String, Object> map,
                         @RequestParam(value = "ids")String ids){

        String[] idArr = ids.split(",");
        for(String id : idArr){
            TradeApi tradeApi = tradeApiService.findById(Integer.valueOf(id));
            if(!UseStatus.Forbidden.equals(tradeApi.getStatus())){
                tradeApi.setStatus(UseStatus.Forbidden);
                tradeApiService.update(tradeApi);
            }
        }

        map.put("statusCode", 200);
        map.put("message", "修改成功");

        return "ssadmin/comm/ajaxDone";
    }

    @RequestMapping("ssadmin/apiRecord")
    @RequiresPermissions("ssadmin/apiRecord.html")
    public String invokeRecord(HttpServletRequest request, Map<String, Object> modelMap,
                               @RequestParam(value = "keyword", required = false)String keyword,
                               @RequestParam(value = "isSucceed", required = false)Integer isSucceed,
                               @RequestParam(value = "pageNum", required = false, defaultValue = "1") int currentPage,
                               @RequestParam(value = "total", required = false, defaultValue = "0") int total){

        List<Map<String, Object>> list = apiInvokeLogService.list(keyword, isSucceed, (currentPage - 1) * Utils.getNumPerPage(), Utils.getNumPerPage());
        if(0 == total || 1 == currentPage){
            total = apiInvokeLogService.count(keyword, isSucceed);
        }

        modelMap.put("list", list);
        modelMap.put("keyword", keyword);
        modelMap.put("isSucceed", isSucceed);
        modelMap.put("currentPage", currentPage);
        modelMap.put("total", total);
        modelMap.put("numPerPage", Utils.getNumPerPage());

        return "ssadmin/apiRecordList";
    }

}

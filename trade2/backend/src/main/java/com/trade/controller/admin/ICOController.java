package com.trade.controller.admin;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.trade.Enum.ICORecordStatusEnum;
import com.trade.Enum.ICOStatusEnum;
import com.trade.dto.FluentHashMap;
import com.trade.model.Fvirtualcointype;
import com.trade.model.ICO;
import com.trade.model.ICORecord;
import com.trade.model.ICOSwapRate;
import com.trade.service.admin.VirtualCoinService;
import com.trade.service.front.ICORecordService;
import com.trade.service.front.ICOService;
import com.trade.service.front.ICOSwapRateService;
import com.trade.util.Constants;
import com.trade.util.HTMLSpirit;
import com.trade.util.Utils;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Author:xuelin
 * Company:招股金服
 * Date:2017/5/23
 * Desc:
 */
@Controller
@RequestMapping("/ssadmin")
public class ICOController extends BaseController {
    @Autowired
    private ICOService icoService;
    @Autowired
    private ICORecordService icoRecordService;
    @Autowired
    private VirtualCoinService virtualCoinService;
    @Autowired
    private ICOSwapRateService icoSwapRateService;

    private Lock refundLock = new ReentrantLock();
    private Lock requiteLock = new ReentrantLock();

    @RequestMapping("/icoList")
    @RequiresPermissions("/ssadmin/icoList.html")
    public Object icoList(ModelMap modelMap, String keyword, Integer id,
                          @DateTimeFormat(pattern = "yyyy-MM-dd") Date startTime,
                          @DateTimeFormat(pattern = "yyyy-MM-dd") Date endTime,
                          @RequestParam(name = "pageNum", defaultValue = "1") int currentPage) {
        List<Fvirtualcointype> coins = virtualCoinService.findAll();
        Map<Long, Fvirtualcointype> coinMap = new HashMap<>();
        for(Fvirtualcointype fvirtualcointype : coins) {
            coinMap.put(Long.valueOf(fvirtualcointype.getFid().toString()), fvirtualcointype);
        }

        List<ICO> list = icoService.find(keyword, id, startTime, endTime, (currentPage - 1) * Constants.PAGE_ITEM_COUNT_40, Constants.PAGE_ITEM_COUNT_40);

        modelMap.put("list", list);
        modelMap.put("numPerPage", Constants.PAGE_ITEM_COUNT_40);
        modelMap.put("currentPage", currentPage);
        modelMap.put("coinMap", coinMap);
        modelMap.put("totalCount", icoService.count(keyword, id, startTime, endTime));
        return "ssadmin/ico/list";
    }

    @RequestMapping("/saveICO")
    @RequiresPermissions("/ssadmin/saveICO.html")
    public Object saveICO(String id,
                          @RequestParam String name,
                          @RequestParam double amount,
                          @RequestParam(required = false, defaultValue = "0") double supplyAmount,
                          @RequestParam(required = false, defaultValue = "0") double rightAmount,
                          @RequestParam(required = false, defaultValue = "0") double limitAmount,
                          @RequestParam(required = false, defaultValue = "0") double minBuyAmount,
                          @RequestParam(required = false, defaultValue = "0") double requiteRate,
                          @RequestParam(required = false, defaultValue = "0") int supportCount,
                          @RequestParam(required = false, defaultValue = "0") int status,
                          @RequestParam MultipartFile image,
                          @RequestParam String declaration,
                          @RequestParam String description,
                          @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") Date startTime,
                          @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") Date endTime,
                          ModelMap modelMap) {
        ICO ico;
        if(StringUtils.hasText(id)) {
            ico = icoService.findById(Integer.valueOf(id));
        } else {
            ico = new ICO();
        }

        ico.setName(name.trim());
        ico.setAmount(Math.abs(amount));
        ico.setSupplyAmount(Math.abs(supplyAmount));
        ico.setRightAmount(Math.abs(rightAmount));
        ico.setLimitAmount(Math.abs(limitAmount));
        ico.setMinBuyAmount(Math.abs(minBuyAmount));
        ico.setRequiteRate(Math.abs(requiteRate));
        ico.setSupportCount(supportCount);
        ico.setDeclaration(HTMLSpirit.delHTMLTag(declaration.trim()));
        ico.setDescription(description.trim());
        ico.setStatus(ICOStatusEnum.valueOf(status));
        ico.setStartTime(startTime);
        ico.setEndTime(endTime);

        // 上传图片
        if(image != null && !image.isEmpty()){
            try {
                byte[] bytes = image.getBytes();
                if(!Utils.isImage(bytes)){
                    // 不是有效图片文件
                    modelMap.put("statusCode",300);
                    modelMap.put("message","非法图片");
                    return "ssadmin/comm/ajaxDone";
                }
                String ext = com.trade.util.StringUtils.getFilenameExtension(image.getOriginalFilename());
                String filePath = Constants.AdminUploadInformationDirectory + Utils.getRelativeFilePath(ext, bytes);
                boolean flag = Utils.uploadFileToOss(image.getInputStream(), filePath);
                if(flag){
                    ico.setImageUrl(filePath);
                }else{
                    modelMap.put("statusCode",300);
                    modelMap.put("message","上传图片失败");
                    return "ssadmin/comm/ajaxDone";
                }
            }catch (IOException e){
                e.printStackTrace();
            }
        }

        if(StringUtils.hasText(id)) {
            icoService.update(ico);
        } else {
            icoService.save(ico);
        }

        modelMap.put("statusCode", 200);
        modelMap.put("callbackType", "closeCurrent");
        modelMap.put("message", "保存成功");
        return "ssadmin/comm/ajaxDone";
    }

    @RequestMapping("/removeICO")
    @RequiresPermissions("/ssadmin/removeICO.html")
    public Object removeICO(int id, ModelMap modelMap) {
        ICO ico = icoService.findById(id);
        ico.setDelete(true);
        icoService.update(ico);
        modelMap.put("statusCode", 200);
        modelMap.put("message", "删除成功");
        return "ssadmin/comm/ajaxDone";
    }

    @RequestMapping("/goICOJSP")
    public ModelAndView goICOJSP(HttpServletRequest request) throws Exception {
        String url = request.getParameter("url");
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName(url);
        if (request.getParameter("uid") != null) {
            int id = Integer.parseInt(request.getParameter("uid"));
            ICO ico = icoService.findById(id);
            modelAndView.addObject("ico", ico);
        }
        return modelAndView;
    }

    @RequestMapping("/icoRecordList")
    @RequiresPermissions("/ssadmin/icoRecordList.html")
    public Object recordList(ModelMap modelMap,
                             String keyword, Integer userId, Integer icoId, Integer coinType, Integer status,
                             @RequestParam(name = "pageNum", defaultValue = "1") int currentPage) {
        List<Fvirtualcointype> coins = virtualCoinService.findAll();
        Map<Long, Fvirtualcointype> coinMap = new HashMap<>();
        for(Fvirtualcointype fvirtualcointype : coins) {
            coinMap.put(Long.valueOf(fvirtualcointype.getFid().toString()), fvirtualcointype);
        }
        ICORecordStatusEnum statusEnum = null;
        if(Objects.nonNull(status)) {
            statusEnum = ICORecordStatusEnum.valueOf(status);
        }
        List<Map> list = icoRecordService.find(keyword, userId, icoId, coinType, statusEnum, (currentPage - 1) * Constants.PAGE_ITEM_COUNT_40, Constants.PAGE_ITEM_COUNT_40);

        modelMap.put("list", list);
        modelMap.put("numPerPage", Constants.PAGE_ITEM_COUNT_40);
        modelMap.put("currentPage", currentPage);
        modelMap.put("coinMap", coinMap);
        modelMap.put("totalCount", icoRecordService.count(keyword, userId, icoId, coinType, statusEnum));
        return "ssadmin/ico/recordList";
    }

    @RequestMapping("/icoSwapRateList")
    @RequiresPermissions("/ssadmin/icoSwapRateList.html")
    public Object swapRateList(ModelMap modelMap, String keyword, Integer icoId, Integer coinType,
                               @RequestParam(name = "pageNum", defaultValue = "1") int currentPage) {
        List<Fvirtualcointype> coins = virtualCoinService.findAll();
        Map<Long, Fvirtualcointype> coinMap = new HashMap<>();
        for(Fvirtualcointype fvirtualcointype : coins) {
            coinMap.put(Long.valueOf(fvirtualcointype.getFid().toString()), fvirtualcointype);
        }
        List<Map> list = icoSwapRateService.find(keyword, icoId, coinType, (currentPage - 1) * Constants.PAGE_ITEM_COUNT_40, Constants.PAGE_ITEM_COUNT_40);

        modelMap.put("list", list);
        modelMap.put("numPerPage", Constants.PAGE_ITEM_COUNT_40);
        modelMap.put("currentPage", currentPage);
        modelMap.put("coinMap", coinMap);
        modelMap.put("totalCount", icoSwapRateService.count(keyword, icoId, coinType));
        return "ssadmin/ico/swapRateList";
    }

    @RequestMapping("/saveICOSwapRate")
    @RequiresPermissions("/ssadmin/saveICOSwapRate.html")
    public Object saveICOSwapRate(ModelMap modelMap, Integer id,
                                  @RequestParam int coinType,
                                  @RequestParam double amount,
                                  @RequestParam(name = "orgLookup.id") int icoId,
                                  @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") Date startTime,
                                  @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") Date endTime) {
        ICOSwapRate swapRate;
        if(Objects.nonNull(id)) {
            swapRate = icoSwapRateService.findById(Integer.valueOf(id));
        } else {
            swapRate = new ICOSwapRate();
            swapRate.setIcoId(icoId);
            swapRate.setCoinType(coinType);
        }

        swapRate.setAmount(amount);
        swapRate.setStartTime(startTime);
        swapRate.setEndTime(endTime);

        if(Objects.nonNull(id)) {
            icoSwapRateService.update(swapRate);
        } else {
            icoSwapRateService.save(swapRate);
        }

        // 更新ico统计
        ICO ico = icoService.findById(swapRate.getIcoId());
        JSONObject jsonExt = JSON.parseObject(ico.getJsonExt());
        // 币已ico数量
        JSONObject coinSumMap = jsonExt.getJSONObject("coinSumMap");
        if(Objects.isNull(coinSumMap)) {
            coinSumMap = new JSONObject();
            jsonExt.put("coinSumMap", coinSumMap);
        }

        // 统计ico数量
        String key = "CNY";
        if(!swapRate.isUseRMB()) {
            Fvirtualcointype fvirtualcointype = virtualCoinService.findById(swapRate.getCoinType());
            key = fvirtualcointype.getfShortName().toUpperCase();
        }
        if (!coinSumMap.containsKey(key)) {
            coinSumMap.put(key, 0);
            ico.setJsonExt(jsonExt.toJSONString());
            icoService.update(ico);
        }

        modelMap.put("statusCode", 200);
        modelMap.put("callbackType", "closeCurrent");
        modelMap.put("message", "保存成功");
        return "ssadmin/comm/ajaxDone";
    }

    @RequestMapping("/removeSwapRate")
    @RequiresPermissions("/ssadmin/removeSwapRate.html")
    public Object removeSwapRate(int id, ModelMap modelMap) {
        ICOSwapRate swapRate = icoSwapRateService.findById(id);
        if(Objects.isNull(swapRate)) {
            modelMap.put("statusCode", 300);
            modelMap.put("message", "记录不存在");
            return "ssadmin/comm/ajaxDone";
        }

        icoSwapRateService.delete(swapRate);
        modelMap.put("statusCode", 200);
        modelMap.put("message", "删除成功");
        return "ssadmin/comm/ajaxDone";
    }

    @RequestMapping("/icoRequiteAll")
    @RequiresPermissions("ssadmin/icoRequiteAll.html")
    public Object requiteAll(@RequestParam int icoId, @RequestParam int coinType) {
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("ssadmin/comm/ajaxDone") ;

        Date curTime = new Date();
        ICO ico = icoService.findById(icoId);
        if(!curTime.after(ico.getEndTime()) || !ico.isSuccess()) {
            modelAndView.addObject("statusCode",300);
            modelAndView.addObject("message","项目未完成或结束");
            return modelAndView;
        }

        if(ico.getRequiteRate() <= 0) {
            modelAndView.addObject("statusCode",300);
            modelAndView.addObject("message","发放回报比例不正确");
            return modelAndView;
        }

        int errorNum = 0;

        try {
            if (requiteLock.tryLock(5000, TimeUnit.SECONDS)) {
                int offset = 0;
                int length = 500;
                List<ICORecord> list;
                do {
                    list = icoRecordService.findByProperties(new FluentHashMap().fluentPut("icoId", icoId), offset, length);

                    for (ICORecord icoRecord : list) {
                        try {
                            icoRecordService.updateRequite(icoRecord, ico, coinType);
                        } catch (Exception e) {
                            e.printStackTrace();
                            errorNum ++;
                        }
                    }

                    offset += length;
                } while (length == list.size());
            } else {
                modelAndView.addObject("statusCode",300);
                modelAndView.addObject("message","项目发放回报中");
                return modelAndView;
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
            modelAndView.addObject("statusCode",300);
            modelAndView.addObject("message","操作失败");
            return modelAndView;
        } finally {
            requiteLock.unlock();
        }

        if(errorNum > 0) {
            modelAndView.addObject("statusCode",300);
            modelAndView.addObject("message","项目发放回报失败" + errorNum + "条");
            return modelAndView;
        }

        modelAndView.addObject("statusCode",200);
        modelAndView.addObject("callbackType", "closeCurrent");
        modelAndView.addObject("message","项目发放回报成功");
        return modelAndView;
    }

    @RequestMapping("/icoRefundAll")
    @RequiresPermissions("ssadmin/icoRefundAll.html")
    public Object refundAll(@RequestParam(name = "id") int icoId) {
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("ssadmin/comm/ajaxDone") ;

        Date curTime = new Date();
        ICO ico = icoService.findById(icoId);
        if(!curTime.after(ico.getEndTime()) || ico.isSuccess()) {
            modelAndView.addObject("statusCode",300);
            modelAndView.addObject("message","项目未结束或已完成");
            return modelAndView;
        }

        int errorNum = 0;

        try {
            if(refundLock.tryLock(5000, TimeUnit.SECONDS)) {
                int offset = 0;
                int length = 500;
                List<ICORecord> list;
                do {
                    list = icoRecordService.findByProperties(new FluentHashMap().fluentPut("icoId", icoId), offset, length);

                    for (ICORecord icoRecord : list) {
                        try {
                            icoRecordService.updateRefund(icoRecord, "系统退款");
                        } catch (Exception e) {
                            e.printStackTrace();
                            errorNum ++;
                        }
                    }

                    offset += length;
                } while (length == list.size());
            } else {
                modelAndView.addObject("statusCode",300);
                modelAndView.addObject("message","项目退款中");
                return modelAndView;
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
            modelAndView.addObject("statusCode",300);
            modelAndView.addObject("message","操作失败");
            return modelAndView;
        } finally {
            refundLock.unlock();
        }

        if(errorNum > 0) {
            modelAndView.addObject("statusCode",300);
            modelAndView.addObject("message","项目退款失败" + errorNum + "条");
            return modelAndView;
        }

        ico.setStatus(ICOStatusEnum.FAILURE);
        icoService.update(ico);
        modelAndView.addObject("statusCode",200);
        modelAndView.addObject("message","项目退款成功");
        return modelAndView;
    }

    @RequestMapping("/icoRefund")
    @RequiresPermissions("ssadmin/icoRefund.html")
    public Object refund(@RequestParam(name = "id") int recordId, @RequestParam(required = false, defaultValue = "系统退款") String remark) {
        ICORecord icoRecord = icoRecordService.findById(recordId);
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("ssadmin/comm/ajaxDone");
        if(ICORecordStatusEnum.SUCCESS.getIndex() != icoRecord.getStatus().getIndex()) {
            modelAndView.addObject("statusCode",300);
            modelAndView.addObject("message","只有认购成功，等待回报的记录的才能退款");
            return modelAndView;
        }
        icoRecordService.updateRefund(icoRecord, remark);
        modelAndView.addObject("statusCode",200);
        modelAndView.addObject("message","退款成功");
        return modelAndView;
    }

    @RequestMapping("/icoRequite")
    @RequiresPermissions("ssadmin/icoRequite.html")
    public Object requite(@RequestParam int recordId, @RequestParam int coinType) {
        ICORecord icoRecord = icoRecordService.findById(recordId);
        ICO ico = icoService.findById(icoRecord.getIcoId());

        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("ssadmin/comm/ajaxDone");

        if(ICORecordStatusEnum.SUCCESS.getIndex() != icoRecord.getStatus().getIndex()) {
            modelAndView.addObject("statusCode",300);
            modelAndView.addObject("message","只有认购成功，等待回报的记录的才能发放回报");
            return modelAndView;
        }

        if(!Utils.getTimestamp().after(ico.getEndTime()) && !ico.isSuccess()) {
            modelAndView.addObject("statusCode",300);
            modelAndView.addObject("message","项目未完成或结束");
            return modelAndView;
        }

        if(ico.getRequiteRate() <= 0) {
            modelAndView.addObject("statusCode",300);
            modelAndView.addObject("message","发放回报比例不正确");
            return modelAndView;
        }

        icoRecordService.updateRequite(icoRecord, ico, coinType);
        modelAndView.addObject("statusCode",200);
        modelAndView.addObject("callbackType", "closeCurrent");
        modelAndView.addObject("message","发放回报成功");
        return modelAndView;
    }

    @RequestMapping("/goICOSwapJSP")
    public ModelAndView goICOSwapJSP(HttpServletRequest request) throws Exception {
        String url = request.getParameter("url");
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName(url);
        if (request.getParameter("id") != null) {
            int id = Integer.parseInt(request.getParameter("id"));
            modelAndView.addObject("swapRate", icoSwapRateService.findById(id));
        }
        modelAndView.addObject("coins", virtualCoinService.findAll());
        return modelAndView;
    }

    @RequestMapping("/goICORequiteJSP")
    public ModelAndView goICORequiteJSP(HttpServletRequest request) throws Exception {
        String url = request.getParameter("url");
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName(url);
        Integer icoId = 0;
        if (request.getParameter("icoId") != null) {
            icoId = Integer.parseInt(request.getParameter("icoId"));
            modelAndView.addObject("ico", icoService.findById(icoId));
        }
        if (request.getParameter("recordId") != null) {
            int recordId = Integer.parseInt(request.getParameter("recordId"));
            ICORecord icoRecord = icoRecordService.findById(recordId);
            icoId = icoRecord.getIcoId();
            modelAndView.addObject("ico", icoService.findById(icoRecord.getIcoId()));
        }

        List<ICOSwapRate> list = icoSwapRateService.findSupportCoin(icoId);
        List<String> amountList = new ArrayList<>(list.size());
        for (ICOSwapRate icoSwapRate : list) {
            double amount = icoRecordService.sumSwapAmount(icoId, icoSwapRate.getCoinType());
            if (icoSwapRate.isUseRMB()) {
                amountList.add(Utils.getDouble(amount, 4) + "CNY");
            } else {
                Fvirtualcointype fvirtualcointype = virtualCoinService.findById(icoSwapRate.getCoinType());
                amountList.add(Utils.getDouble(amount, 4) + fvirtualcointype.getfShortName().toUpperCase());
            }
        }

        modelAndView.addObject("actualAmount", icoRecordService.sumAmount(icoId));
        modelAndView.addObject("actualSwapAmount", StringUtils.collectionToDelimitedString(amountList, " | "));
        modelAndView.addObject("coins", virtualCoinService.findAll());
        return modelAndView;
    }

}













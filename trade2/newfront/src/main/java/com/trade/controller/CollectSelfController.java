package com.trade.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.trade.Enum.EntrustStatusEnum;
import com.trade.auto.OneDayData;
import com.trade.cache.data.KlineDataService;
import com.trade.cache.data.RealTimeDataService;
import com.trade.comm.ConstantMap;
import com.trade.dto.FentrustlogData;
import com.trade.dto.LatestDealData;
import com.trade.dto.ResultBean;
import com.trade.model.*;
import com.trade.service.admin.*;
import com.trade.service.front.*;
import com.trade.util.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;
import redis.clients.jedis.JedisPool;

import javax.servlet.http.HttpServletRequest;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

@Controller
@RequestMapping("/api/v2/collectSelf")
public class CollectSelfController extends BaseController {
    private final Logger log = LoggerFactory.getLogger(CollectSelfController.class);
    @Autowired
    private JedisPool jedisPool;
    @Autowired
    private RealTimeDataService realTimeDataService;
    @Autowired
    private FrontVirtualCoinService frontVirtualCoinService;
    @Autowired
    private FuserCointypeService fuserCointypeService;
    private static final int NUMBER_PER_PAGE = 10;

    /**
     * 查看自选的交易对
     * @param newTradePrice
     * @param dayDown
     * @return
     */
    @ResponseBody
    @RequestMapping("/list")
    public Object allcoinsComplete(@RequestParam(name="newTradePrice",required=false,defaultValue ="0") Integer newTradePrice,@RequestParam(name="dayDown",required=false,defaultValue ="0") Integer dayDown) {
        log.info("*****newTradePrice: "+newTradePrice  + "*****dayDown:" +dayDown);
        Fuser fuser = getFuser();
        if(fuser==null){
            return forFailureResult(401);
        }
        try {
            List<LatestDealData> vlist = realTimeDataService.getLatestDealDataList();
            List<FuserCointype> collectList = fuserCointypeService.findByUser(fuser.getFid(), 0, false);
            //深拷贝后的集合
            List<LatestDealData> immutableList = Utils.deepCopy(vlist);
            //属于USDP的map
            Map<String,Double> groupMap=new HashMap<String,Double>();
            String base = "USDP";
            immutableList.forEach(latestDealData -> {
                if(base.equals(latestDealData.getGroup())){
                    groupMap.put(latestDealData.getfShortName(), latestDealData.getLastDealPrize()*7);

                }
            });
            //选择了交易对集合
            List<LatestDealData> collectSelfList = new ArrayList<LatestDealData>();
            if (collectList != null && collectList.size() != 0) {
                for (LatestDealData latestDealData : immutableList) {

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
                    for (FuserCointype fuserCointype : collectList) {
                        //如果用户已经选择了交易对 ，则返回
                        if (latestDealData.getFid() == fuserCointype.getMarket().getId()) {
                            latestDealData.setIsCollection(Constants.collectSelf);
                            collectSelfList.add(latestDealData);
                        }
                    }
                }

            }
            //最新成交价
            collectSelfList.sort((f1, f2) -> {
                if(newTradePrice!=null){
                    //升序
                    if(newTradePrice==1){
                        return Double.valueOf(f1.getFupanddown()).compareTo(Double.valueOf(f2.getFupanddown()));
                    }
                    return Double.valueOf(f2.getFupanddown()).compareTo(Double.valueOf(f1.getFupanddown()));
                }else if(dayDown!=null){
                    return Double.valueOf(f1.getLastDealPrize()).compareTo(Double.valueOf(f2.getLastDealPrize()));
                }else{
                    return Double.valueOf(f2.getLastDealPrize()).compareTo(Double.valueOf(f1.getLastDealPrize()));
                }

            });
            return forSuccessResult(collectSelfList);
        }catch (Exception e){
            e.printStackTrace();
            return forFailureResult(1,"操作失败");
        }


    }

    /**
     * 取消或收藏交易对
     * @param type  交易对id
     * @param iscollect  1:收藏   0：不收藏
     * @return
     */
    @RequestMapping(value = "/saveAndUpdate")
    @ResponseBody
    public Object consleCollectSelf(@RequestParam(name="typeId",required=true)Integer typeId,
                                    @RequestParam(name="iscollect",required=true)Integer iscollect ) {
        log.info("*****typeId: "+typeId  + "*****iscollect:" +iscollect);
        Fuser fuser=getFuser();
        if(fuser==null){
            return forFailureResult(401);
        }
        try {
            FuserCointype fuserCointype=fuserCointypeService.findByUserIdAndTypeId(fuser.getFid(),typeId,0,false);
            if(fuserCointype==null){

                fuserCointype=new FuserCointype();
                Market market=new Market();
                market.setId(typeId);
                fuserCointype.setFstatus(Constants.collectSelf);
                fuserCointype.setFcreateTime(Utils.getTimestamp());
                fuserCointype.setFuser(fuser);
                fuserCointype.setMarket(market);
                fuserCointypeService.save(fuserCointype);
                return forSuccessResult();
            }else{

                fuserCointype.setFstatus(iscollect);
                fuserCointype.setFlastUpdatTime(Utils.getTimestamp());
                fuserCointypeService.updateFuserCointype(fuserCointype);
                return forSuccessResult();
            }
        }catch (Exception e){
          e.printStackTrace();
            return forFailureResult(1,"操作失败");
        }


    }

}

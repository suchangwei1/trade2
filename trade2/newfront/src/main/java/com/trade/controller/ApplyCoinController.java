package com.trade.controller;

import com.trade.cache.data.RealTimeDataService;
import com.trade.dto.LatestDealData;
import com.trade.model.ApplyCoin;
import com.trade.model.Fuser;
import com.trade.model.FuserCointype;
import com.trade.model.Market;
import com.trade.service.front.ApplyCoinService;
import com.trade.service.front.FrontVirtualCoinService;
import com.trade.service.front.FuserCointypeService;
import com.trade.util.Constants;
import com.trade.util.Utils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import redis.clients.jedis.JedisPool;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/api/v2/ApplyCoin")
public class ApplyCoinController extends BaseController {
    private final Logger log = LoggerFactory.getLogger(ApplyCoinController.class);
    @Autowired
    private ApplyCoinService   applyCoinService;



    /**
     * 保存申请上币对象
     *
     * @return
     */
    @RequestMapping(value = "/save",method = RequestMethod.POST,produces="text/html;charset=UTF-8")
    @ResponseBody
    public Object consleCollectSelf( ApplyCoin applyCoin) {

        Fuser fuser=getFuser();
        if(fuser==null){
            return forFailureResult(401);
        }
        //
        try {

            applyCoin.setFcreateTime(Utils.getTimestamp());
            applyCoin.setFuser(fuser);
            applyCoinService.save(applyCoin);

        }catch (Exception e){
          e.printStackTrace();
            return forFailureResult(1,"操作失败");
        }
        return forSuccessResult();

    }

}

package com.trade.controller;

import com.trade.Enum.TradeApiType;
import com.trade.model.Fuser;
import com.trade.model.TradeApi;
import com.trade.service.front.FrontUserService;
import com.trade.service.front.TradeApiService;
import com.trade.util.Utils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

@RestController
@RequestMapping("/api")
public class ApiController extends BaseController {
    @Autowired
    private TradeApiService tradeApiService;
    @Autowired
    private FrontUserService frontUserService;

    // 最多5个api
    private final static int API_NUM = 5;

    @RequestMapping("/v1/account/api_list")
    public Object list(){
        Fuser fuser = getFuser();

        List<TradeApi> apis = tradeApiService.listByUser(fuser.getFid());
        HashMap map = null;
        List list = new ArrayList(apis.size());
        for (TradeApi api : apis) {
            if (Objects.isNull(map)) {
                map = new HashMap();
            } else {
                map = (HashMap) map.clone();
            }

            map.put("id", api.getId());
            map.put("name", api.getName());
            map.put("key", api.getApiKey());
            map.put("status", api.getStatus().getIndex());
            map.put("type", api.getType().getIndex());
            map.put("create_time", api.getCreateTime());
            map.put("update_time", api.getUpdateTime());
            list.add(map);
        }

        return forSuccessResult(list);
    }

    /**
     * api申请
     *
     * @return
     */
    @RequestMapping(value = "/v1/account/apply_api", method = RequestMethod.POST)
    public Object apply(@RequestParam(value = "name") String name,
                        @RequestParam(value = "type") int type,
                        @RequestParam(value = "password") String password){
        TradeApiType apiType = TradeApiType.get(type);
        if(Objects.isNull(apiType)){
            return forFailureResult(101);
        }

        Fuser fuser = getFuser();
        if(!Utils.MD5(password).equals(fuser.getFtradePassword())){
            // 交易密码不正确
            return forFailureResult(103);
        }

        if(tradeApiService.countByUser(fuser.getFid()) >= API_NUM){
            // api分配过多
            return forFailureResult(105);
        }

        TradeApi tradeApi = new TradeApi();
        tradeApi.setUserId(fuser.getFid());
        tradeApi.setName(name);
        tradeApi.setType(apiType);
        tradeApiService.insertApplyApi(tradeApi);

        Map<String, Object> retMap = new HashMap<>();
        retMap.put("id", tradeApi.getId());
        retMap.put("name", tradeApi.getName());
        retMap.put("key", tradeApi.getApiKey());
        retMap.put("secret", tradeApi.getSecret());
        retMap.put("status", tradeApi.getStatus().getIndex());
        retMap.put("type", tradeApi.getType().getIndex());
        retMap.put("create_time", tradeApi.getCreateTime());
        return forSuccessResult(retMap);
    }

    /**
     * api更新
     * @param type
     * @param password
     * @return
     */
    @RequestMapping(value = "/v1/account/update_api", method = RequestMethod.POST)
    public Object refreshApi(@RequestParam(value = "id") int id,
                             @RequestParam(value = "type") int type,
                             @RequestParam(value = "password") String password){
        TradeApiType apiType = TradeApiType.get(type);
        if(Objects.isNull(apiType)){
            return forFailureResult(101);
        }
        Fuser fuser = getFuser();
        if(!Utils.MD5(password).equals(fuser.getFtradePassword())){
            // 交易密码不正确
            return forFailureResult(103);
        }

        TradeApi tradeApi = tradeApiService.findById(id);
        if(tradeApi.getUserId() != fuser.getFid()){
            // 非法操作
            return forFailureResult(105);
        }

        tradeApi.setType(apiType);
        tradeApiService.update(tradeApi);

        Map<String, Object> retMap = new HashMap<>();
        retMap.put("id", tradeApi.getId());
        retMap.put("name", tradeApi.getName());
        retMap.put("status", tradeApi.getStatus().getIndex());
        retMap.put("type", tradeApi.getType().getIndex());
        retMap.put("create_time", tradeApi.getCreateTime());
        retMap.put("update_time", tradeApi.getUpdateTime());
        return forSuccessResult(retMap);
    }

    /**
     * 刷新api （key secret）
     *
     * @param id
     * @param password
     * @return
     */
    @RequestMapping(value = "/v1/account/refresh_api")
    public Object refresh(@RequestParam(value = "id") int id,
                          @RequestParam(value = "password") String password){
        Fuser fuser = getFuser();
        fuser = frontUserService.findById(fuser.getFid());
        if(!Utils.MD5(password).equals(fuser.getFtradePassword())){
            // 交易密码不正确
            return forFailureResult(101);
        }

        TradeApi tradeApi = tradeApiService.findById(id);
        if(tradeApi.getUserId() != fuser.getFid()){
            // 非法操作
            return forFailureResult(103);
        }

        tradeApiService.updateRefreshApi(tradeApi);

        Map<String, Object> retMap = new HashMap<>();
        retMap.put("id", tradeApi.getId());
        retMap.put("name", tradeApi.getName());
        retMap.put("key", tradeApi.getApiKey());
        retMap.put("secret", tradeApi.getSecret());
        retMap.put("status", tradeApi.getStatus().getIndex());
        retMap.put("type", tradeApi.getType().getIndex());
        retMap.put("create_time", tradeApi.getCreateTime());
        retMap.put("update_time", tradeApi.getUpdateTime());
        return forSuccessResult(retMap);
    }

    /**
     * 查看api密钥
     *
     * @param id
     * @param password
     * @return
     */
    @RequestMapping(value = "/v1/account/view_api")
    public Object view(@RequestParam(value = "id") int id,
                       @RequestParam(value = "password") String password){
        Fuser fuser = getFuser();
        fuser = frontUserService.findById(fuser.getFid());
        if(!Utils.MD5(password).equals(fuser.getFtradePassword())){
            // 交易密码不正确
            return forFailureResult(101);
        }

        TradeApi tradeApi = tradeApiService.findById(id);
        if(tradeApi.getUserId() != fuser.getFid()){
            // 非法操作
            return forFailureResult(103);
        }

        Map<String, Object> retMap = new HashMap<>();
        retMap.put("key", tradeApi.getApiKey());
        retMap.put("secret", tradeApi.getSecret());
        return forSuccessResult(retMap);
    }

    @RequestMapping(value = "/v1/account/delete_api", method = RequestMethod.POST)
    public Object remove(@RequestParam int id, @RequestParam String password) {
        Fuser fuser = getFuser();
        fuser = frontUserService.findById(fuser.getFid());
        if(!Utils.MD5(password).equals(fuser.getFtradePassword())){
            // 交易密码不正确
            return forFailureResult(101);
        }

        TradeApi tradeApi = tradeApiService.findById(id);
        if(tradeApi.getUserId() != fuser.getFid()){
            // 非法操作
            return forFailureResult(103);
        }

        tradeApiService.delete(tradeApi);
        return forSuccessResult();
    }
}

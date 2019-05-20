package com.trade.service.front;

import com.trade.Enum.TradeApiType;
import com.trade.Enum.UseStatus;
import com.trade.dao.TradeApiDao;
import com.trade.model.TradeApi;
import com.trade.util.Utils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class TradeApiService {
    @Autowired
    private TradeApiDao tradeApiDao;

    /**
     * 新增api
     *
     * @param tradeApi
     */
    public void insertApplyApi(TradeApi tradeApi){
        tradeApi.setCreateTime(Utils.getTimestamp());
        tradeApi.setUpdateTime(Utils.getTimestamp());
        tradeApi.setStatus(UseStatus.Normal);
        tradeApi.setApiKey(newApiKey(tradeApi.getType()));
        tradeApi.setSecret(newApiSecret());
        tradeApiDao.save(tradeApi);
    }

    protected String newApiSecret(){
        return Utils.MD5(Utils.UUID());
    }

    protected String newApiKey(TradeApiType tradeApiType){
        return tradeApiType.getPrefix() + Utils.UUID().replace("-", "");
    }

    protected void replaceKeyPrefix(TradeApi tradeApi){
        String apiKey = tradeApi.getApiKey();
        apiKey = apiKey.replaceFirst(apiKey.charAt(0) + "", tradeApi.getType().getPrefix() + "");
        tradeApi.setApiKey(apiKey);
    }

    public TradeApi findById(int id){
        return tradeApiDao.findById(id);
    }

    public void delete(TradeApi tradeApi){
        tradeApiDao.delete(tradeApi);
    }

    /**
     * 刷新api
     *
     * @param tradeApi
     */
    public void updateRefreshApi(TradeApi tradeApi){
        tradeApi.setSecret(newApiSecret());
        tradeApi.setUpdateTime(Utils.getTimestamp());
        tradeApiDao.update(tradeApi);
    }

    public void update(TradeApi tradeApi){
        replaceKeyPrefix(tradeApi);
        tradeApi.setUpdateTime(Utils.getTimestamp());
        tradeApiDao.update(tradeApi);
    }

    public List<TradeApi> listByUser(int userId){
        return tradeApiDao.listByUser(userId);
    }

    public TradeApi findByKey(String apiKey){
        return tradeApiDao.findByKey(apiKey);
    }

    public TradeApi findCacheByKey(String apiKey){
        return tradeApiDao.findByKey(apiKey);
    }

    public int countByUser(int userId){
        return tradeApiDao.countByUser(userId);
    }

    public boolean apiIsExist(int userId, TradeApiType type){
        return tradeApiDao.countByUserAndType(userId, type) > 0;
    }

    public List<TradeApi> list(int firstResult, int maxResults, String filter, boolean isFY){
        return tradeApiDao.list(firstResult, maxResults, filter, isFY);
    }
    public List<Map<String, Object>> list(String keyword, TradeApiType type, UseStatus status, String order, String desc, int firstResult, int maxResults){
        return tradeApiDao.list(keyword, type, status, order, desc, firstResult, maxResults);
    }
    public int count(String keyword, TradeApiType type, UseStatus status) {
        return tradeApiDao.count(keyword, type, status);
    }

}

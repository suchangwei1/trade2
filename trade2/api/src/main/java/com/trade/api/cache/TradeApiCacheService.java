package com.trade.api.cache;

import com.trade.model.TradeApi;
import com.trade.service.front.TradeApiService;
import org.springframework.beans.factory.annotation.Autowired;

public class TradeApiCacheService {
    @Autowired
    private TradeApiService tradeApiService;

    public TradeApi getTradeApi(String key) {
        return tradeApiService.findByKey(key);
    }
}

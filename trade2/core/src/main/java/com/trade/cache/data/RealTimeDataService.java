package com.trade.cache.data;

public interface RealTimeDataService extends RealTimeEntrustDepthService,
        RealTimeEntrustService,
        RealTimePriceService,
        RealTimeDealDataService {

    String getWeChatAccessToken();
}

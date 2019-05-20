package com.trade.jobs.kline;

import java.util.List;

public interface CacheDataService {

    void updateLatestDealData(LatestDealData data);

    void updateLatestDealDataInfo(LatestDealData latestDealData);

    void addLatestDealData(LatestDealData latestDealData);

    LatestDealData getLatestDealData(int fid);

    List<LatestDealData> getLatestDealData();

    void removeLatestDealData(LatestDealData latestDealData);
}

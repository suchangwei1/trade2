package com.trade.deal.market;

import com.trade.deal.model.FentrustlogData;
import com.trade.deal.model.LatestDealData;

public interface CacheDataService {

    void addFentrustLogData(FentrustlogData data);

    void clearFentrustLogData(int fviFid);

    void updateMarking(int fid, double dealPrice, double highestBuy, double lowestDell);

    void updateLatestDealData(LatestDealData data);

    void updateLatestDealDataInfo(LatestDealData latestDealData);

    void addLatestDealData(LatestDealData latestDealData);

    LatestDealData getLatestDealData(int fid);
}

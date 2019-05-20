package com.trade.jobs.kline;

import java.util.List;
import java.util.Map;

public interface MarkService {

    double getClosingPrice(final int fvirtualcointype);

    double getOpenningPrice(final int fvirtualcointype);

    double getEntrustBeforeWeek(final int fvirtualcointype);

    double getHighestBuyPrice(int id, long time);

    double getLowestSellPrice(int id, long time);

    Map<String, Object> getSum24(int id, long time);

    double getLatestDealPrize(int fid);

    double getUpanddown(int id, double latestDealPrice);

    double getUpanddownweek(int id, double latestDealPrice);

    List<LatestDealData> findFvirtualCoinType();

}

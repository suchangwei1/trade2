package com.trade.cache.data;

import java.util.List;
import java.util.Map;

public interface RealTimePriceService {

    /**
     * 最新成交价
     * @param id
     * @return
     */
    double getLatestDealPrize(int id);

    /**
     * 最低卖价
     * @param id
     * @return
     */
    double getLowestSellPrize(int id);

    /**
     * 最高买价
     * @param id
     * @return
     */
    double getHighestBuyPrize(int id);

    /**
     *  获取首页3天成交价数据
     *
     * @return
     */
    Map<String, List<Object[]>> getHourPriceTrendData();

}

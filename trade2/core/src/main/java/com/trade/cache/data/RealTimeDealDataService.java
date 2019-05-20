package com.trade.cache.data;

import com.trade.dto.LatestDealData;

import java.util.List;

public interface RealTimeDealDataService {

    /**
     * 最新成交价
     * @return
     */
    List<LatestDealData> getLatestDealDataList();

    /**
     * 获取排行统计数据
     * @param id
     * @return
     */
    LatestDealData getLatestDealData(int id);

}

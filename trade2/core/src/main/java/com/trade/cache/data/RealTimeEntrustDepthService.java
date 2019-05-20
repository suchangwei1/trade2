package com.trade.cache.data;

import java.util.List;

import com.trade.dto.FentrustData;

public interface RealTimeEntrustDepthService {

    /**
     * 委托卖出列表
     * @param id
     * @param deep
     * @return
     */
    String getSellDepthMap(int id, int deep);
    /**
     * 委托买入列表
     * @param id
     * @param deep
     * @return
     */
    String getBuyDepthMap(int id,int deep);

    /**
     * 获取行情
     * @param fid
     * @return
     */
    String getMarketJSON(int fid);
    
}

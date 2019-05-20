package com.trade.cache.data;

import com.trade.dto.FentrustData;
import com.trade.dto.FentrustlogData;
import com.trade.model.Fentrust;

import java.util.Set;

public interface RealTimeEntrustService {

    /**
     * 委托成功日志
     * @param id
     * @return
     */
    Set<FentrustlogData> getEntrustSuccessMap(int id);

    /**
     * 委托成功日志
     * @param id
     * @param limit
     * @return
     */
    Set<FentrustlogData> getEntrustSuccessMapLimit(int id, int limit);

    /**
     * 新增委托买入单
     * @param id
     * @param fentrust
     */
    void addEntrustBuyMap(int id, Fentrust fentrust);

    /**
     * 取消委托买入单
     * @param id
     * @param fentrust
     */
    void removeEntrustBuyMap(int id, Fentrust fentrust);

    void removeEntrustSellMap(int id, FentrustData fentrustData);

    void removeEntrustBuyMap(int id, FentrustData fentrustData);

    /**
     * 添加委托卖出单
     * @param id
     * @param fentrust
     */
    void addEntrustSellMap(int id, Fentrust fentrust);

    /**
     * 移除委托买入单
     * @param id
     * @param fentrust
     */
    void removeEntrustSellMap(int id, Fentrust fentrust);

}

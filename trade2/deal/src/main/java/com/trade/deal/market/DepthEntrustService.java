package com.trade.deal.market;

import com.trade.deal.model.FentrustData;

import java.util.List;
import java.util.Set;

public interface DepthEntrustService {

    /**
     * 更新深度挂单
     * @param fviFid
     * @param type
     * @param deep
     * @param price
     * @param count
     */
    void updateDepthEntrust(int fviFid, int type, int deep, Double price, double count, double money);

    /**
     * 获取深度挂单
     * @param fviFid
     * @param type
     * @param deep
     * @return
     */
    List<FentrustData> getDepthEntrust(int fviFid, int type, int deep, int size);

    /**
     * 获取所有深度的币ID
     * @return
     */
    Set<Integer> getDepthKeys();

    /**
     * 移除深度挂单数据
     */
    void clearDepthEntrust();
}

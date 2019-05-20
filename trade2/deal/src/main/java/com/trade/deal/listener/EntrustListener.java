package com.trade.deal.listener;

import com.trade.deal.model.FentrustData;

public interface EntrustListener {

    /**
     * 下单事件
     * @param data
     */
    void onCreateEntrust(FentrustData data);

    /**
     * 更新订单事件
     * @param data
     */
    void onUpdateEntrust(FentrustData data);

    /**
     * 取消订单事件
     * @param data
     */
    void onCancelEntrust(FentrustData data);

}

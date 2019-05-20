package com.trade.deal.market;

import com.trade.deal.model.FentrustData;
import com.trade.deal.mq.MessageListener;

public interface DepthCalculateQueue extends MessageListener<FentrustData>, Runnable {

    /**
     * 计算深度挂单数据
     * @param data
     */
    void calculateDepthEntrust(FentrustData data);


}

package com.trade.deal.core;

import com.trade.deal.model.FentrustData;
import com.trade.deal.model.FentrustlogData;

public interface TradeService {

    /**
     * 撮合交易
     * @param buy
     * @param sell
     * @param buyLog
     * @param sellLog
     * @return
     */
    boolean updateDealMaking(final FentrustData buy, final FentrustData sell, FentrustlogData buyLog, FentrustlogData sellLog);

    /**
     * 获取委托单
     * @param id
     * @return
     */
    FentrustData findByFid(int id);

}

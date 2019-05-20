package com.trade.deal.listener;

import com.trade.deal.market.CacheDataService;
import com.trade.deal.model.FentrustlogData;
import com.trade.deal.mq.MessageQueueService;
import com.trade.deal.mq.QueueConstants;

import javax.annotation.Resource;

public class DealMarkingListenerImpl implements DealMarkingListener {

    @Resource
    private CacheDataService cacheDataService;

    @Resource
    private MessageQueueService messageQueueService;

    public DealMarkingListenerImpl() {
    }

    public DealMarkingListenerImpl(CacheDataService cacheDataService, MessageQueueService messageQueueService) {
        this.cacheDataService = cacheDataService;
        this.messageQueueService = messageQueueService;
    }

    @Override
    public void writeLog(FentrustlogData data) {
        if (!data.isactive()) {
            return;
        }
        // 1、更新最新成交
        cacheDataService.addFentrustLogData(data);
        // 2、设置K线数据
        messageQueueService.publish(QueueConstants.SYNC_FENTRUST_LOG_DATA_QUEUE, data);
    }

    @Override
    public void updateMarking(int fid, double dealPrice, double highestBuy, double lowestDell) {
        // 更新最新成交价格，最高买价，最低卖价
        cacheDataService.updateMarking(fid, dealPrice, highestBuy, lowestDell);
    }
}

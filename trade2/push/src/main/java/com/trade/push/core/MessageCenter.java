package com.trade.push.core;

import com.trade.push.mq.MessageQueueService;
import com.trade.push.data.Constants;
import com.trade.push.mq.MessageListener;

import javax.annotation.Resource;
import java.io.Closeable;

public class MessageCenter implements Closeable {

    @Resource
    private MessageQueueService messageQueueService;

    public void start(MessageListener<String> listener) {
        // 这里改成单线程，监听多个事件，不然线程太多了
        messageQueueService.subscribe(listener, "1*", "2*", "3*", "4*", "5*", "6*", "7*", "8*", "9*", "u*", "c*", "n*", Constants.MARKET_CHAT_CHANNEL, "otc:message*");
    }

    @Override
    public void close() {
        try {
            messageQueueService.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


}

package com.trade.deal.mq;

public interface MessageQueueService {

    /**
     * 发送消息
     * @param channel
     * @param message
     */
    void publish(String channel, Object message);

    /**
     * 订阅消息
     * @param channel
     * @param listener
     */
    <T> void subscribe(String channel, MessageListener<T> listener, Class<T> clazz);

}

package com.trade.mq;

public interface MessageQueueService {

    /**
     * 发送消息
     * @param channel
     * @param message
     */
    void publish(String channel, Object message);

    /**
     * 广播消息
     *
     * @param message
     */
    void bpublish(String channel, Object message);

    /**
     * 订阅消息
     * @param channel
     * @param listener
     */
    <T> void subscribe(String channel, MessageListener<T> listener, Class<T> clazz);

    /**
     * 订阅广播消息
     *
     * @param listener
     * @param clazz
     * @param <T>
     */
    <T> void bsubscribe(final String channel, final MessageListener<T> listener, final Class<T> clazz);

}

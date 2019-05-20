package com.trade.push.mq;

public interface MessageListener<T> {

    void onMessage(String channel, T message);

}

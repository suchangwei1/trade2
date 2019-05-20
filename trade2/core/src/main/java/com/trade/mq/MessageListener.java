package com.trade.mq;

public interface MessageListener<T> {

    void onMessage(T message);

}

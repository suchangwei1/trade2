package com.trade.deal.mq;

public interface MessageListener<T> {

    void onMessage(T message);

}

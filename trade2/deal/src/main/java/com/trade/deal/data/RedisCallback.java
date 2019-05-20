package com.trade.deal.data;

import redis.clients.jedis.Jedis;

public interface RedisCallback<T> {

    void doInRedis(Jedis connection);

}

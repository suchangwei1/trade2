package com.trade.push.mq;

import com.alibaba.fastjson.JSON;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPubSub;

import javax.annotation.Resource;
import java.io.IOException;
import java.util.concurrent.atomic.AtomicInteger;

public class RedisMessageQueueServiceImpl implements MessageQueueService {

    private final Logger log = LoggerFactory.getLogger(getClass());

    @Resource
    private JedisPool jedisPool;

    private boolean closed;

    private AtomicInteger counter = new AtomicInteger(0);

    @Override
    public void subscribe(MessageListener<String> listener, String... channels) {
        Thread thread = new Thread(() -> {
            while (!closed) {
                try {
                    log.trace("subscribe {}", JSON.toJSONString(channels));
                    subscribeRedis(listener, channels);
                } catch (Exception e) {
                    log.error("subscribe {} error, reason ", JSON.toJSONString(channels), e.getStackTrace()[0]);
                }
                try {
                    Thread.sleep(3000);
                } catch (Exception e) {

                }
            }
        });
        thread.setName("redis-sub-" + counter.addAndGet(1));
        thread.start();
    }

    private void subscribeRedis(final MessageListener<String> listener, String[] channels) {
        try (Jedis jedis = jedisPool.getResource()) {
            jedis.psubscribe(new JedisPubSub() {
                @Override
                public void onPMessage(String pattern, String channel, String message) {
                    try {
                        listener.onMessage(channel, message);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }, channels);
        }
    }

    @Override
    public void close() throws IOException {
        closed = true;
        jedisPool.close();
    }
}

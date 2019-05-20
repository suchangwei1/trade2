package com.trade.deal.mq;

import com.alibaba.fastjson.JSON;
import com.rabbitmq.client.*;
import com.trade.deal.util.Config;
import org.apache.commons.pool2.BasePooledObjectFactory;
import org.apache.commons.pool2.PooledObject;
import org.apache.commons.pool2.impl.DefaultPooledObject;
import org.apache.commons.pool2.impl.GenericObjectPool;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

public class RabbatMessageQueueServiceImpl implements MessageQueueService {

    private String topic;

    private ConnectionFactory cf;

    private final Logger log = LoggerFactory.getLogger(getClass());

    public RabbatMessageQueueServiceImpl() {

        Config config = Config.getInstance();
        cf = new ConnectionFactory();
        cf.setUsername(config.getProperty("rabbit.username"));
        cf.setPassword(config.getProperty("rabbit.password"));
        cf.setHost(config.getProperty("rabbit.host"));
        cf.setPort(Integer.valueOf(config.getProperty("rabbit.port")));
        cf.setVirtualHost(config.getProperty("rabbit.vhost"));
        cf.setAutomaticRecoveryEnabled(true);
        topic = config.getProperty("rabbit.topic");

        testConnection();
    }

    private void testConnection() {
        try {
            Connection conn = cf.newConnection();
            if (conn.isOpen()) {
                conn.close();
            } else {
                log.error("无法连接消息队列");
                System.exit(-1);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (TimeoutException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void publish(String routingKey, Object message) {
        Channel channel = null;
        try {

            log.debug("send to {} msg = {}", routingKey, message);

            channel = (Channel) channelPool.borrowObject();
            byte[] messageBodyBytes = JSON.toJSONBytes(message);
            channel.basicPublish(topic, routingKey,
                    new AMQP.BasicProperties.Builder()
//                            .expiration("60000")
                            .build(),
                    messageBodyBytes);

        } catch (Exception e) {
            try {
                channelPool.invalidateObject(channel);
            } catch (Exception e2) {
                e2.printStackTrace();
            }
            e.printStackTrace();
        } finally {
            channelPool.returnObject(channel);
        }
    }

    @Override
    public <T> void subscribe(final String routingKey, final MessageListener<T> listener, final Class<T> clazz) {

        try {

            log.debug("subscribe " + routingKey);

            Connection conn = cf.newConnection();
            Channel channel = conn.createChannel();

            String queueName = routingKey;

            channel.exchangeDeclare(topic, "topic", true);
            channel.queueDeclare(queueName, true, false, false, null);
            channel.queueBind(queueName, topic, routingKey);

            channel.basicConsume(queueName, true, new DefaultConsumer(channel){
                @Override
                public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties, byte[] body) throws IOException {
                    try {
                        T msg = JSON.parseObject(body, clazz);
                        listener.onMessage(msg);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            });

        } catch (IOException e) {
            log.error("subscribe error " + e.getLocalizedMessage());
        } catch (TimeoutException e) {
            log.error("subscribe timeout");
        }

    }


    /**
     * 连接池
     */
    private GenericObjectPool connPool = new GenericObjectPool(new BasePooledObjectFactory(){
        @Override
        public Object create() throws Exception {
            Connection conn = cf.newConnection();
            return conn;
        }

        @Override
        public PooledObject wrap(Object obj) {
            return new DefaultPooledObject<>(obj);
        }
    });

    /**
     * 通道池
     */
    private GenericObjectPool channelPool = new GenericObjectPool(new BasePooledObjectFactory(){
        @Override
        public Object create() throws Exception {
            Connection conn = (Connection) connPool.borrowObject();
            Channel channel = conn.createChannel();
            connPool.returnObject(conn);
            return channel;
        }

        @Override
        public PooledObject wrap(Object obj) {
            return new DefaultPooledObject<>(obj);
        }
    });

}


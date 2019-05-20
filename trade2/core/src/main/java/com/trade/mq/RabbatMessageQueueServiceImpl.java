package com.trade.mq;

import com.alibaba.fastjson.JSON;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.FanoutExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.amqp.rabbit.connection.CachingConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitAdmin;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.rabbit.listener.SimpleMessageListenerContainer;
import org.springframework.amqp.rabbit.listener.adapter.MessageListenerAdapter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

public class RabbatMessageQueueServiceImpl implements MessageQueueService {

    private final Logger log = LoggerFactory.getLogger(getClass());

    @Value("${rabbit.host}")
    private String host;
    @Value("${rabbit.port}")
    private int port;
    @Value("${rabbit.username}")
    private String username;
    @Value("${rabbit.password}")
    private String password;
    @Value("${rabbit.topic}")
    private String topic;
    @Value("${rabbit.vhost}")
    private String vhost;

    private CachingConnectionFactory cf;
    private RabbitTemplate template;
    private RabbitAdmin admin;

    @PostConstruct
    private void init() {
        cf = new CachingConnectionFactory(host, port);
        cf.setChannelCacheSize(100);
        cf.setUsername(username);
        cf.setPassword(password);
        cf.setChannelCacheSize(100);
        cf.setVirtualHost(vhost);
        admin = new RabbitAdmin(cf);
        template = new RabbitTemplate(cf);
    }

    /**
     * 广播队列
     *
     * @param channel
     * @param message
     */
    @Override
    public void bpublish(String channel, Object message) {
        template.convertAndSend(channel, null, JSON.toJSONString(message));
    }

    @Override
    public void publish(String channel, Object message) {
        template.convertAndSend(topic, channel, JSON.toJSONString(message));
    }

    @Override
    public <T> void subscribe(final String channel, final MessageListener<T> listener, final Class<T> clazz) {
        /**
         * 监听队列的时候，先声明队列
         * 确保队列已声明
         */
        SimpleMessageListenerContainer container = new SimpleMessageListenerContainer(cf);
        Queue queue = new Queue(channel);
        admin.declareQueue(queue);
        TopicExchange exchange = new TopicExchange(topic);
        admin.declareExchange(exchange);
        admin.declareBinding(
                BindingBuilder.bind(queue).to(exchange).with(channel));

        MessageListenerAdapter adapter = new MessageListenerAdapter(new Object() {
            public void handleMessage(String message) {
                try {
                    T msg = JSON.parseObject(message, clazz);
                    listener.onMessage(msg);
                } catch (Exception e) {
                    log.error("handleMessage string err {}", message, e);
//                    e.printStackTrace();
                }
            }
            public void handleMessage(byte[] body) {
                try {
                    T msg = JSON.parseObject(body, clazz);
                    listener.onMessage(msg);
                } catch (Exception e) {
                    log.error("handleMessage arr err {}", new String(body), e);
                    e.printStackTrace();
                }
            }
        });
        container.setMessageListener(adapter);
        container.setQueueNames(channel);
        container.start();

    }

    @Override
    public <T> void bsubscribe(final String channel, final MessageListener<T> listener, final Class<T> clazz) {
        SimpleMessageListenerContainer container = new SimpleMessageListenerContainer(cf);
        FanoutExchange exchange = new FanoutExchange(channel);
        admin.declareExchange(exchange);
        Queue queue = admin.declareQueue();
        admin.declareExchange(exchange);
        admin.declareBinding(BindingBuilder.bind(queue).to(exchange));

        MessageListenerAdapter adapter = new MessageListenerAdapter(new Object() {
            public void handleMessage(String message) {
                try {
                    T msg = JSON.parseObject(message, clazz);
                    listener.onMessage(msg);
                } catch (Exception e) {
                    log.error("handleMessage string err {}", message, e);
                    e.printStackTrace();
                }
            }
            public void handleMessage(byte[] body) {
                try {
                    T msg = JSON.parseObject(body, clazz);
                    listener.onMessage(msg);
                } catch (Exception e) {
                    log.error("handleMessage arr err {}", new String(body), e);
//                    e.printStackTrace();
                }
            }
        });
        container.setMessageListener(adapter);
        container.setQueueNames(queue.getName());
        container.start();
    }

}


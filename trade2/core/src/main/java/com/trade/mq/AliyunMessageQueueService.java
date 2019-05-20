package com.trade.mq;

import com.alibaba.fastjson.JSON;
import com.aliyun.openservices.ons.api.*;
import org.springframework.beans.factory.annotation.Value;

import javax.annotation.PostConstruct;
import java.util.Properties;

public class AliyunMessageQueueService implements MessageQueueService {

    @Value("${ons.topic}")
    private String topic;
    @Value("${ons.accessKey}")
    private String accessKey;
    @Value("${ons.secretKey}")
    private String secretKey;
    @Value("${ons.producerId}")
    private String producerId;
    @Value("${ons.consumerId}")
    private String consumerId;

    private Producer producer;
    private Consumer consumer;

    @PostConstruct
    public void init() {
        System.out.println("初始化Ons");
        Properties properties = new Properties();
        properties.put(PropertyKeyConst.ConsumeThreadNums, 1);  // 只有一个消费线程
        properties.put(PropertyKeyConst.AccessKey, accessKey);
        properties.put(PropertyKeyConst.SecretKey, secretKey);
        properties.put(PropertyKeyConst.ProducerId, producerId);
        properties.put(PropertyKeyConst.ConsumerId, consumerId);
        consumer = ONSFactory.createConsumer(properties);
        producer = ONSFactory.createProducer(properties);
        producer.start();
        consumer.start();
        System.out.println("初始化Ons end");
    }

    @Override
    public void publish(String channel, Object message) {
        Message msg = new Message(
                topic,
                channel,
                JSON.toJSONString(message).getBytes());
        producer.send(msg);
    }

    @Override
    public void bpublish(String channel, Object message) {

    }

    @Override
    public <T> void subscribe(String channel, final MessageListener<T> listener, final Class<T> clazz) {
        consumer.subscribe(topic, channel, new com.aliyun.openservices.ons.api.MessageListener() {
            public Action consume(Message message, ConsumeContext context) {
                String msgbody = new String(message.getBody());
                T msg = JSON.parseObject(msgbody, clazz);
                listener.onMessage(msg);
                return Action.CommitMessage;
            }
        });
    }

    @Override
    public <T> void bsubscribe(String channel, MessageListener<T> listener, Class<T> clazz) {

    }

}

package com.trade.deal.core;

import com.trade.deal.Enum.EntrustStatusEnum;
import com.trade.deal.Enum.EntrustTypeEnum;
import com.trade.deal.listener.EntrustListener;
//import com.trade.deal.market.MarkService;
import com.trade.deal.model.FentrustData;
//import com.trade.deal.model.LatestDealData;
import com.trade.deal.mq.MessageListener;
import com.trade.deal.mq.MessageQueueService;
import main.Application;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.Resource;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentSkipListSet;

public class MessageCenter implements MessageListener<FentrustData> {

    private final Logger log = LoggerFactory.getLogger(getClass());

    @Resource
    private EntrustListener entrustListener;

    @Resource
    private MessageQueueService messageQueueService;

//    @Resource
//    private MarkService markService;

    private Map<Integer, TradingSystem> systemMap = new ConcurrentHashMap<>();

    private Set<String> messageSet = new ConcurrentSkipListSet<>();

    public MessageCenter() {}

    public MessageCenter(EntrustListener entrustListener, MessageQueueService messageQueueService) {
        this.entrustListener = entrustListener;
        this.messageQueueService = messageQueueService;
    }

    @Override
    public void onMessage(FentrustData message) {
        // 给每个消息一个ID，避免在启动过程中，从数据库加载的一遍，消息队列又来一遍，导致消息重复的问题
        // 从消息队列过来的消息只有两种，一种是创建，一种是取消，
        // 有一种极端情况：当撮合引擎停掉了，或者在重启，这个时候用户在前台下单，消息已经进队列
        // 这个时候撮合引擎启动，会把数据库中的所有订单加到内存中，可能就包含了刚才用户下的那条订单
        // 当撮合启动成功，并开始监听消息队列的时候，刚才那条订单就会传递到这里，这个时候就会出现重复下单的消息
        // 不能简单的根据ID来过滤掉，因为用户下单之后，又取消了，消息队列里面还有有一条取消的消息
        // 所有通过订单ID + 状态来给消息编号，过滤掉重复的情况
        // 只有在重启的时候，下单才会出现这样的情况
        String messageId = message.getFid() + "_" + message.getFstatus();
        synchronized (messageSet) {
            if (messageSet.contains(messageId)) {
                return;
            }
            messageSet.add(messageId);
        }

        log.debug("onMessage {}", message);
        TradingSystem tradingSystem = systemMap.get(message.getFviFid());
        if (tradingSystem == null) {
            log.debug("new " + message.getFviFid() + " system");
            tradingSystem = Application.buildTradingSystem();
            systemMap.put(message.getFviFid(), tradingSystem);
            new Thread(tradingSystem).start();
        }


        // 收到的订单，如果是取消状态，则取消
        if (message.getFstatus() == EntrustStatusEnum.Cancel) {
            // 极端情况下，用户下了单，但是没有被撮合，这个时候撮合引擎重启了，然后用户又取消了
            // 导致队列里面有有取消的消息，撮合引擎重启的时候，又把这条当成已取消了
            // 如果直接更新深度挂单数据，则会出现多减的情况
            if (tradingSystem.getEntrust(message.getFid()) != null) {
                entrustListener.onCreateEntrust(message.clone());
            }
            tradingSystem.cancelEntrust(message);
            return;
        }

        entrustListener.onCreateEntrust(message.clone());

        if (message.getFentrustType() == EntrustTypeEnum.BUY) {
            tradingSystem.addBuyEntrust(message.getFviFid(), message);
        } else {
            tradingSystem.addSellEntrust(message.getFviFid(), message);
        }
    }

    public void init() {
        messageQueueService.subscribe("solve.entrust.*", this, FentrustData.class);
//        autoSubscribe();
//        new Thread(){
//            @Override
//            public void run() {
//                while (true) {
//                    try {
//                        autoSubscribe();
//                    } catch (Exception e) {
//                        e.printStackTrace();
//                        log.error("autoSubscribe ", e);
//                    }
//                    try {
//                        Thread.sleep(1000 * 60 * 5);
//                    } catch (Exception e) {
//                        e.printStackTrace();
//                    }
//                }
//            }
//        }.start();
    }

//    private Set<String> subList = new HashSet<>();
//
//    public void subscribe(String queue) {
//        synchronized (this) {
//            // 不能重复监听
//            if (!subList.contains(queue)) {
//                log.info("subscribe " + queue);
//                messageQueueService.subscribe(queue, this, FentrustData.class);
//                subList.add(queue);
//            }
//        }
//    }
//
//    public void autoSubscribe() {
//        // 从获取库查询所有的币，并监听挂单队列
//        List<LatestDealData> list = markService.findFvirtualCoinType();
//        list.forEach(v -> {
//            subscribe("solve.entrust.queue" + v.getFid());
//        });
//    }

    public Map<Integer, TradingSystem> getSystemMap() {
        return systemMap;
    }

}

package com.trade.deal.mq;

/**
 * 队列常量
 */
public class QueueConstants {

    public static final String EMAIL_COMMON_QUEUE = "email.common";
    public static final String EMAIL_IMPORTANT_QUEUE = "email.important";
    public static final String MESSAGE_COMMON_QUEUE = "message.common";
    public static final String MESSAGE_IMPORTANT_QUEUE = "message.important";
    public static final String SYNC_DEAL_DATA_QUEUE = "sync.deal_data";
    public static final String SYNC_BUY_SELL_PRICE_DATA_QUEUE = "sync.buy.sell.price.queue";
    public static final String SYNC_LAST_UPDATE_TIME_QUEUE = "sync.last_update_time_queue";
    public static final String SYNC_FENTRUST_LOG_DATA_QUEUE = "sync.fentrust_log_data";                     // 同步交易成功数据
    public static final String SYNC_MINUTE_KLINE_QUEUE = "sync.minute.kline";                               // 同步一分钟K线
    public static final String COUNT_MINUTE_KLINE_QUEUE = "count.minute.kline";                             // 计算一分钟K线
    public static final String COUNT_MINUTE_KLINE_SUCCESS_QUEUE = "count.minute.kline.success";             // 计算一分钟K线完成
    public static final String DEPTH_ENTRUST_QUEUE = "depth.entrust.queue";//深度合并队列名
    public static final String SOLVE_ENTRUST_QUEUE = "solve.entrust.queue";//手动挂单队列名
    public static final String ROBOT_ENTRUST_QUEUE = "robot.entrust.queue";//机器人挂单队列名
    public static final String KLIN_TREDE = "user.trade.kline.queue";
}

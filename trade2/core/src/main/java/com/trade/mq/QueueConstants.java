package com.trade.mq;

/**
 * 队列常量
 */
public class QueueConstants {

    public static final String EMAIL_COMMON_QUEUE = "email.common";
    public static final String EMAIL_IMPORTANT_QUEUE = "email.important";
    public static final String MESSAGE_COMMON_QUEUE = "message.common";
    public static final String VOICE_COMMON_QUEUE = "voice.common";
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
    public static final String AWARD_TICKET_QUEUE = "award.ticket.queue";//奖励ticket队列名

    public static final String SYNC_ZHG_USER_SYSTEM_QUEUE = "sync.zhg.user.system.queue";   // 用户系统同步

    public static final String SYNC_USER_REGISTER_QUEUE = "sync.user_register.queue";       // 用户注册异步初始化

    // api调用日志
    public static final String API_INVOKE_LOG_QUEUE = "api.invoke.log.queue";

    // 用户登录后台线程
    public static final String USER_LOGINED_BACK_QUEUE = "user.logined.back.queue";

    // 强制用户离线
    public final static String FORCE_OFFLINE_QUEUE = "force.offline.queue";

    // ico
    public static final String ICO_BUY_QUEUE = "ico.buy.queue";
}

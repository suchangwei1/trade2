package com.trade.deal.core;


public interface SyncTaskService {

    /**
     * 执行异步任务
     * @param runnable
     */
    void execute(Runnable runnable);

    /**
     * 获取任务数量
     * @return
     */
    int count();
}

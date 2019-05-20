package com.trade.queue;

import com.trade.model.ApiInvokeLog;
import com.trade.mq.MessageListener;
import com.trade.mq.MessageQueueService;
import com.trade.mq.QueueConstants;
import com.trade.service.front.ApiInvokeLogService;
import org.springframework.beans.factory.annotation.Autowired;

import javax.annotation.PostConstruct;

public class ApiInvokeLogQueue implements MessageListener {
    @Autowired
    MessageQueueService messageQueueService;
    @Autowired
    private ApiInvokeLogService apiInvokeLogService;

    @PostConstruct
    public void init(){
        messageQueueService.subscribe(QueueConstants.API_INVOKE_LOG_QUEUE, this, ApiInvokeLog.class);
    }

    @Override
    public void onMessage(Object message) {
        apiInvokeLogService.saveLog((ApiInvokeLog) message);
    }
}

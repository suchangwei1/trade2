package com.trade.queue;

import com.trade.model.Fuser;
import com.trade.mq.MessageListener;
import com.trade.mq.MessageQueueService;
import com.trade.mq.QueueConstants;
import com.trade.service.front.FrontUserService;
import org.springframework.beans.factory.annotation.Autowired;

import javax.annotation.PostConstruct;

public class UserRegisterQueue implements MessageListener<Fuser> {
    @Autowired
    private FrontUserService frontUserService;
    @Autowired
    private MessageQueueService messageQueueService;

    @PostConstruct
    public void init() {
        messageQueueService.subscribe(QueueConstants.SYNC_USER_REGISTER_QUEUE, this, Fuser.class);
    }

    @Override
    public void onMessage(Fuser message) {
        frontUserService.updateInitAccount(message);
    }
}

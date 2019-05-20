package com.trade.mq;

import com.alibaba.fastjson.JSON;
import com.trade.Enum.UserStatusEnum;
import com.trade.dto.OperateUserActionDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

import javax.annotation.PostConstruct;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.concurrent.TimeUnit;

public class OperateUserActionListener implements MessageListener<OperateUserActionDTO> {
    @Value("${session.redis.timeout:1800}")
    private int timeout;

    @Autowired
    private MessageQueueService messageQueueService;

    private Logger logger = LoggerFactory.getLogger(OperateUserActionListener.class);

    protected boolean cleaning;

    /**
     * 缓存禁用用户信息
     *
     */
    private final Map<String, OperateUserActionDTO> offlineMap = new HashMap<>();

    @PostConstruct
    public void init() {
        messageQueueService.bsubscribe(QueueConstants.FORCE_OFFLINE_QUEUE, this, OperateUserActionDTO.class);
    }

    public boolean isEmpty() {
        return this.offlineMap.isEmpty();
    }

    public boolean isExist(String userId) {
        return this.offlineMap.containsKey(userId);
    }

    protected String getActionKey(OperateUserActionDTO dto) {
        return String.valueOf(dto.getUserId()).intern();
    }

    /**
     * 清理过期action
     *
     */
    public void clean() {
        try {
            this.cleaning = true;
            for(Iterator<Map.Entry<String, OperateUserActionDTO>> iter = offlineMap.entrySet().iterator(); iter.hasNext();) {
                OperateUserActionDTO dto = iter.next().getValue();
                if (System.currentTimeMillis() - dto.getOperTime().getTime() >= this.timeout * 1000) {
                    logger.info("clean force offline info {}", dto.getUserId());
                    iter.remove();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            this.cleaning = false;
        }
    }

    @Override
    public void onMessage(OperateUserActionDTO message) {
        logger.info("operate user info: {}", JSON.toJSONString(message));
        if (UserStatusEnum.FORBBIN_VALUE == message.getStatus()) {
            // 禁用用户
            offlineMap.put(String.valueOf(message.getUserId()), message);
        } else if (UserStatusEnum.NORMAL_VALUE == message.getStatus() && offlineMap.containsKey(getActionKey(message))) {
            // 解禁用户
            this.removeAction(message);
        }
    }

    /**
     * 解禁过程中确认是否在清除中
     *
     * @param dto
     */
    protected void removeAction(OperateUserActionDTO dto) {
        for (;this.cleaning;) {
            try {
                TimeUnit.SECONDS.sleep(1);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        String key = getActionKey(dto);
        if (offlineMap.containsKey(key)) {
            offlineMap.remove(key);
        }
    }
}

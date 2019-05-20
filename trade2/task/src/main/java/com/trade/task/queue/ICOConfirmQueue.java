package com.trade.task.queue;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.trade.Enum.ICORecordStatusEnum;
import com.trade.model.Fvirtualcointype;
import com.trade.model.ICO;
import com.trade.model.ICORecord;
import com.trade.mq.MessageListener;
import com.trade.mq.MessageQueueService;
import com.trade.mq.QueueConstants;
import com.trade.service.admin.VirtualCoinService;
import com.trade.service.front.ICORecordService;
import com.trade.service.front.ICOService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import javax.annotation.PostConstruct;
import java.util.Objects;


public class ICOConfirmQueue implements MessageListener<Integer> {
    @Autowired
    private MessageQueueService messageQueueService;
    @Autowired
    private ICORecordService icoRecordService;
    @Autowired
    private ICOService icoService;
    @Autowired
    private VirtualCoinService virtualCoinService;

    private Logger logger = LoggerFactory.getLogger(ICOConfirmQueue.class);

    @PostConstruct
    public void init() {
        messageQueueService.subscribe(QueueConstants.ICO_BUY_QUEUE, this, Integer.class);
    }

    @Override
    public void onMessage(Integer message) {
        ICORecord icoRecord = icoRecordService.findById(message);
        if(Objects.isNull(icoRecord) || icoRecord.isDelete()) {
            logger.warn("ico记录ID {}不存在或已删除", icoRecord.getIcoId());
            return;
        }

        if(icoRecord.getStatus() != ICORecordStatusEnum.IN_PROGRESS) {
            logger.warn("ico记录ID {}已处理", icoRecord.getIcoId());
            return;
        }

        try {
            icoService.updateForBuy(icoRecord);
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("ICO {}记录确认失败", icoRecord.getIcoId());

            // 放回队列
            //messageQueueService.publish(QueueConstants.ICO_BUY_QUEUE, message);
            return;
        }

        if (ICORecordStatusEnum.SUCCESS.getIndex() != icoRecord.getStatus().getIndex()) {
            // 认购未成功
            return;
        }

        try {
            ICO ico = icoService.findById(icoRecord.getIcoId());
            JSONObject jsonExt = JSON.parseObject(ico.getJsonExt());
            // 币已ico数量
            JSONObject coinSumMap = jsonExt.getJSONObject("coinSumMap");

            // 统计ico数量
            String key = "CNY";
            if(!icoRecord.isUseRMB()) {
                Fvirtualcointype fvirtualcointype = virtualCoinService.findById(icoRecord.getSwapCoinType());
                key = fvirtualcointype.getfShortName().toUpperCase();
            }
            coinSumMap.put(key, this.icoRecordService.sumSwapAmount(icoRecord.getIcoId(), icoRecord.getSwapCoinType()));
            jsonExt.put("version", jsonExt.getIntValue("version") + 1);
            ico.setJsonExt(jsonExt.toJSONString());
            icoService.update(ico);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

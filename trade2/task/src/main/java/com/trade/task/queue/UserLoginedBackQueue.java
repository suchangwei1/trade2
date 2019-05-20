package com.trade.task.queue;

import com.trade.dto.UserDto;
import com.trade.model.Flog;
import com.trade.model.Fuser;
import com.trade.mq.MessageListener;
import com.trade.mq.MessageQueueService;
import com.trade.mq.QueueConstants;
import com.trade.service.admin.LogService;
import com.trade.service.front.FrontUserService;
import com.trade.service.front.FrontValidateService;
import com.trade.util.CollectionUtils;
import com.trade.util.DateUtils;
import com.trade.util.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;

import javax.annotation.PostConstruct;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.List;

public class UserLoginedBackQueue implements MessageListener<UserDto> {
    @Autowired
    private LogService logService;
    @Autowired
    private FrontUserService frontUserService;
    @Autowired
    private FrontValidateService frontValidateService;
    @Autowired
    private MessageQueueService messageQueueService;

    @PostConstruct
    public void init(){
        messageQueueService.subscribe(QueueConstants.USER_LOGINED_BACK_QUEUE, this, UserDto.class);
    }

    @Override
    public void onMessage(UserDto message) {
        // 检查异地登陆
        checkLoginIp(message);
    }

    protected void checkLoginIp(UserDto dto){
        List<Flog> flogList = logService.list(0, 2, "where fkey1='" + dto.getUserId() + "' order by fid desc", true);
        if(CollectionUtils.isEmpty(flogList)){
            return;
        }

        Flog curLog = flogList.get(0);
        if(1 == flogList.size()){
            addDeviceCode(curLog);
            return;
        }
        Flog preLog = flogList.get(1);
        if(!StringUtils.isEmpty(preLog.getFkey5()) && preLog.getFkey5().equals(curLog.getFkey5())){
            return;
        }
        if(!DateUtils.formatDate(dto.getTimestamp()).equals(DateUtils.formatDate(curLog.getFcreateTime()))){
            return;
        }
        Fuser fuser = frontUserService.findById(dto.getUserId());
        if(!StringUtils.isEmail(fuser.getFemail())){
            return;
        }
        //frontValidateService.updateSendAcountSecurityNotify(fuser, curLog);
    }

    private void addDeviceCode(Flog log){
        if(log != null && !StringUtils.isEmpty(log.getFkey3())){
            String deviceBeforeCode;
            try{
                deviceBeforeCode = URLEncoder.encode(log.getFkey2() + log.getFkey3(),"UTF-8");
                log.setFkey5(deviceBeforeCode);
                logService.updateObj(log);
            }catch(UnsupportedEncodingException e){
                e.printStackTrace();
            }
        }
    }
}

package com.trade.jobs.sms;

import com.aliyuncs.DefaultAcsClient;
import com.aliyuncs.IAcsClient;
import com.aliyuncs.dysmsapi.model.v20170525.SendSmsRequest;
import com.aliyuncs.dysmsapi.model.v20170525.SendSmsResponse;
import com.aliyuncs.exceptions.ClientException;
import com.aliyuncs.profile.DefaultProfile;
import com.aliyuncs.profile.IClientProfile;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.JdbcTemplate;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import java.sql.Timestamp;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;

public class AliSms implements Runnable {

    public static final String MESSAGE_NAME = "messageName";
    public static final String MESSAGE_PASSWORD = "messagePassword";
    public static final String MESSAGE_SIGN = "messageSign";
    public static final String MESSAGE_TEMPLATE = "messageTemplate";
    private final Logger log = LoggerFactory.getLogger(getClass());
    private final int SUCCESS = 2;      // 短信发送成功
    private final int FAIL = 3;         // 短信发送失败
    private final int LIMIT = 800;      // 每次最大发送条数

    static final String product = "Dysmsapi";
    static final String domain = "dysmsapi.aliyuncs.com";

    @Resource
    private ExecutorService executorService;
    @Resource
    private SystemArgsService systemArgsService;
    @Resource
    private JdbcTemplate jdbc;

    private void updateStatus(Object id, int status) {
        try {
            jdbc.update("UPDATE fvalidatemessage SET fstatus = ?, fSendTime = ? WHERE fid = ?", status, new Timestamp(System.currentTimeMillis()), id);
        } catch (Exception e) {
            log.error("更新短信状态失败", e);
        }
    }

    private void batSend() throws Exception {
        List<Map<String, Object>> list = jdbc.queryForList("SELECT * FROM fvalidatemessage WHERE fstatus = 1 AND type = 0 limit ?", LIMIT);
        if (list != null && list.size() > 0) {
            Map<String, String> account = systemArgsService.getSystemArgs(MESSAGE_NAME, MESSAGE_PASSWORD, MESSAGE_SIGN, MESSAGE_TEMPLATE);
            final CountDownLatch latch = new CountDownLatch(list.size());
            list.forEach(map -> {
                executorService.submit(() -> {
                    int status = sendMessage(account, map.get("fPhone"), map.get("fContent")) ? SUCCESS : FAIL;
                    updateStatus(map.get("fid"), status);
                    latch.countDown();
                });
            });
            latch.await();
        }
    }

    private boolean sendMessage(Map<String, String> account, Object phone, Object content) {
        try {
            if (phone == null || content == null) {
                return false;
            }
            log.debug("发送短信到{}", phone);
            return sendSms(phone.toString(), content.toString(), account.get(MESSAGE_SIGN), account.get(MESSAGE_TEMPLATE), account.get(MESSAGE_NAME), account.get(MESSAGE_PASSWORD));
        } catch (Exception e) {
            log.error("发送短信失败", e);
        }
        return false;
    }


    public boolean sendSms(String phone, String code, String name, String template, String key, String secret) throws ClientException {

        System.setProperty("sun.net.client.defaultConnectTimeout", "10000");
        System.setProperty("sun.net.client.defaultReadTimeout", "10000");

        IClientProfile profile = DefaultProfile.getProfile("cn-hangzhou", key, secret);
        DefaultProfile.addEndpoint("cn-hangzhou", "cn-hangzhou", product, domain);
        IAcsClient acsClient = new DefaultAcsClient(profile);

        SendSmsRequest request = new SendSmsRequest();
        request.setPhoneNumbers(phone);
        request.setSignName(name);
        request.setTemplateCode(template);
        request.setTemplateParam("{\"code\":\"" + code + "\"}");

        SendSmsResponse sendSmsResponse = acsClient.getAcsResponse(request);

        return "OK".equals(sendSmsResponse.getCode())? true: false;
    }



    @PostConstruct
    @Override
    public void run() {
        long beginTime = System.currentTimeMillis();
        log.debug("批量发送短信");
        try {
            batSend();
        } catch (Exception e) {
            log.error("批量发送短信 {}", e.toString());
        }
        log.debug("批量发送短信耗时: {}", (System.currentTimeMillis() - beginTime));
    }
}

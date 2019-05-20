package com.trade.jobs.sms;

import com.taobao.api.DefaultTaobaoClient;
import com.taobao.api.TaobaoClient;
import com.taobao.api.TaobaoResponse;
import com.taobao.api.request.AlibabaAliqinFcTtsNumSinglecallRequest;
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

public class SmsVoiceJob implements Runnable {

    private final Logger log = LoggerFactory.getLogger(getClass());
    private final int SUCCESS = 2;      // 短信发送成功
    private final int FAIL = 3;         // 短信发送失败
    private final int LIMIT = 800;

    public static final String ALI_VOICE_API_KEY = "aliVoiceAPIKey";
    public static final String ALI_VOICE_API_SECRET = "aliVoiceAPISecret";
    public static final String ALI_VOICE_API_URL = "aliVoiceAPIUrl";
    public static final String ALI_VOICE_API_TTS = "aliVoiceAPI_TTS";
    public static final String ALI_VOICE_API_CALLED_NUM = "aliVoiceAPICalledNum";
    public static final String WEB_NAME = "webName";

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
        List<Map<String, Object>> list = jdbc.queryForList("SELECT * FROM fvalidatemessage WHERE fstatus = 1 AND type = 1 limit ?", LIMIT);
        if (list != null && list.size() > 0) {
            Map<String, String> account = systemArgsService.getSystemArgs(ALI_VOICE_API_URL, ALI_VOICE_API_KEY, ALI_VOICE_API_SECRET, ALI_VOICE_API_CALLED_NUM, ALI_VOICE_API_TTS, WEB_NAME);
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
            log.debug("发送语音短信到{}", phone);

            TaobaoClient client = new DefaultTaobaoClient(account.get(ALI_VOICE_API_URL), account.get(ALI_VOICE_API_KEY), account.get(ALI_VOICE_API_SECRET));
            AlibabaAliqinFcTtsNumSinglecallRequest req = new AlibabaAliqinFcTtsNumSinglecallRequest();
            req.setCalledNum(phone.toString());
            req.setCalledShowNum(account.get(ALI_VOICE_API_CALLED_NUM));
            req.setTtsCode(account.get(ALI_VOICE_API_TTS));

            // 模板参数
            req.setTtsParamString("{\"name\":\"" + account.get(WEB_NAME) + "用户\", \"number\":\"" + content.toString() + "\"}");

            TaobaoResponse response = client.execute(req);
            return response.isSuccess();
        } catch (Exception e) {
            log.error("发送语音短信失败", e);
        }
        return false;
    }

    @PostConstruct
    @Override
    public void run() {
        long beginTime = System.currentTimeMillis();
        log.debug("批量发送语音短信");
        try {
            batSend();
        } catch (Exception e) {
            log.error("批量发送语音短信 {}", e.toString());
        }
        log.debug("批量发送语音短信耗时: {}", (System.currentTimeMillis() - beginTime));
    }

}

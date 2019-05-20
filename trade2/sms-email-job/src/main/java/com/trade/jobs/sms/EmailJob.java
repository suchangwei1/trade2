package com.trade.jobs.sms;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.util.StringUtils;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;

public class EmailJob implements Runnable {

    public static final String SMTP = "smtp";
    public static final String MAIL_NAME = "mailName";
    public static final String MAIL_PASSWORD = "mailPassword";
    public static final String MAIL_SENDER = "mailSender";
    private final Logger log = LoggerFactory.getLogger(getClass());
    private final int SUCCESS = 2;      // 邮件发送成功
    private final int FAIL = 3;         // 邮件发送失败
    private final int LIMIT = 800;      // 每次最大发送条数

    @Resource
    private ExecutorService executorService;
    @Resource
    private SystemArgsService systemArgsService;
    @Resource
    private JdbcTemplate jdbc;

    private static RequestConfig requestConfig;
    private static HttpClientBuilder httpClientBuilder;

    static {
        httpClientBuilder = HttpClientBuilder.create();
        httpClientBuilder.setKeepAliveStrategy((response, context) -> -1);
        httpClientBuilder.setMaxConnTotal(100);
        httpClientBuilder.setMaxConnPerRoute(100);

        RequestConfig.Builder reqConfigBuilder = RequestConfig.custom();
        reqConfigBuilder.setSocketTimeout(3000);
        reqConfigBuilder.setConnectTimeout(3000);
        reqConfigBuilder.setConnectionRequestTimeout(3000);
        requestConfig = reqConfigBuilder.build();
    }

    private void updateStatus(Object id, int status) {
        try {
            jdbc.update("UPDATE fvalidateemail SET fstatus = ?, fSendTime = ? WHERE fid = ?", status, new Timestamp(System.currentTimeMillis()), id);
        } catch (Exception e) {
            log.error("更新邮件状态失败", e);
        }
    }

    private void batSend() throws Exception {
        List<Map<String, Object>> list = jdbc.queryForList("SELECT * FROM fvalidateemail WHERE fstatus = 1 limit ?", LIMIT);
        if (list != null && list.size() > 0) {
            Map<String, String> account = systemArgsService.getSystemArgs(SMTP, MAIL_NAME, MAIL_PASSWORD, MAIL_SENDER);
            final CountDownLatch latch = new CountDownLatch(list.size());
            list.forEach(map -> {
                executorService.submit(() -> {
                    try {
                        int status = sendEmail(account, map.get("email"), map.get("fTitle"), map.get("fContent")) ? SUCCESS : FAIL;
                        updateStatus(map.get("fid"), status);
                    } catch (Exception e) {
                        e.printStackTrace();
                    } finally {
                        latch.countDown();
                    }
                });
            });
            latch.await();
        }
    }

    private boolean sendEmail(Map<String, String> account, Object toAddress, Object title, Object content) {
        try {
            if (toAddress == null || title == null || content == null) {
                return false;
            }
            log.debug("发送邮件到{}", toAddress);
            return send(account.get(SMTP), account.get(MAIL_NAME), account.get(MAIL_PASSWORD), account.get(MAIL_SENDER), toAddress.toString(), title.toString(), content.toString());
        } catch (Exception e) {
            log.error("发送邮件失败", e);
        }
        return false;
    }

    private boolean send(String smtp, String name, String password, String mailSender,
                               String toAddress, String title, String content) {
        boolean flag = false ;
        CloseableHttpClient httpClient = httpClientBuilder.build();
        HttpPost httpPost = new HttpPost("http://api.sendcloud.net/apiv2/mail/sendtemplate");
        httpPost.setConfig(requestConfig);

        JSONObject contentJson = JSON.parseObject(content);

        List params = new ArrayList();
        // 您需要登录SendCloud创建API_USER，使用API_USER和API_KEY才可以进行邮件的发送。
        params.add(new BasicNameValuePair("apiUser", name));
        params.add(new BasicNameValuePair("apiKey", password));
        params.add(new BasicNameValuePair("from", mailSender));
        params.add(new BasicNameValuePair("xsmtpapi", contentJson.getString("params")));
        params.add(new BasicNameValuePair("templateInvokeName", contentJson.getString("templ")));
        params.add(new BasicNameValuePair("fromName", contentJson.getString("fromName")));

        try {
            httpPost.setEntity(new UrlEncodedFormEntity(params, "UTF-8"));
            // 请求
            HttpResponse response = httpClient.execute(httpPost);
            // 处理响应
            if (response.getStatusLine().getStatusCode() == HttpStatus.SC_OK) { // 正常返回
                // 读取xml文档
                String result = EntityUtils.toString(response.getEntity());
                if (!StringUtils.hasText(result)) {
                    log.error("发送邮件失败，没接收到返回值");
                    return flag;
                }

                JSONObject resultJson = JSON.parseObject(result);
                if (200 == resultJson.getIntValue("statusCode")) {
                    flag = true;
                } else {
                    log.error("发送邮件失败，返回值为: {}", result);
                }
            }
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (ClientProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (Objects.nonNull(httpPost)) {
                httpPost.releaseConnection();
            }
        }

        return flag ;
    }

    @PostConstruct
    @Override
    public void run() {
        long beginTime = System.currentTimeMillis();
        log.debug("批量发送邮件");
        try {
            batSend();
        } catch (Exception e) {
            log.error("批量发送邮件 {}", e.toString());
        }
        log.debug("批量发送邮件耗时: {}", (System.currentTimeMillis() - beginTime));
    }

}

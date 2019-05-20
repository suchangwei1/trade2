package com.trade.push.utils;

import com.trade.push.core.SessionManager;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Map;

public class HttpUtils {

    private static HttpClientBuilder httpClientBuilder;

    private static HttpClient httpClient;

    private static RequestConfig requestConfig;

    private static final String DEFAULT_CHARSET = "utf-8";

    private static final int TIMEOUT = 5000;

    private static final String EMPTY_STRING = "";

    static {
        httpClientBuilder = HttpClientBuilder.create();
        httpClientBuilder.setKeepAliveStrategy((response, context) -> -1);
        httpClient = httpClientBuilder.build();
        RequestConfig.Builder reqConfigBuilder = RequestConfig.custom();
        reqConfigBuilder.setSocketTimeout(TIMEOUT);
        reqConfigBuilder.setConnectTimeout(TIMEOUT);
        reqConfigBuilder.setConnectionRequestTimeout(TIMEOUT);
        requestConfig = reqConfigBuilder.build();
    }

    /**
     * 发起Get请求
     *
     * @param url
     * @return 响应文本
     */
    public static String get(String url, String sessionId) {
        return get(url, null, sessionId);
    }

    /**
     * 发起Get请求
     *
     * @param url
     * @param map
     * @return 响应文本
     */
    public static String get(String url, Map<String, String> map, String sessionId) {
        String queryString = buildQueryString(map, DEFAULT_CHARSET);
        HttpGet httpGet = new HttpGet(url + queryString);
        httpGet.setHeader("Cookie", SessionManager.SESSION_ID_KEY + "=" + sessionId);
        httpGet.setConfig(requestConfig);
        return execute(httpGet, DEFAULT_CHARSET);
    }

    private static String execute(HttpRequestBase request, String charset) {
        // 统一执行请求，释放资源
        String result = null;
        try {
            HttpResponse response = httpClient.execute(request);
            if (response != null) {
                HttpEntity resEntity = response.getEntity();
                result = EntityUtils.toString(resEntity, charset);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        } finally {
            if (request != null) {
                request.releaseConnection();
            }
        }
        return result;
    }

    private static String buildQueryString(Map<String, String> map, String charset) {
        // 构建Get请求参数
        if (map != null && map.size() > 0) {
            StringBuilder sb = new StringBuilder();
            sb.append("?");
            map.forEach((key, val) -> {
                try {
                    String value = URLEncoder.encode(val, charset);
                    sb.append(key + "=" + value + "&");
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
            });
            return sb.substring(0, sb.length() - 1);
        }
        return EMPTY_STRING;
    }

}

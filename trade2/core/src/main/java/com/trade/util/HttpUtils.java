package com.trade.util;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpStatus;
import org.apache.commons.httpclient.MultiThreadedHttpConnectionManager;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.httpclient.methods.PostMethod;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;

public class HttpUtils {
    /**
     * 定义编码格式 UTF-8
     */
    public static final String DEFAUTL_ENCODING = "UTF-8";

    private static final String URL_PARAM_CONNECT_FLAG = "&";

    private static MultiThreadedHttpConnectionManager connectionManager = null;

    private static int connectionTimeOut = 5000;

    private static int socketTimeOut = 5000;

    private static int maxConnectionPerHost = 10;

    private static int maxTotalConnections = 50;

    private static HttpClient httpClient = null;

    private static Logger logger = LoggerFactory.getLogger(HttpUtils.class);

    public static HttpClient getInstance(){
        if(null == httpClient){
            connectionManager = new MultiThreadedHttpConnectionManager();
            connectionManager.getParams().setConnectionTimeout(connectionTimeOut);
            connectionManager.getParams().setSoTimeout(socketTimeOut);
            connectionManager.getParams().setDefaultMaxConnectionsPerHost(maxConnectionPerHost);
            connectionManager.getParams().setMaxTotalConnections(maxTotalConnections);
            httpClient = new HttpClient(connectionManager);
        }
        return httpClient;
    }

    public static String sendGetRequest(String uri, Map<String, Object> params){
        return sendGetRequest(uri, params, DEFAUTL_ENCODING);
    }

    public static String sendGetRequest(String uri, Map<String, Object> params, String encoding){
        String url = "";
        String response = "";
        GetMethod getMethod = null;

        try {
            StringBuilder paramsBuf = new StringBuilder();
            if(!CollectionUtils.isEmpty(params)){
                for(String key : params.keySet()){
                    paramsBuf.append(URL_PARAM_CONNECT_FLAG).append(key).append("=").append(URLEncoder.encode(params.get(key).toString(), encoding));
                }
                if(uri.indexOf("\\?") < 0){
                    url = uri + "?" + paramsBuf.deleteCharAt(0).toString();
                }else{
                    url = uri + paramsBuf.toString();
                }
            }else{
                url = uri;
            }

            getMethod = new GetMethod(url);
            getMethod.setRequestHeader("Content-Type", "application/x-www-form-urlencoded;charset=" + encoding);

            int statusCode = getInstance().executeMethod(getMethod);
            if(HttpStatus.SC_OK == statusCode){
                response = getMethod.getResponseBodyAsString();
            }else{
                logger.debug("发送(" + url + ")请求响应失败，响应码为：" + statusCode);
            }
        } catch (IOException e) {
            e.printStackTrace();
            logger.error("发送(" + url + ")请求响应失败", e);
        } finally {
            if(null != getMethod){
                getMethod.releaseConnection();
            }
        }
        return response;
    }

    public static String sendPostRequest(String uri, Map<String, Object> params){
        return sendPostRequest(uri, params, DEFAUTL_ENCODING);
    }

    public static String sendPostRequest(String uri, Map<String, Object> params, String encoding){
        String response = "";
        PostMethod postMethod = new PostMethod(uri);
        postMethod.setRequestHeader("Content-Type", "application/x-www-form-urlencoded;charset=" + encoding);

        if(!CollectionUtils.isEmpty(params)){
            for(String key : params.keySet()){
                postMethod.addParameter(key, params.get(key).toString());
            }
        }

        try {
            int statusCode = getInstance().executeMethod(postMethod);
            if(HttpStatus.SC_OK == statusCode){
                response = postMethod.getResponseBodyAsString();
            }else{
                logger.debug("发送" + uri + "请求响应失败，响应码为：" + statusCode);
            }
        } catch (IOException e) {
            e.printStackTrace();
            logger.error("发送" + uri + "请求响应失败", e);
        } finally {
            if(null != postMethod){
                postMethod.releaseConnection();
            }
        }
        return response;
    }

    public static JSONObject sendPostRequestForJson(String uri, Map<String, Object> params){
        String response = sendPostRequest(uri, params);
        if(StringUtils.isEmpty(response)){
            return new JSONObject();
        }

        return JSON.parseObject(response);
    }

    public static JSONObject sendGetRequestForJson(String uri, Map<String, Object> params){
        String response = sendGetRequest(uri, params);
        if(StringUtils.isEmpty(response)){
            return new JSONObject();
        }

        return JSON.parseObject(response);
    }

    public static void main(String[] args) {
        Map<String, Object> map = new HashMap();
        map.put("wd", "httpclient乱码");
        System.out.println(sendPostRequest("http://www.baidu.com/s?", map));
    }

    public static byte[] downloadFile(String url){
        byte[] response = new byte[0];
        GetMethod getMethod = null;

        try {
            getMethod = new GetMethod(url);
            getMethod.setRequestHeader("Content-Type", "application/x-www-form-urlencoded;charset=" + DEFAUTL_ENCODING);

            int statusCode = getInstance().executeMethod(getMethod);
            if(HttpStatus.SC_OK == statusCode){
                response = getMethod.getResponseBody();
            }else{
                logger.debug("发送(" + url + ")请求响应失败，响应码为：" + statusCode);
            }
        } catch (IOException e) {
            e.printStackTrace();
            logger.error("发送(" + url + ")请求响应失败", e);
        } finally {
            if(null != getMethod){
                getMethod.releaseConnection();
            }
        }
        return response;
    }
}

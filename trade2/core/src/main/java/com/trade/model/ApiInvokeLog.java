package com.trade.model;

import com.alibaba.fastjson.JSON;
import com.trade.util.StringUtils;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Collections;
import java.util.Date;
import java.util.Map;

@Entity
@Table(name = "fapi_invoke_log")
public class ApiInvokeLog implements Serializable {
    private int id;
    private int apiId;
    private int userId;
    private String ip;
    private String url;
    private String params;
    private String resultCode;
    private boolean isSucceed;
    private Date createTime;

    public ApiInvokeLog() {
    }

    public ApiInvokeLog(int apiId, int userId, String ip, String url, String params, String resultCode, boolean isSucceed) {
        this.apiId = apiId;
        this.userId = userId;
        this.ip = ip;
        this.url = url;
        this.params = params;
        this.resultCode = resultCode;
        this.isSucceed = isSucceed;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @Column(name = "api_id")
    public int getApiId() {
        return apiId;
    }

    public void setApiId(int apiId) {
        this.apiId = apiId;
    }

    @Column(name = "user_id")
    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    @Column(length = 30)
    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    @Column(length = 128)
    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    @Column(length = 256)
    public String getParams() {
        return params;
    }

    public void setParams(String params) {
        this.params = params;
    }

    @Column(length = 10, name = "result_code")
    public String getResultCode() {
        return resultCode;
    }

    public void setResultCode(String resultCode) {
        this.resultCode = resultCode;
    }

    @Transient
    public boolean isSucceed() {
        return isSucceed;
    }

    @Column(name = "is_succeed")
    public boolean getIsSucceed() {
        return isSucceed;
    }

    public void setIsSucceed(boolean succeed) {
        isSucceed = succeed;
    }

    @Column(name = "create_time")
    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    @Transient
    public Map<String, Object> getParamsMap(){
        if(StringUtils.hasText(this.params)){
            return JSON.parseObject(this.params, Map.class);
        }
        return Collections.emptyMap();
    }
}

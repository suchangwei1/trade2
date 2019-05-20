package com.trade.code;

import com.trade.Enum.CountLimitTypeEnum;
import com.trade.util.Constants;
import com.trade.util.Utils;

import java.io.Serializable;
import java.sql.Timestamp;

public class AuthCode implements Serializable {
    private String authStr; //手机号 邮箱号
    private String code;
    private int actionType;
    private Timestamp dateTime;
    private int times;      // 使用次数

    public AuthCode() {
    }

    public AuthCode(String code, int actionType) {
        this.code = code;
        this.actionType = actionType;
        this.dateTime = Utils.getTimestamp();
    }

    public AuthCode(String authStr, String code, int actionType) {
        this.authStr = authStr;
        this.code = "666666";//测试环境写死，方便测试。
        this.actionType = actionType;
        this.dateTime = Utils.getTimestamp();
    }

    public int getTimes() {
        return times;
    }

    public void setTimes(int times) {
        this.times = times;
    }

    public String getAuthStr() {
        return authStr;
    }

    public void setAuthStr(String authStr) {
        this.authStr = authStr;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public int getActionType() {
        return actionType;
    }

    public void setActionType(int actionType) {
        this.actionType = actionType;
    }

    public Timestamp getDateTime() {
        return dateTime;
    }

    public void setDateTime(Timestamp dateTime) {
        this.dateTime = dateTime;
    }

    public boolean imageCodeIsEnabled(String code){
        return System.currentTimeMillis() - this.dateTime.getTime() <= Constants.CAPTCHA_TIME_OUT && this.code.equalsIgnoreCase(code) && this.actionType == CountLimitTypeEnum.IMAGE_CAPTCHA;
    }

    public boolean isEnabled(String authStr, String code, int actionType){
        return System.currentTimeMillis() - this.dateTime.getTime() <= Constants.CAPTCHA_TIME_OUT && this.authStr.equalsIgnoreCase(authStr) && this.code.equals(code) && this.actionType == actionType;
    }

    public boolean isEnabled(String authStr, String code){
        return System.currentTimeMillis() - this.dateTime.getTime() <= Constants.CAPTCHA_TIME_OUT && this.authStr.equalsIgnoreCase(authStr) && this.code.equals(code);
    }



}

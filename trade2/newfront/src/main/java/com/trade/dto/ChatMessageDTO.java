package com.trade.dto;

import java.io.Serializable;
import java.util.Date;

public class ChatMessageDTO implements Serializable, Cloneable {

    private int userId;
    private String nickname;
    private String message;
    private Date createTime;

    public ChatMessageDTO() {
    }

    public ChatMessageDTO(int userId, String nickname, String message, Date createTime) {
        this.userId = userId;
        this.nickname = nickname;
        this.message = message;
        this.createTime = createTime;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    @Override
    public Object clone() throws CloneNotSupportedException {
        return super.clone();
    }
}

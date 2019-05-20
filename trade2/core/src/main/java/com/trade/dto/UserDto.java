package com.trade.dto;

import java.util.Date;

public class UserDto {
    private int userId;
    private Date timestamp;

    public UserDto() {
    }

    public UserDto(int userId, Date timestamp) {
        this.userId = userId;
        this.timestamp = timestamp;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public Date getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Date timestamp) {
        this.timestamp = timestamp;
    }
}

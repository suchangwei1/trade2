package com.trade.dto;

import java.util.Date;

public class OperateUserActionDTO {

    private int userId;
    private int status;     // UserStatusEnum
    private Date operTime;

    public OperateUserActionDTO() {
    }

    public OperateUserActionDTO(int userId, int status, Date operTime) {
        this.userId = userId;
        this.status = status;
        this.operTime = operTime;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public Date getOperTime() {
        return operTime;
    }

    public void setOperTime(Date operTime) {
        this.operTime = operTime;
    }
}

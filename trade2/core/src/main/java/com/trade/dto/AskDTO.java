package com.trade.dto;

import java.util.Date;

public class AskDTO implements Cloneable {

    private int id;
    private int type;
    private int status;
    private String desc;
    private String answer;
    private Date createTime;

    public AskDTO() {
    }

    public AskDTO(int id, int type, int status, String desc, String answer, Date createTime) {
        this.id = id;
        this.type = type;
        this.status = status;
        this.desc = desc;
        this.answer = answer;
        this.createTime = createTime;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public String getAnswer() {
        return answer;
    }

    public void setAnswer(String answer) {
        this.answer = answer;
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

    @Override
    public String toString() {
        return "AskDTO{" +
                "id=" + id +
                ", type=" + type +
                ", status=" + status +
                ", desc='" + desc + '\'' +
                ", answer='" + answer + '\'' +
                ", createTime=" + createTime +
                '}';
    }
}

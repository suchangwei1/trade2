package com.trade.model;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

/**
 * Author:xuelin
 * Company:招股金服
 * Date:2017/5/13
 * Desc:
 */
@Entity
@Table(name = "ico_swap_rate")
public class ICOSwapRate implements Serializable {

    private int id;
    private int icoId;
    private int coinType;
    private double amount;
    private Date startTime;
    private Date endTime;
    private Date createTime;
    private Date updateTime;
    private int version;

    public ICOSwapRate() {
    }

    public ICOSwapRate(int coinType) {
        this.coinType = coinType;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @Column(name = "ico_id")
    public int getIcoId() {
        return icoId;
    }

    public void setIcoId(int icoId) {
        this.icoId = icoId;
    }

    @Column(name = "coin_type")
    public int getCoinType() {
        return coinType;
    }

    public void setCoinType(int coinType) {
        this.coinType = coinType;
    }

    @Column(name = "amount")
    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    @Column(name = "start_time")
    public Date getStartTime() {
        return startTime;
    }

    public void setStartTime(Date startTime) {
        this.startTime = startTime;
    }

    @Column(name = "end_time")
    public Date getEndTime() {
        return endTime;
    }

    public void setEndTime(Date endTime) {
        this.endTime = endTime;
    }

    @Column(name = "create_time")
    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    @Column(name = "update_time")
    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    @Version
    public int getVersion() {
        return version;
    }

    public void setVersion(int version) {
        this.version = version;
    }

    @Transient
    public boolean isUseRMB() {
        return 0 == this.coinType;
    }

    @Override
    public String toString() {
        return "ICOSwapRate{" +
                "id=" + id +
                ", icoId=" + icoId +
                ", coin=" + coinType +
                ", amount=" + amount +
                ", startTime=" + startTime +
                ", endTime=" + endTime +
                ", createTime=" + createTime +
                ", updateTime=" + updateTime +
                ", version=" + version +
                '}';
    }
}

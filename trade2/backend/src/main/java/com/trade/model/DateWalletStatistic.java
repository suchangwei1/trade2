package com.trade.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Transient;
import javax.persistence.Version;
import java.io.Serializable;
import java.util.Date;

@Entity(name = "date_wallet_statistic")
public class DateWalletStatistic implements Serializable{
    private long id;
    private Date date;
    private int coinType;
    private double totalBalance;
    private double totalFreeze;
    private Date updateTime;
    private int version;

    public DateWalletStatistic() {}

    public DateWalletStatistic(Date date, int coinType, double totalBalance, double totalFreeze) {
        this.date = date;
        this.coinType = coinType;
        this.totalBalance = totalBalance;
        this.totalFreeze = totalFreeze;
        this.updateTime = new Date();
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    @Column
    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    @Column(name = "coin_type")
    public int getCoinType() {
        return coinType;
    }

    public void setCoinType(int coinType) {
        this.coinType = coinType;
    }

    @Column(name = "total_balance")
    public double getTotalBalance() {
        return totalBalance;
    }

    public void setTotalBalance(double totalBalance) {
        this.totalBalance = totalBalance;
    }

    @Column(name = "total_freeze")
    public double getTotalFreeze() {
        return totalFreeze;
    }

    public void setTotalFreeze(double totalFreeze) {
        this.totalFreeze = totalFreeze;
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
    public boolean isRMB(){
        return 0 == this.coinType;
    }
}

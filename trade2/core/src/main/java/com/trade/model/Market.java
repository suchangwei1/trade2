package com.trade.model;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "market")
public class Market {

    public static final int STATUS_Normal = 1;              // 正常
    public static final int STATUS_Abnormal = 0;            // 禁用
    public static final int TRADE_STATUS_Normal = 1;        // 正常交易
    public static final int TRADE_STATUS_Abnormal = 0;      // 禁用交易

    @Id
    @GeneratedValue
    private int id;
    @Column(name = "buy_id")
    private int buyId;
    @Column(name = "sell_id")
    private int sellId;
    @Column(name = "decimals")
    private int decimals;
    @Column(name = "buy_fee")
    private double buyFee;
    @Column(name = "sell_fee")
    private double sellFee;
    @Column(name = "min_count")
    private double minCount;
    @Column(name = "max_count")
    private double maxCount;
    @Column(name = "min_price")
    private double minPrice;
    @Column(name = "max_price")
    private double maxPrice;
    @Column(name = "min_money")
    private double minMoney;
    @Column(name = "max_money")
    private double maxMoney;
    @Column(name = "trade_time")
    private String tradeTime;
    @Column(name = "status")
    private int status;
    @Column(name = "trade_status")
    private int tradeStatus;
    @Column(name = "updown")
    private double updown;
    @Version
    private int version;


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getBuyId() {
        return buyId;
    }

    public void setBuyId(int buyId) {
        this.buyId = buyId;
    }

    public int getSellId() {
        return sellId;
    }

    public void setSellId(int sellId) {
        this.sellId = sellId;
    }

    public int getDecimals() {
        return decimals;
    }

    public void setDecimals(int decimals) {
        this.decimals = decimals;
    }

    public double getBuyFee() {
        return buyFee;
    }

    public void setBuyFee(double buyFee) {
        this.buyFee = buyFee;
    }

    public double getSellFee() {
        return sellFee;
    }

    public void setSellFee(double sellFee) {
        this.sellFee = sellFee;
    }

    public double getMinCount() {
        return minCount;
    }

    public void setMinCount(double minCount) {
        this.minCount = minCount;
    }

    public double getMaxCount() {
        return maxCount;
    }

    public void setMaxCount(double maxCount) {
        this.maxCount = maxCount;
    }

    public double getMinMoney() {
        return minMoney;
    }

    public void setMinMoney(double minMoney) {
        this.minMoney = minMoney;
    }

    public double getMaxMoney() {
        return maxMoney;
    }

    public void setMaxMoney(double maxMoney) {
        this.maxMoney = maxMoney;
    }

    public String getTradeTime() {
        return tradeTime;
    }

    public void setTradeTime(String tradeTime) {
        this.tradeTime = tradeTime;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public int getTradeStatus() {
        return tradeStatus;
    }

    public void setTradeStatus(int tradeStatus) {
        this.tradeStatus = tradeStatus;
    }

    public double getUpdown() {
        return updown;
    }

    public void setUpdown(double updown) {
        this.updown = updown;
    }

    public double getMinPrice() {
        return minPrice;
    }

    public void setMinPrice(double minPrice) {
        this.minPrice = minPrice;
    }

    public double getMaxPrice() {
        return maxPrice;
    }

    public void setMaxPrice(double maxPrice) {
        this.maxPrice = maxPrice;
    }

    public int getVersion() {
        return version;
    }

    public void setVersion(int version) {
        this.version = version;
    }
}

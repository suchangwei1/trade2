package com.trade.model;

import com.trade.util.Utils;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "trade_fees_share")
public class TradeFeesShare {

    public static final int BUY = 0;              // 买入
    public static final int SELL = 1;            // 卖出

    private int id;
    private Fuser parent;
    private Fuser child;
    private int market;
    private Date createTime;
    private Date shareTime;
    private int type;
    private Double amount;
    private String coin;

    public TradeFeesShare(){}

    public TradeFeesShare(Fuser parent, Fuser child, int market, int type, Date shareTime, String coin, double amount) {
        this.parent = parent;
        this.child = child;
        this.market = market;
        this.shareTime = shareTime;
        this.createTime = Utils.getTimestamp();
        this.coin = coin;
        this.amount = amount;
        this.type = type;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @ManyToOne()
    @JoinColumn(name = "parent_id")
    public Fuser getParent() {
        return parent;
    }

    public void setParent(Fuser parent) {
        this.parent = parent;
    }

    @ManyToOne()
    @JoinColumn(name = "child_id")
    public Fuser getChild() {
        return child;
    }

    public void setChild(Fuser child) {
        this.child = child;
    }

    @Column(name = "market_id")
    public int getMarket() {
        return market;
    }

    public void setMarket(int market) {
        this.market = market;
    }

    @Column(name = "create_time")
    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    @Column(name = "share_time")
    public Date getShareTime() {
        return shareTime;
    }

    public void setShareTime(Date shareTime) {
        this.shareTime = shareTime;
    }

    @Column(name = "type")
    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    @Column(name = "amount")
    public Double getAmount() {
        return amount;
    }

    public void setAmount(Double amount) {
        this.amount = amount;
    }

    @Column(name = "coin")
    public String getCoin() {
        return coin;
    }

    public void setCoin(String coin) {
        this.coin = coin;
    }
}

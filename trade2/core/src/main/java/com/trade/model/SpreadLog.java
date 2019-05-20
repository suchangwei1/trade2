package com.trade.model;

import com.trade.util.Utils;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "spread_log")
public class SpreadLog {
    private int id;
    private Fuser parent;
    private Fuser child;
    private String parentRewardCoin;
    private double parentRewardNum;
    private String childRewardCoin;
    private double childRewardNum;
    private Date createTime;
    private Date updateTime;

    public SpreadLog(){}

    public SpreadLog(Fuser parent, Fuser child, String parentRewardCoin, double parentRewardNum, String childRewardCoin, double childRewardNum) {
        this.parent = parent;
        this.child = child;
        this.parentRewardCoin = parentRewardCoin;
        this.parentRewardNum = parentRewardNum;
        this.childRewardCoin = childRewardCoin;
        this.childRewardNum = childRewardNum;
        this.createTime = Utils.getTimestamp();
        this.updateTime = Utils.getTimestamp();
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

    @Column(name = "parent_Reward_num")
    public double getParentRewardNum() {
        return parentRewardNum;
    }

    public void setParentRewardNum(double parentRewardNum) {
        this.parentRewardNum = parentRewardNum;
    }

    @Column(name = "child_Reward_num")
    public double getChildRewardNum() {
        return childRewardNum;
    }

    public void setChildRewardNum(double childRewardNum) {
        this.childRewardNum = childRewardNum;
    }

    @Column(name = "parent_reward_coin")
    public String getParentRewardCoin() {
        return parentRewardCoin;
    }

    public void setParentRewardCoin(String parentRewardCoin) {
        this.parentRewardCoin = parentRewardCoin;
    }

    @Column(name = "child_reward_coin")
    public String getChildRewardCoin() {
        return childRewardCoin;
    }

    public void setChildRewardCoin(String childRewardCoin) {
        this.childRewardCoin = childRewardCoin;
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
}

package com.trade.model;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.trade.Enum.ICOStatusEnum;
import com.trade.util.Utils;

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
@Table(name = "ico")
public class ICO implements Serializable {

    private int id;
    private String name;                    // 项目名称
    private double amount;                  // 币份额
    private double supplyAmount;            // 平台补充份额
    private double rightAmount;             // 已认购量
    private double limitAmount;             // 限购数量
    private double minBuyAmount;            // 最小认购量
    private double requiteRate;             // 回报比例
    private int supportCount;               // 认购次数
    private String imageUrl;                // 项目图片
    private String declaration;             // 项目描述
    private String description;             // 项目描述
    private ICOStatusEnum status;           // 状态
    private Date createTime;                // 创建时间
    private Date updateTime;                // 更新时间
    private Date startTime;                 // 项目开始时间
    private Date endTime;                   // 项目结束时间
    private String jsonExt;                 // json扩展字段
    private boolean delete;                 // 是否删除
    private int version;                    // 版本号

    public ICO() {
    }

    public ICO(int id, String name, double amount, double supplyAmount, double rightAmount, double limitAmount, double minBuyAmount, double requiteRate, int supportCount, String imageUrl, String declaration, ICOStatusEnum status, String jsonExt, Date createTime, Date updateTime, Date startTime, Date endTime, int version) {
        this.id = id;
        this.name = name;
        this.amount = amount;
        this.supplyAmount = supplyAmount;
        this.rightAmount = rightAmount;
        this.limitAmount = limitAmount;
        this.minBuyAmount = minBuyAmount;
        this.requiteRate = requiteRate;
        this.supportCount = supportCount;
        this.imageUrl = imageUrl;
        this.declaration = declaration;
        this.status = status;
        this.jsonExt = jsonExt;
        this.createTime = createTime;
        this.updateTime = updateTime;
        this.startTime = startTime;
        this.endTime = endTime;
        this.version = version;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @Column(length = 128)
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Column
    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    @Column(name = "supply_amount")
    public double getSupplyAmount() {
        return supplyAmount;
    }

    public void setSupplyAmount(double supplyAmount) {
        this.supplyAmount = supplyAmount;
    }

    @Column(name = "right_amount")
    public double getRightAmount() {
        return rightAmount;
    }

    public void setRightAmount(double rightAmount) {
        this.rightAmount = rightAmount;
    }

    @Column(name = "limit_amount")
    public double getLimitAmount() {
        return limitAmount;
    }

    public void setLimitAmount(double limitAmount) {
        this.limitAmount = limitAmount;
    }

    @Column(name = "min_buy_amount")
    public double getMinBuyAmount() {
        return minBuyAmount;
    }

    public void setMinBuyAmount(double minBuyAmount) {
        this.minBuyAmount = minBuyAmount;
    }

    @Column(name = "requite_rate")
    public double getRequiteRate() {
        return requiteRate;
    }

    public void setRequiteRate(double requiteRate) {
        this.requiteRate = requiteRate;
    }

    @Column(name = "support_count")
    public int getSupportCount() {
        return supportCount;
    }

    public void setSupportCount(int supportCount) {
        this.supportCount = supportCount;
    }

    @Column(name = "image_url", length = 128)
    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    @Column(length = 256)
    public String getDeclaration() {
        return declaration;
    }

    public void setDeclaration(String declaration) {
        this.declaration = declaration;
    }

    @Column
    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Column
    public ICOStatusEnum getStatus() {
        return status;
    }

    public void setStatus(ICOStatusEnum status) {
        this.status = status;
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

    @Column(name = "is_delete")
    public boolean isDelete() {
        return delete;
    }

    public void setDelete(boolean delete) {
        this.delete = delete;
    }

    @Column(name = "json_ext")
    public String getJsonExt() {
        return jsonExt;
    }

    public void setJsonExt(String jsonExt) {
        this.jsonExt = jsonExt;
    }

    @Version
    public int getVersion() {
        return version;
    }

    public void setVersion(int version) {
        this.version = version;
    }

    @Transient
    public JSONObject getExtObject() {
        return JSON.parseObject(getJsonExt());
    }

    @Transient
    public boolean isInTradeTime() {
        long curTime = Utils.getTimestamp().getTime();
        return curTime >= this.startTime.getTime() && curTime <= this.endTime.getTime();
    }

    @Transient
    public boolean isSuccess() {
        return ICOStatusEnum.SUCCESS.getIndex() == this.status.getIndex();
    }

    @Override
    public String toString() {
        return "ICO{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", amount=" + amount +
                ", supplyAmount=" + supplyAmount +
                ", rightAmount=" + rightAmount +
                ", limitAmount=" + limitAmount +
                ", minBuyAmount=" + minBuyAmount +
                ", requiteRate=" + requiteRate +
                ", supportCount=" + supportCount +
                ", imageUrl='" + imageUrl + '\'' +
                ", declaration='" + declaration + '\'' +
                ", description='" + description + '\'' +
                ", status=" + status +
                ", createTime=" + createTime +
                ", updateTime=" + updateTime +
                ", startTime=" + startTime +
                ", endTime=" + endTime +
                ", jsonExt='" + jsonExt + '\'' +
                ", delete=" + delete +
                ", version=" + version +
                '}';
    }
}

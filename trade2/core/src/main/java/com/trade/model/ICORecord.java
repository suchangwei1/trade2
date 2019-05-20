package com.trade.model;

import com.trade.Enum.ICORecordStatusEnum;

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
@Table(name = "ico_record")
public class ICORecord implements Serializable {
    private int id;
    private int userId;                     // 认购用户
    private int icoId;                      // 认购项目
    private int swapCoinType;               // 交换币种
    private double perSwapAmount;              // 单位兑换币量   BTC兑换锎钛币 1BTC=200CTC
    private double amount;                  // 获得数量
    private double swapAmount;              // 交换数量
    private ICORecordStatusEnum status;     // ico状态
    private String remark;                  // 备注
    private Date createTime;                // 创建时间
    private Date updateTime;                // 更新时间
    private boolean delete;                 // 是否删除
    private int version;                    // 版本号

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @Column(name = "user_id")
    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    @Column(name = "ico_id")
    public int getIcoId() {
        return icoId;
    }

    public void setIcoId(int icoId) {
        this.icoId = icoId;
    }

    @Column(name = "swap_coin_type")
    public int getSwapCoinType() {
        return swapCoinType;
    }

    public void setSwapCoinType(int swapCoinType) {
        this.swapCoinType = swapCoinType;
    }

    @Column(name = "per_swap_amount")
    public double getPerSwapAmount() {
        return perSwapAmount;
    }

    public void setPerSwapAmount(double perSwapAmount) {
        this.perSwapAmount = perSwapAmount;
    }

    @Column(name = "amount")
    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    @Column(name = "swap_amount")
    public double getSwapAmount() {
        return swapAmount;
    }

    public void setSwapAmount(double swapAmount) {
        this.swapAmount = swapAmount;
    }

    @Column
    public ICORecordStatusEnum getStatus() {
        return status;
    }

    public void setStatus(ICORecordStatusEnum status) {
        this.status = status;
    }

    @Column(length = 128)
    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
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

    @Column(name = "is_delete")
    public boolean isDelete() {
        return delete;
    }

    public void setDelete(boolean delete) {
        this.delete = delete;
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
        return 0 == this.swapCoinType;
    }


    @Override
    public String toString() {
        return "ICORecord{" +
                "id=" + id +
                ", user=" + userId +
                ", icoId=" + icoId +
                ", swapCoinType=" + swapCoinType +
                ", amount=" + amount +
                ", swapAmount=" + swapAmount +
                ", status=" + status +
                ", remark='" + remark + '\'' +
                ", createTime=" + createTime +
                ", updateTime=" + updateTime +
                ", delete=" + delete +
                ", version=" + version +
                '}';
    }
}

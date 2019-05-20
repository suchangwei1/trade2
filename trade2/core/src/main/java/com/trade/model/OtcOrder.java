package com.trade.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.trade.util.Utils;

import javax.persistence.*;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "otc_order")
@JsonIgnoreProperties({
        "otcOrderLog",
        "fvirtualcointype",
})
public class OtcOrder {

    private int id;
    private int type;
    private double price;
    private double amount;
    private double minAmount;
    private double maxAmount;
    private String payWay;
    private Fuser fuser;
    private int status = 0; // 0 挂单中 1 成功 2撤销
    private double successAmount = 0;
    private Fvirtualcointype fvirtualcointype;
    private Date createTime;
    private Date updateTime;
    private double frozenAmount = 0;
    private Set<OtcOrderLog> otcOrderLog = new HashSet<>(0);
    private int version;
    private String pay_s;
    private String status_s;
    private String shortName;
    public OtcOrder(){}

    public OtcOrder(int type, double price, double amount, double minAmount, double maxAmount, String payWay, Fuser fuser, Fvirtualcointype fvirtualcointype) {
        this.type = type;
        this.price = price;
        this.amount = amount;
        this.minAmount = minAmount;
        this.maxAmount = maxAmount;
        this.payWay = payWay;
        this.fuser = fuser;
        this.fvirtualcointype = fvirtualcointype;
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

    @Column(name = "type")
    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    @Column(name = "price")
    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    @Column(name = "amount")
    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    @Column(name = "min_amount")
    public double getMinAmount() {
        return minAmount;
    }

    public void setMinAmount(double minAmount) {
        this.minAmount = minAmount;
    }

    @Column(name = "max_amount")
    public double getMaxAmount() {
        return maxAmount;
    }

    public void setMaxAmount(double maxAmount) {
        this.maxAmount = maxAmount;
    }

    @Column(name = "pay_way")
    public String getPayWay() {
        return payWay;
    }

    public void setPayWay(String payWay) {
        this.payWay = payWay;
    }

    @ManyToOne()
    @JoinColumn(name = "uid")
    public Fuser getFuser() {
        return fuser;
    }

    public void setFuser(Fuser fuser) {
        this.fuser = fuser;
    }

    @ManyToOne()
    @JoinColumn(name = "cid")
    public Fvirtualcointype getFvirtualcointype() {
        return fvirtualcointype;
    }

    public void setFvirtualcointype(Fvirtualcointype fvirtualcointype) {
        this.fvirtualcointype = fvirtualcointype;
    }

    @Column(name = "status")
    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    @Column(name = "success_amount")
    public double getSuccessAmount() {
        return successAmount;
    }

    public void setSuccessAmount(double successAmount) {
        this.successAmount = successAmount;
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

    @Column(name = "frozen_amount")
    public double getFrozenAmount() {
        return frozenAmount;
    }

    public void setFrozenAmount(double frozenAmount) {
        this.frozenAmount = frozenAmount;
    }

    @OneToMany(mappedBy = "fuser")
    public Set<OtcOrderLog> getOtcOrderLog() {
        return otcOrderLog;
    }

    public void setOtcOrderLog(Set<OtcOrderLog> otcOrderLog) {
        this.otcOrderLog = otcOrderLog;
    }

    @Version
    public int getVersion() {
        return version;
    }

    public void setVersion(int version) {
        this.version = version;
    }
    @Transient
    public String getShortName() {
        return shortName;
    }

    public void setShortName(String shortName) {
        this.shortName = shortName;
    }

    @Transient
    public String getPay_s() {
        String[] arr = this.getPayWay().split(",");
        String result = "";
        for(int i = 0; i < arr.length; i++){
            switch (arr[i]){
                case "0": result += "银行卡 ";break;
                case "1": result += "支付宝 ";break;
                case "2": result += "微信 ";break;
                default: break;
            }
        }
        return result;
    }

    public void setPay_s(String pay_s) {
        this.pay_s = pay_s;
    }

    @Transient
    public String getStatus_s() {
        switch (getStatus()){
            case 0: return "待成交";
            case 1: return "已完成";
            case 2: return "已撤销";
            default: return "";
        }
    }

    public void setStatus_s(String status_s) {
        this.status_s = status_s;
    }


    @Override
    public String toString() {
        return "OtcOrder{" +
                "id=" + id +
                ", type=" + type +
                ", price=" + price +
                ", amount=" + amount +
                ", minAmount=" + minAmount +
                ", maxAmount=" + maxAmount +
                ", payWay='" + payWay + '\'' +
                ", fuser=" + fuser +
                ", fvirtualcointype=" + fvirtualcointype +
                '}';
    }
}

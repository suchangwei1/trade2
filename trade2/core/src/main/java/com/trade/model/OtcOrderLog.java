package com.trade.model;

import com.trade.util.Utils;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "otc_order_log")
public class OtcOrderLog {

    private int id;
    private OtcOrder otcOrder;
    private double amount;
    private int status;
    private Date createTime;
    private Date updateTime;
    private String payWay;
    private Fuser fuser;
    private int version;
    private String buyerNote;
    private String sllerNote;
    private String pay_s;
    private String status_s;
    private String shortName;
    public OtcOrderLog(){}

    public OtcOrderLog(OtcOrder otcOrder, double amount, Fuser fuser, String payWay) {
        this.otcOrder = otcOrder;
        this.amount = amount;
        this.status = 0; //0待付款 1待放币 2已完成 3已撤销 4冻结中 5申诉中
        this.createTime = Utils.getTimestamp();
        this.updateTime = Utils.getTimestamp();
        this.payWay = payWay;
        this.fuser = fuser;
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
    @JoinColumn(name = "order_id")
    public OtcOrder getOtcOrder() {
        return otcOrder;
    }

    public void setOtcOrder(OtcOrder otcOrder) {
        this.otcOrder = otcOrder;
    }

    @Column(name = "amount")
    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    @Column(name = "status")
    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
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

    @Version
    public int getVersion() {
        return version;
    }

    public void setVersion(int version) {
        this.version = version;
    }

    @Column(name = "buyer_note")
    public String getBuyerNote() {
        return buyerNote;
    }

    public void setBuyerNote(String buyerNote) {
        this.buyerNote = buyerNote;
    }

    @Column(name = "seller_note")
    public String getSllerNote() {
        return sllerNote;
    }

    public void setSllerNote(String sllerNote) {
        this.sllerNote = sllerNote;
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
            case 0: return "待付款";
            case 1: return "待放币";
            case 2: return "已完成";
            case 3: return "已撤销";
            case 4: return "冻结中";
            case 5: return "申诉中";
            default: return "";
        }
    }
    @Transient
    public String getShortName() {
        return shortName;
    }

    public void setShortName(String shortName) {
        this.shortName = shortName;
    }

    public void setStatus_s(String status_s) {
        this.status_s = status_s;
    }
}

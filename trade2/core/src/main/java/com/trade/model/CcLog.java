package com.trade.model;

import com.trade.Enum.CcLogStatusEnum;
import com.trade.Enum.CcLogTypeEnum;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "cclog")
public class CcLog implements java.io.Serializable {

    private int id;
    private Fuser buyyer;
    private CcUser seller;
    private Double amount;
    private Double price;
    private CcLogStatusEnum status;
    private CcLogTypeEnum type;
    private Date createTime;
    private Date updateTime;
    private Fvirtualcointype coin;

    public CcLog(){}

    public CcLog(Fuser buyyer, CcUser seller, Double amount, Double price, CcLogStatusEnum status, CcLogTypeEnum type, Date createTime, Date updateTime, Fvirtualcointype coin) {
        this.buyyer = buyyer;
        this.seller = seller;
        this.amount = amount;
        this.price = price;
        this.status = status;
        this.type = type;
        this.createTime = createTime;
        this.updateTime = updateTime;
        this.coin = coin;
    }

    @GenericGenerator(name = "generator", strategy = "identity")
    @Id
    @GeneratedValue(generator = "generator")
    @Column(name = "id", unique = true, nullable = false)
    public int getId() {
        return this.id;
    }

    public void setId(int id) {
        this.id = id;
    }


    @ManyToOne()
    @JoinColumn(name = "uid")
    public Fuser getBuyyer() {
        return buyyer;
    }

    public void setBuyyer(Fuser buyyer) {
        this.buyyer = buyyer;
    }

    @ManyToOne()
    @JoinColumn(name = "sid")
    public CcUser getSeller() {
        return seller;
    }

    public void setSeller(CcUser seller) {
        this.seller = seller;
    }

    @Column(name = "amount")
    public Double getAmount() {
        return amount;
    }

    public void setAmount(Double amount) {
        this.amount = amount;
    }

    @Column(name = "price")
    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    @Column(name = "status")
    @Enumerated(EnumType.ORDINAL)
    public CcLogStatusEnum getStatus() {
        return status;
    }

    public void setStatus(CcLogStatusEnum status) {
        this.status = status;
    }

    @Column(name = "type")
    @Enumerated(EnumType.ORDINAL)
    public CcLogTypeEnum getType() {
        return type;
    }

    public void setType(CcLogTypeEnum type) {
        this.type = type;
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

    @ManyToOne()
    @JoinColumn(name = "coin")
    public Fvirtualcointype getCoin() {
        return coin;
    }

    public void setCoin(Fvirtualcointype coin) {
        this.coin = coin;
    }
}
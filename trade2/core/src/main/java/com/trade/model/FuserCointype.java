package com.trade.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.sql.Timestamp;

/**
 * Created by echoHU on 2018/6/12 0012.
 */
@Entity
@Table(name = "fuser_cointype")
public class FuserCointype {

    private int fid;
    @JsonIgnore
    private Fuser fuser;//用户ID

    private Market market;//交易对ID

    private Timestamp fcreateTime;
    private int fstatus; // '0：不收藏 ，1：收藏',
    private Timestamp flastUpdatTime;

    public FuserCointype(int fid, Fuser fuser, Market market, Timestamp fcreateTime, int fstatus, Timestamp flastUpdatTime) {
        this.fid = fid;
        this.fuser = fuser;
        this.market = market;
        this.fcreateTime = fcreateTime;
        this.fstatus = fstatus;
        this.flastUpdatTime = flastUpdatTime;
    }

    public FuserCointype() {
        super();
    }

    @GenericGenerator(name = "generator", strategy = "native")
    @Id
    @GeneratedValue(generator = "generator")
    @Column(name = "id", unique = true, nullable = false)
    public int getFid() {
        return fid;
    }

    public void setFid(int fid) {
        this.fid = fid;
    }
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "fus_fid")
    public Fuser getFuser() {
        return fuser;
    }

    public void setFuser(Fuser fuser) {
        this.fuser = fuser;
    }
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "fvi_fid")
    public Market getMarket() {
        return market;
    }

    public void setMarket(Market market) {
        this.market = market;
    }

    @Column(name = "fcreate_time", length = 0)
    public Timestamp getFcreateTime() {
        return fcreateTime;
    }

    public void setFcreateTime(Timestamp fcreateTime) {
        this.fcreateTime = fcreateTime;
    }
    @Column(name = "fstatus", length = 0)
    public int getFstatus() {
        return fstatus;
    }

    public void setFstatus(int fstatus) {
        this.fstatus = fstatus;
    }
    @Column(name = "flast_updatTime", length = 0)
    public Timestamp getFlastUpdatTime() {
        return flastUpdatTime;
    }

    public void setFlastUpdatTime(Timestamp flastUpdatTime) {
        this.flastUpdatTime = flastUpdatTime;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        FuserCointype that = (FuserCointype) o;

        if (fid != that.fid) return false;
        if (fstatus != that.fstatus) return false;
        if (fuser != null ? !fuser.equals(that.fuser) : that.fuser != null) return false;
        if (market != null ? !market.equals(that.market) : that.market != null) return false;
        if (fcreateTime != null ? !fcreateTime.equals(that.fcreateTime) : that.fcreateTime != null) return false;
        if (flastUpdatTime != null ? !flastUpdatTime.equals(that.flastUpdatTime) : that.flastUpdatTime != null)
            return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = fid;
        result = 31 * result + (fuser != null ? fuser.hashCode() : 0);
        result = 31 * result + (market != null ? market.hashCode() : 0);
        result = 31 * result + (fcreateTime != null ? fcreateTime.hashCode() : 0);
        result = 31 * result + fstatus;
        result = 31 * result + (flastUpdatTime != null ? flastUpdatTime.hashCode() : 0);
        return result;
    }
}


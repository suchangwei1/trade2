package com.trade.dto;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.Date;

public class FentrustlogData implements Serializable, Comparable {

    private int fid;
    private Date fcreateTime;      // 成交时间
    private int fEntrustType;           // 交易类型
    private int fviFid;                 // 货币ID
    private double fprize;              // 成交价
    private double fcount;              // 成交价
    private double famount;             // 成交额
    private boolean isactive;

    public FentrustlogData() {
    }

    public boolean isactive() {
        return isactive;
    }

    public void setIsactive(boolean isactive) {
        this.isactive = isactive;
    }

    public int getFid() {
        return fid;
    }

    public void setFid(int fid) {
        this.fid = fid;
    }

    public Timestamp getFcreateTime() {
        return new Timestamp(fcreateTime.getTime());
    }

    public void setFcreateTime(Timestamp fcreateTime) {
        this.fcreateTime = fcreateTime;
    }

    public int getfEntrustType() {
        return fEntrustType;
    }

    public void setfEntrustType(int fEntrustType) {
        this.fEntrustType = fEntrustType;
    }

    public double getFprize() {
        return fprize;
    }

    public void setFprize(double fprize) {
        this.fprize = fprize;
    }

    public double getFcount() {
        return fcount;
    }

    public void setFcount(double fcount) {
        this.fcount = fcount;
    }

    public int getFviFid() {
        return fviFid;
    }

    public void setFviFid(int fviFid) {
        this.fviFid = fviFid;
    }

    public void setFcreateTime(Date fcreateTime) {
        this.fcreateTime = fcreateTime;
    }

    public double getFamount() {
        return famount;
    }

    public void setFamount(double famount) {
        this.famount = famount;
    }

    @Override
    public int compareTo(Object o) {
        FentrustlogData o1 = this;
        FentrustlogData o2 = (FentrustlogData) o;
        boolean flag = o1.fid == o2.fid && o1.fid != 0 ;
        if(flag){
            return 0 ;
        }
        long ret = o2.fid - o1.fid;
//        int ret = (int) (o2.getFcreateTime().getTime() - o1.getFcreateTime().getTime());
        if (ret > 0) {
            ret = 1;
        } else if (ret < 0) {
            ret = -1;
        }
        return (int) ret;
    }

    @Override
    public String toString() {
        return "FentrustlogData{" +
                "fid=" + fid +
                ", fcreateTime=" + fcreateTime +
                ", fEntrustType=" + fEntrustType +
                ", fviFid=" + fviFid +
                ", fprize=" + fprize +
                ", fcount=" + fcount +
                ", famount=" + famount +
                '}';
    }
}

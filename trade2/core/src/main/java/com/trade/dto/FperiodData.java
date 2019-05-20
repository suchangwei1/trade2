package com.trade.dto;

import com.trade.model.Fperiod;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.Date;

public class FperiodData implements Serializable, Comparable {

    private int fid;
    private double fkai;        // 开
    private double fgao;        // 高
    private double fdi;         // 低
    private double fshou;       // 收
    private double fliang;      // 量
    private Date ftime;         // 时间
    private int fviFid;         // 币ID
    private int key;            // 周期

    public FperiodData() {
    }

    public FperiodData(int id, int key, Fperiod fperiod) {
        if (fperiod.getFid() != null) {
            this.setFid(fperiod.getFid());
        }
        this.setFdi(fperiod.getFdi());
        this.setFgao(fperiod.getFgao());
        this.setFkai(fperiod.getFkai());
        this.setFliang(fperiod.getFliang());
        this.setFshou(fperiod.getFshou());
        this.setFtime(fperiod.getFtime());
        this.setFviFid(id);
        this.setKey(key);
    }

    public int getFid() {
        return fid;
    }

    public void setFid(int fid) {
        this.fid = fid;
    }

    public double getFkai() {
        return fkai;
    }

    public void setFkai(double fkai) {
        this.fkai = fkai;
    }

    public double getFgao() {
        return fgao;
    }

    public void setFgao(double fgao) {
        this.fgao = fgao;
    }

    public double getFdi() {
        return fdi;
    }

    public void setFdi(double fdi) {
        this.fdi = fdi;
    }

    public double getFshou() {
        return fshou;
    }

    public void setFshou(double fshou) {
        this.fshou = fshou;
    }

    public double getFliang() {
        return fliang;
    }

    public void setFliang(double fliang) {
        this.fliang = fliang;
    }

    public Timestamp getFtime() {
        return new Timestamp(ftime.getTime());
    }

    public void setFtime(Timestamp ftime) {
        this.ftime = ftime;
    }

    public int getFviFid() {
        return fviFid;
    }

    public void setFviFid(int fviFid) {
        this.fviFid = fviFid;
    }

    public int getKey() {
        return key;
    }

    public void setKey(int key) {
        this.key = key;
    }

    @Override
    public int compareTo(Object o) {
        FperiodData o1 = this;
        FperiodData o2 = (FperiodData) o;
        return o1.ftime.compareTo(o2.ftime);
    }
}

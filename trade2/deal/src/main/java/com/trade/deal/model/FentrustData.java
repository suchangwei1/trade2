package com.trade.deal.model;

import java.io.Serializable;
import java.sql.Timestamp;

/**
 * 交易数据
 * @author 林超
 * @version 1.0
 */
public class FentrustData implements Serializable, Comparable, Cloneable {

	private int fuid;
    private Integer fid;            // 委托单ID
	private Double fleftCount;  	// 未成交数量
	private double famount;
	private Double fprize;      	// 成交价格
    private Integer fviFid;         // 货币ID
    private Integer fentrustType;   // EntrustTypeEnum
    private Integer deep;			//深度
	private int fstatus;
	private int robotStatus;
	private double fsuccessAmount;
	private double fcount;
	private Timestamp fcreateTime;
	private Timestamp flastUpdatTime;
	private double ffees ;
	private double fleftfees;
	private int walletId;			// 钱包ID
	private boolean fneedFee = true;		// 是否需要手续费
	private int flevel;

	public boolean isFneedFee() {
		return fneedFee;
	}

	public void setFneedFee(boolean fneedFee) {
		this.fneedFee = fneedFee;
	}

	public int getFlevel() {
		return flevel;
	}

	public void setFlevel(int flevel) {
		this.flevel = flevel;
	}

	public int getWalletId() {
		return walletId;
	}

	public void setWalletId(int walletId) {
		this.walletId = walletId;
	}

	public Timestamp getFcreateTime() {
		return fcreateTime;
	}

	public void setFcreateTime(Timestamp fcreateTime) {
		this.fcreateTime = fcreateTime;
	}

	public int getFuid() {
		return fuid;
	}

	public void setFuid(int fuid) {
		this.fuid = fuid;
	}

	public double getFamount() {
		return famount;
	}

	public void setFamount(double famount) {
		this.famount = famount;
	}

	public double getFfees() {
		return ffees;
	}

	public void setFfees(double ffees) {
		this.ffees = ffees;
	}

	public double getFleftfees() {
		return fleftfees;
	}

	public void setFleftfees(double fleftfees) {
		this.fleftfees = fleftfees;
	}

	public Timestamp getFlastUpdatTime() {
		return flastUpdatTime;
	}

	public void setFlastUpdatTime(Timestamp flastUpdatTime) {
		this.flastUpdatTime = flastUpdatTime;
	}
	public double getFsuccessAmount() {
		return fsuccessAmount;
	}

	public void setFsuccessAmount(double fsuccessAmount) {
		this.fsuccessAmount = fsuccessAmount;
	}

	public double getFcount() {
		return fcount;
	}

	public void setFcount(double fcount) {
		this.fcount = fcount;
	}

	public int getFstatus() {
		return fstatus;
	}

	public void setFstatus(int fstatus) {
		this.fstatus = fstatus;
	}

	public int getRobotStatus() {
		return robotStatus;
	}

	public void setRobotStatus(int robotStatus) {
		this.robotStatus = robotStatus;
	}

	public Integer getFid() {
		return fid;
	}

	public void setFid(Integer fid) {
		this.fid = fid;
	}

    public Double getFleftCount() {
		return fleftCount;
	}

	public void setFleftCount(Double fleftCount) {
		this.fleftCount = fleftCount;
	}

	public Double getFprize() {
		return fprize;
	}

	public void setFprize(Double fprize) {
		this.fprize = fprize;
	}

	public Integer getFviFid() {
		return fviFid;
	}

	public void setFviFid(Integer fviFid) {
		this.fviFid = fviFid;
	}

	public Integer getFentrustType() {
		return fentrustType;
	}

	public void setFentrustType(Integer fentrustType) {
		this.fentrustType = fentrustType;
	}

	public Integer getDeep() {
		return deep;
	}

	public void setDeep(Integer deep) {
		this.deep = deep;
	}

	@Override
	public FentrustData clone() {
		try {
			return (FentrustData) super.clone();
		} catch (Exception e) {
			return new FentrustData();
		}
	}

	@Override
    public int compareTo(Object o) {
        FentrustData o1 = this;
        FentrustData o2 = (FentrustData) o;
        boolean flag = o1.fid == o2.fid && o1.fid != 0 ;
        if(flag){
            return 0 ;
        }
        int ret = (int) (o1.fprize - o2.fprize);
        if(ret == 0){
            ret = o1.fid - o2.fid;
        }
        if (ret > 0) {
            ret = 1;
        } else if (ret < 0) {
            ret = -1;
        }
        return ret;
    }

	@Override
	public String toString() {
		return "FentrustData{" +
				"fid=" + fid +
				", fleftCount=" + fleftCount +
				", fprize=" + fprize +
				", fviFid=" + fviFid +
				", fentrustType=" + fentrustType +
				", deep=" + deep +
				", fstatus=" + fstatus +
				", robotStatus=" + robotStatus +
				'}';
	}
}

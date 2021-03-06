package com.trade.model;

import java.sql.Timestamp;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Version;

import org.hibernate.annotations.GenericGenerator;

/**
 * Fentrustlog entity. @author MyEclipse Persistence Tools
 */
@Entity
@Table(name = "fentrustlog")
// @Cache(usage=CacheConcurrencyStrategy.READ_WRITE)
public class Fentrustlog implements java.io.Serializable {

	// Fields
	private int fid;
	private Fentrust fentrust;
	private double famount;
	private Timestamp fcreateTime;
	private double fprize;
	private double fcount;
	private int version;
	private boolean isactive;
	private int fvirtualcointype;
	private int fEntrustType;// EntrustTypeEnum
	private String fischarge;
	private int isShared;  // 0 未分润  1 已分润  2 过期   3 没推广人

	private double ffees;

	// Constructors

	/** default constructor */
	public Fentrustlog() {
	}

	/** full constructor */
	public Fentrustlog(Fentrust fentrust, double famount,
			Timestamp fcreateTime, double fprize, double fcount) {
		this.fentrust = fentrust;
		this.famount = famount;
		this.fcreateTime = fcreateTime;
		this.fprize = fprize;
		this.fcount = fcount;
	}

	// Property accessors
	@GenericGenerator(name = "generator", strategy = "native")
	@Id
	@GeneratedValue(generator = "generator")
	@Column(name = "fid", unique = true, nullable = false)
	public Integer getFid() {
		return this.fid;
	}

	public void setFid(int fid) {
		this.fid = fid;
	}

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "FEn_fId")
	public Fentrust getFentrust() {
		return this.fentrust;
	}

	public void setFentrust(Fentrust fentrust) {
		this.fentrust = fentrust;
	}

	@Column(name = "fAmount", precision = 12, scale = 0)
	public double getFamount() {
		return this.famount;
	}

	public void setFamount(double famount) {
		this.famount = famount;
	}

	@Column(name = "fCreateTime", length = 0)
	public Timestamp getFcreateTime() {
		return this.fcreateTime;
	}

	public void setFcreateTime(Timestamp fcreateTime) {
		this.fcreateTime = fcreateTime;
	}

	@Column(name = "fPrize", precision = 12, scale = 0)
	public double getFprize() {
		return this.fprize;
	}

	public void setFprize(double fprize) {
		this.fprize = fprize;
	}

	@Column(name = "fCount", precision = 12, scale = 0)
	public double getFcount() {
		return this.fcount;
	}

	public void setFcount(double fcount) {
		this.fcount = fcount;
	}

	@Version
	@Column(name = "version")
	public int getVersion() {
		return version;
	}

	public void setVersion(int version) {
		this.version = version;
	}

	@Column(name = "isactive")
	public boolean isIsactive() {
		return isactive;
	}

	public void setIsactive(boolean isactive) {
		this.isactive = isactive;
	}

	@Column(name = "FVI_type")
	public int getFvirtualcointype() {
		return fvirtualcointype;
	}

	public void setFvirtualcointype(int fvirtualcointype) {
		this.fvirtualcointype = fvirtualcointype;
	}

	@Column(name = "fEntrustType")
	public int getfEntrustType() {
		return fEntrustType;
	}

	public void setfEntrustType(int fEntrustType) {
		this.fEntrustType = fEntrustType;
	}

	@Column(name = "fischarge")
	public String getFischarge() {
		return fischarge;
	}

	public void setFischarge(String fischarge) {
		this.fischarge = fischarge;
	}

	@Column(name = "ffees")
	public double getFfees() {
		return ffees;
	}

	public void setFfees(double ffees) {
		this.ffees = ffees;
	}

	@Column(name = "isShared")
	public int getIsShared() {
		return isShared;
	}

	public void setIsShared(int isShared) {
		this.isShared = isShared;
	}
}
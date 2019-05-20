package com.trade.model;

import java.sql.Timestamp;
import java.util.Date;
import java.util.Set;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.persistence.Version;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.trade.Enum.EntrustStatusEnum;
import com.trade.Enum.EntrustTypeEnum;
import org.hibernate.annotations.GenericGenerator;


/**
 * Fentrust entity. @author MyEclipse Persistence Tools
 */
@Entity
@Table(name = "fentrust")
// @Cache(usage=CacheConcurrencyStrategy.READ_WRITE)
public class Fentrust implements java.io.Serializable {

	// Fields

	private int fid;
	private Fvirtualcointype fvirtualcointype;
	private Market market;
	private Fuser fuser;
	private Date fcreateTime;
	private Date flastUpdatTime;
	private int fentrustType;// EntrustTypeEnum
	private String fentrustType_s;
	private double fprize;
	private double famount;
	private double ffees ;
	private double fleftfees ;
	private double fsuccessAmount;
	private double fcount;
	private double fleftCount;// 未成交数量
	private int fstatus;// EntrustStatusEnum
	private String fstatus_s;
	private boolean fisLimit;// 按照市价完全成交的订单
	private int version;
	private boolean fhasSubscription;
	private int robotStatus;

	// Constructors

	/** default constructor */
	public Fentrust() {
	}

	/** full constructor */
	public Fentrust(Fvirtualcointype fvirtualcointype,
					Fuser fuser, Timestamp fcreateTime,
					int fentrustType, double fprize, double famount, double fcount,
					int fstatus, Set<Fentrustlog> fentrustlogs) {
		this.fvirtualcointype = fvirtualcointype;
		this.fuser = fuser;
		this.fcreateTime = fcreateTime;
		this.fentrustType = fentrustType;
		this.fprize = fprize;
		this.famount = famount;
		this.fcount = fcount;
		this.fstatus = fstatus;
	}

	// Property accessors
	@GenericGenerator(name = "generator", strategy = "native")
	@Id
	@GeneratedValue(generator = "generator")
	@Column(name = "fId", unique = true, nullable = false)
	public Integer getFid() {
		return this.fid;
	}

	public void setFid(int fid) {
		this.fid = fid;
	}

	//	@ManyToOne(fetch = FetchType.LAZY)
//	@JoinColumn(name = "fVi_fId")
	@Transient
	public Fvirtualcointype getFvirtualcointype() {
		return this.fvirtualcointype;
	}

	public void setFvirtualcointype(Fvirtualcointype fvirtualcointype) {
		this.fvirtualcointype = fvirtualcointype;
	}

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "fVi_fId")
	public Market getMarket() {
		return market;
	}

	public void setMarket(Market market) {
		this.market = market;
	}

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "FUs_fId")
	public Fuser getFuser() {
		return this.fuser;
	}

	public void setFuser(Fuser fuser) {
		this.fuser = fuser;
	}

	@Column(name = "fCreateTime", length = 0)
	public Date getFcreateTime() {
		return this.fcreateTime;
	}

	public void setFcreateTime(Date fcreateTime) {
		this.fcreateTime = fcreateTime;
	}

	@Column(name = "fEntrustType")
	public int getFentrustType() {
		return this.fentrustType;
	}

	public void setFentrustType(int fentrustType) {
		this.fentrustType = fentrustType;
	}

	@Column(name = "fPrize", precision = 12, scale = 0)
	public Double getFprize() {
		return this.fprize;
	}

	public void setFprize(double fprize) {
		this.fprize = fprize;
	}

	@Column(name = "fAmount", precision = 12, scale = 0)
	public double getFamount() {
		return this.famount;
	}

	public void setFamount(double famount) {
		this.famount = famount;
	}

	@Column(name = "fCount", precision = 12, scale = 0)
	public double getFcount() {
		return this.fcount;
	}

	public void setFcount(double fcount) {
		this.fcount = fcount;
	}

	@Column(name = "fStatus")
	public int getFstatus() {
		return this.fstatus;
	}

	public void setFstatus(int fstatus) {
		this.fstatus = fstatus;
	}

	@Column(name = "flastUpdatTime")
	public Date getFlastUpdatTime() {
		return flastUpdatTime;
	}

	public void setFlastUpdatTime(Date flastUpdatTime) {
		this.flastUpdatTime = flastUpdatTime;
	}

	@Column(name = "fleftCount")
	public double getFleftCount() {
		return fleftCount;
	}

	public void setFleftCount(double fleftCount) {
		this.fleftCount = fleftCount;
	}

	@Column(name = "fsuccessAmount")
	public double getFsuccessAmount() {
		return fsuccessAmount;
	}

	public void setFsuccessAmount(double fsuccessAmount) {
		this.fsuccessAmount = fsuccessAmount;
	}

	@Column(name = "fisLimit")
	public boolean isFisLimit() {
		return fisLimit;
	}

	public void setFisLimit(boolean fisLimit) {
		this.fisLimit = fisLimit;
	}

	@Version
	@Column(name = "version")
	public int getVersion() {
		return version;
	}

	public void setVersion(int version) {
		this.version = version;
	}

	@Transient
	public String getFentrustType_s() {
		int type = this.getFentrustType();
		return EntrustTypeEnum.getEnumString(type);
	}

	public void setFentrustType_s(String fentrustType_s) {
		this.fentrustType_s = fentrustType_s;
	}

	@Transient
	public String getFstatus_s() {
		int status = this.getFstatus();
		return EntrustStatusEnum.getEnumString(status);
	}

	public void setFstatus_s(String fstatus_s) {
		this.fstatus_s = fstatus_s;
	}

	@Column(name="ffees")
	public double getFfees() {
		return ffees;
	}

	public void setFfees(double ffees) {
		this.ffees = ffees;
	}

	@Column(name="fleftfees")
	public double getFleftfees() {
		return fleftfees;
	}

	public void setFleftfees(double fleftfees) {
		this.fleftfees = fleftfees;
	}

	@Column(name="fhasSubscription")
	public boolean isFhasSubscription() {
		return fhasSubscription;
	}

	public void setFhasSubscription(boolean fhasSubscription) {
		this.fhasSubscription = fhasSubscription;
	}

	@Column(name="robotStatus")
	public int getRobotStatus() {
		return this.robotStatus;
	}

	public void setRobotStatus(int robotStatus) {
		this.robotStatus = robotStatus;
	}

}
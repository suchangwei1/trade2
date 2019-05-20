package com.trade.model;

import java.sql.Timestamp;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.persistence.Version;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.GenericGenerator;

import com.trade.Enum.VirtualCoinTypeStatusEnum;

/**
 * Fvirtualcointype entity. @author MyEclipse Persistence Tools
 */
@Entity
@Table(name = "fvirtualcointype")
//@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@JsonIgnoreProperties({
		"otcOrders"
})
public class Fvirtualcointype implements java.io.Serializable {

	// Fields

	private int fid;
	private boolean fisShare;// 是否可以交易
	private boolean FIsWithDraw;// 是否可以提现
	private boolean FIsRecharge;// 是否可以充值
	private String fname;
	private String fShortName;
	private Timestamp faddTime;
	private int fstatus;// VirtualCoinTypeStatusEnum
	private String fstatus_s;
	private String faccess_key;
	private String fsecrt_key;
	private String fip;
	private String fport;
	private double lastDealPrize;// fake,最新成交价格
	private double higestBuyPrize;
	private double lowestSellPrize;
	private double volumn;
	private Set<Ffees> ffees = new HashSet<Ffees>(0);

	private Set<Fentrust> fentrusts = new HashSet<Fentrust>(0);
	private Set<Fvirtualcaptualoperation> fvirtualcaptualoperations = new HashSet<Fvirtualcaptualoperation>(
			0);
	private Set<Fvirtualwallet> fvirtualwallets = new HashSet<Fvirtualwallet>(0);
	private int version;
	private String furl;
	private double fupanddown;//日涨跌
	private double fupanddownweek;//周涨跌 
	private double fmarketValue;//总市值
	private double fentrustValue;//日成交额

	private int confirmTimes;	// 充值确认次数
	private boolean isOtcActive;
	private double otcSellPrice = 0; // otc固定的卖价
	private double otcBuyPrice = 0; // otc固定的买价

	private Set<CcLog> ccLogs = new HashSet<>(0);
	private Set<OtcOrder> otcOrders = new HashSet<>(0);

	private double otcRate = 0;
	/** default constructor */
	public Fvirtualcointype() {
	}

	/** full constructor */
	public Fvirtualcointype(String fname, String fdescription,
			Timestamp faddTime,
			Set<Fentrust> fentrusts,
			Set<Fvirtualcaptualoperation> fvirtualcaptualoperations,
			Set<Fvirtualwallet> fvirtualwallets) {
		this.fname = fname;
		this.faddTime = faddTime;
		this.fentrusts = fentrusts;
		this.fvirtualcaptualoperations = fvirtualcaptualoperations;
		this.fvirtualwallets = fvirtualwallets;
	}

	// Property accessors
	@GenericGenerator(name = "generator", strategy = "identity")
	@Id
	@GeneratedValue(generator = "generator")
	@Column(name = "fId", unique = true, nullable = false)
	public Integer getFid() {
		return this.fid;
	}

	public void setFid(int fid) {
		this.fid = fid;
	}

	@Column(name = "fName", length = 32)
	public String getFname() {
		return this.fname;
	}

	public void setFname(String fname) {
		this.fname = fname;
	}

	@Column(name = "fAddTime", length = 0)
	public Timestamp getFaddTime() {
		return this.faddTime;
	}

	public void setFaddTime(Timestamp faddTime) {
		this.faddTime = faddTime;
	}

	@Transient
	public Set<Fentrust> getFentrusts() {
		return this.fentrusts;
	}

	public void setFentrusts(Set<Fentrust> fentrusts) {
		this.fentrusts = fentrusts;
	}

	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "fvirtualcointype")
	public Set<Fvirtualcaptualoperation> getFvirtualcaptualoperations() {
		return this.fvirtualcaptualoperations;
	}

	public void setFvirtualcaptualoperations(
			Set<Fvirtualcaptualoperation> fvirtualcaptualoperations) {
		this.fvirtualcaptualoperations = fvirtualcaptualoperations;
	}

	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "fvirtualcointype")
	public Set<Ffees> getFfees() {
		return ffees;
	}

	public void setFfees(Set<Ffees> ffees) {
		this.ffees = ffees;
	}

	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "fvirtualcointype")
	public Set<Fvirtualwallet> getFvirtualwallets() {
		return this.fvirtualwallets;
	}

	public void setFvirtualwallets(Set<Fvirtualwallet> fvirtualwallets) {
		this.fvirtualwallets = fvirtualwallets;
	}

	@Column(name = "fstatus")
	public int getFstatus() {
		return fstatus;
	}

	public void setFstatus(int fstatus) {
		this.fstatus = fstatus;
	}

	@Column(name = "fShortName")
	public String getfShortName() {
		return fShortName;
	}

	public void setfShortName(String fShortName) {
		this.fShortName = fShortName;
	}

	@Transient
	public String getFstatus_s() {
		return VirtualCoinTypeStatusEnum.getEnumString(this.getFstatus());
	}

	public void setFstatus_s(String fstatus_s) {
		this.fstatus_s = fstatus_s;
	}

	@Version
	@Column(name = "version")
	public int getVersion() {
		return version;
	}

	public void setVersion(int version) {
		this.version = version;
	}

	@Column(name = "faccess_key")
	public String getFaccess_key() {
		return faccess_key;
	}

	public void setFaccess_key(String faccess_key) {
		this.faccess_key = faccess_key;
	}

	@Column(name = "fsecrt_key")
	public String getFsecrt_key() {
		return fsecrt_key;
	}

	public void setFsecrt_key(String fsecrt_key) {
		this.fsecrt_key = fsecrt_key;
	}

	@Column(name = "fip")
	public String getFip() {
		return fip;
	}

	public void setFip(String fip) {
		this.fip = fip;
	}

	@Column(name = "fport")
	public String getFport() {
		return fport;
	}

	public void setFport(String fport) {
		this.fport = fport;
	}

	@Transient
	public double getLastDealPrize() {
		return lastDealPrize;
	}

	public void setLastDealPrize(double lastDealPrize) {
		this.lastDealPrize = lastDealPrize;
	}

	@Transient
	public double getHigestBuyPrize() {
		return higestBuyPrize;
	}

	public void setHigestBuyPrize(double higestBuyPrize) {
		this.higestBuyPrize = higestBuyPrize;
	}

	@Transient
	public double getLowestSellPrize() {
		return lowestSellPrize;
	}

	public void setLowestSellPrize(double lowestSellPrize) {
		this.lowestSellPrize = lowestSellPrize;
	}

	@Transient
	public String getFid_s() {
		Integer id = this.getFid();
		if (id != null) {
			return String.valueOf(id);
		}
		return String.valueOf(0);
	}

	@Column(name = "fisShare")
	public boolean isFisShare() {
		return fisShare;
	}

	public void setFisShare(boolean fisShare) {
		this.fisShare = fisShare;
	}

	@Column(name = "FIsWithDraw")
	public boolean isFIsWithDraw() {
		return FIsWithDraw;
	}

	public void setFIsWithDraw(boolean fIsWithDraw) {
		FIsWithDraw = fIsWithDraw;
	}

	@Column(name = "furl")
	public String getFurl() {
		return furl;
	}

	public void setFurl(String furl) {
		this.furl = furl;
	}
	
	@Transient
	public double getVolumn() {
		return volumn;
	}

	public void setVolumn(double volumn) {
		this.volumn = volumn;
	}

	
	@Transient
	public double getFupanddown(){
		return this.fupanddown;
	}
	
	public void setFupanddown(double fupanddown){
		this.fupanddown = fupanddown;
	}
	
	@Transient
	public double getFupanddownweek(){
		return this.fupanddownweek;
	}
	
	public void setFupanddownweek(double fupanddownweek){
		this.fupanddownweek = fupanddownweek;
	}
	
	@Transient
	public double getFmarketValue(){
		return this.fmarketValue;
	}
	
	public void setFmarketValue(double fmarketValue){
		this.fmarketValue = fmarketValue;
	}

	@Transient
	public double getFentrustValue(){
		return this.fentrustValue;
	}
	
	public void setFentrustValue(double fentrustValue){
		this.fentrustValue = fentrustValue;
	}

	@Column(name = "confirm_times")
	public int getConfirmTimes() {
		return confirmTimes;
	}

	public void setConfirmTimes(int confirmTimes) {
		this.confirmTimes = confirmTimes;
	}

	@Column(name = "FIsRecharge")
	public boolean isFIsRecharge() {
		return FIsRecharge;
	}

	public void setFIsRecharge(boolean FIsRecharge) {
		this.FIsRecharge = FIsRecharge;
	}

	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "coin")
	public Set<CcLog> getCcLogs() {
		return ccLogs;
	}

	public void setCcLogs(Set<CcLog> ccLogs) {
		this.ccLogs = ccLogs;
	}

	@OneToMany(mappedBy = "fvirtualcointype")
	public Set<OtcOrder> getOtcOrders() {
		return otcOrders;
	}

	public void setOtcOrders(Set<OtcOrder> otcOrders) {
		this.otcOrders = otcOrders;
	}

	@Column(name = "isOtcActive")
	public boolean isOtcActive() {
		return isOtcActive;
	}

	public void setOtcActive(boolean otcActive) {
		isOtcActive = otcActive;
	}

	@Column(name = "otcRate")
	public double getOtcRate() {
		return otcRate;
	}

	public void setOtcRate(double otcRate) {
		this.otcRate = otcRate;
	}

	@Column(name = "otc_sell_price")
	public double getOtcSellPrice() {
		return otcSellPrice;
	}

	public void setOtcSellPrice(double otcSellPrice) {
		this.otcSellPrice = otcSellPrice;
	}

	@Column(name = "otc_buy_price")
	public double getOtcBuyPrice() {
		return otcBuyPrice;
	}

	public void setOtcBuyPrice(double otcBuyPrice) {
		this.otcBuyPrice = otcBuyPrice;
	}


	//通过ID判断两个币种是否为同一个币种
	@Override
	public boolean equals(Object obj) {
		
		if(obj == null){
			return false;
		}
		if(obj instanceof Fvirtualcointype){
			Fvirtualcointype fvirtualcointype = (Fvirtualcointype) obj;
			if(fid == fvirtualcointype.getFid()){
				return true;
			}
		}
		return false;
	}
}
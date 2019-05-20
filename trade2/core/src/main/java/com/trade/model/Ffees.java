package com.trade.model;

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

import com.trade.util.Utils;
import org.hibernate.annotations.GenericGenerator;

/**
 * Ffees entity. @author MyEclipse Persistence Tools
 */
@Entity
@Table(name = "ffees")
public class Ffees implements java.io.Serializable {

	// Fields

	private int fid;
	private double buyFee;// 买入手续费
	private double ffee;// 交易手续费
	private double withdraw;// 提现手续费固定值
	private double minWithdraw;		// 最小提现额度
	private double withdrawRatio;	// 提现手续费比例
	private int withdrawFeeType;	// 手续费类型  币类型
	private int flevel ;
	private Fvirtualcointype fvirtualcointype;
	private int version;

	// Constructors

	/** default constructor */
	public Ffees() {
	}

	// Property accessors
	@GenericGenerator(name = "generator", strategy = "native")
	@Id
	@GeneratedValue(generator = "generator")
	@Column(name = "fid", unique = true, nullable = false)
	public int getFid() {
		return this.fid;
	}

	public void setFid(int fid) {
		this.fid = fid;
	}

	@Column(name = "ffee", precision = 15, scale = 0)
	public double getFfee() {
		return this.ffee;
	}

	public void setFfee(double ffee) {
		this.ffee = ffee;
	}

	public double getWithdraw() {
		return withdraw;
	}

	public void setWithdraw(double withdraw) {
		this.withdraw = withdraw;
	}

	@Version
	@Column(name = "version")
	public int getVersion() {
		return version;
	}

	public void setVersion(int version) {
		this.version = version;
	}

	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="fvir_id")
	public Fvirtualcointype getFvirtualcointype() {
		return fvirtualcointype;
	}

	public void setFvirtualcointype(Fvirtualcointype fvirtualcointype) {
		this.fvirtualcointype = fvirtualcointype;
	}

	@Column(name="flevel")
	public int getFlevel() {
		return flevel;
	}

	public void setFlevel(int flevel) {
		this.flevel = flevel;
	}

	@Column(name = "buy_fee", precision = 15, scale = 0)
	public double getBuyFee() {
		return buyFee;
	}

	public void setBuyFee(double buyFee) {
		this.buyFee = buyFee;
	}

	@Column(name = "min_withdraw")
	public double getMinWithdraw() {
		return minWithdraw;
	}

	public void setMinWithdraw(double minWithdraw) {
		this.minWithdraw = minWithdraw;
	}

	@Column(name = "withdraw_ratio")
	public double getWithdrawRatio() {
		return withdrawRatio;
	}

	public void setWithdrawRatio(double withdrawRatio) {
		this.withdrawRatio = withdrawRatio;
	}

	@Column(name = "withdraw_fee_type")
	public int getWithdrawFeeType() {
		return withdrawFeeType;
	}

	public void setWithdrawFeeType(int withdrawFeeType) {
		this.withdrawFeeType = withdrawFeeType;
	}

	@Transient
	public double getWithdrawFee(double amount) {
		return Utils.getDouble(getWithdraw() + amount * getWithdrawRatio(), 8);
	}
}
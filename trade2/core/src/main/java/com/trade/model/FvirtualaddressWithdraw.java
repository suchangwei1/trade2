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
 * FvirtualaddressWithdraw entity. @author MyEclipse Persistence Tools
 */
@Entity
@Table(name = "fvirtualaddress_withdraw")
public class FvirtualaddressWithdraw implements java.io.Serializable {

	// Fields

	private int fid;
	private int version;
	private Fvirtualcointype fvirtualcointype;
	private String fadderess;
	private String flabel;
	private Fuser fuser ;
	private Timestamp fcreateTime;
	private boolean init ;

	// Constructors

	/** default constructor */
	public FvirtualaddressWithdraw() {
	}

	/** full constructor */
	public FvirtualaddressWithdraw(Fvirtualcointype fvirtualcointype,
								   String fadderess, Fuser fuser, Timestamp fcreateTime,String flabel) {
		this.fvirtualcointype = fvirtualcointype;
		this.fadderess = fadderess;
		this.fcreateTime = fcreateTime;
		this.fuser = fuser;
		this.flabel = flabel;
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

	@Version
	@Column(name = "version")
	public int getVersion() {
		return this.version;
	}

	public void setVersion(int version) {
		this.version = version;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "fVi_fId")
	public Fvirtualcointype getFvirtualcointype() {
		return this.fvirtualcointype;
	}

	public void setFvirtualcointype(Fvirtualcointype fvirtualcointype) {
		this.fvirtualcointype = fvirtualcointype;
	}

	@Column(name = "fAdderess", length = 128)
	public String getFadderess() {
		return this.fadderess;
	}

	public void setFadderess(String fadderess) {
		this.fadderess = fadderess;
	}

	@Column(name = "fCreateTime", length = 0)
	public Timestamp getFcreateTime() {
		return this.fcreateTime;
	}

	public void setFcreateTime(Timestamp fcreateTime) {
		this.fcreateTime = fcreateTime;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "fuid")
	public Fuser getFuser() {
		return fuser;
	}

	public void setFuser(Fuser fuser) {
		this.fuser = fuser;
	}

	@Column(name="init")
	public boolean isInit() {
		return init;
	}

	public void setInit(boolean init) {
		this.init = init;
	}

	@Column(name = "flabel", length = 50)
	public String getFlabel() {
		return flabel;
	}

	public void setFlabel(String flabel) {
		this.flabel = flabel;
	}

}
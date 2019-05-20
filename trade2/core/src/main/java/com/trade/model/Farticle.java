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
import javax.persistence.Transient;
import javax.persistence.Version;

import com.trade.util.HTMLSpirit;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.GenericGenerator;

@Entity
@Table(name = "farticle")
@Cache(usage=CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class Farticle implements java.io.Serializable {

	private int fid;
	private Farticletype farticletype;
	private Fadmin fadminByFcreateAdmin;
	private Fadmin fadminByFmodifyAdmin;
	private String ftitle;
	private String enTitle;
	private String enContent;
	private boolean isTop;
	private String fcontent;
	private Timestamp fcreateDate;
	private Timestamp flastModifyDate;
	private int version ;

	private Fvirtualcointype fvirtualcointype;

	public Farticle() {
	}

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

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "fArticleType")
	public Farticletype getFarticletype() {
		return this.farticletype;
	}

	public void setFarticletype(Farticletype farticletype) {
		this.farticletype = farticletype;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "fCreateAdmin")
	public Fadmin getFadminByFcreateAdmin() {
		return this.fadminByFcreateAdmin;
	}

	public void setFadminByFcreateAdmin(Fadmin fadminByFcreateAdmin) {
		this.fadminByFcreateAdmin = fadminByFcreateAdmin;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "fModifyAdmin")
	public Fadmin getFadminByFmodifyAdmin() {
		return this.fadminByFmodifyAdmin;
	}

	public void setFadminByFmodifyAdmin(Fadmin fadminByFmodifyAdmin) {
		this.fadminByFmodifyAdmin = fadminByFmodifyAdmin;
	}

	@Column(name = "fTitle", length = 1024)
	public String getFtitle() {
		return this.ftitle;
	}

	public void setFtitle(String ftitle) {
		this.ftitle = ftitle;
	}

	@Column(name = "fContent", length = 65535)
	public String getFcontent() {
		return this.fcontent;
	}

	public void setFcontent(String fcontent) {
		this.fcontent = fcontent;
	}

	@Column(name = "fCreateDate", length = 0)
	public Timestamp getFcreateDate() {
		return this.fcreateDate;
	}

	public void setFcreateDate(Timestamp fcreateDate) {
		this.fcreateDate = fcreateDate;
	}

	@Column(name = "fLastModifyDate", length = 0)
	public Timestamp getFlastModifyDate() {
		return this.flastModifyDate;
	}

	public void setFlastModifyDate(Timestamp flastModifyDate) {
		this.flastModifyDate = flastModifyDate;
	}
	@Version
    @Column(name="version")
	public int getVersion() {
		return version;
	}

	public void setVersion(int version) {
		this.version = version;
	}

	@Column(name = "isTop")
	public boolean isTop() {
		return isTop;
	}

	public void setTop(boolean top) {
		isTop = top;
	}

	@Column(name = "en_title")
	public String getEnTitle() {
		return enTitle;
	}

	public void setEnTitle(String enTitle) {
		this.enTitle = enTitle;
	}

	@Column(name = "en_content")
	public String getEnContent() {
		return enContent;
	}

	public void setEnContent(String enContent) {
		this.enContent = enContent;
	}
}

package com.trade.model;

import java.sql.Timestamp;
import java.util.Date;
import java.util.Set;
import javax.persistence.*;

import com.trade.util.DateUtils;
import com.trade.util.Utils;
import org.hibernate.annotations.GenericGenerator;


/**
 * Fvirtualwallet entity. @author MyEclipse Persistence Tools
 */
@Entity
@Table(name="fvirtualwallet"
)
//@Cache(usage=CacheConcurrencyStrategy.READ_WRITE)
public class Fvirtualwallet  implements java.io.Serializable {


    // Fields    

     private int fid;
     private Fvirtualcointype fvirtualcointype;
     private double ftotal;
     private double ffrozen;
     private Timestamp flastUpdateTime;
     private Fuser fuser ;
     private int version ;

    // Constructors

    /** default constructor */
    public Fvirtualwallet() {
    }

    
    /** full constructor */
    public Fvirtualwallet(Fvirtualcointype fvirtualcointype, double ftotal, double ffrozen, Timestamp flastUpdateTime, Set<Fuser> fusers) {
        this.fvirtualcointype = fvirtualcointype;
        this.ftotal = ftotal;
        this.ffrozen = ffrozen;
        this.flastUpdateTime = flastUpdateTime;
    }

   
    // Property accessors
    @GenericGenerator(name="generator", strategy="native")
    @Id @GeneratedValue(generator="generator") 
    @Column(name="fId", unique=true, nullable=false) 
    public Integer getFid() {
        return this.fid;
    }
    
    public void setFid(int fid) {
        this.fid = fid;
    }
	@ManyToOne(fetch=FetchType.EAGER)
	@JoinColumn(name = "fVi_fId")

    public Fvirtualcointype getFvirtualcointype() {
        return this.fvirtualcointype;
    }
    
    public void setFvirtualcointype(Fvirtualcointype fvirtualcointype) {
        this.fvirtualcointype = fvirtualcointype;
    }
    
    @Column(name="fTotal", precision=12, scale=0)

    public double getFtotal() {
        return this.ftotal;
    }
    
    public void setFtotal(double ftotal) {
        this.ftotal = ftotal;
    }
    
    @Column(name="fFrozen", precision=12, scale=0)

    public double getFfrozen() {
        return this.ffrozen;
    }
    
    public void setFfrozen(double ffrozen) {
        this.ffrozen = ffrozen;
    }
    
    @Column(name="fLastUpdateTime", length=0)

    public Timestamp getFlastUpdateTime() {
        return this.flastUpdateTime;
    }
    
    public void setFlastUpdateTime(Timestamp flastUpdateTime) {
        this.flastUpdateTime = flastUpdateTime;
    }

      @ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "fuid")
	public Fuser getFuser() {
		return fuser;
	}


	public void setFuser(Fuser fuser) {
		this.fuser = fuser;
	}

	@Version
      @Column(name="version")
	public int getVersion() {
		return version;
	}

	public void setVersion(int version) {
		this.version = version;
	}

}
package com.trade.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.trade.Enum.IdentityTypeEnum;
import com.trade.Enum.UserStatusEnum;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.sql.Timestamp;
import java.util.HashSet;
import java.util.Set;

/**
 * Fuser entity. @author MyEclipse Persistence Tools
 */
@Entity
@Table(name = "fuser")
@JsonIgnoreProperties({
        "Fuser",
        "fidentityType",
        "fidentityType_s",
        "fregisterTime",
        "flastLoginTime",
        "flastUpdateTime",
        "fstatus",
        "fstatus_s",
        "fIdentityPath",
        "fIdentityPath2",
        "fpostRealValidateTime",
        "fIntroUser_id",
        "fvirtualaddresses",
        "fvirtualaddressWithdraws",
        "emailvalidates",
        "validateemailses",
        "fentrustplans",
        "fentrusts",
        "fcapitaloperations",
        "fvirtualcaptualoperations",
        "fvirtualwallets",
        "version",
        "fsourceUrl",
        "fregisterIp",
        "ccLogs",
        "otcAccount",
        "otcOrders",
        "otcOrderLog",
        "spreadLogs",
        "childLogs",
        "tradeFeesShareParent",
        "tradeFeesShareChild",

})
public class Fuser implements java.io.Serializable {

    // Fields
    private int fid;
    private String floginName;
    private String floginPassword;
    private String ftradePassword;
    private String fnickName;
    private String frealName;
    private String fareaCode;
    private String ftelephone;
    private String femail;
    private int fidentityType;//IdentityTypeEnum
    private String fidentityType_s;
    private String fidentityNo;
    private Timestamp fregisterTime;
    private Timestamp flastLoginTime;
    private Timestamp flastUpdateTime;

    private int fstatus;
    private String fstatus_s;

    private boolean fpostRealValidate;//已经提交身份认证
    private boolean fhasRealValidate;//通过身份认证
    private String fIdentityPath;//身份证。
    private String fIdentityPath2;//身份证。反面
    private String fIdentityPath3;//手持身份证
    private int fIdentityStatus;// 身份证审核状态 0未提交|1已提交|2审核通过|3审核不通过
    private Timestamp fpostRealValidateTime;
    private Timestamp fhasRealValidateTime;

    private Fuser fIntroUser_id;

    private Set<Fvirtualaddress> fvirtualaddresses = new HashSet<Fvirtualaddress>(0);
    private Set<FvirtualaddressWithdraw> fvirtualaddressWithdraws = new HashSet<FvirtualaddressWithdraw>(0);
    private Set<Fvalidateemail> validateemailses = new HashSet<Fvalidateemail>(
            0);
    private Set<Fentrust> fentrusts = new HashSet<Fentrust>(0);
    private Set<FuserCointype> fuserCointype=new HashSet<FuserCointype>();
   // private Set<ApplyCoin> applyCoin=new HashSet<ApplyCoin>();

    private Set<Fvirtualcaptualoperation> fvirtualcaptualoperations = new HashSet<Fvirtualcaptualoperation>(
            0);
    private Set<Fvirtualwallet> fvirtualwallets = new HashSet<Fvirtualwallet>(0);
    private int version;
    private String fsourceUrl;
    private String fregisterIp;
    private boolean fneedFee = true;        // 是否需要手续费
    private String headImgUrl;
    private String bankAccount;  //银行卡号
    private boolean hasOtcSet = false;
    private OtcAccount otcAccount;
    private OtcAccount account;
    private boolean canOtc = false;
    private Set<CcLog> ccLogs = new HashSet<>(0);
    private Set<OtcOrder> otcOrders = new HashSet<>(0);
    private Set<OtcOrderLog> otcOrderLog = new HashSet<>(0);
    private Set<SpreadLog> spreadLogs = new HashSet<>(0);
    private Set<SpreadLog> childLogs = new HashSet<>(0);
    private Set<SpreadLog> tradeFeesShareParent = new HashSet<>(0);
    private Set<SpreadLog> tradeFeesShareChild = new HashSet<>(0);

    private int otcTimes;

    /**
     * default constructor
     */
    public Fuser() {
    }

    // Property accessors
    @GenericGenerator(name = "generator", strategy = "identity")
    @Id
    @GeneratedValue(generator = "generator")
    @Column(name = "fId", unique = true, nullable = false)
    public int getFid() {
        return this.fid;
    }

    public void setFid(int fid) {
        this.fid = fid;
    }

    @Column(name = "floginName", length = 255)
    public String getFloginName() {
        return this.floginName;
    }

    public void setFloginName(String floginName) {
        this.floginName = floginName;
    }

    @Column(name = "fLoginPassword", length = 255)
    public String getFloginPassword() {
        return this.floginPassword;
    }

    public void setFloginPassword(String floginPassword) {
        this.floginPassword = floginPassword;
    }

    @Column(name = "fTradePassword", length = 32)
    public String getFtradePassword() {
        return this.ftradePassword;
    }

    public void setFtradePassword(String ftradePassword) {
        this.ftradePassword = ftradePassword;
    }

    @Column(name = "fNickName", length = 32)
    public String getFnickName() {
        return this.fnickName;
    }

    public void setFnickName(String fnickName) {
        this.fnickName = fnickName;
    }

    @Column(name = "fRealName", length = 32)
    public String getFrealName() {
        return this.frealName;
    }

    public void setFrealName(String frealName) {
        this.frealName = frealName;
    }

    @Column(name = "fTelephone", length = 32)
    public String getFtelephone() {
        return this.ftelephone;
    }

    public void setFtelephone(String ftelephone) {
        this.ftelephone = ftelephone;
    }

    @Column(name = "fEmail", length = 255)
    public String getFemail() {
        return this.femail;
    }

    public void setFemail(String femail) {
        this.femail = femail;
    }

    @Column(name = "fIdentityNo", length = 128)
    public String getFidentityNo() {
        return this.fidentityNo;
    }

    public void setFidentityNo(String fidentityNo) {
        this.fidentityNo = fidentityNo;
    }

    @Column(name = "fRegisterTime", length = 0)
    public Timestamp getFregisterTime() {
        return this.fregisterTime;
    }

    public void setFregisterTime(Timestamp fregisterTime) {
        this.fregisterTime = fregisterTime;
    }

    @Column(name = "fLastLoginTime", length = 0)
    public Timestamp getFlastLoginTime() {
        return this.flastLoginTime;
    }

    public void setFlastLoginTime(Timestamp flastLoginTime) {
        this.flastLoginTime = flastLoginTime;
    }

    @Column(name = "fStatus")
    public int getFstatus() {
        return this.fstatus;
    }

    public void setFstatus(int fstatus) {
        this.fstatus = fstatus;
    }

    @Transient
    public String getFstatus_s() {
        int status = getFstatus();
        return UserStatusEnum.getEnumString(status);
    }

    public void setFstatus_s(String fstatus_s) {
        this.fstatus_s = fstatus_s;
    }


    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "fuser")
    public Set<Fvalidateemail> getValidateemailses() {
        return this.validateemailses;
    }

    public void setValidateemailses(Set<Fvalidateemail> validateemailses) {
        this.validateemailses = validateemailses;
    }


    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "fuser")
    public Set<Fvirtualaddress> getFvirtualaddresses() {
        return fvirtualaddresses;
    }

    public void setFvirtualaddresses(Set<Fvirtualaddress> fvirtualaddresses) {
        this.fvirtualaddresses = fvirtualaddresses;
    }

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "fuser")
    public Set<FvirtualaddressWithdraw> getFvirtualaddressWithdraws() {
        return fvirtualaddressWithdraws;
    }

    public void setFvirtualaddressWithdraws(
            Set<FvirtualaddressWithdraw> fvirtualaddressWithdraws) {
        this.fvirtualaddressWithdraws = fvirtualaddressWithdraws;
    }


    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "fuser")
    public Set<Fentrust> getFentrusts() {
        return this.fentrusts;
    }

    public void setFentrusts(Set<Fentrust> fentrusts) {
        this.fentrusts = fentrusts;
    }

    @JsonIgnore
    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER, mappedBy = "fuser")
    public Set<FuserCointype> getFuserCointype() {

        return fuserCointype;
    }

    public void setFuserCointype(Set<FuserCointype> fuserCointype) {
        this.fuserCointype = fuserCointype;
    }

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "fuser")
    public Set<Fvirtualcaptualoperation> getFvirtualcaptualoperations() {
        return this.fvirtualcaptualoperations;
    }

    public void setFvirtualcaptualoperations(
            Set<Fvirtualcaptualoperation> fvirtualcaptualoperations) {
        this.fvirtualcaptualoperations = fvirtualcaptualoperations;
    }

    @Column(name = "fpostRealValidate")
    public boolean getFpostRealValidate() {
        return fpostRealValidate;
    }

    public void setFpostRealValidate(boolean fpostRealValidate) {
        this.fpostRealValidate = fpostRealValidate;
    }

    @Column(name = "fhasRealValidate")
    public boolean getFhasRealValidate() {
        return fhasRealValidate;
    }

    public void setFhasRealValidate(boolean fhasRealValidate) {
        this.fhasRealValidate = fhasRealValidate;
    }

    @Column(name = "fpostRealValidateTime")
    public Timestamp getFpostRealValidateTime() {
        return fpostRealValidateTime;
    }

    public void setFpostRealValidateTime(Timestamp fpostRealValidateTime) {
        this.fpostRealValidateTime = fpostRealValidateTime;
    }

    @Column(name = "fhasRealValidateTime")
    public Timestamp getFhasRealValidateTime() {
        return fhasRealValidateTime;
    }

    public void setFhasRealValidateTime(Timestamp fhasRealValidateTime) {
        this.fhasRealValidateTime = fhasRealValidateTime;
    }

    @Column(name = "fidentityType")
    public int getFidentityType() {
        return fidentityType;
    }

    public void setFidentityType(int fidentityType) {
        this.fidentityType = fidentityType;
    }

    @Transient
    public String getFidentityType_s() {
        int type = getFidentityType();
        return IdentityTypeEnum.getEnumString(type);
    }

    public void setFidentityType_s(String fidentityType_s) {
        this.fidentityType_s = fidentityType_s;
    }


    @Column(name = "flastUpdateTime")
    public Timestamp getFlastUpdateTime() {
        return flastUpdateTime;
    }

    public void setFlastUpdateTime(Timestamp flastUpdateTime) {
        this.flastUpdateTime = flastUpdateTime;
    }

    @Column(name = "fareaCode")
    public String getFareaCode() {
        return fareaCode;
    }

    public void setFareaCode(String fareaCode) {
        this.fareaCode = fareaCode;
    }

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "fuser")
    public Set<Fvirtualwallet> getFvirtualwallets() {
        return fvirtualwallets;
    }

    public void setFvirtualwallets(Set<Fvirtualwallet> fvirtualwallets) {
        this.fvirtualwallets = fvirtualwallets;
    }

    @OneToMany(mappedBy = "buyyer")
    public Set<CcLog> getCcLogs() {
        return ccLogs;
    }

    public void setCcLogs(Set<CcLog> ccLogs) {
        this.ccLogs = ccLogs;
    }

    /*@JsonIgnore
    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER, mappedBy = "fuser")
    public Set<ApplyCoin> getApplyCoin() {
        return applyCoin;
    }*/

    /*public void setApplyCoin(Set<ApplyCoin> applyCoin) {
        this.applyCoin = applyCoin;
    }*/

    @OneToMany(mappedBy = "fuser")
    public Set<OtcOrder> getOtcOrders() {
        return otcOrders;
    }

    public void setOtcOrders(Set<OtcOrder> otcOrders) {
        this.otcOrders = otcOrders;
    }

    @OneToMany(mappedBy = "fuser")
    public Set<OtcOrderLog> getOtcOrderLog() {
        return otcOrderLog;
    }

    public void setOtcOrderLog(Set<OtcOrderLog> otcOrderLog) {
        this.otcOrderLog = otcOrderLog;
    }

    @Version
    @Column(name = "version")
    public int getVersion() {
        return version;
    }

    public void setVersion(int version) {
        this.version = version;
    }

    @Column(name = "fIdentityPath")
    public String getfIdentityPath() {
        return fIdentityPath;
    }

    public void setfIdentityPath(String fIdentityPath) {
        this.fIdentityPath = fIdentityPath;
    }


    @Column(name = "fIdentityPath2")
    public String getfIdentityPath2() {
        return fIdentityPath2;
    }

    public void setfIdentityPath2(String fIdentityPath2) {
        this.fIdentityPath2 = fIdentityPath2;
    }
    @Column(name = "fIdentityPath3")
    public String getfIdentityPath3() {
        return fIdentityPath3;
    }

    public void setfIdentityPath3(String fIdentityPath3) {
        this.fIdentityPath3 = fIdentityPath3;
    }

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "fIntroUser_id")
    public Fuser getfIntroUser_id() {
        return fIntroUser_id;
    }

    public void setfIntroUser_id(Fuser fIntroUser_id) {
        this.fIntroUser_id = fIntroUser_id;
    }


    @Column(name = "fsourceurl", length = 255)
    public String getFsourceUrl() {
        return this.fsourceUrl;
    }

    public void setFsourceUrl(String fsourceUrl) {
        this.fsourceUrl = fsourceUrl;
    }

    @Column(name = "fregisterip", length = 50)
    public String getFregisterIp() {
        return this.fregisterIp;
    }

    public void setFregisterIp(String fregisterIp) {
        this.fregisterIp = fregisterIp;
    }

    @Column(name = "fneedfee")
    public boolean getFneedFee() {
        return fneedFee;
    }

    public void setFneedFee(boolean fneedFee) {
        this.fneedFee = fneedFee;
    }


    @Column(name = "head_img_url", length = 128)
    public String getHeadImgUrl() {
        return headImgUrl;
    }

    public void setHeadImgUrl(String headImgUrl) {
        this.headImgUrl = headImgUrl;
    }

    @Column(name = "fIdentityStatus")
    public int getfIdentityStatus() {
        return fIdentityStatus;
    }

    public void setfIdentityStatus(int fIdentityStatus) {
        this.fIdentityStatus = fIdentityStatus;
    }


    @Column(name = "bank_account")
    public String getBankAccount() {
        return bankAccount;
    }

    public void setBankAccount(String bankAccount) {
        this.bankAccount = bankAccount;
    }

    @Column(name = "has_otc_set")
    public boolean isHasOtcSet() {
        return hasOtcSet;
    }

    public void setHasOtcSet(boolean hasOtcSet) {
        this.hasOtcSet = hasOtcSet;
    }

    @OneToOne(fetch = FetchType.LAZY, mappedBy = "fuser")
    public OtcAccount getOtcAccount() {
        return otcAccount;
    }

    public void setOtcAccount(OtcAccount otcAccount) {
        this.otcAccount = otcAccount;
    }

    @Column(name = "can_otc")
    public boolean isCanOtc() {
        return canOtc;
    }

    public void setCanOtc(boolean canOtc) {
        this.canOtc = canOtc;
    }

    @Column(name = "otc_times")
    public int getOtcTimes() {
        return otcTimes;
    }

    public void setOtcTimes(int otcTimes) {
        this.otcTimes = otcTimes;
    }

    @Transient
    public OtcAccount getAccount() {
        return account;
    }

    public void setAccount(OtcAccount account) {
        this.account = account;
    }

    @OneToMany(mappedBy = "parent")
    public Set<SpreadLog> getSpreadLogs() {
        return spreadLogs;
    }

    public void setSpreadLogs(Set<SpreadLog> spreadLogs) {
        this.spreadLogs = spreadLogs;
    }

    @OneToMany(mappedBy = "child")
    public Set<SpreadLog> getChildLogs() {
        return childLogs;
    }

    public void setChildLogs(Set<SpreadLog> childLogs) {
        this.childLogs = childLogs;
    }

    @OneToMany(mappedBy = "parent")
    public Set<SpreadLog> getTradeFeesShareParent() {
        return tradeFeesShareParent;
    }

    public void setTradeFeesShareParent(Set<SpreadLog> tradeFeesShareParent) {
        this.tradeFeesShareParent = tradeFeesShareParent;
    }

    @OneToMany(mappedBy = "child")
    public Set<SpreadLog> getTradeFeesShareChild() {
        return tradeFeesShareChild;
    }

    public void setTradeFeesShareChild(Set<SpreadLog> tradeFeesShareChild) {
        this.tradeFeesShareChild = tradeFeesShareChild;
    }
}
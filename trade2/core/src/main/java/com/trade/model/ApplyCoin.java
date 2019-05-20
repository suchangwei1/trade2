package com.trade.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.hibernate.annotations.GenericGenerator;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import java.math.BigDecimal;
import java.sql.Date;
import java.sql.Timestamp;

/**
 * Created by echoHU on 2018/6/29 0012.
 */
@Entity
@Table(name = "apply_coin")
public class ApplyCoin {

    private int id;
    private  String email;
    private String phone;
    private String coinTypeEn;//币种英文介绍
    private String coinTypeCn;
    private String coinTypeMark;//币种符号
    private Date icoDate;//'首次发行日期
    private Date circulationDate;//流通日期
    private String InternetType;//网络类型
    private String address;
    private int decimals;//小数点位数

    private String website;//网址
    private String whitWebsite;//白皮书网址
    private String browser;//区块浏览器
    private String logoImg;//
    private String twitterLink;//推特链接
    private String telegramLink;//电报链接
    private String introductionCn;//中文介绍
    private String introductionEn;
    private BigDecimal coinTotal;//币种总量
    private BigDecimal coinTrade;//币种交易量
    private BigDecimal coinScaling;//分配比例
    private BigDecimal price;
    private String tradingLink;//已上线交易平台
    private String codeLink;//代码开源链接
    private String introduce;//应用场景
    @JsonIgnore
    private Fuser fuser;//用户ID
    private Timestamp fcreateTime;


    @GenericGenerator(name = "generator", strategy = "native")
    @Id
    @GeneratedValue(generator = "generator")
    @Column(name = "id", unique = true, nullable = false)
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
    @Column(name = "email")
    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
    @Column(name = "phone")
    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }
    @Column(name = "coin_type_en")
    public String getCoinTypeEn() {
        return coinTypeEn;
    }

    public void setCoinTypeEn(String coinTypeEn) {
        this.coinTypeEn = coinTypeEn;
    }
    @Column(name = "coin_type_cn")
    public String getCoinTypeCn() {
        return coinTypeCn;
    }

    public void setCoinTypeCn(String coinTypeCn) {
        this.coinTypeCn = coinTypeCn;
    }
    @Column(name = "coin_type_mark")
    public String getCoinTypeMark() {
        return coinTypeMark;
    }

    public void setCoinTypeMark(String coinTypeMark) {
        this.coinTypeMark = coinTypeMark;
    }
    @Column(name = "ico_date",length = 0)
    @DateTimeFormat(pattern="yyyy-MM-dd")
    public Date getIcoDate() {
        return icoDate;
    }

    public void setIcoDate(Date icoDate) {
        this.icoDate = icoDate;
    }

    @Column(name = "circulation_date",length = 0)
    @DateTimeFormat(pattern="yyyy-MM-dd")
    public Date getCirculationDate() {
        return circulationDate;
    }

    public void setCirculationDate(Date circulationDate) {
        this.circulationDate = circulationDate;
    }
    @Column(name = "Internet_type")
    public String getInternetType() {
        return InternetType;
    }

    public void setInternetType(String internetType) {
        InternetType = internetType;
    }
    @Column(name = "address")
    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }
    @Column(name = "decimals")
    public int getDecimals() {
        return decimals;
    }

    public void setDecimals(int decimals) {
        this.decimals = decimals;
    }
    @Column(name = "website")
    public String getWebsite() {
        return website;
    }

    public void setWebsite(String website) {
        this.website = website;
    }
    @Column(name = "whit_website")
    public String getWhitWebsite() {
        return whitWebsite;
    }

    public void setWhitWebsite(String whitWebsite) {
        this.whitWebsite = whitWebsite;
    }
    @Column(name = "browser")
    public String getBrowser() {
        return browser;
    }

    public void setBrowser(String browser) {
        this.browser = browser;
    }
    @Column(name = "logo_img")
    public String getLogoImg() {
        return logoImg;
    }

    public void setLogoImg(String logoImg) {
        this.logoImg = logoImg;
    }
    @Column(name = "twitter_link")
    public String getTwitterLink() {
        return twitterLink;
    }

    public void setTwitterLink(String twitterLink) {
        this.twitterLink = twitterLink;
    }
    @Column(name = "telegram_link")
    public String getTelegramLink() {
        return telegramLink;
    }

    public void setTelegramLink(String telegramLink) {
        this.telegramLink = telegramLink;
    }
    @Column(name = "introduction_cn")
    public String getIntroductionCn() {
        return introductionCn;
    }

    public void setIntroductionCn(String introductionCn) {
        this.introductionCn = introductionCn;
    }
    @Column(name = "introduction_en")
    public String getIntroductionEn() {
        return introductionEn;
    }

    public void setIntroductionEn(String introductionEn) {
        this.introductionEn = introductionEn;
    }
    @Column(name = "coin_total")
    public BigDecimal getCoinTotal() {
        return coinTotal;
    }

    public void setCoinTotal(BigDecimal coinTotal) {
        this.coinTotal = coinTotal;
    }
    @Column(name = "coin_trade")
    public BigDecimal getCoinTrade() {
        return coinTrade;
    }

    public void setCoinTrade(BigDecimal coinTrade) {
        this.coinTrade = coinTrade;
    }
    @Column(name = "coin_scaling")
    public BigDecimal getCoinScaling() {
        return coinScaling;
    }

    public void setCoinScaling(BigDecimal coinScaling) {
        this.coinScaling = coinScaling;
    }
    @Column(name = "price")
    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }
    @Column(name = "trading_link")
    public String getTradingLink() {
        return tradingLink;
    }

    public void setTradingLink(String tradingLink) {
        this.tradingLink = tradingLink;
    }
    @Column(name = "code_link")
    public String getCodeLink() {
        return codeLink;
    }

    public void setCodeLink(String codeLink) {
        this.codeLink = codeLink;
    }
    @Column(name = "introduce")
    public String getIntroduce() {
        return introduce;
    }

    public void setIntroduce(String introduce) {
        this.introduce = introduce;
    }

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    public Fuser getFuser() {
        return fuser;
    }

    public void setFuser(Fuser fuser) {
        this.fuser = fuser;
    }
    @Column(name = "fcreate_time",length = 0)
    public Timestamp getFcreateTime() {
        return fcreateTime;
    }

    public void setFcreateTime(Timestamp fcreateTime) {
        this.fcreateTime = fcreateTime;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        ApplyCoin applyCoin = (ApplyCoin) o;

        if (id != applyCoin.id) return false;
        if (decimals != applyCoin.decimals) return false;
        if (email != null ? !email.equals(applyCoin.email) : applyCoin.email != null) return false;
        if (phone != null ? !phone.equals(applyCoin.phone) : applyCoin.phone != null) return false;
        if (coinTypeEn != null ? !coinTypeEn.equals(applyCoin.coinTypeEn) : applyCoin.coinTypeEn != null) return false;
        if (coinTypeCn != null ? !coinTypeCn.equals(applyCoin.coinTypeCn) : applyCoin.coinTypeCn != null) return false;
        if (coinTypeMark != null ? !coinTypeMark.equals(applyCoin.coinTypeMark) : applyCoin.coinTypeMark != null)
            return false;
        if (icoDate != null ? !icoDate.equals(applyCoin.icoDate) : applyCoin.icoDate != null) return false;
        if (circulationDate != null ? !circulationDate.equals(applyCoin.circulationDate) : applyCoin.circulationDate != null)
            return false;
        if (InternetType != null ? !InternetType.equals(applyCoin.InternetType) : applyCoin.InternetType != null)
            return false;
        if (address != null ? !address.equals(applyCoin.address) : applyCoin.address != null) return false;
        if (website != null ? !website.equals(applyCoin.website) : applyCoin.website != null) return false;
        if (whitWebsite != null ? !whitWebsite.equals(applyCoin.whitWebsite) : applyCoin.whitWebsite != null)
            return false;
        if (browser != null ? !browser.equals(applyCoin.browser) : applyCoin.browser != null) return false;
        if (logoImg != null ? !logoImg.equals(applyCoin.logoImg) : applyCoin.logoImg != null) return false;
        if (twitterLink != null ? !twitterLink.equals(applyCoin.twitterLink) : applyCoin.twitterLink != null)
            return false;
        if (telegramLink != null ? !telegramLink.equals(applyCoin.telegramLink) : applyCoin.telegramLink != null)
            return false;
        if (introductionCn != null ? !introductionCn.equals(applyCoin.introductionCn) : applyCoin.introductionCn != null)
            return false;
        if (introductionEn != null ? !introductionEn.equals(applyCoin.introductionEn) : applyCoin.introductionEn != null)
            return false;
        if (coinTotal != null ? !coinTotal.equals(applyCoin.coinTotal) : applyCoin.coinTotal != null) return false;
        if (coinTrade != null ? !coinTrade.equals(applyCoin.coinTrade) : applyCoin.coinTrade != null) return false;
        if (coinScaling != null ? !coinScaling.equals(applyCoin.coinScaling) : applyCoin.coinScaling != null)
            return false;
        if (price != null ? !price.equals(applyCoin.price) : applyCoin.price != null) return false;
        if (tradingLink != null ? !tradingLink.equals(applyCoin.tradingLink) : applyCoin.tradingLink != null)
            return false;
        if (codeLink != null ? !codeLink.equals(applyCoin.codeLink) : applyCoin.codeLink != null) return false;
        if (introduce != null ? !introduce.equals(applyCoin.introduce) : applyCoin.introduce != null) return false;
        if (fuser != null ? !fuser.equals(applyCoin.fuser) : applyCoin.fuser != null) return false;
        return fcreateTime != null ? fcreateTime.equals(applyCoin.fcreateTime) : applyCoin.fcreateTime == null;
    }

    @Override
    public int hashCode() {
        int result = id;
        result = 31 * result + (email != null ? email.hashCode() : 0);
        result = 31 * result + (phone != null ? phone.hashCode() : 0);
        result = 31 * result + (coinTypeEn != null ? coinTypeEn.hashCode() : 0);
        result = 31 * result + (coinTypeCn != null ? coinTypeCn.hashCode() : 0);
        result = 31 * result + (coinTypeMark != null ? coinTypeMark.hashCode() : 0);
        result = 31 * result + (icoDate != null ? icoDate.hashCode() : 0);
        result = 31 * result + (circulationDate != null ? circulationDate.hashCode() : 0);
        result = 31 * result + (InternetType != null ? InternetType.hashCode() : 0);
        result = 31 * result + (address != null ? address.hashCode() : 0);
        result = 31 * result + decimals;
        result = 31 * result + (website != null ? website.hashCode() : 0);
        result = 31 * result + (whitWebsite != null ? whitWebsite.hashCode() : 0);
        result = 31 * result + (browser != null ? browser.hashCode() : 0);
        result = 31 * result + (logoImg != null ? logoImg.hashCode() : 0);
        result = 31 * result + (twitterLink != null ? twitterLink.hashCode() : 0);
        result = 31 * result + (telegramLink != null ? telegramLink.hashCode() : 0);
        result = 31 * result + (introductionCn != null ? introductionCn.hashCode() : 0);
        result = 31 * result + (introductionEn != null ? introductionEn.hashCode() : 0);
        result = 31 * result + (coinTotal != null ? coinTotal.hashCode() : 0);
        result = 31 * result + (coinTrade != null ? coinTrade.hashCode() : 0);
        result = 31 * result + (coinScaling != null ? coinScaling.hashCode() : 0);
        result = 31 * result + (price != null ? price.hashCode() : 0);
        result = 31 * result + (tradingLink != null ? tradingLink.hashCode() : 0);
        result = 31 * result + (codeLink != null ? codeLink.hashCode() : 0);
        result = 31 * result + (introduce != null ? introduce.hashCode() : 0);
        result = 31 * result + (fuser != null ? fuser.hashCode() : 0);
        result = 31 * result + (fcreateTime != null ? fcreateTime.hashCode() : 0);
        return result;
    }

}


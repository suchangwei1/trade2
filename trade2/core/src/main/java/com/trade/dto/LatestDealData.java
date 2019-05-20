package com.trade.dto;

import java.io.Serializable;

public class LatestDealData implements Serializable {

    private int fid;                // 货币ID
    private boolean fisShare;       // 是否可以交易
    private String fname;           // 币名
    private String fShortName;      // 币编码
    private String fname_sn;        // 币名（币编码）
    private String furl;            // 币图片
    private double fupanddown;      // 日涨跌
    private double fupanddownweek;  // 周涨跌
    private double fmarketValue;    // 总市值
    private double fentrustValue;   // 日成交额
    private double volumn;          // 成交量
    private double lastDealPrize;   // 最新成交价格
    private double higestBuyPrize;  // 最高买价
    private double lowestSellPrize; // 最低卖价
    private int status;             // 状态
    private String openTrade;       // 开启交易时间
    private int coinTradeType;      // 币币交易类型
    private String group;           // 交易区
    private boolean homeShow;       // 是否首页展示
    private double cnPrice;//人民币
    private double enPrice;//美金
    private int homeOrder;          // 首页次序
    private int typeOrder;          // 类型次序
    private int totalOrder;         //整体排序
    private int isCollection;   //是否收藏

    // 24小时数据
    private double lowestPrize24;   // 最低价
    private double highestPrize24;  // 最高价
    private double totalDeal24;     // 总成交价
    private double entrustValue24;  // 日交易金额

    public LatestDealData() {
    }

    public int getFid() {
        return fid;
    }

    public void setFid(int fid) {
        this.fid = fid;
    }

    public boolean isFisShare() {
        return fisShare;
    }

    public void setFisShare(boolean fisShare) {
        this.fisShare = fisShare;
    }

    public String getFname() {
        return fname;
    }

    public void setFname(String fname) {
        this.fname = fname;
    }

    public String getfShortName() {
        return fShortName;
    }

    public void setfShortName(String fShortName) {
        this.fShortName = fShortName;
    }

    public String getFname_sn() {
        return fname_sn;
    }

    public void setFname_sn(String fname_sn) {
        this.fname_sn = fname_sn;
    }

    public double getFupanddown() {
        return fupanddown;
    }

    public void setFupanddown(double fupanddown) {
        this.fupanddown = fupanddown;
    }

    public String getFurl() {
        return furl;
    }

    public void setFurl(String furl) {
        this.furl = furl;
    }

    public double getFupanddownweek() {
        return fupanddownweek;
    }

    public void setFupanddownweek(double fupanddownweek) {
        this.fupanddownweek = fupanddownweek;
    }

    public double getFmarketValue() {
        return fmarketValue;
    }

    public void setFmarketValue(double fmarketValue) {
        this.fmarketValue = fmarketValue;
    }

    public double getFentrustValue() {
        return fentrustValue;
    }

    public void setFentrustValue(double fentrustValue) {
        this.fentrustValue = fentrustValue;
    }

    public double getVolumn() {
        return volumn;
    }

    public void setVolumn(double volumn) {
        this.volumn = volumn;
    }

    public double getLastDealPrize() {
        return lastDealPrize;
    }

    public void setLastDealPrize(double lastDealPrize) {
        this.lastDealPrize = lastDealPrize;
    }

    public double getHigestBuyPrize() {
        return higestBuyPrize;
    }

    public void setHigestBuyPrize(double higestBuyPrize) {
        this.higestBuyPrize = higestBuyPrize;
    }

    public double getLowestSellPrize() {
        return lowestSellPrize;
    }

    public void setLowestSellPrize(double lowestSellPrize) {
        this.lowestSellPrize = lowestSellPrize;
    }

    public double getLowestPrize24() {
        return lowestPrize24;
    }

    public void setLowestPrize24(double lowestPrize24) {
        this.lowestPrize24 = lowestPrize24;
    }

    public double getHighestPrize24() {
        return highestPrize24;
    }

    public void setHighestPrize24(double highestPrize24) {
        this.highestPrize24 = highestPrize24;
    }

    public double getTotalDeal24() {
        return totalDeal24;
    }

    public void setTotalDeal24(double totalDeal24) {
        this.totalDeal24 = totalDeal24;
    }

    public double getEntrustValue24() {
        return entrustValue24;
    }

    public void setEntrustValue24(double entrustValue24) {
        this.entrustValue24 = entrustValue24;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getOpenTrade() {
        return openTrade;
    }

    public void setOpenTrade(String openTrade) {
        this.openTrade = openTrade;
    }

    public int getCoinTradeType() {
        return coinTradeType;
    }

    public void setCoinTradeType(int coinTradeType) {
        this.coinTradeType = coinTradeType;
    }

    public boolean isHomeShow() {
        return homeShow;
    }

    public void setHomeShow(boolean homeShow) {
        this.homeShow = homeShow;
    }

    public int getHomeOrder() {
        return homeOrder;
    }

    public void setHomeOrder(int homeOrder) {
        this.homeOrder = homeOrder;
    }

    public int getTypeOrder() {
        return typeOrder;
    }

    public void setTypeOrder(int typeOrder) {
        this.typeOrder = typeOrder;
    }

    @Override
    public String toString() {
        return "LatestDealData{" +
                "fid=" + fid +
                ", fisShare=" + fisShare +
                ", fname='" + fname + '\'' +
                ", fShortName='" + fShortName + '\'' +
                ", fname_sn='" + fname_sn + '\'' +
                ", furl='" + furl + '\'' +
                ", fupanddown=" + fupanddown +
                ", fupanddownweek=" + fupanddownweek +
                ", fmarketValue=" + fmarketValue +
                ", fentrustValue=" + fentrustValue +
                ", volumn=" + volumn +
                ", lastDealPrize=" + lastDealPrize +
                ", higestBuyPrize=" + higestBuyPrize +
                ", lowestSellPrize=" + lowestSellPrize +
                ", lowestPrize24=" + lowestPrize24 +
                ", highestPrize24=" + highestPrize24 +
                ", totalDeal24=" + totalDeal24 +
                ", entrustValue24=" + entrustValue24 +
                '}';
    }

    public int getTotalOrder() {
        return totalOrder;
    }

    public void setTotalOrder(int totalOrder) {
        this.totalOrder = totalOrder;
    }

    public String getGroup() {
        return group;
    }

    public void setGroup(String group) {
        this.group = group;
    }

    public int getIsCollection() {
        return isCollection;
    }

    public void setIsCollection(int isCollection) {
        this.isCollection = isCollection;
    }

    public double getCnPrice() {
        return cnPrice;
    }

    public void setCnPrice(double cnPrice) {
        this.cnPrice = cnPrice;
    }

    public double getEnPrice() {
        return enPrice;
    }

    public void setEnPrice(double enPrice) {
        this.enPrice = enPrice;
    }
}

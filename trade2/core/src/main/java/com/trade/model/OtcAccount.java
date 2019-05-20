package com.trade.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import javax.persistence.*;

@Entity
@Table(name = "otc_account")
@JsonIgnoreProperties({
        "fuser",
})
public class OtcAccount {
    private int id;
    private String bankName;
    private String bankBranch;
    private String bankAccount;
    private String aliUrl;
    private String aliAccount;
    private String wechatUrl;
    private String wechatAccount;
    private Fuser fuser;

    public OtcAccount(){}

    public OtcAccount(String bankName, String bankBranch, String bankAccount, String aliUrl, String aliAccount, String wechatUrl, String wechatAccount, Fuser fuser) {
        this.bankName = bankName;
        this.bankBranch = bankBranch;
        this.bankAccount = bankAccount;
        this.aliUrl = aliUrl;
        this.aliAccount = aliAccount;
        this.wechatUrl = wechatUrl;
        this.wechatAccount = wechatAccount;
        this.fuser = fuser;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @Column(name = "bank_name")
    public String getBankName() {
        return bankName;
    }

    public void setBankName(String bankName) {
        this.bankName = bankName;
    }

    @Column(name = "bank_branch")
    public String getBankBranch() {
        return bankBranch;
    }

    public void setBankBranch(String bankBranch) {
        this.bankBranch = bankBranch;
    }

    @Column(name = "bank_account")
    public String getBankAccount() {
        return bankAccount;
    }

    public void setBankAccount(String bankAccount) {
        this.bankAccount = bankAccount;
    }

    @Column(name = "ali_url")
    public String getAliUrl() {
        return aliUrl;
    }

    public void setAliUrl(String aliUrl) {
        this.aliUrl = aliUrl;
    }

    @Column(name = "ali_account")
    public String getAliAccount() {
        return aliAccount;
    }

    public void setAliAccount(String aliAccount) {
        this.aliAccount = aliAccount;
    }

    @Column(name = "wechat_url")
    public String getWechatUrl() {
        return wechatUrl;
    }

    public void setWechatUrl(String wechatUrl) {
        this.wechatUrl = wechatUrl;
    }

    @Column(name = "wechat_account")
    public String getWechatAccount() {
        return wechatAccount;
    }

    public void setWechatAccount(String wechatAccount) {
        this.wechatAccount = wechatAccount;
    }

    @OneToOne
    @JoinColumn(name = "uid")
    public Fuser getFuser() {
        return fuser;
    }

    public void setFuser(Fuser fuser) {
        this.fuser = fuser;
    }

    @Override
    public String toString() {
        return "OtcAccount{" +
                "id=" + id +
                ", bankName='" + bankName + '\'' +
                ", bankBranch='" + bankBranch + '\'' +
                ", bankAccount='" + bankAccount + '\'' +
                ", aliUrl='" + aliUrl + '\'' +
                ", aliAccount='" + aliAccount + '\'' +
                ", wechatUrl='" + wechatUrl + '\'' +
                ", wechatAccount='" + wechatAccount + '\'' +
                ", fuser=" + fuser +
                '}';
    }
}

package com.trade.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "ccuser")
@JsonIgnoreProperties({
    "ccLogs",
})
public class CcUser implements java.io.Serializable {

    private int id;
    private String name;
    private String contactWay;
    private String branch;
    private String account;
    private Set<CcLog> ccLogs = new HashSet<>(0);

    public CcUser(){}

    public CcUser(String name, String contactWay, String branch, String account) {
        this.name = name;
        this.contactWay = contactWay;
        this.branch = branch;
        this.account = account;
    }

    @GenericGenerator(name = "generator", strategy = "identity")
    @Id
    @GeneratedValue(generator = "generator")
    @Column(name = "id", unique = true, nullable = false)
    public int getId() {
        return this.id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @Column(name = "name")
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Column(name = "contact_way")
    public String getContactWay() {
        return contactWay;
    }

    public void setContactWay(String contactWay) {
        this.contactWay = contactWay;
    }

    @OneToMany( mappedBy = "seller")
    public Set<CcLog> getCcLogs() {
        return ccLogs;
    }

    public void setCcLogs(Set<CcLog> ccLogs) {
        this.ccLogs = ccLogs;
    }

    @Column(name = "branch")
    public String getBranch() {
        return branch;
    }

    public void setBranch(String branch) {
        this.branch = branch;
    }

    @Column(name = "account")
    public String getAccount() {
        return account;
    }

    public void setAccount(String account) {
        this.account = account;
    }
}
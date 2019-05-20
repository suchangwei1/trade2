package com.trade.model;

import com.trade.Enum.TradeApiType;
import com.trade.Enum.UseStatus;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;
import java.util.Date;

@Entity
@Table(name = "ftrade_api")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class TradeApi implements Serializable {
    private int id;
    private int userId;
    private String name;
    private String apiKey;
    private String secret;
    private TradeApiType type;      // 权限
    private UseStatus status;       // 状态
    private Date createTime;
    private Date updateTime;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @Column(name = "user_id")
    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    @Column(length = 128)
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Column(length = 128, name = "api_key")
    public String getApiKey() {
        return apiKey;
    }

    public void setApiKey(String apiKey) {
        this.apiKey = apiKey;
    }

    @Column(length = 256)
    public String getSecret() {
        return secret;
    }

    public void setSecret(String secret) {
        this.secret = secret;
    }

    @Column
    @Enumerated(EnumType.ORDINAL)
    public TradeApiType getType() {
        return type;
    }

    public void setType(TradeApiType type) {
        this.type = type;
    }

    @Column
    @Enumerated(EnumType.ORDINAL)
    public UseStatus getStatus() {
        return status;
    }

    public void setStatus(UseStatus status) {
        this.status = status;
    }

    @Column(name = "create_time")
    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    @Column(name = "update_time")
    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }
}

package com.trade.dto;

import java.io.Serializable;

/**
 * 各类数据统计
 */
public class CountData implements Serializable {

    private long userQty;   // 用户数
    private long tradeAmt;  // 交易金额

    public long getUserQty() {
        return userQty;
    }

    public void setUserQty(long userQty) {
        this.userQty = userQty;
    }

    public long getTradeAmt() {
        return tradeAmt;
    }

    public void setTradeAmt(long tradeAmt) {
        this.tradeAmt = tradeAmt;
    }
}

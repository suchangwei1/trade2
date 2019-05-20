package com.trade.model;

import java.util.Date;

public class BTCInfo {
	private String account;// 帐户，USERID
	private String address;// 充向地址
	private String category;// 类型，receive OR SEND
	private double amount;// 数量
	private int confirmations;// 确认数
	private String txid;// 交易ID
	private Date time;// 时间
	private String comment;// 备注

	public String getAccount() {
		return account;
	}

	public void setAccount(String account) {
		this.account = account;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getCategory() {
		return category;
	}

	public void setCategory(String category) {
		this.category = category;
	}

	public double getAmount() {
		return amount;
	}

	public void setAmount(double amount) {
		this.amount = amount;
	}

	public int getConfirmations() {
		return confirmations;
	}

	public void setConfirmations(int confirmations) {
		this.confirmations = confirmations;
	}

	public String getTxid() {
		return txid;
	}

	public void setTxid(String txid) {
		this.txid = txid;
	}

	public Date getTime() {
		return time;
	}

	public void setTime(Date time) {
		this.time = time;
	}

	public String getComment() {
		return comment;
	}

	public void setComment(String comment) {
		this.comment = comment;
	}

	@Override
	public String toString() {
		return "BTCInfo{" +
				"account='" + account + '\'' +
				", address='" + address + '\'' +
				", category='" + category + '\'' +
				", amount=" + amount +
				", confirmations=" + confirmations +
				", txid='" + txid + '\'' +
				", time=" + time +
				", comment='" + comment + '\'' +
				'}';
	}
}

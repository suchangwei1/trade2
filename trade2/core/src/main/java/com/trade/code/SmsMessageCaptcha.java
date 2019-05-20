package com.trade.code;

import java.sql.Timestamp;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.trade.Enum.CountLimitTypeEnum;
import com.trade.util.Constants;
import com.trade.util.Utils;

@JsonIgnoreProperties({"expire", "type"})
public class SmsMessageCaptcha implements Captcha {
	private String code;
	private String areaCode;
	private String cellphone;
	private Timestamp createTime;

	public SmsMessageCaptcha() {
	}

	public SmsMessageCaptcha(String code, String areaCode, String cellphone) {
		super();
		this.code = code;
		this.areaCode = areaCode;
		this.cellphone = cellphone;
		this.createTime = Utils.getTimestamp();
	}

	public void setCode(String code) {
		this.code = code;
	}

	public void setAreaCode(String areaCode) {
		this.areaCode = areaCode;
	}

	public void setCellphone(String cellphone) {
		this.cellphone = cellphone;
	}

	public void setCreateTime(Timestamp createTime) {
		this.createTime = createTime;
	}

	@Override
	public String getCode() {
		return code;
	}

	@Override
	public Timestamp getCreateTime() {
		return createTime;
	}

	public String getAreaCode() {
		return areaCode;
	}

	public String getCellphone() {
		return cellphone;
	}

	@Override
	public boolean isExpire() {
		return System.currentTimeMillis() - this.createTime.getTime() > Constants.CAPTCHA_TIME_OUT;
	}

	@Override
	public int getType() {
		return CountLimitTypeEnum.TELEPHONE;
	}
}











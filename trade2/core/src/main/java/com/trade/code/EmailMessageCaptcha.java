package com.trade.code;

import java.sql.Timestamp;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.trade.Enum.CountLimitTypeEnum;
import com.trade.util.Constants;
import com.trade.util.Utils;

@JsonIgnoreProperties({"expire", "type"})
public class EmailMessageCaptcha implements Captcha {
	private String code;
	private String email;
	private Timestamp createTime;

	public EmailMessageCaptcha() {
	}

	public EmailMessageCaptcha(String code, String email) {
		super();
		this.code = code;
		this.email = email;
		this.createTime = Utils.getTimestamp();
	}

	public void setCode(String code) {
		this.code = code;
	}

	public void setEmail(String email) {
		this.email = email;
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

	public String getEmail() {
		return email;
	}
	
	@Override
	public boolean isExpire() {
		return System.currentTimeMillis() - this.createTime.getTime() > Constants.CAPTCHA_TIME_OUT;
	}

	@Override
	public int getType() {
		return CountLimitTypeEnum.EMAIL;
	}
}

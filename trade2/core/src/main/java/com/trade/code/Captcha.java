package com.trade.code;

import java.sql.Timestamp;

public interface Captcha {
	
	String getCode();
	
	Timestamp getCreateTime();
	
	boolean isExpire();

	int getType();

}

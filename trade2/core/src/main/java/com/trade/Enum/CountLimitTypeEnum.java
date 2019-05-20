package com.trade.Enum;

public class CountLimitTypeEnum {
    public static final int GOOGLE = 0;
    public static final int LOGIN_PASSWORD = 1;
    public static final int TRADE_PASSWORD = 2;
    public static final int TELEPHONE = 3;
    public static final int AdminLogin = 4;
    public static final int EMAIL = 5;
    public static final int IMAGE_CAPTCHA = 6;
    public static final int VOICE_CAPTCHA = 7;

    public static String getEnumString(int value) {
		String name = "";
		switch (value) {
		case GOOGLE:
			name = "谷歌验证" ;
			break;
		case LOGIN_PASSWORD:
			name = "登陆密码" ;		
			break;
		case TRADE_PASSWORD:
			name = "交易密码" ;
			break;
		case TELEPHONE:
			name = "短信验证" ;
			break;
		case EMAIL:
			name = "邮箱验证" ;
			break;
		case IMAGE_CAPTCHA:
			name = "图片验证码验证" ;
			break;
		case AdminLogin:
			name = "管理员登陆" ;
			break;
		case VOICE_CAPTCHA:
			name = "语音验证" ;
			break;
		}
		return name;
	}
    
}

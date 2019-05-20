package com.trade.Enum;

public class SendMailTypeEnum {
    public static final int ValidateMail = 1;//验证邮件
    public static final int FindPassword = 2;//找回密码
    
    public static String getEnumString(int value) {
		String name = "";
		if(value == ValidateMail){
			name = "ValidateMail";
		}else if(value == FindPassword){
			name = "ValidateMail";
		}
		return name;
	}
    
}

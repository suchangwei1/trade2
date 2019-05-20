package com.trade.Enum;

public class LogTypeEnum {
    public static final int User_LOGIN = 1;//
    
    public static String getEnumString(int value) {
		String name = "";
		if(value == User_LOGIN){
			name = "用户登录";
		}
		return name;
	}
    
}

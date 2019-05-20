package com.trade.Enum;

public class ActivityStatusEnum {
    public static final int ACTIVE = 1;//
    public static final int NOT_ACTIVE = 2;//
    
    public static String getEnumString(int value) {
		String name = "";
		if(value == ACTIVE){
			name = "正常";
		}else if(value == NOT_ACTIVE){
			name = "禁用";
		}
		return name;
	}
    
}

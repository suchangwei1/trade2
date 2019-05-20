package com.trade.Enum;

public class SharePlanLogStatusEnum {
    public static final int NOSEND = 1;//初始化
    public static final int HASSEND = 2;//已发送
    
    public static String getEnumString(int value) {
		String name = "";
		if(value == NOSEND){
			name = "未发送";
		}else if(value == HASSEND){
			name = "已发送";
		}
		return name;
	}
}

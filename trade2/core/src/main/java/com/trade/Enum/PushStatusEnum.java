package com.trade.Enum;


public class PushStatusEnum {
    public static final int READY_VALUE = 0;//
    public static final int SUCCEED_VALUE = 1;//
    public static final int FALIED_VALUE = 2;//
    
    public static String getEnumString(int value) {
		String name = "";
		if(value == READY_VALUE){
			name = "待推送";
		}else if(value == SUCCEED_VALUE){
			name = "推送成功";
		}else if(value == FALIED_VALUE){
			name = "推送失败";
		}
		return name;
	}
    
}

package com.trade.Enum;

public class HedginglogStatusEnum {
    public static final int NOSTART = 1;//正常
    public static final int TAKING = 2;//禁用
    public static final int DOING = 3;//禁用
    public static final int END = 4;//禁用
    
    public static String getEnumString(int value) {
		String name = "";
		if(value == NOSTART){
			name = "未开始";
		}else if(value == TAKING){
			name = "投注中";
		}else if(value == DOING){
			name = "开奖中";
		}else if(value == END){
			name = "已结束";
		}
		return name;
	}
    
}

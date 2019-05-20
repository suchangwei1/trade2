package com.trade.Enum;

public class HedginglogTypeEnum {
	public static final int NO = 0;//正常
    public static final int UP = 1;//正常
    public static final int PING = 2;//禁用
    public static final int DOWN = 3;//禁用
    
    public static String getEnumString(int value) {
		String name = "";
		if(value == UP){
			name = "涨";
		}else if(value == PING){
			name = "平";
		}else if(value == DOWN){
			name = "跌";
		}else if(value == NO){
			name = "无";
		}
		return name;
	}
    
}

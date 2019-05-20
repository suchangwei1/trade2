package com.trade.Enum;

public class HedgingTypeEnum {
    public static final int UP = 1;//正常
    public static final int PING = 2;//禁用
    public static final int DOWN = 3;//禁用
    
    public static String getEnumString(int value) {
		String name = "";
		if(value == UP){
			name = "看涨";
		}else if(value == PING){
			name = "看平";
		}else if(value == DOWN){
			name = "看跌";
		}
		return name;
	}
    
}

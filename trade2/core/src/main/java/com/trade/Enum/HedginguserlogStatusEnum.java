package com.trade.Enum;

public class HedginguserlogStatusEnum {
    public static final int DOING = 1;//正常
    public static final int SENDING = 2;//禁用
    public static final int WIN = 3;//禁用
    public static final int LOST = 4;//禁用
    
    public static String getEnumString(int value) {
		String name = "";
		if(value == DOING){
			name = "等待开奖";
		}else if(value == SENDING){
			name = "发奖中";
		}else if(value == WIN){
			name = "中奖";
		}else if(value == LOST){
			name = "未中奖";
		}
		return name;
	}
    
}

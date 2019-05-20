package com.trade.Enum;

public class LotteryRewardTypeEnum {
    public static final int SHIWU = 1;//
    public static final int XUNIBI = 2;//
    
    public static String getEnumString(int value) {
		String name = "";
		if(value == SHIWU){
			name = "实物";
		}else if(value == XUNIBI){
			name = "金豆";
		}
		return name;
	}
    
}

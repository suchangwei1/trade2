package com.trade.Enum;

public class EntrustRobotStatusEnum { 
    public static final int Normal = 0;//正常挂单
    public static final int Robot1 = 1 ;//机器人挂单可成交
    public static final int Robot2 = 2;//机器人挂单不成交
    
    public static String getEnumString(int value) {
		String name = "";
		if(value == Normal){
			name = "正常挂单";
		}else if(value == Robot1){
			name = "机器人挂单可成交";
		}else if(value == Robot2){
			name = "机器人挂单不成交";
		}

		return name;
	}
}

package com.trade.Enum;

public class OperationlogEnum {
    public static final int SAVE = 1;//保存
    public static final int AUDIT = 2;//审核
   
    public static String getEnumString(int value) {
		String name = "";
		if(value == SAVE){
			name = "暂存";
		}else if(value == AUDIT){
			name = "已审核";
		}
		return name;
	}
}

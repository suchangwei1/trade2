package com.trade.Enum;

public class CanPayTypeEnum {
    public static final int CNY = 0;
    public static final int ZGC = 1;
    public static final int ALL = 2;
    
    public static String getEnumString(int value) {
		String name = "";
		if(value == CNY){
			name = "人民币";
		}else if(value == ZGC){
			name = "ZGC";
		}else if(value == ALL){
			name = "全部";
		}
		return name;
	}
    
}

package com.trade.Enum;

public class PaytypeEnum {
    public static final int CNY = 0;
    public static final int ZGC = 1;
    public static final int ZTUP = 2;
    
    public static String getEnumString(int value) {
		String name = "";
		if(value == CNY){
			name = "人民币";
		}else if(value == ZGC){
			name = "ZGC";
		}else if(value == ZTUP){
			name = "Z-TUP";
		}
		return name;
	}
    
}

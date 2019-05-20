package com.trade.Enum;

public class GameTypeEnum {
    public static final int LB = 0;
    public static final int HLG = 1;
    public static final int SZ = 2;
    public static final int JDS = 3;
    public static final int JZ = 4;
    public static final int GZ = 5;
    public static final int RSG = 6;
    public static final int HS = 7;
    public static final int QZ = 8;
            
    public static String getEnumString(int value) {
		String name = "";
		if(value == LB){
			name = "萝卜";
		}else if(value == HLG){
			name = "火龙果";
		}else if(value == SZ){
			name = "柿子";
		}else if(value == JDS){
			name = "金豆树";
		}else if(value == JZ){
			name = "橘子";
		}else if(value == GZ){
			name = "甘蔗";
		}else if(value == RSG){
			name = "人参果";
		}else if(value == HS){
			name = "花生";
		}else if(value == QZ){
			name = "茄子";
		}
		return name;
	}
    
    public static String getEnumName(int value) {
		String name = "";
		if(value == LB){
			name = "LB";
		}else if(value == HLG){
			name = "HLG";
		}else if(value == SZ){
			name = "SZ";
		}else if(value == JDS){
			name = "JDS";
		}else if(value == JZ){
			name = "JZ";
		}else if(value == GZ){
			name = "GZ";
		}else if(value == RSG){
			name = "RSG";
		}else if(value == HS){
			name = "HS";
		}else if(value == QZ){
			name = "QZ";
		}
		return name;
	}
}

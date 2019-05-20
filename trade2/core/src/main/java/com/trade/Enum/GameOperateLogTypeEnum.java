package com.trade.Enum;

public class GameOperateLogTypeEnum {
	public static final int ZHONG = 1;//
	public static final int SHOU = 2;//
	public static final int UPGRAD = 3;//
	public static final int GRASS = 4;
	public static final int BUY = 5;
	
	public static String getEnumString(int value) {
		String name = "";
		switch (value) {
		case ZHONG:
			name = "种值";
			break;
		case SHOU:
			name = "收获";
			break;
		case UPGRAD:
			name = "升级";
			break;
		case GRASS:
			name = "除草";
			break;	
		case BUY:
			name = "购买土地";
			break;		
		}
		return name;
	}
}

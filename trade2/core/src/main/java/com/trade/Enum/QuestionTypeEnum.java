package com.trade.Enum;

public class QuestionTypeEnum {
    public static final int COIN_RECHARGE = 1;
    public static final int COIN_WITHDRAW = 2;
/*    public static final int CNY_RECHARGE = 3;
    public static final int CNY_WITHDRAW = 4;*/
    public static final int OTHERS = 3;
    
    public static String getEnumString(int value) {
		String name = "";
		switch (value) {
		case COIN_RECHARGE:
			name = "充值问题";
			break;
		case COIN_WITHDRAW:
			name = "提现问题";		
			break;
/*		case CNY_RECHARGE:
			name = "浇水问题";
			break;
		case CNY_WITHDRAW:
			name = "预购树苗问题";
			break;*/
		case OTHERS:
			name = "其他问题";
			break;
		}
		return name;
	}
    
}

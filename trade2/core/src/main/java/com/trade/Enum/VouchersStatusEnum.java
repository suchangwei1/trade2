package com.trade.Enum;

//短信内容类型
public class VouchersStatusEnum {
    public static final int NO = 1;//申请api
    public static final int DOING = 2;//绑定手机
    public static final int USED = 3;//解绑手机
    public static final int END = 4;//解绑手机
    
    
    
    public static String getEnumString(int value) {
		String name = "";
		switch (value) {
		case NO:
			name = "未激活";
			break;
		case DOING:
			name = "激活中";
			break;
		case USED:
			name = "已使用";
			break;
		case END:
			name = "已过期";
			break;	
		}
		return name;
	}
    
}

package com.trade.Enum;



public class PushOpenTyepEnum {

	/**
	 * 系统消息
	 */
    public static final int SYSTEM_MSG = 1;

	/**
	 * 财务总览
	 */
	public static final int ALL_FINANCE = 3;

	/**
	 * 个人财务
	 */
	public static final int FINANCE = 4;

	/**
	 * 委托管理
	 */
	public static final int ENTRUST = 6;

	/**
	 * 资讯
	 */
	public static final int ARTICLE = 7;

	/**
	 * 公告
	 */
    public static final int NOTICE = 8;

    public static String getEnumString(int value) {
		String name = "";
		if(value == SYSTEM_MSG){
			name = "系统消息";
		}else if(value == ALL_FINANCE){
			name = "财务总览";
		}else if(value == FINANCE){
			name = "个人财务";
		}else if(value == ENTRUST){
			name = "委托管理";
		}else if(value == ARTICLE){
			name = "公告";
		}else if(value == NOTICE){
			name = "资讯";
		}
		return name;
	}
    
}

package com.trade.Enum;

/**
 * 意见反馈状态类型
 */
public class FeedbackStatusEnum {
	public static final int UNTREATED = 1 ;
	public static final int TREATING = 2 ;
	public static final int TREATED = 3 ;
	public static final int DELETED = 4 ;

	public static String getEnumString(int value) {
		String name = "";
		if(value == UNTREATED){
			name = "未处理";
		}else if(value == TREATING){
			name = "处理中";
		}else if(value == TREATED){
			name = "已处理";
		}else if(value == DELETED ){
			name = "已删除";
		}else{
			name = "状态异常";
		}
		return name;
	}
}

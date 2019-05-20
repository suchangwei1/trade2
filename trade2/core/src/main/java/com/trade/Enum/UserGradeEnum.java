package com.trade.Enum;

public class UserGradeEnum {
	public static final int LEVEL0 = 0;//未委托
    public static final int LEVEL1 = 1;//未委托
    public static final int LEVEL2 = 2 ;//已委托
    public static final int LEVEL3 = 3;//取消
    public static final int LEVEL4 = 4;//取消
    public static final int LEVEL5 = 5;//取消
    
 //  有效用户； 金豆初级玩家（个数），金豆资深玩家（个数），金豆圈子成员（个数），金豆圈子领袖（个数）
    public static String getEnumString(int value) {
		String name = "";
		if(value == LEVEL0){
			name = "普通用户";
		}else if(value == LEVEL1){
			name = "有效用户";
		}else if(value == LEVEL2){
			name = "金豆初级玩家";
		}else if(value == LEVEL3){
			name = "金豆资深玩家";
		}else if(value == LEVEL4){
			name = "金豆高级玩家";
		}else if(value == LEVEL5){
			name = "金豆圈子领袖";
		}
		return name;
	}
    
}

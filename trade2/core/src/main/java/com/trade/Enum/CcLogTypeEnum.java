package com.trade.Enum;

public enum CcLogTypeEnum {

	BUY,SELL;

	public int getIndex(){
		return this.ordinal();
	}

	public static CcLogTypeEnum get(int index){
		for(CcLogTypeEnum type : values()){
			if(index == type.ordinal()){
				return type;
			}
		}
		return null;
	}

	public String getName() {
		if(this.equals(BUY)){
			return "买入";
		}else if(this.equals(SELL)){
			return "卖出";
		}
		return "";
	}
    
}

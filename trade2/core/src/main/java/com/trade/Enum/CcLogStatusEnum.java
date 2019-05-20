package com.trade.Enum;

public enum CcLogStatusEnum {
    UN_CHECK, PASSED, NO_PASSED;

	public int getIndex(){
		return this.ordinal();
	}

	public static CcLogStatusEnum get(int index){
		for(CcLogStatusEnum status : values()){
			if(index == status.ordinal()){
				return status;
			}
		}
		return null;
	}

	public String getName() {
		if(this.equals(UN_CHECK)){
			return "未处理";
		}else if(this.equals(PASSED)){
			return "交易成功";
		}else if(this.equals(NO_PASSED)){
			return "已撤销";
		}
		return "";
	}
}
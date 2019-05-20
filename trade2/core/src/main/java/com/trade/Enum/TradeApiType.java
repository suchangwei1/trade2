package com.trade.Enum;

public enum TradeApiType {
    All, Read_Only, Closed;

    public static TradeApiType get(int type){
        for(TradeApiType apiType : TradeApiType.values()){
            if(type == apiType.ordinal()){
                return apiType;
            }
        }
        return null;
    }

    public static int getEnableSize(){
        return values().length - 1;
    }

    /**
     * api key的前缀，可根据key判断出api权限及合法性
     *
     * @return
     */
    public char getPrefix(){
        return this.toString().toLowerCase().charAt(0);
    }

    public int getIndex(){
        return this.ordinal();
    }

    public static TradeApiType getByPrefix(char prefix){
        for(TradeApiType type : values()){
            if(0 == type.toString().toLowerCase().indexOf(prefix)){
                return type;
            }
        }
        return null;
    }

    public String getName(){
        if(this.equals(TradeApiType.All)){
            return "全部权限";
        }else if(this.equals(TradeApiType.Read_Only)){
            return "只读权限";
        }else if(this.equals(TradeApiType.Closed)){
            return "关闭";
        }
        return "";
    }
}

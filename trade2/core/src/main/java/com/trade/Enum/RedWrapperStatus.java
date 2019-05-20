package com.trade.Enum;

public enum RedWrapperStatus {
    Default, Normal, Finished, Refunded, Part_Refunded;

    public int getIndex(){
        return this.ordinal();
    }
}

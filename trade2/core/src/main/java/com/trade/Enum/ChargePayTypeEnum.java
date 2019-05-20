package com.trade.Enum;

public enum ChargePayTypeEnum {

    Default, _95epay_EBank, Wechat_Scan_Code;

    public static ChargePayTypeEnum get(int payType){
        for(ChargePayTypeEnum type : values()){
            if(payType == type.ordinal()){
                return type;
            }
        }
        return ChargePayTypeEnum.Default;
    }

    public int getIndex(){
        return this.ordinal();
    }

    public String getName(){
        String name = "线下充值";
        switch (this.ordinal()){
            case 1:
                name = "双乾网银支付";
            break;
            case 2:
                name = "微信扫码支付";
            break;
        }
        return name;
    }

}

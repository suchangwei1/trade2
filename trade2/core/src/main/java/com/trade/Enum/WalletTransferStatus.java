package com.trade.Enum;

public enum WalletTransferStatus {
    Paid, Going, Success, Refund;

    public String getName(){
        if(this.equals(Paid)){
            return "已支付";
        }
        if(this.equals(Going)){
            return "进行中";
        }
        if(this.equals(Success)){
            return "已到账";
        }
        if(this.equals(Refund)){
            return "已退款";
        }

        return null;
    }

    public static WalletTransferStatus get(int index){
        for(WalletTransferStatus status : values()){
            if(index == status.ordinal()){
                return status;
            }
        }

        return null;
    }

    public int getIndex(){
        return this.ordinal();
    }
}

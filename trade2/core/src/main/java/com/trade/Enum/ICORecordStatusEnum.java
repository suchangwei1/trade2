package com.trade.Enum;

/**
 * Author:xuelin
 * Company:招股金服
 * Date:2017/5/15
 * Desc:
 */
public enum ICORecordStatusEnum {

    IN_PROGRESS("认购中"), SUCCESS("待回报"), REFUND("已退款"), REQUITE("已回报");

    private String name;

    ICORecordStatusEnum() {
    }

    ICORecordStatusEnum(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getIndex() {
        return this.ordinal();
    }

    public static ICORecordStatusEnum valueOf(int index) {
        for(ICORecordStatusEnum status : ICORecordStatusEnum.values()) {
            if(index == status.ordinal()) {
                return status;
            }
        }

        return null;
    }

    @Override
    public String toString() {
        return "ICORecordStatusEnum{" +
                "name='" + name + '\'' +
                '}';
    }
}

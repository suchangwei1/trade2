package com.trade.Enum;

/**
 * Author:xuelin
 * Company:招股金服
 * Date:2017/6/27
 * Desc:
 */
public enum ICOStatusEnum {

    DEFAULT("未完成"), SUCCESS("已完成"), FAILURE("已失败");

    private String name;

    ICOStatusEnum() {
    }

    ICOStatusEnum(String name) {
        this.name = name;
    }

    public int getIndex() {
        return this.ordinal();
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public static ICOStatusEnum valueOf(int index) {
        for (ICOStatusEnum statusEnum : ICOStatusEnum.values()) {
            if (index == statusEnum.ordinal()) {
                return statusEnum;
            }
        }

        return null;
    }
}

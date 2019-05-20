package com.trade.Enum;

import org.apache.commons.collections.map.HashedMap;

import java.util.Map;

public enum EpayBankEnum {
    All(""),
    ICBC("中国工商银行"),
    ABC("中国农业银行"),
    BOCSH("中国银行"),
    CCB("中国建设银行"),
    CMB("招商银行"),
    SPDB("上海浦东发展银行"),
    GDB("广东发展银行"),
    BOCOM("交通银行"),
    PSBC("中国邮政储蓄银行"),
    CNCB("中信银行"),
    CMBC("中国民生银行"),
    CEB("光大银行"),
    HXB("华夏银行"),
    NJCB("南京银行"),
    CBHB("渤海银行"),
    BRCB("北京农村商业银行"),
    HZCB("杭州银行"),
    PAB("平安银行"),
    BOS("上海银行"),
    NBBANK("宁波银行"),
    FDB("富滇银行"),
    BCCB("北京银行"),
    SRCB("上海农村商业银行"),
    HFCBCNSH("微商银行"),
    CZBANK("浙商银行"),
    CIB("兴业银行");

    private String name;

    EpayBankEnum(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public String getEnName() {
        return this.toString();
    }

    public static EpayBankEnum get(String bank) {
        for (EpayBankEnum epayBankEnum : values()) {
            if (epayBankEnum.toString().equals(bank)) {
                return epayBankEnum;
            }
        }
        return null;
    }

    public final static Map<String, EpayBankEnum> banks = new HashedMap(values().length);

    static{
        for (EpayBankEnum epayBankEnum : values()) {
            banks.put(epayBankEnum.getName(), epayBankEnum);
        }
    }
}

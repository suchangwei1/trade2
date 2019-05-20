package com.trade.api;

public enum APIResultCode {
    Code_200("200", "成功"),

    /*-------------接口交互错误---------------*/
    Code_101("101", "必填参数不能为空"),
    Code_102("102", "API key不存在"),
    Code_103("103", "API已禁止使用"),
    Code_104("104", "权限已关闭"),
    Code_105("105", "权限不足"),
    Code_106("106", "签名不匹配"),

    /*---------------业务错误----------------*/
    Code_201("201", "虚拟币不存在"),
    Code_202("202", "虚拟币不能充值"),
    Code_203("203", "虚拟币还没分配到钱包地址"),
    Code_204("204", "取消挂单失败（部分成交或全部已成交）"),


    /*---------------交易错误----------------*/
    Code_1("-1", "非法操作"),
    Code_2("-2", "该交易对已被禁用"),
    Code_3("-3", "该交易对已暂停交易"),
    Code_4("-4", "不在交易时间内"),
    Code_5("-5", "交易数量不能小于"),
    Code_6("-6", "交易数量不能大于"),
    Code_7("-7", "交易总金额不能小于"),
    Code_8("-8", "交易总金额不能大于"),
    Code_9("-9", "交易价格不能小于"),
    Code_10("-10", "交易价格不能大于"),
    Code_11("-11", "余额不足"),
    /*---------------非合理错误---------------*/
    Code_401("401", "非法参数"),
    Code_402("402", "系统异常");

    private String code;
    private String message;

    APIResultCode(String code, String message) {
        this.code = code;
        this.message = message;
    }

    public String getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }

    public static APIResultCode get(String code){
        for(APIResultCode obj : values()){
            if(code.equals(obj.getCode())){
                return obj;
            }
        }

        return null;
    }

    @Override
    public String toString() {
        return this.getCode();
    }

    public static void main(String[] args) {
        System.out.println(APIResultCode.Code_101);
    }
}

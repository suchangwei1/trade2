package com.trade.util;

import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.util.regex.Matcher;

/**
 * Created by h on 2016/6/3.
 */
public class FormatUtils {
    // 保留8位小数
    private final static String COIN_ZERO_PATTERN = "0.00000000";
    // 8位小数，去除末尾0
    private final static String COIN_PATTERN = "0.########";

    /**
     * 人民币数字格式化 两位小数 不足补0 -0.02至0.01显示0
     *
     * @param num
     * @return
     */
    public static String formatCNY(Double num){
        if(null == num){
            return "0";
        }

        if(num > -0.02 && num < 0.01){
            return "0";
        }

        DecimalFormat format = new DecimalFormat("0.00");
        format.setRoundingMode(RoundingMode.DOWN);
        return format.format(num);
    }

    /**
     * 虚拟币数字格式化 四位小数 不足补0 -0.0002至0.0001显示0
     *
     * @param num
     * @return
     */
    public static String formatCoin(Double num, String pattern){
        if(null == num){
            return "0";
        }

        if(num > -0.00000002 && num < 0.00000001){
            return "0";
        }

        DecimalFormat format = new DecimalFormat(pattern);
        format.setRoundingMode(RoundingMode.DOWN);
        return format.format(num);
    }

    public static String formatCoin(Double num){
        return formatCoin(num, COIN_ZERO_PATTERN);
    }

    public static String formatBalance(Double num){
        return formatCoin(num, "0.0000");
    }

    public static String defaultFormatCoin(Double num){
        return formatCoin(num, COIN_PATTERN);
    }

    public static String formatCNYUnit(Double amount){
        if(null == amount || 0 == amount){
            return "0";
        }

        DecimalFormat format = new DecimalFormat("0.00");

        if(amount >= 10000 * 10000){
            return format.format(amount / (10000 * 10000)) + "亿";
        }else if(amount >= 10000){
            return format.format(amount / 10000) + "万";
        }else{
            return format.format(amount);
        }
    }

    public static String formatCoinUnit(Double amount){
        if(null == amount || 0 == amount){
            return "0";
        }

        if(amount >= 10000 * 10000){
            DecimalFormat format = new DecimalFormat("0.00");
            return format.format(amount / (10000 * 10000)) + "亿";
        }else if(amount >= 10000){
            DecimalFormat format = new DecimalFormat("0.00");
            return format.format(amount / 10000) + "万";
        }else{
            DecimalFormat format = new DecimalFormat("0.0000");
            return format.format(amount);
        }
    }
}

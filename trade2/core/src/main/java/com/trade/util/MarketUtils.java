package com.trade.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public abstract class MarketUtils {

    /**
     * 支持多条规则
     * 每行一个规则
     * 是否开启交易
     * @param value
     * @return
     */
    public static boolean openTrade(String value) {
        SimpleDateFormat df = new SimpleDateFormat("HH:mm");
        boolean isOpenTrade = false;
        try {
            String[] times = value.split("\n");
            for (String t : times) {
                //Date类befor和after方法判断方式是大于或者小于，所以在本来区间上向左向右各增加1秒
                Date beforeDate = df.parse(t.trim().split("-")[0]);
                beforeDate.setTime(beforeDate.getTime() - 1000);
                Date afterDate = df.parse(t.trim().split("-")[1]);
                afterDate.setTime(afterDate.getTime() + 1000);
                Date time = df.parse(df.format(new Date()));
                if ( time.after(beforeDate) && time.before(afterDate)) {
                    isOpenTrade = true;
                    break;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("输入的时间区间不能进行格式化，时间格式为：HH:mm-HH:mm");
        }
        //抛出异常，返回false
        return isOpenTrade;
    }

}

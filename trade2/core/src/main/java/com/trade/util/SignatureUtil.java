package com.trade.util;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Map;

public class SignatureUtil {


    public static String getSign(Map<String, Object> map) {
        String msg = "";
        try {
            for (String value : map.keySet())
                msg += value + "=" + URLEncoder.encode(map.get(value).toString(), "UTF-8") + "&";
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        msg = msg.substring(0, msg.lastIndexOf("&"));
        return HashUtil.SHA1(msg);
    }

    public static String getSign(String msg) {
        return HashUtil.SHA1(msg);
    }
}

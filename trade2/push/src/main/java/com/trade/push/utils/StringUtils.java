package com.trade.push.utils;

public abstract class StringUtils {

    public static boolean isEmpty(Object str) {
        return (str == null || "".equals(str));
    }

    public static boolean hasLength(CharSequence str) {
        return (str != null && str.length() > 0);
    }

    public static boolean hasText(CharSequence str) {
        if (!hasLength(str)) {
            return false;
        }
        int strLen = str.length();
        for (int i = 0; i < strLen; i++) {
            if (!Character.isWhitespace(str.charAt(i))) {
                return true;
            }
        }
        return false;
    }

    public static boolean hasText(String str) {
        return hasText((CharSequence) str);
    }

}

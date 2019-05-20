package com.trade.util;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class HTMLSpirit {
    
    private static final String regEx_script = "<script[^>]*?>[\\s\\S]*?<\\/script>"; // 定义script的正则表达式
    private static final String regEx_style = "<style[^>]*?>[\\s\\S]*?<\\/style>"; // 定义style的正则表达式
    private static final String regEx_html = "<[^>]+>"; // 定义HTML标签的正则表达式
    
    public static String delHTMLTag(String s) {
        if(s == null){
            return "";
        }
        Pattern p_script = Pattern.compile(regEx_script, Pattern.CASE_INSENSITIVE);
        Matcher m_script = p_script.matcher(s);
        s = m_script.replaceAll(""); // 过滤script标签

        Pattern p_style = Pattern.compile(regEx_style, Pattern.CASE_INSENSITIVE);
        Matcher m_style = p_style.matcher(s);
        s = m_style.replaceAll(""); // 过滤style标签

        Pattern p_html = Pattern.compile(regEx_html, Pattern.CASE_INSENSITIVE);
        Matcher m_html = p_html.matcher(s);
        s = m_html.replaceAll(""); // 过滤html标签
        s=s.replaceAll("\\&nbsp;","");
        s=s.replaceAll("\\&amp;","\\&");
        s=s.replaceAll("\\&lt;","\\<");
        s=s.replaceAll("\\&gt;","\\>");
        s=s.replaceAll("\\&quot;","");
        s=s.replaceAll("\\&#39;","");
        s=s.replaceAll("\\&#47;","\\/");

        return s.trim(); // 返回文本字符串
    }
    
}
package com.trade.util;

import org.apache.commons.codec.binary.Base64;
import org.springframework.util.ObjectUtils;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.Map;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;

public class StringUtils extends org.springframework.util.StringUtils {
	public static String activityTimeFotmat(String time){
		String[] times = time.split(" - ");
		String time1=DateUtils.formatDate(DateUtils.formatDate(times[0],"yyyy-MM-dd HH:mm:ss"),"yyyy.MM.dd");
		String time2=DateUtils.formatDate(DateUtils.formatDate(times[1],"yyyy-MM-dd HH:mm:ss"),"yyyy.MM.dd");
		String activityTimeFormat=time1+"-"+time2;
		return activityTimeFormat;
	}
	/**
	 * @description 百分比转换 如:String[] s=new String[]{"#.#####","#.####"};String str=getPercent(1,3,s);
	 * @方法名 getPercent
	 * @param num
	 * @param totalCount
	 * @return String
	 * @exception
	 */
	public static String getPercent(int num,float totalCount,String format){//动态参数
		return StringUtils.doubleToString((num/totalCount)*100,format)+"%";
	}

	/*public static void main(String[] args) {
		System.out.println(getPercent(1,100,"#"));
	}*/
	/**
	 *
	 * @description 将小数格式化成字符串，会进行四舍五入  如：doubleToString(123123.3232,"#.###")
	 * @方法名 doubleToString
	 * @param dou
	 * @param format
	 * @return String
	 * @exception
	 */

	public static String doubleToString(double dou,String format) {
		if(isEmpty(format))format = "#.##";
		DecimalFormat decimalFormat = new DecimalFormat(format);
		String string = decimalFormat.format(dou);// 四舍五入，逢五进一
		return string;
	}
	public static String hide_tel(String tel){
		int pos=tel.indexOf("@");
		if(pos!=-1){
			String str1=tel.substring(0,3);
			String str2=tel.substring(pos);
			return str1+"***"+str2;

		}else if(tel.length()==11){
			String str1=tel.substring(0,3);
			String str2=tel.substring(7,11);
			return str1+"****"+str2;
		}else {
			String str1 = tel.substring(0, 3);
			return str1 + "***";
		}
	}

	/*public static void main(String[] args) {
		System.out.println(hide_tel("112345678910"));
	}*/
	/*比特风云业务标签  投票数 2016/06/23*/
	public static Map<String, String> voteNum(String num, String format) {
		int length=num.length();
		Map<String,String> map = new HashMap();
		String[] colors=new String[8];
		String pos="";
		if(length<8) {
			for (int i = length; i < 8; i++) {
				pos += "0";
			}
			for(int i=0;i<8-length;i++){
				colors[i]="0";
			}
		}
		String newStr=pos+num;
		String result="0";
		switch (format){
			case "1":result=newStr.substring(7,8);break;
			case "2":result=newStr.substring(6,7);break;
			case "3":result=newStr.substring(5,6);break;
			case "4":result=newStr.substring(4,5);break;
			case "5":result=newStr.substring(3,4);break;
			case "6":result=newStr.substring(2,3);break;
			case "7":result=newStr.substring(1,2);break;
			case "8":result=newStr.substring(0,1);break;
		}
		int formatNum=Integer.parseInt(format);
		for(int i=0;i<8;i++){
			if(colors[i]==null){
				colors[i]="1";
			}
		};
		/*for(int i=0;i<8;i++){
			System.out.print(colors[i]);
		};
		System.out.println("---------------");*/
		map.put("color",colors[8-formatNum]);
		map.put("result",result);
		return map;
	}

	/*public static void main(String[] args) {
		System.out.println(voteNum("2345","8"));
	}*/
	/**
	 * 截断
	 * @param dou
	 * @param format
	 * @return
	 */
	public static String doubleToString(Double dou, String format) {
		DecimalFormat decimalFormat = new DecimalFormat(format);
		decimalFormat.setRoundingMode(RoundingMode.FLOOR);
		String string = decimalFormat.format(dou);// 四舍五入，逢五进一
		return string;
	}
	
	/**
	 * null转空字符
	 * @param str
	 * @return
	 */
	public static String null2EmptyString(String str){
		return hasText(str) ? str : "";
	}

	public static String null2EmptyString(Object obj){
		if(obj instanceof  String){
			return null2EmptyString((String)obj);
		}
		if(null == obj){
			return "";
		}
		return obj.toString();
	}

	public static boolean isMobile(String str){
		if(!hasText(str)) return false;
		return str.matches("^[0-9]+$");
	}
	
	public static boolean isEmail(String str){
		if(!hasText(str)) return false;
		return str.matches("^[a-z0-9]+([._\\-]*[a-z0-9])*@([a-z0-9]+[-a-z0-9]*[a-z0-9]+.){1,63}[a-z0-9]+$");
	}

	public static boolean isNumber(String str){
		if(isEmpty(str)) return false;
		return str.matches("^[0-9]+$");
	}
	
	public static boolean isEmpty(String str){
		return !hasText(str);
	}

	/**
	 * base64加密
	 *
	 * @param str
	 * @return
     */
	public static String encodeBase64(String str){
		if(isEmpty(str)){
			return str;
		}
		return new String(Base64.encodeBase64(str.getBytes()));
	}

	public static String decodeBase64(String str){
		if(isEmpty(str)){
			return str;
		}
		return new String(Base64.decodeBase64(str.getBytes()));
	}

	/**
	 * 十进制字节数组转十六进制字符串
	 *
	 * @param b
	 * @return
	 */
	public static final String byte2hex(byte[] b) { // 一个字节数，转成16进制字符串
		StringBuilder hs = new StringBuilder(b.length * 2);
		String stmp = "";
		for (int n = 0; n < b.length; n++) {
			// 整数转成十六进制表示
			stmp = Integer.toHexString(b[n] & 0XFF);
			if (stmp.length() == 1)
				hs.append("0").append(stmp);
			else
				hs.append(stmp);
		}
		return hs.toString(); // 转成大写
	}

	/**
	 * 十六进制字符串转十进制字节数组
	 *
	 * @param hs
	 * @return
	 */
	public static final byte[] hex2byte(String hs) {
		byte[] b = new byte[0];
		try {
			b = hs.getBytes("UTF-8");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		if ((b.length % 2) != 0)
			throw new IllegalArgumentException("长度不是偶数");
		byte[] b2 = new byte[b.length / 2];
		for (int n = 0; n < b.length; n += 2) {
			String item = new String(b, n, 2);
			// 两位一组，表示一个字节,把这样表示的16进制字符串，还原成一个十进制字节
			b2[n / 2] = (byte) Integer.parseInt(item, 16);
		}
		return b2;
	}

	// 压缩
	public static String compress(String str) throws IOException {
		if (str == null || str.length() == 0) {
			return str;
		}
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		GZIPOutputStream gzip = new GZIPOutputStream(out);
		gzip.write(str.getBytes());
		gzip.close();
		return out.toString("UTF-8");
	}

	// 解压缩
	public static String uncompress(String str) throws IOException {
		if (str == null || str.length() == 0) {
			return str;
		}
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		ByteArrayInputStream in = new ByteArrayInputStream(str
				.getBytes("UTF-8"));
		GZIPInputStream gunzip = new GZIPInputStream(in);
		byte[] buffer = new byte[256];
		int n;
		while ((n = gunzip.read(buffer)) >= 0) {
			out.write(buffer, 0, n);
		}
		// toString()使用平台默认编码，也可以显式的指定如toString()
		return out.toString();
	}

	public static byte[] string2UTF8Bytes(Object ... args){
		StringBuilder strBuf = new StringBuilder();
		for(Object arg : args){
			if(null != arg){
				strBuf.append(String.valueOf(arg));
			}
		}

		try {
			return strBuf.toString().getBytes(Constants.UTF8_CHARSET);
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}

		return new byte[0];
	}

	public static String bytes2UTF8String(byte[] bytes){
		if(ObjectUtils.isEmpty(bytes)){
			return null;
		}

		try {
			return new String(bytes, Constants.UTF8_CHARSET);
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}

		return null;
	}

	/**
	 * 隐藏邮箱
	 *
	 * @param email
	 * @return
	 */
	public static String safeEmail(String email) {
		if (!isEmail(email)) {
			return null;
		}

		String[] args = email.split("@");
		StringBuilder emailBuf = new StringBuilder();
		int len = args[0].length();
		if (len < 4) {
			return emailBuf.append(args[0].charAt(0)).append("****").append("@").append(args[1]).toString();
		}
		return emailBuf.append(args[0].charAt(0)).append(args[0].charAt(1)).append("****")
				.append(args[0].charAt(len - 2)).append(args[0].charAt(len - 1)).append("@").append(args[1]).toString();
	}

	/**
	 * 隐藏card id
	 *
	 * @param cardId
	 * @return
	 */
	public static String safeCardId(String cardId) {
		if (!hasText(cardId)) {
			return null;
		}

		StringBuilder idBuf = new StringBuilder();
		if (cardId.length() < 6) {
			return idBuf.append(cardId.charAt(0)).append("******").toString();
		}
		return idBuf.append(cardId.charAt(0)).append(cardId.charAt(1)).append(cardId.charAt(2)).append("******")
				.append(cardId.charAt(cardId.length() - 3)).append(cardId.charAt(cardId.length() - 2))
				.append(cardId.charAt(cardId.length() - 1)).toString();
	}

	/**
	 * 隐藏姓名
	 *
	 * @return
	 */
	public static String safeRealname(String realname) {
		if (!hasText(realname)) {
			return null;
		}

		if (realname.length() < 5) {
			return realname.charAt(0) + "**";
		}

		return realname.substring(0, 5) + "**";
	}

	/**
	 * 敏感词汇替换
	 *
	 * @param src
	 * @param replace
	 * @return
	 */
	public static String replaceMessage(String src, String replace) {
		if (!hasText(replace)) {
			return src;
		}

		String[] strs = delimitedListToStringArray(replace, ",");
		for (String str : strs) {
			if (src.indexOf(str) > -1) {
				src = src.replaceAll(str, "*");
			}
		}
		return src;
	}

	public static void main(String[] args) {
		System.out.println(safeRealname("sde "));
	}
}







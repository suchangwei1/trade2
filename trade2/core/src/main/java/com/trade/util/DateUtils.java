package com.trade.util;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public abstract class DateUtils {

	public static String getTimeFormat(Date startTime){
		try{
			if(startTime==null){return null;}
			long startTimeMills = startTime.getTime();
			long endTimeMills = System.currentTimeMillis();
			long diff = (endTimeMills - startTimeMills)/1000;//秒
			long day_diff  = (long) Math.floor(diff/86400);//天
			StringBuffer buffer = new StringBuffer();
			if(day_diff<0){
				return "[error],时间越界...";
			}else{
				if(day_diff==0 && diff<60){
					if(diff==0)diff=1;
					buffer.append(diff+"秒前");
				}else if(day_diff==0 && diff<120){
					buffer.append("1 分钟前");
				}else if(day_diff==0 && diff<3600){
					buffer.append(Math.round(Math.floor(diff/60))+"分钟前");
				}else if(day_diff==0 && diff<7200){
					buffer.append("1小时前");
				}else if(day_diff==0 && diff<86400){
					buffer.append(Math.round(Math.floor(diff/3600))+"小时前");
				}else if(day_diff==1){
					buffer.append("1天前");
				}else if(day_diff<7){
					buffer.append(day_diff+"天前");
				}else if(day_diff <30){
					buffer.append(Math.round(Math.ceil( day_diff / 7 )) + " 星期前");
				}else if(day_diff >=30 && day_diff<=179 ){
					buffer.append(Math.round(Math.ceil( day_diff / 30 )) + "月前");
				}else if(day_diff >=180 && day_diff<365){
					buffer.append("半年前");
				}else if(day_diff>=365){
					buffer.append(Math.round(Math.ceil( day_diff /30/12))+"年前");
				}
			}
			return buffer.toString();
		}catch(Exception ex){
			return "";
		}
	}
	/*public static void main(String[] args) {
		System.out.println(getTimeFormat(formatDate("2016-05-22 11:11:11","yyyy-MM-dd HH:mm:ss")));
	}*/
	public final static String DEFAULT_FORMAT = "yyyy-MM-dd";

	public static String formatDate(Date date, String format){
		SimpleDateFormat dateFormat = new SimpleDateFormat(format);
		return dateFormat.format(date);
	}

	/*public static void main(String[] args) {
		System.out.println(formatDate(new Date(),"yyyy.MM.dd"));
	}*/

	public static String formatDate(Date date){
		return formatDate(date, DEFAULT_FORMAT);
	}
	
	public static Date formatDate(String dateStr, String format){
		SimpleDateFormat dateFormat = new SimpleDateFormat(format);
		try {
			return dateFormat.parse(dateStr);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public static Date formatDate(String dateStr){
		return formatDate(dateStr, DEFAULT_FORMAT);
	}
	
	/**
	 * 计算相差天数
	 * 
	 * @param start
	 * @param end
	 * @return
	 */
	public static int getDays(Date start, Date end){
		return (int) (((end.getTime() - start.getTime()) / (24*60*60*1000l)));
	}
	
	/**
	 * 当天开始时间
	 * 
	 * @param date
	 * @return
	 */
	public static Date getStartDate(Date date){
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		calendar.set(Calendar.HOUR_OF_DAY, 0);
		calendar.set(Calendar.MINUTE, 0);
		calendar.set(Calendar.SECOND, 0);
		return calendar.getTime();
	}

	/**
	 * 当天结束时间
	 *
	 * @param date
	 * @return
	 */
	public static Date getDateLastTime(Date date){
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		calendar.set(Calendar.HOUR_OF_DAY, 23);
		calendar.set(Calendar.MINUTE, 59);
		calendar.set(Calendar.SECOND, 59);
		calendar.set(Calendar.MILLISECOND, 999);
		return calendar.getTime();
	}

	/**
	 * 一个月前
	 * @return
	 */
	public static Date getMonthBefore(){
		Calendar calendar = Calendar.getInstance();
		calendar.add(Calendar.MONTH, -1);
		calendar.set(Calendar.HOUR_OF_DAY, 0);
		calendar.set(Calendar.MINUTE, 0);
		calendar.set(Calendar.SECOND, 0);
		return calendar.getTime();
	}
	
	/**
	 * 15天前
	 * @return
	 */
	public static Date getHalfMonthBefore(){
		return getDaysBefore(15);
	}
	
	/**
	 * 几天前
	 * @param days
	 * @return
	 */
	public static Date getDaysBefore(int days){
		Calendar calendar = Calendar.getInstance();
		calendar.add(Calendar.DAY_OF_MONTH, -days);
		calendar.set(Calendar.MINUTE, 0);
		calendar.set(Calendar.SECOND, 0);
		return calendar.getTime();
	}

	/**
	 * 几小时后
	 * @param hours
	 * @return
     */
	public static Date getHoursAfter(Date date, int hours){
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		calendar.add(Calendar.HOUR_OF_DAY, hours);
		return calendar.getTime();
	}

	/**
	 * 几秒后
	 *
	 * @param date
	 * @param seconds
     * @return
     */
	public static Date getSecondAfter(Date date, int seconds){
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		calendar.add(Calendar.SECOND, seconds);
		return calendar.getTime();
	}

	/**
	 * 判断是否是同一天
	 * @param start
	 * @param end
	 * @return
	 */
	public static boolean isSameDate(Date start, Date end){
		Calendar startCal = Calendar.getInstance();
		Calendar endCal = Calendar.getInstance();
		startCal.setTime(start);
		endCal.setTime(end);
		return startCal.get(Calendar.YEAR) == endCal.get(Calendar.YEAR) && startCal.get(Calendar.MONTH) == endCal.get(Calendar.MONTH) && startCal.get(Calendar.DAY_OF_MONTH) == endCal.get(Calendar.DAY_OF_MONTH);
	}
	
	public static void main(String[] args) {
		System.out.println(getDateLastTime(new Date()));
	}
}










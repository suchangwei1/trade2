package com.trade.util;

import com.trade.oss.AliyunService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import sun.misc.BASE64Encoder;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.Timestamp;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.UUID;
import java.util.regex.Pattern;

public class Utils {

	private static final Logger log = LoggerFactory.getLogger(Utils.class);

	public static String getFileExt(String fileName) {
		if (fileName.lastIndexOf(".") == -1)
			return "";
		int pos = fileName.lastIndexOf(".") + 1;
		return fileName.substring(pos, fileName.length());
	}

	public static String getUploadPath(String fileName) {
		int length=fileName.length();
		int pos = fileName.lastIndexOf("upload");
		if (pos == -1)
			return "";
		return fileName.substring(pos,length);
	}

	public static String getContentType(String ext){
		String type="application/octet-stream";
		switch(ext){
			case "xls":type="application/x-xls";break;
			case "doc":type="application/msword";break;
			case "png":type="image/png";break;
			case "jpeg":type="image/jpeg";break;
			case "jpg":type="image/jpeg";break;
			case "gif":type="image/gif";break;
			case "ico":type="application/x-ico";break;
			
		}
		return type;
	}
	// 获得随机字符串
	public static String randomString(int count) {
		String str = "abcdefghigklmnopkrstuvwxyzABCDEFGHIGKLMNOPQRSTUVWXYZ0123456789";
		int size = str.length();
		StringBuffer sb = new StringBuffer();
		Random random = new Random();
		while (count > 0) {
			sb.append(String.valueOf(str.charAt(random.nextInt(size))));
			count--;
		}
		return sb.toString();
	}

	public static String randomInteger(int length) {
		StringBuffer sb = new StringBuffer();
		for (int i = 0; i < length; i++) {
			sb.append(new Random().nextInt(10));
		}
		return sb.toString();
	}

	public static String getRandomImageName() {
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat(
				"yyyyMMddHHmmsss");
		String path = simpleDateFormat.format(new Date());
		path += "_" + randomString(5);
		return path;
	}

	public static boolean saveFile(String dir, String fileName,
			InputStream inputStream){
		String path=getUploadPath(dir);
		String ext=getFileExt(fileName);
		AliyunService aliyunService = SpringContextUtils.getBean(AliyunService.class);
		try {
			aliyunService.updateFile(inputStream,path+"/"+fileName, getContentType(ext));
			System.out.println("文件上传路径是"+path+"/"+fileName);
			System.out.println("文件类型是"+getContentType(ext));
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}

	public static boolean uploadFileToOss(InputStream is, String filePath){
		AliyunService aliyunService = SpringContextUtils.getBean(AliyunService.class);
		try {
			aliyunService.updateFile(is, filePath, getFileExt(filePath));
			return true;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}

	/*public static boolean saveFile(String dir, String fileName,
			InputStream inputStream) {
		boolean flag = false;
		File directory = new File(dir);

		if (!directory.exists()) {
			directory.mkdirs();
		}

		if (!directory.isDirectory()) {
			log.debug("Not a directory!");
			return flag;
		}
		if (!directory.exists()) {
			directory.mkdirs();
		}

		if (inputStream == null) {
			log.debug("InputStream null.");
			return flag;
		}

		File realFile = new File(directory, fileName);
		FileOutputStream fileOutputStream = null;
		int tmp = 0;
		try {
			fileOutputStream = new FileOutputStream(realFile);
			while ((tmp = inputStream.read()) != -1) {
				fileOutputStream.write(tmp);
			}

			if (fileOutputStream != null) {
				fileOutputStream.close();
			}

			if (inputStream != null) {
				inputStream.close();
			}

			flag = true;

		} catch (Exception e) {
			log.debug("Read InputStream fail.");
		} finally {
			fileOutputStream = null;
			inputStream = null;
		}

		return flag;
	}*/

//	public static String MD5(String content) {
////		MessageDigest md5 = MessageDigest.getInstance("MD5");
////		sun.misc.BASE64Encoder baseEncoder = new sun.misc.BASE64Encoder();
////		String retString = baseEncoder.encode(md5.digest(content.getBytes()));
//		return getMD5_32(content);
//	}

	public static String MD5(String password) {
		MessageDigest md5 = null;
		try {
			md5 = MessageDigest.getInstance("MD5");
		} catch (Exception e) {
			e.printStackTrace();
		}
		BASE64Encoder baseEncoder = new BASE64Encoder();
		String retString = baseEncoder.encode(md5.digest(password.getBytes()));
		return retString;
	}

	public static String getMD5_32(String str) {
		MessageDigest messageDigest = null;

		try {
			messageDigest = MessageDigest.getInstance("MD5");

			messageDigest.reset();

			messageDigest.update(str.getBytes("UTF-8"));
		} catch (NoSuchAlgorithmException e) {
			System.out.println("NoSuchAlgorithmException caught!");
			System.exit(-1);
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}

		byte[] byteArray = messageDigest.digest();

		StringBuffer md5StrBuff = new StringBuffer();

		for (int i = 0; i < byteArray.length; i++) {
			if (Integer.toHexString(0xFF & byteArray[i]).length() == 1)
				md5StrBuff.append("0").append(
						Integer.toHexString(0xFF & byteArray[i]));
			else
				md5StrBuff.append(Integer.toHexString(0xFF & byteArray[i]));
		}

		return md5StrBuff.toString();
	}

	public static String getCookie(Cookie[] cookies, String key) {
		String value = null;
		if (cookies != null && key != null) {
			for (Cookie cookie : cookies) {
				if (key.equals(cookie.getName())) {
					value = cookie.getValue();
				}
			}
		}

		return value;
	}

	public static Timestamp getTimestamp() {
		return new Timestamp(new Date().getTime());
	}
	
	public static Long getTimeLong(){
		return new Date().getTime();
	}

	public static int getNumPerPage() {
		return 40;
	}

	public static synchronized String UUID() {
		return UUID.randomUUID().toString();
	}

	// return seconds
	public static long timeMinus(Timestamp t1, Timestamp t2) {
		return (t1.getTime() - t2.getTime()) / 1000;
	}

	// 获得今天0点
	public static long getTimesmorning() {
		Calendar cal = Calendar.getInstance();
		cal.set(Calendar.HOUR_OF_DAY, 0);
		cal.set(Calendar.SECOND, 0);
		cal.set(Calendar.MINUTE, 0);
		cal.set(Calendar.MILLISECOND, 0);
		return cal.getTimeInMillis();
	}

	public static String getCurTimeString() {
		return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
	}

	public static String number2String(double f) {
		DecimalFormat df = new DecimalFormat();
		String style = "0.00000";// 定义要显示的数字的格式
		df.applyPattern(style);
		return df.format(f);
	}

	private static String getHeader(HttpServletRequest request, String headName) {
		String value = request.getHeader(headName);
		return !StringUtils.isEmpty(value) && !"unknown".equalsIgnoreCase(value) ? value : "";
	}

	public static String getIpAddr(HttpServletRequest request) {
		String ipFromNginx = getHeader(request, "X-Real-IP");
		if (!StringUtils.isEmpty(ipFromNginx)) {
			return ipFromNginx;
		}
		return request.getRemoteAddr();
	}

	public static double getDouble(double value,int scale){
		if(value == 0) return 0d;
		String a ="";
		for(int i=0;i<scale;i++){
			a = a+"0";
		}
		DecimalFormat s=new DecimalFormat("###."+a);
		return Double.valueOf(s.format(value));
	}
	
	public static String dateFormat(Timestamp timestamp){
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss") ;
		return sdf.format(timestamp) ;
	}
	
	public static boolean isNumeric(String str){
		if(str == null ||str.trim().length() ==0) return false;
	    Pattern pattern = Pattern.compile("[0-9]*");
	    return pattern.matcher(str).matches();   
	 }
	
	public static String getRandomString(int length) { //length表示生成字符串的长度
	    String base = "01234567890123456789012345678901234567890123456789";  
	    Random random = new Random();  
	    StringBuffer sb = new StringBuffer();  
	    for (int i = 0; i < length; i++) {  
	        int number = random.nextInt(base.length());  
	        sb.append(base.charAt(number));  
	    }  
	    return sb.toString();  
	 } 
	
	public static double guassian(double mean,double var){
		double V1,V2,S;
		int phase = 0;
		double X;
		
		S = 1;
		V2 = 0;
		if(phase == 0){
			do{
				double U1 = Math.random();
				double U2 = Math.random();
				
				V1 = 2*U1 - 1;
				V2 = 2*U2 - 1;
				S = V1*V1 + V2*V2;
			}while(S>=1||S==0);
			
			X = V1 * Math.sqrt(-2*Math.log(S)/S);
		}else{
			X = V2 * Math.sqrt(-2*Math.log(S)/S);
		}
		
		phase = 1 - phase;
		
		double ret = X*Math.sqrt(var) + mean;
		if(ret<=0)
			ret = 0.1;
		/*else if(ret>mean*2)
			ret = mean*2;*/
		return ret;
	}
	
	public static boolean isAjax(HttpServletRequest request){
		String header = request.getHeader("X-Requested-With"); 
	    return header != null && "XMLHttpRequest".equals(header);
	}

	/**
	 * 把时间戳转换成long
	 * @param str
	 * @return
     */
	public static long parseStr(String str){
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
		try {
			return format.parse(str).getTime()/1000;
		} catch (ParseException e) {
			e.printStackTrace();
			return 0;
		}
	}

	/**
	 * 判断是否是活动时间
	 * @param time
	 * @return
     */
	public static boolean isActivityTime(String time){
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
		String[] times = time.split(" - ");

		try {
			long activeBeginTime = format.parse(times[0]).getTime()/1000;
			long activeEndTime = format.parse(times[1]).getTime()/1000;
			long now = new Date().getTime()/1000;
			if(activeBeginTime <= now && activeEndTime >= now){
				return true;
			}else {
				return false;
			}
		} catch (ParseException e) {
			e.printStackTrace();
			return false;
		}
	}

	public static String convertStar(String phone){
		if(phone.length() != 11){
			return phone;
		}
		StringBuilder sb = new StringBuilder(phone.substring(0, 3));
		sb.append("****");
		sb.append(phone.substring(phone.length() - 4));
		return sb.toString();
	}

	/**
	 *从系统参数中获取奖池
	 * @param type 1，票选奖池；2，竞猜奖池；3，票选成功奖池；4，竞猜成功奖池；
	 * @return
     */
	public static int getAwardPool(int type, String awardPool){
		if(awardPool == null || "".equals(awardPool)){
			log.error("解析比特风云活动奖池失败");
			return -1;
		}
		String[] pools = awardPool.split(",");
		int result = 0;
		switch (type){
			case 1:
				result = Integer.parseInt(pools[0]);
				break;
			case 2:
				result = Integer.parseInt(pools[1]);
				break;
			case 3:
				result = Integer.parseInt(pools[2]);
				break;
			case 4:
				result = Integer.parseInt(pools[3]);
				break;
			default:
				log.error("type参数错误，except：1-4，actual：" + type);
				break;
		}
		return result;
	}

	/**
	 * 获得几天前的日期
	 * @param day
     * @return
     */
	public static Date getDateBefore(int day){
		Calendar calendar = Calendar.getInstance();
//		calendar.set(Calendar.DATE, calendar.get(Calendar.DATE) - day);
		calendar.add(Calendar.DATE, day);
		return calendar.getTime();
	}

	/**
	 * 获得几秒前的日期
	 * @param seconds
	 * @return
     */
	public static Date getBeforeSeconds(int seconds){
		Calendar calendar = Calendar.getInstance();
		calendar.add(Calendar.SECOND, -seconds);
		return calendar.getTime();
	}


	public static List deepCopy(List src){
		ByteArrayOutputStream byteOutput = new ByteArrayOutputStream();
		List dest = null;
		try {
			ObjectOutputStream out = new ObjectOutputStream(byteOutput);
			out.writeObject(src);
			ByteArrayInputStream byteIn = new ByteArrayInputStream(byteOutput.toByteArray());
			ObjectInputStream in =new ObjectInputStream(byteIn);
			dest = (List) in.readObject();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
		return dest;
	}

	public static Map<String, String> getParameterMap(HttpServletRequest request){
		Map<String, String[]> arrMap = request.getParameterMap();
		Map<String, String> paramMap = new HashMap();
		for(String key : arrMap.keySet()){
			paramMap.put(key, arrMap.get(key)[0]);
		}

		return paramMap;
	}

	private static String bytesToHexString(byte[] src) {
		StringBuilder stringBuilder = new StringBuilder();
		if (src == null || src.length <= 0) {
			return null;
		}
		for (int i = 0; i < src.length; i++) {
			int v = src[i] & 0xFF;
			String hv = Integer.toHexString(v);
			if (hv.length() < 2) {
				stringBuilder.append(0);
			}
			stringBuilder.append(hv);
		}
		return stringBuilder.toString();
	}

	public static String getRelativeFilePath(String fileExt, byte[] bytes){
		return getRelativeFilePath(fileExt, bytes, null);
	}

	public static String getRelativeFilePath(String fileExt, byte[] bytes, Object extFileName){
		StringBuilder filePath = new StringBuilder();

		filePath.append("/").append(DateUtils.formatDate(new Date(), "yyyyMM")).append("/");
		if(bytes.length > 40){
			byte[] copyArr = new byte[10];
			System.arraycopy(bytes, 30, copyArr, 0, 10);
			String hexStr = bytesToHexString(copyArr);
			filePath.append(hexStr).append(randomString(5));
		}else{
			filePath.append(getRandomImageName());
		}
		if(null != extFileName){
			// 标识参数
			filePath.append("_").append(extFileName);
		}
		filePath.append(".").append(fileExt);
		return filePath.toString();
	}

	public static boolean isImage(byte[] bytes){
		if(0 == bytes.length){
			return false;
		}

		byte[] copyArr = new byte[28];
		System.arraycopy(bytes, 0, copyArr, 0, 28);
		String hexStr = bytesToHexString(copyArr).toUpperCase();

		// jpeg|png|gif
		return hexStr.startsWith("FFD8FF") || hexStr.startsWith("89504E47") || hexStr.startsWith("47494638");
	}

	/**
	 * service断言
	 *
	 * @param expression
	 * @param message
	 */
	public static void assertTrueForService(boolean expression, String message){
		if(!expression){
			throw new RuntimeException(message);
		}
	}

	public static <K, V> HashMap<K, V> newHashMap(int size) {
		if (size < 3) {
			return new HashMap<>(size + 1);
		}
		return new HashMap<>((int) (size / 0.75F + 1.0F));
	}

	public static void main(String[] args) {

		System.out.println(getDouble(-51.08809, 4));
	}

}

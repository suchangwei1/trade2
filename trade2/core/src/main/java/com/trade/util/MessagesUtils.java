package com.trade.util;

import java.util.HashMap;
import java.util.Map;

public class MessagesUtils {
	
	/*
	 * 100			发送成功
		101			验证失败
		102			手机号码格式不正确
		103			会员级别不够
		104			内容未审核
		105			内容过多
		106			账户余额不足
		107			Ip受限
		108			手机号码发送太频繁
		120			系统升级
	 * 
	 * */
    public static int send(String name,String password,String phone,String content){
		return _send(name, password, phone, content);
//    	int retCode = ReturnCode.FAIL ;
//    	try {
//
//    		String u = "http://gbk.sms.webchinese.cn/?Uid="+name.trim()+"&Key="+password.trim()+"&smsMob="+phone.trim()+"&smsText="+content;
//    		URL url = new URL(u) ;
//			System.out.println(url.toString());
//    		BufferedReader br = new BufferedReader(new InputStreamReader( url.openStream()) ) ;
//			StringBuffer sb = new StringBuffer() ;
//			String tmp = null ;
//			while((tmp=br.readLine())!=null){
//				sb.append(tmp) ;
//			}
//			if(sb.toString().indexOf("100")!=-1){
//				retCode = ReturnCode.SUCCESS ;
//			}
//			System.out.println(sb.toString());
//			br.close() ;
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
//    	return retCode ;
    }

	public static int _send(String name,String password,String phone,String content){
		int retCode = ReturnCode.FAIL ;
		try {
			/*
			 * 因为短信接口是gbk编码
			 * 需要在在提交的时候加上Content-Type： charset=gbk
			 * 否则在linux环境会导致乱码
			 */
//			HttpClient client = new HttpClient();
//			PostMethod post = new PostMethod("http://gbk.sms.webchinese.cn");
//			post.addRequestHeader("Content-Type","application/x-www-form-urlencoded;charset=gbk");//在头文件中设置转码
//			NameValuePair[] data ={ new NameValuePair("Uid", name.trim()),new NameValuePair("Key", password.trim()),new NameValuePair("smsMob",phone.trim()),new NameValuePair("smsText", content)};
//			post.setRequestBody(data);
//
//			client.executeMethod(post);
//			String result = new String(post.getResponseBodyAsString().getBytes("gbk"));

			Map<String, String> map = new HashMap<>();
			map.put("Uid", name.trim());
			map.put("Key", password.trim());
			map.put("smsMob", phone.trim());
			map.put("smsText", content.trim());
			String result = HttpClientUtils.post("http://gbk.sms.webchinese.cn", map, "gbk");

			//System.out.println(post.getResponseBodyAsString());
			//System.out.println(result.toString());
			//System.out.println("发送短信：" + phone + " : " + content);

			if(result.toString().indexOf("100")!=-1){
				retCode = ReturnCode.SUCCESS ;
			}

//			post.releaseConnection();

		} catch (Exception e) {
			e.printStackTrace();
		}
		return retCode ;
	}

    public static void main(String args[]) throws Exception{
    	System.out.println(MessagesUtils.send("zhonggutrade", "4b8c96f4219d16ac04b9", "18682029734", "您的验证码是："+233333+"。请不要把验证码泄露给其他人。如非本人操作，可不用理会！"));
    }
}

package com.trade.util;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Properties;

import javax.mail.Message;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import javax.mail.internet.MimeUtility;

public class SendMailUtil {
	public static boolean send(String smtp, String name, String password,
			String toAddress, String title, String content) {
		boolean flag = false ;
		try {
			Properties props = new Properties();
			props.setProperty("mail.transport.protocol", "smtp");
			props.setProperty("mail.smtp.auth", "true");
			props.setProperty("mail.smtp.localhost", "localHostAdress");
			Session session = Session.getInstance(props);
			session.setDebug(true);
			Transport transport = session.getTransport();
			transport.connect(smtp.trim(), name.trim(), password.trim());

			// 设置邮件内容
			MimeMultipart contents = new MimeMultipart();
			
			Message message = new MimeMessage(session);
			message.setSentDate(new Date());
			 String nick="";  
		        try {  
		            nick=javax.mail.internet.MimeUtility.encodeText("网");
		        } catch (UnsupportedEncodingException e) {  
		            e.printStackTrace();  
		        }   
	        message.setFrom(new InternetAddress(nick+" <"+name+">")); 
	        message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(toAddress));  
			message.setSubject(MimeUtility.encodeText(title, "gb2312", "b"));

			//正文HTML
			MimeBodyPart body = new MimeBodyPart() ;
			body.setContent(content, "text/html;charset=gb2312") ;
			contents.addBodyPart(body) ;
			
			message.setContent(contents);

			
			List<String> emails = new ArrayList<String>() ;
			emails.add(toAddress) ;
			transport.sendMessage(message, InternetAddress.parse(emails.toString().replace("[", "").replace("]", ""))) ;
			transport.close();
			
			flag = true ;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return flag ;
	}
	
	public static void main(String[] args) {
		//"mx7.dns.com.cn"
		send("smtp.mxhichina.com", "service@xiangmay.com", "ABCqwer1123", "1456019685@qq.com", "test", "test...");
	} 
}

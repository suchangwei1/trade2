package com.trade.filters;

import com.trade.model.Fuser;
import com.trade.util.Constants;

import javax.servlet.*;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
/**
 * 请求重定向
 * **/
public class UrlRedirectFilter implements Filter {
	public void init(FilterConfig arg0) throws ServletException {}
	public void destroy() {}

	public String getIpAddr(HttpServletRequest request) {      
        String ip = request.getHeader("x-forwarded-for");      
       if(ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {      
           ip = request.getHeader("Proxy-Client-IP");      
       }      
       if(ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {      
           ip = request.getHeader("WL-Proxy-Client-IP");      
        }      
      if(ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {      
            ip = request.getRemoteAddr();      
       }      
      return ip;      
	}
	
	public void doFilter(ServletRequest request, ServletResponse response,
			FilterChain chain) throws IOException, ServletException {
		HttpServletRequest req = (HttpServletRequest) request;
		HttpServletResponse resp = (HttpServletResponse) response ;
		
		String uri = req.getRequestURI().toLowerCase().trim() ;
		
		/*if(!uri.endsWith(".js")&&!uri.endsWith(".png")&&!uri.endsWith(".css"))
		if(uri.startsWith("/appapi.html")==true||uri.startsWith("/kline/start.html")==true||uri.startsWith("/kline/period.html")==true
				||uri.startsWith("/kline/depth.html")==true||uri.startsWith("/kline/trades.html")==true){
			chain.doFilter(request, response) ;
			return ;
		}else{
			return ;
		}*/

		Cookie[] cookies = req.getCookies() ;
		boolean cookieFlag = false ;
		if(cookies!=null){
			for (Cookie cookie : cookies) {
				if(cookie.getName().equals("open") ){
					cookieFlag = true ;
				}
			}
		}
		if(!cookieFlag){
			resp.addCookie(new Cookie("open", "true")) ;
		}
		
		//不接受任何jsp请求
		if(uri.endsWith(".jsp")){
			return ;
		}
		
		//只拦截.html结尾的请求
		if(!uri.endsWith(".html")){
			chain.doFilter(request, response) ;
			return ;
		}
		
		
		///////////////////////////////////////////////////////////////////////////////
		if(	uri.startsWith("/index.html")//首页
				||uri.startsWith("/appapi.html")//app apiiiiii
				||uri.startsWith("/qqLogin")//qq
				||uri.startsWith("/user/afterqqlogin")//qq
				||uri.startsWith("/user/qq_login_register")//qq绑定账号或创建
				||uri.startsWith("/user/qqloginregister")//qq绑定账号或创建
				||uri.startsWith("/user/qqloginbind")//qq绑定账号或创建
				||uri.startsWith("/error/")//error
				||uri.startsWith("/api/")//api
				||uri.startsWith("/data/ticker.html")
				||uri.startsWith("/line/period-btcdefault.html")
				||uri.startsWith("/data/depth-btcdefault.html")
				||uri.startsWith("/data/stock-btcdefault.html")
				||uri.startsWith("/appapi.html")//app apiiiiii
				||uri.startsWith("/index_chart.html")
			||uri.startsWith("/user/login")//登陆
			||uri.startsWith("/user/logout")//退出
			||uri.startsWith("/user/reg")//注册
			||uri.startsWith("/app/reg.html")//注册
			||uri.startsWith("/app/suc.html")//注册
			||uri.startsWith("/real/")//行情
			||uri.startsWith("/market")//行情
			||uri.startsWith("/kline/")//行情
			||uri.startsWith("/json.html")//行情
			||uri.startsWith("/json/")//行情
			||uri.startsWith("/validate/")//邮件激活,找回密码
			||uri.startsWith("/about/")//文章管理
			||uri.startsWith("/service/")//文章管理
			||uri.startsWith("/download/")//文章管理
			){
			chain.doFilter(request, response) ;
			return ;
		}else{
			
			if(uri.startsWith("/ssadmin/")){
				
				//限制管理端访问IP
				String[] allowIps = Constants.AdminAllowIp.trim().split(",");
				if( !Constants.AdminAllowIp.trim().equals("") && allowIps.length>0 ){
					String accessIp = getIpAddr(req);
					boolean isExist = false;
					for( int i=0; i<allowIps.length; i++ ){
						if( allowIps[i].equals(accessIp) ){
							isExist = true;
							break;
						}
					}
					if( isExist == false ){
						resp.sendRedirect("/") ;
						return ;
					}
				}
				
				//后台
				if(uri.startsWith("/ssadmin/plj32_dsjds23.html")
					||uri.startsWith("/ssadmin/submitlogin__zhg.html") 
					||uri.startsWith("/ssadmin/sendloginsmscode.html")
					||uri.startsWith("/ssadmin/smslogin.html")
					|| req.getSession().getAttribute("login_admin")!=null){
					chain.doFilter(request, response) ;
				}else if( uri.startsWith(Constants.AdminUrl) ){//过滤后台登录域名
					resp.sendRedirect("/ssadmin/plj32_dsjds23.html") ;
				}else{
					resp.sendRedirect("/") ;
				}
				return ;
			}else{
				
				Object login_user = req.getSession().getAttribute("login_user") ;
				if(login_user==null){
					resp.sendRedirect("/?forwardUrl="+req.getRequestURI().trim()) ;
					return ;
				}else{
					if( ((Fuser)login_user).getFpostRealValidate() ){
						//提交身份认证信息了
						chain.doFilter(request, response) ;
						return ;
					}else{
						if(uri.startsWith("/user/userinfo.html")
							||uri.startsWith("/user/validateidentity.html")
							){
							chain.doFilter(request, response) ;
						}else{
							resp.sendRedirect("/user/userinfo.html") ;
						}
						return ;
					}
				}
				
			}
			
			
		}
		
	}


}

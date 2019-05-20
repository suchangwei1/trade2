<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ include file="comm/include.inc.jsp"%>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path;
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>管理系統</title>
<link href="/static/ssadmin/js/themes/css/login.css" rel="stylesheet" type="text/css" />
<script type="text/javascript">
<c:if test="${error != null}">
alert("${error}") ;
</c:if>
</script>
</head>
<body>
<script type="text/javascript">
    function validate(f){
        f.src = "/servlet/ImageCode?"+Math.random() ;
    }
</script>
	<div id="login" class="main">
		<img src="/static/ssadmin/img/bg.png" alt="">
		 <div class="login_wrapper">
			 <div class="title">
				 <img src="/static/ssadmin/img/logo.png" alt="" />
				 <span>后台登录系统</span>
			 </div>
			 <div class="content">
				 <form action="/ssadmin/smsLogin.html" method="post">
				 <p>
					 <i class="iconfont">&#xe60d;</i>
					 <input id="mobile"  type="text" name="mobile" placeholder="手机号码" />
				 </p>
				 <p>
					 <i class="iconfont" style="font-size:20px;">&#xe7b8;</i>
					 <input type="text" name="password" id="password" placeholder="密码" />
				 </p>
				 <div class="clear">
					 <p class="code_input">
						 <i class="iconfont">&#xe696;</i>
						 <input type="text" class="code" name="code" placeholder="验证码" />
						 <a href="javascript:void(0)" onclick="sendSmsCode();" id="sendBtn">发送</a>
					 </p>
				 </div>
				 <a href="javascript:void(0)" class="confirm">登录</a>
				 </form>
			 </div>
		 </div>
	</div>
	<div class="copyright">
		粤ICP备11017824号-4   粤ICP证130164号   粤公网安备11010502000501号   ©金旭升科技有限公司
	</div>
<script src="/static/ssadmin/js/js/jquery-1.7.2.min.js"></script>
<script>
	$("#password").focus(function(){
		$(this).get(0).setAttribute("type","password");
	});
</script>
<script src="/static/ssadmin/js/js/jquery.cookie.js"></script>
<script src="/static/ssadmin/js/verify.code.js"></script>
<script>
	$(".confirm").click(function(){
	    $("form").submit();
	})
	function sendSmsCode(){
		var mobile = document.getElementById('mobile').value;
		if(!mobile){
			alert('请输入手机号！');
			return;
		}
		var id = 'sendBtn';
		var $send_btn = $('#' + id);
		var flag = $send_btn.data(_const.SUBMIT_FLAG);
		if(false == flag){
			return;
		}
		$send_btn.data(_const.SUBMIT_FLAG, false);
		$.post('/ssadmin/sendLoginSmsCode.html', {mobile : mobile}, function(data) {
			if('200' == data){
				_handler.addTipsHandler(_const.SMS_CAPTCHA_KEY, id);
				_handler.updateTipsSeconds(id, _const.CAPTCHA_SECONDS);
			}else if('101' == data){
				alert('请输入正确的手机号！');
				$send_btn.data(_const.SUBMIT_FLAG, true);
			}else if('102' == data){
				alert('手机号不存在！');
				$send_btn.data(_const.SUBMIT_FLAG, true);
			}else{
				alert('发送验证码失败！');
				$send_btn.data(_const.SUBMIT_FLAG, true);
			}
		});
	}
</script>
</body>
</html>

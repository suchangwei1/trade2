<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="comm/include.inc.jsp"%>
<h2 class="contentTitle">修改管理员：${login_admin.fname }手机号</h2>


<div class="pageContent">
	
	<form method="post" action="ssadmin/updateAdminMobile.html" class="pageForm required-validate"
	 onsubmit="return validateCallback(this,dialogAjaxDone)">
		<input type="hidden" name="fadmin.fid" value="${null == fadmin ? login_admin.fid : fadmin.fid}" />
		<div class="pageFormContent nowrap" layoutH="97">
			<dl>
				<dt>手机号：</dt>
				<dd>
					<input name="fadmin.mobile" type="tel" maxlength="20" class="required" size="60"/>
				</dd>
			</dl>
		</div>
		<div class="formBar">
			<ul>
				<li><div class="buttonActive"><div class="buttonContent"><button type="submit">修改</button></div></div></li>
				<li><div class="button"><div class="buttonContent"><button type="button" class="close">取消</button></div></div></li>
			</ul>
		</div>
	</form>
	
</div>


<script type="text/javascript">
function customvalidXxx(element){
	if ($(element).val() == "xxx") return false;
	return true;
}
</script>

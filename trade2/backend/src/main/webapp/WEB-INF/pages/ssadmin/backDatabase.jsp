<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="comm/include.inc.jsp"%>
<h2 class="contentTitle">备份数据库</h2>


<div class="pageContent">
	
	<form method="post" action="ssadmin/backDatabase.html" class="pageForm required-validate" onsubmit="return validateCallback(this,dialogAjaxDone)">
		<div class="pageFormContent nowrap" layoutH="97">
			<dl>
				<dt>IP地址：</dt>
				<dd>
					<input type="text" name="fip" maxlength="20" class="required" size="70"/>
				</dd>
			</dl>
			<dl>
				<dt>端口号：</dt>
				<dd>
					<input type="text" name="fport" maxlength="20" class="required" size="40"/>
				</dd>
			</dl>
			<dl>
				<dt>数据库名：</dt>
				<dd>
					<input type="text" name="dataBase" maxlength="100" class="required" size="70"/>
				</dd>
			</dl>
			<dl>
				<dt>用户名：</dt>
				<dd>
					<input type="text" name="fname" maxlength="100" class="required" size="70"/>
				</dd>
			</dl>
			<dl>
				<dt>密码：</dt>
				<dd>
					<input type="password" name="fpassword" maxlength="100" class="required" size="70"/>
				</dd>
			</dl>
		</div>
		<div class="formBar">
			<ul>
				<li><div class="buttonActive"><div class="buttonContent"><button type="submit">备份</button></div></div></li>
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

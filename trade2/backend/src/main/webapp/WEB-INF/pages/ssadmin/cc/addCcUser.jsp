<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="../comm/include.inc.jsp"%>
<h2 class="contentTitle">添加商戶</h2>

<div class="pageContent">
	
	<form method="post" action="ssadmin/ccUserSave.html" class="pageForm required-validate" onsubmit="return validateCallback(this,dialogAjaxDone)">
		<div class="pageFormContent nowrap" layoutH="97">
			<dl>
				<dt>名称：</dt>
				<dd>
					<input type="text" name="user.name" maxlength="20" class="required" size="70"/>
				</dd>
			</dl>
			<dl>
				<dt>联系方式：</dt>
				<dd>
					<input type="text" name="user.contactWay" maxlength="50" class="required" size="70"/>
				</dd>
			</dl>
			<dl>
				<dt>银行账号：</dt>
				<dd>
					<input type="text" name="user.account" maxlength="50" class="required" size="70"/>
				</dd>
			</dl>
			<dl>
				<dt>支行：</dt>
				<dd>
					<input type="text" name="user.branch" maxlength="50" class="required" size="70"/>
				</dd>
			</dl>
		</div>
		<div class="formBar">
			<ul>
				<li><div class="buttonActive"><div class="buttonContent"><button type="submit">保存</button></div></div></li>
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

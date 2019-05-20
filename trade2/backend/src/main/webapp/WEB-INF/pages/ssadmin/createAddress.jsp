<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="comm/include.inc.jsp"%>
<h2 class="contentTitle">生成:<font color="red">${virtualCoinType.fname}</font> 地址</h2>


<div class="pageContent">
	
	<form method="post" action="ssadmin/createWalletAddress.html" class="pageForm required-validate" onsubmit="return validateCallback(this,dialogAjaxDone)">
		<div class="pageFormContent nowrap" layoutH="97">
			<dl>
				<dt>钱包密码：</dt>
				<dd>
				    <input type="hidden" name="uid" value="${virtualCoinType.fid}"/>
				    <input type="password" name="passWord" size="50" class=""/>
				</dd>
			</dl>
			<dl>
				<dt>生成地址数量：</dt>
				<dd>
					<input type="text" name="count" size="50" value="10000" class=""/>
				</dd>
			</dl>
		</div>
		<div class="formBar">
			<ul>
				<li><div class="buttonActive"><div class="buttonContent"><button type="submit">生成地址</button></div></div></li>
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

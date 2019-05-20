<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="comm/include.inc.jsp"%>
<h2 class="contentTitle">修改奖励</h2>


<div class="pageContent">

	<form method="post" action="ssadmin/updateFfreeLotteryRule.html"
		class="pageForm required-validate"
		onsubmit="return validateCallback(this,dialogAjaxDone)">
		<div class="pageFormContent nowrap" layoutH="97">
			<dl>
				<dt>奖励名称：</dt>
				<dd>
					<input type="hidden" name="fid" value="${ffreeLotteryRule.fid}" />
					<input type="text" name="fname" maxlength="30" class="required"
						size="70" value="${ffreeLotteryRule.fname}" />
				</dd>
			</dl>
			<dl>
				<dt>奖励：</dt>
				<dd>
					<input type="text" name="freward" maxlength="30"
						class="required number" size="70" value="<fmt:formatNumber value="${ffreeLotteryRule.freward}" pattern="##.##" maxFractionDigits="8"></fmt:formatNumber>" />
				</dd>
			</dl>
		</div>
		<div class="formBar">
			<ul>
				<li><div class="buttonActive">
						<div class="buttonContent">
							<button type="submit">保存</button>
						</div>
					</div></li>
				<li><div class="button">
						<div class="buttonContent">
							<button type="button" class="close">取消</button>
						</div>
					</div></li>
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

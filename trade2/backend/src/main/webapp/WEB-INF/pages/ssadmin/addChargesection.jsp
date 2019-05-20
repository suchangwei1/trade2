<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="comm/include.inc.jsp"%>
<h2 class="contentTitle">添加结算时间</h2>


<div class="pageContent">

	<form method="post" action="ssadmin/saveChargesection.html"
		class="pageForm required-validate"
		onsubmit="return validateCallback(this,dialogAjaxDone)">
		<div class="pageFormContent nowrap" layoutH="97">
			<dl>
				<dt>开始结算小时：</dt>
				<dd>
					<input type="text" name="fstartHour" maxlength="30"
						class="required number" size="30" />
				</dd>
			</dl>
			<dl>
				<dt>结束结算小时：</dt>
				<dd>
					<input type="text" name="fendHour" maxlength="30"
						class="required number" size="30" />
				</dd>
			</dl>
		</div>
		<div class="formBar">
			<ul>
				<li><div class="buttonActive">
						<div class="buttonContent">
							<button type="submit">保存</button>
						</div>
					</div>
				</li>
				<li><div class="button">
						<div class="buttonContent">
							<button type="button" class="close">取消</button>
						</div>
					</div>
				</li>
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

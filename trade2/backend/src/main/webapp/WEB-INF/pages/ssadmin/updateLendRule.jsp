<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="comm/include.inc.jsp"%>
<h2 class="contentTitle">修改货币规则信息</h2>


<div class="pageContent">

	<form method="post" action="ssadmin/updateLendRule.html"
		class="pageForm required-validate"
		onsubmit="return validateCallback(this,dialogAjaxDone)">
		<div class="pageFormContent nowrap" layoutH="97">
			<dl>
				<dt>会员等级：</dt>
				<dd>
					<input type="hidden" name="fid" value="${lendrule.fid }" /> 
					<input type="text" name="flevel" value="${lendrule.flevel_s }" readonly="true"/>
				</dd>
			</dl>
			<dl>
				<dt>最大借款金额：</dt>
				<dd>
					<input type="text" name="famount" class="required number" value="${lendrule.famount }" />
				</dd>
			</dl>
			<dl>
				<dt>杆杠倍数：</dt>
				<dd>
					<input type="text" name="frate" class="required number" value="${lendrule.frate }" />
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

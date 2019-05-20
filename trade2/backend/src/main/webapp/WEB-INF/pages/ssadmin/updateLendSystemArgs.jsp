<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="comm/include.inc.jsp"%>
<h2 class="contentTitle">更新参数信息</h2>


<div class="pageContent">

	<form method="post" action="ssadmin/updateLendsystemargs.html"
		class="pageForm required-validate"
		onsubmit="return validateCallback(this,dialogAjaxDone)">
		<div class="pageFormContent nowrap" layoutH="97">
			<dl>
				<dt>KEY:</dt>
				<dd>
					<input type="hidden" name="fid" value="${lendsystemargs.fid}" /> <input
						type="text" name="fkey" maxlength="50" class="required" size="70"
						value="${lendsystemargs.fkey}" readonly="true" />
				</dd>
			</dl>
			<dl>
				<dt>参数值:</dt>
				<dd>
					<textarea name="fvalue" rows="10" cols="70" class="required">${lendsystemargs.fvalue}</textarea>
				</dd>
			</dl>
			<dl>
				<dt>备注:</dt>
				<dd>
					<textarea name="fdesc" cols="70" rows="1"  readonly="true" >${lendsystemargs.fdesc}</textarea>
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

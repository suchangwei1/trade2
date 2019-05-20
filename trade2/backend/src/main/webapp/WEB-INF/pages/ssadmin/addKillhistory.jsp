<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="comm/include.inc.jsp"%>
<h2 class="contentTitle">添加长虫信息</h2>


<div class="pageContent">

	<form method="post" action="ssadmin/saveKillhistory.html"
		class="pageForm required-validate"
		onsubmit="return validateCallback(this,dialogAjaxDone)">
		<div class="pageFormContent nowrap" layoutH="97">
			<dl>
				<dt>类型：</dt>
				<dd>
					<select type="combox" name="ftype">
					<c:forEach items="${typeMap}" var="t">
						<option value="${t.key}">${t.value}</option>
					</c:forEach>
		            </select>
				</dd>
			</dl>
			<dl>
				<dt>用户ID：</dt>
				<dd>
					<input type="text" name="fuser" maxlength="50"  size="100" />
					<span>用|隔开，一个用户不用|，所有用户为空！</span>
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

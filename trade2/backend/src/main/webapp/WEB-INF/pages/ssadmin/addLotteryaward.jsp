<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="comm/include.inc.jsp"%>
<h2 class="contentTitle">添加奖品信息</h2>

<div class="pageContent">

	<form method="post" action="ssadmin/saveLotteryaward.html"
		class="pageForm required-validate"
		onsubmit="return validateCallback(this,dialogAjaxDone)">
		<div class="pageFormContent nowrap" layoutH="97">
			<dl>
				<dt>奖品类型：</dt>
				<dd>
					<select type="combox" name="ftype" class="required">
						<c:forEach items="${map}" var="type">
							<option value="${type.key}">${type.value}</option>
						</c:forEach>
					</select>
				</dd>
			</dl>
			<dl>
				<dt>奖品名称：</dt>
				<dd>
					<input type="text" name="fname" class="required" size="70" />
				</dd>
			</dl>
			<dl>
				<dt>奖品数量：</dt>
				<dd>
					<input type="text" name="fqty" class="required number" />
				</dd>
			</dl>
			<dl>
				<dt>中奖概率：</dt>
				<dd>
					<input type="text" name="fchance" class="required number" />
				</dd>
			</dl>
			<dl>
				<dt>奖品剩余总数量：</dt>
				<dd>
					<input type="text" name="ftotal" class="required number" />
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

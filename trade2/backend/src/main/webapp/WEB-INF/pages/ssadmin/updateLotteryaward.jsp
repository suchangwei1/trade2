<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="comm/include.inc.jsp"%>
<h2 class="contentTitle">修改奖品信息</h2>

<div class="pageContent">

	<form method="post" action="ssadmin/updateLotteryaward.html"
		class="pageForm required-validate"
		onsubmit="return validateCallback(this,dialogAjaxDone)">
		<div class="pageFormContent nowrap" layoutH="97">
			<dl>
				<dt>奖品类型：</dt>
				<dd>
					<select type="combox" name="ftype" class="required">
						<c:forEach items="${map}" var="type">
							<c:if test="${type.key == lotteryaward.type}">
								<option value="${type.key}" selected="true">${type.value}</option>
							</c:if>
							<c:if test="${type.key != lotteryaward.type}">
								<option value="${type.key}">${type.value}</option>
							</c:if>
						</c:forEach>
					</select>
				</dd>
			</dl>
			<dl>
				<dt>奖品名称：</dt>
				<dd>
					<input type="hidden" name="fid" value="${lotteryaward.fid }" /> <input
						type="text" name="fname" class="required" size="70"
						value="${lotteryaward.fname }" />
				</dd>
			</dl>
			<dl>
				<dt>奖品数量：</dt>
				<dd>
					<input type="text" name="fqty" class="required number"
						value="${lotteryaward.count }" />
				</dd>
			</dl>
			<dl>
				<dt>奖品剩余总数量：</dt>
				<dd>
					<input type="text" name="ftotal" class="required number" value="${lotteryaward.ftotal }"/>
				</dd>
			</dl>
			<dl>
				<dt>中奖概率：</dt>
				<dd>
					<input type="text" name="fchance" class="required number"
						value="<fmt:formatNumber value="${lotteryaward.fchance }"  pattern="##.##" maxIntegerDigits="10" maxFractionDigits="10"/>" />
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

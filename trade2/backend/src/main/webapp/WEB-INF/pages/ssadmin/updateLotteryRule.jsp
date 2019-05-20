<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="comm/include.inc.jsp"%>
<h2 class="contentTitle">修改奖励规则信息</h2>

<div class="pageContent">

	<form method="post" action="ssadmin/updateLotteryRule.html"
		class="pageForm required-validate"
		onsubmit="return validateCallback(this,dialogAjaxDone)">
		<div class="pageFormContent nowrap" layoutH="97">
			<dl>
				<dt>公告：</dt>
				<dd>
				    <input type="hidden" name="fid" value="${lotteryrule.fid}"/>
					<input type="text" name="ftitle" class="required" size="70" value="${lotteryrule.ftitle }"/>
				</dd>
			</dl>
			<dl>
				<dt>消耗币名称：</dt>
				<dd>
					<input type="hidden" name="fid" value="${lotteryrule.fid }" /> <select
						type="combox" name="vid" class="required">
						<c:forEach items="${allType}" var="type">
							<c:if test="${type.fid == lotteryrule.fvirtualCoinTypeId.fid}">
								<option value="${type.fid}" selected="true">${type.fname}</option>
							</c:if>
							<c:if test="${type.fid != lotteryrule.fvirtualCoinTypeId.fid}">
								<option value="${type.fid}">${type.fname}</option>
							</c:if>
						</c:forEach>
					</select>
				</dd>
			</dl>
			<dl>
				<dt>消耗数量：</dt>
				<dd>
					<input type="text" name="fqty" class="required number" value="${lotteryrule.fqty }"/>
				</dd>
			</dl>
			<dl>
				<dt>消耗积分：</dt>
				<dd>
					<input type="text" name="fscore" class="required number" value="${lotteryrule.fscore }"/>
				</dd>
			</dl>
			<dl>
				<dt>兑换次数：</dt>
				<dd>
					<input type="text" name="fplayEggTimes" class="required digits" value="${lotteryrule.fplayEggTimes }"/>
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

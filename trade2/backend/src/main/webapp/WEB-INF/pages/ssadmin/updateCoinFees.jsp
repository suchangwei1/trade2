<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="comm/include.inc.jsp"%>
<h2 class="contentTitle">
	修改<font color="red">${virtualCoinType.fname}</font>提现管理
</h2>

<div class="pageContent">
	<form method="post" action="ssadmin/updateCoinFee.html"
		class="pageForm required-validate"
		onsubmit="return validateCallback(this,dialogAjaxDone)">
		<div class="pageFormContent nowrap" layoutH="97">
		<input type="hidden" name="fid" value="${virtualCoinType.fid}"/>
			<%--<dl>
				<dt>买入手续费(百分比)：</dt>
				<dd>
					<input type="text" name="buyFee"
						   value="<fmt:formatNumber
							value="${fee.buyFee}" pattern="#0.######" />"
						   size="70" class="required number" />
				</dd>
			</dl>--%>
			<%--<dl>
				<dt>交易手续费(百分比)：</dt>
				<dd>
					<input type="text" name="fee"
						value="<fmt:formatNumber
							value="${fee.ffee}" pattern="#0.######" />"
						size="70" class="required number" />
				</dd>
			</dl>--%>
			<dl>
				<dt>最小提现额度：</dt>
				<dd>
					<input type="text" name="minWithdraw"
						value="<fmt:formatNumber
							value="${fee.minWithdraw}" pattern="#0.######"/>"
						size="20" class="required number" />
				</dd>
			</dl>
			<dl>
				<dt>提现手续费(固定+百分比)：</dt>
				<dd>
					<input type="text" name="withdraw"
						value="<fmt:formatNumber
							value="${fee.withdraw}" pattern="#0.######"/>"
						size="20" class="required number" />
					<span style="float:left;font-size: 20px;">+</span>
					<input type="text" name="withdrawRatio"
						value="<fmt:formatNumber
							value="${fee.withdrawRatio}" pattern="#0.######"/>"
						size="20" class="required number" />
				</dd>
			</dl>
			<dl>
				<dt>提现手续费的币种类型：</dt>
				<dd>
					<select class="required" name="withdrawFeeType">
						<option value="">请选择</option>
						<c:forEach items="${coins}" var="coin">
							<option <c:if test="${fee.withdrawFeeType == coin.fid}">selected</c:if> value="${coin.fid}">${coin.fname}(${coin.fShortName})</option>
						</c:forEach>
					</select>
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

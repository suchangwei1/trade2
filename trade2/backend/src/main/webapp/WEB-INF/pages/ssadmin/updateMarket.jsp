<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="comm/include.inc.jsp"%>
<h2 class="contentTitle">添加交易市场</h2>


<div class="pageContent">

	<form method="post" action="ssadmin/updateMarket.html"
		class="pageForm required-validate"
		onsubmit="return validateCallback(this,dialogAjaxDone)">
		<div class="pageFormContent nowrap" layoutH="97">
			<input type="hidden" name="id" value="${market.id}">
			<dl>
				<dt>买入手续费：</dt>
				<dd>
					<input maxlength="20" class="required textInput" size="50" name="buyFee" value="<fmt:formatNumber value="${market.buyFee * 100}" pattern="#.##"/>" />%
				</dd>
			</dl>
			<dl>
				<dt>卖出手续费：</dt>
				<dd>
					<input maxlength="20" class="required textInput" size="50" name="sellFee" value="<fmt:formatNumber value="${market.sellFee * 100}" pattern="#.##"/>" />%
				</dd>
			</dl>
			<dl>
				<dt>最小交易量：</dt>
				<dd>
					<input name="minCount" maxlength="20" class="required textInput" size="50" value="<fmt:formatNumber value="${market.minCount}" pattern="#.####"/>"/>
				</dd>
			</dl>
			<dl>
				<dt>最大交易量：</dt>
				<dd>
					<input name="maxCount" maxlength="20" class="required textInput" size="50" value="<fmt:formatNumber value="${market.maxCount}" pattern="#.####"/>"/>
				</dd>
			</dl>
			<dl>
				<dt>最小价格：</dt>
				<dd>
					<input name="minPrice" maxlength="20" class="required textInput" size="50" value="<fmt:formatNumber value="${market.minPrice}" pattern="#.####"/>"/>
				</dd>
			</dl>
			<dl>
				<dt>最大价格：</dt>
				<dd>
					<input name="maxPrice" maxlength="20" class="required textInput" size="50" value="<fmt:formatNumber value="${market.maxPrice}" pattern="#.####"/>"/>
				</dd>
			</dl>
			<dl>
				<dt>最小交易额：</dt>
				<dd>
					<input name="minMoney" maxlength="20" class="required textInput" size="50" value="<fmt:formatNumber value="${market.minMoney}" pattern="#.####"/>"/>
				</dd>
			</dl>
			<dl>
				<dt>最大交易额：</dt>
				<dd>
					<input name="maxMoney" maxlength="20" class="required textInput" size="50" value="<fmt:formatNumber value="${market.maxMoney}" pattern="#.####"/>"/>
				</dd>
			</dl>
			<dl>
				<dt>交易时间：</dt>
				<dd>
					<input name="tradeTime" maxlength="20" class="required textInput" size="50" value="${market.tradeTime}"/>例：00:00-24:00
				</dd>
			</dl>
			<%--<dl>
				<dt>涨跌幅：</dt>
				<dd>
					<input name="updown" maxlength="20" class="textInput" size="50" value="<fmt:formatNumber value="${market.updown * 100}" pattern="#.##"/>"/>%
				</dd>
			</dl>--%>
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

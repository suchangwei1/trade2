<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="comm/include.inc.jsp"%>
<h2 class="contentTitle">更新限价信息</h2>


<div class="pageContent">

	<form method="post" action="ssadmin/updateLimittrade.html"
		class="pageForm required-validate"
		onsubmit="return validateCallback(this,dialogAjaxDone)">
		<div class="pageFormContent nowrap" layoutH="97">
			<dl>
				<dt>虚拟币名称：</dt>
				<dd>
				    <input type="hidden" value="${flimittrade.fid }" name="fid"/>
					<select type="combox" name="vid">
						<c:forEach items="${fvirtualcointypes}" var="v">
							<c:if test="${v.fid == flimittrade.fvirtualcointype.fid}">
								<option value="${v.fid}" selected="true">${v.fname}</option>
							</c:if>
							<c:if test="${v.fid != flimittrade.fvirtualcointype.fid}">
								<option value="${v.fid}">${v.fname}</option>
							</c:if>
						</c:forEach>
					</select>
				</dd>
			</dl>
			<dl>
				<dt>最低价格：</dt>
				<dd>
					<input type="text" name="fdownprice" maxlength="20"
						class="required number" value="${flimittrade.fdownprice}" />
				</dd>
			</dl>
			<dl>
				<dt>最高价格：</dt>
				<dd>
					<input type="text" name="fupprice" maxlength="20"
						class="required number" value="${flimittrade.fupprice}" />
				</dd>
			</dl>
			<dl>
				<dt>涨跌幅：</dt>
				<dd>
					<input type="text" name="fpercent" maxlength="20"
						   class="required number" value="${flimittrade.fpercent}" /> 设置了涨跌幅，会忽略最高/低价价格，设置为0，则不启用涨跌幅限制，0.1 = 10 %
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

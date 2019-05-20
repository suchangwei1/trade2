<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="../comm/include.inc.jsp"%>
<h2 class="contentTitle">编辑ICO兑换率(<span style="color: red;">不在时间范围内不能购买</span>)</h2>


<div class="pageContent">

	<form method="post" action="/ssadmin/saveICOSwapRate.html"
		class="pageForm required-validate"
		onsubmit="return validateCallback(this,dialogAjaxDone)">
		<div class="pageFormContent nowrap" layoutH="97">
			<input type="hidden" name="id" value="${swapRate.id}">
			<dl <c:if test="${!empty swapRate.id}">style="display:none;"</c:if>>
				<dt>币种：</dt>
				<dd>
					<select class="required" name="coinType">
						<option value="">请选择</option>
						<%--<option <c:if test="${swapRate.coinType == 0}">selected</c:if> value="0">人民币</option>--%>
						<c:forEach items="${coins}" var="coin">
							<option <c:if test="${swapRate.coinType == coin.fid}">selected</c:if> value="${coin.fid}">${coin.fname}</option>
						</c:forEach>
					</select>
				</dd>
			</dl>
			<dl <c:if test="${!empty swapRate.id}">style="display:none;"</c:if>>
				<dt>ICO ID：</dt>
				<dd>
					<input class="required" readonly name="orgLookup.id" value="${swapRate.icoId}" type="text" size="20" />
					<a class="btnLook" href="/ssadmin/icoList.html?lookup=1" lookupgroup="orgLookup">查找带回</a>
				</dd>
			</dl>
			<dl>
				<dt>单位兑换量：</dt>
				<dd>
					<input class="required" name="amount" value="${swapRate.amount}" type="text" size="20" />(如：1BTC=500CTC)
				</dd>
			</dl>
			<dl>
				<dt>开启时间：</dt>
				<dd>
					<input name="startTime" class="date required" readonly value="<fmt:formatDate value="${swapRate.startTime}" pattern="yyyy-MM-dd HH:mm:ss"/>" datefmt="yyyy-MM-dd HH:mm:ss" type="text" size="20" />
				</dd>
			</dl>
			<dl>
				<dt>结束时间：</dt>
				<dd>
					<input name="endTime" class="date required" readonly value="<fmt:formatDate value="${swapRate.endTime}" pattern="yyyy-MM-dd HH:mm:ss"/>" datefmt="yyyy-MM-dd HH:mm:ss" type="text" size="20" />
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

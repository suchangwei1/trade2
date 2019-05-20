<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="../comm/include.inc.jsp"%>
<h2 class="contentTitle">ICO发放回报(<span style="color: red;">项目未结束或未成功不能发放</span>)</h2>


<div class="pageContent">

	<c:choose>
		<c:when test="${empty param.recordId}">
			<form method="post" action="/ssadmin/icoRequiteAll.html"
		</c:when>
		<c:otherwise>
			<form method="post" action="/ssadmin/icoRequite.html"
		</c:otherwise>
	</c:choose>
		class="pageForm required-validate"
		onsubmit="return validateCallback(this,dialogAjaxDone)">
		<div class="pageFormContent nowrap" layoutH="97">
			<input type="hidden" name="recordId" value="${param.recordId}">
			<input type="hidden" name="icoId" value="${param.icoId}">
			<dl <c:if test="${!empty swapRate.id}">style="display:none;"</c:if>>
				<dt>币种：</dt>
				<dd>
					<select class="required" name="coinType">
						<option value="">请选择</option>
						<c:forEach items="${coins}" var="coin">
							<option <c:if test="${swapRate.coinType == coin.fid}">selected</c:if> value="${coin.fid}">${coin.fname}</option>
						</c:forEach>
					</select>
				</dd>
			</dl>
			<dl>
				<dt>ICO项目：</dt>
				<dd>
					${ico.name}
				</dd>
			</dl>
			<dl>
				<dt>回报比例：</dt>
				<dd>
					1份&nbsp;=&nbsp;<fmt:formatNumber value="${ico.requiteRate}" pattern="#.####"/>
				</dd>
			</dl>
			<dl>
				<dt>实际已认购份额：</dt>
				<dd>
					<fmt:formatNumber value="${actualAmount}" pattern="#.####"/>
				</dd>
			</dl>
			<dl>
				<dt>实际已认购金额：</dt>
				<dd>
					${actualSwapAmount}
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

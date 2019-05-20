<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="comm/include.inc.jsp"%>
<h2 class="contentTitle">修改虚拟币类型信息</h2>


<div class="pageContent">

	<form method="post" action="ssadmin/updateVirtualCoinTypeUnforbidden.html"
		class="pageForm required-validate" enctype="multipart/form-data"
		onsubmit="return iframeCallback(this, dialogAjaxDone);">
		<div class="pageFormContent nowrap" layoutH="97">
		    <dl>
				<dt>图标链接：</dt>
				<dd>
					<input type="file" class="inputStyle" value="" name="filedata"
						id="filedata" />
				</dd>
			</dl>
		    <dl>
				<dt>ACCESS_KEY：</dt>
				<dd>
					<input type="hidden" name="fid" value="${virtualCoinType.fid}" />
					<input type="password" name="faccess_key" maxlength="30"
						class="required" size="70" value="${virtualCoinType.faccess_key}" />
				</dd>
			</dl>
			<dl>
				<dt>SECRT_KEY：</dt>
				<dd>
					<input type="password" name="fsecrt_key" maxlength="30"
						class="required" size="70" value="${virtualCoinType.fsecrt_key}" />
				</dd>
			</dl>
			<dl>
				<dt>IP地址：</dt>
				<dd>
					<input type="text" name="fip" maxlength="30" class="required"
						size="70" value="${virtualCoinType.fip}" />
				</dd>
			</dl>
			<dl>
				<dt>端口号：</dt>
				<dd>
					<input type="text" name="fport" maxlength="30"
						class="required number" size="30" value="${virtualCoinType.fport}" />
				</dd>
			</dl>
			<dl>
				<dt>总量：</dt>
				<dd>
				
					<input type="text" name="ftotalamount" maxlength="30"
						class="required number" size="30" value="<fmt:formatNumber value="${virtualCoinType.ftotalamount}" groupingUsed="false"/>">
				</dd>
				
			</dl>
			<dl>
				<dt>交易时间限制：</dt>
				<dd>
					<input type="text" name="openTrade" maxlength="30"
						class="required" size="30" value= "${virtualCoinType.openTrade}">
				</dd>
			</dl>
			<dl>
				<dt>币币交易类型：</dt>
				<dd>
					<select class="required" type="combox" name="coinTradeType">
						<%--<option <c:if test="${1 == virtualCoinType.coinTradeType}">selected="selected"</c:if> value="1">人民币区</option>--%>
						<option <c:if test="${2 == virtualCoinType.coinTradeType}">selected="selected"</c:if> value="2">ETH区</option>
						<option <c:if test="${3 == virtualCoinType.coinTradeType}">selected="selected"</c:if> value="3">BTC区</option>
						<%--<option <c:if test="${3 == virtualCoinType.coinTradeType}">selected="selected"</c:if> value="3">国际板</option>--%>
					</select>
				</dd>
			</dl>
			<%--<dl>
				<dt>权益交易类型：</dt>
				<dd>
					<input type="checkbox" name="equityType" <c:if test="${1 == virtualCoinType.equityType}">checked="checked"</c:if> value="1" />
				</dd>
			</dl>--%>
			<dl>
				<dt>是否首页展示：</dt>
				<dd>
					<input type="checkbox" name="homeShow" <c:if test="${virtualCoinType.homeShow}">checked="checked"</c:if> value="1" />
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

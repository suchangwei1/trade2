<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="comm/include.inc.jsp"%>
<h2 class="contentTitle">修改虚拟币类型信息</h2>


<div class="pageContent">

	<form method="post" action="ssadmin/updateVirtualCoinType.html"
		class="pageForm required-validate" enctype="multipart/form-data"
		onsubmit="return iframeCallback(this, dialogAjaxDone);">
		<div class="pageFormContent nowrap" layoutH="97">
			<dl>
				<dt>币种名称：</dt>
				<dd>
					<input type="hidden" name="fid" value="${virtualCoinType.fid}" />
					<input type="text" name="fname" maxlength="30" class="required"
						size="70" value="${virtualCoinType.fname}" />
				</dd>
			</dl>
			<dl>
				<dt>币种简称：</dt>
				<dd>
					<input type="text" name="fShortName" maxlength="30"
						class="required" size="70" value="${virtualCoinType.fShortName}" />
				</dd>
			</dl>
			<dl>
				<dt>符号：</dt>
				<dd>
					<input type="text" name="fSymbol" maxlength="30" class="required"
						size="40" value="${virtualCoinType.fSymbol}" />
				</dd>
			</dl>
			<dl>
				<dt>开盘价：</dt>
				<dd>
					<input type="text" name="fopenPrice" maxlength="30"
						class="required number" size="30" value="${virtualCoinType.fopenPrice}" />
				</dd>
			</dl> 
			<dl>
				<dt>是否可以提现：</dt>
				<dd>
					<c:choose>
						<c:when test="${virtualCoinType.FIsWithDraw}">
							<input type="checkbox" name="FIsWithDraw" checked='1' />
						</c:when>
						<c:otherwise>
							<input type="checkbox" name="FIsWithDraw" />
						</c:otherwise>
					</c:choose>
				</dd>
			</dl>
			<dl>
				<dt>是否可以充值：</dt>
				<dd>
					<c:choose>
						<c:when test="${virtualCoinType.FIsRecharge}">
							<input type="checkbox" name="FIsRecharge" checked='1' />
						</c:when>
						<c:otherwise>
							<input type="checkbox" name="FIsRecharge" />
						</c:otherwise>
					</c:choose>
				</dd>
			</dl>
			<dl>
				<dt>是否可以交易：</dt>
				<dd>
					<c:choose>
						<c:when test="${virtualCoinType.fisShare}">
							<input type="checkbox" name="fisShare" checked='1' />
						</c:when>
						<c:otherwise>
							<input type="checkbox" name="fisShare" />
						</c:otherwise>
					</c:choose>
				</dd>
			</dl>
			<dl>
				<dt>备注：</dt>
				<dd>
					<textarea name="fdescription" cols="70" rows="1">${virtualCoinType.fdescription}</textarea>
				</dd>
			</dl>
			<dl>
				<dt>介绍页链接：</dt>
				<dd>
					<input type="text" name="fintroUrl" value="${virtualCoinType.fintroUrl}"   size="90" />
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

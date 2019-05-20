<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="../comm/include.inc.jsp"%>
<div class="pageContent">
	<form method="get" action="/ssadmin/exportCoinWalletList.html" class="pageForm required-validate" onsubmit="return doExport(this);">
		<div class="pageFormContent nowrap" layoutH="97">
			<c:forEach items="${list}" var="type">
				<dl style="margin-left: 50px;">
					<dt></dt>
					<dd>
						<input type="radio" name="symbol" value="${type.fid}" class="required">&nbsp;&nbsp;${type.fname}
						<c:if test="${!empty type.fShortName}">(${type.fShortName})</c:if>
					</dd>
				</dl>
			</c:forEach>
		</div>
		<div class="formBar">
			<ul>
				<li><div class="buttonActive">
						<div class="buttonContent">
							<button type="submit">导出</button>
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
	function doExport(_this){
		var ret = $(_this).valid();
		if(ret){
			$.pdialog.closeCurrent();
		}
		
		return ret;
	}
</script>


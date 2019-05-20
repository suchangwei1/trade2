<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="comm/include.inc.jsp"%>
<h2 class="contentTitle">修改业务员</h2>


<div class="pageContent">

	<form method="post" action="ssadmin/updateSalescontract.html"
		class="pageForm required-validate"
		onsubmit="return validateCallback(this,dialogAjaxDone)">
		<div class="pageFormContent nowrap" layoutH="97">
			<dl>
				<dt>会员：</dt>
				<dd>
					<input type="hidden" name="fid" value="${fsalescontract.fid }"/>
					<c:if test="${salespercentLookup.id == null}">
						<input type="hidden" name="userLookup.id" value="${fsalescontract.fuser.fid}"/>
					</c:if>
					<c:if test="${salespercentLookup.id != null}">
						<input type="hidden" name="userLookup.id" value="${userLookup.id}"/>
					</c:if>
					
				    <input type="text" class="required" name="userLookup.floginName" value="${fsalescontract.fuser.floginName }" suggestFields="id,floginName"
				     suggestUrl="ssadmin/userLookup.html" lookupGroup="orgLookup" readonly="readonly" size="70"/>
				    <a class="btnLook" href="ssadmin/userLookup.html" lookupGroup="userLookup">查找带回</a>	
				</dd>
			</dl>
			<dl>
				<dt>等级：</dt>
				<dd>
					<input type="hidden" name="salespercentLookup.id" value="${fsalescontract.fsalespercent.fid}"/>
				    <input type="text" class="required" name="salespercentLookup.level" value="${fsalescontract.fsalespercent.flevel }" suggestFields="id,level,fgrade_s,fgrade"
				     suggestUrl="ssadmin/salespercentLookup.html" lookupGroup="orgLookup" readonly="readonly" size="70"/>
				    <a class="btnLook" href="ssadmin/salespercentLookup.html" lookupGroup="salespercentLookup">查找带回</a>	
				</dd>
			</dl>
			<dl>
				<dt>级别类型:</dt>
					<input type="hidden" name="salespercentLookup.fgrade" value="${fsalescontract.fgrade}"/>
					<input type="text" name="salespercentLookup.fgrade_s" value="${fsalescontract.fgrade_s}" readonly="readonly"/>			    
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

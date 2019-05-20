<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="comm/include.inc.jsp"%>
<h2 class="contentTitle">业务员升级</h2>


<div class="pageContent">

	<form method="post" action="ssadmin/upLevelSalescontract.html"
		class="pageForm required-validate"
		onsubmit="return validateCallback(this,dialogAjaxDone)">
		<div class="pageFormContent nowrap" layoutH="97">
			<dl>
				<dt>会员：</dt>
				<dd>
					<input type="hidden" name="fid" value="${fsalescontract.fid }"/>
				    <input type="text" class="required" name="userLookup.floginName" value="${fsalescontract.fuser.floginName }" suggestFields="id,floginName"
				     suggestUrl="ssadmin/userLookup.html" lookupGroup="orgLookup" readonly="readonly" size="70"/>
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
				<dt>保证金:</dt>
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

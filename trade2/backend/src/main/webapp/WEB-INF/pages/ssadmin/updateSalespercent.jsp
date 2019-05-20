<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="comm/include.inc.jsp"%>
<h2 class="contentTitle">修改业务员等级</h2>


<div class="pageContent">

	<form method="post" action="ssadmin/updateSalespercent.html"
		class="pageForm required-validate"
		onsubmit="return validateCallback(this,dialogAjaxDone)">
		<div class="pageFormContent nowrap" layoutH="97">
			<dl>
				<dt>业务员等级：</dt>
				<dd>
					<input type="hidden" name="fid" value="${fsalespercent.fid }"/>		
					<input type="text" name="flevel" maxlength="30"
						class="required number" size="30" value="${fsalespercent.flevel}" readonly="readonly"/>
				</dd>
			</dl>
			<dl>
				<dt>级别类型：</dt>
				<dd>
					<select type="combox" name="type">
					<c:forEach items="${map}" var="m">
					<c:if test="${m.key == fsalespercent.fgrade}">
				    	<option value="${m.key}" selected="true">${m.value}</option>
					</c:if>
					<c:if test="${m.key != fsalespercent.fgrade}">
				    	<option value="${m.key}">${m.value}</option>
					</c:if>
					</c:forEach>
		            </select>
				</dd>
			</dl>
			<dl>
				<dt>总分成比例：</dt>
				<dd>
					<input type="text" name="ftotalpercent" maxlength="30"
						class="required number" size="30" value="${fsalespercent.ftotalpercent}"/>
				</dd>
			</dl>
			<dl>
				<dt>砸FC比例：</dt>
				<dd>
					<input type="text" name="feggpercent" maxlength="30"
						class="required number" size="30" value="${fsalespercent.feggpercent}"/>
				</dd>
			</dl>
			<dl>
				<dt>领导奖比例：</dt>
				<dd>
					<input type="text" name="fleaderpercent" maxlength="30"
						class="required number" size="30" value="${fsalespercent.fleaderpercent}"/>
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

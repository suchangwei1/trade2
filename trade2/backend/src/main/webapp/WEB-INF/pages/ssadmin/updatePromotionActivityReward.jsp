<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="comm/include.inc.jsp"%>
<h2 class="contentTitle">修改推荐人奖励信息</h2>

<div class="pageContent">

	<form method="post" action="ssadmin/updatePromotionActivityReward.html"
		class="pageForm required-validate"
		onsubmit="return validateCallback(this,dialogAjaxDone)">
		<div class="pageFormContent nowrap" layoutH="97">
			<dl>
				<dt>活动：</dt>
				<dd>
					<input type="hidden" name="uid" value="${activityreward.fid}" /> <input
						type="hidden" name="activityLookup.id"
						value="${activityreward.factivity.fid}" /> <input type="text"
						class="required" name="activityLookup.ftitle"
						value="${activityreward.factivity.ftitle}"
						suggestFields="id,ftitle" suggestUrl="ssadmin/activityLookup.html"
						lookupGroup="orgLookup" readonly="readonly" size="70" />
					<!--  <a
						class="btnLook" href="ssadmin/activityLookup.html"
						lookupGroup="activityLookup">查找带回</a> -->
				</dd>
			</dl>
			<dl>
				<dt>奖励币种：</dt>
				<dd>
					<select type="combox" name="vid" class="required">
						<c:forEach items="${allType}" var="type">
							<c:if test="${type.fid == activityreward.fvirtualcointype.fid}">
								<option value="${type.fid}" selected="true">${type.fname}</option>
							</c:if>
							<c:if test="${type.fid != activityreward.fvirtualcointype.fid}">
								<option value="${type.fid}">${type.fname}</option>
							</c:if>
						</c:forEach>
					</select>
				</dd>
			</dl>
			<dl>
				<dt>是否按比率：</dt>
				<dd>
					<c:choose>
						<c:when test="${activityreward.frateOrReal}">
							<input type="checkbox" name="frateOrReal" checked='1' />
						</c:when>
						<c:otherwise>
							<input type="checkbox" name="frateOrReal" />
						</c:otherwise>
					</c:choose>
					<span>奖励比率(true) 还是奖励实数(false)</span>
				</dd>
			</dl>
			<dl>
				<dt>奖励数量：</dt>
				<dd>
					<input type="text" name="famount" class="required number"
						value="${activityreward.famount}" />
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

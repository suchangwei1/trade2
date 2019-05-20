<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="comm/include.inc.jsp"%>
<h2 class="contentTitle">修改认购信息</h2>

<div class="pageContent">

	<form method="post" action="ssadmin/updateSubscription1.html"
		class="pageForm required-validate"
		onsubmit="return validateCallback(this,dialogAjaxDone)">
		<div class="pageFormContent nowrap" layoutH="97">
		   <dl>
				<dt>简介：</dt>
				<dd>
					<textarea class="required" name="ftitle" rows="4" cols="70">${subscription.ftitle }</textarea>
				</dd>
			</dl>
			<dl>
				<dt>认购虚拟币名称：</dt>
				<dd>
					<input type="hidden" name="fid" value="${subscription.fid }" /> <select
						type="combox" name="vid" class="required">
						<c:forEach items="${allType}" var="type">
							<c:if test="${type.fid == subscription.fvirtualcointype.fid}">
								<option value="${type.fid}" selected="true">${type.fname}</option>
							</c:if>
							<c:if test="${type.fid != subscription.fvirtualcointype.fid}">
								<option value="${type.fid}">${type.fname}</option>
							</c:if>
						</c:forEach>
					</select>
				</dd>
			</dl>
			<dl>
				<dt>是否开放认购：</dt>
				<dd>
					<c:choose>
						<c:when test="${subscription.fisopen}">
							<input type="checkbox" name="fisopen" checked='1' />
						</c:when>
						<c:otherwise>
							<input type="checkbox" name="fisopen" />
						</c:otherwise>
					</c:choose>
				</dd>
			</dl>
			<dl>
				<dt>认购总数量：</dt>
				<dd>
					<input type="text" name="ftotal" class="required number"
						value="${subscription.ftotal}" />
				</dd>
			</dl>
			<dl>
				<dt>认购价格：</dt>
				<dd>
					<input type="text" name="fprice" class="required number"
						value="${subscription.fprice}" />
				</dd>
			</dl>
			<dl>
				<dt>每人最大认购数量：</dt>
				<dd>
					<input type="text" name="fbuyCount" class="required digits"
						value="${subscription.fbuyCount}" /><span>0为无限</span>
				</dd>
			</dl>
			<dl>
				<dt>每人最多认购次数：</dt>
				<dd>
					<input type="text" name="fbuyTimes" class="required digits"
						value="${subscription.fbuyTimes}" /><span>0为无限</span>
				</dd>
			</dl>
			<dl>
				<dt>冻结天数：</dt>
				<dd>
					<input type="text" name="fdays" class="required digits" value="${subscription.fdays}"/><span>0为不冻结</span>
				</dd>
			</dl>
			<dl>
				<dt>开始时间：</dt>
				<dd>
					<input type="text" name="fbeginTime" class="required date"
						readonly="true" dateFmt="yyyy-MM-dd HH:mm:ss" size="40"
						value="${s}" /> <a class="inputDateButton"
						href="javascript:;">选择</a>
				</dd>
			</dl>
			<dl>
				<dt>结束时间：</dt>
				<dd>
					<input type="text" name="fendTime" class="required date"
						readonly="true" dateFmt="yyyy-MM-dd HH:mm:ss" size="40"
						value="${e}" /> <a class="inputDateButton"
						href="javascript:;">选择</a>
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

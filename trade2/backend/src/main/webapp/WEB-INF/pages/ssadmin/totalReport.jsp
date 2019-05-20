<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="comm/include.inc.jsp"%>
<!DOCTYPE HTML>
<html>
<head>
</head>
<body>
	<div class="pageFormContent" layoutH="20">

		<fieldset>
			<legend>会员信息</legend>
			<dl>
				<dt>全站总会员数：</dt>
				<dd style="color:red;font-weight:bold;">
					<span class="unit">${totalUser}</span>
				</dd>
			</dl>
			<dl>
				<dt>全站已认证会员数：</dt>
				<dd style="color:red;font-weight:bold;">
					<span class="unit">${totalValidateUser}</span>
				</dd>
			</dl>
			<dl>
				<dt>今日新增会员数：</dt>
				<dd style="color:red;font-weight:bold;">
					<span class="unit">${todayTotalUser}</span>
				</dd>
			</dl>
			<dl>
				<dt>今日已认证会员数：</dt>
				<dd style="color:red;font-weight:bold;">
					<span class="unit">${todayValidateUser}</span>
				</dd>
			</dl>
		</fieldset>

		<fieldset>
		<legend>当前累计充值信息(充值成功)</legend>
		<c:forEach items="${virtualInAllMap}" var="v">
			<dl>
				<dt>${v.fName}数量：</dt>
				<dd style="color:red;font-weight:bold;">
						<span class="unit"><fmt:formatNumber
								value="${v.amount}" pattern="#0.######" /> </span>
				</dd>
			</dl>
		</c:forEach>
		</fieldset>

		<fieldset>
			<legend>今日充值信息(充值成功)</legend>
			<c:forEach items="${virtualInMap}" var="virtual">
			<dl>
			<dt>${virtual.fName}数量：</dt>
			<dd style="color:red;font-weight:bold;">
			<span class="unit"><fmt:formatNumber
			value="${virtual.amount}" pattern="#0.######" /> </span>
			</dd>
			</dl>
			</c:forEach>
		</fieldset>

		<fieldset>
			<legend>当前累计待提现信息(提现成功)</legend>
			<c:forEach items="${virtualOutAllSuccessMap}" var="v">
				<dl>
					<dt>${v.fName}数量：</dt>
					<dd style="color:red;font-weight:bold;">
						<span class="unit"><fmt:formatNumber
								value="${v.amount}" pattern="#0.######" /> </span>
					</dd>
				</dl>
			</c:forEach>
		</fieldset>

		<fieldset>
			<legend>今日提现信息（提现成功）</legend>
			<%--<dl>--%>
				<%--<dt>今日提现金额：</dt>--%>
				<%--<dd style="color:red;font-weight:bold;">--%>
					<%--<span class="unit"><fmt:formatNumber--%>
							<%--value="${amountOutMap.totalAmount}" pattern="#0.######" /> </span>--%>
				<%--</dd>--%>
			<%--</dl>--%>
			<%--<dl>--%>
				<%--<dt>累计提现金额：</dt>--%>
				<%--<dd style="color:red;font-weight:bold;">--%>
					<%--<span class="unit"><fmt:formatNumber--%>
							<%--value="${amountOutMap1.totalAmount}" pattern="#0.######" /> </span>--%>
				<%--</dd>--%>
			<%--</dl>--%>
			<c:forEach items="${virtualOutSuccessMap}" var="virtualOutSuccess">
				<dl>
					<dt>${virtualOutSuccess.fName}数量：</dt>
					<dd style="color:red;font-weight:bold;">
						<span class="unit"><fmt:formatNumber
								value="${virtualOutSuccess.amount}" pattern="#0.######" /> </span>
					</dd>
				</dl>
			</c:forEach>
		</fieldset>
	</div>
</body>
</html>


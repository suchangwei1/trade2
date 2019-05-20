<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="comm/include.inc.jsp"%>
<!DOCTYPE HTML>
<html>
<head>
</head>

<body>
	<div class="pageHeader">
		<form action="ssadmin/curEntrustReport.html" method="post" onsubmit="return navTabSearch(this);">
			<div class="searchBar">
				<table class="searchContent">
					<tr>
						<td>时间段：<input type="text" name="startDate" class="date textInput readonly required"
							readonly="true" size="10" value="${startDate}"></td>
						<td>至&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<input type="text" name="endDate" class="date textInput readonly required"
							readonly="true" size="10" value="${endDate}"></td>
						<td>统计全部：<input <c:if test="${!empty isAll}">checked="checked"</c:if> type="checkbox" name="isAll" class="required" value="true"></td>
						<td>委托类型： <select type="combox" name="type" class="required">
								<option value="0" <c:if test="${0 == type}">selected="selected"</c:if>>买入</option>
								<option value="1" <c:if test="${1 == type}">selected="selected"</c:if>>卖出</option>
						</select>
						</td>
					</tr>
				</table>
				<div class="subBar">
					<ul>
						<li><div class="buttonActive">
								<div class="buttonContent">
									<button type="submit">查询</button>
								</div>
							</div></li>
					</ul>
				</div>
			</div>
		</form>
	</div>
	<div class="pageFormContent" layoutH="20">
		<c:if test="${0 == type }">
			<fieldset>
				<legend>当前买入委托信息</legend>
				<c:forEach items="${entrustMap}" var="entrustBuy">
					<dl>
						<dt>买入${entrustBuy.fName}数量：</dt>
						<dd style="color: red; font-weight: bold;">
							<span class="unit"><fmt:formatNumber value="${entrustBuy.total}" pattern="#0.######" />
							</span>
						</dd>
					</dl>
				</c:forEach>
			</fieldset>
		</c:if>
		<c:if test="${1 == type }">
			<fieldset>
				<legend>当前卖出委托信息</legend>
				<c:forEach items="${entrustMap}" var="entrustBuy">
					<dl>
						<dt>卖出${entrustBuy.fName}数量：</dt>
						<dd style="color: red; font-weight: bold;">
							<span class="unit"><fmt:formatNumber value="${entrustBuy.total}" pattern="#0.######" />
							</span>
						</dd>
					</dl>
				</c:forEach>
			</fieldset>
		</c:if>
	</div>
</body>
</html>


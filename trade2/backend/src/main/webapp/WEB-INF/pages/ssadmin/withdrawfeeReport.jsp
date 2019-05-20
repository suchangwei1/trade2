<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" %>
<%@ include file="comm/include.inc.jsp" %>
<!DOCTYPE HTML>
<html>
<head>
</head>
<body>
<div class="pageHeader">
	<form id="queryForm" onsubmit="return navTabSearch(this);"
		  action="ssadmin/withdrawfeeReport.html" method="post">
		<div class="searchBar">

			<table class="searchContent">
				<tr>
					<td>起始日期： <input type="text" dateFmt="yyyy-MM-dd HH:mm:ss" name="startDate" class="date"
									 readonly="true"  value="${startDate }" />
					</td>
					<td>结束日期： <input type="text" name="endDate" class="date"
									 readonly="true" dateFmt="yyyy-MM-dd HH:mm:ss" value="${endDate }" />
					</td>
				</tr>
			</table>
			<div class="subBar">
				<ul>
					<li><div class="buttonActive">
						<div class="buttonContent">
							<button type="submit" style="line-height:27px;">查询</button>
						</div>
					</div>
					</li>
				</ul>
			</div>
		</div>
	</form>
</div>
<div class="pageContent">
	<div class="pageFormContent" layoutH="20">
		<fieldset>
			<legend>提现手续费</legend>
			<c:forEach items="${feeList}" var="list">
				<dl>
					<dt>${coinMap[list.cid]}：</dt>
					<dd style="color:red;font-weight:bold;">
						<span class="unit"><fmt:formatNumber
								value="${list.fee}" pattern="#0.######"/> </span>
					</dd>
				</dl>
			</c:forEach>
		</fieldset>
		<fieldset>
			<legend>交易手续费</legend>
			<c:forEach items="${tradeList}" var="list">
				<dl>
					<dt>${list.type == 0 ? "买入": "卖出"}${marketMap[list.mid]}：</dt>
					<dd style="color:red;font-weight:bold;">
						<span class="unit"><fmt:formatNumber
								value="${list.fee}" pattern="#0.######"/> </span>
					</dd>
				</dl>
			</c:forEach>
		</fieldset>
	</div>
</div>
</body>
</html>


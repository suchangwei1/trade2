<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="comm/include.inc.jsp"%>

<form id="pagerForm" method="post"
	  action="/ssadmin/informationTypeLookup.html">
	<input type="hidden" name="status" value="${param.status}"> <input
		type="hidden" name="keywords" value="${keywords}" /> <input
		type="hidden" name="pageNum" value="${currentPage}" /> <input
		type="hidden" name="numPerPage" value="${numPerPage}" /> <input
		type="hidden" name="orderField" value="${param.orderField}" />
</form>
<%--<div class="pageHeader">--%>
	<%--<form rel="pagerForm" method="post"--%>
		  <%--action="ssadmin/blockArticleTypeLookup.html"--%>
		  <%--onsubmit="return dwzSearch(this, 'dialog');">--%>
		<%--<div class="searchBar">--%>
			<%--<ul class="searchContent">--%>
				<%--<li><label>关键词:</label> <input class="textInput"--%>
											   <%--name="keywords" value="${keywords}" type="text" size="70">--%>
				<%--</li>--%>
			<%--</ul>--%>
			<%--<div class="subBar">--%>
				<%--<ul>--%>
					<%--<li><div class="buttonActive">--%>
						<%--<div class="buttonContent">--%>
							<%--<button type="submit">查询</button>--%>
						<%--</div>--%>
					<%--</div></li>--%>
				<%--</ul>--%>
			<%--</div>--%>
		<%--</div>--%>
	<%--</form>--%>
<%--</div>--%>
<div class="pageContent">

	<table class="table" layoutH="118" targetType="dialog" width="100%">
		<thead>
			<tr>
				<th width="20">序号</th>
				<th width="30">分类名称</th>
				<th width="30">分类关键字</th>
				<th width="30">查找带回</th>
			</tr>
		</thead>
		<tbody>
			<c:forEach items="${keywordList}" var="keyword" varStatus="num">
				<tr>
					<td>${num.index +1}</td>
					<td>${keyword.className}</td>
					<td>${keyword.keyName}</td>
					<td><a class="btnSelect"
						href="javascript:$.bringBack({id:'${keyword.id}', informationKeyword:'${keyword.keyName}'})"
						title="查找带回">选择</a>
					</td>
				</tr>
			</c:forEach>
		</tbody>
	</table>

	<div class="panelBar">
		<div class="pages">
			<span>总共: ${totalCount}条</span>
		</div>
		<div class="pagination" targetType="dialog" totalCount="${totalCount}"
			numPerPage="${numPerPage}" pageNumShown="5"
			currentPage="${currentPage}"></div>
	</div>
</div>
<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="comm/include.inc.jsp"%>
<form id="pagerForm" method="post" action="ssadmin/entrustlogList.html">
	<input type="hidden" name="status" value="${param.status}"> <input
		type="hidden" name="keywords" value="${keywords}" /><input
		type="hidden" name="startDate" value="${startDate}" /> <input
		type="hidden" name="endDate" value="${endDate}" /> <input
		type="hidden" name="pageNum" value="${currentPage}" /> 
		<input type="hidden" name="numPerPage" value="${numPerPage}" />
		<input type="hidden" name="totalCount" value="${totalCount}" />
</form>


<div class="pageHeader">
	<form onsubmit="return navTabSearch(this);"
		action="ssadmin/entrustlogList.html" method="post">
		<div class="searchBar">

			<table class="searchContent">
				<tr>
					<td>会员信息：<input type="text" name="keywords"
						value="${keywords}" size="40" /></td>
					<td>日期：<input type="text" name="startDate" class="date required"
						readonly="true" value="${startDate }" /></td>
					<td>至&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<input type="text" name="endDate" class="date required"
						readonly="true" value="${endDate }" /></td>
				</tr>
			</table>
			<div class="subBar">
				<ul>
					<li><div class="buttonActive">
							<div class="buttonContent">
								<button type="submit">查询</button>
							</div>
						</div>
					</li>
				</ul>
			</div>
		</div>
	</form>
</div>
<div class="pageContent">
	<div class="panelBar">
		<ul class="toolBar">

		</ul>
	</div>
	<table class="table" width="100%" layoutH="138">
		<thead>
			<tr>
				<th width="20">序号</th>
				<th width="60">会员昵称</th>
				<th width="60">真实姓名</th>
				<th width="60">登陆名称</th>
				<th width="60">买入</th>
				<th width="60">卖出</th>
				<th width="60">买入+卖出</th>
				<th width="60">交易时间</th>
			</tr>
		</thead>
		<tbody>
			<c:forEach items="${entrustlogList}" var="entrustlog" varStatus="num">
				<tr>
					<td>${num.index +1}</td>
					<c:forEach items="${entrustlog}" var="e">
						<td>${e}</td>
					</c:forEach>
				</tr>
			</c:forEach>
		</tbody>
	</table>

	<div class="panelBar">
		<div class="pages">
			<span>总共: ${totalCount}条</span>
		</div>
		<div class="pagination" targetType="navTab" totalCount="${totalCount}"
			numPerPage="${numPerPage}" pageNumShown="5"
			currentPage="${currentPage}"></div>
	</div>
</div>

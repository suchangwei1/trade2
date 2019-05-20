<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="comm/include.inc.jsp"%>
<form id="pagerForm" method="post" action="ssadmin/deductTotalList.html">
	<input type="hidden" name="status" value="${param.status}"> <input
		type="hidden" name="keywords" value="${keywords}" /><input
		type="hidden" name="startDate" value="${startDate}" /> <input
		type="hidden" name="pageNum" value="${currentPage}" /> <input
		type="hidden" name="numPerPage" value="${numPerPage}" />
</form>


<div class="pageHeader">
	<form onsubmit="return navTabSearch(this);"
		action="ssadmin/deductTotalList.html" method="post">
		<div class="searchBar">

			<table class="searchContent">
				<tr>
					<td>日期：<input type="text" name="startDate" class="date"
						readonly="true" value="${startDate }" /></td>
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
				<th width="60">日期</th>
				<th width="60">开始小时</th>
				<th width="60">结束小时</th>
				<th width="60">人民币总额</th>
				<th width="60">FC总数量</th>
			</tr>
		</thead>
		<tbody>
			<c:forEach items="${deductTotalList}" var="deductTotal" varStatus="num">
				<tr>
					<td>${num.index +1}</td>
					<c:forEach items="${deductTotal}" var="e">
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

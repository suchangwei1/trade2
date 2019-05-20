<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="comm/include.inc.jsp"%>
<form id="pagerForm" method="post" action="ssadmin/lendRuleList.html">
	<input type="hidden" name="status" value="${param.status}"> <input
		type="hidden" name="keywords" value="${keywords}" /> <input
		type="hidden" name="pageNum" value="${currentPage}" /> <input
		type="hidden" name="numPerPage" value="${numPerPage}" /> <input
		type="hidden" name="orderField" value="${param.orderField}" />
</form>


<div class="pageHeader">
	<form onsubmit="return navTabSearch(this);"
		action="ssadmin/lendRuleList.html" method="post">
		<div class="searchBar">
			<table class="searchContent">
				<tr>
					<td></td>
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
<div class="pageContent">
	<div class="panelBar">
		<ul class="toolBar">
			 <li><a class="edit"
				href="ssadmin/goLendRuleJSP.html?url=ssadmin/updateLendRule&uid={sid_user}"
				height="300" width="800" target="dialog" rel="updateLendRule"><span>修改</span>
			</a></li>
		</ul>
	</div>
	<table class="table" width="100%" layoutH="138">
		<thead>
			<tr>
				<th width="20">序号</th>
				<th width="60">类型</th>
				<th width="60">等级</th>
				<th width="60">金额</th>
				<th width="60">利率</th>
				<th width="60">创建时间</th>
			</tr>
		</thead>
		<tbody>
			<c:forEach items="${lendRuleList}" var="lendRule" varStatus="num">
				<tr target="sid_user" rel="${lendRule.fid}">
					<td>${num.index +1}</td>
					<td>${lendRule.ftype_s}</td>
					<td>${lendRule.flevel_s}</td>
					<td>${lendRule.famount}</td>
					<td>${lendRule.frate}</td>
					<td>${lendRule.fcreate}</td>
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

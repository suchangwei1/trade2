<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="comm/include.inc.jsp"%>
<form id="pagerForm" method="post" action="ssadmin/betlogList.html">
	<input type="hidden" name="fstatus" value="${fstatus}"> <input
		type="hidden" name="term" value="${term}" /> <input
		type="hidden" name="pageNum" value="${currentPage}" /> <input
		type="hidden" name="numPerPage" value="${numPerPage}" /> <input
		type="hidden" name="orderField" value="${param.orderField}" /><input
		type="hidden" name="orderDirection" value="${param.orderDirection}" />
</form>


<div class="pageHeader">
	<form onsubmit="return navTabSearch(this);"
		action="ssadmin/betlogList.html" method="post">
		<div class="searchBar">

			<table class="searchContent">
				<tr>
					<td>期数：<input type="text" name="term" value="${term}"
						size="30" />
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
<div class="pageContent">
	<div class="panelBar">
		<ul class="toolBar">
		</ul>
	</div>
	<table class="table" width="100%" layoutH="138">
		<thead>
			<tr>
				<th width="20">期数</th>
				<th width="100">下注结果</th>
				<th width="100">下注金券</th>
				<th width="100">赢得金券</th>
				<th width="100">下注时间</th>
			</tr>
		</thead>
		<tbody>
			<c:forEach items="${betlogList}" var="betlogList" varStatus="num">
				<tr target="sid_user" rel="${betlogList.fid}">
					<td>${betlogList.fpopcornlogId}</td>
					<td>${betlogList.fbetResult_s}</td>
					<td>${betlogList.fbetqty}</td>
					<td>${betlogList.fwinqty}</td>
					<td>${betlogList.fcreatetime}</td>
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

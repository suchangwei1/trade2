<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="comm/include.inc.jsp"%>
<form id="pagerForm" method="post"
	action="ssadmin/lendSystemArgsList.html">
	<input type="hidden" name="status" value="${param.status}"> <input
		type="hidden" name="keywords" value="${keywords}" /> <input
		type="hidden" name="pageNum" value="${currentPage}" /> <input
		type="hidden" name="numPerPage" value="${numPerPage}" /> <input
		type="hidden" name="orderField" value="${param.orderField}" />
</form>


<div class="pageHeader">
	<form onsubmit="return navTabSearch(this);"
		action="ssadmin/lendSystemArgsList.html" method="post">
		<div class="searchBar">
			<table class="searchContent">
				<tr>
					<td>KEY：<input type="text" name="keywords" value="${keywords}"
						size="60" /></td>
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
			<li><a class="edit"
				href="ssadmin/goLendsystemargsJSP.html?url=ssadmin/updateLendSystemArgs&uid={sid_user}"
				height="400" width="800" target="dialog" rel="updateLendsystemargs"><span>修改</span>
			</a>
			</li>
		</ul>
	</div>
	<table class="table" width="100%" layoutH="138">
		<thead>
			<tr>
				<th width="20">序号</th>
				<th width="60">KEY</th>
				<th width="60">参数值</th>
				<th width="60">备注</th>
			</tr>
		</thead>
		<tbody>
			<c:forEach items="${lendSystemArgsList}" var="lendSystemArgs" varStatus="num">
				<tr target="sid_user" rel="${lendSystemArgs.fid}">
					<td>${num.index +1}</td>
					<td>${lendSystemArgs.fkey}</td>
					<td>${lendSystemArgs.fvalue_s}</td>
					<td>${lendSystemArgs.fdesc}</td>
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

<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="comm/include.inc.jsp"%>
<form id="pagerForm" method="post" action="ssadmin/popcornList.html">
	<input type="hidden" name="status" value="${param.status}"> <input
		type="hidden" name="keywords" value="${keywords}" /> <input
		type="hidden" name="pageNum" value="${currentPage}" /> <input
		type="hidden" name="numPerPage" value="${numPerPage}" /> <input
		type="hidden" name="orderField" value="${param.orderField}" /><input
		type="hidden" name="orderDirection" value="${param.orderDirection}" />
</form>


<div class="pageHeader">
	<form onsubmit="return navTabSearch(this);"
		action="ssadmin/popcornList.html" method="post">
		<div class="searchBar">

			<table class="searchContent">
				<tr>
					<td>开奖时间：<input type="text" name="keywords" value="${keywords}"
						size="60" />
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
				<li><a class="edit"
					href="ssadmin/goPopcornJSP.html?url=ssadmin/updatePopcorn&uid={sid_user}"
					height="400" width="900" target="dialog" rel="updatePopcorn"><span>修改</span>
				</a>
				</li>
		</ul>
	</div>
	<table class="table" width="100%" layoutH="138">
		<thead>
			<tr>
				<th width="20">序号</th>
				<th width="60">标题</th>
				<th width="70">开奖时间</th>
				<th width="70">开奖间隔时间</th>
				<th width="70">手续费比例</th>
				<th width="70">公司下注比例</th>
				<th width="100">创建时间</th>
			</tr>
		</thead>
		<tbody>
			<c:forEach items="${popcornList}" var="popcornList" varStatus="num">
				<tr target="sid_user" rel="${popcornList.fid}">
					<td>${num.index +1}</td>
					<td>${popcornList.ftitle}</td>
					<td>${popcornList.ftime}</td>
					<td>${popcornList.fspantime }</td>
					<td>${popcornList.frate }</td>
					<td>${popcornList.fbetRate }</td>
					<td>${popcornList.fcreatetime}</td>
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

<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="comm/include.inc.jsp"%>
<form id="pagerForm" method="post" action="ssadmin/viewUserWallet.html">
	<input type="hidden" name="status" value="${param.status}"> <input
		type="hidden" name="keywords" value="${keywords}" /><input
		type="hidden" name="cid" value="${cid}" /> <input type="hidden"
		name="pageNum" value="${currentPage}" /> <input type="hidden"
		name="numPerPage" value="${numPerPage}" /> <input type="hidden"
		name="orderField" value="${param.orderField}" /><input type="hidden"
		name="orderDirection" value="${param.orderDirection}" />
</form>


<div class="pageHeader">
	<form onsubmit="return dialogSearch(this);"
		action="ssadmin/viewUserWallet.html" method="post">
		<div class="searchBar">

			<table class="searchContent">
				<tr>
					<input type="hidden" name="cid" value="${cid}" />
					<td>会员信息：<input type="text" name="keywords"
						value="${keywords}" size="60" />
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
	<div class="panelBar"></div>
	<table class="table" width="100%" layoutH="138">
		<thead>
			<tr>
				<th width="20">序号</th>
				<th width="60">会员登陆名称</th>
				<th width="60">会员昵称</th>
				<th width="60">会员真实姓名</th>
				<th width="60">总金额</th>
				<th width="60">冻结金额</th>
				<th width="60">最后修改时间</th>
			</tr>
		</thead>
		<tbody>
			<c:forEach items="${userList}" var="user" varStatus="num">
				<tr target="sid_user" rel="${user.fwallet.fid}">
					<td>${num.index +1}</td>
					<td>${user.floginName}</td>
					<td>${user.fnickName}</td>
					<td>${user.frealName}</td>
					<td>${user.fwallet.ftotalRmb}</td>
					<td>${user.fwallet.ffrozenRmb}</td>
					<td>${user.fwallet.flastUpdateTime}</td>
				</tr>
			</c:forEach>
		</tbody>
	</table>

	<div class="panelBar">
		<div class="pages">
			<span>总共: ${totalCount}条</span>
		</div>
		<div class="pagination" targetType="dialogTab" totalCount="${totalCount}"
			numPerPage="${numPerPage}" pageNumShown="5"
			currentPage="${currentPage}"></div>
	</div>
</div>

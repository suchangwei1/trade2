<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="comm/include.inc.jsp"%>
<form id="pagerForm" method="post"
	action="ssadmin/subscriptionBuyLogList1.html">
	<input type="hidden" name="status" value="${param.status}"> <input
		type="hidden" name="keywords" value="${keywords}" /><input
		type="hidden" name="parentId" value="${parentId}" /><input
		type="hidden" name="pageNum" value="1" /> <input type="hidden"
		name="numPerPage" value="${numPerPage}" /> <input type="hidden"
		name="orderField" value="${param.orderField}" /><input type="hidden"
		name="orderDirection" value="${param.orderDirection}" />
</form>


<div class="pageHeader">
	<form onsubmit="return dialogSearch(this);"
		action="ssadmin/subscriptionBuyLogList1.html" method="post">
		<div class="searchBar">

			<table class="searchContent">
				<tr>
					<input type="hidden" name="parentId" value="${parentId}" />
					<td>标题：<input type="text" name="keywords" value="${keywords}"
						size="60" />[会员信息]</td>
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
	<div class="panelBar"></div>
	<table class="table" width="100%" layoutH="138">
		<thead>
			<tr>
				<th width="20">序号</th>
				<th width="60">会员登陆名</th>
				<th width="60">会员昵称</th>
				<th width="60">会员真实姓名</th>
				<th width="60">回购数量</th>
				<th width="60">回购价格</th>
				<th width="60">创建人</th>
				<th width="60">创建时间</th>
			</tr>
		</thead>
		<tbody>
			<c:forEach items="${subscriptionBuyLogList1}" var="subscriptionBuyLog"
				varStatus="num">
				<tr target="sid_user" rel="${subscriptionBuyLog.fid}">
					<td>${num.index +1}</td>
					<td>${subscriptionBuyLog.fsubscriptionlog.fuser.floginName}</td>
					<td>${subscriptionBuyLog.fsubscriptionlog.fuser.fnickName}</td>
					<td>${subscriptionBuyLog.fsubscriptionlog.fuser.frealName}</td>
					<td>${subscriptionBuyLog.fqty}</td>
					<td>${subscriptionBuyLog.fprice}</td>
					<td>${subscriptionBuyLog.fadmin.fname}</td>
					<td>${subscriptionBuyLog.fcreateTime}</td>
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

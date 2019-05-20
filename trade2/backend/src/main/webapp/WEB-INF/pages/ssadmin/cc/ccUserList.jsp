<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="../comm/include.inc.jsp"%>
<form id="pagerForm" method="post" action="ssadmin/ccUserList.html">
	<input type="hidden" name="pageNum"
		value="${currentPage}" /> <input type="hidden" name="numPerPage"
		value="${numPerPage}" /> <input type="hidden" name="orderField"
		value="${param.orderField}" /> <input type="hidden"
		name="orderDirection" value="${param.orderDirection}" />
</form>

<div class="pageContent">
	<div class="panelBar">
		<ul class="toolBar">
			<shiro:hasPermission name="ssadmin/cccheck.html">
				<li>
					<a class="add"
					   href="ssadmin/goCcJSP.html?url=ssadmin/cc/addCcUser"
					   height="300" width="800" target="dialog" rel="addLink"><span>新增</span>
				</a>
				</li>
			</shiro:hasPermission>
			<shiro:hasPermission name="ssadmin/cccheck.html">
				<li><a class="delete"
					href="ssadmin/ccUserDelet.html?uid={sid_user}"
					target="ajaxTodo" title="确定要删除吗?"><span>删除</span> </a></li>
			</shiro:hasPermission>
		</ul>
	</div>
	<table class="table" width="100%" layoutH="138">
		<thead>
			<tr>
				<th>商户UID</th>
				<th>商户名</th>
				<th>商户联系方式</th>
				<th>商户联系银行</th>
				<th>商户联系银行卡号</th>
			</tr>
		</thead>
		<tbody>
			<c:forEach items="${userList}" var="user" varStatus="num">
				<tr target="sid_user" rel="${user.id}">
					<td>${user.id}</td>
					<td>${user.name}</td>
					<td>${user.contactWay}</td>
					<td>${user.branch}</td>
					<td>${user.account}</td>
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

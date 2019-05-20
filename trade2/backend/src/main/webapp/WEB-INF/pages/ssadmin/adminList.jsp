<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="comm/include.inc.jsp"%>
<form id="pagerForm" method="post" action="ssadmin/adminList.html">
	<input type="hidden" name="status" value="${param.status}"> <input
		type="hidden" name="keywords" value="${keywords}" /> <input
		type="hidden" name="pageNum" value="${currentPage}" /> <input
		type="hidden" name="numPerPage" value="${numPerPage}" /> <input
		type="hidden" name="orderField" value="${param.orderField}" /><input
		type="hidden" name="orderDirection" value="${param.orderDirection}" />
</form>


<div class="pageHeader">
	<form onsubmit="return navTabSearch(this);"
		action="ssadmin/adminList.html" method="post">
		<div class="searchBar">

			<table class="searchContent">
				<tr>
					<td>管理员名称：<input type="text" name="keywords"
						value="${keywords}" size="60" /></td>
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
			<shiro:hasPermission name="ssadmin/saveAdmin.html">
				<li><a class="add"
					href="ssadmin/goAdminJSP.html?url=ssadmin/addAdmin" height="300"
					width="800" target="dialog" rel="addAdmin"><span>新增</span> </a></li>
			</shiro:hasPermission>
			<shiro:hasPermission name="ssadmin/forbbinAdmin.html?status=1">
				<li><a class="delete"
					href="ssadmin/forbbinAdmin.html?uid={sid_user}&status=1"
					target="ajaxTodo" title="确定要禁用吗?"><span>禁用</span> </a></li>
			</shiro:hasPermission>
			<shiro:hasPermission name="ssadmin/forbbinAdmin.html?status=2">
				<li><a class="edit"
					href="ssadmin/forbbinAdmin.html?uid={sid_user}&status=2"
					target="ajaxTodo" title="确定要解除吗?"><span>解除禁用</span> </a>
				</li>
			</shiro:hasPermission>
			<shiro:hasPermission name="ssadmin/updateAdmin.html">
				<li><a class="edit"
					href="ssadmin/goAdminJSP.html?url=ssadmin/updateAdmin&uid={sid_user}"
					height="300" width="800" target="dialog" rel="updateAdmin"><span>修改密码</span>
				</a></li>
			</shiro:hasPermission>
			<shiro:hasPermission name="ssadmin/updateAdminRole.html">
				<li><a class="edit"
					href="ssadmin/goAdminJSP.html?url=ssadmin/updateAdminRole&uid={sid_user}"
					height="200" width="800" target="dialog" rel="updateAdmin"><span>修改角色</span>
				</a></li>
			</shiro:hasPermission>
			<shiro:hasPermission name="ssadmin/updateAdminMobile.html">
				<li><a class="edit"
					href="ssadmin/goAdminJSP.html?url=ssadmin/updateAdminMobile&uid={sid_user}"
					height="200" width="800" target="dialog" rel="updateAdmin"><span>修改手机号</span>
				</a></li>
			</shiro:hasPermission>
		</ul>
	</div>
	<table class="table" width="100%" layoutH="138">
		<thead>
			<tr>
				<th width="20">序号</th>
				<th width="60">管理员名</th>
				<th width="60">手机号</th>
				<th width="60" orderField="fstatus"
					<c:if test='${param.orderField == "fstatus" }'> class="${param.orderDirection}"  </c:if>>状态</th>
				<th width="40">角色</th>
				<th width="40">创建日期</th>
			</tr>
		</thead>
		<tbody>
			<c:forEach items="${adminList}" var="admin" varStatus="num">
				<tr target="sid_user" rel="${admin.fid}">
					<td>${num.index +1}</td>
					<td>${admin.fname}</td>
					<td>${admin.mobile}</td>
					<td>${admin.fstatus_s}</td>
					<td>${admin.frole.fname}</td>
					<td>${admin.fcreateTime}</td>
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

<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="comm/include.inc.jsp"%>
<form id="pagerForm" method="post" action="ssadmin/apiList.html">
	<input type="hidden" name="status" value="${status}">
	<input type="hidden" name="total" value="${total}">
	<input type="hidden" name="type" value="${type}"> <input
		type="hidden" name="keyword" value="${keyword}" /><input
		type="hidden" name="pageNum" value="${currentPage}" /> <input
		type="hidden" name="orderField" value="${orderField}" /><input
		type="hidden" name="orderDirection" value="${orderDirection}" />
</form>

<div class="pageHeader">
	<form onsubmit="return navTabSearch(this);"
		action="ssadmin/apiList.html" method="post">
		<div class="searchBar">

			<table class="searchContent">
				<tr>
					<td>关键字：<input type="text" name="keyword" value="${keyword}"
						size="30" />[会员名称、姓名]</td>
					<td>权限： <select name="type">
						<option value="">请选择</option>
						<option value="0" <c:if test="${0 == type}">selected</c:if>>全部权限</option>
						<option value="1" <c:if test="${1 == type}">selected</c:if>>只读权限</option>
						<option value="2" <c:if test="${2 == type}">selected</c:if>>关闭</option>
					</select>
					</td>
					<td>状态： <select name="status">
						<option value="">请选择</option>
						<option value="0" <c:if test="${0 == status}">selected</c:if>>正常</option>
						<option value="1" <c:if test="${1 == status}">selected</c:if>>禁用</option>
					</select>
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
			<shiro:hasPermission name="ssadmin/updateApi.html?status=0">
				<li><a class="edit"
					href="ssadmin/updateApi.html?id={sid_user}&status=0"
					target="ajaxTodo" title="确定要解锁吗?"><span>解锁</span> </a>
				</li>
			</shiro:hasPermission>
			<shiro:hasPermission name="ssadmin/updateApi.html?status=1">
				<li><a class="edit"
					href="ssadmin/updateApi.html?id={sid_user}&status=1"
					target="ajaxTodo" title="确定要禁用吗?"><span>禁用</span> </a>
				</li>
			</shiro:hasPermission>
			<shiro:hasPermission name="ssadmin/lockAllApi.html">
				<li><a title="确实要锁定这些记录吗?" target="selectedTodo" rel="ids"
					postType="string" href="ssadmin/lockAllApi.html"
					class="edit"><span>全部禁用</span> </a>
				</li>
			</shiro:hasPermission>
		</ul>
	</div>
	<table class="table" width="120%" layoutH="138">
		<colgroup>
			<col width="15">
			<col width="60">
			<col width="60">
			<col width="30">
			<col width="30">
			<col width="30">
			<col width="100">
			<col width="100">
			<col width="50">
			<col width="50">
		</colgroup>
		<thead>
			<tr>
				<th><input type="checkbox" group="ids"
					class="checkboxCtrl">
				</th>
				<th orderField="u.floginName" <c:if test='${param.orderField == "u.floginName" }'> class="${param.orderDirection}"  </c:if>>会员登陆名</th>
				<th orderField="u.frealName" <c:if test='${param.orderField == "u.frealName" }'> class="${param.orderDirection}"  </c:if>>会员真实姓名</th>
				<th>权限</th>
				<th>状态</th>
				<th>API名称</th>
				<th>API key</th>
				<th>密钥</th>
				<th orderField="a.create_time" <c:if test='${param.orderField == "a.create_time" }'> class="${param.orderDirection}"  </c:if>>创建时间</th>
				<th orderField="a.update_time" <c:if test='${param.orderField == "a.update_time" }'> class="${param.orderDirection}"  </c:if>>修改时间</th>
			</tr>
		</thead>
		<tbody>
			<c:forEach items="${list}" var="line"
				varStatus="num">
				<tr target="sid_user" rel="${line.id}">
					<td><input name="ids" value="${line.id}"
						type="checkbox">
					</td>
					<td>${line.floginName}</td>
					<td>${line.frealName}</td>
					<td>${line.type.name}</td>
					<td>${line.status.name}</td>
					<td>${line.name}</td>
					<td>${line.apiKey}</td>
					<td>${line.secret}</td>
					<td><fmt:formatDate pattern="yyyy-MM-dd HH:mm:ss" value="${line.createTime}" /></td>
					<td><fmt:formatDate pattern="yyyy-MM-dd HH:mm:ss" value="${line.updateTime}" /></td>
				</tr>
			</c:forEach>
		</tbody>
	</table>

	<div class="panelBar">
		<div class="pages">
			<span>总共: ${total}条</span>
		</div>
		<div class="pagination" targetType="navTab" totalCount="${total}"
			numPerPage="${numPerPage}" pageNumShown="5"
			currentPage="${currentPage}"></div>
	</div>
</div>

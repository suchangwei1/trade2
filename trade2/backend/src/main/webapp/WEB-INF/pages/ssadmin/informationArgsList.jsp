<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="comm/include.inc.jsp"%>
<form id="pagerForm" method="post" action="/ssadmin/informationArgsList.html">
</form>
<div class="pageContent">
	<div class="panelBar">
		<ul class="toolBar">
			<shiro:hasPermission name="/ssadmin/saveInformationArgs.html">
				<li><a class="add" href="/ssadmin/goInformationArgsJsp.html?url=ssadmin/addInformationArgs"
					height="400" width="800" target="dialog" rel="addAricle"><span>新增</span> </a></li>
			</shiro:hasPermission>
			<%--<shiro:hasPermission name="/ssadmin/deleteInformationArgs.html">--%>
				<%--<li><a class="delete" href="/ssadmin/deleteInformationArgs.html?key={sid_user}"--%>
					<%--target="ajaxTodo" title="确定要删除吗?"><span>删除</span> </a></li>--%>
			<%--</shiro:hasPermission>--%>
			<shiro:hasPermission name="/ssadmin/updateInformationArgs.html">
				<li><a class="edit"
					href="/ssadmin/goInformationArgsJsp.html?url=ssadmin/updateInformationArgs&key={sid_user}" height="400"
					width="800" target="dialog" rel="updateInformationArgs"><span>修改</span> </a></li>
			</shiro:hasPermission>
		</ul>
	</div>
	<table class="table" width="100%" layoutH="138">
		<thead>
			<tr>
				<th width="20">序号</th>
				<th width="60">标题</th>
				<th width="60">内容</th>
			</tr>
		</thead>
		<tbody>
			<c:forEach items="${map}" var="item" varStatus="num">
				<tr target="sid_user" rel="${item.key}">
					<td>${num.index +1}</td>
					<td>${item.key}</td>
					<td>${item.value}</td>
				</tr>
			</c:forEach>
		</tbody>
	</table>
</div>

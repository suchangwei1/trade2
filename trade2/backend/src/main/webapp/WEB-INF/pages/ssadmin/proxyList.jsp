<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="comm/include.inc.jsp"%>
<form id="pagerForm" method="post" action="ssadmin/proxyList.html">
</form>


<div class="pageHeader">
	<form onsubmit="return navTabSearch(this);"
		action="ssadmin/proxyList.html" method="post">
		<div class="searchBar">

			<div class="subBar">
				<ul>
				</ul>
			</div>
		</div>
	</form>
</div>
<div class="pageContent">
	<div class="panelBar">
		<ul class="toolBar">
				<li><a class="add"
					href="ssadmin/goProxyJSP.html?url=ssadmin/addProxy"
					height="350" width="800" target="dialog" rel="addProxy"><span>新增</span>
				</a>
				</li>
				<li><a class="delete"
					href="ssadmin/deleteProxy.html?uid={sid_user}" target="ajaxTodo"
					title="确定要删除吗?"><span>删除</span> </a>
				</li>
				<li><a class="edit"
					href="ssadmin/goProxyJSP.html?url=ssadmin/updateProxy&uid={sid_user}"
					height="350" width="800" target="dialog" rel="updateProxy"><span>修改</span>
				</a>
				</li>
		</ul>
	</div>
	<table class="table" width="100%" layoutH="138">
		<thead>
			<tr>
				<th width="100">序号</th>
				<th width="100">代理名称</th>
				<th width="100">保证金</th>
				<th width="100">QQ</th>
				<th width="100">姓名</th>
				<th width="300">账号</th>
			</tr>
		</thead>
		<tbody>
			<c:forEach items="${proxyList}" var="v" varStatus="vs">
				<tr target="sid_user" rel="${v.fid}">
					<td>${vs.index +1}</td>
					<td>${v.name}</td>
					<td>${v.amount}</td>
					<td>${v.qq}</td>
					<td>${v.realname}</td>
					<td>${v.account}</td>
				</tr>
			</c:forEach>
		</tbody>
	</table>

	<div class="panelBar">
		<div class="pages">
		</div>
	</div>
</div>

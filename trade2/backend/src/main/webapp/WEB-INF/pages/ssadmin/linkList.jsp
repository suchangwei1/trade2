<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="comm/include.inc.jsp"%>
<form id="pagerForm" method="post" action="ssadmin/linkList.html">
	<input type="hidden" name="status" value="${param.status}"> <input
		type="hidden" name="keywords" value="${keywords}" /> <input
		type="hidden" name="pageNum" value="${currentPage}" /> <input
		type="hidden" name="numPerPage" value="${numPerPage}" /> 
		<input type="hidden" name="orderField" value="${param.orderField}" />
		<input type="hidden" name="orderDirection" value="${param.orderDirection}" />
</form>


<div class="pageHeader">
	<form onsubmit="return navTabSearch(this);"
		action="ssadmin/linkList.html" method="post">
		<div class="searchBar">

			<table class="searchContent">
				<tr>
					<td>关键字：<input type="text" name="keywords" value="${keywords}"
						size="60" />[名称、URL]</td>
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
			<shiro:hasPermission name="ssadmin/saveLink.html">
				<li><a class="add"
					href="ssadmin/goLinkJSP.html?url=ssadmin/addLink&type=1"
					height="300" width="800" target="dialog" rel="addLink"><span>新增</span>
				</a>
				</li>
			</shiro:hasPermission>
			<shiro:hasPermission name="ssadmin/deleteLink.html">
				<li><a class="delete"
					href="ssadmin/deleteLink.html?uid={sid_user}" target="ajaxTodo"
					title="确定要删除吗?"><span>删除</span> </a>
				</li>
			</shiro:hasPermission>
			<shiro:hasPermission name="ssadmin/updateLink.html">
				<li><a class="edit"
					href="ssadmin/goLinkJSP.html?url=ssadmin/updateLink&uid={sid_user}"
					height="300" width="800" target="dialog" rel="updateLink"><span>修改</span>
				</a>
				</li>
			</shiro:hasPermission>
		</ul>
	</div>
	<table class="table" width="100%" layoutH="138">
		<thead>
			<tr>
				<th width="20">序号</th>
				<th width="60">名称</th>
				<th width="60" orderField="ftype"
                    <c:if test='${param.orderField == "ftype" }'> class="${param.orderDirection}"  </c:if>>类型</th>
				<th width="60">链接</th>
				<th width="60" orderField="forder"
                    <c:if test='${param.orderField == "forder" }'> class="${param.orderDirection}"  </c:if>>顺序</th>
				<th width="60">描述</th>
				<th width="60" orderField="fcreateTime"
                    <c:if test='${param.orderField == "fcreateTime" }'> class="${param.orderDirection}"  </c:if>>创建时间</th>
			</tr>
		</thead>
		<tbody>
			<c:forEach items="${linkList}" var="link" varStatus="num">
				<tr target="sid_user" rel="${link.fid}">
					<td>${num.index +1}</td>
					<td>${link.fname}</td>
					<td>${link.ftype_s}</td>
					<td>${link.furl}</td>
					<td>${link.forder}</td>
					<td>${link.fdescription}</td>
					<td>${link.fcreateTime}</td>
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

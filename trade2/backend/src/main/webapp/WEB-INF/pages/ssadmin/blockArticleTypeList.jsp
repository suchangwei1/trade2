<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="comm/include.inc.jsp"%>
<form id="pagerForm" method="post" action="/ssadmin/blockArticleTypeList.html">
	<input type="hidden" name="status" value="${param.status}"> <input type="hidden"
		name="keywords" value="${keywords}" /> <input type="hidden" name="pageNum" value="${currentPage}" />
	<input type="hidden" name="numPerPage" value="${numPerPage}" /> <input type="hidden"
		name="orderField" value="${param.orderField}" /><input type="hidden" name="orderDirection"
		value="${param.orderDirection}" />
</form>


<div class="pageHeader">
	<form onsubmit="return navTabSearch(this);" action="/ssadmin/blockArticleTypeList.html" method="post">
		<div class="searchBar">

			<table class="searchContent">
				<tr>
					<td>关键字：<input type="text" name="keywords" value="${keywords}" size="60" />[名称、关键词、描述]
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
			<shiro:hasPermission name="/ssadmin/saveBlockArticleType.html">
				<li><a class="add" href="/ssadmin/goBlockArticleTypeJSP.html?url=/ssadmin/addBlockArticleType"
					height="300" width="800" target="dialog" rel="addBlockAricleType"><span>新增</span> </a></li>
			</shiro:hasPermission>
			<shiro:hasPermission name="/ssadmin/deleteBlockArticleType.html">
				<li><a class="delete" href="/ssadmin/deleteBlockArticleType.html?id={sid_user}"
					target="ajaxTodo" title="确定要删除吗?"><span>删除</span> </a></li>
			</shiro:hasPermission>
			<shiro:hasPermission name="/ssadmin/updateBlockArticleType.html">
				<li><a class="edit"
					href="ssadmin/goBlockArticleTypeJSP.html?url=/ssadmin/updateBlockArticleType&id={sid_user}" height="300"
					width="800" target="dialog" rel="updateBlockAricleType"><span>修改</span> </a></li>
			</shiro:hasPermission>
		</ul>
	</div>
	<table class="table" width="100%" layoutH="138">
		<thead>
			<tr>
				<th width="20">序号</th>
				<th width="60" orderField="name"
					<c:if test='${param.orderField == "name" }'> class="${param.orderDirection}"  </c:if>>类型名称</th>
				<th width="60" orderField="createTime"
					<c:if test='${param.orderField == "createTime" }'> class="${param.orderDirection}"  </c:if>>创建时间</th>
				<th width="60" orderField="lastUpdateTime"
					<c:if test='${param.orderField == "lastUpdateTime" }'> class="${param.orderDirection}"  </c:if>>修改时间</th>
			</tr>
		</thead>
		<tbody>
			<c:forEach items="${blockArticleTypeList}" var="blockArticleType" varStatus="num">
				<tr target="sid_user" rel="${blockArticleType.id}">
					<td>${num.index +1}</td>
					<td>${blockArticleType.name}</td>
					<td>${blockArticleType.createTime}</td>
					<td>${blockArticleType.lastUpdateTime}</td>
				</tr>
			</c:forEach>
		</tbody>
	</table>

	<div class="panelBar">
		<div class="pages">
			<span>总共: ${totalCount}条</span>
		</div>
		<div class="pagination" targetType="navTab" totalCount="${totalCount}" numPerPage="${numPerPage}"
			pageNumShown="5" currentPage="${currentPage}"></div>
	</div>
</div>

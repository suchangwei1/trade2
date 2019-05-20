<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="comm/include.inc.jsp"%>
<form id="pagerForm" method="post" action="ssadmin/systemArgsList.html">
	<input type="hidden" name="status" value="${param.status}"> <input
		type="hidden" name="keywords" value="${keywords}" /> <input
		type="hidden" name="pageNum" value="${currentPage}" /> <input
		type="hidden" name="numPerPage" value="${numPerPage}" /> 
		<input type="hidden" name="orderField" value="${param.orderField}" />
		<input type="hidden" name="orderDirection" value="${param.orderDirection}" />
</form>


<div class="pageHeader">
	<form onsubmit="return navTabSearch(this);"
		action="ssadmin/systemArgsList.html" method="post">
		<div class="searchBar">

			<table class="searchContent">
				<tr>
					<td>KEY：<input type="text" name="keywords" value="${keywords}"
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
			<shiro:hasPermission name="ssadmin/saveSystemArgs.html">
				<li><a class="add"
					href="ssadmin/goArticleJSP.html?url=ssadmin/addSystemArgs"
					height="400" width="800" target="dialog" rel="addSystemArgs"><span>新增</span>
				</a></li>
			</shiro:hasPermission>
			<shiro:hasPermission name="ssadmin/updateSystemArgs.html">
				<li><a class="edit"
					href="ssadmin/goSystemArgsJSP.html?url=ssadmin/updateSystemArgs&uid={sid_user}"
					height="400" width="800" target="dialog" rel="updateSystemArgs"><span>修改</span>
				</a></li>
			</shiro:hasPermission>
		</ul>
	</div>
	<table class="table" width="100%" layoutH="138">
		<thead>
			<tr>
				<th width="20">序号</th>
				<th width="60" orderField="fkey"
                    <c:if test='${param.orderField == "fkey" }'> class="${param.orderDirection}"  </c:if>>KEY</th>
				<th width="60">URL</th>
				<th width="60">参数值</th>
				<th width="60">备注</th>
			</tr>
		</thead>
		<tbody>
			<c:forEach items="${systemArgsList}" var="systemArgs" varStatus="num">
				<tr target="sid_user" rel="${systemArgs.fid}">
					<td>${num.index +1}</td>
					<td>${systemArgs.fkey}</td>
					<td>${systemArgs.furl}</td>
					<td>${systemArgs.fvalue_s}</td>
					<td>${systemArgs.fdescription}</td>
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

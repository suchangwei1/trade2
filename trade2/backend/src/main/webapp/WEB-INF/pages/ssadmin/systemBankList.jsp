<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="comm/include.inc.jsp"%>
<form id="pagerForm" method="post" action="ssadmin/systemBankList.html">
	<input type="hidden" name="status" value="${param.status}"> <input
		type="hidden" name="keywords" value="${keywords}" /> <input
		type="hidden" name="pageNum" value="${currentPage}" /> <input
		type="hidden" name="numPerPage" value="${numPerPage}" /> 
		<input type="hidden" name="orderField" value="${param.orderField}" />
		<input type="hidden" name="orderDirection" value="${param.orderDirection}" />
</form>


<div class="pageHeader">
	<form onsubmit="return navTabSearch(this);"
		action="ssadmin/systemBankList.html" method="post">
		<div class="searchBar">

			<table class="searchContent">
				<tr>
					<td>标题：<input type="text" name="keywords" value="${keywords}"
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
			<shiro:hasPermission name="ssadmin/saveSystemBank.html">
				<li><a class="add"
					href="ssadmin/goSystemBankJSP.html?url=ssadmin/addSystemBank"
					height="300" width="800" target="dialog" rel="addSystemBankList"><span>新增</span>
				</a></li>
			</shiro:hasPermission>
			<shiro:hasPermission name="ssadmin/forbbinSystemBank.html?status=1">
				<li><a class="delete"
					href="ssadmin/forbbinSystemBank.html?uid={sid_user}&status=1"
					target="ajaxTodo" title="确定要禁用吗?"><span>禁用</span> </a></li>
			</shiro:hasPermission>
			<shiro:hasPermission name="ssadmin/forbbinSystemBank.html?status=2">
				<li><a class="edit"
					href="ssadmin/forbbinSystemBank.html?uid={sid_user}&status=2"
					target="ajaxTodo" title="确定要解除禁用吗?"><span>解除禁用</span> </a></li>
			</shiro:hasPermission>
		</ul>
	</div>
	<table class="table" width="100%" layoutH="138">
		<thead>
			<tr>
				<th width="20">序号</th>
				<th width="60" orderField="fbankName"
                    <c:if test='${param.orderField == "fbankName" }'> class="${param.orderDirection}"  </c:if>>银行名称</th>
				<th width="60" orderField="fstatus"
                    <c:if test='${param.orderField == "fstatus" }'> class="${param.orderDirection}"  </c:if>>状态</th>
				<th width="60">开户姓名</th>
				<th width="60">开户地址</th>
				<th width="60" orderField="fbankNumber"
                    <c:if test='${param.orderField == "fbankNumber" }'> class="${param.orderDirection}"  </c:if>>银行卡号</th>
				<th width="60" orderField="fcreateTime"
                    <c:if test='${param.orderField == "fcreateTime" }'> class="${param.orderDirection}"  </c:if>>创建时间</th>
			</tr>
		</thead>
		<tbody>
			<c:forEach items="${systembankList}" var="systembank" varStatus="num">
				<tr target="sid_user" rel="${systembank.fid}">
					<td>${num.index +1}</td>
					<td>${systembank.fbankName}</td>
					<td>${systembank.fstatus_s}</td>
					<td>${systembank.fownerName}</td>
					<td>${systembank.fbankAddress}</td>
					<td>${systembank.fbankNumber}</td>
					<td>${systembank.fcreateTime}</td>
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

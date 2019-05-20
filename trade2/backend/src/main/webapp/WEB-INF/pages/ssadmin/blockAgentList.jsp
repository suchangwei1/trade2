<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="comm/include.inc.jsp"%>
<form id="pagerForm" method="post" action="ssadmin/blockAgentList.html">
	<input type="hidden" name="status" value="${param.status}"> <input
		type="hidden" name="keywords" value="${keywords}" /> <input
		type="hidden" name="status" value="${param.status}" /> <input
		type="hidden" name="pageNum" value="${currentPage}" /> <input
		type="hidden" name="numPerPage" value="${numPerPage}" /> <input
		type="hidden" name="orderField" value="${param.orderField}" /><input
		type="hidden" name="orderDirection" value="${param.orderDirection}" />
</form>


<div class="pageHeader">
	<form onsubmit="return navTabSearch(this);"
		action="ssadmin/blockAgentList.html" method="post">
		<div class="searchBar" style="float: left;">
			关键字：<input type="text" name="keywords" value="${keywords}" size="60" />
			<select name="status">
				<option value="">审核状态</option>
				<option <c:if test="${0 == param.status}">selected</c:if> value="0">未审核</option>
				<option <c:if test="${1 == param.status}">selected</c:if> value="1">未通过</option>
				<option <c:if test="${2 == param.status}">selected</c:if> value="2">已通过</option>
			</select>
		</div>
		<div class="buttonActive">
			<div class="buttonContent">
				<button type="submit">查询</button>
			</div>
		</div>
	</form>
</div>
<div class="pageContent">
	<div class="panelBar">
		<ul class="toolBar">
			<shiro:hasPermission name="ssadmin/auditBlockAgent.html">
				<li><a class="edit"
					   href="/ssadmin/auditBlockAgent.html?id={sid_user}&status=1" rel="blockAgentList" target="ajaxTodo"><span>审核不通过</span> </a></li>
			</shiro:hasPermission>
			<shiro:hasPermission name="ssadmin/auditBlockAgent.html">
				<li><a class="edit"
					   href="/ssadmin/auditBlockAgent.html?id={sid_user}&status=2" rel="blockAgentList" target="ajaxTodo"><span>审核通过</span> </a></li>
			</shiro:hasPermission>
			<shiro:hasPermission name="ssadmin/exportBlockAgentList.html">
				<li><a class="icon"
					   href="/ssadmin/exportBlockAgentList.html" rel="exportBlockAgentList" target="dwzExport" targetType="navTab"><span>导出</span> </a></li>
			</shiro:hasPermission>
		</ul>
	</div>
	<table class="table" width="100%" layoutH="138">
		<thead>
			<tr>
				<th width="20">序号</th>
				<th width="60" orderField="realName" <c:if test='${param.orderField == "realName" }'> class="${param.orderDirection}"</c:if>>真实姓名</th>
				<th width="60" orderField="sex" <c:if test='${param.orderField == "sex" }'> class="${param.orderDirection}"</c:if>>性别</th>
				<th width="60" orderField="mobile" <c:if test='${param.orderField == "mobile" }'> class="${param.orderDirection}"</c:if>>手机号</th>
				<th width="60" orderField="email" <c:if test='${param.orderField == "email" }'> class="${param.orderDirection}"</c:if>>邮箱</th>
				<th width="60" orderField="wechatNo" <c:if test='${param.orderField == "wechatNo" }'> class="${param.orderDirection}"</c:if>>微信号</th>
				<th width="60" orderField="identifyNo" <c:if test='${param.orderField == "identifyNo" }'> class="${param.orderDirection}"</c:if>>身份证号</th>
				<th width="40" orderField="job" <c:if test='${param.orderField == "job" }'> class="${param.orderDirection}"</c:if>>职业</th>
				<th width="40" orderField="company" <c:if test='${param.orderField == "company" }'> class="${param.orderDirection}"</c:if>>公司</th>
				<th width="40">公司地址</th>
				<th width="40" orderField="status" <c:if test='${param.orderField == "status" }'> class="${param.orderDirection}"</c:if>>审核状态</th>
				<th width="40" orderField="createTime" <c:if test='${param.orderField == "createTime" }'> class="${param.orderDirection}"</c:if>>创建时间</th>
				<th width="40" orderField="updateTime" <c:if test='${param.orderField == "updateTime" }'> class="${param.orderDirection}"</c:if>>更新时间</th>
			</tr>
		</thead>
		<tbody>
			<c:forEach items="${list}" var="agent" varStatus="num">
				<tr target="sid_user" rel="${agent.id}">
					<td>${num.index +1}</td>
					<td>${agent.realName}</td>
					<td>
						<c:choose>
							<c:when test="${0 == agent.sex}">女</c:when>
							<c:when test="${1 == agent.sex}">男</c:when>
						</c:choose>
					</td>
					<td>${agent.mobile}</td>
					<td>${agent.email}</td>
					<td>${agent.wechatNo}</td>
					<td>${agent.identifyNo}</td>
					<td>${agent.job}</td>
					<td>${agent.company}</td>
					<td>${agent.companyAddress}</td>
					<td>${agent.status.name}</td>
					<td><fmt:formatDate value="${agent.createTime}" pattern="yyyy-MM-dd HH:mm:ss"/></td>
					<td><fmt:formatDate value="${agent.updateTime}" pattern="yyyy-MM-dd HH:mm:ss"/></td>
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

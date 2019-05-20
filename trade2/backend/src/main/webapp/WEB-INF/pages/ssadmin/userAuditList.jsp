<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="comm/include.inc.jsp"%>
<form id="pagerForm" method="post" action="ssadmin/userAuditList.html">
	<input type="hidden" name="status" value="${param.status}"> <input
		type="hidden" name="keywords" value="${keywords}" /> <input
		type="hidden" name="pageNum" value="${currentPage}" /> <input
		type="hidden" name="numPerPage" value="${numPerPage}" /> <input
		type="hidden" name="orderField" value="${param.orderField}" /> <input
		type="hidden" name="orderDirection" value="${param.orderDirection}" />
</form>


<div class="pageHeader">
	<form onsubmit="return navTabSearch(this);"
		action="ssadmin/userAuditList.html" method="post">
		<div class="searchBar">

			<table class="searchContent">
				<tr>
					<td>会员信息：<input type="text" name="keywords" value="${keywords}"
						size="60" /></td>
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
			<shiro:hasPermission name="ssadmin/auditUser.html">
				<%--<li><a class="edit"
					href="ssadmin/goUserJSP.html?uid={sid_user}&url=ssadmin/auditUser&status=audit"
					target="dialog" rel=auditUser height="600" width="800"><span>审核证件信息</span>
				</a>
				</li>--%>
			</shiro:hasPermission>
			<shiro:hasPermission name="ssadmin/auditIdentify.html">
				<li><a class="edit"
					href="ssadmin/goUserJSP.html?uid={sid_user}&url=ssadmin/auditIdentify&status=audit"
					target="dialog" rel=auditIdentify height="600" width="800"><span>审核证件照片</span>
				</a>
				</li>
			</shiro:hasPermission>
		</ul>
	</div>
	<table class="table" width="120%" layoutH="138">
		<thead>
			<tr>
				<th width="20">序号</th>
				<th width="100" orderField="floginName"
					<c:if test='${param.orderField == "floginName" }'> class="${param.orderDirection}"  </c:if>>会员登陆名</th>
				<th width="60" orderField="fstatus"
					<c:if test='${param.orderField == "fstatus" }'> class="${param.orderDirection}"  </c:if>>会员状态</th>
				<th width="60">昵称</th>
				<th width="60">证件信息审核</th>
				<th width="60">证件照片审核</th>
				<th width="60">电话号码</th>
				<th width="60">邮箱地址</th>
				<th width="60">证件类型</th>
				<th width="60">真实姓名</th>
				<th width="60">证件号码</th>
				<th width="60" orderField="fregisterTime"
					<c:if test='${param.orderField == "fregisterTime" }'> class="${param.orderDirection}"  </c:if>>注册时间</th>
				<th width="60" orderField="flastLoginTime"
					<c:if test='${param.orderField == "flastLoginTime" }'> class="${param.orderDirection}"  </c:if>>上次登陆时间</th>
			</tr>
		</thead>
		<tbody>
			<c:forEach items="${userList}" var="user" varStatus="num">
				<tr target="sid_user" rel="${user.fid}">
					<td>${num.index +1}</td>
					<td>${user.floginName}</td>
					<td>${user.fstatus_s}</td>
					<td>${user.fnickName}</td>
					<td>
						待审核
						<%--<c:choose>
							<c:when test="${user.fhasRealValidate}">
								已通过
							</c:when>
							<c:when test="${!user.fhasRealValidate && user.fpostRealValidate}">
								已提交
							</c:when>
							<c:otherwise>
								未提交
							</c:otherwise>
						</c:choose>--%>
					</td>
					<td>
						<c:choose>
							<c:when test="${0 == user.fIdentityStatus}">
								未提交
							</c:when>
							<c:when test="${1 == user.fIdentityStatus}">
								已提交
							</c:when>
							<c:when test="${2 == user.fIdentityStatus}">
								已通过
							</c:when>
							<c:when test="${3 == user.fIdentityStatus}">
								未通过
							</c:when>
						</c:choose>
					</td>
					<td>${user.ftelephone}</td>
					<td>${user.femail}</td>
					<td>${user.fidentityType_s}</td>
					<td>${user.frealName}</td>
					<td>${user.fidentityNo}</td>
					<td>${user.fregisterTime}</td>
					<td>${user.flastLoginTime}</td>
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

<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="comm/include.inc.jsp"%>
<form id="pagerForm" method="post"
	action="ssadmin/gameoperatelogList.html">
	<input type="hidden" name="status" value="${param.status}"> <input
		type="hidden" name="keywords" value="${keywords}" /><input
		type="hidden" name="user" value="${user}" /> <input type="hidden"
		name="pageNum" value="${currentPage}" /> <input type="hidden"
		name="numPerPage" value="${numPerPage}" /> <input type="hidden"
		name="orderField" value="${param.orderField}" /><input type="hidden"
		name="orderDirection" value="${param.orderDirection}" />
</form>


<div class="pageHeader">
	<form onsubmit="return navTabSearch(this);"
		action="ssadmin/gameoperatelogList.html" method="post">
		<div class="searchBar">
			<table class="searchContent">
				<tr>
					<td>类型 <select type="combox" name="keywords">
							<c:forEach items="${typeMap}" var="type">
								<c:if test="${type.key == keywords}">
									<option value="${type.key}" selected="true">${type.value}</option>
								</c:if>
								<c:if test="${type.key != keywords}">
									<option value="${type.key}">${type.value}</option>
								</c:if>
							</c:forEach>
					</select></td>
					<td>会员信息：<input type="text" name="user" size="30" value="${user }"/>
					</td>
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

		</ul>
	</div>
	<table class="table" width="100%" layoutH="138">
		<thead>
			<tr>
				<th width="20">序号</th>
				<th width="60" orderField="fuser.floginName" <c:if test='${param.orderField == "fuser.floginName" }'> class="${param.orderDirection}"  </c:if>>会员登陆名</th>
				<th width="60" orderField="fuser.fnickName"
					<c:if test='${param.orderField == "fuser.fnickName" }'> class="${param.orderDirection}"  </c:if>>会员昵称</th>
				<th width="60" orderField="fuser.frealName"
					<c:if test='${param.orderField == "fuser.frealName" }'> class="${param.orderDirection}"  </c:if>>会员真实姓名</th>
				<th width="30" orderField="fgamerule.flevel"
					<c:if test='${param.orderField == "fgamerule.flevel" }'> class="${param.orderDirection}"  </c:if>>参与等级</th>
				<th width="30">类型</th>
				<th width="30">数量</th>
				<th width="40" orderField="fcreateTime"
					<c:if test='${param.orderField == "fcreateTime" }'> class="${param.orderDirection}"  </c:if>>创建时间</th>
			</tr>
		</thead>
		<tbody>
			<c:forEach items="${gameoperatelogList}" var="gameoperatelog"
				varStatus="num">
				<tr target="sid_user" rel="${gameoperatelog.fid}">
					<td>${num.index +1}</td>
					<td>${gameoperatelog.fuser.floginName}</td>
					<td>${gameoperatelog.fuser.fnickName}</td>
					<td>${gameoperatelog.fuser.frealName}</td>
					<td>${gameoperatelog.fgamerule.flevel}</td>
					<td>${gameoperatelog.ftype_s}</td>
					<td>${gameoperatelog.fqty}</td>
					<td>${gameoperatelog.fcreateTime}</td>
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

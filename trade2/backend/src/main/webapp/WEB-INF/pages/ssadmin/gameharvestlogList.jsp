<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="comm/include.inc.jsp"%>
<form id="pagerForm" method="post" action="ssadmin/gameharvestlogList.html">
	<input type="hidden" name="status" value="${param.status}"> <input
		type="hidden" name="keywords" value="${keywords}" /> <input
		type="hidden" name="pageNum" value="${currentPage}" /> <input
		type="hidden" name="numPerPage" value="${numPerPage}" /> <input
		type="hidden" name="orderField" value="${param.orderField}" /><input
		type="hidden" name="orderDirection" value="${param.orderDirection}" />
</form>


<div class="pageHeader">
	<form onsubmit="return navTabSearch(this);"
		action="ssadmin/gameharvestlogList.html" method="post">
		<div class="searchBar">
			<table class="searchContent">
				<tr>
					<td>会员信息：<input type="text" name="keywords"
						value="${keywords}" size="60" /></td>
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
				<th width="60">本人登陆名</th>
                <th width="60">推荐人登陆名</th>
				<th width="60">土地编码</th>
				<th width="60">已收获数量</th>
				<th width="60">冻结数量</th>
				<th width="60">状态</th>
				<th width="60">类型</th>
				<th width="60" orderField="fcreateTime"
					<c:if test='${param.orderField == "fcreateTime" }'> class="${param.orderDirection}"  </c:if>>创建时间</th>
			</tr>
		</thead>
		<tbody>
			<c:forEach items="${gameharvestlogList}" var="gameharvestlog" varStatus="num">
				<tr target="sid_user" rel="${gameharvestlog.fid}">
					<td>${num.index +1}</td>
					<td>${gameharvestlog.fuser.floginName}</td>
					<td>${gameharvestlog.fintrolUser.floginName}</td>
					<td>${gameharvestlog.fgroupqty}</td>
					<td>${gameharvestlog.fharvestqty}</td>
					<td>${gameharvestlog.ffrozenqty}</td>
					<td>${gameharvestlog.fstatus_s}</td>
					<td>${gameharvestlog.ftype_s}</td>
					<td>${gameharvestlog.fcreatetime}</td>
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

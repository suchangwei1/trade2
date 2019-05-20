<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="comm/include.inc.jsp"%>
<form id="pagerForm" method="post" action="ssadmin/usergameList.html">
	<input type="hidden" name="status" value="${param.status}"> <input
		type="hidden" name="keywords" value="${keywords}" />
		<input type="hidden" name="pageNum"
		value="${currentPage}" /> <input type="hidden" name="numPerPage"
		value="${numPerPage}" /> <input type="hidden" name="orderField"
		value="${param.orderField}" /> <input type="hidden"
		name="orderDirection" value="${param.orderDirection}" />
</form>


<div class="pageHeader">
	<form onsubmit="return navTabSearch(this);"
		action="ssadmin/usergameList.html" method="post">
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
						</div></li>
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
				<th width="40">会员UID</th>
				<th width="40">推荐人UID</th>
				<th width="60">会员登陆名</th>
				<th width="60">真实姓名</th>
				<th width="60">摇钱树累计已发放</th>
				<th width="60">摇钱树累计已冻结</th>
				<th width="60">推荐累计已发放</th>
				<th width="60">推荐累计已冻结</th>
				<th width="60">租用土地消耗FC</th>
				<th width="60">土地数量</th>
				<th width="60">种树数量</th>
			</tr>
		</thead>
		<tbody>
			<c:forEach items="${usergameList}" var="user" varStatus="num">
				<tr target="sid_user">
				    <c:forEach items="${user }" var="v">
					<td>${v }</td>
					</c:forEach>
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

<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="../comm/include.inc.jsp"%>
<form id="pagerForm" method="post"
	action="/ssadmin/coinVoteLogList.html">
	<input type="hidden" name="status" value="${param.status}"> <input
		type="hidden" name="keywords" value="${keywords}" /> <input
		type="hidden" name="pageNum" value="${currentPage}" /> <input
		type="hidden" name="numPerPage" value="${numPerPage}" /> <input
		type="hidden" name="orderField" value="${param.orderField}" /><input
		type="hidden" name="orderDirection" value="${param.orderDirection}" />
</form>

<div class="pageHeader">
	<form onsubmit="return navTabSearch(this);"
		action="/ssadmin/coinVoteLogList.html" method="post">
		<div class="searchBar">

			<table class="searchContent">
				<tr>
					<td>关键字：<input type="text" name="keywords" value="${keywords}"
						size="60" />[名称、简称、描述]</td>
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
	</div>
	<table class="table" width="100%" layoutH="138">
		<thead>
			<tr>
				<th width="20">序号</th>
				<th width="20" orderField="fuser.fid"
                    <c:if test='${param.orderField == "fuser.fid" }'> class="${param.orderDirection}"  </c:if>>用户ID</th>
				<th width="30">用户名</th>
				<th width="60">虚拟币名称</th>
				<th width="60">投票</th>
				<th width="60" orderField="fcreatetime"
                    <c:if test='${param.orderField == "fcreatetime" }'> class="${param.orderDirection}"  </c:if>>时间</th>
			</tr>
		</thead>
		<tbody>
			<c:forEach items="${coinVoteLogList}" var="virtualCoinType"
				varStatus="num">
				<tr target="sid_user" rel="${virtualCoinType.fid}">
					<td>${num.index +1}</td>
					<td>${virtualCoinType.fuser.fid}</td>
					<td>${virtualCoinType.fuser.floginName}</td>
					<td>${virtualCoinType.fcoinvote.fshortName}</td>
					<td>${virtualCoinType.vote==-1?'反对':'赞成'}</td>
					<td>${virtualCoinType.fcreatetime}</td>
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

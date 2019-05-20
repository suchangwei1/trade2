<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="comm/include.inc.jsp"%>
<form id="pagerForm" method="post" action="ssadmin/userIntroList.html">
	<input type="hidden" name="keywords" value="${keywords}" />
	<input type="hidden" name="pageNum" value="${currentPage}" />
	<input type="hidden" name="numPerPage" value="${numPerPage}" />
	<input type="hidden" name="orderField" value="${param.orderField}" />
	<input type="hidden" name="orderDirection" value="${param.orderDirection}" />
</form>


<div class="pageHeader">
	<form onsubmit="return navTabSearch(this);"
		action="ssadmin/userIntroList.html" method="post">
		<div class="searchBar">

			<table class="searchContent">
				<tr>
					<td>推广人信息：<input type="text" name="keywords" value="${keywords}" size="60" /> [会员UID、账号、真实姓名]</td>
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
	<table class="table" width="120%" layoutH="138">
		<thead>
			<tr>
				<th width="20">序号</th>
				<th width="100">推广人UID</th>
				<th width="60">推广人奖励</th>
				<th width="60">被推荐人UID</th>
				<th width="60">被推荐人奖励</th>
				<th width="60" orderField="createTime"
					<c:if test='${param.orderField == "createTime" }'> class="${param.orderDirection}"  </c:if>>推荐时间</th>
			</tr>
		</thead>
		<tbody>
			<c:forEach items="${list}" var="log" varStatus="num">
				<tr target="sid_user" rel="${log.id}">
					<td>${num.index +1}</td>
					<td>${log.parent.fid}</td>
					<td>${log.parentRewardNum} ${log.parentRewardCoin}</td>
					<td>${log.child.fid}</td>
					<td>${log.childRewardNum} ${log.childRewardCoin}</td>
					<td>${log.createTime}</td>
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

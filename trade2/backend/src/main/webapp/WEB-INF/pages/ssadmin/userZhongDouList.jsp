<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="comm/include.inc.jsp"%>
<form id="pagerForm" method="post" action="ssadmin/userZhongDouList.html">
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
		action="ssadmin/userZhongDouList.html" method="post">
		<div class="searchBar">

			<table class="searchContent">
				<tr>
					<td>会员UID：<input type="text" name="keywords"
						value="${keywords}" size="60" /></td>
					<td>种树日期： <input type="text" name="startDate" class="date"
						readonly="true" value="${startDate }" />
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
			<li>
			 总数量：${totalZhongdouqty }，总数量(BY时间):${totalZhongdouqty_bytime }
			翻新土地消耗：${totalTakeNewqty }，翻新土地消耗(BY时间):${totalTakeNewqty_bytime }
			购买土地消耗：${totalOpenqty }，购买土地消耗(BY时间):${totalOpenqty_bytime }
			</li>
		</ul>
	</div>
	<table class="table" width="100%" layoutH="138">
		<thead>
			<tr>
				<th width="100">推荐UID——会员UID</th>
				<th width="100">真实姓名</th>
				<th width="100">种树总数量</th>
				<th width="100">种树数量(by时间)</th>
				<th width="100">翻新土地消耗</th>
				<th width="100">购买土地消耗</th>
				<th width="100">翻新土地消耗(by时间)</th>
				<th width="100">购买土地消耗(by时间)</th>
			</tr>
		</thead>
		<tbody>
			<c:forEach items="${userZhongDouList}" var="user" varStatus="num">
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

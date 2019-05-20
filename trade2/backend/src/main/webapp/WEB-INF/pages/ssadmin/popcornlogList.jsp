<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="comm/include.inc.jsp"%>
<form id="pagerForm" method="post" action="ssadmin/popcornlogList.html">
	<input type="hidden" name="fstatus" value="${fstatus}"> <input
		type="hidden" name="keywords" value="${keywords}" /> <input
		type="hidden" name="pageNum" value="${currentPage}" /> <input
		type="hidden" name="numPerPage" value="${numPerPage}" /> <input
		type="hidden" name="orderField" value="${param.orderField}" /><input
		type="hidden" name="orderDirection" value="${param.orderDirection}" />
</form>


<div class="pageHeader">
	<form onsubmit="return navTabSearch(this);"
		action="ssadmin/popcornlogList.html" method="post">
		<div class="searchBar">

			<table class="searchContent">
				<tr>
					<td>状态： <select type="combox" name="fstatus">
							<c:forEach items="${statusMap}" var="status">
								<c:if test="${status.key == fstatus}">
									<option value="${status.key}" selected="true">${status.value}</option>
								</c:if>
								<c:if test="${status.key != fstatus}">
									<option value="${status.key}">${status.value}</option>
								</c:if>
							</c:forEach>
					</select></td>
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
				<th width="20">期数</th>
				<th width="60">状态</th>
				<th width="100">开奖结果</th>
				<th width="100">创建时间</th>
			</tr>
		</thead>
		<tbody>
			<c:forEach items="${popcornlogList}" var="popcornlogList" varStatus="num">
				<tr target="sid_user" rel="${popcornlogList.fid}">
					<td>${popcornlogList.fid}</td>
					<td>${popcornlogList.fstatus_s}</td>
					<td>${popcornlogList.fresult1_s}${popcornlogList.fresult2_s}</td>
					<td>${popcornlogList.fcreatetime}</td>
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

<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="comm/include.inc.jsp"%>
<form id="pagerForm" method="post" action="ssadmin/salespercentLookup.html">
	<input type="hidden" name="status" value="${param.status}"> <input
		type="hidden" name="keywords" value="${keywords}" /> <input
		type="hidden" name="pageNum" value="${currentPage}" /> <input
		type="hidden" name="numPerPage" value="${numPerPage}" /> <input
		type="hidden" name="orderField" value="${param.orderField}" />
</form>

<div class="pageHeader">
	<form rel="pagerForm" method="post" action="ssadmin/salespercentLookup.html"
		onsubmit="return dwzSearch(this, 'dialog');">
		<div class="searchBar">

			<table class="searchContent">
				<tr>
					<td>关键字：<input type="text" name="keywords" value="${keywords}"
						size="60" /> [等级]</td>
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
	<table class="table" width="100%" layoutH="138">
		<thead>
			<tr>
				<th width="20">序号</th>
				<th width="100">等级</th>
				<th width="60">级别类型</th>
				<th width="60">总分成比例</th>
				<th width="60">砸FC比例</th>
				<th width="60">创建时间</th>
				<th width="60">查找带回</th>
			</tr>
		</thead>
		<tbody>
			<c:forEach items="${salespercent1}" var="salespercent" varStatus="num">
				<tr>
					<td>${num.index +1}</td>
					<td>${salespercent.flevel}</td>
					<td>${salespercent.fgrade_s}</td>
					<td>${salespercent.ftotalpercent}</td>
					<td>${salespercent.feggpercent}</td>
					<td>${salespercent.fcreatetime}</td>
					<td><a class="btnSelect"
						href="javascript:$.bringBack({id:'${salespercent.fid}', level:'${salespercent.flevel}', fgrade_s:'${salespercent.fgrade_s}', fgrade:'${salespercent.fgrade}'})"
						title="查找带回">选择</a>
					</td>
				</tr>
			</c:forEach>
		</tbody>
	</table>

	<div class="panelBar">
		<div class="pages">
			<span>总共: ${totalCount}条</span>
		</div>
		<div class="pagination" targetType="dialog" totalCount="${totalCount}"
			numPerPage="${numPerPage}" pageNumShown="5"
			currentPage="${currentPage}"></div>
	</div>
</div>

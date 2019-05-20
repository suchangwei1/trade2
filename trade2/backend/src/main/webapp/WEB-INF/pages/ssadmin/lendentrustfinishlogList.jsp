<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="comm/include.inc.jsp"%>
<form id="pagerForm" method="post"
	action="ssadmin/lendEntrustFinishLogList.html">
	<input type="hidden" name="status" value="${param.status}"> <input
		type="hidden" name="keywords" value="${keywords}" /><input
		type="hidden" name="type" value="${type}" /><input type="hidden"
		name="uid" value="${uid}" /> <input type="hidden" name="pageNum"
		value="${currentPage}" /> <input type="hidden" name="numPerPage"
		value="${numPerPage}" /> <input type="hidden" name="orderField"
		value="${param.orderField}" /><input type="hidden"
		name="orderDirection" value="${param.orderDirection}" />
</form>


<div class="pageHeader">
	<form onsubmit="return dialogSearch(this);"
		action="ssadmin/lendEntrustFinishLogList.html" method="post">
		<div class="searchBar">

			<table class="searchContent">
				<tr>
					<td></td>
				</tr>
			</table>
			<div class="subBar">
				
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
				<th width="60">下单类型</th>
				<th width="60">还款本金</th>
				<th width="60">总手续费</th>
				<th width="60">放款人收取手续费</th>
				<th width="60">推荐人收取手续费</th>
				<th width="60">领导收取手续费</th>
				<th width="60">借款时间</th>
				<th width="60">最迟还款时间</th>
				<th width="60">还款时间</th>
			</tr>
		</thead>
		<tbody>
			<c:forEach items="${lendEntrustFinishLogList}" var="lendEntrustFinishLog" varStatus="num">
				<tr target="sid_user" rel="${lendEntrustFinishLog.fid}">
					<td>${num.index +1}</td>
					<td>${lendEntrustFinishLog.freturnType_s}</td>
					<td>${lendEntrustFinishLog.famount}</td>
					<td>${lendEntrustFinishLog.ffees}</td>
					<td>${lendEntrustFinishLog.fowerFees}</td>
					<td>${lendEntrustFinishLog.fintrolFees}</td>
					<td>${lendEntrustFinishLog.fleaderFees}</td>
					<td>${lendEntrustFinishLog.flendentrustlog.fcreateTime}</td>
					<td>${lendEntrustFinishLog.flendentrustlog.fMustReturnTime}</td>
					<td>${lendEntrustFinishLog.fcreateTime}</td>
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

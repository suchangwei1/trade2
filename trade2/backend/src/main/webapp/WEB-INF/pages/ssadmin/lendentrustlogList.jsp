<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="comm/include.inc.jsp"%>
<form id="pagerForm" method="post"
	action="ssadmin/lendentrustlogList.html">
	<input type="hidden" name="status" value="${param.status}"> <input
		type="hidden" name="keywords" value="${keywords}" /><input
		type="hidden" name="type" value="${type}" /><input
		type="hidden" name="status1" value="${status1}" /><input type="hidden"
		name="uid" value="${uid}" /> <input type="hidden" name="pageNum"
		value="${currentPage}" /> <input type="hidden" name="numPerPage"
		value="${numPerPage}" /> <input type="hidden" name="orderField"
		value="${param.orderField}" /><input type="hidden"
		name="orderDirection" value="${param.orderDirection}" />
</form>


<div class="pageHeader">
	<form onsubmit="return dialogSearch(this);"
		action="ssadmin/lendentrustlogList.html" method="post">
		<div class="searchBar">

			<table class="searchContent">
				<tr>
					<td>状态： <select type="combox" name="status1">
							<c:forEach items="${status1Map}" var="status">
								<c:if test="${status.key == status1}">
									<option value="${status.key}" selected="true">${status.value}</option>
								</c:if>
								<c:if test="${status.key != status1}">
									<option value="${status.key}">${status.value}</option>
								</c:if>
							</c:forEach>
					</select>
					<input type="hidden" name="type" value="${type}" />
					<input type="hidden" name="uid" value="${uid}" />
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
			<li><a class="edit"
				href="ssadmin/lendEntrustFinishLogList.html?uid={sid_user}"
				height="400" width="700" target="dialog" rel="lendEntrustFinishLogList"><span>查看明细</span>
			</a></li>
		</ul>
	</div>
	<table class="table" width="100%" layoutH="138">
		<thead>
			<tr>
				<th width="20">序号</th>
				<th width="60">借款/放款UID</th>
				<th width="60">借款/放款登陆名</th>
				<th width="60">借款/放款真实姓名</th>
				<th width="60">借款/放款总金额</th>
				<th width="60">已还金额</th>
				<th width="60">日利率</th>
				<th width="60">状态</th>
				<th width="60">借款时间</th>
				<th width="60">最迟还款时间</th>
				<th width="60">今日新增手续费</th>
				<th width="60">累计未还手续费</th>
				<th width="60">修改时间</th>
			</tr>
		</thead>
		<tbody>
			<c:forEach items="${lendentrustlogList}" var="lendentrustlog" varStatus="num">
				<tr target="sid_user" rel="${lendentrustlog.fid}">
					<td>${num.index +1}</td>
					<td>${lendentrustlog.fuser.fid}</td>
					<td>${lendentrustlog.fuser.floginName}</td>
					<td>${lendentrustlog.fuser.frealName}</td>
					<td>${lendentrustlog.famount}</td>
					<td>${lendentrustlog.freturnAmount}</td>
					<td>${lendentrustlog.fdailyRate}</td>
					<td>${lendentrustlog.fstatus_s}</td>
					<td>${lendentrustlog.fcreateTime}</td>
					<td>${lendentrustlog.fMustReturnTime}</td>
					<td>${lendentrustlog.ftodayfees}</td>
					<td>${lendentrustlog.ftotalfees}</td>
					<td>${lendentrustlog.flastUpdateTime}</td>
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

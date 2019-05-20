<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="comm/include.inc.jsp"%>
<form id="pagerForm" method="post" action="ssadmin/deductlogDetail.html">
	<input type="hidden" name="status" value="${param.status}"> <input
		type="hidden" name="keywords" value="${keywords}" /><input
		type="hidden" name="uid" value="${uid}" /><input type="hidden"
		name="type" value="${ftype}" /> <input type="hidden" name="pageNum"
		value="${currentPage}" /> <input type="hidden" name="numPerPage"
		value="${numPerPage}" /> <input type="hidden" name="orderField"
		value="${param.orderField}" /><input type="hidden"
		name="orderDirection" value="${param.orderDirection}" />
</form>


<div class="pageHeader">
	<form onsubmit="return dialogSearch(this);"
		action="ssadmin/deductlogDetail.html" method="post">
		<div class="searchBar">

			<table class="searchContent">
				<tr>
					<input type="hidden" name="uid" value="${uid}" />
					<td>会员信息：<input type="text" name="keywords"
						value="${keywords}" size="60" />
					</td>
					<td></td>
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
	<div class="panelBar"></div>
	<table class="table" width="100%" layoutH="138">
		<thead>
			<tr>
				<th width="20">序号</th>
				<th width="60">来源会员登录名</th>
				<th width="60">来源会员昵称</th>
				<th width="60">来源真实姓名</th>
				<th width="60">提成类型</th>
				<th width="60">金额/FC</th>
				<th width="60">手续费</th>
				<th width="60">得到提成</th>
				<th width="60">是否是人民币</th>
				<th width="60">创建时间</th>
			</tr>
		</thead>
		<tbody>
			<c:forEach items="${deductlogList}" var="deductlogList"
				varStatus="num">
				<tr target="sid_user" rel="${deductlogList.fid}">
					<td>${num.index +1}</td>
					<td>${deductlogList.fsourceUserId.floginName}</td>
					<td>${deductlogList.fsourceUserId.fnickName}</td>
					<td>${deductlogList.fsourceUserId.frealName}</td>
					<td>${deductlogList.ftype_s}</td>
					<td><fmt:formatNumber value="${deductlogList.famt}" pattern="##.######" maxIntegerDigits="10" maxFractionDigits="4"/></td>
					<td><fmt:formatNumber value="${deductlogList.ffees}" pattern="##.######" maxIntegerDigits="10" maxFractionDigits="4"/></td>
					<td><fmt:formatNumber value="${deductlogList.ftakeAmt}" pattern="##.######" maxIntegerDigits="10" maxFractionDigits="4"/></td>
					<td>${deductlogList.fisMoney}</td>
					<td>${deductlogList.fcreatetime}</td>
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

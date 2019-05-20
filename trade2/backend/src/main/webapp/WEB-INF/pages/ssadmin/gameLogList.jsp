<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="comm/include.inc.jsp"%>
<form id="pagerForm" method="post" action="ssadmin/gameLogList.html">
	<input type="hidden" name="status" value="${param.status}"> <input
		type="hidden" name="keywords" value="${keywords}" /> <input
		type="hidden" name="pageNum" value="${currentPage}" /> <input
		type="hidden" name="numPerPage" value="${numPerPage}" /> <input
		type="hidden" name="orderField" value="${param.orderField}" /><input
		type="hidden" name="orderDirection" value="${param.orderDirection}" />
</form>


<div class="pageHeader">
	<form onsubmit="return navTabSearch(this);"
		action="ssadmin/gameLogList.html" method="post">
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
				<th width="60" orderField="fuser.floginName"
					<c:if test='${param.orderField == "fuser.floginName" }'> class="${param.orderDirection}"  </c:if>>会员登陆名</th>
				<th width="60" orderField="fuser.fnickName"
					<c:if test='${param.orderField == "fuser.fnickName" }'> class="${param.orderDirection}"  </c:if>>会员昵称</th>
				<th width="60" orderField="fuser.frealName"
					<c:if test='${param.orderField == "fuser.frealName" }'> class="${param.orderDirection}"  </c:if>>会员真实姓名</th>
				<th width="60">种植类型</th>		
				<th width="60" orderField="fgamerule.flevel"
					<c:if test='${param.orderField == "fgamerule.flevel" }'> class="${param.orderDirection}"  </c:if>>会员等级</th>
				<th width="60" orderField="fstartTime"
					<c:if test='${param.orderField == "fstartTime" }'> class="${param.orderDirection}"  </c:if>>开始时间</th>
				<th width="60" orderField="fplanEndTime"
					<c:if test='${param.orderField == "fplanEndTime" }'> class="${param.orderDirection}"  </c:if>>计划结束时间</th>
				<th width="60" orderField="factualEndTime"
					<c:if test='${param.orderField == "factualEndTime" }'> class="${param.orderDirection}"  </c:if>>实际结束时间</th>
				<th width="60" orderField="fstatus"
					<c:if test='${param.orderField == "fstatus" }'> class="${param.orderDirection}"  </c:if>>状态</th>	
				<th width="60" orderField="fplanHarvestQty"
					<c:if test='${param.orderField == "fplanHarvestQty" }'> class="${param.orderDirection}"  </c:if>>计划收获数量</th>
				<th width="60" orderField="factualHarvestQty"
					<c:if test='${param.orderField == "factualHarvestQty" }'> class="${param.orderDirection}"  </c:if>>实际收获数量</th>
				<th width="60" orderField="fcreateTime"
					<c:if test='${param.orderField == "fcreateTime" }'> class="${param.orderDirection}"  </c:if>>创建时间</th>
			</tr>
		</thead>
		<tbody>
			<c:forEach items="${gameLogList}" var="gameLog" varStatus="num">
				<tr target="sid_user" rel="${gameLog.fid}">
					<td>${num.index +1}</td>
					<td>${gameLog.fuser.floginName}</td>
					<td>${gameLog.fuser.fnickName}</td>
					<td>${gameLog.fuser.frealName}</td>
					<td>${gameLog.ftype_s}</td>
					<td>${gameLog.fgamerule.flevel}</td>
					<td>${gameLog.fstartTime}</td>
					<td>${gameLog.fplanEndTime}</td>
					<td>${gameLog.factualEndTime}</td>
					<td>${gameLog.fstatus_s}</td>
					<td>${gameLog.fplanHarvestQty}</td>
					<td>${gameLog.factualHarvestQty}</td>
					<td>${gameLog.fcreateTime}</td>
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

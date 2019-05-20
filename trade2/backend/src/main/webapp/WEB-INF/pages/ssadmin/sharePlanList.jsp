<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="comm/include.inc.jsp"%>
<form id="pagerForm" method="post" action="ssadmin/sharePlanList.html">
	<input type="hidden" name="status" value="${param.status}"> <input
		type="hidden" name="keywords" value="${keywords}" /> <input
		type="hidden" name="type" value="${ftype}" /><input type="hidden"
		name="pageNum" value="${currentPage}" /> <input type="hidden"
		name="numPerPage" value="${numPerPage}" /> <input type="hidden"
		name="orderField" value="${param.orderField}" /><input type="hidden"
		name="orderDirection" value="${param.orderDirection}" />
</form>


<div class="pageHeader">
	<form onsubmit="return navTabSearch(this);"
		action="ssadmin/sharePlanList.html" method="post">
		<div class="searchBar">

			<table class="searchContent">
				<tr>
					<td>标题：<input type="text" name="keywords" value="${keywords}"
						size="60" /></td>
					<td>虚拟币类型： <select type="combox" name="ftype">
							<c:forEach items="${typeMap}" var="type">
								<c:if test="${type.key == ftype}">
									<option value="${type.key}" selected="true">${type.value}</option>
								</c:if>
								<c:if test="${type.key != ftype}">
									<option value="${type.key}">${type.value}</option>
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
			<li><a class="add"
				href="ssadmin/goSharePlanJSP.html?url=ssadmin/addSharePlan"
				height="300" width="800" target="dialog" rel="addSharePlan"><span>新增</span>
			</a>
			</li>
			<li><a class="edit"
				href="ssadmin/goSharePlanJSP.html?url=ssadmin/updateSharePlan&uid={sid_user}"
				height="300" width="800" target="dialog" rel="updateSharePlan"><span>修改</span>
			</a>
			</li>
			<li><a class="delete"
				href="ssadmin/deleteSharePlan.html?uid={sid_user}" target="ajaxTodo"
				title="确定要删除吗?"><span>删除</span> </a>
			</li>
			<li><a class="edit"
				href="ssadmin/auditSharePlan.html?uid={sid_user}" target="ajaxTodo"
				title="确定要审核吗?"><span>审核</span> </a>
			</li>
			<li><a class="edit" href="ssadmin/sendMoney.html?uid={sid_user}"
				target="ajaxTodo" title="确定要发钱吗?"><span>发钱</span> </a></li>
			<li><a class="edit"
				href="ssadmin/sharePlanLogList.html?parentId={sid_user}"
				height="400" width="800" target="dialog" rel="sharePlanLogList"><span>查看记录</span>
			</a>
			</li>
		</ul>
	</div>
	<table class="table" width="100%" layoutH="138">
		<thead>
			<tr>
				<th width="20">序号</th>
				<th width="60">标题</th>
				<th width="60">状态</th>
				<th width="60">虚拟币名称</th>
				<th width="60">分红总金额</th>
				<th width="100">审核人</th>
				<th width="100">审核时间</th>
			</tr>
		</thead>
		<tbody>
			<c:forEach items="${sharePlanList}" var="sharePlan" varStatus="num">
				<tr target="sid_user" rel="${sharePlan.fid}">
					<td>${num.index +1}</td>
					<td>${sharePlan.ftitle}</td>
					<td>${sharePlan.fstatus_s}</td>
					<td>${sharePlan.fvirtualcointype.fname}</td>
					<td>${sharePlan.famount}</td>
					<td>${sharePlan.fcreator.fname}</td>
					<td>${sharePlan.fcreateTime}</td>
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
<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="comm/include.inc.jsp"%>
<form id="pagerForm" method="post" action="ssadmin/salescontractList.html">
	<input type="hidden" name="status" value="${param.status}"> <input
		type="hidden" name="keywords" value="${keywords}" /><input
		type="hidden" name="type" value="${ftype}" /> <input type="hidden"
		name="pageNum" value="${currentPage}" /> <input type="hidden"
		name="numPerPage" value="${numPerPage}" /> <input type="hidden"
		name="orderField" value="${param.orderField}" /><input type="hidden"
		name="orderDirection" value="${param.orderDirection}" />
</form>


<div class="pageHeader">
	<form onsubmit="return navTabSearch(this);"
		action="ssadmin/salescontractList.html" method="post">
		<div class="searchBar">

			<table class="searchContent">
				<tr>
					<td>会员信息：<input type="text" name="keywords" value="${keywords}"
						size="60" /></td>
					<td></td>
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
					href="ssadmin/goSalescontractJSP.html?url=ssadmin/addSalescontract"
					height="300" width="900" target="dialog" rel="addSalescontract"><span>新增</span>
				<li><a class="delete"
					href="ssadmin/deleteSalespercent.html?uid={sid_user}" target="ajaxTodo"
					title="确定要删除吗?"><span>删除</span> </a></li>
				<li><a class="edit"
					href="ssadmin/goSalescontractJSP.html?url=ssadmin/updateSalescontract&uid={sid_user}"
					height="300" width="900" target="dialog" rel="updateArticle"><span>修改</span>
				</a></li>
				<li><a class="edit"
					href="ssadmin/salesContractForbid.html?uid={sid_user}"
					target="ajaxTodo" title="确定要审核吗?"><span>审核</span> </a></li>
				<li><a class="delete"
					href="ssadmin/salesContractDisForbid.html?uid={sid_user}"
					target="ajaxTodo" title="确定要取消审核吗?"><span>取消审核</span> </a></li>
				<li class="line">line</li>
				<li><a class="icon" href="ssadmin/salescontractExport.html"
					target="dwzExport" targetType="navTab" title="实要导出这些记录吗?"><span>导出EXCEL</span>
				</a>
				</li>	
		</ul>
	</div>
	<table class="table" width="100%" layoutH="138">
		<thead>
			<tr>
				<th width="20">序号</th>
				<th width="60" orderField="fuser.floginName"
					<c:if test='${param.orderField == "fuser.floginName" }'> class="${param.orderDirection}"  </c:if>>会员登录名</th>
				<th width="60" orderField="fuser.frealName"
					<c:if test='${param.orderField == "fuser.frealName" }'> class="${param.orderDirection}"  </c:if>>真实姓名</th>	
				<th width="60">手机号码</th>
				<th width="60" orderField="fsalespercent.flevel"
					<c:if test='${param.orderField == "fsalespercent.flevel" }'> class="${param.orderDirection}"  </c:if>>等级</th>
				<th width="60">级别类型</th>
				<th width="60" orderField="fstatus"
					<c:if test='${param.orderField == "fstatus" }'> class="${param.orderDirection}"  </c:if>>状态</th>
				<th width="60" orderField="fcreateTime"
					<c:if test='${param.orderField == "fcreateTime" }'> class="${param.orderDirection}"  </c:if>>创建时间</th>
			</tr>
		</thead>
		<tbody>
			<c:forEach items="${salescontractList}" var="salescontractList" varStatus="num">
				<tr target="sid_user" rel="${salescontractList.fid}">
					<td>${num.index +1}</td>
					<td>${salescontractList.fuser.floginName }</td>
					<td>${salescontractList.fuser.frealName }</td>
					<td>${salescontractList.fuser.ftelephone }</td>
					<td>${salescontractList.fsalespercent.flevel}</td>
					<td>${salescontractList.fgrade_s}</td>
					<td>${salescontractList.fstatus_s}</td>
					<td>${salescontractList.fcreateTime}</td>
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

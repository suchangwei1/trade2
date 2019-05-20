<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="../comm/include.inc.jsp"%>
<form id="pagerForm" method="post" action="ssadmin/ccList.html">
	<input type="hidden" name="status" value="${param.status}">
	<input type="hidden" name="fstatus" value="${param.fstatus}"><input
		type="hidden" name="keywords" value="${keywords}" /><input
		type="hidden" name="startDate" value="${startDate}" /><input
		type="hidden" name="endDate" value="${startDate}" /> <input
		type="hidden" name="troUid" value="${troUid}" /> <input type="hidden"
		name="ftype" value="${ftype}" /> <input type="hidden" name="pageNum"
		value="${currentPage}" /> <input type="hidden" name="numPerPage"
		value="${numPerPage}" /> <input type="hidden" name="orderField"
		value="${param.orderField}" /> <input type="hidden" name="orderDirection" value="${param.orderDirection}" />
</form>


<div class="pageHeader">
	<form onsubmit="return navTabSearch(this);"
		  action="ssadmin/ccList.html" method="post">
		<div class="searchBar">

			<table class="searchContent">
				<tr>
					<td>会员信息：<input type="text" name="keywords"
									value="${keywords}" size="60" placeholder="用户ID/登录名/姓名/手机号/身份证号/邮箱/用户银行卡号"/></td>
					<td>订单类型： <select type="combox" name="ftype">
						<option value="" selected="true">全部</option>
						<c:forEach items="${typeMap}" var="type">
							<c:if test="${type.key == ftype}">
								<option value="${type.key}" selected="true">${type.value}</option>
							</c:if>
							<c:if test="${type.key != ftype}">
								<option value="${type.key}">${type.value}</option>
							</c:if>
						</c:forEach>
					</select>
					</td>
					<td>订单状态： <select type="combox" name="fstatus">
						<option value="" selected="true">全部</option>
						<c:forEach items="${statusMap}" var="status">
							<c:if test="${status.key == fstatus}">
								<option value="${status.key}" selected="true">${status.value}</option>
							</c:if>
							<c:if test="${status.key != fstatus}">
								<option value="${status.key}">${status.value}</option>
							</c:if>
						</c:forEach>
					</select>
					</td>
					 <td>开始日期： <input type="text" name="startDate" class="date"
						readonly="true" value="${startDate }" /></td>
					<td>结束日期： <input type="text" name="endDate" class="date"
									 readonly="true" value="${endDate }" /></td>
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
			<shiro:hasPermission name="ssadmin/cccheck.html">
				<li><a class="edit"
					   href="ssadmin/ccUpdate.html?uid={sid_user}&status=1"
					   target="ajaxTodo" title="确定要通过吗?"><span>通过</span> </a></li>
			</shiro:hasPermission>
			<shiro:hasPermission name="ssadmin/cccheck.html">
				<li><a class="delete"
					   href="ssadmin/ccUpdate.html?uid={sid_user}&status=2"
					   target="ajaxTodo" title="确定要取消吗?"><span>取消</span> </a></li>
			</shiro:hasPermission>
			<shiro:hasPermission name="ssadmin/cccheck.html">
				<li><a class="icon" href="ssadmin/ccExport.html"
					   target="dwzExport" targetType="navTab" title="是要导出这些记录吗?"><span>导出</span>
				</a></li>
			</shiro:hasPermission>
		</ul>
		<ul></ul>
	</div>
	<table class="table" width="100%" layoutH="138">
		<thead>
		<tr>
			<th width="40">订单ID</th>
			<th width="40">类型</th>
			<th width="40">用户名</th>
			<th width="40">商户名</th>
			<th width="40">用户银行卡号</th>
			<th width="40">币种</th>
			<th width="40">数量</th>
			<th width="40">价格</th>
			<th width="40">状态</th>
			<th width="60" orderField="createTime"
					<c:if test='${param.orderField == "createTime" }'> class="${param.orderDirection}"  </c:if>>生成时间</th>
			<th width="60" orderField="updateTime"
					<c:if test='${param.orderField == "updateTime" }'> class="${param.orderDirection}"  </c:if>>更新时间</th>
		</tr>
		</thead>
		<tbody>
		<c:forEach items="${userList}" var="user" varStatus="num">
			<tr target="sid_user" rel="${user.id}">
				<td>${user.id}</td>
				<td>${user.type.name}</td>
				<td>${user.buyyer.frealName}</td>
				<td>${user.seller.name}</td>
				<td>${user.buyyer.bankAccount}</td>
				<td>${user.coin.fShortName}</td>
				<td>${user.amount}</td>
				<td>${user.price}</td>
				<td>${user.status.name}</td>
				<td>${user.createTime}</td>
				<td>${user.updateTime}</td>
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

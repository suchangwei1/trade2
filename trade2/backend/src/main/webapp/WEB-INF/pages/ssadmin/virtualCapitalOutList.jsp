<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="comm/include.inc.jsp"%>
<form id="pagerForm" method="post"
	action="ssadmin/virtualCapitalOutList.html">
	<input type="hidden" name="status" value="${param.status}"> <input
		type="hidden" name="keywords" value="${keywords}" /><input
		type="hidden" name="ftype" value="${ftype}" /><input type="hidden"
		name="pageNum" value="${currentPage}" /> <input type="hidden"
		name="numPerPage" value="${numPerPage}" /> <input type="hidden"
		name="orderField" value="${param.orderField}" /><input type="hidden"
		name="orderDirection" value="${param.orderDirection}" />
</form>

<div class="pageHeader">
	<form onsubmit="return navTabSearch(this);"
		action="ssadmin/virtualCapitalOutList.html" method="post">
		<div class="searchBar">

			<table class="searchContent">
				<tr>
					<td>标题：<input type="text" name="keywords" value="${keywords}"
						size="60" />[会员信息、提现地址]</td>
					<td>虚拟币类型： <select type="combox" name="ftype">
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
			<shiro:hasPermission name="ssadmin/virtualCapitalOutAudit.html">
				<li><a class="edit"
					href="ssadmin/goVirtualCapitaloperationJSP.html?uid={sid_user}&url=ssadmin/viewVirtualCaptual"
					height="320" width="800" target="dialog"
					rel="viewVirtualCapitaloperation" title="确定要审核吗?"><span>审核</span>
				</a>
				</li>
			</shiro:hasPermission>
			<shiro:hasPermission name="ssadmin/virtualCapitalOutAuditTrade">
				<li><a class="edit"
					href="ssadmin/goVirtualCapitaloperationJSP.html?uid={sid_user}&url=ssadmin/viewVirtualCaptualTrade"
					height="320" width="800" target="dialog"
					rel="viewVirtualCapitaloperation" title="确定要审核吗?"><span>标记为已审核</span>
				</a>
				</li>
			</shiro:hasPermission>
			<shiro:hasPermission name="ssadmin/goVirtualCapitaloperationChangeStatus.html?type=1">
				<li><a class="edit"
					href="ssadmin/goVirtualCapitaloperationChangeStatus.html?uid={sid_user}&type=1"
					height="320" width="800" target="ajaxTodo"
					rel="viewVirtualCapitaloperation" title="确定要锁定吗?"><span>锁定</span>
				</a></li>
			</shiro:hasPermission>
			<shiro:hasPermission name="ssadmin/goVirtualCapitaloperationChangeStatus.html?type=2">
				<li><a class="edit"
					href="ssadmin/goVirtualCapitaloperationChangeStatus.html?uid={sid_user}&type=2"
					height="320" width="800" target="ajaxTodo"
					rel="viewVirtualCapitaloperation" title="确定要取消锁定吗?"><span>取消锁定</span>
				</a>
				</li>
			</shiro:hasPermission>
			<shiro:hasPermission name="ssadmin/goVirtualCapitaloperationChangeStatus.html?type=3">
				<li><a class="edit"
					href="ssadmin/goVirtualCapitaloperationChangeStatus.html?uid={sid_user}&type=3"
					height="320" width="800" target="ajaxTodo"
					rel="viewVirtualCapitaloperation" title="确定要取消提现吗?"><span>取消提现</span>
				</a>
				</li>
			</shiro:hasPermission>
			<shiro:hasPermission name="ssadmin/exportViCoinWithdrawList.html">
				<li><a class="icon"
					href="ssadmin/exportViCoinWithdrawList.html"
					height="320" width="800" target="dwzExport"
					rel="viewVirtualCapitaloperation" title="确定要导出提现记录吗?"><span>导出提现</span>
				</a>
				</li>
			</shiro:hasPermission>
		</ul>
	</div>
	<table class="table" width="160%" layoutH="138">
		<thead>
			<tr>
				<th width="20">序号</th>
				<th width="60" orderField="fuser.floginName"
					<c:if test='${param.orderField == "fuser.floginName" }'> class="${param.orderDirection}"  </c:if>>会员登陆名</th>
				<th width="60" orderField="fuser.fnickName"
					<c:if test='${param.orderField == "fuser.fnickName" }'> class="${param.orderDirection}"  </c:if>>会员昵称</th>
				<th width="60" orderField="fuser.femail"
					<c:if test='${param.orderField == "fuser.femail" }'> class="${param.orderDirection}"  </c:if>>会员邮箱</th>
				<th width="60" orderField="fuser.ftelephone"
					<c:if test='${param.orderField == "fuser.ftelephone" }'> class="${param.orderDirection}"  </c:if>>会员手机号</th>
				<th width="60" orderField="fuser.frealName"
					<c:if test='${param.orderField == "fuser.frealName" }'> class="${param.orderDirection}"  </c:if>>会员真实姓名</th>
				<th width="60" orderField="fvirtualcointype.fname"
					<c:if test='${param.orderField == "fvirtualcointype.fname" }'> class="${param.orderDirection}"  </c:if>>虚拟币类型</th>
				<th width="60" orderField="fstatus"
					<c:if test='${param.orderField == "fstatus" }'> class="${param.orderDirection}"  </c:if>>状态</th>
				<th width="60" orderField="ftype"
					<c:if test='${param.orderField == "ftype" }'> class="${param.orderDirection}"  </c:if>>交易类型</th>
				<th width="60" orderField="famount"
					<c:if test='${param.orderField == "famount" }'> class="${param.orderDirection}"  </c:if>>数量</th>
				<th width="60" orderField="ffees"
					<c:if test='${param.orderField == "ffees" }'> class="${param.orderDirection}"  </c:if>>手续费</th>
				<th width="60">提现地址</th>
				<th width="60" orderField="fcreateTime"
					<c:if test='${param.orderField == "fcreateTime" }'> class="${param.orderDirection}"  </c:if>>创建时间</th>
				<th width="60" orderField="flastUpdateTime"
					<c:if test='${param.orderField == "flastUpdateTime" }'> class="${param.orderDirection}"  </c:if>>最后修改时间</th>
			</tr>
		</thead>
		<tbody>
			<c:forEach items="${virtualCapitaloperationList}"
				var="virtualCapitaloperation" varStatus="num">
				<tr target="sid_user" rel="${virtualCapitaloperation.fid}">
					<td>${num.index +1}</td>
					<td>${virtualCapitaloperation.fuser.floginName}</td>
					<td>${virtualCapitaloperation.fuser.fnickName}</td>
					<td>${virtualCapitaloperation.fuser.femail}</td>
					<td>${virtualCapitaloperation.fuser.ftelephone}</td>
					<td>${virtualCapitaloperation.fuser.frealName}</td>
					<td>${virtualCapitaloperation.fvirtualcointype.fname}</td>
					<td>${virtualCapitaloperation.fstatus_s}</td>
					<td>${virtualCapitaloperation.ftype_s}</td>
					<td>${virtualCapitaloperation.famount}</td>
					<%--<td><fmt:formatNumber value="${virtualCapitaloperation.famount}" pattern="#######.####"></fmt:formatNumber></td>--%>
					<td>${virtualCapitaloperation.ffees}</td>
					<td>${virtualCapitaloperation.withdraw_virtual_address}</td>
					<td>${virtualCapitaloperation.fcreateTime}</td>
					<td>${virtualCapitaloperation.flastUpdateTime}</td>
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

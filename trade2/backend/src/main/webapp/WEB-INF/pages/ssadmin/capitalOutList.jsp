<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="comm/include.inc.jsp"%>
<form id="pagerForm" method="post" action="ssadmin/capitalOutList.html">
	<input type="hidden" name="status" value="${param.status}"> <input
		type="hidden" name="keywords" value="${keywords}" /><input
		type="hidden" name="capitalId" value="${capitalId}" /><input
		type="hidden" name="logDate" value="${logDate}" /> <input
		type="hidden" name="pageNum" value="${currentPage}" /> <input
		type="hidden" name="numPerPage" value="${numPerPage}" /> <input
		type="hidden" name="orderField" value="${param.orderField}" /><input
		type="hidden" name="orderDirection" value="${param.orderDirection}" />
</form>

<div class="pageHeader">
	<form onsubmit="return navTabSearch(this);"
		action="ssadmin/capitalOutList.html" method="post">
		<div class="searchBar">

			<table class="searchContent">
				<tr>
					<td>标题：<input type="text" name="keywords" value="${keywords}"
						size="40" />[会员名称、手机、银行帐户、金额、收款人]</td>
					<td>充值ID：<input type="text" name="capitalId"
						value="${capitalId}" size="10" />
					</td>
					<td>日期： <input type="text" name="logDate" class="date"
						readonly="true" value="${logDate }" />
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
			<shiro:hasPermission name="ssadmin/capitalOutAudit.html">
				<li><a class="edit"
					href="ssadmin/capitalOutAudit.html?uid={sid_user}&type=1"
					target="ajaxTodo" title="确定要审核吗?"><span>审核</span> </a>
				</li>
			</shiro:hasPermission>
			<shiro:hasPermission name="ssadmin/capitalOutConfirmAll.html">
				<li><a class="edit"
					   href="ssadmin/capitalOutConfirmAll.html"
					   title="确定要审核吗?"
					   target="selectedTodo" rel="ids"
					   postType="string"><span>批量审核</span> </a>
				</li>
			</shiro:hasPermission>
			<shiro:hasPermission name="ssadmin/capitalOutAudit.html?type=2">
				<li><a class="edit"
					href="ssadmin/capitalOutAudit.html?uid={sid_user}&type=2"
					target="ajaxTodo" title="确定要锁定吗?"><span>锁定</span> </a>
				</li>
			</shiro:hasPermission>
			<shiro:hasPermission name="ssadmin/capitalOutAudit.html?type=3">
				<li><a class="edit"
					href="ssadmin/capitalOutAudit.html?uid={sid_user}&type=3"
					target="ajaxTodo" title="确定要取消锁定吗?"><span>取消锁定</span> </a>
				</li>
			</shiro:hasPermission>
			<shiro:hasPermission name="ssadmin/capitalOutAudit.html?type=4">
				<li><a class="edit"
					href="ssadmin/capitalOutAudit.html?uid={sid_user}&type=4"
					target="ajaxTodo" title="确定要取消提现吗?"><span>取消提现</span> </a>
				</li>
			</shiro:hasPermission>
			<shiro:hasPermission name="ssadmin/capitalOutAuditAll.html">
				<li><a title="确实要锁定这些记录吗?" target="selectedTodo" rel="ids"
					postType="string" href="ssadmin/capitalOutAuditAll.html"
					class="edit"><span>批量锁定</span> </a>
				</li>
			</shiro:hasPermission>
			<shiro:hasPermission name="ssadmin/viewUserWallet.html">
				<li><a class="edit"
					href="ssadmin/viewUserWallet.html?cid={sid_user}" height="320"
					width="800" target="dialog"><span>查看会员资金情况</span> </a></li>
			</shiro:hasPermission>
			<shiro:hasPermission name="ssadmin/viewUser1.html">
				<li><a class="edit"
					href="ssadmin/viewUser1.html?cid={sid_user}" height="500"
					width="800" target="dialog"><span>查看会员信息</span> </a></li>	
			</shiro:hasPermission>
			<!--<li><a class="edit"
					href="ssadmin/userinfoList1.html?id={sid_user}"
					height="320" width="800" target="dialog"><span>查看会员利润情况</span> </a></li>-->	
			<shiro:hasPermission name="ssadmin/capitalOutExport.html">
				<li><a class="icon" href="ssadmin/capitalOutExport.html"
					target="dwzExport" targetType="navTab" title="是要导出这些记录吗?"><span>导出EXCEL</span>
				</a></li>	
			</shiro:hasPermission>
			<%--<shiro:hasPermission name="ssadmin/capitalOutExportSq.html">--%>
				<%--<li><a class="icon" href="ssadmin/capitalOutExportSq.html"--%>
					<%--target="dwzExport" targetType="navTab" title="是要导出这些记录吗?"><span>导出 双乾支付EXCEL</span>--%>
				<%--</a></li>	--%>
			<%--</shiro:hasPermission>--%>
			<shiro:hasPermission name="ssadmin/capitalOutExportSq.html">
				<li><a class="icon" href="ssadmin/capitalOutExportYs.html"
					   target="dwzExport" targetType="navTab" title="是要导出这些记录吗?"><span>导出 易生支付EXCEL</span>
				</a></li>
			</shiro:hasPermission>
		</ul>
	</div>
	<table class="table" width="120%" layoutH="138">
		<thead>
			<tr>
				<th width="22"><input type="checkbox" group="ids"
					class="checkboxCtrl">
				</th>
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
				<th width="60" orderField="ftype"
					<c:if test='${param.orderField == "ftype" }'> class="${param.orderDirection}"  </c:if>>类型</th>
				<th width="60" orderField="fstatus"
					<c:if test='${param.orderField == "fstatus" }'> class="${param.orderDirection}"  </c:if>>状态</th>
				<th width="60" orderField="famount"
					<c:if test='${param.orderField == "famount" }'> class="${param.orderDirection}"  </c:if>>金额</th>
				<th width="60" orderField="ffees"
					<c:if test='${param.orderField == "ffees" }'> class="${param.orderDirection}"  </c:if>>手续费</th>
				<th width="60" orderField="fBank"
					<c:if test='${param.orderField == "fBank" }'> class="${param.orderDirection}"  </c:if>>银行</th>
				<th width="60" orderField="fAccount"
					<c:if test='${param.orderField == "fAccount" }'> class="${param.orderDirection}"  </c:if>>收款帐号</th>
				<th width="60" orderField="fPhone"
					<c:if test='${param.orderField == "fPhone" }'> class="${param.orderDirection}"  </c:if>>手机号码</th>
				<th width="60">备注</th>
				<th width="60" orderField="fcreateTime"
					<c:if test='${param.orderField == "fcreateTime" }'> class="${param.orderDirection}"  </c:if>>创建时间</th>
				<th width="60" orderField="fLastUpdateTime"
					<c:if test='${param.orderField == "fLastUpdateTime" }'> class="${param.orderDirection}"  </c:if>>最后修改时间</th>
			</tr>
		</thead>
		<tbody>
			<c:forEach items="${capitaloperationList}" var="capitaloperation"
				varStatus="num">
				<tr target="sid_user" rel="${capitaloperation.fid}">
					<td><input name="ids" value="${capitaloperation.fid}"
						type="checkbox">
					</td>
					<td>${capitaloperation.fuser.floginName}</td>
					<td>${capitaloperation.fuser.fnickName}</td>
					<td>${capitaloperation.fuser.femail}</td>
					<td>${capitaloperation.fuser.ftelephone}</td>
					<td>${capitaloperation.fuser.frealName}</td>
					<td>${capitaloperation.ftype_s}</td>
					<td>${capitaloperation.fstatus_s}</td>
					<td>${capitaloperation.famount}</td>
					<td>${capitaloperation.ffees}</td>
					<td>${capitaloperation.fBank}</td>
					<td>${capitaloperation.faccount_s}</td>
					<td>${capitaloperation.fPhone}</td>
					<td>${capitaloperation.fid}</td>
					<td>${capitaloperation.fcreateTime}</td>
					<td>${capitaloperation.fLastUpdateTime}</td>
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

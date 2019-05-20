<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="comm/include.inc.jsp"%>
<form id="pagerForm" method="post" action="ssadmin/capitalInList.html">
	<input type="hidden" name="status" value="2"> 
	<input type="hidden" name="type" value="1"> 
	<input type="hidden" name="keywords" value="${keywords}" /> <input
		type="hidden" name="capitalId" value="${capitalId}" /> <input
		type="hidden" name="pageNum" value="${currentPage}" /> <input
		type="hidden" name="logDate" value="${logDate}" /><input
		type="hidden" name="numPerPage" value="${numPerPage}" /> <input
		type="hidden" name="orderField" value="${param.orderField}" /><input
		type="hidden" name="orderDirection" value="${param.orderDirection}" />
</form>


<div class="pageHeader">
	<form onsubmit="return navTabSearch(this);"
		action="ssadmin/capitalInList.html" method="post">
		<div class="searchBar">

			<table class="searchContent">
				<tr>
					<td>关键词：<input type="text" name="keywords" value="${keywords}" />[会员名称、手机、银行帐户、金额、收款人]</td>
					<td>充值ID：<input type="text" name="capitalId"
						value="${capitalId}" size="10" /></td>
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
			<shiro:hasPermission name="ssadmin/capitalInAudit.html">
				<li><a class="edit"
					href="ssadmin/capitalInAudit.html?uid={sid_user}" target="ajaxTodo"
					title="确定要审核吗?"><span>审核</span> </a>
				</li>
			</shiro:hasPermission>
			<shiro:hasPermission name="ssadmin/capitalInCancel.html">
				<li><a class="edit"
					href="ssadmin/capitalInCancel.html?uid={sid_user}"
					target="ajaxTodo" title="确定要取消充值吗?"><span>取消充值</span> </a>
				</li>
			</shiro:hasPermission>
			<shiro:hasPermission name="ssadmin/exportCapitalOperation.html">
				<li><a class="icon"
					href="ssadmin/exportCapitalOperation.html"
					target="dwzExport" title="确定要导出这些记录吗?"><span>导出审核列表</span> </a>
				</li>
			</shiro:hasPermission>
		</ul>

	</div>
	<table class="table" width="120%" layoutH="138">
		<thead>
			<tr>
				<th width="60">序号</th>
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
					<c:if test='${param.orderField == "fBank" }'> class="${param.orderDirection}"  </c:if>>汇款银行</th>
				<th width="60" orderField="fAccount"
					<c:if test='${param.orderField == "fAccount" }'> class="${param.orderDirection}"  </c:if>>汇款帐号</th>
				<th width="60" orderField="fPayee"
					<c:if test='${param.orderField == "fPayee" }'> class="${param.orderDirection}"  </c:if>>汇款人</th>
				<th width="60" orderField="fPhone"
					<c:if test='${param.orderField == "fPhone" }'> class="${param.orderDirection}"  </c:if>>汇款手机号码</th>
				<th width="60">汇入银行</th>
				<th width="60">汇入帐号</th>
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
					<td width="60">${num.index +1}</td>
					<td width="60">${capitaloperation.fuser.floginName}</td>
					<td width="60">${capitaloperation.fuser.fnickName}</td>
					<td width="60">${capitaloperation.fuser.femail}</td>
					<td width="60">${capitaloperation.fuser.ftelephone}</td>
					<td width="60">${capitaloperation.fuser.frealName}</td>
					<td width="60">${capitaloperation.ftype_s}</td>
					<td width="60">${capitaloperation.fstatus_s}</td>
					<td width="60">${capitaloperation.famount}</td>
					<td width="60">${capitaloperation.ffees}</td>
					<td width="60">${capitaloperation.fBank}</td>
					<td width="60">${capitaloperation.faccount_s}</td>
					<td width="60">${capitaloperation.fPayee}</td>
					<td width="60">${capitaloperation.fPhone}</td>
					<td width="60">${capitaloperation.systembankinfo.fbankName}</td>
					<td width="60">${capitaloperation.systembankinfo.fbankNumber}</td>
					<td width="60">${capitaloperation.fid}</td>
					<td width="60">${capitaloperation.fcreateTime}</td>
					<td width="60">${capitaloperation.fLastUpdateTime}</td>
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

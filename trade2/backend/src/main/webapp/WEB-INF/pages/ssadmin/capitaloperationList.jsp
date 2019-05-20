<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="comm/include.inc.jsp"%>
<form id="pagerForm" method="post"
	action="ssadmin/capitaloperationList.html">
	<input type="hidden" name="status" value="${param.status}"> <input
		type="hidden" name="keywords" value="${param.keywords}" /><input
		type="hidden" name="fstatus" value="${fstatus}" /> <input
		type="hidden" name="capitalId" value="${capitalId}" /><input
		type="hidden" name="logDate" value="${param.logDate}" /><input
		type="hidden" name="pageNum" value="${currentPage}" /> <input
		type="hidden" name="numPerPage" value="${numPerPage}" /> <input
		type="hidden" name="orderField" value="${param.orderField}" /><input
		type="hidden" name="orderDirection" value="${param.orderDirection}" />
</form>


<div class="pageHeader">
	<form onsubmit="return navTabSearch(this);"
		action="ssadmin/capitaloperationList.html" method="post">
		<div class="searchBar">

			<table class="searchContent">
				<tr>
					<td>关键词：<input type="text" name="keywords" value="${keywords}"
						size="30" />[会员名称、手机、银行帐户、金额、收款人]</td>
					<td>充值ID：<input type="text" name="capitalId"
						value="${capitalId}" size="10" /></td>
					<td>状态： <select type="combox" name="fstatus">
							<c:forEach items="${statusMap}" var="status">
								<c:if test="${status.key == fstatus}">
									<option value="${status.key}" selected="true">${status.value}</option>
								</c:if>
								<c:if test="${status.key != fstatus}">
									<option value="${status.key}">${status.value}</option>
								</c:if>
							</c:forEach>
					</select></td>
					<td>日期： <input type="text" name="logDate" class="date"
						readonly="true" value="${param.logDate }" />
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
			<shiro:hasPermission name="ssadmin/capitaloperationExport.html">
				<li><a class="icon" href="ssadmin/capitaloperationExport.html"
					target="dwzExport" targetType="navTab" title="是要导出这些记录吗?"><span>导出EXCEL</span>
				</a></li>
			</shiro:hasPermission>
		</ul>
	</div>
	<table class="table" width="150%" layoutH="138">
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
				<th width="60" orderField="ftype"
					<c:if test='${param.orderField == "ftype" }'> class="${param.orderDirection}"  </c:if>>类型</th>
				<th width="60" orderField="fstatus"
					<c:if test='${param.orderField == "fstatus" }'> class="${param.orderDirection}"  </c:if>>状态</th>
				<th width="60" orderField="famount"
					<c:if test='${param.orderField == "famount" }'> class="${param.orderDirection}"  </c:if>>金额</th>
				<th width="60" orderField="ffees"
					<c:if test='${param.orderField == "ffees" }'> class="${param.orderDirection}"  </c:if>>手续费</th>
				<th width="60">备注</th>
				<th width="60" orderField="fcreateTime"
					<c:if test='${param.orderField == "fcreateTime" }'> class="${param.orderDirection}"  </c:if>>创建时间</th>
				<th width="60" orderField="fLastUpdateTime"
					<c:if test='${param.orderField == "fLastUpdateTime" }'> class="${param.orderDirection}"  </c:if>>最后修改时间</th>
				<th width="60">审核人</th>
				<th width="60">支付平台交易号</th>
			</tr>
		</thead>
		<tbody>
			<c:forEach items="${capitaloperationList}" var="capitaloperation"
				varStatus="num">
				<tr target="sid_user" rel="${capitaloperation.fid}">
					<td>${num.index +1}</td>
					<td>${capitaloperation.fuser.floginName}</td>
					<td>${capitaloperation.fuser.fnickName}</td>
					<td>${capitaloperation.fuser.femail}</td>
					<td>${capitaloperation.fuser.ftelephone}</td>
					<td>${capitaloperation.fuser.frealName}</td>
					<td>${capitaloperation.payType.name}</td>
					<td>${capitaloperation.fstatus_s}</td>
					<td>${capitaloperation.famount}</td>
					<td>${capitaloperation.ffees}</td>

					<td>
						<c:choose>
							<c:when test="${2 == capitaloperation.payType.index}">
								${wechatOrderNoPrefix}${capitaloperation.fid}
							</c:when>
							<c:otherwise>
								${capitaloperation.fid}
							</c:otherwise>
						</c:choose>
					</td>

					<td>${capitaloperation.fcreateTime}</td>
					<td>${capitaloperation.fLastUpdateTime}</td>
					<td>${capitaloperation.fAuditee_id.fname}</td>
					<td>${capitaloperation.outTradeNo}</td>
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

<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="comm/include.inc.jsp"%>
<form id="pagerForm" method="post"
	action="ssadmin/virtualCaptualoperationList.html">
	<input type="hidden" name="status" value="${param.status}"> <input
		type="hidden" name="keywords" value="${keywords}" /><input
		type="hidden" name="ftype" value="${ftype}" /><input
		type="hidden" name="oper_ftype" value="${oper_ftype}" />
	<input type="hidden" name="fstatus" value="${fstatus}" />
	<input type="hidden" name="pageNum" value="${currentPage}" /> <input type="hidden"
		name="numPerPage" value="${numPerPage}" /> <input type="hidden"
		name="orderField" value="${param.orderField}" /><input type="hidden"
		name="orderDirection" value="${param.orderDirection}" />
</form>


<div class="pageHeader">
	<form onsubmit="return navTabSearch(this);"
		action="ssadmin/virtualCaptualoperationList.html" method="post">
		<div class="searchBar">

			<table class="searchContent">
				<tr>
					<td>标题：<input type="text" name="keywords" value="${keywords}"
						size="60" />[会员信息、充值地址、提现地址]</td>
					<td>虚拟币类型： <select type="combox" name="ftype">
							<option value="0">全部</option>
							<c:forEach items="${typeMap}" var="type">
								<c:if test="${type.key == ftype}">
									<option value="${type.key}" selected="true">${type.value.fname}</option>
								</c:if>
								<c:if test="${type.key != ftype}">
									<option value="${type.key}">${type.value.fname}</option>
								</c:if>
							</c:forEach>
					</select></td>
					<%--<td>操作类型： <select type="combox" name="oper_ftype">
						<option value="0">全部</option>
						<option value="1" <c:if test="${oper_ftype == 1}" >selected="true"</c:if>>虚拟币充值</option>
						<option value="2" <c:if test="${oper_ftype == 2}" >selected="true"</c:if>>虚拟币提现</option>
					</select></td>--%>
					<td>状态： <select type="combox" name="fstatus">
						<option value="0">全部</option>
						<option value="1" <c:if test="${fstatus == 1}" >selected="true"</c:if>>等待提现</option>
						<option value="2" <c:if test="${fstatus == 2}" >selected="true"</c:if>>正在处理</option>
						<option value="3" <c:if test="${fstatus == 3}" >selected="true"</c:if>>提现成功</option>
						<option value="4" <c:if test="${fstatus == 4}" >selected="true"</c:if>>用户取消</option>
					</select></td>
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
		<!-- <li><a class="edit"
				href="ssadmin/goVirtualCapitaloperationJSP.html?url=ssadmin/viewVirtualCaptualStatus&uid={sid_user}&type=ViewStatus"
				height="300" width="800" target="dialog" rel="updateLink"><span>查看交易状态</span>
			</a></li> -->
			<%--<shiro:hasPermission name="ssadmin/detail.html">--%>
			<%--<li><a class="edit"--%>
				   <%--href="ssadmin/detail.html?uid={sid_user}"--%>
				   <%--height="400" width="750" target="dialog"--%>
				   <%--rel="viewVirtualCapitaloperation" title="查看交易信息"><span>查看交易信息</span>--%>
			<%--</a>--%>
			<%--</li>--%>
			<%--</shiro:hasPermission>--%>
			<shiro:hasPermission name="ssadmin/reVirtualCapitalOutAudit.html">
				<%--<shiro:hasPermission name="ssadmin/virtualCapitalOutAuditTrade">--%>
				<li>
					<a class="edit"
					   href="ssadmin/goVirtualCapitaloperationJSP.html?uid={sid_user}&url=ssadmin/viewVirtualCaptualRe"
					   height="320" width="800" target="dialog"
					   rel="viewVirtualCapitaloperation" title="数据库已更新，钱包没有转币，确定重新审核吗?"><span>重新审核</span>
				</a>
				</li>
			</shiro:hasPermission>

			<shiro:hasPermission name="ssadmin/exportVirtualCaptualoperationList.html">
				<li><a class="icon"
					href="ssadmin/exportVirtualCaptualoperationList.html"
				 target="dwzExport" title="确定要导出操作记录吗？"><span>导出操作记录</span> </a></li>
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
				<th width="60" orderField=famount
					<c:if test='${param.orderField == "famount" }'> class="${param.orderDirection}"  </c:if>>数量</th>
				<th width="60" orderField="ffees"
					<c:if test='${param.orderField == "ffees" }'> class="${param.orderDirection}"  </c:if>>手续费</th>
				<th width="60">提现地址</th>
				<th width="60">充值地址</th>
				<th width="60">交易ID</th>
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
					<td>${virtualCapitaloperation.ffees}${typeMap[virtualCapitaloperation.feeCoinType + 0].fShortName}</td>
					<td>${virtualCapitaloperation.withdraw_virtual_address}</td>
					<td>${virtualCapitaloperation.recharge_virtual_address}</td>
					<td>${virtualCapitaloperation.ftradeUniqueNumber}</td>
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

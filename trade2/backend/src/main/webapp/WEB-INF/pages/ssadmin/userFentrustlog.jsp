<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="comm/include.inc.jsp"%>
<form id="pagerForm" method="post" action="ssadmin/userFentrustlog.html">
	<input type="hidden" name="status" value="${param.status}"> <input
		type="hidden" name="keywords" value="${keywords}" /><input
		type="hidden" name="startDate" value="${startDate}" /><input
		type="hidden" name="endDate" value="${endDate}" /><input
		type="hidden" name="price" value="${price}" /><input
		type="hidden" name="ftype" value="${ftype}" /><input
		type="hidden" name="status" value="${status}" /><input
		type="hidden" name="entype" value="${entype}" /> <input type="hidden"
		name="pageNum" value="${currentPage}" /> <input type="hidden"
		name="numPerPage" value="${numPerPage}" /> <input type="hidden"
		name="orderField" value="${param.orderField}" /><input type="hidden"
		name="orderDirection" value="${param.orderDirection}" />
</form>


<div class="pageHeader">
	<form id="queryForm" onsubmit="return navTabSearch(this);"
		action="ssadmin/userFentrustlog.html" method="post">
		<div class="searchBar">

			<table class="searchContent">
				<tr>
					<td>会员信息：<input type="text" name="keywords" value="${keywords}"
						size="30" placeholder="昵称/真实姓名/登录账号" /></td>
					<td>交易对： <select type="combox" name="ftype">
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
					<td>起始日期： <input type="text" name="startDate" class="date"
						readonly="true" value="${startDate }" />
					</td>
					<td>结束日期： <input type="text" name="endDate" class="date"
						readonly="true" value="${endDate }" />
					</td>
					<td>价格：<input type="text" name="price" value="${price}"
								  size="10" /></td>
					<td>类型： <select type="combox" name="entype">
						<option value="">全部</option>
						<option value="0" <c:if test="${entype == 0}">selected</c:if>>买入</option>
						<option value="1" <c:if test="${entype == 1}">selected</c:if>>卖出</option>
					</select>
					</td>
				</tr>
			</table>
			<div class="subBar">
				<ul>
					<li><div class="buttonActive">
							<div class="buttonContent">
								<button type="submit" style="line-height:27px;">查询</button>
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
			<shiro:hasPermission name="ssadmin/exportEntrustList.html">
				<li><a class="icon" target="dwzExport" title="确定要导出历史成交单列表吗？" href="/ssadmin/exportEntrustLogList.html"><span>导出列表</span></a>
				</li>
			</shiro:hasPermission>
		</ul>
	</div>
	<table class="table" width="120%" layoutH="138">
		<thead>
			<tr>
				<th width="20">序号</th>
				<th width="50" >会员登陆名</th>
				<th width="60" >会员昵称</th>
				<th width="60" >会员邮箱</th>
				<th width="60" >会员手机号</th>
				<th width="60" >会员真实姓名</th>
				<th width="60">交易对</th>
				<th width="60" orderField="l.fEntrustType"
					<c:if test='${param.orderField == "l.fEntrustType"}'> class="${param.orderDirection}"  </c:if>>交易类型</th>
				<th width="60" orderField="l.fPrize"
					<c:if test='${param.orderField == "l.fPrize"}'> class="${param.orderDirection}"  </c:if>>单价</th>
				<th width="60" orderField="l.fCount"
					<c:if test='${param.orderField == "l.fCount"}'> class="${param.orderDirection}"  </c:if>>数量</th>
				<th width="60" orderField="l.fAmount"
					<c:if test='${param.orderField == "l.fAmount"}'> class="${param.orderDirection}"  </c:if>>成交金额</th>
				<th width="60" orderField="l.fCreateTime"
					<c:if test='${param.orderField == "l.fCreateTime"}'> class="${param.orderDirection}"  </c:if>>创建时间</th>
			</tr>
		</thead>
		<tbody>
			<c:forEach items="${entrustList}" var="entrust" varStatus="num">
				<tr target="sid_user" rel="${entrust.fid}">
					<td>${num.index +1}</td>
					<td>${entrust.fLoginName}</td>
					<td>${entrust.fNickName}</td>
					<td>${entrust.fEmail}</td>
					<td>${entrust.fTelephone}</td>
					<td>${entrust.fRealName}</td>
					<td>${typeMap[entrust.cid] }</td>
					<td>
						<c:if test="${entrust.fEntrustType == 0}">买入</c:if>
						<c:if test="${entrust.fEntrustType == 1}">卖出</c:if>
					</td>
					<td>${entrust.fPrize}</td>
					<td>${entrust.fCount}</td>
					<td>${entrust.fAmount}</td>
					<td>${entrust.fCreateTime}</td>
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



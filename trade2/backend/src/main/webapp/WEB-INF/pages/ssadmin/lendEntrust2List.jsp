<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="comm/include.inc.jsp"%>
<form id="pagerForm" method="post" action="ssadmin/lendentrustList.html">
	<input type="hidden" name="status" value="${param.status}"> <input
		type="hidden" name="type" value="${type}" /><input type="hidden"
		name="keywords" value="${keywords}" /> <input
		type="hidden" name="status1" value="${status1}" /><input
		type="hidden" name="status2" value="${status2}" /><input type="hidden"
		name="pageNum" value="${currentPage}" /> <input type="hidden"
		name="numPerPage" value="${numPerPage}" /> <input type="hidden"
		name="orderField" value="${param.orderField}" /><input type="hidden"
		name="orderDirection" value="${param.orderDirection}" />
</form>


<div class="pageHeader">
	<form onsubmit="return navTabSearch(this);"
		action="ssadmin/lendentrustList.html" method="post">
		<div class="searchBar">
			<table class="searchContent">
				<tr>
					<td>会员信息：<input type="text" name="keywords"
						value="${keywords}" size="60" /> <input type="hidden" name="type"
						value="${type}" />
					</td>
					<td>成交状态： <select type="combox" name="status1">
							<c:forEach items="${status1Map}" var="status">
								<c:if test="${status.key == status1}">
									<option value="${status.key}" selected="true">${status.value}</option>
								</c:if>
								<c:if test="${status.key != status1}">
									<option value="${status.key}">${status.value}</option>
								</c:if>
							</c:forEach>
					</select></td>
					<td>还款状态： <select type="combox" name="status2">
							<c:forEach items="${status2Map}" var="status">
								<c:if test="${status.key == status2}">
									<option value="${status.key}" selected="true">${status.value}</option>
								</c:if>
								<c:if test="${status.key != status2}">
									<option value="${status.key}">${status.value}</option>
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
			<li><a class="add"
				href="ssadmin/lendentrustlogList.html?type=2&uid={sid_user}"
				height="500" width="900" target="dialog" rel="lendentrustlogList"><span>查看借款明细</span>
			</a>
			</li>
		</ul>
	</div>
	<table class="table" width="100%" layoutH="138">
		<thead>
			<tr>
				<th width="20">序号</th>
				<th width="40" orderField="fuser.floginName"
					<c:if test='${param.orderField == "fuser.floginName" }'> class="${param.orderDirection}"  </c:if>>会员登陆名称</th>
				<th width="40" orderField="fuser.fnickName"
					<c:if test='${param.orderField == "fuser.fnickName" }'> class="${param.orderDirection}"  </c:if>>会员昵称</th>
				<th width="40" orderField="fuser.frealName"
					<c:if test='${param.orderField == "fuser.frealName" }'> class="${param.orderDirection}"  </c:if>>会员真实姓名</th>
				<th width="40" orderField="fcnyOrCoin"
					<c:if test='${param.orderField == "fcnyOrCoin" }'> class="${param.orderDirection}"  </c:if>>融资OR融币</th>
				<th width="40">下单类型</th>	
<%-- 				<th width="40" orderField="fvirtualcointype.fname"
					<c:if test='${param.orderField == "fvirtualcointype.fname" }'> class="${param.orderDirection}"  </c:if>>虚拟币类型</th>
	 --%>			
				<th width="40" orderField="famount"
					<c:if test='${param.orderField == "famount" }'> class="${param.orderDirection}"  </c:if>>委托总金额</th>
				<th width="40" orderField="fsuccessAmount"
					<c:if test='${param.orderField == "fsuccessAmount" }'> class="${param.orderDirection}"  </c:if>>已成交金额</th>
				<th width="30" orderField="fdailyRate"
					<c:if test='${param.orderField == "fdailyRate" }'> class="${param.orderDirection}"  </c:if>>日利率</th>
				<th width="40" orderField="ftimeLength"
					<c:if test='${param.orderField == "ftimeLength" }'> class="${param.orderDirection}"  </c:if>>放款天数</th>
			<%-- 	<th width="40" orderField="fsubType"
					<c:if test='${param.orderField == "fsubType" }'> class="${param.orderDirection}"  </c:if>>放款类型</th> --%>
				<th width="40" orderField="fstatus1"
					<c:if test='${param.orderField == "fstatus1" }'> class="${param.orderDirection}"  </c:if>>成交状态</th>
				<th width="40" orderField="fstatus2"
					<c:if test='${param.orderField == "fstatus2" }'> class="${param.orderDirection}"  </c:if>>还款状态</th>
				<th width="40" orderField="fcreateTime"
					<c:if test='${param.orderField == "fcreateTime" }'> class="${param.orderDirection}"  </c:if>>创建时间</th>
				<th width="40" orderField="flastUpdateTime"
					<c:if test='${param.orderField == "flastUpdateTime" }'> class="${param.orderDirection}"  </c:if>>修改时间</th>
			</tr>
		</thead>
		<tbody>
			<c:forEach items="${lendentrustList}" var="lendentrust"
				varStatus="num">
				<tr target="sid_user" rel="${lendentrust.fid}">
					<td>${num.index +1}</td>
					<td>${lendentrust.fuser.floginName}</td>
					<td>${lendentrust.fuser.fnickName}</td>
					<td>${lendentrust.fuser.frealName}</td>
					<td>${lendentrust.fcnyOrCoin_s}</td>
					<td>${lendentrust.freturnType_s}</td>
					<%-- <td>${lendentrust.fvirtualcointype.fname}</td> --%>
					<td>${lendentrust.famount}</td>
					<td>${lendentrust.fsuccessAmount}</td>
					<td>${lendentrust.fdailyRate}</td>
					<td>${lendentrust.ftimeLength}</td>
					<%-- <td>${lendentrust.fsubType_s}</td> --%>
					<td>${lendentrust.fstatus1_s}</td>
					<td>${lendentrust.fstatus2_s}</td>
					<td>${lendentrust.fcreateTime}</td>
					<td>${lendentrust.flastUpdateTime}</td>
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

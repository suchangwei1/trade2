<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="comm/include.inc.jsp"%>
<form id="pagerForm" method="post" action="ssadmin/balanceList.html">
	<input type="hidden" name="status" value="${param.status}"> <input
		type="hidden" name="keywords" value="${keywords}" /> <input
		type="hidden" name="pageNum" value="${currentPage}" /> <input
		type="hidden" name="numPerPage" value="${numPerPage}" /> <input
		type="hidden" name="orderField" value="${param.orderField}" /><input
		type="hidden" name="orderDirection" value="${param.orderDirection}" />
</form>


<div class="pageHeader">
	<form onsubmit="return navTabSearch(this);"
		action="ssadmin/balanceList.html" method="post">
		<div class="searchBar">
			<table class="searchContent">
				<tr>
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
			<shiro:hasPermission name="ssadmin/updateBalance.html">
				<li><a class="edit"
						href="ssadmin/goBalanceJSP.html?url=ssadmin/updateBalance&uid={sid_user}"
						height="300" width="800" target="dialog" rel="updateBalance"><span>修改</span>
					</a>
					</li>
			</shiro:hasPermission>
		</ul>
	</div>
	<table class="table" width="100%" layoutH="138">
		<thead>
			<tr>
				<th width="20">序号</th>
				<th width="100">定存简介</th>
				<th width="60" orderField="fintrolRate"
                    <c:if test='${param.orderField == "fintrolRate" }'> class="${param.orderDirection}"  </c:if>>推荐人收益比例</th>
				<th width="60" orderField="fcreatetime"
                    <c:if test='${param.orderField == "fcreatetime" }'> class="${param.orderDirection}"  </c:if>>创建时间</th>
			</tr>
		</thead>
		<tbody>
			<c:forEach items="${balanceList}" var="balance" varStatus="num">
				<tr target="sid_user" rel="${balance.fid}">
					<td>${num.index +1}</td>
					<td>${balance.ftitle}</td>
					<td>${balance.fintrolRate}</td>
					<td>${balance.fcreatetime}</td>
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

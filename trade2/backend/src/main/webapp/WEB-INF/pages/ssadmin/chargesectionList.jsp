<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="comm/include.inc.jsp"%>
<form id="pagerForm" method="post" action="ssadmin/chargesectionList.html">
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
		action="ssadmin/chargesectionList.html" method="post">
		<div class="searchBar">

			<table class="searchContent">
				<tr>
					<td>结算小时：<input type="text" name="keywords" value="${keywords}"
						size="60" style="width: 137px; "/></td>
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
			<!-- 
				<li><a class="add"
					href="ssadmin/goChargesectionJSP.html?url=ssadmin/addChargesection"
					height="300" width="800" target="dialog" rel="addChargesection"><span>新增</span>
				</a></li>
				<li><a class="edit"
					href="ssadmin/goChargesectionJSP.html?url=ssadmin/updateChargesection&uid={sid_user}"
					height="300" width="800" target="dialog" rel="updateArticle"><span>修改</span>
				</a></li>
		 -->
		</ul>
	</div>
	<table class="table" width="100%" layoutH="138">
		<thead>
			<tr>
				<th width="20">序号</th>
				<th width="60" orderField="fstartHour"
					<c:if test='${param.orderField == "fstartHour" }'> class="${param.orderDirection}"  </c:if>>开始结算小时</th>
				<th width="60" orderField="fendHour"
					<c:if test='${param.orderField == "fendHour" }'> class="${param.orderDirection}"  </c:if>>结束结算小时</th>
				<th width="60" orderField="fcreatetime"
					<c:if test='${param.orderField == "fcreatetime" }'> class="${param.orderDirection}"  </c:if>>创建时间</th>
			</tr>
		</thead>
		<tbody>
			<c:forEach items="${chargesectionList}" var="chargesection" varStatus="num">
				<tr target="sid_user" rel="${chargesection.fid}">
					<td>${num.index +1}</td>
					<td>${chargesection.fstartHour}</td>
					<td>${chargesection.fendHour}</td>
					<td>${chargesection.fcreatetime}</td>
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

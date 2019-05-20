<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="comm/include.inc.jsp"%>
<form id="pagerForm" method="post" action="ssadmin/salespercentList.html">
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
		action="ssadmin/salespercentList.html" method="post">
		<div class="searchBar">

			<table class="searchContent">
				<tr>
					<td>等级：<input type="text" name="keywords" value="${keywords}"
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
			
				<li><a class="add"
					href="ssadmin/goSalespercentJSP.html?url=ssadmin/addSalespercent"
					height="400" width="800" target="dialog" rel="addSalespercent"><span>新增</span>
				</a></li>
				<li><a class="edit"
					href="ssadmin/goSalespercentJSP.html?url=ssadmin/updateSalespercent&uid={sid_user}"
					height="400" width="800" target="dialog" rel="updateArticle"><span>修改</span>
				</a></li>
		
		</ul>
	</div>
	<table class="table" width="100%" layoutH="138">
		<thead>
			<tr>
				<th width="20">序号</th>
				<th width="60" orderField="flevel"
					<c:if test='${param.orderField == "flevel" }'> class="${param.orderDirection}"  </c:if>>业务员等级</th>
				<th width="60">级别类型</th>
				<th width="60" orderField="ftotalpercent"
					<c:if test='${param.orderField == "ftotalpercent" }'> class="${param.orderDirection}"  </c:if>>总分成比例</th>
				<th width="60" orderField="feggpercent"
					<c:if test='${param.orderField == "feggpercent" }'> class="${param.orderDirection}"  </c:if>>砸FC比例</th>
				<th width="60" orderField="fleaderpercent"
					<c:if test='${param.orderField == "fleaderpercent" }'> class="${param.orderDirection}"  </c:if>>领导奖比例</th>
				<th width="60" orderField="fcreatetime"
					<c:if test='${param.orderField == "fcreatetime" }'> class="${param.orderDirection}"  </c:if>>创建时间</th>
			</tr>
		</thead>
		<tbody>
			<c:forEach items="${salespercentList}" var="salespercent" varStatus="num">
				<tr target="sid_user" rel="${salespercent.fid}">
					<td>${num.index +1}</td>
					<td>${salespercent.flevel}</td>
					<td>${salespercent.fgrade_s}</td>
					<td>${salespercent.ftotalpercent}</td>
					<td>${salespercent.feggpercent}</td>
					<td>${salespercent.fleaderpercent}</td>
					<td>${salespercent.fcreatetime}</td>
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

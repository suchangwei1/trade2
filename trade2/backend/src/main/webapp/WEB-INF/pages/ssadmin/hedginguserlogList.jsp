<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="comm/include.inc.jsp"%>
<form id="pagerForm" method="post" action="ssadmin/hedginguserlogList.html">
	<input type="hidden" name="status" value="${param.status}"> <input
		type="hidden" name="keywords" value="${keywords}" /> <input
		type="hidden" name="pageNum" value="${currentPage}" /> <input
		type="hidden" name="numPerPage" value="${numPerPage}" /> <input
		type="hidden" name="orderField" value="${param.orderField}" /><input
		type="hidden" name="orderDirection" value="${param.orderDirection}" />
</form>


<div class="pageHeader">
	<form onsubmit="return navTabSearch(this);"
		action="ssadmin/hedginguserlogList.html" method="post">
		<div class="searchBar">

			<table class="searchContent">
				<tr>
					<td>会员信息：<input type="text" name="keywords"  value="${keywords}"
						size="60"/>
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
				
		</ul>
	</div>
	<table class="table" width="100%" layoutH="138">
		<thead>
			<tr>
				<th width="20">序号</th>
				<th width="60" orderField="fuser.floginName"
                    <c:if test='${param.orderField == "fuser.floginName" }'> class="${param.orderDirection}"  </c:if>>登录名</th>
				<th width="100" orderField="fuser.fnickName"
                    <c:if test='${param.orderField == "fuser.fnickName" }'> class="${param.orderDirection}"  </c:if>>昵称</th>
				<th width="100" orderField="fuser.frealName"
                    <c:if test='${param.orderField == "fuser.frealName" }'> class="${param.orderDirection}"  </c:if>>真实姓名</th>
				<th width="100" orderField="fhedginglog.fnumber"
                    <c:if test='${param.orderField == "fhedginglog.fnumber" }'> class="${param.orderDirection}"  </c:if>>期数</th>
				<th width="100" orderField="fhedginglog.fvirtualcointypeByFid.fname"
                    <c:if test='${param.orderField == "fhedginglog.fvirtualcointypeByFid.fname" }'> class="${param.orderDirection}"  </c:if>>对冲虚拟币名称</th>
				<th width="100" orderField="fhedginglog.fvirtualcointypeByFvid2.fname"
                    <c:if test='${param.orderField == "fhedginglog.fvirtualcointypeByFvid2.fname" }'> class="${param.orderDirection}"  </c:if>>投注虚拟币名称</th>
				<th width="100" orderField="fstatus"
                    <c:if test='${param.orderField == "fstatus" }'> class="${param.orderDirection}"  </c:if>>状态</th>
				<th width="100" orderField="ftaketype"
                    <c:if test='${param.orderField == "ftaketype" }'> class="${param.orderDirection}"  </c:if>>下注类型</th>
				<th width="100" orderField="fqty"
                    <c:if test='${param.orderField == "fqty" }'> class="${param.orderDirection}"  </c:if>>投注数量</th>
				<th width="100" orderField="fwinqty"
                    <c:if test='${param.orderField == "fwinqty" }'> class="${param.orderDirection}"  </c:if>>赢得数量</th>
				<th width="100" orderField="fcreatetime"
                    <c:if test='${param.orderField == "fcreatetime" }'> class="${param.orderDirection}"  </c:if>>创建时间</th>
			</tr>
		</thead>
		<tbody>
			<c:forEach items="${hedginguserlogList}" var="hedginguserlogList" varStatus="num">
				<tr target="sid_user" rel="${hedginguserlogList.fid}">
					<td>${num.index +1}</td>
					<td>${hedginguserlogList.fuser.floginName}</td>
					<td>${hedginguserlogList.fuser.fnickName}</td>
					<td>${hedginguserlogList.fuser.frealName}</td>
					<td>${hedginguserlogList.fhedginglog.fnumber}</td>
					<td>${hedginguserlogList.fhedginglog.fvirtualcointypeByFid.fname}</td>
					<td>${hedginguserlogList.fhedginglog.fvirtualcointypeByFvid2.fname}</td>
					<td>${hedginguserlogList.fstatus_s}</td>
					<td>${hedginguserlogList.ftaketype_s}</td>
					<td><fmt:formatNumber value="${hedginguserlogList.fqty}" pattern="##.##" maxIntegerDigits="10" maxFractionDigits="6"/></td>
					<td><fmt:formatNumber value="${hedginguserlogList.fwinqty}" pattern="##.##" maxIntegerDigits="10" maxFractionDigits="6"/></td>
					<td>${hedginguserlogList.fcreatetime}</td>
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

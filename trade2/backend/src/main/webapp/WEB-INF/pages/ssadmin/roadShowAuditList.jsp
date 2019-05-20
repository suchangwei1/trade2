<%--<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8"%>--%>
<%--<%@ include file="comm/include.inc.jsp"%>--%>
<%--<form id="pagerForm" method="post" action="/ssadmin/listRoadShow.html?status=${status}">--%>
	<%--<input type="hidden" name="status" value="${param.status}"> <input--%>
		<%--type="hidden" name="keywords" value="${title}" /><input--%>
		<%--type="hidden" name="ftype" value="${param.ftype}" /> <input type="hidden"--%>
		<%--name="pageNum" value="${currentPage}" /> <input type="hidden"--%>
		<%--name="numPerPage" value="${numPerPage}" /> <input type="hidden"--%>
		<%--name="orderField" value="${param.orderField}" /><input type="hidden"--%>
		<%--name="orderDirection" value="${param.orderDirection}" />--%>
<%--</form>--%>


<%--<div class="pageHeader">--%>
	<%--<form onsubmit="return navTabSearch(this);" action="/ssadmin/listRoadShow.html?status=${status}" method="post">--%>
		<%--<div class="searchBar">--%>
			<%--<table class="searchContent">--%>
				<%--<tr>--%>
					<%--<td>标题：<input type="text" name="title" value="${title}"--%>
						<%--size="60" /></td>--%>
					<%--<td></td>--%>
					<%--</td>--%>
				<%--</tr>--%>
			<%--</table>--%>
			<%--<div class="subBar">--%>
				<%--<ul>--%>
					<%--<li><div class="buttonActive">--%>
							<%--<div class="buttonContent">--%>
								<%--<button type="submit">查询</button>--%>
							<%--</div>--%>
						<%--</div></li>--%>
				<%--</ul>--%>
			<%--</div>--%>
		<%--</div>--%>
	<%--</form>--%>
<%--</div>--%>
<%--<div class="pageContent">--%>
	<%--<div class="panelBar">--%>
		<%--<ul class="toolBar">--%>
			<%--<shiro:hasPermission name="/ssadmin/auditRoadShow.html">--%>
				<%--<li><a class="edit"--%>
					<%--href="/ssadmin/auditRoadShow.html?id={roadShow_id}&status=${status}" target="ajaxTodo"--%>
                    <%--title="确定审核吗"><span>审核</span>--%>
				<%--</a></li>--%>
			<%--</shiro:hasPermission>--%>
		<%--</ul>--%>
	<%--</div>--%>
	<%--<table class="table" width="100%" layoutH="138">--%>
		<%--<thead>--%>
			<%--<tr>--%>
				<%--<th width="20">序号</th>--%>
				<%--<th width="60" orderField="speaker"--%>
					<%--<c:if test='${param.orderField == "speaker" }'> class="${param.orderDirection}"  </c:if>>演讲者</th>--%>
				<%--<th width="60" orderField="title"--%>
					<%--<c:if test='${param.orderField == "title" }'> class="${param.orderDirection}"  </c:if>>标题</th>--%>
				<%--<th width="60" orderField="状态"--%>
						<%--<c:if test='${param.orderField == "status" }'> class="${param.orderDirection}"  </c:if>>状态</th>--%>
				<%--<th width="60" orderField="createdTime"--%>
					<%--<c:if test='${param.orderField == "createdTime" }'> class="${param.orderDirection}"  </c:if>>创建时间</th>--%>
			<%--</tr>--%>
		<%--</thead>--%>
		<%--<tbody>--%>
			<%--<c:forEach items="${roadShotList}" var="roadShow" varStatus="num">--%>
				<%--<tr target="roadShow_id" rel="${article.fid}">--%>
					<%--<td>${num.index +1}</td>--%>
					<%--<td>${roadShow.speaker}</td>--%>
					<%--<td>${roadShow.title}</td>--%>
					<%--<td>${roadShow.status == 1 ? "未审核" : "审核"}</td>--%>
					<%--<td>${roadShow.createdTime}</td>--%>
				<%--</tr>--%>
			<%--</c:forEach>--%>
		<%--</tbody>--%>
	<%--</table>--%>

	<%--<div class="panelBar">--%>
		<%--<div class="pages">--%>
			<%--<span>总共: ${totalCount}条</span>--%>
		<%--</div>--%>
		<%--<div class="pagination" targetType="navTab" totalCount="${totalCount}"--%>
			<%--numPerPage="${numPerPage}" pageNumShown="5"--%>
			<%--currentPage="${currentPage}"></div>--%>
	<%--</div>--%>
<%--</div>--%>

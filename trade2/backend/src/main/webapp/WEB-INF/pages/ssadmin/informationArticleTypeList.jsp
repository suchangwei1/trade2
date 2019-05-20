<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="comm/include.inc.jsp"%>
<form id="pagerForm" method="post" action="/ssadmin/informationArticleTypeList.html">
</form>

<div class="pageHeader">
	<form onsubmit="return navTabSearch(this);" action="/ssadmin/informationArticleTypeList.html" method="post">
		<div class="searchBar">
			<table class="searchContent">
				<%--<tr>--%>
					<%--<td>关键字：<input type="text" name="keywords" value="${keywords}" size="60" />[名称、关键词、描述]--%>
					<%--</td>--%>
				<%--</tr>--%>
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
			<%--<shiro:hasPermission name="/ssadmin/saveInformationArticleType.html">--%>
				<%--<li><a class="add" href="/ssadmin/goInformationArticleJSP.html?url=ssadmin/addInformationArticleType"--%>
					<%--height="300" width="800" target="dialog" rel="addAricle"><span>新增</span> </a></li>--%>
			<%--</shiro:hasPermission>--%>
			<%--<shiro:hasPermission name="/ssadmin/deleteInformationArticleType.html">--%>
				<%--<li><a class="delete" href="/ssadmin/deleteInformationArticleType.html?uid={sid_user}"--%>
					<%--target="ajaxTodo" title="确定要删除吗?"><span>删除</span> </a></li>--%>
			<%--</shiro:hasPermission>--%>
			<%--<shiro:hasPermission name="/ssadmin/updateInformationArticleType.html">--%>
				<%--<li><a class="edit"--%>
					<%--href="/ssadmin/goInformationArticleTypeJSP.html?url=ssadmin/updateInformationArticleType&uid={sid_user}" height="300"--%>
					<%--width="800" target="dialog" rel="updateInformationAricleType"><span>修改标题</span> </a></li>--%>
			<%--</shiro:hasPermission>--%>
			<%--<shiro:hasPermission name="/ssadmin/updateInformationKeyword.html">--%>
				<%--<li><a class="edit"--%>
					<%--href="/ssadmin/goInformationArticleTypeJSP.html?url=ssadmin/keywordList&classId={sid_user}" height="600"--%>
					<%--width="900" target="dialog" rel="updateInformationKeyword"><span>修改关键字</span> </a></li>--%>
			<%--</shiro:hasPermission>--%>
		</ul>
	</div>
	<table class="table" width="100%" layoutH="138">
		<thead>
			<tr>
				<th width="20">序号</th>
				<th width="60">类型名称</th>
				<th width="60">创建时间</th>
				<%--<th width="60">状态</th>--%>
				<%--<th width="60" orderField="className"--%>
					<%--<c:if test='${param.orderField == "className" }'> class="${param.orderDirection}"  </c:if>>类型名称</th>--%>
				<%--<th width="60" orderField="headlineTitle"--%>
					<%--<c:if test='${param.orderField == "headlineTitle" }'> class="${param.orderDirection}"  </c:if>>标题</th>--%>
				<%--<th width="60">图片地址</th>--%>
				<%--<th width="60" orderField="content"--%>
					<%--<c:if test='${param.orderField == "content" }'> class="${param.orderDirection}"  </c:if>>内容</th>--%>
				<%--<th width="60" orderField="createdTime"--%>
					<%--<c:if test='${param.orderField == "createdTime" }'> class="${param.createdTime}"  </c:if>>创建时间</th>--%>
				<%--<th width="60" orderField="status"--%>
					<%--<c:if test='${param.orderField == "status" }'> class="${param.status}"  </c:if>>状态</th>--%>
			</tr>
		</thead>
		<tbody>
			<c:forEach items="${list}" var="informationArticleType" varStatus="num">
				<tr target="sid_user" rel="${informationArticleType.id}">
					<td>${num.index +1}</td>
					<td>${informationArticleType.className}</td>
					<td><fmt:formatDate value="${informationArticleType.createdTime}" pattern="yyyy-MM-dd HH:mm:ss"/></td>
					<%--<td>${informationArticleType.status}</td>--%>
				</tr>
			</c:forEach>
		</tbody>
	</table>

	<div class="panelBar">
		<div class="pages">
			<span>总共: ${totalCount}条</span>
		</div>
		<div class="pagination" targetType="navTab" totalCount="${totalCount}" numPerPage="${numPerPage}"
			pageNumShown="5" currentPage="${currentPage}"></div>
	</div>
</div>

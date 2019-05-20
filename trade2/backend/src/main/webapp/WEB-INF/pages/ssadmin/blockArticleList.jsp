<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="comm/include.inc.jsp"%>
<form id="pagerForm" method="post" action="ssadmin/blockArticleList.html">
	<input type="hidden" name="status" value="${param.status}"> <input
		type="hidden" name="keywords" value="${keywords}" /><input
		type="hidden" name="ftype" value="${param.type}" /> <input type="hidden"
		name="pageNum" value="${currentPage}" /> <input type="hidden"
		name="numPerPage" value="${numPerPage}" /> <input type="hidden"
		name="orderField" value="${param.orderField}" /><input type="hidden"
		name="orderDirection" value="${param.orderDirection}" />
</form>


<div class="pageHeader">
	<form onsubmit="return navTabSearch(this);"
		action="ssadmin/blockArticleList.html" method="post">
		<div class="searchBar">
			<table class="searchContent">
				<tr>
					<td>关键词：<input type="text" name="keywords" value="${keywords}"
						size="60" />[标题、关键词]</td>
					<td></td>
					<td>文章类型： <select type="combox" name="type">
							<c:forEach items="${typeMap}" var="ftype">
								<c:if test="${ftype.key == type}">
									<option value="${ftype.key}" selected="true">${ftype.value}</option>
								</c:if>
								<c:if test="${ftype.key != type}">
									<option value="${ftype.key}">${ftype.value}</option>
								</c:if>
							</c:forEach>
					</select>
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
			<shiro:hasPermission name="/ssadmin/saveBlockArticle.html">
				<li><a class="add"
					href="/ssadmin/goArticleJSP.html?url=ssadmin/addBlockArticle"
					height="500" width="900" target="dialog" rel="addBlockArticle"><span>新增</span>
				</a></li>
			</shiro:hasPermission>
			<shiro:hasPermission name="/ssadmin/deleteBlockArticle.html">
				<li><a class="delete"
					href="/ssadmin/deleteBlockArticle.html?id={sid_user}" target="ajaxTodo"
					title="确定要删除吗?"><span>删除</span> </a></li>
			</shiro:hasPermission>
			<shiro:hasPermission name="/ssadmin/updateBlockArticle.html">
				<li><a class="edit"
					href="/ssadmin/goBlockArticleJSP.html?url=ssadmin/updateBlockArticle&id={sid_user}"
					height="500" width="900" target="dialog" rel="updateBlockArticle"><span>修改</span>
				</a></li>
			</shiro:hasPermission>
		</ul>
	</div>
	<table class="table" width="100%" layoutH="138">
		<thead>
			<tr>
				<th width="20">序号</th>
				<th width="60" orderField="ftitle"
					<c:if test='${param.orderField == "title" }'> class="${param.orderDirection}"  </c:if>>标题</th>
				<th width="60" orderField="blockArticleType.name"
					<c:if test='${param.orderField == "farticletype.name" }'> class="${param.orderDirection}"  </c:if>>类型</th>
				<th width="60" orderField="keyword"
					<c:if test='${param.orderField == "keyword" }'> class="${param.orderDirection}"  </c:if>>关键词</th>
				<th width="60">文件链接</th>
				<th width="60">图片地址</th>
				<th width="60" orderField="createTime"
					<c:if test='${param.orderField == "createTime" }'> class="${param.orderDirection}"  </c:if>>创建时间</th>
				<th width="60" orderField="lastUpdateTime"
					<c:if test='${param.orderField == "lastUpdateTime" }'> class="${param.orderDirection}"  </c:if>>修改时间</th>
			</tr>
		</thead>
		<tbody>
			<c:forEach items="${blockArticleList}" var="article" varStatus="num">
				<tr target="sid_user" rel="${article.id}">
					<td>${num.index +1}</td>
					<td>${article.title}</td>
					<td>${article.blockArticleType.name}</td>
					<td>${article.keyword}</td>
					<td>${article.docUrl}</td>
					<td>${article.imgUrl}</td>
					<td>${article.createTime}</td>
					<td>${article.lastUpdateTime}</td>
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

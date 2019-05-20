<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="comm/include.inc.jsp"%>
<form id="pagerForm" method="post" action="/ssadmin/keywordList.html">
	<input type="hidden" name="type" value="${param.type}"/>
	<input type="hidden" name="pageNum" value="${currentPage}"/>
	<input type="hidden" name="numPerPage" value="${numPerPage}"/>
</form>
<div class="pageHeader">
	<form onsubmit="return navTabSearch(this);"
		action="/ssadmin/keywordList.html" method="post">
		<div class="searchBar">
			<table class="searchContent">
				<tr>
					<td>文章类型： <select type="combox" name="type" onchange="changeKeyword()">
							<c:forEach items="${typeNameMap}" var="typeEntry">
								<c:if test="${typeEntry.key == type}">
									<option value="${typeEntry.key}" selected="true">${typeEntry.value}</option>
								</c:if>
								<c:if test="${typeEntry.key != type}">
									<option value="${typeEntry.key}">${typeEntry.value}</option>
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
								<button type="submit" class="keywordClick">查询</button>
							</div>
						</div></li>
				</ul>
			</div>
			<script>
				function changeKeyword() {
					$(".keywordClick").click();
				}
			</script>
		</div>
	</form>
</div>
<div class="pageContent">
	<div class="panelBar">
		<ul class="toolBar">
			<shiro:hasPermission name="/ssadmin/saveKeyword.html">
				<li><a class="add"
					href="/ssadmin/goKeywordJSP.html?url=ssadmin/addKeyword&classId=${type}"
					height="500" width="900" target="dialog" rel="addKeyword"><span>新增</span>
				</a></li>
			</shiro:hasPermission>
			<shiro:hasPermission name="/ssadmin/deleteKeyword.html">
				<li><a class="delete"
					href="/ssadmin/deleteKeyword.html?id={sid_user}" target="ajaxTodo"
					title="确定要删除吗?"><span>删除</span> </a></li>
			</shiro:hasPermission>
			<shiro:hasPermission name="/ssadmin/updateKeyword.html">
				<li><a class="edit"
					href="/ssadmin/goKeywordJSP.html?url=ssadmin/updateKeyword&id={sid_user}"
					height="500" width="900" target="dialog" rel="updateKeyword"><span>修改</span>
				</a></li>
			</shiro:hasPermission>
		</ul>
	</div>
	<table class="table" width="100%" layoutH="138">
		<thead>
			<tr>
				<th width="20">序号</th>
				<th width="60">标题</th>
				<th width="60">类型</th>
			</tr>
		</thead>
		<tbody>
			<c:forEach items="${keywordList}" var="keyword" varStatus="num">
				<tr target="sid_user" rel="${keyword.id}">
					<td>${num.index +1}</td>
					<td>${keyword.keyName}</td>
					<td>${keyword.className}</td>
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

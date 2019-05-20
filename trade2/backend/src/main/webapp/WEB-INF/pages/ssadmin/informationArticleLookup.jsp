<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" %>
<%@ include file="comm/include.inc.jsp" %>
<form id="pagerForm" method="post" action="/ssadmin/forInformationArticleLookup.html">
	<input type="hidden" name="status" value="${param.status}">
	<input type="hidden" name="keywords" value="${keywords}"/>
	<input type="hidden" name="type" value="${param.type}"/>
	<input type="hidden" name="pageNum" value="${currentPage}"/>
	<input type="hidden" name="numPerPage" value="${numPerPage}"/>
	<input type="hidden" name="orderField" value="${param.orderField}"/>
	<input type="hidden" name="orderDirection" value="${param.orderDirection}"/>
</form>


<div class="pageHeader">
	<form onsubmit="return dwzSearch(this, 'dialog');"
	action="/ssadmin/forInformationArticleLookup.html" method="post" enctype="multipart/form-data">
		<div class="searchBar">
			<table class="searchContent">
				<tr>
					<td>关键词：<input type="text" name="keywords" value="${keywords}" size="60"/>[标题]</td>
					<td></td>
					<td>文章类型： <select type="combox" name="classId" onchange="changeArticleLookup()">
						<c:forEach items="${typeMap}" var="typeEntry">
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
					<li>
						<div class="buttonActive">
							<div class="buttonContent">
								<button type="submit" class="informationLookupClick">查询</button>
							</div>
						</div>
					</li>
				</ul>
			</div>
			<script>
				function changeArticleLookup() {
					$(".informationLookupClick").click();
				}
			</script>
		</div>
	</form>
</div>
<div class="pageContent">
	<table class="table" width="100%" layoutH="138" targetType="dialog">
		<thead>
		<tr>
			<th width="20">序号</th>
			<th width="60">文章ID</th>
			<th width="60" orderField="title"
					<c:if test="${param.orderField == 'title'}"> class="${param.orderDirection}" </c:if>>
				标题
			</th>
			<th width="60">类型</th>
			<th width="30">查找带回</th>
		</tr>
		</thead>
		<tbody>
		<c:forEach items="${articleList}" var="informationArticle" varStatus="num">
			<tr target="article_id" rel="${informationArticle.id}">
				<td>${num.index +1}</td>
				<td>${informationArticle.id}</td>
				<td><a href="${pageUrl}${informationArticle.id}" target="_blank">${informationArticle.title}</a></td>
				<td>${informationArticle.className}</td>
				<td><a class="btnSelect"
					   href="javascript:$.bringBack({articleId: '${informationArticle.id}', id:'${informationArticle.id}', title:'${informationArticle.title}'})"
					   title="查找带回">选择</a>
				</td>

			</tr>
		</c:forEach>
		</tbody>
	</table>

	<div class="panelBar">
		<div class="pages">
			<span>总共: ${totalCount}条</span>
		</div>
		<div class="pagination" targetType="dialog" totalCount="${totalCount}"
			 numPerPage="${numPerPage}" pageNumShown="5"
			 currentPage="${currentPage}"></div>
	</div>
</div>
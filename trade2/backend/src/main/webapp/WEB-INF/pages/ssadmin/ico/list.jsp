<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="../comm/include.inc.jsp"%>
<form id="pagerForm" method="post" action="/ssadmin/icoList.html">
	<input type="hidden" name="keyword" value="${param.keyword}"/>
	<input type="hidden" name="id" value="${param.id}"/>
	<input type="hidden" name="startTime" value="${param.startTime}"/>
	<input type="hidden" name="endTime" value="${param.endTime}"/>
	<input type="hidden" name="pageNum" value="${currentPage}"/>
	<input type="hidden" name="orderField" value="${param.orderField}" />
	<input type="hidden" name="orderDirection" value="${param.orderDirection}"/>
</form>


<div class="pageHeader">
	<form onsubmit="return navTabSearch(this);"
		action="/ssadmin/icoList.html" method="post">
		<div class="searchBar">

			<table class="searchContent">
				<tr>
					<td>ICO信息：<input type="text" name="keyword" value="${param.keyword}" size="30" />[ICO名称]</td>
					<td>ICO ID：<input type="text" name="id" value="${param.id}" size="10" /></td>
					<td>开启时间：
						<input type="text" class="date" name="startTime" value="${param.startTime}" readonly datefmt="yyyy-MM-dd" size="10"> -
						<input type="text" class="date" name="endTime" value="${param.endTime}" readonly datefmt="yyyy-MM-dd" size="10">
					</td>
					<td>
						<div class="buttonActive">
							<div class="buttonContent">
								<button type="submit">查询</button>
							</div>
						</div>
					</td>
				</tr>
			</table>
		</div>
	</form>
</div>
<div class="pageContent">
	<div class="panelBar">
		<ul class="toolBar">
			<shiro:hasPermission name="/ssadmin/saveICO.html">
				<li><a class="add"
					href="/ssadmin/goICOJSP.html?url=ssadmin/ico/edit"
					target="dialog" rel=saveICO height="600" width="1000"><span>新增</span> </a></li>
			</shiro:hasPermission>
			<shiro:hasPermission name="/ssadmin/saveICO.html">
				<li><a class="edit"
					href="/ssadmin/goICOJSP.html?url=ssadmin/ico/edit&uid={sid_user}"
					target="dialog" rel=saveICO height="600" width="1000"><span>修改</span> </a></li>
			</shiro:hasPermission>
			<shiro:hasPermission name="/ssadmin/removeICO.html">
				<li><a class="delete"
					href="/ssadmin/removeICO.html?id={sid_user}"
					target="ajaxTodo" title="确定要删除吗?"><span>删除</span> </a></li>
			</shiro:hasPermission>
			<shiro:hasPermission name="ssadmin/icoRefundAll.html">
				<li><a class="edit"
					href="/ssadmin/icoRefundAll.html?id={sid_user}"
					target="ajaxTodo" title="确定要退款吗?"><span>退款</span> </a></li>
			</shiro:hasPermission>
			<shiro:hasPermission name="ssadmin/icoRequiteAll.html">
				<li><a class="edit"
					   href="/ssadmin/goICORequiteJSP.html?url=ssadmin/ico/requite&icoId={sid_user}"
					   target="dialog" rel=icoRequite height="400" width="800"><span>发放回报</span> </a></li>
			</shiro:hasPermission>
		</ul>
		<ul></ul>
	</div>
	<table class="table" width="100%" layoutH="138">
		<thead>
			<tr>
				<c:if test="${!empty param.lookup}">
					<th width="40">查找带回</th>
				</c:if>
				<th width="40">ID</th>
				<th width="40">ICO名称</th>
				<th width="40">ICO份额</th>
				<th width="40">填充份额</th>
				<th width="40">已售出量</th>
				<th width="40">限购份额</th>
				<th width="40">最小认购份额</th>
				<th width="40">回报比例</th>
				<th width="40">已认购次数</th>
				<th width="40">状态</th>
				<th width="40">开始时间</th>
				<th width="40">结束时间</th>
				<th width="40">创建时间</th>
				<th width="40">更新时间</th>
			</tr>
		</thead>
		<tbody>
			<c:forEach items="${list}" var="item" varStatus="num">
				<tr target="sid_user" rel="${item.id}">
					<c:if test="${!empty param.lookup}">
						<td><a class="btnSelect" href="javascript:$.bringBack({id:'${item.id}'})" title="查找带回">选择</a></td>
					</c:if>
					<td>${item.id}</td>
					<td>${item.name}</td>
					<td><fmt:formatNumber pattern="#.####" value="${item.amount}" /></td>
					<td><fmt:formatNumber pattern="#.####" value="${item.supplyAmount}" /></td>
					<td><fmt:formatNumber pattern="#.####" value="${item.rightAmount}" /></td>
					<td><fmt:formatNumber pattern="#.####" value="${item.limitAmount}" /></td>
					<td><fmt:formatNumber pattern="#.####" value="${item.minBuyAmount}" /></td>
					<td><fmt:formatNumber pattern="#.####" value="${item.requiteRate}" /></td>
					<td>${item.supportCount}</td>
					<td>${item.status.name}</td>
					<td><fmt:formatDate value="${item.startTime}" pattern="yyyy-MM-dd HH:mm:ss" /></td>
					<td><fmt:formatDate value="${item.endTime}" pattern="yyyy-MM-dd HH:mm:ss" /></td>
					<td><fmt:formatDate value="${item.createTime}" pattern="yyyy-MM-dd HH:mm:ss" /></td>
					<td><fmt:formatDate value="${item.updateTime}" pattern="yyyy-MM-dd HH:mm:ss" /></td>
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

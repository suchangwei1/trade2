<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="comm/include.inc.jsp"%>
<form id="pagerForm" method="post" action="ssadmin/activityList.html">
	<input type="hidden" name="status" value="${param.status}"> <input
		type="hidden" name="keywords" value="${keywords}" /> <input
		type="hidden" name="type" value="${ftype}" /><input type="hidden"
		name="pageNum" value="${currentPage}" /> <input type="hidden"
		name="numPerPage" value="${numPerPage}" /> <input type="hidden"
		name="orderField" value="${param.orderField}" /><input type="hidden"
		name="orderDirection" value="${param.orderDirection}" />
</form>


<div class="pageHeader">
	<form onsubmit="return navTabSearch(this);"
		action="ssadmin/activityList.html" method="post">
		<div class="searchBar">

			<table class="searchContent">
				<tr>
					<td>关键词：<input type="text" name="keywords" value="${keywords}"
						size="60" />[标题、内容]</td>
					<td>虚拟币类型： <select type="combox" name="ftype">
							<c:forEach items="${typeMap}" var="type">
								<c:if test="${type.key == ftype}">
									<option value="${type.key}" selected="true">${type.value}</option>
								</c:if>
								<c:if test="${type.key != ftype}">
									<option value="${type.key}">${type.value}</option>
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
						</div>
					</li>
				</ul>
			</div>
		</div>
	</form>
</div>
<div class="pageContent">
	<div class="panelBar">
		<ul class="toolBar">
			<shiro:hasPermission name="ssadmin/viewActivity.html">
				<li><a class="add"
					href="ssadmin/goActivityJSP.html?url=ssadmin/viewActivity&uid={sid_user}"
					height="500" width="800" target="dialog" rel="viewActivity"><span>查看</span>
				</a></li>
			</shiro:hasPermission>
			<shiro:hasPermission name="ssadmin/addActivity.html">
				<li><a class="add"
					href="ssadmin/goActivityJSP.html?url=ssadmin/addActivity"
					height="500" width="800" target="dialog" rel="addActivity"><span>新增</span>
				</a></li>
			</shiro:hasPermission>
			<shiro:hasPermission name="ssadmin/updateActivity.html">
				<li><a class="edit"
					href="ssadmin/goActivityJSP.html?url=ssadmin/updateActivity&uid={sid_user}"
					height="500" width="800" target="dialog" rel="updateActivity"><span>修改</span>
				</a></li>
			</shiro:hasPermission>
			<shiro:hasPermission name="ssadmin/deleteActivity.html">
				<li><a class="delete"
					href="ssadmin/deleteActivity.html?uid={sid_user}" target="ajaxTodo"
					title="确定要删除吗?"><span>删除</span> </a></li>
			</shiro:hasPermission>
			<shiro:hasPermission name="ssadmin/updateATStatus.html?type=1">
				<li><a class="edit"
					href="ssadmin/updateATStatus.html?type=1&uid={sid_user}"
					target="ajaxTodo" title="确定要启用吗?"><span>启用</span> </a></li>
			</shiro:hasPermission>
			<shiro:hasPermission name="ssadmin/updateATStatus.html?type=2">
				<li><a class="edit"
					href="ssadmin/updateATStatus.html?type=2&uid={sid_user}"
					target="ajaxTodo" title="确定要禁用吗?"><span>禁用</span> </a></li>
			</shiro:hasPermission>
			<shiro:hasPermission name="ssadmin/activityrewardList.html">
				<li><a class="edit"
					href="ssadmin/activityrewardList.html?&aid={sid_user}"
					target="navTab" rel="activityrewardList"><span>查看本人奖励列表</span>
				</a></li>
			</shiro:hasPermission>
			<shiro:hasPermission name="ssadmin/promotionActivityRewardList.html">
				<li><a class="edit"
					href="ssadmin/promotionActivityRewardList.html?aid={sid_user}"
					target="navTab" rel="promotionActivityRewardList"><span>查看推荐人奖励列表</span>
				</a></li>
			</shiro:hasPermission>
		</ul>
	</div>
	<table class="table" width="100%" layoutH="138">
		<thead>
			<tr>
				<th width="20">序号</th>
				<th width="60">标题</th>
				<th width="60" orderField="fstatus"
					<c:if test='${param.orderField == "fstatus" }'> class="${param.orderDirection}"  </c:if>>状态</th>
				<th width="60" orderField="factivitytype.fname"
					<c:if test='${param.orderField == "factivitytype.fname" }'> class="${param.orderDirection}"  </c:if>>活动类型</th>
				<th width="60">内容</th>
				<th width="60" orderField="fcreateTime"
					<c:if test='${param.orderField == "fcreateTime" }'> class="${param.orderDirection}"  </c:if>>创建日期</th>
				<th width="60" orderField="flastUpdateTime"
					<c:if test='${param.orderField == "flastUpdateTime" }'> class="${param.orderDirection}"  </c:if>>更新日期</th>
			</tr>
		</thead>
		<tbody>
			<c:forEach items="${activityList}" var="activity" varStatus="num">
				<tr target="sid_user" rel="${activity.fid}">
					<td>${num.index +1}</td>
					<td>${activity.ftitle}</td>
					<td>${activity.fstatus_s}</td>
					<td>${activity.factivitytype.fvirtualCoinType.fname}${activity.factivitytype.fname}</td>
					<td>${activity.fcontent}</td>
					<td>${activity.fcreateTime}</td>
					<td>${activity.flastUpdateTime}</td>
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

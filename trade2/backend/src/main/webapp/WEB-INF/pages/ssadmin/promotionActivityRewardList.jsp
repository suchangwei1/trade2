<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="comm/include.inc.jsp"%>
<form id="pagerForm" method="post"
	action="ssadmin/promotionActivityRewardList.html">
	<input type="hidden" name="status" value="${param.status}"> <input
		type="hidden" name="keywords" value="${keywords}" /> <input
		type="hidden" name="type" value="${ftype}" /><input type="hidden"
		name="aid" value="${aid}" /><input type="hidden" name="pageNum"
		value="${currentPage}" /> <input type="hidden" name="numPerPage"
		value="${numPerPage}" /> <input type="hidden" name="orderField"
		value="${param.orderField}" /><input type="hidden"
		name="orderDirection" value="${param.orderDirection}" />
</form>


<div class="pageHeader">
	<form onsubmit="return navTabSearch(this);"
		action="ssadmin/promotionActivityRewardList.html" method="post">
		<div class="searchBar">

			<table class="searchContent">
				<tr>
					<input type="hidden" name="aid" value="${aid}" size="60" />
					<td>活动标题：<input type="text" name="keywords"
						value="${keywords}" size="60" />
					</td>
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
			<shiro:hasPermission name="ssadmin/addPromotionActivityReward.html">
				<li><a class="add"
					href="ssadmin/goPromotionActivityRewardJSP.html?url=ssadmin/addPromotionActivityReward&aid=${aid}"
					height="350" width="800" target="dialog"
					rel="addPromotionActivityReward"><span>新增</span> </a></li>
			</shiro:hasPermission>
			<shiro:hasPermission
				name="ssadmin/updatePromotionActivityReward.html">
				<li><a class="edit"
					href="ssadmin/goPromotionActivityRewardJSP.html?url=ssadmin/updatePromotionActivityReward&uid={sid_user}"
					height="350" width="800" target="dialog"
					rel="updatePromotionActivityReward"><span>修改</span> </a></li>
			</shiro:hasPermission>
			<shiro:hasPermission
				name="ssadmin/deletePromotionActivityReward.html">
				<li><a class="delete"
					href="ssadmin/deletePromotionActivityReward.html?uid={sid_user}"
					target="ajaxTodo" title="确定要删除吗?"><span>删除</span> </a></li>
			</shiro:hasPermission>
		</ul>
	</div>
	<table class="table" width="100%" layoutH="138">
		<thead>
			<tr>
				<th width="20">序号</th>
				<th width="60">活动标题</th>
				<th width="60">奖励虚拟币类型</th>
				<th width="60">是否奖励虚拟币</th>
				<th width="60">是否比率</th>
				<th width="60">奖励数量</th>
				<th width="60">创建日期</th>
			</tr>
		</thead>
		<tbody>
			<c:forEach items="${promotionActivityRewardList}"
				var="activityreward" varStatus="num">
				<tr target="sid_user" rel="${activityreward.fid}">
					<td>${num.index +1}</td>
					<td>${activityreward.factivity.ftitle}</td>
					<td><c:choose>
							<c:when test="${activityreward.fvirtualcointype.fname == null}">
							人民币
						</c:when>
							<c:otherwise>
							${activityreward.fvirtualcointype.fname}
						</c:otherwise>
						</c:choose>
					</td>
					<td>${activityreward.fvirtualCoinOrCny}</td>
					<td>${activityreward.frateOrReal}</td>
					<td>${activityreward.famount}</td>
					<td>${activityreward.fcreateTime}</td>
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

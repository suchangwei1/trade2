<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="comm/include.inc.jsp"%>
<form id="pagerForm" method="post" action="ssadmin/gameRuleList.html">
	<input type="hidden" name="status" value="${param.status}"> <input
		type="hidden" name="keywords" value="${keywords}" /> <input
		type="hidden" name="pageNum" value="${currentPage}" /> <input
		type="hidden" name="numPerPage" value="${numPerPage}" /> <input
		type="hidden" name="orderField" value="${param.orderField}" /><input
		type="hidden" name="orderDirection" value="${param.orderDirection}" />
</form>


<div class="pageHeader">
	<form onsubmit="return navTabSearch(this);"
		action="ssadmin/gameRuleList.html" method="post">
		<div class="searchBar">
			<table class="searchContent">
				<tr>
					<td>会员等级：<input type="text" name="keywords"
						value="${keywords}" size="60" /></td>
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
			<shiro:hasPermission name="ssadmin/updateGameRule.html">
				<li><a class="edit"
					href="ssadmin/goGameRuleJSP.html?url=ssadmin/updateGameRule&uid={sid_user}"
					height="470" width="800" target="dialog" rel="updateGameRule"><span>修改</span>
				</a></li>
			</shiro:hasPermission>
		</ul>
	</div>
	<table class="table" width="150%" layoutH="138">
		<thead>
			<tr>
				<th width="20">序号</th>
				<th width="60">会员等级</th>
				
				<th width="60">升级消耗虚拟币类型</th>
				<th width="60">升级消耗数量</th>
				<th width="60">升级所需推荐注册数</th>
				
				<th width="60">种植和收获虚拟币类型</th>
				<th width="60">种植消耗数量/</th>
				<th width="60">可种时长</th>
				<th width="60">每小时收获数量</th>
				
				<th width="60">购买土地消耗虚拟币类型</th>
				<th width="60">每块土地消耗数量</th>
				
 				<th width="60">增产概率</th>
				<th width="60">减产概率</th>

                <th width="60">种植类型</th>
                
                <th width="60">赠送土地</th>
                <th width="60">土地总数量</th>

				<th width="60">最后修改时间</th>
			</tr>
		</thead>
		<tbody>
			<c:forEach items="${gameRuleList}" var="gameRule" varStatus="num">
				<tr target="sid_user" rel="${gameRule.fid}">
					<td>${num.index +1}</td>
					<td>${gameRule.flevel}</td>
					
					<td>${gameRule.fupgradeCoinType.fname}</td>
					<td>${gameRule.fupgradeNeedQty}</td>
				    <td>${gameRule.fupgradeNeedReQty}</td>
					
					<td>${gameRule.fvirtualcointype.fname}</td>
					<td>${gameRule.fexpendQty}/每块</td>
					<td>${gameRule.fcanZdtimes}</td>
					<td>${gameRule.fharvestQty}/每块</td>
					
					<td>${gameRule.fvirtualcointype1.fname}</td>
					<td>${gameRule.fbuyQty}</td>
					
					<td>${gameRule.fgoodRate}</td>
					<td>${gameRule.fbadRate}</td>

                    <td>${gameRule.ftype_s}</td>
                    
                    <td>${gameRule.fsendqty}</td>
                    <td>${gameRule.ftotalqty}</td>

					<td>${gameRule.flastUpdateTime}</td>
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

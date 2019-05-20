<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="comm/include.inc.jsp"%>
<form id="pagerForm" method="post" action="ssadmin/gameList.html">
	<input type="hidden" name="status" value="${param.status}"> <input type="hidden"
		name="keywords" value="${keywords}" /> <input type="hidden" name="pageNum" value="${currentPage}" />
	<input type="hidden" name="numPerPage" value="${numPerPage}" /> <input type="hidden"
		name="orderField" value="${param.orderField}" /><input type="hidden" name="orderDirection"
		value="${param.orderDirection}" />
</form>


<div class="pageHeader">
	<form onsubmit="return navTabSearch(this);" action="ssadmin/gameList.html" method="post">
		<div class="searchBar">
			<table class="searchContent">
				<tr>
					<td></td>
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
			<shiro:hasPermission name="ssadmin/updateGame.html">
				<li><a class="edit" href="ssadmin/goGameJSP.html?url=ssadmin/updateGame&uid={sid_user}"
					height="470" width="800" target="dialog" rel="updateGame"><span>修改</span> </a></li>
			</shiro:hasPermission>
		</ul>
	</div>
	<table class="table" width="100%" layoutH="138">
		<thead>
			<tr>
				<th width="20">序号</th>
				<th width="100">游戏简介</th>
				<th width="60">最小收获时间</th>
				<th width="60">变草时长</th>
				<th width="60">开通消耗币类型</th>
				<th width="60">开通消耗币数量</th>
				<th width="60">开通冻结币类型</th>
				<th width="60">开通冻结币数量</th>
				<th width="60">除草消耗币类型</th>
				<th width="60">除草消耗币数量</th>
				<th width="60">有效时间</th>
				<th width="60">最后修改时间</th>
			</tr>
		</thead>
		<tbody>
			<c:forEach items="${gameList}" var="game" varStatus="num">
				<tr target="sid_user" rel="${game.fid}">
					<td>${num.index +1}</td>
					<td>${game.fdescription}</td>
					<td>${game.fminHarvestTime}</td>
					<td>${game.fgrassTime}</td>
					<td>${game.fvirtualcointype.fname}</td>
					<td>${game.fqty}</td>
					<td>${game.ffrozenvirtualcointype.fname}</td>
					<td>${game.ffrozenqty}</td>
					<td>${game.fgrassvirtualcointype.fname}</td>
					<td>${game.fgrassqty}</td>
					<td>${game.fdays}</td>
					<td>${game.flastUpdateTime}</td>
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

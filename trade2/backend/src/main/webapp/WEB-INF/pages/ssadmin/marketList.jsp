<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="comm/include.inc.jsp"%>
<form id="pagerForm" method="post" action="ssadmin/marketList.html">
	<input type="hidden" name="status" value="${param.status}">
	<input type="hidden" name="tradeStatus" value="${param.tradeStatus}">
	<input type="hidden" name="keywords" value="${keywords}" />
	<input type="hidden" name="pageNum" value="${currentPage}" />
	<input type="hidden" name="numPerPage" value="${numPerPage}" />
	<input type="hidden" name="orderField" value="${param.orderField}" />
	<input type="hidden" name="orderDirection" value="${param.orderDirection}" />
</form>

<div class="pageHeader">
	<form onsubmit="return navTabSearch(this);"
		action="ssadmin/marketList.html" method="post">
		<div class="searchBar">
			<table class="searchContent">
				<tr>
					<td>
						状态：
						<select name="status">
							<option value="">全部</option>
							<option <c:if test="${'0' == param.status}">selected</c:if> value="0">禁用</option>
							<option <c:if test="${'1' == param.status}">selected</c:if> value="1">开启</option>
						</select>
					</td>
					<td>
						交易状态：
						<select name="tradeStatus">
							<option value="">全部</option>
							<option <c:if test="${'0' == param.tradeStatus}">selected</c:if> value="0">不开放</option>
							<option <c:if test="${'1' == param.tradeStatus}">selected</c:if> value="1">已开放</option>
						</select>
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
			<%--<shiro:hasPermission name="ssadmin/createMarket.html">--%>
				<li><a class="add"
					href="ssadmin/goMarketJSP.html?url=ssadmin/addMarket"
					height="500" width="900" target="dialog" rel="createMarket"><span>创建</span>
				</a>
				</li>
			<%--</shiro:hasPermission>--%>
			<%--<shiro:hasPermission name="ssadmin/updateMarket.html">--%>
				<li><a class="edit"
					href="ssadmin/goMarketJSP.html?url=ssadmin/updateMarket&id={sid_user}"
					height="500" width="900" target="dialog" rel="updateMarket"><span>修改</span>
				</a>
				</li>
			<%--</shiro:hasPermission>--%>
			<%--<shiro:hasPermission name="ssadmin/startMarket.html">--%>
				<li>
					<a class="edit" href="ssadmin/startMarket.html?id={sid_user}" title="确定要启用交易市场吗？" target="ajaxTodo" rel="startMarket"><span>启用</span>
				</a>
				</li>
			<%--</shiro:hasPermission>--%>
			<%--<shiro:hasPermission name="ssadmin/stopMarket.html">--%>
				<li>
					<a class="edit" href="ssadmin/stopMarket.html?id={sid_user}" title="确定要禁用交易市场吗？" target="ajaxTodo" rel="startMarket"><span>禁用</span>
				</a>
				</li>
			<%--</shiro:hasPermission>--%>
			<%--<shiro:hasPermission name="ssadmin/startMarketTrade.html">--%>
				<li>
					<a class="edit" href="ssadmin/startMarketTrade.html?id={sid_user}" title="确定要开放交易吗？" target="ajaxTodo" rel="startMarket"><span>开放交易</span>
				</a>
				</li>
			<%--</shiro:hasPermission>--%>
			<%--<shiro:hasPermission name="ssadmin/stopMarketTrade.html">--%>
				<li>
					<a class="edit" href="ssadmin/stopMarketTrade.html?id={sid_user}" title="确定要关闭交易吗？" target="ajaxTodo" rel="startMarket"><span>关闭交易</span>
				</a>
				</li>
			<%--</shiro:hasPermission>--%>
		</ul>
	</div>
	<table class="table" width="100%" layoutH="138">
		<thead>
			<tr>
				<th width="20">ID</th>
				<th width="60">市场</th>
				<th width="60">交易币种</th>
				<th width="60">保留小数位</th>
				<th width="60">状态</th>
				<th width="60">交易状态</th>
				<th width="60">交易时间</th>
				<th width="60">涨跌幅</th>
				<th width="60">买入手续费</th>
				<th width="60">卖出手续费</th>
				<th width="60">最小交易量</th>
				<th width="60">最大交易量</th>
				<th width="60">最小交易价</th>
				<th width="60">最大交易价</th>
				<th width="60">最小交易额</th>
				<th width="60">最大交易额</th>
			</tr>
		</thead>
		<tbody>
			<c:forEach items="${list}" var="item" varStatus="num">
				<tr target="sid_user" rel="${item.id}">
					<td>${item.id}</td>
					<td>${coinMap[item.buyId + 0].fname}</td>
					<td>${coinMap[item.sellId + 0].fname}</td>
					<td>${item.decimals}</td>
					<td>
						<c:choose>
							<c:when test="${0 == item.status}">禁用</c:when>
							<c:when test="${1 == item.status}">启用</c:when>
						</c:choose>
					</td>
					<td>
						<c:choose>
							<c:when test="${0 == item.tradeStatus}">不开放</c:when>
							<c:when test="${1 == item.tradeStatus}">已开放</c:when>
						</c:choose>
					</td>
					<td>${item.tradeTime}</td>
					<td><fmt:formatNumber value="${item.updown * 100}" pattern="#.##" />%</td>
					<td><fmt:formatNumber value="${item.buyFee * 100}" pattern="#.##"/>%</td>
					<td><fmt:formatNumber value="${item.sellFee * 100}" pattern="#.##"/>%</td>
					<td><fmt:formatNumber value="${item.minCount}" pattern="#.########" /></td>
					<td><fmt:formatNumber value="${item.maxCount}" pattern="#.########" /></td>
					<td><fmt:formatNumber value="${item.minPrice}" pattern="#.########" /></td>
					<td><fmt:formatNumber value="${item.maxPrice}" pattern="#.########" /></td>
					<td><fmt:formatNumber value="${item.minMoney}" pattern="#.########" /></td>
					<td><fmt:formatNumber value="${item.maxMoney}" pattern="#.########" /></td>
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

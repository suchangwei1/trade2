<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="comm/include.inc.jsp"%>
<form id="pagerForm" method="post" action="ssadmin/profitList.html">
	<input type="hidden" name="keywords" value="${keywords}" />
	<input type="hidden" name="pageNum" value="${currentPage}" />
	<input type="hidden" name="numPerPage" value="${numPerPage}" />
	<input type="hidden" name="orderField" value="${param.orderField}" />
	<input type="hidden" name="orderDirection" value="${param.orderDirection}" />
	<input type="hidden" name="createTime" value="${createTime}" />
	<input type="hidden" name="shareTime" value="${shareTime}" />
	<input type="hidden" name="type" value="${type}" />
	<input type="hidden" name="coin" value="${coin}" />
	<input type="hidden" name="market" value="${market}" />

</form>


<div class="pageHeader">
	<form onsubmit="return navTabSearch(this);"
		action="ssadmin/profitList.html" method="post">
		<div class="searchBar">

			<table class="searchContent">
				<tr>
					<td>上级用户：<input type="text" name="keywords" value="${keywords}" size="60" placeholder="上级用户ID" /> </td>
					<td>交易对： <select type="combox" name="market">
						<c:forEach items="${typeMap}" var="type">
							<c:if test="${type.key == market}">
								<option value="${type.key}" selected="true">${type.value}</option>
							</c:if>
							<c:if test="${type.key != market}">
								<option value="${type.key}">${type.value}</option>
							</c:if>
						</c:forEach>
					</select>
					</td>
					<td>交易类型： <select type="combox" name="type">
						<option value="">全部</option>
						<option value="0" <c:if test="${type == 0}">selected</c:if>>买入</option>
						<option value="1" <c:if test="${type == 1}">selected</c:if>>卖出</option>
					</select>
					</td>
					<td>分润币种： <select type="combox" name="coin">

							<option value="">全部</option>


						<c:forEach items="${fvirtualcointypeMap}" var="type">
							<c:if test="${type.value == coin}">
								<option value="${type.value}" selected="true">${type.value}</option>
							</c:if>
							<c:if test="${type.value != coin}">
								<option value="${type.value}">${type.value}</option>
							</c:if>
						</c:forEach>
					</select>
					</td>

					<td>创建时间： <input type="text" name="createTime" class="date"
									 readonly="true" value="${createTime }" />
					</td>
					<td>分润时间： <input type="text" name="shareTime" class="date"
									 readonly="true" value="${shareTime }" />
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
      <shiro:hasPermission name="ssadmin/entrustlogList.html">

				<li><a class="icon" target="dwzExport" title="确定要导出分润列表吗？" href="/ssadmin/exportProfitListTable.html"><span>导出列表</span></a>
				</li>
	  </shiro:hasPermission>
		</ul>
	</div>
	<table class="table" width="100%" layoutH="138">
		<thead>
			<tr>
				<th width="20">序号</th>
				<th width="100">上级用户</th>
				<th width="60">下级用户</th>
				<th width="60">交易对</th>
				<th width="60">交易类型</th>
				<th width="60">分润数量</th>
				<th width="60">分润币种</th>
				<th width="60" orderField="shareTime"
						<c:if test='${param.orderField == "shareTime" }'> class="${param.orderDirection}"  </c:if>>分润时间</th>
				<th width="60" orderField="createTime"
						<c:if test='${param.orderField == "createTime" }'> class="${param.orderDirection}"  </c:if>>生成时间</th>


			</tr>
		</thead>
		<tbody>
			<c:forEach items="${list}" var="tradeFeesShare" varStatus="num">
				<tr target="sid_user" rel="${tradeFeesShare.id}">
					<td>${num.index +1}</td>
					<td>${tradeFeesShare.parent.fid}</td>
					<td>${tradeFeesShare.child.fid} </td>
					<td>${typeMap[tradeFeesShare.market] }</td><!--交易对  -->
					<td>
						<c:if test="${tradeFeesShare.type == 0}">买入</c:if>
						<c:if test="${tradeFeesShare.type == 1}">卖出</c:if>
					</td>
					<td>${tradeFeesShare.amount}</td>
					<td>${tradeFeesShare.coin}</td>
					<td>${tradeFeesShare.shareTime}</td>
					<td>${tradeFeesShare.createTime}</td>

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

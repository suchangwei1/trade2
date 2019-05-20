<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="../comm/include.inc.jsp"%>
<form id="pagerForm" method="post" action="ssadmin/otcOrderLogList.html">
	<input type="hidden" name="fstatus" value="${param.fstatus}">
	<input type="hidden" name="coin" value="${param.coin}">
	<input type="hidden" name="id" value="${id}" />
	<input type="hidden" name="keywords" value="${keywords}" />
	<input type="hidden" name="startDate" value="${startDate}" />
	<input type="hidden" name="endDate" value="${endDate}" />
	<input type="hidden" name="ftype" value="${ftype}" />
	<input type="hidden" name="pageNum" value="${currentPage}" />
	<input type="hidden" name="numPerPage" value="${numPerPage}" />
	<input type="hidden" name="orderField" value="${param.orderField}" />
	<input type="hidden" name="orderDirection" value="${param.orderDirection}" />
</form>


<div class="pageHeader">
	<form onsubmit="return navTabSearch(this);"
		  action="ssadmin/otcOrderLogList.html" method="post">
		<div class="searchBar">

			<table class="searchContent">
				<tr>
					<td>订单ID：<input type="text" name="id"
									value="${id}" size="10" placeholder="订单ID"/></td>
					<td>会员信息：<input type="text" name="keywords"
									value="${keywords}" size="60" placeholder="用户ID/登录名/姓名/手机号/身份证号/邮箱"/></td>
					<td>订单类型： <select type="combox" name="ftype">
						<option value="" selected="true">全部</option>
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
					<td>订单状态： <select type="combox" name="fstatus">
						<option value="" selected="true">全部</option>
						<c:forEach items="${statusMap}" var="status">
							<c:if test="${status.key == fstatus}">
								<option value="${status.key}" selected="true">${status.value}</option>
							</c:if>
							<c:if test="${status.key != fstatus}">
								<option value="${status.key}">${status.value}</option>
							</c:if>
						</c:forEach>
					</select>
					</td>
					<td>币种： <select type="combox" name="coin">
						<option value="" selected="true">全部</option>
						<c:forEach items="${coinMap}" var="c">
							<c:if test="${c.key == coin}">
								<option value="${c.key}" selected="true">${c.value}</option>
							</c:if>
							<c:if test="${c.key != coin}">
								<option value="${c.key}">${c.value}</option>
							</c:if>
						</c:forEach>
					</select>
					</td>
					 <td>开始日期： <input type="text" name="startDate" class="date"
						readonly="true" value="${startDate }" /></td>
					<td>结束日期： <input type="text" name="endDate" class="date"
									 readonly="true" value="${endDate }" /></td>
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
			<shiro:hasPermission name="ssadmin/otccheck.html">
				<li><a class="edit"
					   href="ssadmin/otcUpdate.html?uid={sid_user}&status=2"
					   target="ajaxTodo" title="确定买方已付款？"><span>完成订单</span> </a></li>
			</shiro:hasPermission>
			<shiro:hasPermission name="ssadmin/otccheck.html">
				<li><a class="delete"
					   href="ssadmin/otcUpdate.html?uid={sid_user}&status=3"
					   target="ajaxTodo" title="确定买方未付款？"><span>取消订单</span> </a></li>
			</shiro:hasPermission>
			<shiro:hasPermission name="ssadmin/otccheck.html">
				<li><a class="icon" href="ssadmin/otcOrderLogExport.html"
					   target="dwzExport" targetType="navTab" title="是要导出这些记录吗?"><span>导出</span>
				</a></li>
			</shiro:hasPermission>
		</ul>
		<ul></ul>
	</div>
	<table class="table" width="100%" layoutH="138">
		<thead>
		<tr>
			<th width="40">订单ID</th>
			<th width="40">对应挂单ID</th>
			<th width="40">挂单用户</th>
			<th width="40">币种</th>
			<th width="40">用户</th>
			<th width="40">类型</th>
			<th width="40">价格</th>
			<th width="40">数量</th>
			<th width="40">支付方式</th>
			<th width="40">买方申诉理由</th>
			<th width="40">卖方申诉理由</th>
			<th width="40">状态</th>
			<th width="60" orderField="createTime"
					<c:if test='${param.orderField == "createTime" }'> class="${param.orderDirection}"  </c:if>>生成时间</th>
			<th width="60" orderField="updateTime"
					<c:if test='${param.orderField == "updateTime" }'> class="${param.orderDirection}"  </c:if>>更新时间</th>
		</tr>
		</thead>
		<tbody>
		<c:forEach items="${list}" var="item" varStatus="num">
			<tr target="sid_user" rel="${item.id}">
				<td>${item.id}</td>
				<td>${item.otcOrder.fuser.frealName}</td>
				<td>${item.otcOrder.id}</td>
				<td>${item.otcOrder.fvirtualcointype.fShortName}</td>
				<td>${item.fuser.frealName}</td>
				<td>${item.otcOrder.type == 0 ? "卖出": "买入"}</td>
				<td><fmt:formatNumber value="${item.otcOrder.price}" pattern="##.##"/></td>
				<td><fmt:formatNumber value="${item.amount}" pattern="##.##"/></td>
				<td>${item.pay_s}</td>
				<td>${item.buyerNote != null? item.buyerNote: "--"}</td>
				<td>${item.sllerNote != null? item.sllerNote: "--"}</td>
				<td>${item.status_s}</td>
				<td>${item.createTime}</td>
				<td>${item.updateTime}</td>
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

<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="../comm/include.inc.jsp"%>
<form id="pagerForm" method="post" action="/ssadmin/icoRecordList.html">
	<input type="hidden" name="keyword" value="${param.keyword}"/>
	<input type="hidden" name="status" value="${param.status}"/>
	<input type="hidden" name="userId" value="${param.userId}"/>
	<input type="hidden" name="icoId" value="${param.icoId}"/>
	<input type="hidden" name="coinType" value="${param.coinType}"/>
	<input type="hidden" name="pageNum" value="${currentPage}"/>
	<input type="hidden" name="orderField" value="${param.orderField}" />
	<input type="hidden" name="orderDirection" value="${param.orderDirection}"/>
</form>


<div class="pageHeader">
	<form onsubmit="return navTabSearch(this);"
		action="/ssadmin/icoRecordList.html" method="post">
		<div class="searchBar">

			<table class="searchContent">
				<tr>
					<td>会员信息：<input type="text" name="keyword" value="${param.keyword}" size="30" />[ICO名称、会员邮箱、会员昵称]</td>
					<td>会员ID：<input type="text" name="userId" value="${param.userId}" size="10"></td>
					<td>ICO ID：<input type="text" name="icoId" value="${param.icoId}" size="10"></td>
					<td>状态：
						<select name="status">
							<option value="">全部</option>
							<option <c:if test="${!empty param.status && 0 == param.status}">selected</c:if> value="0">认购中</option>
							<option <c:if test="${!empty param.status && 1 == param.status}">selected</c:if> value="1">已认购</option>
							<option <c:if test="${!empty param.status && 2 == param.status}">selected</c:if> value="2">已退款</option>
							<option <c:if test="${!empty param.status && 3 == param.status}">selected</c:if> value="2">已回报</option>
						</select>
					</td>
					<td>消费币种：
						<select name="coinType">
							<option value="">全部</option>
							<option <c:if test="${!empty param.coinType && param.coinType == 0}">selected</c:if> value="0">人民币</option>
							<c:forEach items="${coinMap}" var="coin">
								<option <c:if test="${param.coinType == coin.key}">selected</c:if> value="${coin.key}">${coin.value.fname}</option>
							</c:forEach>
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
			<shiro:hasPermission name="ssadmin/icoRequite.html">
				<li><a class="edit"
					   href="/ssadmin/goICORequiteJSP.html?url=ssadmin/ico/requite&recordId={sid_user}"
					   target="dialog" rel=icoRequite height="400" width="800"><span>发放回报</span> </a></li>
			</shiro:hasPermission>
			<shiro:hasPermission name="ssadmin/icoRefund.html">
				<li><a class="edit" href="ssadmin/icoRefund.html?id={sid_user}" target="ajaxTodo" title="确定要退款吗?"><span>退款</span> </a></li>
			</shiro:hasPermission>
		</ul>
	</div>
	<table class="table" width="100%" layoutH="138">
		<thead>
			<tr>
				<th width="40">ID</th>
				<th width="40">ICO名称</th>
				<th width="40">会员手机号</th>
				<th width="40">会员邮箱</th>
				<th width="40">会员真实姓名</th>
				<th width="40">消费币种</th>
				<th width="40">购买数量</th>
				<th width="40">消费币量</th>
				<th width="40">订单状态</th>
				<th width="40">订单备注</th>
				<th width="40">创建时间</th>
				<th width="40">更新时间</th>
			</tr>
		</thead>
		<tbody>
			<c:forEach items="${list}" var="item" varStatus="num">
				<tr target="sid_user" rel="${item.id}">
					<td>${item.id}</td>
					<td>${item.icoName}</td>
					<td>${item.ftelephone}</td>
					<td>${item.femail}</td>
					<td>${item.frealName}</td>
					<td>
						<c:choose>
							<c:when test="${0 == item.swapCoinType}">
								人民币
							</c:when>
							<c:otherwise>
								${coinMap[item.swapCoinType + 0].fname}
							</c:otherwise>
						</c:choose>
					</td>
					<td><fmt:formatNumber pattern="#.####" value="${item.amount}" /></td>
					<td><fmt:formatNumber pattern="#.####" value="${item.swapAmount}" /></td>
					<td>${item.status.name}</td>
					<td>${item.remark}</td>
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

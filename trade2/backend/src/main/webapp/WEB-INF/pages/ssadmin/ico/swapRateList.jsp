<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="../comm/include.inc.jsp"%>
<form id="pagerForm" method="post" action="/ssadmin/icoSwapRateList.html">
	<input type="hidden" name="keyword" value="${param.keyword}"/>
	<input type="hidden" name="icoId" value="${param.icoId}"/>
	<input type="hidden" name="coinType" value="${param.coinType}"/>
</form>


<div class="pageHeader">
	<form onsubmit="return navTabSearch(this);"
		action="/ssadmin/icoSwapRateList.html" method="post">
		<div class="searchBar">

			<table class="searchContent">
				<tr>
					<td>ICO信息：<input type="text" name="keyword" value="${param.keyword}" size="30" />[ICO名称]</td>
					<td>ICO ID：<input type="text" name="icoId" value="${param.icoId}" size="10"></td>
					<td>购买币种：
						<select name="coinType">
							<option value="">请选择</option>
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
			<shiro:hasPermission name="/ssadmin/saveICOSwapRate.html">
				<li><a class="add"
					   href="/ssadmin/goICOSwapJSP.html?url=ssadmin/ico/editSwapRate&icoId=${ico.id}"
					   target="dialog" rel=saveICOSwapRate height="400" width="800"><span>新增</span> </a></li>
			</shiro:hasPermission>
			<shiro:hasPermission name="/ssadmin/saveICOSwapRate.html">
				<li><a class="edit"
					   href="/ssadmin/goICOSwapJSP.html?url=ssadmin/ico/editSwapRate&id={sid_user}&icoId=${ico.id}"
					   target="dialog" rel=saveICOSwapRate height="400" width="800"><span>修改</span> </a></li>
			</shiro:hasPermission>
			<shiro:hasPermission name="/ssadmin/removeSwapRate.html">
				<li><a class="delete"
					   href="/ssadmin/removeSwapRate.html?id={sid_user}"
					   target="ajaxToDo" rel=removeSwapRate title="确定要删除吗?"><span>删除</span> </a></li>
			</shiro:hasPermission>
		</ul>
		<ul></ul>
	</div>
	<table class="table" width="100%" layoutH="138">
		<thead>
			<tr>
				<th width="40">序号</th>
				<th width="40">ICO ID</th>
				<th width="40">ICO名称</th>
				<th width="40">币名称</th>
				<th width="40">单位兑换量</th>
				<th width="50">开启时间</th>
				<th width="50">结束时间</th>
				<th width="40">创建时间</th>
				<th width="40">更新时间</th>
			</tr>
		</thead>
		<tbody>
			<c:forEach items="${list}" var="item" varStatus="num">
				<tr target="sid_user" rel="${item.id}">
					<td>${num.count}</td>
					<td>${item.icoId}</td>
					<td>${item.icoName}</td>
					<td>
						<c:choose>
							<c:when test="${0 == item.coinType}">
								人民币
							</c:when>
							<c:otherwise>
								${coinMap[item.coinType + 0].fname}
							</c:otherwise>
						</c:choose>
					</td>
					<td>${item.amount}</td>
					<td><fmt:formatDate value="${item.startTime}" pattern="yyyy-MM-dd HH:mm:ss" /></td>
					<td><fmt:formatDate value="${item.endTime}" pattern="yyyy-MM-dd HH:mm:ss" /></td>
					<td><fmt:formatDate value="${item.createTime}" pattern="yyyy-MM-dd HH:mm:ss" /></td>
					<td><fmt:formatDate value="${item.updateTime}" pattern="yyyy-MM-dd HH:mm:ss" /></td>
				</tr>
			</c:forEach>
		</tbody>
	</table>
</div>

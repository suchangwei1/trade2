<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="../comm/include.inc.jsp"%>
<form id="pagerForm" method="post" action="ssadmin/otcSetList.html">
	<input type="hidden" name="keywords" value="${keywords}" />
	<input type="hidden" name="pageNum" value="${currentPage}" />
	<input type="hidden" name="numPerPage" value="${numPerPage}" />
</form>


<div class="pageHeader">
	<form onsubmit="return navTabSearch(this);"
		  action="ssadmin/otcSetList.html" method="post">
		<div class="searchBar">

			<table class="searchContent">
				<tr>
					<td>会员信息：<input type="text" name="keywords"
									value="${keywords}" size="60" placeholder="用户ID/登录名/姓名/手机号/身份证号/邮箱"/></td>
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
				<li><a class="icon" href="ssadmin/otcSetExport.html"
					   target="dwzExport" targetType="navTab" title="是要导出这些记录吗?"><span>导出</span>
				</a></li>
			</shiro:hasPermission>
		</ul>
		<ul></ul>
	</div>
	<table class="table" width="100%" layoutH="138">
		<thead>
		<tr>
			<th width="40">用户名</th>
			<th width="40">银行</th>
			<th width="40">支行</th>
			<th width="40">银行卡号</th>
			<th width="40">支付宝账号</th>
			<th width="40">微信账号</th>
		</tr>
		</thead>
		<tbody>
		<c:forEach items="${list}" var="item" varStatus="num">
			<tr target="sid_user" rel="${item.id}">
				<td>${item.fuser.frealName}</td>
				<td>${item.bankName}</td>
				<td>${item.bankBranch}</td>
				<td>${item.bankAccount}</td>
				<td>${item.aliAccount}</td>
				<td>${item.wechatAccount}</td>
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

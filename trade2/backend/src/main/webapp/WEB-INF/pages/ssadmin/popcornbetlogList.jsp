<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="comm/include.inc.jsp"%>
<form id="pagerForm" method="post" action="ssadmin/popcornbetlogList.html"> <input
		type="hidden" name="keywords" value="${keywords}" /> <input
		type="hidden" name="fstatus" value="${fstatus}" /><input
		type="hidden" name="pageNum" value="${currentPage}" /> <input
		type="hidden" name="numPerPage" value="${numPerPage}" /> <input
		type="hidden" name="orderField" value="${param.orderField}" /><input
		type="hidden" name="orderDirection" value="${param.orderDirection}" />
</form>


<div class="pageHeader">
	<form onsubmit="return navTabSearch(this);"
		action="ssadmin/popcornbetlogList.html" method="post">
		<div class="searchBar">

			<table class="searchContent">
				<tr>
					<td>会员信息：<input type="text" name="keywords" value="${keywords}"
						size="30" />
					</td>
					<td>期数：<input type="text" name="term" value="${term}"
						size="30" />
					</td>
					<td>状态： <select type="combox" name="fstatus">
							<c:forEach items="${statusMap}" var="status">
								<c:if test="${status.key == fstatus}">
									<option value="${status.key}" selected="true">${status.value}</option>
								</c:if>
								<c:if test="${status.key != fstatus}">
									<option value="${status.key}">${status.value}</option>
								</c:if>
							</c:forEach>
					</select></td>
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
			<li><a title="确定要发奖吗?" target="selectedTodo" rel="ids"
				postType="string" href="ssadmin/sendPopcornbetlog.html" class="edit"><span>发奖</span>
			</a></li>
		</ul>
	</div>
	<table class="table" width="120%" layoutH="138">
		<thead>
			<tr>
				<th width="22"><input type="checkbox" group="ids"
					class="checkboxCtrl"></th>
				<th width="30">序号</th>
				<th width="70">会员登陆名</th>
				<th width="70">会员昵称</th>
				<th width="70">真实名字</th>
				<th width="60">状态</th>		
				<th width="60">期数</th>
				<th width="60">全站投中数量</th>
				<th width="60">全站投错数量</th>
				<th width="100">下注1(红绿)</th>
				<th width="100">下注数量1</th>
				<th width="100">开奖结果1</th>
				<th width="100">下注2(深浅)</th>
				<th width="100">下注数量2</th>
				<th width="100">开奖结果2</th>
				<th width="100">赢得数量</th>
				<th width="100">手续费</th>
				<th width="100">实际获得数量</th>
				<th width="100">创建时间</th>
		</tr>
		</thead>
		<tbody>
			<c:forEach items="${popcornbetlogList}" var="popcornbetlogList" varStatus="num">
				<tr target="sid_user" rel="${popcornbetlogList.fid}">
					<td><input name="ids" value="${popcornbetlogList.fid}"
						type="checkbox"></td>
					<td>${num.index +1}</td>
					<td>${popcornbetlogList.fuser.floginName}</td>
					<td>${popcornbetlogList.fuser.fnickName}</td>
					<td>${popcornbetlogList.fuser.frealName}</td>
					<td>${popcornbetlogList.fstatus_s}</td>
					<td>${popcornbetlogList.fpopcornlog.fid}</td>
					<td>${popcornbetlogList.fallWinQty}</td>
					<td>${popcornbetlogList.fallLostQty}</td>
					<td>${popcornbetlogList.fbetresult2_s}</td>
					<td>${popcornbetlogList.fbetQty2}</td>
					<td>${popcornbetlogList.factualResult2_s}</td>
					<td>${popcornbetlogList.fbetresult1_s}</td>
					<td>${popcornbetlogList.fbetQty1}</td>
					<td>${popcornbetlogList.factualResult1_s}</td>
					<td>${popcornbetlogList.fwinQty}</td>
					<td>${popcornbetlogList.ffees}</td>
					<td>${popcornbetlogList.factualWinQty}</td>
					<td>${popcornbetlogList.fcreatetime}</td>
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

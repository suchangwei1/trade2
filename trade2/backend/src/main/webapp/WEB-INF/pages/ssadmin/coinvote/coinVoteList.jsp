<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="../comm/include.inc.jsp"%>
<form id="pagerForm" method="post"
	action="/ssadmin/coinVoteList.html">
	<input type="hidden" name="status" value="${param.status}"> <input
		type="hidden" name="keywords" value="${keywords}" /> <input
		type="hidden" name="pageNum" value="${currentPage}" /> <input
		type="hidden" name="numPerPage" value="${numPerPage}" /> <input
		type="hidden" name="orderField" value="${param.orderField}" /><input
		type="hidden" name="orderDirection" value="${param.orderDirection}" />
</form>

<div class="pageHeader">
	<form onsubmit="return navTabSearch(this);"
		action="/ssadmin/coinVoteList.html" method="post">
		<div class="searchBar">

			<table class="searchContent">
				<tr>
					<td>关键字：<input type="text" name="keywords" value="${keywords}"
						size="60" />[名称、简称、描述]</td>
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
			<shiro:hasPermission name="ssadmin/saveCoinVote.html">
				<li><a class="add"
					href="ssadmin/goVirtualCoinVoteJSP.html?url=ssadmin/coinvote/addCoinVote"
					height="350" width="800" target="dialog" rel="addCoinVote"><span>新增</span>
				</a>
				</li>
			</shiro:hasPermission>
			<shiro:hasPermission name="ssadmin/updateCoinVote.html">
				<li><a class="edit"
					href="ssadmin/goVirtualCoinVoteJSP.html?url=ssadmin/coinvote/editCoinVote&uid={sid_user}"
					height="350" width="800" target="dialog" rel="editCoinVote"><span>修改</span>
				</a>
				</li>
			</shiro:hasPermission>
			<shiro:hasPermission name="ssadmin/updateCoinVoteStatus1.html">
				<li><a class="delete"
					href="ssadmin/updateCoinVoteStatus1.html?uid={sid_user}"
					target="ajaxTodo" title="确定要启动吗?"><span>启动</span> </a>
				</li>
			</shiro:hasPermission>
			<shiro:hasPermission name="ssadmin/updateCoinVoteStatus2.html">
				<li><a class="delete"
					href="ssadmin/updateCoinVoteStatus2.html?uid={sid_user}"
					target="ajaxTodo" title="确定要禁用吗?"><span>禁用</span> </a>
				</li>
			</shiro:hasPermission>
		</ul>
	</div>
	<table class="table" width="100%" layoutH="138">
		<thead>
			<tr>
				<th width="20">序号</th>
				<th width="20">中文名称</th>
				<th width="30">英文名称</th>
				<th width="60">简称</th>
				<th width="60">链接</th>
				<th width="60" orderField="fyes"
                    <c:if test='${param.orderField == "fyes" }'> class="${param.orderDirection}"  </c:if>>支持票</th>
				<th width="60" orderField="fno"
                    <c:if test='${param.orderField == "fno" }'> class="${param.orderDirection}"  </c:if>>否定票</th>
				<th width="60">总票</th>
				<th width="60" orderField="fstatus"
                    <c:if test='${param.orderField == "fstatus" }'> class="${param.orderDirection}"  </c:if>>状态</th>
			</tr>
		</thead>
		<tbody>
			<c:forEach items="${coinVoteList}" var="virtualCoinType"
				varStatus="num">
				<tr target="sid_user" rel="${virtualCoinType.fid}">
					<td>${num.index +1}</td>
					<td>${virtualCoinType.fcnName}</td>
					<td>${virtualCoinType.fenName}</td>
					<td>${virtualCoinType.fshortName}</td>
					<td>${virtualCoinType.furl}</td>
					<td>${virtualCoinType.fyes}</td>
					<td>${virtualCoinType.fno}</td>
					<td>${virtualCoinType.fyes-virtualCoinType.fno}</td>
					<td>${virtualCoinType.fstatus_s}</td>
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

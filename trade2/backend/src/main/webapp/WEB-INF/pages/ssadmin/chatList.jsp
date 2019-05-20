<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="comm/include.inc.jsp"%>
<form id="pagerForm" method="post" action="ssadmin/chatList.html">
	<input type="hidden" name="keywords" value="${keywords}" />
	<input type="hidden" name="nickName" value="${nickName}" />
	<input type="hidden" name="userId" value="${userId}" />
	<input type="hidden" name="pageNum" value="${currentPage}" />
	<input type="hidden" name="numPerPage" value="${numPerPage}" />
</form>
<div class="pageHeader">
	<form onsubmit="return navTabSearch(this);"
		action="ssadmin/chatList.html" method="post">
		<div class="searchBar">
			<table class="searchContent">
				<tr>
					<td>消息关键词：<input type="text" name="keywords" value="${keywords}"
						size="60" />
					</td>
					<td>昵称：<input type="text" name="nickName" value="${nickName}"
						size="60" />
					</td>
					<td>用户Id：<input id="userId" type="number" name="userId" value="${userId}"
									size="60"
									onkeyup="if(this.value.length==1){this.value=this.value.replace(/[^1-9]/g,'')}else{this.value=this.value.replace(/\D/g,'')}"
					/>
					</td>
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
			<shiro:hasPermission name="ssadmin/chatDelete.html">
				<li><a title="确实要删除这些记录吗?" target="selectedTodo" rel="ids"
					   postType="string" href="ssadmin/chatDelete.html"
					   class="edit"><span>批量删除</span> </a>
				</li>
			</shiro:hasPermission>
			<shiro:hasPermission name="ssadmin/deleteOldChat.html">
				<li><a title="确实要删除这些消息吗?" target="ajaxTodo"
					    href="ssadmin/deleteOldChat.html"
					   class="edit"><span>删除三天前</span> </a>
				</li>
			</shiro:hasPermission>
		</ul>
	</div>
	<table class="table" width="100%" layoutH="138">
		<thead>
			<tr>
				<th width="22"><input type="checkbox" group="ids"
									  class="checkboxCtrl">
				</th>
				<th width="20">序号</th>
				<th width="25">会员ID</th>
				<th width="20">登录名</th>
				<th width="20">昵称</th>
				<th width="20">真实姓名</th>
				<th width="20">邮箱</th>
				<th width="20">消息</th>
				<th width="20">发送时间</th>

			</tr>
		</thead>
		<tbody>
			<c:forEach items="${list}" var="chat" varStatus="num">
				<tr target="sid_user" rel="${chat.id}">
					<td><input name="ids" value="${chat.id}"
							   type="checkbox">
					</td>
					<td>${num.index +1}</td>
					<td>${chat.fuser.fid}</td>
					<td>${chat.fuser.floginName}</td>
					<td>${chat.fuser.fnickName}</td>
					<td>${chat.fuser.frealName}</td>
					<td>${chat.fuser.femail}</td>
					<td>${chat.message}</td>
					<td>${chat.createTime}</td>
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



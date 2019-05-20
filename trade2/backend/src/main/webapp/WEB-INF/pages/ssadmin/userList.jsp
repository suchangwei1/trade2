<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="comm/include.inc.jsp"%>
<form id="pagerForm" method="post" action="ssadmin/userList.html">
	<input type="hidden" name="status" value="${param.status}">
	<input type="hidden" name="fstatus" value="${param.fstatus}"><input
		type="hidden" name="keywords" value="${keywords}" /><input
		type="hidden" name="startDate" value="${startDate}" /> <input
		type="hidden" name="troUid" value="${troUid}" /> <input type="hidden"
		name="ftype" value="${ftype}" /> <input type="hidden" name="pageNum"
		value="${currentPage}" /> <input type="hidden" name="numPerPage"
		value="${numPerPage}" /> <input type="hidden" name="orderField"
		value="${param.orderField}" /> <input type="hidden"
		name="orderDirection" value="${param.orderDirection}" />
</form>


<div class="pageHeader">
	<form onsubmit="return navTabSearch(this);"
		action="ssadmin/userList.html" method="post">
		<div class="searchBar">

			<table class="searchContent">
				<tr>
					<td>会员信息：<input type="text" name="keywords"
						value="${keywords}" size="40" placeholder="用户ID/登录名/姓名/手机号/身份证号/邮箱"/></td>
					<td>会员状态： <select type="combox" name="ftype">
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
					<td>证件照片状态
						<select name="fstatus">
							<option value="-1">全部</option>
							<option value="0" <c:if test="${fstatus == 0}">selected</c:if> >未上传</option>
							<option value="1" <c:if test="${fstatus == 1}">selected</c:if>>待审核</option>
							<option value="2" <c:if test="${fstatus == 2}">selected</c:if>>已通过</option>
							<option value="3" <c:if test="${fstatus == 3}">selected</c:if>>未通过</option>
						</select>
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
			<shiro:hasPermission name="ssadmin/userForbbin.html?status=1">
				<li><a class="delete"
					href="ssadmin/userForbbin.html?uid={sid_user}&status=1&rel=listUser"
					target="ajaxTodo" title="确定要禁用吗?"><span>禁用</span> </a></li>
			</shiro:hasPermission>
			<shiro:hasPermission name="ssadmin/userForbbin.html?status=2">
				<li><a class="edit"
					href="ssadmin/userForbbin.html?uid={sid_user}&status=2&rel=listUser"
					target="ajaxTodo" title="确定要解除禁用吗?"><span>解除禁用</span> </a></li>
			</shiro:hasPermission>
			<shiro:hasPermission name="ssadmin/setNeedFee.html?fneedFee=false">
				<li><a class="edit"
					href="ssadmin/setNeedFee.html?uid={sid_user}&fneedFee=false&rel=listUser" target="ajaxTodo"
					height="200" width="800"><span>取消手续费</span> </a></li>
			</shiro:hasPermission>
			<shiro:hasPermission name="ssadmin/setNeedFee.html?fneedFee=true">
				<li><a class="edit" href="ssadmin/setNeedFee.html?uid={sid_user}&fneedFee=true&rel=listUser"
					target="ajaxTodo" height="200" width="800"><span>恢复手续费</span> </a></li>
			</shiro:hasPermission>
			<shiro:hasPermission name="ssadmin/setNeedFee.html?fneedFee=false">
				<li><a class="edit"
					href="ssadmin/setCanOtc.html?uid={sid_user}&canOtc=false&rel=listUser" target="ajaxTodo"
					height="200" width="800"><span>取消OTC挂单功能</span> </a></li>
			</shiro:hasPermission>
			<shiro:hasPermission name="ssadmin/setNeedFee.html?fneedFee=true">
				<li><a class="edit" href="ssadmin/setCanOtc.html?uid={sid_user}&canOtc=true&rel=listUser"
					target="ajaxTodo" height="200" width="800"><span>恢复OTC挂单功能</span> </a></li>
			</shiro:hasPermission>
			<shiro:hasPermission name="ssadmin/userForbbin.html?status=3">
				<li><a class="edit"
					href="ssadmin/userForbbin.html?uid={sid_user}&status=3&rel=listUser"
					target="ajaxTodo" title="确定要重设登陆密码吗?"><span>重设登陆密码</span> </a></li>
			</shiro:hasPermission>
			<shiro:hasPermission name="ssadmin/userForbbin.html?status=4">
				<li><a class="edit"
					href="ssadmin/userForbbin.html?uid={sid_user}&status=4&rel=listUser"
					target="ajaxTodo" title="确定要重设交易密码吗?"><span>重设交易密码</span> </a></li>
			</shiro:hasPermission>
			<%--<shiro:hasPermission name="ssadmin/cancelGoogleCode.html">--%>
				<%--<li><a class="edit"--%>
					<%--href="ssadmin/cancelGoogleCode.html?uid={sid_user}"--%>
					<%--target="ajaxTodo" title="确定要重设GOOGLE验证吗?"><span>重置GOOGLE</span>--%>
				<%--</a></li>--%>
			<%--</shiro:hasPermission>--%>
			<%--<shiro:hasPermission name="ssadmin/cancelPhone.html">--%>
				<%--<li><a class="edit"--%>
					<%--href="ssadmin/cancelPhone.html?uid={sid_user}" target="ajaxTodo"--%>
					<%--title="确定要重置手机绑定吗?"><span>重置手机绑定</span> </a></li>--%>
			<%--</shiro:hasPermission>--%>
			<%--<shiro:hasPermission name="ssadmin/cancelEmail.html">--%>
				<%--<li><a class="edit"--%>
					<%--href="ssadmin/cancelEmail.html?uid={sid_user}" target="ajaxTodo"--%>
					<%--title="确定要重置邮箱绑定吗?"><span>重置邮箱绑定</span> </a></li>--%>
			<%--</shiro:hasPermission>--%>
			<%--<shiro:hasPermission name="ssadmin/setUserNo.html">--%>
				<%--<li><a class="edit"--%>
					<%--href="ssadmin/goUserJSP.html?uid={sid_user}&url=ssadmin/setUserNo"--%>
					<%--target="dialog" rel="auditUser" height="200" width="800"><span>设置商家号</span>--%>
				<%--</a></li>--%>
			<%--</shiro:hasPermission>--%>
			<%--<shiro:hasPermission name="/ssadmin/collectionList.html">--%>
				<%--<li>--%>
					<%--<a class="edit" href="/ssadmin/collectionList.html?uid={sid_user}" height="700" width="800" target="dialog" rel="collectionList"><span>查看收藏项目</span></a>--%>
				<%--</li>--%>
			<%--</shiro:hasPermission>--%>
				<li class="line">line</li>
			<shiro:hasPermission name="ssadmin/userExport.html">
				<li><a class="icon" href="ssadmin/userExport.html"
					target="dwzExport" targetType="navTab" title="是要导出这些记录吗?"><span>导出</span>
				</a></li>
			</shiro:hasPermission>
			<shiro:hasPermission name="ssadmin/auditIdentify.html">
				<li>
					<a class="edit"
					   href="ssadmin/goUserJSP.html?uid={sid_user}&url=ssadmin/auditIdentifySeek&status=audit"
					   target="dialog" rel=auditIdentify height="600" width="800"><span>查看证件照片</span>
					</a>
				</li>
			</shiro:hasPermission>
			<shiro:hasPermission name="ssadmin/modifyIdentity.html">
				<li><a class="edit"
					   href="ssadmin/goUserJSP.html?uid={sid_user}&url=ssadmin/modifyIdentity&status=audit"
					   target="dialog" rel=modifyIdentity height="600" width="800"><span>修改证件信息</span>
				</a>
				</li>
			</shiro:hasPermission>
		</ul>
		<ul></ul>
	</div>
	<table class="table" width="200%" layoutH="138">
		<thead>
			<tr>
				<th width="40">会员UID</th>
				<th width="40">推荐人UID</th>
				<th width="60" orderField="floginName"
					<c:if test='${param.orderField == "floginName" }'> class="${param.orderDirection}"  </c:if>>会员登陆名</th>
				<th width="60" orderField="fstatus"
					<c:if test='${param.orderField == "fstatus" }'> class="${param.orderDirection}"  </c:if>>会员状态</th>
				<th width="60" orderField="fpostRealValidate"
					<c:if test='${param.orderField == "fpostRealValidate" }'> class="${param.orderDirection}"  </c:if>>证件是否提交</th>
				<th width="60" orderField="fhasRealValidate"
					<c:if test='${param.orderField == "fhasRealValidate" }'> class="${param.orderDirection}"  </c:if>>证件是否已审</th>
				<th width="60" orderField="fIdentityStatus"
					<c:if test='${param.orderField == "fhasRealValidate" }'> class="${param.orderDirection}"  </c:if>>证件照片状态</th>
				<th width="60">昵称</th>
				<th width="60">真实姓名</th>
				<th width="60">是否需要手续费</th>
				<th width="60">是否可以OTC挂单</th>
				<th width="60">电话号码</th>
				<th width="60">邮箱地址</th>
				<th width="60">证件号码</th>
				<th width="60" orderField="fregisterTime"
					<c:if test='${param.orderField == "fregisterTime" }'> class="${param.orderDirection}"  </c:if>>注册时间</th>
				<th width="60" orderField="flastLoginTime"
					<c:if test='${param.orderField == "flastLoginTime" }'> class="${param.orderDirection}"  </c:if>>上次登陆时间</th>
			</tr>
		</thead>
		<tbody>
			<c:forEach items="${userList}" var="user" varStatus="num">
				<tr target="sid_user" rel="${user.fid}">
					<td>${user.fid}</td>
					<td>${user.fIntroUser_id.fid}</td>
					<td>${user.floginName}</td>
					<td>${user.fstatus_s}</td>
					<td>${user.fpostRealValidate  ? "是": "否"}</td>
					<td>${user.fhasRealValidate  ? "是": "否"}</td>
					<td>
						<c:if test="${user.fIdentityStatus == 0}">未上传</c:if>
						<c:if test="${user.fIdentityStatus == 1}">待审核</c:if>
						<c:if test="${user.fIdentityStatus == 2}">已通过</c:if>
						<c:if test="${user.fIdentityStatus == 3}">未通过</c:if>
					</td>
					<td>${user.fnickName}</td>
					<td>${user.frealName}</td>
					<td>${user.fneedFee ? "是": "否"}</td>
					<td>${user.canOtc ? "是": "否"}</td>
					<td>${user.ftelephone}</td>
					<td>${user.femail}</td>
					<td>${user.fidentityNo}</td>
					<td>${user.fregisterTime}</td>
					<td>${user.flastLoginTime}</td>
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

<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="comm/include.inc.jsp"%>
<form id="pagerForm" method="post"
	  action="ssadmin/feedbackList.html">
	<input type="hidden" name="status" value="${param.status}"> <input
		type="hidden" name="keywords" value="${keywords}" /><input
		type="hidden" name="ftype" value="${status}" /><input type="hidden"
															  name="pageNum" value="${currentPage}" /> <input type="hidden"
																											  name="numPerPage" value="${numPerPage}" /> <input type="hidden"
																																								name="orderField" value="${param.orderField}" /><input type="hidden"
																																																					   name="orderDirection" value="${param.orderDirection}" />
</form>


<div class="pageHeader">
	<form onsubmit="return navTabSearch(this);"
		  action="ssadmin/feedbackList.html" method="post">
		<div class="searchBar">

			<table class="searchContent">
				<tr>
					<td>标题：<input type="text" name="keywords" value="${keywords}"
								  size="60" />[会员信息]</td>
					<td>状态： <select type="combox" name="status">
								<option value="0">全部</option>
								<option value="1" <c:if test="${status == 1}">selected="true"</c:if>>未处理</option>
								<option value="2" <c:if test="${status == 2}">selected="true"</c:if>>处理中</option>
								<option value="3" <c:if test="${status == 3}">selected="true"</c:if>>已处理</option>
								<option value="4" <c:if test="${status == 4}">selected="true"</c:if>>删除</option>
					</select></td>
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
			<shiro:hasPermission name="ssadmin/goFeedbackChangeStatus.html?toStatus=1">
				<li><a class="edit"
					   href="ssadmin/goFeedbackChangeStatus.html?uid={uid}&toStatus=1"
					   height="320" width="800" target="ajaxTodo"
					   rel="viewVirtualCapitaloperation" title="确定要标记为未处理吗?"><span>标记为未处理</span>
				</a></li>
			</shiro:hasPermission>
			<shiro:hasPermission name="ssadmin/goFeedbackChangeStatus.html?toStatus=2">
				<li><a class="edit"
					   href="ssadmin/goFeedbackChangeStatus.html?uid={uid}&toStatus=2"
					   height="320" width="800" target="ajaxTodo"
					   rel="viewVirtualCapitaloperation" title="确定要标记为处理中吗?"><span>标记为处理中</span>
				</a></li>
			</shiro:hasPermission>
			<shiro:hasPermission name="ssadmin/goFeedbackChangeStatus.html?toStatus=3">
				<li><a class="edit"
					   href="ssadmin/goFeedbackChangeStatus.html?uid={uid}&toStatus=3"
					   height="320" width="800" target="ajaxTodo"
					   rel="viewVirtualCapitaloperation" title="确定要标记为已处理吗?"><span>标记为已处理</span>
				</a></li>
			</shiro:hasPermission>
			<shiro:hasPermission name="ssadmin/goFeedbackChangeStatus.html?toStatus=4">
				<li><a class="edit"
					   href="ssadmin/goFeedbackChangeStatus.html?uid={uid}&toStatus=4"
					   height="320" width="800" target="ajaxTodo"
					   rel="viewVirtualCapitaloperation" title="确定要标记为已删除吗?"><span>标记为已删除</span>
				</a></li>
			</shiro:hasPermission>
			<shiro:hasPermission name="ssadmin/exportFeedbackList.html">
				<li><a class="icon"
					   href="ssadmin/exportFeedbackList.html"
					   target="dwzExport" title="确定要导出意见反馈列表吗？"><span>导出意见反馈列表</span> </a></li>
			</shiro:hasPermission>
		</ul>
	</div>
	<table class="table" width="160%" layoutH="138">
		<thead>
		<tr>
			<th width="20">序号</th>
			<th width="60" orderField="fuser.floginName"
					<c:if test='${param.orderField == "fuser.floginName" }'> class="${param.orderDirection}"  </c:if>>会员登陆名</th>
			<th width="60" orderField="fuser.fnickName"
					<c:if test='${param.orderField == "fuser.fnickName" }'> class="${param.orderDirection}"  </c:if>>会员昵称</th>
			<th width="60" orderField="fuser.femail"
					<c:if test='${param.orderField == "fuser.femail" }'> class="${param.orderDirection}"  </c:if>>会员邮箱</th>
			<th width="60" orderField="fuser.ftelephone"
					<c:if test='${param.orderField == "fuser.ftelephone" }'> class="${param.orderDirection}"  </c:if>>会员手机号</th>
			<th width="60" orderField="fuser.frealName"
					<c:if test='${param.orderField == "fuser.frealName" }'> class="${param.orderDirection}"  </c:if>>会员真实姓名</th>
			<th width="60" orderField="fContactWay"
					<c:if test='${param.orderField == "fContactWay" }'> class="${param.orderDirection}"  </c:if>>联系方式</th>
			<th width="60" orderField="fContent"
					<c:if test='${param.orderField == "fContent" }'> class="${param.orderDirection}"  </c:if>>反馈内容</th>
			<th width="60" orderField="fStatusStr"
					<c:if test='${param.orderField == "fStatusStr" }'> class="${param.orderDirection}"  </c:if>>状态</th>
			<th width="60" orderField="fCreateTime"
					<c:if test='${param.orderField == "fCreateTime" }'> class="${param.orderDirection}"  </c:if>>创建时间</th>
			<th width="60" orderField="fLastUpdateTime"
					<c:if test='${param.orderField == "fLastUpdateTime" }'> class="${param.orderDirection}"  </c:if>>最后修改时间</th>
		</tr>
		</thead>
		<tbody>
		<c:forEach items="${feedbackList}"
				   var="feedback" varStatus="num">
			<tr target="uid" rel="${feedback.fId}">
				<td>${num.index +1}</td>
				<td>${feedback.fuser.floginName}</td>
				<td>${feedback.fuser.fnickName}</td>
				<td>${feedback.fuser.femail}</td>
				<td>${feedback.fuser.ftelephone}</td>
				<td>${feedback.fuser.frealName}</td>
				<td>${feedback.fContactWay}</td>
				<td>${feedback.fContent}</td>
				<td>${feedback.fStatusStr}</td>
				<td>${feedback.fCreateTime}</td>
				<td>${feedback.fLastUpdateTime}</td>
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

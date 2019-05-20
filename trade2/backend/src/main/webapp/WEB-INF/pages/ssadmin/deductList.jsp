<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="comm/include.inc.jsp"%>
<form id="pagerForm" method="post" action="ssadmin/deductList.html">
	<input type="hidden" name="status" value="${param.status}"> <input
		type="hidden" name="keywords" value="${keywords}" /><input
		type="hidden" name="fstatus" value="${fstatus}" /><input
		type="hidden" name="logDate" value="${logDate}" /><input
		type="hidden" name="type" value="${ftype}" /> <input type="hidden"
		name="pageNum" value="${currentPage}" /> <input type="hidden"
		name="numPerPage" value="${numPerPage}" /> <input type="hidden"
		name="orderField" value="${param.orderField}" /><input type="hidden"
		name="orderDirection" value="${param.orderDirection}" />
</form>


<div class="pageHeader">
	<form onsubmit="return navTabSearch(this);"
		action="ssadmin/deductList.html" method="post">
		<div class="searchBar">

			<table class="searchContent">
				<tr>
					<td>会员信息：<input type="text" name="keywords"
						value="${keywords}" size="60" style="width: 137px; " />
					</td>
					<td></td>
					<td>日期： <input type="text" name="logDate" class="date"
						readonly="true" value="${logDate }" />
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
			<li><a class="edit"
				href="ssadmin/deductlogDetail.html?uid={sid_user}" height="500"
				width="900" target="dialog" rel="deductlogDetail"><span>查看明细</span>
			</a>
			</li>
			<li><a title="确定要审核吗?" target="selectedTodo" rel="ids"
				postType="string" href="ssadmin/examineDeduct.html" class="edit"><span>审核</span>
			</a>
			</li>
			<li><a title="确定要发放提成吗?" target="selectedTodo" rel="ids"
				postType="string" href="ssadmin/sendDeduct.html" class="edit"><span>发放提成</span>
			</a>
			</li>

		</ul>
	</div>
	<table class="table" width="100%" layoutH="138">
		<thead>
			<tr>
				<th width="22"><input type="checkbox" group="ids"
					class="checkboxCtrl">
				</th>
				<th width="60" orderField="fuser.floginName"
					<c:if test='${param.orderField == "fuser.floginName" }'> class="${param.orderDirection}"  </c:if>>会员登录名</th>
				<th width="60" orderField="fuser.fid"
					<c:if test='${param.orderField == "fuser.fid" }'> class="${param.orderDirection}"  </c:if>>会员昵称</th>
				<th width="60" orderField="fuser.fid"
					<c:if test='${param.orderField == "fuser.fid" }'> class="${param.orderDirection}"  </c:if>>真实姓名</th>
				<th width="60" orderField="fchargeDate"
					<c:if test='${param.orderField == "fchargeDate" }'> class="${param.orderDirection}"  </c:if>>日期</th>
				<th width="40" orderField="fchargesection.fid"
					<c:if test='${param.orderField == "fchargesection.fid" }'> class="${param.orderDirection}"  </c:if>>结算区间</th>
				<th width="60" orderField="ftotalAmt"
					<c:if test='${param.orderField == "ftotalAmt" }'> class="${param.orderDirection}"  </c:if>>总人民币金额</th>
				<th width="60" orderField="ftotalQty"
					<c:if test='${param.orderField == "ftotalQty" }'> class="${param.orderDirection}"  </c:if>>FC总数量</th>
				<th width="40" orderField="fstatus"
					<c:if test='${param.orderField == "fstatus" }'> class="${param.orderDirection}"  </c:if>>状态</th>
				<th width="60" orderField="fcreatetime"
					<c:if test='${param.orderField == "fcreatetime" }'> class="${param.orderDirection}"  </c:if>>创建时间</th>
				<th width="60" orderField="fadmin.fid"
					<c:if test='${param.orderField == "fadmin.fid" }'> class="${param.orderDirection}"  </c:if>>审核人</th>
				<th width="60" orderField="faudittime"
					<c:if test='${param.orderField == "faudittime" }'> class="${param.orderDirection}"  </c:if>>审核时间</th>
			</tr>
		</thead>
		<tbody>
			<c:forEach items="${deductList}" var="deductList" varStatus="num">
				<tr target="sid_user" rel="${deductList.fid}">
					<td><input name="ids" value="${deductList.fid}"
						type="checkbox">
					</td>
					<td>${deductList.fuser.floginName}</td>
					<td>${deductList.fuser.fnickName}</td>
					<td>${deductList.fuser.frealName}</td>
					<td>${deductList.fchargeDate}</td>
					<td>${deductList.fchargesection.fstartHour} -
						${deductList.fchargesection.fendHour}</td>
					<td><fmt:formatNumber value="${deductList.ftotalAmt}" pattern="##.######" maxIntegerDigits="10" maxFractionDigits="4"/></td>
					<td><fmt:formatNumber value="${deductList.ftotalQty}" pattern="##.######" maxIntegerDigits="10" maxFractionDigits="4"/></td>
					<td>${deductList.fstatus_s}</td>
					<td>${deductList.fcreatetime}</td>
					<td>${deductList.fadmin.fname}</td>
					<td>${deductList.faudittime}</td>
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

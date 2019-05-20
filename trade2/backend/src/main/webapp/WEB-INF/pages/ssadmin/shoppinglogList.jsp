<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="comm/include.inc.jsp"%>
<form id="pagerForm" method="post" action="ssadmin/shoppinglogList.html">
	<input type="hidden" name="status" value="${param.status}"> <input
		type="hidden" name="supplierNo" value="${supplierNo}" /> <input
		type="hidden" name="status" value="${status}" /><input type="hidden"
		name="pageNum" value="${currentPage}" /> <input type="hidden"
		name="numPerPage" value="${numPerPage}" /> <input type="hidden"
		name="orderField" value="${param.orderField}" /><input type="hidden"
		name="orderDirection" value="${param.orderDirection}" />
</form>


<div class="pageHeader">
	<form onsubmit="return navTabSearch(this);"
		action="ssadmin/shoppinglogList.html" method="post">
		<div class="searchBar">

			<table class="searchContent">
				<tr>
					<td>商家号：<input type="text" name="supplierNo" value="${supplierNo}"
						size="60" />
					</td>
					<td>订单状态： <select type="combox" name="status">
							<c:forEach items="${statusMap}" var="s">
								<c:if test="${s.key == status}">
									<option value="${s.key}" selected="true">${s.value}</option>
								</c:if>
								<c:if test="${s.key != status}">
									<option value="${s.key}">${s.value}</option>
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
			<shiro:hasPermission name="ssadmin/sendShoppinglog.html">
				<li><a class="edit"
					href="ssadmin/goShoppinglogJSP.html?url=ssadmin/sendShoppinglog&uid={sid_user}"
					height="200" width="800" target="dialog" rel="sendShoppinglog"><span>发货</span>
				</a>
			</li>
			</shiro:hasPermission>
			<shiro:hasPermission name="ssadmin/chargeShoppinglog.html">
				<li><a class="delete"
					href="ssadmin/chargeShoppinglog.html?uid={sid_user}"
					target="ajaxTodo" title="确定要结算吗?"><span>结算</span> </a>
				</li>
			</shiro:hasPermission>
		</ul>
	</div>
	<table class="table" width="100%" layoutH="138">
		<thead>
			<tr>
				<th width="20">序号</th>
				<th width="60" orderField="fgoods.fname"
                    <c:if test='${param.orderField == "fgoods.fname" }'> class="${param.orderDirection}"  </c:if>>商品名称</th>
				<th width="60" orderField="fgoods.fsupplierNo"
                    <c:if test='${param.orderField == "fgoods.fsupplierNo" }'> class="${param.orderDirection}"  </c:if>>商家号</th>
				<th width="40" orderField="fgoods.fstatus"
                    <c:if test='${param.orderField == "fgoods.fstatus" }'> class="${param.orderDirection}"  </c:if>>状态</th>
				<th width="60">购买者UID</th>
				<th width="40" orderField="fqty"
                    <c:if test='${param.orderField == "fqty" }'> class="${param.orderDirection}"  </c:if>>购买数量</th>
				<th width="40" orderField="fpaytype"
                    <c:if test='${param.orderField == "fpaytype" }'> class="${param.orderDirection}"  </c:if>>消耗类型</th>
				<th width="40" orderField="fpriceAmt"
                    <c:if test='${param.orderField == "fpriceAmt" }'> class="${param.orderDirection}"  </c:if>>消耗人民币金额</th>
				<th width="40" orderField="fpriceCoin"
                    <c:if test='${param.orderField == "fpriceCoin" }'> class="${param.orderDirection}"  </c:if>>消耗虚拟币数量</th>
				<!-- <th width="40">消耗积分</th> -->
				<th width="40">手机号码</th>
				<th width="40">收货地址</th>
				<th width="40">快递单号</th>
				<th width="40" orderField="fsendtime"
                    <c:if test='${param.orderField == "fsendtime" }'> class="${param.orderDirection}"  </c:if>>发货日期</th>
				<th width="40" orderField="fusetime"
                    <c:if test='${param.orderField == "fusetime" }'> class="${param.orderDirection}"  </c:if>>使用日期</th>
				<th width="40" orderField="fcreatetime"
                    <c:if test='${param.orderField == "fcreatetime" }'> class="${param.orderDirection}"  </c:if>>下单日期</th>
			</tr>
		</thead>
		<tbody>
			<c:forEach items="${shoppinglogList}" var="shoppinglogList"
				varStatus="num">
				<tr target="sid_user" rel="${shoppinglogList.fid}">
					<td>${num.index +1}</td>
					<td>${shoppinglogList.fgoods.fname}</td>
					<td>${shoppinglogList.fgoods.fsupplierNo}</td>
					<td>${shoppinglogList.fstatus_s}</td>
					<td>${shoppinglogList.fuser.fid}</td>
					<td>${shoppinglogList.fqty}</td>
					<td>${shoppinglogList.fpaytype_s}</td>
					<td>${shoppinglogList.fpriceAmt}</td>
					<td>${shoppinglogList.fpriceCoin}</td>
					<%-- <td>${shoppinglogList.fscore}</td> --%>
					<td>${shoppinglogList.fphone}</td>
					<td>${shoppinglogList.freceiveAddress}</td>
					<td>${shoppinglogList.fexpressNo}</td>
					<td>${shoppinglogList.fsendtime}</td>
					<td>${shoppinglogList.fusetime}</td>
					<td>${shoppinglogList.fcreatetime}</td>
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

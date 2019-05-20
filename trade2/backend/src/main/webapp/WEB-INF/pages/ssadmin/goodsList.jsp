<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="comm/include.inc.jsp"%>
<form id="pagerForm" method="post" action="ssadmin/goodsList.html">
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
		action="ssadmin/goodsList.html" method="post">
		<div class="searchBar">

			<table class="searchContent">
				<tr>
					<td>商家号：<input type="text" name="supplierNo" value="${supplierNo}"
						size="60" />
					</td>
					<td>商品状态： <select type="combox" name="status">
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
			<shiro:hasPermission name="ssadmin/saveGoods.html">
				<li><a class="add"
					href="ssadmin/goGoodsJSP.html?url=ssadmin/addGoods"
					height="500" width="800" target="dialog" rel="addGoods"><span>新增</span>
				</a>
				</li>
			</shiro:hasPermission>
			<shiro:hasPermission name="ssadmin/updateGoods.html">
				<li><a class="edit"
					href="ssadmin/goGoodsJSP.html?url=ssadmin/updateGoods&uid={sid_user}"
					height="500" width="800" target="dialog" rel="updateGoods"><span>修改</span>
				</a>
				</li>
			</shiro:hasPermission>
			<shiro:hasPermission name="ssadmin/upGoods.html">
				<li><a class="add"
					href="ssadmin/upGoods.html?uid={sid_user}"
					target="ajaxTodo" title="确定要把该商品上架吗?"><span>商品上架</span> </a>
				</li>
			</shiro:hasPermission>
			<shiro:hasPermission name="ssadmin/takeOutGoods.html">
				<li><a class="delete"
					href="ssadmin/takeOutGoods.html?uid={sid_user}"
					target="ajaxTodo" title="确定要把该商品下架吗?"><span>商品下架</span> </a>
				</li>
			</shiro:hasPermission>
		</ul>
	</div>
	<table class="table" width="100%" layoutH="138">
		<thead>
			<tr>
				<th width="20">序号</th>
				<th width="60" orderField="fname" <c:if test='${param.orderField == "fname" }'> class="${param.orderDirection}"  </c:if>>商品名称</th>
				<th width="40" orderField="fsupplierNo" <c:if test='${param.orderField == "fsupplierNo" }'> class="${param.orderDirection}"  </c:if>>商家号</th>
				<th width="40" orderField="fstatus" <c:if test='${param.orderField == "fstatus" }'> class="${param.orderDirection}"  </c:if>>状态</th>
				<th width="40" orderField="fprice" <c:if test='${param.orderField == "fprice" }'> class="${param.orderDirection}"  </c:if>>单价</th>
				<!-- <th width="40">使用积分</th> -->
				<th width="40" orderField="fmarketPrice" <c:if test='${param.orderField == "fmarketPrice" }'> class="${param.orderDirection}"  </c:if>>市场价</th>
				<th width="40" orderField="ftotalQty" <c:if test='${param.orderField == "ftotalQty" }'> class="${param.orderDirection}"  </c:if>>总库存数量</th>
				<th width="40" orderField="flastQty" <c:if test='${param.orderField == "flastQty" }'> class="${param.orderDirection}"  </c:if>>剩余数量</th>
				<th width="40" orderField="fgoodtype" <c:if test='${param.orderField == "fgoodtype" }'> class="${param.orderDirection}"  </c:if>>分类</th>
				<th width="40" orderField="fqq" <c:if test='${param.orderField == "fqq" }'> class="${param.orderDirection}"  </c:if>>QQ</th>
				<th width="40" orderField="fcanpaytype" <c:if test='${param.orderField == "fcanpaytype" }'> class="${param.orderDirection}"  </c:if>>购买方式</th>
				<th width="40">每人限制数量</th>
				<th width="40" orderField="fcreatetime" <c:if test='${param.orderField == "fcreatetime" }'> class="${param.orderDirection}"  </c:if>>创建日期</th>
			</tr>
		</thead>
		<tbody>
			<c:forEach items="${goodsList}" var="goodsList"
				varStatus="num">
				<tr target="sid_user" rel="${goodsList.fid}">
					<td>${num.index +1}</td>
					<td>${goodsList.fname}</td>
					<td>${goodsList.fsupplierNo}</td>
					<td>${goodsList.fstatus_s}</td>
					<td>${goodsList.fprice}</td>
				<%-- 	<td>${goodsList.fscore}</td> --%>
					<td>${goodsList.fmarketPrice}</td>
					<td>${goodsList.ftotalQty}</td>
					<td>${goodsList.flastQty}</td>
					<td>${goodsList.fgoodtype.fname}</td>
					<td>${goodsList.fqq}</td>
					<td>${goodsList.fcanpaytype_s}</td>
					<td>${goodsList.fcanBuyQty}</td>
					<td>${goodsList.fcreatetime}</td>
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

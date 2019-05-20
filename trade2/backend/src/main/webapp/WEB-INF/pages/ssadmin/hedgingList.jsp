<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="comm/include.inc.jsp"%>
<form id="pagerForm" method="post" action="ssadmin/hedgingList.html">
	<input type="hidden" name="status" value="${param.status}"> <input
		type="hidden" name="keywords" value="${keywords}" /> <input
		type="hidden" name="pageNum" value="${currentPage}" /> <input
		type="hidden" name="numPerPage" value="${numPerPage}" /> 
		<input type="hidden" name="orderField" value="${param.orderField}" />
		<input type="hidden" name="orderDirection" value="${param.orderDirection}" />
</form>


<div class="pageHeader">
	<form onsubmit="return navTabSearch(this);"
		action="ssadmin/hedgingList.html" method="post">
		<div class="searchBar">

			<table class="searchContent">
				<tr>
					<td></td>
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
			<shiro:hasPermission name="ssadmin/saveHedging.html">
				<li><a class="add"
					href="ssadmin/goHedgingJSP.html?url=ssadmin/addHedging&type=2" height="400"
					width="800" target="dialog" rel="addHedging"><span>新增</span> </a></li>
			</shiro:hasPermission>
			<shiro:hasPermission name="ssadmin/deleteHedging.html">
				<li><a class="delete"
					href="ssadmin/deleteHedging.html?uid={sid_user}" target="ajaxTodo"
					title="确定要删除吗?"><span>删除</span> </a></li>
			</shiro:hasPermission>
			<shiro:hasPermission name="ssadmin/updateHedging.html">
				<li><a class="edit"
					href="ssadmin/goHedgingJSP.html?url=ssadmin/updateHedging&uid={sid_user}"
					height="400" width="800" target="dialog" rel="updateHedging"><span>修改</span>
				</a></li>
			</shiro:hasPermission>
		</ul>
	</div>
	<table class="table" width="100%" layoutH="138">
		<thead>
			<tr>
				<th width="20">序号</th>
				<th width="60">对冲虚拟币名称</th>
				<th width="60">投注虚拟币名称</th>
				<th width="60" orderField="fminqty"
                    <c:if test='${param.orderField == "fminqty" }'> class="${param.orderDirection}"  </c:if>>最小数量</th>
				<th width="60" orderField="fmaxqty"
                    <c:if test='${param.orderField == "fmaxqty" }'> class="${param.orderDirection}"  </c:if>>最大数量</th>
				<!-- <th width="60">看涨赔率</th>
				<th width="60">看平赔率</th>
				<th width="60">看跌赔率</th> -->
				<th width="60" orderField="fupthreshold"
                    <c:if test='${param.orderField == "fupthreshold" }'> class="${param.orderDirection}"  </c:if>>看涨门限</th>
				<th width="60" orderField="fdownthreshold"
                    <c:if test='${param.orderField == "fdownthreshold" }'> class="${param.orderDirection}"  </c:if>>看跌门限</th> 
				<th width="60" orderField="fstarttime"
                    <c:if test='${param.orderField == "fstarttime" }'> class="${param.orderDirection}"  </c:if>>开始投注时间</th>
				<th width="60" orderField="fendtime"
                    <c:if test='${param.orderField == "fendtime" }'> class="${param.orderDirection}"  </c:if>>结束投注时间</th>
				<th width="60" orderField="fstartpricetime"
                    <c:if test='${param.orderField == "fstartpricetime" }'> class="${param.orderDirection}"  </c:if>>基准价格时间</th>
				<th width="60" orderField="fendpricetime"
                    <c:if test='${param.orderField == "fendpricetime" }'> class="${param.orderDirection}"  </c:if>>交割价格时间</th>
				<th width="60">取价格API</th>
				<th width="60" orderField="fcreatetime"
                    <c:if test='${param.orderField == "fcreatetime" }'> class="${param.orderDirection}"  </c:if>>创建时间</th>
			</tr>
		</thead>
		<tbody>
			<c:forEach items="${hedgingList}" var="hedging" varStatus="num">
				<tr target="sid_user" rel="${hedging.fid}">
					<td>${num.index +1}</td>
					<td>${hedging.fvirtualcointypeByFid.fname}</td>
					<td>${hedging.fvirtualcointypeByFvid2.fname}</td>
					<td>${hedging.fminqty}</td>
					<td>${hedging.fmaxqty}</td>
					<!-- <td>${hedging.frate1}</td>
					<td>${hedging.frate2}</td>
					<td>${hedging.frate3}</td> -->
					<td>${hedging.fupthreshold}</td>
					<td>${hedging.fdownthreshold}</td>
					<td>${hedging.fstarttime}</td>
					<td>${hedging.fendtime}</td>
					<td>${hedging.fstartpricetime}</td>
					<td>${hedging.fendpricetime}</td>
					<td>${hedging.fpriceurl}</td>
					<td>${hedging.fcreatetime}</td>
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

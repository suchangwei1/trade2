<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="comm/include.inc.jsp"%>
<form id="pagerForm" method="post"
	action="ssadmin/hedginglogList.html">
	<input type="hidden" name="status" value="${param.status}"> <input
		type="hidden" name="keywords" value="${keywords}" /><input
		type="hidden" name="ftype" value="${ftype}" /> <input type="hidden"
		name="pageNum" value="${currentPage}" /> <input type="hidden"
		name="numPerPage" value="${numPerPage}" /> <input type="hidden"
		name="orderField" value="${param.orderField}" /><input type="hidden"
		name="orderDirection" value="${param.orderDirection}" />
</form>


<div class="pageHeader">
	<form onsubmit="return navTabSearch(this);"
		action="ssadmin/hedginglogList.html" method="post">
		<div class="searchBar">

			<table class="searchContent">
				<tr>
					<td>期数：<input type="text" name="keywords" value="${keywords}"
						size="60" /></td>
					<td>对冲虚拟币类型： <select type="combox" name="ftype">
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
	<div class="panelBar"></div>
	<table class="table" width="150%" layoutH="138">
		<thead>
			<tr>
				<th width="30">期数</th>
				<th width="60">对冲虚拟币</th>
				<th width="60">投注虚拟币</th>
				<th width="60" orderField="ftotal1"
                    <c:if test='${param.orderField == "ftotal1" }'> class="${param.orderDirection}"  </c:if>>看涨总数量</th>
				<th width="60" orderField="ftotal2"
                    <c:if test='${param.orderField == "ftotal2" }'> class="${param.orderDirection}"  </c:if>>看平总数量</th>
				<th width="60" orderField="ftotal3"
                    <c:if test='${param.orderField == "ftotal3" }'> class="${param.orderDirection}"  </c:if>>看跌总数量</th>
				<th width="60" orderField="fstartprice"
                    <c:if test='${param.orderField == "fstartprice" }'> class="${param.orderDirection}"  </c:if>>开始价格</th>
				<th width="60" orderField="fendprice"
                    <c:if test='${param.orderField == "fendprice" }'> class="${param.orderDirection}"  </c:if>>交割价格</th>
				<th width="60" orderField="fstatus"
                    <c:if test='${param.orderField == "fstatus" }'> class="${param.orderDirection}"  </c:if>>状态</th>
				<th width="60" orderField="fresult"
                    <c:if test='${param.orderField == "fresult" }'> class="${param.orderDirection}"  </c:if>>结果</th>
				<th width="60" orderField="fstarttime"
                    <c:if test='${param.orderField == "fstarttime" }'> class="${param.orderDirection}"  </c:if>>开始投注时间</th>
				<th width="60" orderField="fendtime"
                    <c:if test='${param.orderField == "fendtime" }'> class="${param.orderDirection}"  </c:if>>结束投注时间</th>
				<th width="60" orderField="fstartpricetime"
                    <c:if test='${param.orderField == "fstartpricetime" }'> class="${param.orderDirection}"  </c:if>>基准价格时间</th>
				<th width="60" orderField="fendpricetime"
                    <c:if test='${param.orderField == "fendpricetime" }'> class="${param.orderDirection}"  </c:if>>交割价格时间</th>
				<th width="60" orderField="fcreatetime"
                    <c:if test='${param.orderField == "fcreatetime" }'> class="${param.orderDirection}"  </c:if>>创建时间</th>
			</tr>
		</thead>
		<tbody>
			<c:forEach items="${hedginglogList}" var="hedginglog"
				varStatus="num">
				<tr target="sid_user" rel="${hedginglog.fid}">
					<td>${hedginglog.fnumber}</td>
					<td>${hedginglog.fvirtualcointypeByFid.fname}</td>
					<td>${hedginglog.fvirtualcointypeByFvid2.fname}</td>
					<td><fmt:formatNumber value="${hedginglog.ftotal1}" pattern="##.######" maxIntegerDigits="10" maxFractionDigits="4"/></td>
					<td><fmt:formatNumber value="${hedginglog.ftotal2}" pattern="##.######" maxIntegerDigits="10" maxFractionDigits="4"/></td>
					<td><fmt:formatNumber value="${hedginglog.ftotal3}" pattern="##.######" maxIntegerDigits="10" maxFractionDigits="4"/></td>
					<td><fmt:formatNumber value="${hedginglog.fstartprice}" pattern="##.######" maxIntegerDigits="10" maxFractionDigits="4"/></td>
					<td><fmt:formatNumber value="${hedginglog.fendprice}" pattern="##.######" maxIntegerDigits="10" maxFractionDigits="4"/></td>
					<td>${hedginglog.fstatus_s}</td>
					<td>${hedginglog.fresult_s}</td>
					<td>${hedginglog.fstarttime}</td>
					<td>${hedginglog.fendtime}</td>
					<td>${hedginglog.fstartpricetime}</td>
					<td>${hedginglog.fendpricetime}</td>
					<td>${hedginglog.fcreatetime}</td>
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

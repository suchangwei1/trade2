<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="comm/include.inc.jsp"%>
<form id="pagerForm" method="post"
	action="/ssadmin/freeLotterySetting.html">
</form>

<div class="pageContent">
	<div class="panelBar">
		<ul class="toolBar">
				<li><a class="add"
					href="ssadmin/goFreeLotterySettingJSP.html?url=ssadmin/updateFfreeLotteryRule&uid={sid_user}"
					height="350" width="800" target="dialog" rel="addVirtualCoinType"><span>修改奖励</span>
				</a>
				</li>
		</ul>
	</div>
	<table class="table" width="100%" layoutH="138">
		<thead>
			<tr>
				<th width="20">序号</th>
				<th width="30">奖项名称</th>
				<th width="60">幸运数字</th>
				<th width="60">奖励</th>
			</tr>
		</thead>
		<tbody>
			<c:forEach items="${list}" var="v" varStatus="num">
				<tr target="sid_user" rel="${v.fid}">
					<td>${num.index +1}</td>
					<td>${v.fname}</td>
					<td>${v.ffrom} ~ ${v.fto} </td>
					<td><fmt:formatNumber pattern="##.##" value="${v.freward}" maxFractionDigits="8"></fmt:formatNumber></td>
				</tr>
			</c:forEach>
		</tbody>
	</table>

</div>

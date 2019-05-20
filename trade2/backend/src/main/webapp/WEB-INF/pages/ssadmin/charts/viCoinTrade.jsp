<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ include file="/WEB-INF/pages/ssadmin/comm/include.inc.jsp"%>
<script src="../../static/ssadmin/js/js/highcharts.js"></script>
<script src="../../static/ssadmin/js/js/modules/exporting.js"></script>
<div class="pageHeader">
	<form action="ssadmin/viCoinTradeReport.html" method="post" onsubmit="return navTabSearch(this);">
		<div class="searchBar">
			<table class="searchContent">
				<tr>
					<td>时间段：<input type="text" name="startDate"
						class="date textInput readonly required" readonly="true" size="10"
						value="${startDate}"></td>
					<td>至</td>
					<td><input type="text" name="endDate"
						class="date textInput readonly required" readonly="true" size="10"
						value="${endDate}"></td>
					<td>交易对： <select type="combox" name="coinType" class="required" >
							<c:forEach items="${types}" var="type">
								<option value="${type.id}"
									<c:if test="${type.id == coinType}">selected="selected"</c:if>>${coinMap[type.sellId]}/ ${coinMap[type.buyId]}</option>
							</c:forEach>
					</select>
					</td>
					<%--<td>数据类型： <select type="combox" name="lineType" class="required">--%>
							<%--<option value="1"--%>
								<%--<c:if test="${1 == lineType}">selected="selected"</c:if>>成交量</option>--%>
							<%--<option value="2"--%>
								<%--<c:if test="${2 == lineType}">selected="selected"</c:if>>成交人民币金额</option>--%>
					<%--</select>--%>
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
		</ul>
	</div>
	<div id="viVoinTradeReport" style="min-width: 310px; height: 400px; margin: 0 auto"></div>
</div>
<script type="text/javascript">
	function loadData(obj){
		var _this = $(obj);
	}
	
	$(function() {
		var lineName = "成交量";
		$('#viVoinTradeReport').highcharts(
				{
				 	chart: {
			            type: 'line'
			        },
					title : {
						text : '虚拟币' + lineName
					},
					xAxis : {
						categories : ${dates}
					},
					tooltip : {
						formatter : function() {
							var s;
							if (this.point.name) { // the pie chart
								s = '' + this.point.name + ': ' + this.y
										+ ' fruits';
							} else {
								s = '日期：' + this.x + '| ' + lineName + '：' + this.y;
							}
							return s;
						}
					},
					yAxis: {
			            title: {
			                text: lineName
			            }
			        },
					series : [ {
						name : '日期',
						data : ${datas}
					} ]
				});
	});
</script>
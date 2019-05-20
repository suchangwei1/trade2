<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ include file="/WEB-INF/pages/ssadmin/comm/include.inc.jsp"%>
<script src="../../static/ssadmin/js/js/highcharts.js"></script>
<script src="../../static/ssadmin/js/js/modules/exporting.js"></script>
<div class="pageHeader">
	<form id="pagerForm" action="ssadmin/capitalInReport.html" method="post" onsubmit="return navTabSearch(this);">
		<div class="searchBar">
			<table class="searchContent">
				<tr>
					<td>时间段：<input type="text" name="startDate"
						class="date textInput readonly required" readonly="true" size="10"
						value="<fmt:formatDate value="${startDate}" pattern="yyyy-MM-dd"/>"></td>
					<td>至</td>
					<td><input type="text" name="endDate"
						class="date textInput readonly required" readonly="true" size="10"
						value="<fmt:formatDate value="${endDate}" pattern="yyyy-MM-dd"/>"></td>
					<td>支付渠道：
						<select id="payType" type="combox" name="payType" >
							<option value="">全部</option>
							<option <c:if test="${'1' == param.payType}">selected</c:if> value="1">双乾支付</option>
							<option <c:if test="${'2' == param.payType}">selected</c:if> value="2">线下支付宝</option>
							<option <c:if test="${'3' == param.payType}">selected</c:if> value="3">线下银行卡</option>
							<option <c:if test="${'4' == param.payType}">selected</c:if> value="4">微信扫码支付</option>
						</select>
					</td>
					<td>
						<div class="buttonActive">
							<div class="buttonContent">
								<button type="submit">查询</button>
							</div>
						</div>
					</td>
				</tr>
			</table>
		</div>
	</form>
</div>
<div class="pageContent">
	<div class="panelBar">
		<ul class="toolBar">
			<shiro:hasPermission name="ssadmin/exportCapitalInReport.html">
				<li>
					<a class="icon" href="ssadmin/capitalInReport.html?url=ssadmin/excel/exportCapitalInReport"
					   target="dwzExport" targetType="navTab" title="是要导出报表吗?"><span>导出报表</span>
					</a>
				</li>
			</shiro:hasPermission>
		</ul>
	</div>
	<div id="rechargReportChart" style="min-width: 310px; height: 400px; margin: 0 auto"></div>
</div>
<script type="text/javascript">
	function loadData(obj){
		var _this = $(obj);
	}
	
	$(function() {
		var lineName = $('#payType').find("option:selected").text();
		if('全部' == lineName){
			lineName = '总充值金额';
		}else{
			lineName += '金额';
		}

		var total = 0;
		var datas = ${yAxis};
		for(var i=0; i<datas.length; i++){
			total += datas[i];
		}

		$('#rechargReportChart').highcharts(
				{
				 	chart: {
			            type: 'line'
			        },
					title : {
						text : lineName + ' 区间总数：' + total + '元'
					},
					xAxis : {
						categories : ${xAxis}
					},
					tooltip : {
						formatter : function() {
							var s;
							if (this.point.name) { // the pie chart
								s = '' + this.point.name + ': ' + this.y
										+ ' fruits';
							} else {
								s = '日期：' + this.x + '| ' + lineName + '：' + this.y + '元';
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
						data : datas
					} ]
				});
	});
</script>
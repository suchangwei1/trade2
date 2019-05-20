<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ include file="/WEB-INF/pages/ssadmin/comm/include.inc.jsp"%>
<script src="../../static/ssadmin/js/js/highcharts.js"></script>
<script src="../../static/ssadmin/js/js/modules/exporting.js"></script>
<div class="pageHeader">
	<form id="pagerForm" action="ssadmin/transferInReport.html" method="post" onsubmit="return navTabSearch(this);">
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
					<td>币种：
						<select id="coinType" type="combox" name="coinType" >
							<option value="0">人民币</option>
							<c:forEach items="${coins}" var="coin">
								<option <c:if test="${param.coinType == coin.fid}">selected</c:if> value="${coin.fid}">${coin.fname}</option>
							</c:forEach>
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
			<shiro:hasPermission name="ssadmin/exportTransferInReport.html">
				<li>
					<a class="icon" href="ssadmin/transferInReport.html?url=ssadmin/excel/exportTransferInReport"
					   target="dwzExport" targetType="navTab" title="是要导出报表吗?"><span>导出报表</span>
					</a>
				</li>
			</shiro:hasPermission>
		</ul>
	</div>
	<div id="transferReportChart" style="min-width: 310px; height: 400px; margin: 0 auto"></div>
</div>
<script type="text/javascript">
	function loadData(obj){
		var _this = $(obj);
	}
	
	$(function() {
		var lineName = $('#coinType').find("option:selected").text() + '金额';

		var total = 0;
		var datas = ${yAxis};
		for(var i=0; i<datas.length; i++){
			total += datas[i];
		}

		$('#transferReportChart').highcharts(
				{
				 	chart: {
			            type: 'line'
			        },
					title : {
						text : lineName + ' 区间总数：' + total
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
						data : datas
					} ]
				});
	});
</script>
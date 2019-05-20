<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="comm/include.inc.jsp"%>
<!DOCTYPE HTML>
<html>
<head>
<script src="../../static/ssadmin/js/js/highcharts.js"></script>
<script src="../../static/ssadmin/js/js/modules/exporting.js"></script>
<script type="text/javascript">
	$(function() {
		$('#capitalOperationOutReport').highcharts(
				{
					chart : {},
					title : {
						text : '人民币提现统计表,区间总数：${total}元'
					},
					xAxis : {
						categories : ${key}
					},
					tooltip : {
						formatter : function() {
							var s;
							if (this.point.name) { // the pie chart
								s = '' + this.point.name + ': ' + this.y
										+ ' fruits';
							} else {
								s = '日期：' + this.x + '| 金额：' + this.y + '元';
							}
							return s;
						}
					},
					labels : {
						items : [ {
							html : '',
							style : {
								left : '40px',
								top : '8px',
								color : 'black'
							}
						} ]
					},
					series : [ {
						name : '日期',
						data : ${value}
					} ]
				});
	});
</script>

<div class="pageHeader">
	<form id="pagerForm" onsubmit="return navTabSearch(this);"
		action="ssadmin/capitaloperationReport.html" method="post">
		<div class="searchBar">
			<table class="searchContent">
				<tr>
					<td>开始日期： <input type="text" name="startDate" class="date"
						readonly="true" value="${startDate }" /><font color="red">*</font>
						<input type="hidden" name="isSearch" value="1" />
						<input type="hidden" name="type" value="2" />
						<input type="hidden" name="status" value="3" />
						<input type="hidden" name="url" value="ssadmin/capitaloperationOutReport" />
					</td>
					<td>结束日期： <input type="text" name="endDate" class="date"
						readonly="true" value="${endDate }" /><font color="red">*</font>
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
			<shiro:hasPermission name="ssadmin/exportCapitalOutReport.html">
				<li>
					<a class="icon" href="ssadmin/capitaloperationReport.html?url=ssadmin/excel/exportCapitalOutReport"
					   target="dwzExport" targetType="navTab" title="是要导出报表吗?"><span>导出报表</span>
					</a>
				</li>
			</shiro:hasPermission>
		</ul>
	</div>
</div>
</head>
<body>

	<div id="capitalOperationOutReport"
		style="min-width: 310px; height: 400px; margin: 0 auto"></div>

</body>
</html>


<%@ page language="java" contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ include file="/WEB-INF/pages/ssadmin/comm/include.inc.jsp" %>
<script src="/static/ssadmin/js/chart/Chart.min.js"></script>
<div class="pageHeader">
    <form id="pagerForm" action="ssadmin/dateWalletReport.html" method="post" onsubmit="return navTabSearch(this);">
        <div class="searchBar">
            <table class="searchContent">
                <tr>
                    <td>时间段：<input type="text" name="startDate"
                                   class="date textInput readonly" readonly="true" size="10"
                                   value="<fmt:formatDate value="${startDate}" pattern="yyyy-MM-dd"/>"></td>
                    <td>至</td>
                    <td><input type="text" name="endDate"
                               class="date textInput readonly" readonly="true" size="10"
                               value="<fmt:formatDate value="${endDate}" pattern="yyyy-MM-dd"/>"></td>
                    <td>币种： <select type="combox" name="coinType" class="required" onchange="submitForm();">
                        <option value="0">人民币</option>
                        <c:forEach items="${types}" var="type">
                            <option value="${type.fid}"
                                    <c:if test="${type.fid == coinType}">selected="selected"</c:if>>${type.fname}</option>
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
            <shiro:hasPermission name="ssadmin/exportDateWalletReport.html">
                <li>
                    <a class="icon" href="ssadmin/exportDateWalletReport.html"
                       target="dwzExport" targetType="navTab" title="是要导出报表吗?"><span>导出报表</span>
                    </a>
                </li>
            </shiro:hasPermission>
        </ul>
    </div>
    <canvas id="dateWalletReport" style="max-width: 1000px;"></canvas>
</div>
<script type="text/javascript">
    function submitForm(){
        $('#pagerForm').submit();
    }
    var data = {
        labels: ${dateList},
        datasets: [
            {
                label: "总余额",
                fill: false,
                backgroundColor: "rgba(47,105,191,0.4)",
                borderColor: "rgba(47,105,191,1)",
                pointBackgroundColor: "rgba(47,105,191,1)",
                pointStrokeColor: "#fff",
                data: ${balanceList}
            },
            {
                label: "总冻结金额",
                fill: false,
                backgroundColor: "rgba(197,215,124,0.4)",
                borderColor: "rgba(197,215,124,1)",
                pointBackgroundColor: "rgba(197,215,124,1)",
                pointStrokeColor: "#fff",
                data: ${freezeList}
            }
        ]
    }

    var ctx = document.getElementById("dateWalletReport").getContext("2d");
    var myLineChart = new Chart(ctx, {
        type: 'line',
        data: data,
//		options: options
    });
</script>
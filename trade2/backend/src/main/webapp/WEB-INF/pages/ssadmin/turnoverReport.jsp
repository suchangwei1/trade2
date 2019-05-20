<%@ page language="java" contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ include file="/WEB-INF/pages/ssadmin/comm/include.inc.jsp" %>
<script src="../../static/ssadmin/js/js/highcharts.js"></script>
<script src="../../static/ssadmin/js/js/modules/exporting.js"></script>
<div class="pageHeader">
    <form action="ssadmin/turnoverReport.html" method="post" onsubmit="return navTabSearch(this);">
        <div class="searchBar">
            <table class="searchContent">
                <tr>
                    <td>时间段：<input type="text" name="start"
                                   class="date textInput readonly required" readonly="true" size="10"
                                   value="${start}"></td>
                    <td>至</td>
                    <td><input type="text" name="end"
                               class="date textInput readonly required" readonly="true" size="10"
                               value="${end}"></td>
                </tr>
            </table>
            <div class="subBar">
                <ul>
                    <li>
                        <div class="buttonActive">
                            <div class="buttonContent">
                                <button type="submit">查询</button>
                            </div>
                        </div>
                    </li>
                </ul>
            </div>
        </div>


    </form>

</div>
<div class="pageContent">
    <div class="panelBar">
        <ul class="toolBar">
            <shiro:hasPermission name="ssadmin/reportturnover.html">
                <li><a class="icon" target="dwzExport"
                       href="ssadmin/reportturnover.html?start=${start}&end=${end}" height="200" width="800"><span>导出涨跌幅列表</span>
                </a></li>
            </shiro:hasPermission>
        </ul>
    </div>
    <table class="table" width="50%" layoutH="138">
        <thead>
        <tr>
            <th>虚拟币</th>
            <th>交易额</th>
        </tr>
        </thead>
        <tbody>
        <c:forEach var="turnoverhelp" items="${data}">
            <tr>
                <td>${turnoverhelp.name}</td>
                <td><fmt:formatNumber value="${turnoverhelp.amount}" pattern="#,#00"></fmt:formatNumber></td>
            </tr>
        </c:forEach>
        </tbody>
    </table>
</div>
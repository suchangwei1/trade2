<%@ page language="java" contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ include file="/WEB-INF/pages/ssadmin/comm/include.inc.jsp" %>
<script src="../../static/ssadmin/js/js/highcharts.js"></script>
<script src="../../static/ssadmin/js/js/modules/exporting.js"></script>
<div class="pageHeader">
    <form action="ssadmin/upanddownReport.html" method="post" onsubmit="return navTabSearch(this);">
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
                    <%--<td>币种： <select type="combox" name="coinType" class="required" >--%>
                    <%--<c:forEach items="${types}" var="type">--%>
                    <%--<option value="${type.fid}"--%>
                    <%--<c:if test="${type.fid == coinType}">selected="selected"</c:if>>${type.fname}</option>--%>
                    <%--</c:forEach>--%>
                    <%--</select>--%>
                    <%--</td>--%>
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
            <shiro:hasPermission name="ssadmin/exportupanddown.html">
                <li><a class="icon" target="dwzExport"
                       href="ssadmin/exportupanddown.html?start=${start}&end=${end}" height="200" width="800"><span>导出涨跌幅列表</span>
                </a></li>
            </shiro:hasPermission>
        </ul>
    </div>
    <table class="table" width="50%" layoutH="138">
        <thead>
        <tr>
            <th>币种</th>
            <th>涨跌幅</th>
        </tr>
        </thead>
        <tbody>
        <c:forEach var="UpanddownHelper" items="${data}">
            <tr>
                <td>${UpanddownHelper.name}</td>
                <td><fmt:formatNumber type="PERCENT" value="${UpanddownHelper.upanddown}" pattern="#.##%"></fmt:formatNumber></td>
            </tr>
        </c:forEach>
        </tbody>
    </table>
</div>
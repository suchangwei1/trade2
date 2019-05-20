<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ include file="/WEB-INF/pages/ssadmin/comm/include.inc.jsp" %>
<html>
<head>
    <title>导出${coinName}单日沉淀资金报表</title>
    <style>
        table {
            width: 500px;
        }

        td, th {
            text-align: center;
            font-size: 12px;
            font-family: "verdana,Arial";
            border: #555 1px solid;
            color: #404040;
        }

        table, tr {
            border-style: none;
        }
    </style>
    <meta http-equiv="Content-Type" content="text/html; charset=gb2312">
</head>
<body>
<%
    String fileName = request.getAttribute("coinName") + "单日沉淀资金报表";
    request.setAttribute("fileName", fileName);
    response.setContentType("application/vnd.ms-excel;charset=gb2312");
    response.setHeader("Content-disposition", "attachment;filename=" + new String(fileName.getBytes("gb2312"), "ISO8859-1") + ".xls");
    response.setHeader("Pragma", "no-cache");
    response.setHeader("Expires", "0");
%>

<table>
    <thead>
    <tr>
        <th colspan="3" style="font-size: 16px;">${fileName}</th>
    </tr>
    <tr>
        <th>日期</th>
        <th>总余额</th>
        <th>总冻结金额</th>
    </tr>
    </thead>
    <tbody>
        <c:forEach items="${list}" var="item" varStatus="s">
            <tr>
                <td><fmt:formatDate value="${item.date}" pattern="yyyy-MM-dd"/></td>
                <td><fmt:formatNumber value="${item.totalBalance}" pattern="#.####"/></td>
                <td><fmt:formatNumber value="${item.totalFreeze}" pattern="#.####"/></td>
            </tr>
        </c:forEach>
    </tbody>
</table>
</body>
</html>

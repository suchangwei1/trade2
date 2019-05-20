<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ include file="/WEB-INF/pages/ssadmin/comm/include.inc.jsp"%>
<html>
<head>
    <title>导出人民币提现报表</title>
    <style>
        table{
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
</head>
<body>
<%
    response.setContentType("application/vnd.ms-excel;charset=gb2312");
    response.setHeader("Content-disposition", "attachment;filename=" + new String("人民币提现报表".getBytes("gb2312"), "ISO8859-1") + ".xls");
    response.setHeader("Pragma", "no-cache");
    response.setHeader("Expires", "0");
%>

<table>
    <thead>
        <tr>
            <th colspan="2" style="font-size: 16px;">人民币提现报表(元)&nbsp;&nbsp;&nbsp;&nbsp;总金额${total}元</th>
        </tr>
        <tr>
            <th>日期</th>
            <th>提现金额</th>
        </tr>
    </thead>
    <tbody>
        <c:forEach items="${all}" var="item" varStatus="s">
            <tr>
                <td>${item[1]}</td>
                <td>${item[0]}</td>
            </tr>
        </c:forEach>
    </tbody>
</table>
</body>
</html>

<%@ page import="com.trade.dto.LatestDealData" %>
<%@ page import="java.util.List" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ include file="/WEB-INF/pages/ssadmin/comm/include.inc.jsp"%>
<html>
<head>
    <title>导出转入资金报表</title>
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
    String fileName;
    LatestDealData data = (LatestDealData) request.getAttribute("coin");
    if(null != data){
        fileName = data.getFname() + "转入资金报表";
    }else{
        fileName = "人民币转入资金报表";
    }
    request.setAttribute("fileName", fileName);
    response.setContentType("application/vnd.ms-excel;charset=gb2312");
    response.setHeader("Content-disposition", "attachment;filename=" + new String(fileName.getBytes("gb2312"), "ISO8859-1") + ".xls");
    response.setHeader("Pragma", "no-cache");
    response.setHeader("Expires", "0");

    double total = 0;
    List arr = (List) request.getAttribute("yAxis");
    for(Object obj : arr){
        total += Double.valueOf(obj.toString());
    }
    request.setAttribute("total", total);
%>

<table>
    <thead>
        <tr>
            <th colspan="2" style="font-size: 16px;">${fileName}&nbsp;&nbsp;&nbsp;&nbsp;总金额${total}</th>
        </tr>
        <tr>
            <th>日期</th>
            <th>转入金额</th>
        </tr>
    </thead>
    <tbody>
        <c:forEach items="${xAxis}" var="x" varStatus="s">
            <tr>
                <td>${x}</td>
                <td>${yAxis[s.index]}</td>
            </tr>
        </c:forEach>
    </tbody>
</table>
</body>
</html>

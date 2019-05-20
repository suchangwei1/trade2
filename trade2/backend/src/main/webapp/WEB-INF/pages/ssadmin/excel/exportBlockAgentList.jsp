<%@ page import="java.util.List" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ include file="/WEB-INF/pages/ssadmin/comm/include.inc.jsp"%>
<html>
<head>
    <title>导出区块链申请代理商报表</title>
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
    String fileName = "区块链申请代理商报表";
    request.setAttribute("fileName", fileName);
    response.setContentType("application/vnd.ms-excel;charset=gb2312");
    response.setHeader("Content-disposition", "attachment;filename=" + new String(fileName.getBytes("gb2312"), "ISO8859-1") + ".xls");
    response.setHeader("Pragma", "no-cache");
    response.setHeader("Expires", "0");
%>

<table>
    <thead>
        <tr>
            <th colspan="2" style="font-size: 16px;">${fileName}</th>
        </tr>
        <tr>
            <th>序号</th>
            <th>真实姓名</th>
            <th>性别</th>
            <th>手机号</th>
            <th>邮箱</th>
            <th>微信号</th>
            <th>身份证号</th>
            <th>职业</th>
            <th>公司</th>
            <th>公司地址</th>
            <th>审核状态</th>
            <th>创建时间</th>
            <th>更新时间</th>
        </tr>
    </thead>
    <tbody>
    <c:forEach items="${list}" var="agent" varStatus="num">
        <tr target="sid_user" rel="${agent.id}">
            <td>${num.index +1}</td>
            <td>${agent.realName}</td>
            <td>
                <c:choose>
                    <c:when test="${0 == agent.sex}">女</c:when>
                    <c:when test="${1 == agent.sex}">男</c:when>
                </c:choose>
            </td>
            <td>${agent.mobile}</td>
            <td>${agent.email}</td>
            <td>${agent.wechatNo}</td>
            <td>${agent.identifyNo}</td>
            <td>${agent.job}</td>
            <td>${agent.company}</td>
            <td>${agent.companyAddress}</td>
            <td>${agent.status.name}</td>
            <td><fmt:formatDate value="${agent.createTime}" pattern="yyyy-MM-dd HH:mm:ss"/></td>
            <td><fmt:formatDate value="${agent.updateTime}" pattern="yyyy-MM-dd HH:mm:ss"/></td>
        </tr>
    </c:forEach>
    </tbody>
</table>
</body>
</html>

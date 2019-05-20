<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" %>
<%@ include file="comm/include.inc.jsp" %>
<form id="pagerForm" method="post" action="ssadmin/newYearRedWrapperDetail.html">
    <input type="hidden" name="keyword" value="${keyword}"/>
    <input type="hidden" name="symbol" value="${symbol}"/>
    <input type="hidden" name="pageNum" value="1"/>
    <input type="hidden" name="total" value="${total}"/>
    <input type="hidden" name="orderField" value="${param.orderField}"/>
    <input type="hidden" name="orderDirection" value="${param.orderDirection}"/>
</form>

<div class="pageHeader">
</div>
<div class="pageContent">
    <table class="table" width="100%" layoutH="138">
        <thead>
        <tr>
            <th width="20">序号</th>
            <th width="60">日期</th>
            <th width="100">剩余金额</th>
            <th width="100">剩余份额</th>
            </th>
        </tr>
        </thead>
        <tbody>
        <c:forEach items="${list}" var="i" varStatus="num">
            <tr>
                <td>${num.index +1}</td>
                <td>${i.date}</td>
                <td>${i.amount}</td>
                <td>${i.count}</td>
            </tr>
        </c:forEach>
        </tbody>
    </table>
</div>

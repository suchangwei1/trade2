<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" %>
<%@ include file="comm/include.inc.jsp" %>
<form id="pagerForm" method="post" action="ssadmin/newYearRedWrapperList.html">
    <input type="hidden" name="date" value="${param.date}"/>
    <input type="hidden" name="pageNum" value="1"/>
    <input type="hidden" name="type" value="${param.type}"/>
    <input type="hidden" name="orderField" value="${param.orderField}"/>
    <input type="hidden" name="orderDirection" value="${param.orderDirection}"/>
</form>

<div class="pageHeader">
    <form onsubmit="return navTabSearch(this);"
          action="ssadmin/newYearRedWrapperList.html" method="post">
        <div class="searchBar" style="float: left;">
            日期：<input type="text" name="date" class="date"readonly="true" value="${param.date}" />
            类型：<select name="type">
                <option value="">全部</option>
                <option <c:if test="${1 == param.type}">selected</c:if> value="1">七嘴八舌聊天</option>
                <option <c:if test="${2 == param.type}">selected</c:if> value="2">资讯新闻评论</option>
                <option <c:if test="${3 == param.type}">selected</c:if> value="3">交易</option>
            </select>
        </div>
        <div class="buttonActive">
            <div class="buttonContent">
                <button type="submit">查询</button>
            </div>
        </div>
    </form>
</div>
<div class="pageContent">
    <div class="panelBar">
    </div>
    <table class="table" width="100%" layoutH="138">
        <thead>
        <tr>
            <th width="20">序号</th>
            <th width="60" orderField="u.floginName"
                    <c:if test='${param.orderField == "u.floginName" }'> class="${param.orderDirection}" </c:if>>登录名</th>
            <th width="100">真实姓名</th>
            <th width="100">手机号码</th>
            <th width="100">邮箱</th>
            <th width="100">类型</th>
            <th width="100" orderField="w.amount"
                    <c:if test='${param.orderField == "w.amount" }'> class="${param.orderDirection}" </c:if>>金额</th>
            <th width="100" orderField="w.create_time"
                    <c:if test='${param.orderField == "w.create_time" }'> class="${param.orderDirection}" </c:if>>分配时间
            </th>
        </tr>
        </thead>
        <tbody>
        <c:forEach items="${list}" var="i" varStatus="num">
            <tr>
                <td>${num.index +1}</td>
                <td>${i.floginName}</td>
                <td>${i.frealName}</td>
                <td>${i.ftelephone}</td>
                <td>${i.femail}</td>
                <td>
                    <c:choose>
                        <c:when test="${1 == i.type}">七嘴八舌聊天</c:when>
                        <c:when test="${2 == i.type}">资讯新闻评论</c:when>
                        <c:when test="${3 == i.type}">交易</c:when>
                    </c:choose>
                </td>
                <td>${i.amount}</td>
                <td><fmt:formatDate value="${i.createTime}" pattern="yyyy-MM-dd HH:mm:ss" /></td>
            </tr>
        </c:forEach>
        </tbody>
    </table>

    <div class="panelBar">
        <div class="pages">
            <span>总共: ${total}条</span>
        </div>
        <div class="pagination" targetType="navTab" totalCount="${total}"
             numPerPage="${numPerPage}" pageNumShown="5"
             currentPage="${null == param.pageNum ? 1 : param.pageNum}"></div>
    </div>
</div>

<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="comm/include.inc.jsp"%>
<form id="pagerForm" method="post" action="ssadmin/apiRecord.html">
    <input type="hidden" name="total" value="${total}">
    <input type="hidden" name="isSucceed" value="${isSucceed}"> <input
        type="hidden" name="keyword" value="${keyword}" /><input
        type="hidden" name="pageNum" value="${currentPage}" /> <input
        type="hidden" name="orderField" value="${orderField}" /><input
        type="hidden" name="orderDirection" value="${orderDirection}" />
</form>

<div class="pageHeader">
    <form onsubmit="return navTabSearch(this);"
          action="ssadmin/apiRecord.html" method="post">
        <div class="searchBar">

            <table class="searchContent">
                <tr>
                    <td>关键字：<input type="text" name="keyword" value="${keyword}"
                                   size="30" />[会员名称、姓名]</td>
                    <td>状态： <select name="isSucceed">
                        <option value="">请选择</option>
                        <option value="0" <c:if test="${0 == isSucceed}">selected</c:if>>失败</option>
                        <option value="1" <c:if test="${1 == isSucceed}">selected</c:if>>成功</option>
                    </select>
                    </td>
                </tr>
            </table>
            <div class="subBar">
                <ul>
                    <li><div class="buttonActive">
                        <div class="buttonContent">
                            <button type="submit">查询</button>
                        </div>
                    </div></li>
                </ul>
            </div>
        </div>
    </form>
</div>
<div class="pageContent">
    <div class="panelBar">
        <ul class="toolBar">

        </ul>
    </div>
    <table class="table" width="120%" layoutH="138">
        <thead>
        <tr>
            <th>会员登陆名</th>
            <th>会员真实姓名</th>
            <th>APP ID</th>
            <th>IP</th>
            <th>URL</th>
            <th>参数</th>
            <th>提示信息</th>
            <th>请求状态</th>
            <th>请求时间</th>
        </tr>
        </thead>
        <tbody>
        <c:forEach items="${list}" var="line"
                   varStatus="num">
            <tr target="sid_user" rel="${line.id}">
                <td>${line.floginName}</td>
                <td>${line.frealName}</td>
                <td>${line.apiId}</td>
                <td>${line.ip}</td>
                <td>${line.apiUrl}</td>
                <td>${line.params}</td>
                <td>${line.code.message}</td>
                <td>
                    <c:choose>
                        <c:when test="${!line.isSucceed}">
                            失败
                        </c:when>
                        <c:when test="${line.isSucceed}">
                            成功
                        </c:when>
                    </c:choose>
                </td>
                <td><fmt:formatDate pattern="yyyy-MM-dd HH:mm:ss" value="${line.createTime}" /></td>
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
             currentPage="${currentPage}"></div>
    </div>
</div>

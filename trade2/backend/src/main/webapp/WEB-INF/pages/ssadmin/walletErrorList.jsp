<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" %>
<%@ include file="comm/include.inc.jsp" %>
<form id="pagerForm" method="post" action="ssadmin/walletErrorList.html">
    <input type="hidden" name="keyword" value="${keyword}"/>
    <input type="hidden" name="symbol" value="${symbol}"/>
    <input type="hidden" name="pageNum" value="1"/>
    <input type="hidden" name="total" value="${total}"/>
    <input type="hidden" name="orderField" value="${param.orderField}"/>
    <input type="hidden" name="orderDirection" value="${param.orderDirection}"/>
</form>

<div class="pageHeader">
    <form onsubmit="return navTabSearch(this);"
          action="ssadmin/walletErrorList.html" method="post">
        <div class="searchBar" style="float: left;">
            关键字：<input type="text" name="keyword" value="${keyword}"/></td>
            &nbsp;&nbsp;钱包类型：<select type="combox" name="symbol">
            <option value="">全部</option>
            <%--<option <c:if test="${0 == symbol}">selected="selected"</c:if> value="0">人民币</option>--%>
            <c:forEach items="${coins}" var="c">
                <option <c:if test="${c.value.fid == symbol}">selected="selected"</c:if> value="${c.value.fid}">${c.value.fname}</option>
            </c:forEach>
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
        <ul class="toolBar">
            <shiro:hasPermission name="ssadmin/exportWalletErrorList.html">
                <li>
                    <a class="icon" target="dwzExport" targetType="navTab" href="/ssadmin/exportWalletErrorList.html" title="确定到导出列表吗？"><span>导出execl</span></a>
                </li>
            </shiro:hasPermission>
        </ul>
    </div>
    <table class="table" width="100%" layoutH="138">
        <thead>
        <tr>
            <th width="20">序号</th>
            <th width="60" orderField="floginName"
                    <c:if test='${param.orderField == "floginName" }'> class="${param.orderDirection}" </c:if>>登录名
            </th>
            <th width="100">真实姓名</th>
            <th width="100">手机号码</th>
            <th width="100">邮箱</th>
            <th width="100">钱包类型</th>
            <th width="100">余额</th>
            <th width="100" orderField="fFrozen"
                    <c:if test='${param.orderField == "fFrozen" }'> class="${param.orderDirection}" </c:if>>已冻结
            </th>
        </tr>
        </thead>
        <tbody>
        <c:forEach items="${list}" var="i" varStatus="num">
            <tr>
                <td>${num.index +1}</td>
                <td>${i.floginName}</td>
                <td>${i.fRealName}</td>
                <td>${i.fTelephone}</td>
                <td>${i.fEmail}</td>
                <td>
                    <c:set var="key"><c:out value="${i.fvid}"/></c:set>
                    <c:choose>
                        <c:when test="${'0' == i.fvid}">
                            人民币
                        </c:when>
                        <c:otherwise>
                            ${coins[key].fname}
                        </c:otherwise>
                    </c:choose>
                </td>
                <td>${i.fTotal}</td>
                <td>${i.fFrozen}</td>
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
             currentPage="${page}"></div>
    </div>
</div>

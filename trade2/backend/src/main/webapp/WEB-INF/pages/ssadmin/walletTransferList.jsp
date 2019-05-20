<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" %>
<%@ include file="comm/include.inc.jsp" %>
<form id="pagerForm" method="post" action="/ssadmin/walletTransferList.html">
    <input type="hidden" name="keyword" value="${keyword}"/>
    <input type="hidden" name="userName" value="${userName}"/>
    <input type="hidden" name="coinId" value="${coinId}"/>
    <input type="hidden" name="userId" value="${userId}"/>
    <input type="hidden" name="active" value="${active}"/>
    <input type="hidden" name="pageNum" value="${currentPage}"/>
    <input type="hidden" name="total" value="${total}"/>
    <input type="hidden" name="orderField" value="${param.orderField}"/>
    <input type="hidden" name="orderDirection" value="${param.orderDirection}"/>
</form>

<div class="pageHeader">
    <form onsubmit="return navTabSearch(this);"
          action="/ssadmin/walletTransferList.html" method="post">
        <div class="searchBar" style="float: left;">
            交易种类：<select type="combox" name="coinId">
                        <option value="">全部</option>
                        <c:forEach items="${coinMap}" var="entry">
                            <option <c:if test="${entry.key == coinId}">selected="selected"</c:if> value="${entry.key}">${entry.value}</option>
                        </c:forEach>
                    </select>
            用户ID：<input type="text" name="userId" value="${userId}">
            用户名：<input type="text" name="userName" value="${userName}">
            转入/转出：<select type="combox" name="active">
                            <option value="">全部</option>
                            <option <c:if test="${active == 'false'}">selected="selected"</c:if> value="false">转入</option>
                            <option <c:if test="${active == 'true'}">selected="selected"</c:if> value="true">转出</option>
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
            <shiro:hasPermission name="ssadmin/exportWalletTransferList.html">
                <li>
                    <a class="icon" target="dwzExport" targetType="navTab" href="/ssadmin/exportWalletTransferList.html" title="确定到导出列表吗？"><span>导出execl</span></a>
                </li>
            </shiro:hasPermission>
        </ul>
    </div>
    <table class="table" width="100%" layoutH="138">
        <thead>
        <tr>
            <th width="20">序号</th>
            <th width="60" orderField="userId"
                    <c:if test='${param.orderField == "userId" }'> class="${param.orderDirection}" </c:if>>用户ID
            </th>
            <th width="60">登录名</th>
            <th width="60">用户邮箱</th>
            <th width="60">用户手机号</th>
            <th width="60">用户姓名</th>
            <th width="100" orderField="virtualCoinId"
                    <c:if test='${param.orderField == "virtualCoinId" }'> class="${param.orderDirection}" </c:if>>交易种类</th>
            <th width="100">交易号</th>
            <th width="100" orderField="active"
                <c:if test='${param.orderField == "active" }'> class="${param.orderDirection}" </c:if>>转入/转出</th>
            <th width="100" orderField="amount"
                    <c:if test='${param.orderField == "amount" }'> class="${param.orderDirection}" </c:if>>金额
            </th>
            <th width="100">状态</th>
            <th width="100" orderField="createTime"
                    <c:if test='${param.orderField == "createTime" }'> class="${param.orderDirection}" </c:if>>创建时间</th>
            <th width="100" orderField="updateTime"
                    <c:if test='${param.orderField == "updateTime" }'> class="${param.orderDirection}" </c:if>>到账时间</th>
        </tr>
        </thead>
        <tbody>
        <c:forEach items="${list}" var="walletTransfer" varStatus="num">
            <tr>
                <td>${num.index +1}</td>
                <td>${walletTransfer.userId}</td>
                <td>${users[walletTransfer.userId].floginName}</td>
                <td>${users[walletTransfer.userId].femail}</td>
                <td>${users[walletTransfer.userId].ftelephone}</td>
                <td>${users[walletTransfer.userId].frealName}</td>
                <%--<td>${walletTransfer.floginName}</td>--%>
                <%--<td>${walletTransfer.femail}</td>--%>
                <%--<td>${walletTransfer.ftelephone}</td>--%>
                <%--<td>${walletTransfer.frealName}</td>--%>
                <td>${walletTransfer.virtualCoinId != 0 ? coinMap[walletTransfer.virtualCoinId] : "人民币" }</td>
                <td>${walletTransfer.tradeNo}</td>
                <td>${walletTransfer.active ? "转出" : "转入"}</td>
                <td>${walletTransfer.active ? "-" : "+"}<fmt:formatNumber>${walletTransfer.amount}</fmt:formatNumber></td>
                <td><c:choose>
                        <c:when test="${walletTransfer.status == 'Paid'}">已付款</c:when>
                        <c:when test="${walletTransfer.status == 'Going'}">进行中</c:when>
                        <c:when test="${walletTransfer.status == 'Success'}">已到账</c:when>
                        <c:when test="${walletTransfer.status == 'Refund'}">已退款</c:when>
                    </c:choose>
                </td>
                <td>${walletTransfer.createTime}</td>
                <td>${walletTransfer.updateTime}</td>
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

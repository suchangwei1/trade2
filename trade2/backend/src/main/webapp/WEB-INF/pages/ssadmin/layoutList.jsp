<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" %>
<%@ include file="comm/include.inc.jsp" %>

<form id="pagerForm" method="post" action="/ssadmin/layoutList.html">
    <input type="hidden" name="status" value="${param.status}">
    <input type="hidden" name="keywords" value="${keywords}"/>
    <input type="hidden" name="frameId" value="${param.frameId}"/>
    <input type="hidden" name="pageNum" value="${currentPage}"/>
    <input type="hidden" name="numPerPage" value="${numPerPage}"/>
    <input type="hidden" name="orderField" value="${param.orderField}"/>
    <input type="hidden" name="orderDirection" value="${param.orderDirection}"/>
</form>
<div class="pageHeader">
    <form onsubmit="return navTabSearch(this);"
          action="/ssadmin/layoutList.html" method="post" enctype="multipart/form-data">
        <div class="searchBar">
            <table class="searchContent">
                <tr>
                    <td>推荐板块： <select type="combox" name="frameId" onchange="changeLayout()">
                        <c:forEach items="${frameNameMap}" var="entry">
                            <c:if test="${entry.key == frameId}">
                                <option value="${entry.key}" selected="true">${entry.value}</option>
                            </c:if>
                            <c:if test="${entry.key != frameId}">
                                <option value="${entry.key}">${entry.value}</option>
                            </c:if>
                        </c:forEach>
                    </select>
                    </td>
                </tr>
            </table>
            <div class="subBar">
                <ul>
                    <li>
                        <div class="buttonActive">
                            <div class="buttonContent">
                                <button type="submit" class="layoutClick">查询</button>
                            </div>
                        </div>
                    </li>
                </ul>
            </div>
            <script>
                function changeLayout() {
                    $(".layoutClick").click();
                }
            </script>
        </div>
    </form>
</div>
<div class="pageContent">
    <div class="panelBar">
        <ul class="toolBar">
            <shiro:hasPermission name="/ssadmin/saveLayout.html">
                <li><a class="add"
                       href="/ssadmin/goLayoutJSP.html?url=ssadmin/addLayout"
                       height="500" width="900" target="dialog" rel="addLayout"><span>新增</span>
                </a></li>
            </shiro:hasPermission>
            <shiro:hasPermission name="/ssadmin/deleteLayout.html">
                <li><a class="delete"
                       href="/ssadmin/deleteLayout.html?id={layout_id}" target="ajaxTodo"
                       title="确定要删除吗?" rel=""><span>删除</span> </a></li>
            </shiro:hasPermission>
            <shiro:hasPermission name="/ssadmin/updateLayout.html">
                <li><a class="edit"
                       href="/ssadmin/goLayoutJSP.html?url=ssadmin/updateLayout&id={layout_id}"
                       height="500" width="900" target="dialog" rel="updateLayout"><span>修改</span>
                </a></li>
            </shiro:hasPermission>
        </ul>
    </div>
    <table class="table" width="100%" layoutH="138">
        <thead>
        <tr>
            <th width="20">序号</th>
            <th width="60" orderField="frameId"
                    <c:if test="${param.orderField == 'frameId'}"> class="${param.orderDirection}" </c:if>>
                推荐板块
            </th>
            <th width="60"
                orderField="articleId"
                    <c:if test="${param.orderField == 'articleId'}"> class="${param.orderDirection}" </c:if>>
                文章ID
            </th>
            <th width="60">文章标题</th>
            <th width="60" orderField="rank"
                    <c:if test="${param.orderField == 'rank'}"> class="${param.orderDirection}" </c:if>>
                权重
            </th>
            <th width="60" orderField="createdTime"
                    <c:if test="${param.orderField == 'createdTime'}"> class="${param.orderDirection}" </c:if>>
                创建时间
            </th>
        </tr>
        </thead>
        <tbody>
        <c:forEach items="${list}" var="layoutInfo" varStatus="num">
            <tr target="layout_id" rel="${layoutInfo.id}">
                <td>${num.index +1}</td>
                <td>${layoutInfo.frameName}</td>
                <td>${layoutInfo.articleId}</td>
                <td><a href="${pageUrl}${layoutInfo.articleId}" target="_blank">${layoutInfo.title}</a></td>
                <td>${layoutInfo.rank}</td>
                <td><fmt:formatDate value="${layoutInfo.createdTime}" pattern="yyyy-MM-dd HH:mm:ss"/></td>
            </tr>
        </c:forEach>
        </tbody>
    </table>

    <div class="panelBar">
        <div class="pages">
            <span>总共: ${totalCount}条</span>
        </div>
        <div class="pagination" targetType="navTab" totalCount="${totalCount}"
             numPerPage="${numPerPage}" pageNumShown="5"
             currentPage="${currentPage}"></div>
    </div>
</div>

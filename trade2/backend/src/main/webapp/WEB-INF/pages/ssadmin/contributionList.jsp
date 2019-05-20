<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" %>
<%@ include file="comm/include.inc.jsp" %>
<form id="pagerForm" method="post" action="/ssadmin/contributionList.html">
    <input type="hidden" name="status" value="${param.status}">
    <input type="hidden" name="pageNum" value="${currentPage}"/>
    <input type="hidden" name="numPerPage" value="${numPerPage}"/>
    <input type="hidden" name="orderField" value="${param.orderField}"/>
    <input type="hidden" name="orderDirection" value="${param.orderDirection}"/>
</form>


<div class="pageHeader">
    <form onsubmit="return navTabSearch(this);"
          action="/ssadmin/contributionList.html" method="post" enctype="multipart/form-data">
        <div class="searchBar">
            <table class="searchContent">
                <tr>
                    <td>作者ID：<input type="text" name="authorId" value="${authorId}" size="30"/></td>
                    <td></td>
                    <td>作者用户名：<input type="text" name="loginName" value="${loginName}" size="30"/></td>
                    <td>投稿类型： <select id="contributionType" type="combox" name="status" onchange="loadContribution()">
                        <option value="0">全部</option>
                        <option value="1" <c:if test="${status == 1}">selected="true"</c:if>>未审核</option>
                        <option value="2" <c:if test="${status == 2}">selected="true"</c:if>>已通过</option>
                        <option value="3" <c:if test="${status == 3}">selected="true"</c:if>>未通过</option>
                        <option value="-2" <c:if test="${status == -2}">selected="true"</c:if>>已删除</option>
                    </select>
                    </td>
                    <script>
                        function loadContribution() {
                            $(".contributionListClick").click();
                        }
                    </script>
                </tr>
            </table>
            <div class="subBar">
                <ul>
                    <li>
                        <div class="buttonActive">
                            <div class="buttonContent">
                                <button type="submit" class="contributionListClick">查询</button>
                            </div>
                        </div>
                    </li>
                </ul>
            </div>
        </div>
    </form>
</div>
<div class="pageContent">
    <div class="panelBar">
        <ul class="toolBar">
            <shiro:hasPermission name="ssadmin/adoptContribution.html">
                <li>
                    <a height="600" width="900" target="dialog"
                       href="/ssadmin/goContributionJSP.html?id={id}&url=ssadmin/adoptContribution" rel="adoptContribution"
                       class="edit"><span>审核</span></a>
                </li>
            </shiro:hasPermission>
            <shiro:hasPermission name="ssadmin/notAdoptContribution.html">
                <li>
                    <a title="确定不通过吗" target="selectedTodo" href="/ssadmin/notAdoptContribution.html" class="edit" rel="ids" postType="string"><span>不通过</span></a>
                </li>
            </shiro:hasPermission>
        </ul>
    </div>
    <table class="table" width="100%" layoutH="138">
        <thead>
        <tr>
            <th width="22"><input type="checkbox" group="ids" class="checkboxCtrl"></th>
            <th width="20">序号</th>
            <%--<th width="60">投稿ID</th>--%>
            <th width="60" orderField="title"
                    <c:if test="${param.orderField == 'title'}"> class="${param.orderDirection}" </c:if>>
                标题
            </th>
            <th width="60">作者ID</th>
            <th width="60">作者用户名</th>
            <th width="60">作者昵称</th>
            <th width="60" orderField="path"
                    <c:if test="${param.orderField == 'path'}"> class="${param.orderDirection}" </c:if>>
                文件地址（点击下载）
            </th>
            <th width="60" orderField="status"
                <c:if test="${param.orderField == 'status'}">class="${param.orderDirection}" </c:if>>
                状态</th>
            <th width="60" orderField="createdTime"
                    <c:if test="${param.orderField == 'createdTime'}"> class="${param.orderDirection}" </c:if>>
                创建时间
            </th>
            <th width="60" orderField="articleId"
                    <c:if test="${param.orderField == 'articleId'}"> class="${param.orderDirection}" </c:if>>
                文章ID（点击打开）
            </th>
            <%--<th width="60" orderField="introduction"--%>
                    <%--<c:if test="${param.orderField == 'introduction'}"> class="${param.orderDirection}" </c:if>>--%>
                <%--介绍--%>
            <%--</th>--%>
        </tr>
        </thead>
        <tbody>
        <c:forEach items="${list}" var="contribution" varStatus="num">
            <tr target="id" rel="${contribution.id}">
                <td><input name="ids" value="${contribution.id}" type="checkbox"></td>
                <td>${num.index +1}</td>
                <%--<td>${contribution.id}</td>--%>
                <td>${contribution.title}</td>
                <td>${contribution.userId}</td>
                <td>${contribution.loginName}</td>
                <td>${contribution.nickName}</td>
                <td><a title="下载" href="${cdn}/${contribution.path}">${contribution.path}</a></td>
                <td>${contribution.status}</td>
                <td><fmt:formatDate value="${contribution.createdTime}" pattern="yyyy-MM-dd HH:mm:ss"></fmt:formatDate></td>
                <td><c:if test="${contribution.articleId != 0 }"><a href="${pageUrl}/details.html?id=${contribution.articleId}">${contribution.articleId}</a></c:if></td>
                <%--<td>${contribution.introduction}</td>--%>
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

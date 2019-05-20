<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" %>
<%@ include file="comm/include.inc.jsp" %>
<form id="pagerForm" method="post" action="/ssadmin/informationArticleList.html">
    <input type="hidden" name="status" value="${param.status}">
    <input type="hidden" name="keywords" value="${keywords}"/>
    <input type="hidden" name="type" value="${param.type}"/>
    <input type="hidden" name="pageNum" value="${currentPage}"/>
    <input type="hidden" name="numPerPage" value="${numPerPage}"/>
    <input type="hidden" name="orderField" value="${param.orderField}"/>
    <input type="hidden" name="orderDirection" value="${param.orderDirection}"/>
</form>


<div class="pageHeader">
    <form onsubmit="return navTabSearch(this);"
          action="/ssadmin/informationArticleList.html" method="post" enctype="multipart/form-data">
        <div class="searchBar">
            <table class="searchContent">
                <tr>
                    <td>关键词：<input type="text" name="keywords" value="${keywords}" size="60"/>[标题]</td>
                    <td></td>
                    <td>文章类型： <select type="combox" name="type" onchange="changeInformation()">
                        <c:forEach items="${typeMap}" var="typeEntry">
                            <c:if test="${typeEntry.key == type}">
                                <option value="${typeEntry.key}" selected="true">${typeEntry.value}</option>
                            </c:if>
                            <c:if test="${typeEntry.key != type}">
                                <option value="${typeEntry.key}">${typeEntry.value}</option>
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
                                <button type="submit" class="informationClick">查询</button>
                            </div>
                        </div>
                    </li>
                </ul>
            </div>
            <script>
                function changeInformation() {
                    $(".informationClick").click();
                }
            </script>
        </div>
    </form>
</div>
<div class="pageContent">
    <div class="panelBar">
        <ul class="toolBar">
            <shiro:hasPermission name="/ssadmin/saveInformationArticle.html">
                <li><a class="add"
                       href="/ssadmin/goInformationArticleJSP.html?url=ssadmin/addInformationArticle"
                       height="600" width="900" target="dialog" rel="addArticle"><span>新增</span>
                </a></li>
            </shiro:hasPermission>
            <shiro:hasPermission name="/ssadmin/deleteInformationArticle.html">
                <li><a class="delete"
                       href="/ssadmin/deleteInformationArticle.html?id={article_id}" target="ajaxTodo"
                       title="确定要删除吗?" rel=""><span>删除</span> </a></li>
            </shiro:hasPermission>
            <shiro:hasPermission name="/ssadmin/updateInformationArticle.html">
                <li><a class="edit"
                       href="/ssadmin/goInformationArticleJSP.html?url=ssadmin/updateInformationArticle&id={article_id}"
                       height="600" width="900" target="dialog" rel="updateInformationArticle"><span>修改</span>
                </a></li>
            </shiro:hasPermission>
            <shiro:hasPermission name="ssadmin/commentList.html">
                <li>
                    <a height="600" width="900" target="dialog" rel="commentList" href="ssadmin/commentList.html?articleId={article_id}" class="edit"><span>评论列表</span></a>
                </li>
            </shiro:hasPermission>
        </ul>
    </div>
    <table class="table" width="100%" layoutH="138">
        <thead>
        <tr>
            <th width="20">序号</th>
            <th width="60">文章ID</th>
            <th width="60" orderField="title"
                    <c:if test="${param.orderField == 'title'}"> class="${param.orderDirection}" </c:if>>
                标题
            </th>
            <th width="60">类型</th>
            <th width="60" orderField="imgPath"
                    <c:if test="${param.orderField == 'imgPath'}"> class="${param.orderDirection}" </c:if>>
                图片地址
            </th>
            <th width="60" orderField="createTime"
                    <c:if test="${param.orderField == 'createTime'}"> class="${param.orderDirection}" </c:if>>
                创建时间
            </th>
            <th width="60" orderField="sources"
                    <c:if test="${param.orderField == 'sources'}"> class="${param.orderDirection}" </c:if>>
                来源
            </th>
            <th width="60" orderField="readingNum"
                    <c:if test="${param.orderField == 'readingNum'}"> class="${param.orderDirection}" </c:if>>
                阅读数量
            </th>
            <th width="60" orderField="commentNum"
                    <c:if test="${param.orderField == 'commentNum'}"> class="${param.orderDirection}" </c:if>>
                评论数量
            </th>
            <%--<th width="60" orderField="status"--%>
                    <%--<c:if test="${param.orderField == 'status'}"> class="${param.orderDirection}" </c:if>>--%>
                <%--状态--%>
            <%--</th>--%>
            <th width="60" orderField="authorName"
                    <c:if test="${param.orderField == 'authorName'}"> class="${param.orderDirection}" </c:if>>
                作者
            </th>
        </tr>
        </thead>
        <tbody>
        <c:forEach items="${articleList}" var="informationArticle" varStatus="num">
            <tr target="article_id" rel="${informationArticle.id}">
                <td>${num.index +1}</td>
                <td>${informationArticle.id}</td>
                <td><a href="${pageUrl}${informationArticle.id}" target="_blank">${informationArticle.title}</a></td>
                <td>${informationArticle.className}</td>
                <td>${informationArticle.imgPath}</td>
                <td><fmt:formatDate value="${informationArticle.createdTime}"
                                    pattern="yyyy-MM-dd HH:mm:ss"></fmt:formatDate></td>
                <td>${informationArticle.sources}</td>
                <td>${informationArticle.readingNum}</td>
                <td>${informationArticle.commentNum}</td>
                <%--<td>${informationArticle.status}</td>--%>
                <td>${informationArticle.authorName}</td>
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

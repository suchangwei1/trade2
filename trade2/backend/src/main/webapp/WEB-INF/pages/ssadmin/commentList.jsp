<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" %>
<%@ include file="comm/include.inc.jsp" %>
<form id="pagerForm" method="post" action="ssadmin/commentList.html">
    <input type="hidden" name="pageNum" value="${pageNum}"/>
    <input type="hidden" name="articleId" value="${articleId}"/>
    <input type="hidden" name="numPerPage" value="${numPerPage}"/>
    <input type="hidden" name="orderField" value="${param.orderField}"/>
    <input type="hidden" name="orderDirection" value="${param.orderDirection}"/>
</form>
<div class="pageContent">
    <div class="panelBar">
        <ul class="toolBar">
            <shiro:hasPermission name="ssadmin/delComment.html">
                <li>
                    <a title="确定要刪除吗?" callback="reloadCommentList" targetType="dialog" target="selectedTodo" rel="ids" postType="string" href="ssadmin/delComment.html" class="delete"><span>删除评论</span></a>
                </li>
            </shiro:hasPermission>
        </ul>
    </div>
    <table class="table" width="100%" layoutH="138" targetType="dialog">
        <thead>
        <tr>
            <th width="22"><input type="checkbox" group="ids"
                                  class="checkboxCtrl">
            </th>
            <th width="50">序号</th>
            <th width="150" orderField="articleId"
                    <c:if test='${param.orderField == "articleId" }'> class="${param.orderDirection}" </c:if>>文章ID编号</th>
            <th width="150" orderField="loginName"
                    <c:if test='${param.orderField == "loginName" }'> class="${param.orderDirection}" </c:if>>登陆名</th>
            <th width="150" orderField="authorName"
                    <c:if test='${param.orderField == "authorName" }'> class="${param.orderDirection}" </c:if>>用户名</th>
            <th width="150" orderField="createdTime"
                    <c:if test='${param.orderField == "createdTime" }'> class="${param.orderDirection}" </c:if>>时间</th>
            <th width="">内容</th>
        </tr>
        </thead>
        <tbody>
        <c:forEach items="${list}" var="comment" varStatus="num">
            <tr target="id" rel="${comment.id}">
                <td>
                    <input name="ids" value="${comment.id}" type="checkbox">
                </td>
                <td>${num.index +1}</td>
                <td>${comment.articleId}</td>
                <td>${comment.loginName}</td>
                <td>${comment.authorName}</td>
                <td><fmt:formatDate value="${comment.createdTime}" pattern="yyyy-MM-dd HH:mm:ss"/> </td>
                <td>${comment.content}</td>
            </tr>
        </c:forEach>
        </tbody>
    </table>

    <div class="panelBar">
        <div class="pages">
            <span>总共: ${totalCount}条</span>
        </div>
        <div class="pagination" targetType="dialog" totalCount="${totalCount}"
             numPerPage="${numPerPage}" pageNumShown="5"
             currentPage="${pageNum}"></div>
    </div>
    <script>
        function reloadCommentList(json){
            DWZ.ajaxDone(json);
            if (json.statusCode == DWZ.statusCode.ok){
                if ("closeCurrent" == json.callbackType) {
                    $.pdialog.closeCurrent();
                }
                if (json.navTabId){
                    navTab.reload(json.forwardUrl, {navTabId: json.navTabId});
                } else {
                    $.pdialog.switchDialog($('body').data('commentList'));
                    var $pagerForm = $("#pagerForm", navTab.getCurrentPanel());
                    var args = $pagerForm.size()>0 ? $pagerForm.serializeArray() : {}
                    dialogPageBreak(args, json.rel);
                }
            }
        }
    </script>
</div>

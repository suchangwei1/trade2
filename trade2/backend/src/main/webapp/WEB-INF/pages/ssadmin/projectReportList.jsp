<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" %>
<%@ include file="comm/include.inc.jsp" %>
<form id="pagerForm" class="projectReportList" method="post" action="ssadmin/projectReportList.html">
    <input type="hidden" name="pageNum" value="${pageNum}"/>
    <input type="hidden" name="articleId" value="${articleId}"/>
    <input type="hidden" name="numPerPage" value="${numPerPage}"/>
    <input type="hidden" name="orderField" value="${param.orderField}"/>
    <input type="hidden" name="orderDirection" value="${param.orderDirection}"/>
</form>
<div class="pageContent">
    <div class="panelBar">
        <ul class="toolBar">
            <shiro:hasPermission name="ssadmin/addProjectReport.html">
                <li>
                    <a height="400" width="900" target="dialog" rel="editProjectReport" href="ssadmin/editProjectReport.html?articleId=${param.articleId}&act=add" class="add"><span>新增报道</span></a>
                </li>
            </shiro:hasPermission>
            <shiro:hasPermission name="ssadmin/updateProjectReport.html">
                <li>
                    <a height="400" width="900" target="dialog" rel="editProjectReport" href="ssadmin/editProjectReport.html?act=edit&id={id}" class="edit"><span>修改报道</span></a>
                </li>
            </shiro:hasPermission>
            <shiro:hasPermission name="ssadmin/delProjectReport.html">
                <li>
                    <a title="确定要刪除吗?" callback="reloadReportList" targetType="dialog" target="selectedTodo" rel="ids" postType="string" href="ssadmin/delProjectReport.html" class="delete"><span>删除报道</span></a>
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
            <th width="150" orderField="title"
                    <c:if test='${param.orderField == "title" }'> class="${param.orderDirection}" </c:if>>报道标题</th>
            <th width="150" orderField="createdTime"
                    <c:if test='${param.orderField == "createdTime" }'> class="${param.orderDirection}" </c:if>>时间</th>
            <th width="">内容</th>
        </tr>
        </thead>
        <tbody>
        <c:forEach items="${list}" var="report" varStatus="num">
            <tr target="id" rel="${report.id}">
                <td>
                    <input name="ids" value="${report.id}" type="checkbox">
                </td>
                <td>${num.index +1}</td>
                <td>${report.articleId}</td>
                <td>${report.title}</td>
                <td><fmt:formatDate value="${report.createdTime}" pattern="yyyy-MM-dd HH:mm:ss"/> </td>
                <td>${report.content}</td>
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
        function reloadReportList(json){
            DWZ.ajaxDone(json);
            if (json.statusCode == DWZ.statusCode.ok){
                if ("closeCurrent" == json.callbackType) {
                    $.pdialog.closeCurrent();
                }
                if (json.navTabId){
                    navTab.reload(json.forwardUrl, {navTabId: json.navTabId});
                } else {
                    $.pdialog.switchDialog($('body').data('projectReportList'));
                    var $pagerForm = $("#pagerForm", $.pdialog.getCurrent());
                    var args = $pagerForm.size()>0 ? $pagerForm.serializeArray() : {}
                    dialogPageBreak(args, json.rel);
                }
            }
        }
    </script>
</div>

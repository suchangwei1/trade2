<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" %>
<%@ include file="comm/include.inc.jsp" %>
<form id="pagerForm" method="post" action="/ssadmin/roadShowPictureList.html">
    <input type="hidden" name="pageNum" value="${pageNum}"/>
    <input type="hidden" name="roadShowId" value="${roadShowId}"/>
    <input type="hidden" name="numPerPage" value="${numPerPage}"/>
    <input type="hidden" name="orderField" value="${param.orderField}"/>
    <input type="hidden" name="orderDirection" value="${param.orderDirection}"/>
</form>
<div class="pageContent">
    <div class="panelBar">
        <ul class="toolBar">
            <shiro:hasPermission name="ssadmin/addRoadShowPictures.html">
                <li>
                    <form action="/ssadmin/addRoadShowPictures.html" method="post" enctype="multipart/form-data"
                          targetType="dialog" onsubmit="return iframeCallback(this, reloadPicture);">
                        <input type="hidden" value="${roadShowId}" name="roadShowId">
                        <input type="file" hidden formenctype="multipart/form-data" id="file" name="file">
                        <input type="submit" hidden id="submit">
                    </form>

                    <a class="add" href="javascript:;" onclick="$('#file').click()" height="500" width="900">
                        <span>新增</span>
                    </a>
                </li>
            </shiro:hasPermission>
            <shiro:hasPermission name="/ssadmin/deleteRoadShowPictures.html">
                <li>
                    <a title="确定要刪除吗?" callback="reloadRoadShowPictureList" targetType="dialog" target="selectedTodo" rel="ids" postType="string" href="/ssadmin/deleteRoadShowPictures.html" class="delete"><span>删除图片</span></a>
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
            <th width="22">序号</th>
            <th width="50">演讲主题</th>
            <th align="center" style="width: 120px">图片</th>
            <th width="50" orderField="createdTime" <c:if test="${param.orderField == 'createdTime'}"> class="${param.orderDirection}" </c:if>>
                上传时间
            </th>
        </tr>
        </thead>
        <tbody>
        <c:forEach items="${list}" var="roadShowPicture" varStatus="num">
            <tr target="id" rel="${roadShowPicture.id}" class="pictureTr">
                <td>
                    <input style="height: 90px" name="ids" value="${roadShowPicture.id}" type="checkbox">
                </td>
                <td>${num.index +1}</td>
                <td>${roadShow.title}</td>
                <td><img height="100" width="120" src="${cdn}/${roadShowPicture.path}"></td>
                <td><fmt:formatDate value="${roadShowPicture.createdTime}" pattern="yyyy-MM-dd HH:mm:ss"/></td>
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
        function reloadRoadShowPictureList(json){
            DWZ.ajaxDone(json);
            if (json.statusCode == DWZ.statusCode.ok) {
                if ("closeCurrent" == json.callbackType) {
                    $.pdialog.closeCurrent();
                }
                if (json.navTabId) {
                    navTab.reload(json.forwardUrl, {navTabId: json.navTabId});
                } else {
                    $.pdialog.switchDialog($('body').data('roadShowPictureList'));
                    var $pagerForm = $("#pagerForm", navTab.getCurrentPanel());
                    var args = $pagerForm.size() > 0 ? $pagerForm.serializeArray() : {};
                    dialogPageBreak(args, json.rel);
                }
            }
        }

        function reloadPicture() {
            var dialog = $.pdialog.getCurrent();
            $.pdialog.reload(dialog.data("url"));
        }


        $("#file").on("change", function () {
            var value = $(this).val();
            if(value){
                $("input[type=submit]").click();
            }
        })


    </script>
</div>

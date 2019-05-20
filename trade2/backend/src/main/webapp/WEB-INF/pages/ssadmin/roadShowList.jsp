<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" %>
<%@ include file="comm/include.inc.jsp" %>
<form id="pagerForm" method="post" action="/ssadmin/roadShowList.html">
    <input type="hidden" name="status" value="${param.status}">
    <input type="hidden" name="keywords" value="${keywords}"/>
    <input type="hidden" name="checkStatus" value="${param.checkStatus}"/>
    <input type="hidden" name="articleId" value="${param.articleId}"/>
    <input type="hidden" name="isAudit" value="${param.isAudit}"/>
    <input type="hidden" name="pageNum" value="${currentPage}"/>
    <input type="hidden" name="numPerPage" value="${numPerPage}"/>
    <input type="hidden" name="orderField" value="${param.orderField}"/>
    <input type="hidden" name="isDialog" value="${param.isDialog}">
    <input type="hidden" name="orderDirection" value="${param.orderDirection}"/>
</form>


<div class="pageHeader">
    <form onsubmit="return ${isDialog == true ? "dialog" : "navTab"}Search(this);" action="/ssadmin/roadShowList.html" method="post">
        <input type="hidden" name="articleId" value="${articleId}"/>
        <input type="hidden" name="isDialog" value="${isDialog}">
        <div class="searchBar">
            <table class="searchContent">
                <tr>
                    <%--<td>标题：<input type="text" name="title" value="${title}" size="60"/></td>--%>
                    <%--<td></td>--%>
                    <c:if test="${isAudit != true}">
                        <td>审核状态：
                            <select type="combox" name="checkStatus">
                                <option <c:if test="${checkStatus == 0}">selected</c:if> value="0">全部</option>
                                <option <c:if test="${checkStatus == 1}">selected</c:if> value="1">未审核</option>
                                <option <c:if test="${checkStatus == 2}">selected</c:if> value="2">已审核</option>
                                <option <c:if test="${checkStatus == 3}">selected</c:if> value="3">已结束</option>
                                <%--<option <c:if test="${checkStatus == -1}">selected</c:if> value="-1">临时</option>--%>
                                <option <c:if test="${checkStatus == -3}">selected</c:if> value="-3">审核未通过</option>
                            </select>
                        </td>
                    </c:if>
                </tr>
            </table>
            <c:if test="${isAudit != true}">
            <div class="subBar">
                <ul>
                    <li>
                        <div class="buttonActive">
                            <div class="buttonContent">

                                    <button type="submit">查询</button>
                            </div>
                        </div>
                    </li>
                </ul>
            </div>
            </c:if>
        </div>
    </form>
</div>
<div class="pageContent">
    <div class="panelBar">
        <ul class="toolBar">
            <c:choose>
                <c:when test="${isAudit}">
                    <shiro:hasPermission name="/ssadmin/updateRoadShow.html">
                        <li><a class="edit"
                               href="/ssadmin/goRoadshowJsp.html?id={roadShow_id}&url=ssadmin/auditRoadShow&isAudit=true"
                               target="dialog" rel=roadshowAudit height="400" width="800"><span>审核</span>
                        </a></li>
                    </shiro:hasPermission>
                </c:when>
                <c:when test="${empty isAudit}">
                    <shiro:hasPermission name="/ssadmin/updateRoadShow.html">
                        <li><a class="edit"
                               href="/ssadmin/goRoadshowJsp.html?id={roadShow_id}&url=ssadmin/auditRoadShow<c:if test="${isDialog}">&isDialog=true</c:if>"
                               target="dialog" rel=roadshowAudit height="400" width="800"><span>审核</span>
                        </a></li>
                    </shiro:hasPermission>
                    <shiro:hasPermission name="/ssadmin/updateRoadShow.html">
                        <li><a class="edit"
                               href="/ssadmin/goRoadshowJsp.html?id={roadShow_id}&url=ssadmin/updateRoadShow<c:if test="${isDialog}">&isDialog=true</c:if>"
                               target="dialog"
                               rel="updateRoadShow" width="800" height="500"><span>修改</span>
                        </a></li>
                    </shiro:hasPermission>
                    <shiro:hasPermission name="/ssadmin/deleteRoadShow.html">
                        <li><a class="delete"
                               href="/ssadmin/deleteRoadShow.html?id={roadShow_id}<c:if test="${isDialog}">&isDialog=true</c:if>" target="ajaxTodo"
                               callback="reloadRoadShowList"
                               title="确定删除吗"><span>删除</span>
                        </a></li>
                    </shiro:hasPermission>
                </c:when>
            </c:choose>
            <shiro:hasPermission name="/ssadmin/roadShowPictureList.html">
                <li>
                    <a height="600" width="900" target="dialog" rel="roadShowPictureList" href="/ssadmin/roadShowPictureList.html?roadShowId={roadShow_id}" class="edit"><span>图片列表</span></a>
                </li>
            </shiro:hasPermission>
        </ul>
    </div>
    <table class="table" width="100%" layoutH="138" <c:if test="${isDialog}">targetType="dialog"</c:if>>
        <thead>
        <tr>
            <th width="20">序号</th>
            <th width="60" orderField="speaker"
                    <c:if test='${param.orderField == "speaker" }'> class="${param.orderDirection}" </c:if>>演讲者
            </th>
            <th width="60" orderField="title"
                    <c:if test='${param.orderField == "title" }'> class="${param.orderDirection}" </c:if>>标题
            </th>
            <th width="60" orderField="checkStatus"
                    <c:if test='${param.orderField == "checkStatus" }'> class="${param.orderDirection}" </c:if>>状态
            </th>
            <th width="60" orderField="startTime"
                    <c:if test='${param.orderField == "startTime" }'> class="${param.orderDirection}" </c:if>>路演时间
            </th>
            <th width="60" orderField="createdTime"
                    <c:if test='${param.orderField == "createdTime" }'> class="${param.orderDirection}" </c:if>>创建时间
            </th>
        </tr>
        </thead>
        <tbody>
        <c:forEach items="${list}" var="roadShow" varStatus="num">
            <tr target="roadShow_id" rel="${roadShow.id}">
                <td>${num.index +1}</td>
                <td>${roadShow.speaker}</td>
                <td>${roadShow.title}</td>
                <td>${roadShow.checkStatus == 1 ? "未审核" : roadShow.checkStatus == 2 ? "已审核" : roadShow.checkStatus == 3 ? "已结束" : roadShow.checkStatus == -1 ? "临时" : "审核未通过"}</td>
                <td><fmt:formatDate value="${roadShow.startTime}" pattern="yyyy-MM-dd HH:mm:ss"/></td>
                <td><fmt:formatDate value="${roadShow.createdTime}" pattern="yyyy-MM-dd HH:mm:ss"/></td>
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
    <script>
        function reloadRoadShowList(json){
            DWZ.ajaxDone(json);
            if (json.statusCode == DWZ.statusCode.ok){
                if ("closeCurrent" == json.callbackType) {
                    $.pdialog.closeCurrent();
                }
                console.info("haha");
                if (json.navTabId){
                    console.info("hehe");
                    navTab.reload(json.forwardUrl, {navTabId: json.navTabId});
                } else {
                    console.info("xixi");
                    $.pdialog.switchDialog($('body').data('roadShowList'));
                    var $pagerForm = $("#pagerForm", navTab.getCurrentPanel());
                    var args = $pagerForm.size()>0 ? $pagerForm.serializeArray() : {}
                    dialogPageBreak(args, json.rel);
                }
            }
        }
    </script>

</div>

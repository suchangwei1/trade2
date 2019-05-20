<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" %>
<%@ include file="comm/include.inc.jsp" %>
<form id="pagerForm" method="post" action="ssadmin/projectList.html">
    <input type="hidden" name="pageNum" value="${pageNum}"/>
    <input type="hidden" name="numPerPage" value="${numPerPage}"/>
    <input type="hidden" name="orderField" value="${param.orderField}"/>
    <input type="hidden" name="orderDirection" value="${param.orderDirection}"/>
</form>


<div class="pageHeader">
    <form onsubmit="return navTabSearch(this);"
          action="ssadmin/projectList.html" method="post">
        <div class="searchBar">
            <%--<table class="searchContent">
                <tr>
                    <td>会员信息：<input type="text" name="keywords"
                                    value="${keywords}" size="60" style="width: 137px; "/>
                    </td>
                    <td></td>
                    <td>日期： <input type="text" name="logDate" class="date"
                                   readonly="true" value="${logDate }"/>
                    </td>
                    <td>状态： <select type="combox" name="fstatus">
                        <c:forEach items="${statusMap}" var="status">
                            <c:if test="${status.key == fstatus}">
                                <option value="${status.key}" selected="true">${status.value}</option>
                            </c:if>
                            <c:if test="${status.key != fstatus}">
                                <option value="${status.key}">${status.value}</option>
                            </c:if>
                        </c:forEach>
                    </select>
                    </td>
                </tr>
            </table>--%>
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
        </div>
    </form>
</div>
<div class="pageContent">
    <div class="panelBar">
        <ul class="toolBar">
            <shiro:hasPermission name="ssadmin/addProject.html">
            <li>
                <a class="add" href="ssadmin/editProject.html?act=add" height="600" width="900" target="dialog" rel="editProject"><span>新增项目</span></a>
            </li>
            </shiro:hasPermission>
            <shiro:hasPermission name="ssadmin/updateProject.html">
            <li>
                <a height="600" width="900" target="dialog" href="ssadmin/editProject.html?articleId={sid}&act=edit" rel="editProject" class="edit"><span>修改项目</span></a>
            </li>
            </shiro:hasPermission>
            <shiro:hasPermission name="ssadmin/delProject.html">
            <li>
                <a title="确定要刪除吗?" target="selectedTodo" rel="ids" postType="string" href="ssadmin/delProject.html" class="delete"><span>删除项目</span></a>
            </li>
            </shiro:hasPermission>
            <shiro:hasPermission name="ssadmin/commentList.html">
            <li>
                <a height="600" width="900" target="dialog" href="ssadmin/commentList.html?articleId={sid}" rel="commentList" class="edit"><span>评论列表</span></a>
            </li>
            </shiro:hasPermission>
            <shiro:hasPermission name="ssadmin/projectReportList.html">
                <li>
                    <a height="600" width="900" target="dialog" href="ssadmin/projectReportList.html?articleId={sid}" rel="projectReportList" class="edit"><span>项目报道</span></a>
                </li>
            </shiro:hasPermission>
            <shiro:hasPermission name="/ssadmin/roadShowList.html">
                <li>
                    <a height="600" width="900" target="dialog" href="/ssadmin/roadShowList.html?articleId={sid}&isDialog=true" rel="roadShowList" class="edit"><span>相关路演</span></a>
                </li>
            </shiro:hasPermission>
        </ul>
    </div>
    <table class="table" width="100%" layoutH="138">
        <thead>
        <tr>
            <th width="22"><input type="checkbox" group="ids"
                                  class="checkboxCtrl">
            </th>
            <th width="60" orderField="articleId"
                    <c:if test='${param.orderField == "articleId" }'> class="${param.orderDirection}" </c:if>>项目id编号
            </th>
            <th width="60" orderField="projectName"
                    <c:if test='${param.orderField == "projectName" }'> class="${param.orderDirection}" </c:if>>项目名称
            </th>
            <th width="60" orderField="authorId"
                    <c:if test='${param.orderField == "authorId" }'> class="${param.orderDirection}" </c:if>>用户ID
            </th>
            <th width="60" orderField="projectType"
                    <c:if test='${param.orderField == "projectType" }'> class="${param.orderDirection}" </c:if>>推荐
            </th>
            <th width="60" <%--orderField="createdTime"
                    <c:if test='${param.orderField == "createdTime" }'> class="${param.orderDirection}" </c:if>--%>>项目创建时间
            </th>
            <th width="60" >公司名称
            </th>
            <th width="60">公司地址</th>
            <th width="40" orderField="teamSize"
                    <c:if test='${param.orderField == "teamSize" }'> class="${param.orderDirection}" </c:if>>团队人数
            </th>
            <th width="60" orderField="companyCreatedTime"
                    <c:if test='${param.orderField == "companyCreatedTime" }'> class="${param.orderDirection}" </c:if>>公司成立时间
            </th>
            <th width="60" <%--orderField="financingRound"
                    <c:if test='${param.orderField == "financingRound" }'> class="${param.orderDirection}" </c:if>--%>>融资轮次
            </th>
            <th width="60" >CEO
            </th>
            <th width="60" >CEO头像地址
            </th>
            <th width="60" >CEO简介
            </th>
            <%--<th width="60" >项目简介--%>
            <%--</th>--%>
            <%--<th width="60" >项目介绍--%>
            <%--</th>--%>
        </tr>
        </thead>
        <tbody>
        <c:forEach items="${list}" var="item" varStatus="num">
            <tr target="sid" rel="${item.articleId}">
                <td><input name="ids" value="${item.articleId}"
                           type="checkbox">
                </td>
                <td>${item.articleId}</td>
                <td><a target="_blank" href="${pageUrl}/pro/details.html?id=${item.articleId}">${item.projectName}</a></td>
                <td>${item.authorId}</td>
                <td><c:choose><c:when test="${1 == item.projectType}">是</c:when><c:otherwise>否</c:otherwise></c:choose></td>
                <td><fmt:formatDate value="${item.createdTime}" pattern="yyyy-MM-dd" /></td>
                <td>${item.companyName}</td>
                <td>${item.companyLocation}</td>
                <td>${item.teamSize}</td>
                <td><fmt:formatDate value="${item.companyCreatedTime}" pattern="yyyy-MM-dd" /></td>
                <td>${item.financingRound}</td>
                <td>${item.CEO}</td>
                <td>${item.headImgPath}</td>
                <td>${item.CEOIntroduction}</td>
                <%--<td>${item.content}</td>--%>
                <%--<td>${item.introduction}</td>--%>
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
             currentPage="${pageNum}"></div>
    </div>
</div>

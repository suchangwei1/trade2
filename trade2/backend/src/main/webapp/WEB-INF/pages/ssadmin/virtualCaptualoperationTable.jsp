<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" %>
<%@ include file="comm/include.inc.jsp" %>
<form id="pagerForm" method="post"
      action="/ssadmin/virtualCaptualoperationTable.html">
    <input type="hidden" name="" value="${param.status}"> <input
        type="hidden" name="keywstatusords" value="${keywords}"/>
    <input
            type="hidden" name="notContainKeyWords" value="${notContainKeyWords}"/>
    <input
            type="hidden" name="ftype" value="${ftype}"/><input
        type="hidden" name="oper_ftype" value="${oper_ftype}"/>
    <input type="hidden" name="startDate" value="${startDate}"/>
    <input type="hidden" name="endDate" value="${endDate}"/>
    <input type="hidden" name="fstatus" value="${fstatus}"/>
    <input type="hidden" name="pageNum" value="${currentPage}"/> <input type="hidden"
                                                                        name="numPerPage" value="${numPerPage}"/> <input
        type="hidden"
        name="orderField" value="${param.orderField}"/><input type="hidden"
                                                              name="orderDirection" value="${param.orderDirection}"/>
</form>


<div class="pageHeader">
    <form onsubmit="return navTabSearch(this);"
          action="ssadmin/virtualCaptualoperationTable.html" method="post">
        <div class="searchBar">

            <table class="searchContent">

                <tr>

                    <td>开始日期： <input type="text" name="startDate" class="date"
                                     readonly="true" value="${startDate }"/></td>
                    <td>结束日期： <input type="text" name="endDate" class="date"
                                     readonly="true" value="${endDate }"/></td>
            <%--        <td>会员信息：<input type="text" name="keywords"
                                    value="${keywords}" size="40" placeholder="用户ID/登录名/姓名/手机号/身份证号/邮箱"/></td>
                    <td>不包含会员信息：<input type="text" name="notContainKeyWords"
                                       value="${notContainKeyWords}" size="40" placeholder="用户ID/登录名/姓名/手机号/身份证号/邮箱"/></td>--%>
                    <td>虚拟币类型： <select type="combox" name="ftype">
                        <option value="0">全部</option>
                        <c:forEach items="${typeMap}" var="type">
                            <c:if test="${type.key == ftype}">
                                <option value="${type.key}" selected="true">${type.value.fname}</option>
                            </c:if>
                            <c:if test="${type.key != ftype}">
                                <option value="${type.key}">${type.value.fname}</option>
                            </c:if>
                        </c:forEach>
                    </select></td>
                </tr>
            </table>
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


            <%--<shiro:hasPermission name="ssadmin/exportVirtualCaptualoperationList.html">
                <li><a class="icon"
                    href="ssadmin/exportVirtualCaptualoperationList.html"
                 target="dwzExport" title="确定要导出操作记录吗？"><span>导出操作记录</span> </a></li>
            </shiro:hasPermission>--%>
<shiro:hasPermission name="ssadmin/exportVirtualCaptualoperationTable.html">
            <li><a class="icon"
                   href="ssadmin/exportVirtualCaptualoperationTable.html"
                   target="dwzExport" title="确定要导出操作记录吗？"><span>导出操作记录</span> </a></li>
</shiro:hasPermission>
        </ul>
    </div>
    <table class="table" width="100%" layoutH="138">
        <thead>
        <tr>
            <th width="20">序号</th>
           <%-- <th width="30" orderField="ftelephone"
                    <c:if test='${param.orderField == "fTelephone" }'> class="${param.orderDirection}" </c:if>>会员手机号
            </th>
            <th width="30" orderField="frealName"
                    <c:if test='${param.orderField == "frealName" }'> class="${param.orderDirection}" </c:if>>会员真实姓名
            </th>--%>
          <%--  <th width="30" orderField=famount
                    <c:if test='${param.orderField == "famount" }'> class="${param.orderDirection}" </c:if>>数量
            </th>--%>
            <th width="30" orderField=famount
                    <c:if test='${param.orderField == "famount" }'> class="${param.orderDirection}" </c:if>>数量
            </th>
            <th width="30" orderField="ffees"
                    <c:if test='${param.orderField == "ffees" }'> class="${param.orderDirection}" </c:if>>手续费
            </th>
            <th width="30" orderField="fname"
                    <c:if test='${param.orderField == "fname" }'> class="${param.orderDirection}" </c:if>>虚拟币类型
            </th>
            <th width="30" orderField="fstatus_s"
                    <c:if test='${param.orderField == "fstatus_s" }'> class="${param.orderDirection}"  </c:if>>状态</th>
            <th width="30" orderField="flastUpdateTime"
                    <c:if test='${param.orderField == "flastUpdateTime" }'> class="${param.orderDirection}" </c:if>>最后修改时间
            </th>

        </tr>
        </thead>
        <tbody>
        <c:forEach items="${virtualCaptualoperationTable}"
                   var="virtualCapitaloperation" varStatus="num">
            <tr target="sid_user" rel="${virtualCapitaloperation.fid}">
                <td>${num.index +1}</td>
            <%--    <td>${virtualCapitaloperation.ftelephone}</td>
                <td>${virtualCapitaloperation.frealName}</td>--%>
              <%--  <td>${virtualCapitaloperation.famount}</td>--%>
                <td>${virtualCapitaloperation.famount}</td>
                <td>${virtualCapitaloperation.ffees}${typeMap[virtualCapitaloperation.feeCoinType + 0].fShortName}</td>
                <td>${virtualCapitaloperation.fname}</td>
                <td><c:if test=" ${virtualCapitaloperation.fstatus_s}==3"></c:if>提现成功</td>
                <td>${virtualCapitaloperation.flastUpdateTime}</td>

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

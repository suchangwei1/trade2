<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="comm/include.inc.jsp"%>

    <h2 class="contentTitle">查看交易详情</h2>
    <div class="pageContent">
        <form method="post" action="ssadmin/detail.html"
              class="pageForm required-validate" onsubmit="return dialogSearch(this);">
            <%--onsubmit="return validateCallback(this,dialogAjaxDone)">--%>
            <div class="pageFormContent nowrap" layoutH="97">
                <dl>
                    <dt >交易号：</dt>
                    <dd>
                        <input type="text" name="txid" size="75" />
                    </dd>
                </dl>
                <dl>
                    <dt >币种：</dt>
                    <dd>
                        <select type="combox" name="ftype">
                            <c:forEach items="${typeMap}" var="type">
                                <c:if test="${type.key == ftype}">
                                    <option value="${type.key}" selected="true">${type.value}</option>
                                </c:if>
                                <c:if test="${type.key != ftype}">
                                    <option value="${type.key}">${type.value}</option>
                                </c:if>
                            </c:forEach>
                        </select>
                    </dd>
                </dl>

            </div>
            <div class="formBar">
                <ul>
                    <li><div class="buttonActive">
                        <div class="buttonContent">
                            <button type="submit" >查看</button>
                            <%--<a class="edit"--%>
                            <%--href="ssadmin/xiangqing2.html"--%>
                            <%--&lt;%&ndash;href="ssadmin/xiangqing2.html?uid={uid}&password={password}"&ndash;%&gt;--%>
                            <%--height="320" width="800" target="dialog"--%>
                            <%--rel="viewVirtualCapitaloperation" title="查看交易信息"><span>查看交易信息</span>--%>
                            <%--</a>--%>
                        </div>
                    </div>
                    </li>
                    <li><div class="button">
                        <div class="buttonContent">
                            <button type="button" class="close">取消</button>
                        </div>
                    </div>
                    </li>
                </ul>
            </div>
        </form>
    </div>



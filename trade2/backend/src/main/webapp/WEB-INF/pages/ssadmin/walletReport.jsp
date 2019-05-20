<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" %>
<%@ include file="comm/include.inc.jsp" %>
<!DOCTYPE HTML>
<html>
<head>
</head>
<body>
<div class="pageHeader">
    <form onsubmit="return navTabSearch(this);"
          action="ssadmin/walletReport.html" method="post">
        <div class="searchBar">

            <table class="searchContent">
                <tr>
                    <td>日期：<input type="text" name="curDate"
                                  class="date textInput readonly" readonly="true" size="15"
                                  value="<fmt:formatDate value="${curDate}" pattern="yyyy-MM-dd"/>"></td>
                    <td>
                        <div class="buttonActive">
                            <div class="buttonContent">
                                <button type="submit" style="line-height:27px;">查询</button>
                            </div>
                        </div>
                    </td>
                </tr>
            </table>
        </div>
    </form>
</div>
<div class="pageContent">
    <div class="panelBar">
        <ul class="toolBar">
        </ul>
    </div>
    <div class="pageFormContent" layoutH="20">
        <fieldset>
            <legend>钱包信息</legend>
            <c:forEach items="${virtualQtyList}" var="virtual">
                <dl>
                    <dt>全站${virtual.fName}总数量：</dt>
                    <dd style="color:red;font-weight:bold;">
						<span class="unit"><fmt:formatNumber
                                value="${virtual.totalQty}" pattern="#0.######"/> </span>
                    </dd>
                </dl>
                <dl>
                    <dt>含冻结：</dt>
                    <dd style="color:red;font-weight:bold;">
						<span class="unit"><fmt:formatNumber
                                value="${virtual.frozenQty}" pattern="#0.######"/> </span>
                    </dd>
                </dl>
            </c:forEach>
        </fieldset>
    </div>
</div>
</body>
</html>


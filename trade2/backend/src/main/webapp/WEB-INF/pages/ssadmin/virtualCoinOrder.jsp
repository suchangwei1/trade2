<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" %>
<%@ include file="comm/include.inc.jsp" %>

<%--刷新页面--%>
<form id="pagerForm" method="post"
      action="ssadmin/virtualCoinOrder.html">
    <input type="hidden" name="status" value="${param.status}"> <input
        type="hidden" name="keywords" value="${keywords}"/> <input
        type="hidden" name="pageNum" value="${currentPage}"/> <input
        type="hidden" name="numPerPage" value="${numPerPage}"/> <input
        type="hidden" name="orderField" value="${param.orderField}"/><input
        type="hidden" name="orderDirection" value="${param.orderDirection}"/>
</form>
<div class="pageHeader">
    <form onsubmit="return navTabSearch(this);"
          action="/ssadmin/virtualCoinOrder.html" method="post">
        <div class="searchBar">

            <table class="searchContent">
                <tr>
                    <td>虚拟币名称：<input type="text" name="keywords"
                                     value="${keywords}" size="60"/></td>
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
            <%--<li>
                <a class="edit"
                   href="/ssadmin/virtualCoinOrder.html?type=4" target="navTab"
                   height="600" width="800"><span>首页排序</span> </a>
            </li>
            <li>
                <a class="edit"
                   href="/ssadmin/virtualCoinOrder.html?type=2" target="navTab"
                   height="600" width="800"><span>ETH区排序</span> </a>
            </li>--%>
            <shiro:hasPermission name="/ssadmin/updteTotalOrder.html">
            <li>
                <a class="edit"
                   href="/ssadmin/virtualCoinOrder.html?type=5" target="navTab"
                   height="600" width="800"><span>整体排序</span> </a>
            </li>
            </shiro:hasPermission>
        </ul>
    </div>
    <style>
        .dn {
            display: none
        }
        .order{
            position: relative;
            top: 4px;
        }
    </style>
    <table class="table" width="40%" layoutH="138">
        <thead>
        <tr>
            <th width="50">序号</th>
            <th width="50">名称</th>
            <c:choose>
                <c:when test="${type ==4 || type==0}">
                    <th width="50">首页排序</th>
                </c:when>
                <c:when test="${type == 2}">
                    <th width="50">ETH区排序</th>
                </c:when>
                <c:when test="${type == 5}">
                    <th width="50">整体排序</th>
                </c:when>
            </c:choose>
        </tr>
        </thead>
        <tbody>
        <c:forEach items="${virtualCoinTypeList}" var="virtualCoinType" varStatus="num">
            <tr target="sid_user" rel="${virtualCoinType.fid}">
                <td>${num.index +1}</td>
                <td>${virtualCoinType.fname}</td>
                <c:choose>
                <c:when test="${type ==4 || type==0}">
                    <td class="editable" data-type="${type}" data-id="${virtualCoinType.fid}">
                        <input class="dn" type="tel" value="${virtualCoinType.homeOrder}">
                        <span class="order">${virtualCoinType.homeOrder}</span>
                    </td>
                </c:when>
                <c:when test="${type == 1 || type == 2 || type == 3}">
                    <td class="editable" data-type="${type}" data-id="${virtualCoinType.fid}">
                        <input class="dn" type="tel" value="${virtualCoinType.typeOrder}">
                        <span class="order">${virtualCoinType.typeOrder}</span>
                    </td>
                </c:when>
                <c:when test="${type == 5}">
                    <td class="editable" data-type="${type}" data-id="${virtualCoinType.fid}">
                        <input class="dn" type="tel" value="${virtualCoinType.totalOrder}">
                        <span class="order">${virtualCoinType.totalOrder}</span>
                    </td>
                </c:when>
                </c:choose>
            </tr>
        </c:forEach>
        <%--<c:if test="${type ==4 || type==0}">--%>
        <%--<td class="editable"><span class="dn typeid">${type}</span><span class="coinid dn">${virtualCoinType.fid}</span><input class="dn" type="tel" value="${virtualCoinType.homeOrder}"><span--%>
        <%--class="order">${virtualCoinType.homeOrder}</span></td>--%>
        <%--</c:if>--%>
        <%--<c:if test="${type !=4}">--%>
        <%--<td class="editable"><span class="dn typeid">${type}</span><span class="coinid dn">${virtualCoinType.fid}</span><input class="dn" type="tel" value="${virtualCoinType.typeOrder}"><span--%>
        <%--class="order">${virtualCoinType.typeOrder}</span></td>--%>
        <%--</c:if>--%>
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

<%--<script src="/static/ssadmin/js/js/jquery-1.7.2.min.js"></script>--%>
<script>
    $(function () {

        $(".pageContent").on('click', '.editable', function () {
            var $this = $(this);

            //order value
            var $input = $this.find("input");
            var value = $input.val();

            //coinId
            var id = $this.data("id");

            //隐藏span，显示input
            var $order = $this.find(".order");
            $order.addClass("dn");
            $input.removeClass("dn");
            var val = $input.val();
            $input.focus();
            $input.val("");
            $input.val(val);

            //恢复其他input
            var $tbody = $this.parent().parent();

            $tbody.find("input").each(function () {
                if (!$(this).is($input)) {
                    if (!$(this).hasClass("dn")) {
                        $(this).parent().find(".order").removeClass("dn");
                        $(this).addClass("dn");
                    }
                }
            });

        })


        //onblur函数
        $(".pageContent").on('blur', '.editable input', function () {
            var $this = $(this);
            var $parent = $this.parent().parent(".editable");
            var value = $this.val();
            var value2 = $this.siblings(".order").text();
            if(value != value2){
                var id = $parent.data("id");
                var type = $parent.data("type");
                if (type == 4) {
                    var url = "/ssadmin/updateHomeOrder.html";
                } else if(type == 1 || type == 2 || type == 3){
                    var url = "/ssadmin/updateTypeOrder.html";
                }else if(type == 5){
                    var url = "/ssadmin/updteTotalOrder.html";
                }
                var paras = {"id": id, "value": value};
                $.post(url, paras, function(data){
                    $this.siblings(".order").text(data);
                    $this.val(data);
                    var href = "/ssadmin/virtualCoinOrder.html?type=" + type;
                    $("a[href='"+href+"']").click();
                });
            }
        })

        //Enter函数
        $(".pageContent").on('keypress', '.editable input', function (e) {
            if(e.which == 13){
                var $this = $(this);
                var $parent = $this.parent().parent(".editable");
                var value = $this.val();
                var value2 = $this.siblings(".order").text();
                if(value != value2){
                    var id = $parent.data("id");
                    var type = $parent.data("type");
                    if (type == 4) {
                        var url = "/ssadmin/updateHomeOrder.html";
                    } else if(type == 1 || type == 2 || type == 3){
                        var url = "/ssadmin/updateTypeOrder.html";
                    }else if(type == 5){
                        var url = "/ssadmin/updteTotalOrder.html";
                    }
                    var paras = {"id": id, "value": value};
                    $.post(url, paras, function(data){
                        $this.siblings(".order").text(data);
                        $this.val(data);
                        var href = "/ssadmin/virtualCoinOrder.html?type=" + type;
                        $("a[href='"+href+"']").click();
                    });
                }
            }
        })

    })


</script>

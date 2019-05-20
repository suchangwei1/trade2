<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" %>
<%@ include file="comm/include.inc.jsp" %>
<form id="pagerForm" method="post"
      action="ssadmin/chitChatList.html">
</form>
<div class="pageContent">
    <div class="panelBar">
        <ul class="toolBar">
            <shiro:hasPermission name="ssadmin/delChitChatMessage.html">
                <li>
                    <a title="确定要刪除吗?" id="delChitChatMessage" href="ssadmin/delChitChatMessage.html?message={msg}" class="delete"><span>删除信息</span></a>
                </li>
            </shiro:hasPermission>
        </ul>
    </div>
    <table class="table" width="100%" layoutH="138">
        <thead>
        <tr>
            <th width="50">序号</th>
            <th width="150">登录名</th>
            <th width="150">用户名</th>
            <th width="150">时间</th>
            <th width="">内容</th>
        </tr>
        </thead>
        <tbody>
        <c:forEach items="${list}" var="msg" varStatus="num">
            <tr target="msg" rel="${msg.msg_sources}">
                <td>${num.index +1}</td>
                <td>${msg.loginName}</td>
                <td>${msg.name}</td>
                <td>${msg.time}</td>
                <td>${msg.content}</td>
            </tr>
        </c:forEach>
        </tbody>
    </table>
    <script>
        $(function(){
            $("#delChitChatMessage").on('click', function (event) {
                var $this = $(this);
                var url = unescape($this.attr("href")).replaceTmById($(event.target).parents(".unitBox:first"));
                DWZ.debug(url);
                if (!url.isFinishedTm()) {
                    alertMsg.error($this.attr("warn") || DWZ.msg("alertSelectMsg"));
                    return false;
                }
                var url_args = url.split('\?');
                alertMsg.confirm('确定要刪除吗?', {
                    okCall: function(){
                        $.ajax({
                            type:'POST',
                            url:url_args[0],
                            data:url_args[1],
                            dataType:"json",
                            cache: false,
                            success: navTabAjaxDone,
                            error: DWZ.ajaxError
                        });
                    }
                });
                event.preventDefault();
            });
        });
    </script>
</div>

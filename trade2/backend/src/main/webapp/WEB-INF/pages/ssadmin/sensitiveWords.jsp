<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" %>
<%@ include file="comm/include.inc.jsp" %>
<form id="pagerForm" method="post"
      action="ssadmin/sensitiveWords.html">
</form>
<div class="pageContent">
    <div class="panelBar">
        <ul class="toolBar">
            <shiro:hasPermission name="ssadmin/addSensitiveWord.html">
                <li>
                    <a rel="addSensitiveWord" height="300" width="600" target="dialog" href="ssadmin/editSensitiveWord.html?act=add" class="add"><span>新增</span></a>
                </li>
            </shiro:hasPermission>
            <shiro:hasPermission name="ssadmin/updateSensitiveWord.html">
                <li>
                    <a rel="updateSensitiveWord" height="300" width="600" target="dialog" href="ssadmin/editSensitiveWord.html?key={id_key}&act=edit" class="delete"><span>修改</span></a>
                </li>
            </shiro:hasPermission>
            <shiro:hasPermission name="ssadmin/delChitChatMessage.html">
                <li>
                    <a title="确定要刪除吗?" target="selectedTodo" rel="keys" href="ssadmin/delSensitiveWord.html" class="delete"><span>删除词汇</span></a>
                </li>
            </shiro:hasPermission>
        </ul>
    </div>
    <table class="table" width="100%" layoutH="138">
        <thead>
        <tr>
            <th width="10"><input type="checkbox" group="keys"
                                  class="checkboxCtrl">
            </th>
            <th width="150">序号</th>
            <th width="150">词汇</th>
            <th width="150">替换词汇</th>
        </tr>
        </thead>
        <tbody>
        <c:forEach items="${words}" var="word" varStatus="num">
            <tr target="id_key" rel="${word.id_key}">
                <td><input name="keys" value="${word.id_key}"
                           type="checkbox">
                </td>
                <td>${num.index +1}</td>
                <td>${word.key}</td>
                <td>${word.val}</td>
            </tr>
        </c:forEach>
        </tbody>
    </table>
</div>

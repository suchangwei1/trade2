<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" %>
<%@ include file="comm/include.inc.jsp" %>
<form id="pagerForm" method="post"
      action="ssadmin/coinDetailList.html">
</form>
<div class="pageContent">
    <div class="panelBar">
        <ul class="toolBar">
            <shiro:hasPermission name="ssadmin/updateVirtualCoinDetail.html">
                <li><a class="edit"
                       href="/ssadmin/viewVirtualCoinDetail.html?fVid={sid_user}"
                       height="600" width="700" target="dialog" rel="viewVirtualCoinDetail"><span>详情</span>
                </a>
                </li>
            </shiro:hasPermission>
        </ul>
    </div>
    <table class="table" width="100%" layoutH="138">
        <thead>
        <tr>
            <th width="20">序号</th>
            <th width="20">币类型</th>
            <th width="20">中文名称</th>
            <th width="20">英文名称</th>
            <th width="20">研发者</th>
            <th width="20">货币数量</th>
            <th width="20">发布时间</th>
            <th width="20">区块时间</th>
            <th width="20">特色</th>
            <th width="20">不足之处</th>
            <th width="20">描述</th>
            <th width="20">备注信息</th>
        </tr>
        </thead>
        <tbody>
        <c:forEach items="${coins}" var="c" varStatus="num">
            <tr target="sid_user" rel="${c.fid}">
                <td>${num.index +1}</td>
                <td>${c.fname}</td>
                <td>${details[c.fid].name}</td>
                <td>${details[c.fid].ename}</td>
                <td>${details[c.fid].developers}</td>
                <td>${details[c.fid].amount}</td>
                <td><fmt:formatDate value="${details[c.fid].releaseTime}" pattern="yyyy-MM-dd HH:mm:ss"/></td>
                <td>${details[c.fid].blocksTime}</td>
                <td>${details[c.fid].feature}</td>
                <td>${details[c.fid].defects}</td>
                <td>${details[c.fid].desc}</td>
                <td>${details[c.fid].remark}</td>
            </tr>
        </c:forEach>
        </tbody>
    </table>
</div>

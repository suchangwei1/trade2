<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" %>
<%@ include file="comm/include.inc.jsp" %>
<h2 class="contentTitle">添加布局</h2>
<div class="pageContent">

    <form method="post" action="/ssadmin/updateLayout.html"
          class="pageForm required-validate"
          onsubmit="return iframeCallback(this, dialogAjaxDone);">
        <div class="pageFormContent nowrap" layoutH="97">
            <input type="hidden" name="layoutId" value="${layoutInfo.id}">
            <dl>
                <dt>推荐板块：</dt>
                <dd>
                    <select name="frameId">
                        <c:forEach items="${frameNameMap}" var="entry">
                            <c:if test="${entry.key == layoutInfo.frameId}">
                                <option value="${entry.key}" selected="true">${entry.value}</option>
                            </c:if>
                            <c:if test="${entry.key != layoutInfo.frameId}">
                                <option value="${entry.key}">${entry.value}</option>
                            </c:if>
                        </c:forEach>
                    </select>
                </dd>
            </dl>
            <dl>
                <dt>文章ID：</dt>
                <dd>
                    <input type="tel" name="articleId" class="inputStyle" value="${layoutInfo.articleId}">
                </dd>
            </dl>
            <dl>
                <dt>文章标题：</dt>
                <dd>
                    <input type="text" name="title" value="${layoutInfo.title}"
                           suggestFields="id,title"
                           suggestUrl="/ssadmin/forInformationArticleLookup.html"
                           lookupGroup="orgLookup" readonly="readonly" size="30"/>
                    <a class="btnLook" href="/ssadmin/forInformationArticleLookup.html" lookupGroup="">查找带回</a>
                </dd>
            </dl>
            <dl>
                <dt>权重：</dt>
                <dd>
                    <input type="number" name="rank" class="inputStyle" value="${layoutInfo.rank}">(权重越大，排名越靠前。最大9999)
                </dd>
            </dl>
        </div>
        <div class="formBar">
            <ul>
                <li>
                    <div class="buttonActive">
                        <div class="buttonContent">
                            <button type="submit">保存</button>
                        </div>
                    </div>
                </li>
                <li>
                    <div class="button">
                        <div class="buttonContent">
                            <button type="button" class="close">取消</button>
                        </div>
                    </div>
                </li>
            </ul>
        </div>
    </form>

</div>


<script type="text/javascript">
    function customvalidXxx(element) {
        if ($(element).val() == "xxx") return false;
        return true;
    }
</script>

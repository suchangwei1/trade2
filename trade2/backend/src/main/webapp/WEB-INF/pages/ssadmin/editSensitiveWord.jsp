<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="comm/include.inc.jsp"%>
<c:choose>
    <c:when test="${'add' == param.act}">
        <h2 class="contentTitle">新增敏感词汇</h2>
    </c:when>
    <c:when test="${'edit' == param.act}">
        <h2 class="contentTitle">编辑敏感词汇</h2>
    </c:when>
</c:choose>


<div class="pageContent">
    <c:choose>
    <c:when test="${'add' == param.act}">
    <form method="post" action="ssadmin/addSensitiveWord.html"
          class="pageForm required-validate" enctype="multipart/form-data"
          onsubmit="return iframeCallback(this, dialogAjaxDone);">
        </c:when>
        <c:when test="${'edit' == param.act}">
        <form method="post" action="ssadmin/updateSensitiveWord.html"
              class="pageForm required-validate" enctype="multipart/form-data"
              onsubmit="return iframeCallback(this, dialogAjaxDone);">
            </c:when>
            </c:choose>
        <div class="pageFormContent nowrap" layoutH="97">
            <dl>
                <dt>词汇：</dt>
                <dd>
                    <input type="text" name="key"  class="required"
                           size="50"  value="${key}"/>
                </dd>
            </dl>
            <dl><dt>替换词汇：</dt>
                <dd class="keyworddd">
                    <input type="text" name="val"  class="required"
                           size="50"  value="${val}"/>
                </dd>
            </dl>
        </div>
        <div class="formBar">
            <ul>
                <li><div class="buttonActive">
                    <div class="buttonContent">
                        <button type="submit">保存</button>
                    </div>
                </div></li>
                <li><div class="button">
                    <div class="buttonContent">
                        <button type="button" class="close">取消</button>
                    </div>
                </div></li>
            </ul>
        </div>
    </form>

</div>


<script type="text/javascript">
    function customvalidXxx(element) {
        if ($(element).val() == "xxx")
            return false;
        return true;
    }
</script>
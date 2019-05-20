<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="comm/include.inc.jsp"%>
<c:choose>
    <c:when test="${'add' == param.act}">
        <h2 class="contentTitle">新增项目报道</h2>
    </c:when>
    <c:when test="${'add' == param.act}">
        <h2 class="contentTitle">编辑项目报道</h2>
    </c:when>
</c:choose>


<div class="pageContent">
    <c:choose>
    <c:when test="${'add' == param.act}">
    <form method="post" action="ssadmin/addProjectReport.html"
          class="pageForm required-validate" enctype="multipart/form-data"
          onsubmit="return iframeCallback(this, reloadReportList);">
        </c:when>
        <c:when test="${'edit' == param.act}">
        <form method="post" action="ssadmin/updateProjectReport.html"
              class="pageForm required-validate" enctype="multipart/form-data"
              onsubmit="return iframeCallback(this, reloadReportList);">
            </c:when>
            </c:choose>
            <div class="pageFormContent nowrap" layoutH="97">
                <input type="hidden" name="id" value="${item.id}">
                <input type="hidden" name="articleId" value="${param.articleId}${item.articleId}">
                <dl>
                    <dt>报道标题：</dt>
                    <dd>
                        <input type="text" name="title"  class="required"
                               size="50"  value="${item.title}"/>
                    </dd>
                </dl>
                <dl>
                    <dt>报道内容：</dt>
                    <textarea class="editor" name="content" rows="10" cols="100"
                              tools="mfull" upImgUrl="ssadmin/upload.html"
                              upImgExt="jpg,jpeg,gif,png">${item.content }
                    </textarea>
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
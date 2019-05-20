<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" %>
<%@ include file="comm/include.inc.jsp" %>
<h2 class="contentTitle">新增首页推荐</h2>
<div class="pageContent">

    <form method="post" action="/ssadmin/saveLayout.html"
          class="pageForm required-validate"
          onsubmit="return iframeCallback(this, dialogAjaxDone);">
        <div class="pageFormContent nowrap" layoutH="97">
            <dl>
                <dt>推荐板块：</dt>
                <dd>
                    <select name="frameId" id="frameIdSelect" onchange="appendClassId()">
                        <c:forEach var="entry" items="${frameNameMap}">
                            <option value="${entry.key}">${entry.value}</option>
                        </c:forEach>
                    </select>
                </dd>
                <script>
                    function appendClassId() {
                        var frameId = $("#frameIdSelect option:selected").val();
                        var url = $("#informationLookup").attr("href")
                        var param = "?classId=" + frameId;
                        if(frameId == 1){
                            $("#informationLookup").attr("href", url.substr(0, url.lastIndexOf("html") + 4));
                            return;
                        }
                        var url = $("#informationLookup").attr("href")
                        if(url.charAt(url.length - 1) == "l"){
                            $("#informationLookup").attr("href", url + param);
                        }else{
                            $("#informationLookup").attr("href", url.substr(0, url.lastIndexOf("html") + 4) + param);
                        }
                    }
                </script>
            </dl>
            <dl>
                <dt>文章ID：</dt>
                <dd>
                    <input style="width: 124px" type="tel" name="informationArticleLookup.id" class="inputStyle" value="">
                </dd>
            </dl>
            <dl>
                <dt>文章标题：</dt>
                <dd>
                    <input type="text" name="informationArticleLookup.title" value=""
                           suggestFields="id,title"
                           suggestUrl="/ssadmin/forInformationArticleLookup.html"
                           lookupGroup="orgLookup" readonly="readonly" size="30"/>
                    <a id= "informationLookup" class="btnLook" href="/ssadmin/forInformationArticleLookup.html" lookupGroup="informationArticleLookup">查找带回</a>
                </dd>
            </dl>
            <dl>
                <dt>权重：</dt>
                <dd>
                    <input type="number" name="rank" class="inputStyle" style="width: 145px" value="0">(权重越大，排名越靠前。最大9999)
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

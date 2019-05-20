<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" %>
<%@ include file="comm/include.inc.jsp" %>
<h2 class="contentTitle">
    路演审核
</h2>


<div class="pageContent">
    <form method="post" action="/ssadmin/updateRoadShow.html"
          class="pageForm required-validate"
          onsubmit="return validateCallback(this,reloadRoadShowList)">
        <input type="hidden" name="isAudit" value="${isAudit ? "true" : "false"}">
        <input type="hidden" name="isDialog" value="${empty isDialog ? "false" : "true"}">
        <input type="hidden" name="id" value="${roadshow.id}"/>
        <div class="pageFormContent nowrap" layoutH="97">
            <dl>
                <dt>审核结果：</dt>
                <dd>
                    <select type="combox" name="checkStatus" class="required">
                        <option value="2">通过</option>
                        <option value="-3">不通过</option>
                    </select>
                </dd>
            </dl>

            <dl>
                <dt>演讲者：</dt>
                <dd>
                    <input type="text" name="speaker" readonly="true" size="70" value="${roadshow.speaker}"/>
                </dd>
            </dl>
            <dl>
                <dt>题目：</dt>
                <dd>
                    <input type="text" name="title" readonly="true" size="70" value="${roadshow.title}"/>
                </dd>
            </dl>
            <dl>
                <dt>开始时间：</dt>
                <dd>
                    <input type="text" name="startTime" readonly="true" size="70"
                           value="<fmt:formatDate value="${roadshow.startTime}" pattern="yyyy-MM-dd HH:mm:ss"/>">
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
    function reloadRoadShowList(json){
        DWZ.ajaxDone(json);
        if ("closeCurrent" == json.callbackType) {
            $.pdialog.closeCurrent();
        }
        if (json.navTabId){
            navTab.reload(json.forwardUrl, {navTabId: json.navTabId});
        } else {
            $.pdialog.switchDialog($('body').data('roadShowList'));
            var $pagerForm = $("#pagerForm", navTab.getCurrentPanel());
            var args = $pagerForm.size()>0 ? $pagerForm.serializeArray() : {}
            dialogPageBreak(args, json.rel);
        }
    }
</script>


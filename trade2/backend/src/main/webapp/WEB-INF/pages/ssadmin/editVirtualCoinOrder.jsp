<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" %>
<%@ include file="comm/include.inc.jsp" %>
<h2 class="contentTitle">修改虚拟币显示次序</h2>
<div class="pageContent">
    <form method="post" action="ssadmin/updateVirtualCoinOrder.html"
          class="pageForm required-validate" enctype="multipart/form-data"
          onsubmit="return iframeCallback(this, dialogAjaxDone);">
        <div class="pageFormContent nowrap" layoutH="96" style="height: 100px;">

            <dl>
                <dt>币种名称：</dt>
                <dd>
                    <input type="hidden" name="fid" value="${virtualCoinType.fid}"/>
                    <input type="text" name="fname" maxlength="30" class="required"
                           size="10" value="${virtualCoinType.fname}"/>
                </dd>
            </dl>
            <dl>
                <dt>首页次序：</dt>
                <dd>
                    <input type="text" name="homeOrder" maxlength="30" class="required number"
                           size="10" value="${virtualCoinType.homeOrder}"/>
                </dd>
            </dl>
            <dl>
                <dt>板块次序：</dt>
                <dd>
                    <input type="text" name="typeOrder" maxlength="30"
                           class="required number" size="10" value="${virtualCoinType.typeOrder}"/>
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

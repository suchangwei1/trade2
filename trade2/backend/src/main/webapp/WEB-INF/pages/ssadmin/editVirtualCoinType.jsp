<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" %>
<%@ include file="comm/include.inc.jsp" %>
<h2 class="contentTitle">修改虚拟币类型信息</h2>
<div class="pageContent">
    <form method="post" action="ssadmin/updateVirtualCoinType.html"
          class="pageForm required-validate" enctype="multipart/form-data"
          onsubmit="return iframeCallback(this, dialogAjaxDone);">
        <div class="pageFormContent nowrap" layoutH="96" style="height: 400px;">
            <dl>
                <dt>图标链接：</dt>
                <dd>
                    <input type="file" class="inputStyle" value="" name="filedata"
                           id="filedata"/>
                </dd>
            </dl>
            <dl>
                <dt>币种名称：</dt>
                <dd>
                    <input type="hidden" name="fid" value="${virtualCoinType.fid}"/>
                    <input type="text" name="fname" maxlength="30" class="required"
                           size="70" value="${virtualCoinType.fname}"/>
                </dd>
            </dl>
            <dl>
                <dt>币种简称：</dt>
                <dd>
                    <input type="text" name="fShortName" maxlength="30"
                           class="required" size="70" value="${virtualCoinType.fShortName}"/>
                </dd>
            </dl>
            <dl>
                <dt>ACCESS_KEY：</dt>
                <dd>
                    <input type="hidden" name="fid" value="${virtualCoinType.fid}"/>
                    <input type="text" name="faccess_key" maxlength="30"
                           class="required" size="70" value="${virtualCoinType.faccess_key}"/>
                </dd>
            </dl>
            <dl>
                <dt>SECRT_KEY：</dt>
                <dd>
                    <input type="text" name="fsecrt_key" maxlength="30"
                           class="required" size="70" value="${virtualCoinType.fsecrt_key}"/>
                </dd>
            </dl>
            <dl>
                <dt>IP地址：</dt>
                <dd>
                    <input type="text" name="fip" maxlength="30" class="required"
                           size="70" value="${virtualCoinType.fip}"/>
                </dd>
            </dl>
            <dl>
                <dt>端口号：</dt>
                <dd>
                    <input type="text" name="fport" maxlength="30"
                           class="required number" size="30" value="${virtualCoinType.fport}"/>
                </dd>
            </dl>
            <dl>
                <dt>充值确认次数：</dt>
                <dd>
                    <input type="text" name="confirmTimes" maxlength="30"
                           class="required" size="30" value="${virtualCoinType.confirmTimes}">
                </dd>
            </dl>
            <dl>
                <dt>是否可以提现：</dt>
                <dd>
                    <c:choose>
                        <c:when test="${virtualCoinType.FIsWithDraw}">
                            <input type="checkbox" name="FIsWithDraw" checked='1'/>
                        </c:when>
                        <c:otherwise>
                            <input type="checkbox" name="FIsWithDraw"/>
                        </c:otherwise>
                    </c:choose>
                </dd>
            </dl>
            <dl>
                <dt>是否可以充值：</dt>
                <dd>
                    <c:choose>
                        <c:when test="${virtualCoinType.FIsRecharge}">
                            <input type="checkbox" name="FIsRecharge" checked='1'/>
                        </c:when>
                        <c:otherwise>
                            <input type="checkbox" name="FIsRecharge"/>
                        </c:otherwise>
                    </c:choose>
                </dd>
            </dl>
            <dl>
                <dt>是否可以交易：</dt>
                <dd>
                    <c:choose>
                        <c:when test="${virtualCoinType.fisShare}">
                            <input type="checkbox" name="fisShare" checked='1'/>
                        </c:when>
                        <c:otherwise>
                            <input type="checkbox" name="fisShare"/>
                        </c:otherwise>
                    </c:choose>
                </dd>
            </dl>
            <dl>
                <dt>是否可以OTC：</dt>
                <dd>
                    <c:choose>
                        <c:when test="${virtualCoinType.otcActive}">
                            <input type="checkbox" name="isOtcActive" checked='1'/>
                        </c:when>
                        <c:otherwise>
                            <input type="checkbox" name="isOtcActive"/>
                        </c:otherwise>
                    </c:choose>
                </dd>
            </dl>
            <dl>
                <dt>OTC手续费：</dt>
                <dd>
                    <input type="text" name="otcRate" maxlength="30" class="required number" size="30" value="<fmt:formatNumber value="${virtualCoinType.otcRate}" groupingUsed="false"/>"> 如：0.002表示千分之二
                </dd>
            </dl>
            <dl>
                <dt>OTC买入价：</dt>
                <dd>
                    <input type="text" name="otcBuyPrice" maxlength="30" class=" number" size="30" value="<fmt:formatNumber value="${virtualCoinType.otcBuyPrice}" groupingUsed="false"/>">单位￥
                </dd>
            </dl>
            <dl>
                <dt>OTC卖出价：</dt>
                <dd>
                    <input type="text" name="otcSellPrice" maxlength="30" class=" number" size="30" value="<fmt:formatNumber value="${virtualCoinType.otcSellPrice}" groupingUsed="false"/>">单位￥
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

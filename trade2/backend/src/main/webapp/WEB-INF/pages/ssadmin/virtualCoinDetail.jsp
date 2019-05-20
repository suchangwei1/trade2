<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" %>
<%@ include file="comm/include.inc.jsp" %>
<h2 class="contentTitle">虚拟币类型简介</h2>
<div class="pageContent">
    <form method="post" action="/ssadmin/updateVirtualCoinDetail.html"
          class="pageForm required-validate"
          onsubmit="return iframeCallback(this, dialogAjaxDone);">
        <input type="hidden" name="fid", value="${detail.fid}">
        <input type="hidden" name="fviFid", value="${fVid}">
        <div class="pageFormContent nowrap" layoutH="97">
            <dl>
                <dt>英文名称：</dt>
                <dd>
                    <input type="text" name="ename" value="${detail.ename}" maxlength="30" class="required"/>
                </dd>
            </dl>
            <dl>
                <dt>中文名称：</dt>
                <dd>
                    <input type="text" name="name" value="${detail.name}" maxlength="35" class="required"/>
                </dd>
            </dl>
            <dl>
                <dt>研发者：</dt>
                <dd>
                    <input type="text" name="developers" value="${detail.developers}" maxlength="50" class=""/>
                </dd>
            </dl>
            <dl>
                <dt>货币数量：</dt>
                <dd>
                    <input type="text" name="amount" value="${detail.amount}" maxlength="50" class=""/>
                </dd>
            </dl>
            <dl>
                <dt>发布时间：</dt>
                <dd>
                    <input type="text" name="releaseTime" dateFmt="yyyy-MM-dd HH:mm:ss" value="<fmt:formatDate value="${detail.releaseTime}" pattern="yyyy-MM-dd HH:mm:ss"/>" class="date" readonly="true"/>
                </dd>
            </dl>
            <dl>
                <dt>区块时间：</dt>
                <dd>
                    <input type="text" name="blocksTime"  value="${detail.blocksTime}" class=""/>
                </dd>
            </dl>
            <dl>
                <dt>钱包地址：</dt>
                <dd>
                    <input type="text" name="walletLink" maxlength="255" value="${detail.walletLink}" class="" size="70"/>
                </dd>
            </dl>
            <dl>
                <dt>源码地址：</dt>
                <dd>
                    <input type="text" name="sourceLink" maxlength="255" value="${detail.sourceLink}" class="" size="70"/>
                </dd>
            </dl>
            <dl>
                <dt>白皮书下载：</dt>
                <dd>
                    <input type="text" name="whitePaperLink" maxlength="255" value="${detail.whitePaperLink}" class="" size="70"/>
                </dd>
            </dl>
            <dl>
                <dt>区块浏览器地址：</dt>
                <dd>
                    <input type="text" name="blockBrowserLink" maxlength="255" value="${detail.blockBrowserLink}" class="" size="70"/>
                </dd>
            </dl>
            <dl>
                <dt>描述：</dt>
                <dd>
                    <textarea name="desc" cols="70" rows="4" maxlength="255">${detail.desc}</textarea>
                </dd>
            </dl>
            <dl>
                <dt>核心算法：</dt>
                <dd>
                    <textarea name="algorithm" cols="70" rows="4" maxlength="255">${detail.algorithm}</textarea>
                </dd>
            </dl>
            <dl>
                <dt>区块奖励：</dt>
                <dd>
                    <textarea name="blocksReward" cols="70" rows="4" maxlength="255">${detail.blocksReward}</textarea>
                </dd>
            </dl>
            <dl>
                <dt>特色：</dt>
                <dd>
                    <textarea name="feature" cols="70" rows="4" maxlength="1024">${detail.feature}</textarea>
                </dd>
            </dl>
            <dl>
                <dt>不足之处：</dt>
                <dd>
                    <textarea name="defects" cols="70" rows="4" maxlength="1024">${detail.defects}</textarea>
                </dd>
            </dl>
            <dl>
                <dt>备注信息：</dt>
                <dd>
                    <textarea name="remark" cols="70" rows="4" maxlength="255">${detail.remark}</textarea>
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

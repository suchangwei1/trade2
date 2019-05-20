<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="comm/include.inc.jsp"%>
<c:choose>
    <c:when test="${'add' == param.act}">
        <h2 class="contentTitle">新增项目信息</h2>
    </c:when>
    <c:when test="${'edit' == param.act}">
        <h2 class="contentTitle">编辑项目信息</h2>
    </c:when>
</c:choose>


<div class="pageContent">
    <c:choose>
        <c:when test="${'add' == param.act}">
        <form method="post" action="ssadmin/addProject.html"
              class="pageForm required-validate" enctype="multipart/form-data"
              onsubmit="return iframeCallback(this, dialogAjaxDone);">
        </c:when>
        <c:when test="${'edit' == param.act}">
            <form method="post" action="ssadmin/updateProject.html"
                  class="pageForm required-validate" enctype="multipart/form-data"
                  onsubmit="return iframeCallback(this, dialogAjaxDone);">
        </c:when>
    </c:choose>
        <div class="pageFormContent nowrap" layoutH="97">
            <input type="hidden" name="id" value="${pro.id}">
            <input type="hidden" name="classId" value="15">
            <input type="hidden" name="articleId" value="${pro.articleId}">
            <dl>
                <dt>项目名称：</dt>
                <dd>
                    <input type="text" name="projectName"  class="required"
                           size="50"  value="${pro.projectName }"/>
                </dd>
            </dl>
            <dl>
                <dt>用户ID：</dt>
                <dd>
                    <input type="text" name="authorId"  class="digits required"
                           size="50"  value="${pro.authorId }"/>
                </dd>
            </dl>
            <dl>
                <dt>关键字：</dt>
                <dd class="keyworddd">
                    <c:forEach items="${keywordList}" var="keyword">
                        ${keyword.keyName}<input type="checkbox" name="keywords" <c:if test="${keywordMap[keyword.id + 0]}">checked</c:if> value="${keyword.id}">&nbsp;&nbsp;
                    </c:forEach>
                </dd>
            </dl>
            <dl>
                <dt>图片地址：</dt>
                <input type="text" class="inputStyle" placeholder="图片地址" size="50" name="imgPath" id="imgPath" value="${pro.imgPath}" />
            </dl>
            <dl>
                <dt>上传图片：</dt>
                <dd>
                    <input type="file" name="imgFile">
                </dd>
            </dl>
            <dl>
                <dt>是否推荐：</dt>
                <dd>
                    <input type="checkbox" name="projectType" value="1" <c:if test="${1 == pro.projectType}">checked</c:if> />
                </dd>
            </dl>
            <dl>
                <dt>项目创建时间：</dt>
                <dd>
                    <input type="text" name="createdTime" class="date required"
                           readonly="true" dateFmt="yyyy-MM-dd" size="17" value="<fmt:formatDate value="${pro.createdTime}" pattern="yyyy-MM-dd" />" /> <a
                        class="inputDateButton" href="javascript:;">选择</a>
                </dd>
            </dl>
            <dl>
                <dt>公司名称：</dt>
                <dd>
                    <input type="text" name="companyName"  class=""
                           size="50"  value="${pro.companyName }"/>
                </dd>
            </dl>
            <dl>
                <dt>公司地址：</dt>
                <dd>
                    <input type="text" name="companyLocation"  class=""
                           size="50"  value="${pro.companyLocation}"/>(公司所在城市)
                </dd>
            </dl>
            <dl>
                <dt>CEO：</dt>
                <dd>
                    <input type="text" name="ceo"
                           class="" size="20"  value="${pro.ceo }"/>
                </dd>
            </dl>
            <dl>
                <dt>CEO头像地址：</dt>
                <dd>
                    <input type="text" name="headImgPath" placeholder="图片地址"
                           class="" size="20"  value="${pro.headImgPath }"/>
                </dd>
            </dl>
            <dl>
                <dt>CEO头像上传：</dt>
                <dd>
                    <input type="file" name="headImgFile"
                           class="" size="20" />
                </dd>
            </dl>
            <dl>
                <dt>团队人数：</dt>
                <dd>
                    <input type="text" name="teamSize"  class="digits"
                           size="20"  value="${pro.teamSize }"/>
                </dd>
            </dl>
            <dl>
                <dt>公司成立时间：</dt>
                <dd>
                    <input type="text" name="companyCreatedTime" class="date"
                           readonly="true" dateFmt="yyyy-MM-dd" size="17" value="<fmt:formatDate value="${pro.companyCreatedTime}" pattern="yyyy-MM-dd" />" /> <a
                        class="inputDateButton" href="javascript:;">选择</a>
                </dd>
            </dl>
            <dl>
                <dt>融资轮次：</dt>
                <dd>
                    <select name="financingRound">
                        <option value="天使轮" <c:if test="${pro.financingRound == '天使轮'}">selected</c:if>>天使轮</option>
                        <option value="A轮" <c:if test="${pro.financingRound == 'A轮'}">selected</c:if>>A轮</option>
                        <option value="B轮" <c:if test="${pro.financingRound == 'B轮'}">selected</c:if>>B轮</option>
                        <option value="C轮" <c:if test="${pro.financingRound == 'C轮'}">selected</c:if>>C轮</option>
                        <option value="D轮及以上" <c:if test="${pro.financingRound == 'D轮及以上'}">selected</c:if>>D轮及以上</option>
                        <option value="ICO" <c:if test="${pro.financingRound == 'ICO'}">selected</c:if>>ICO</option>
                    </select>
                </dd>
            </dl>
            <%--<dl>--%>
                <%--<dt>融资轮次：</dt>--%>
                <%--<dd>--%>
                    <%--<input type="text" name="financingRound"--%>
                           <%--class="" size="20"  value="${pro.financingRound }"/>--%>
                <%--</dd>--%>
            <%--</dl>--%>
            <dl>
                <dt>CEO简介：</dt>
                <dd>
                    <textarea name="ceoIntroduction" style="width: 500px; height: 100px">${pro.ceointroduction}</textarea>
                </dd>
            </dl>
            <dl>
                <dt>项目简介：</dt>
                <dd>
					<textarea class="editor" name="content" rows="20" cols="100" maxlength="62500"
                              tools="mfull" upImgUrl="ssadmin/upload.html"
                              upImgExt="jpg,jpeg,gif,png">${pro.content}
                    </textarea>
                </dd>
            </dl>
            <dl>
                <dt>项目介绍：</dt>
                <dd>
					<textarea class="editor" name="introduction" rows="20" cols="100" maxlength="62500"
                              tools="mfull" upImgUrl="/ssadmin/upload.html"
                              upImgExt="jpg,jpeg,gif,png">${pro.introduction}
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
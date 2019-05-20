<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="comm/include.inc.jsp"%>
<h2 class="contentTitle">修改文章信息</h2>
<div class="pageContent">

	<form method="post" action="/ssadmin/updateInformationArticle.html"
		class="pageForm required-validate" enctype="multipart/form-data"
		onsubmit="return iframeCallback(this, dialogAjaxDone);">
		<div class="pageFormContent nowrap" layoutH="100">
			<dl>
				<dt>文章标题：</dt>
				<dd>
				    <input type="hidden" name="id" value="${informationArticle.id}"/>
					<input type="text" name="title" maxlength="50"
						class="required" size="70" value="${informationArticle.title}"/>
				</dd>
			</dl>
			<dl>
				<dt>文章类型：</dt>
				<dd>
					<c:forEach items="${typeNameMap}" var="entry">
						<span style="margin-right: 20px; white-space: nowrap"><input type="radio" class="classId" name="informationArticleLookup.id" value="${entry.key}" onchange="getkeywordList(${entry.key})" <c:if test="${informationArticle.classId == entry.key}">checked</c:if>>${entry.value}</span>
					</c:forEach>
				</dd>
			</dl>
			<%--<dl>--%>
				<%--<dt>类型：</dt>--%>
				<%--<dd>--%>
					<%--<input type="hidden" class="classId" name="informationArticleLookup.id" value="${informationArticle.classId}"/>--%>
				    <%--<input type="text" class="required" name="informationArticleLookup.informationArticleType" value="${informationArticle.className}"--%>
						   <%--suggestFields="id,informationArticleType"--%>
						   <%--suggestUrl="/ssadmin/informationArticleTypeLookup.html"--%>
						   <%--lookupGroup="orgLookup" readonly="readonly"/>--%>
				    <%--<a class="btnLook" href="/ssadmin/informationArticleTypeLookup.html" lookupGroup="informationArticleLookup">查找带回</a>--%>
				<%--</dd>--%>
			<%--</dl>--%>
			<dl>
				<dt>关键字：</dt>
				<input type="hidden" class="keywordClassId" value="">
				<dd class="keyworddd">
					<c:forEach items="${keywordInfoList}" var="keywordInfo">
						<span style="margin-right: 20px; white-space: nowrap"><input type="checkbox" name="keywords" value="${keywordInfo.id}" <c:if test="${keywordInfo.checked}">checked</c:if>>${keywordInfo.keyName}</span>
					</c:forEach>
				</dd>
			</dl>
			<script>
				function getkeywordList(id) {
					if(typeof(id) == "undefined"){
						return false;
					}
					var url = "/ssadmin/keywordListJson.html?classId=";
//					var classId = $(".classId").val();
//					var keywordClassId = $(".keywordClassId").val();
//					if(classId != "" && classId != keywordClassId){
//						$(".keywordClassId").val(classId);
//						$(".keyworddd").html("");
//						$.getJSON(url + classId, function (data) {
//							$.each(data, function (e,value) {
//								var htmlStr = "<span style='margin-right: 20px; white-space: nowrap'><input type='checkbox' name='keywords' value="+value.id+">"+value.keyName+"</span>";
//								$(".keyworddd").append(htmlStr);
//							});
//						});
//
//					}
					$.get(url + id, function (data) {
						var htmlStr = "";
						$.each(data, function (e,value) {
							htmlStr += "<span style='margin-right: 20px; white-space: nowrap'><input type='checkbox' name='keywords' value="+value.id+">"+value.keyName+"</span>";
						});
						$(".keyworddd").html(htmlStr);
					}, "json");
				}
			</script>
			<dl>
				<dt>图片地址：</dt>
				<dd>
					<input type="text" name="imgPath" value="${informationArticle.imgPath}">
				</dd>
			</dl>
			<dl>
				<dt>上传图片：</dt>
				<dd>
					<input type="file" name="imgFile">
				</dd>
			</dl>
			<%--<dl>--%>
				<%--<dt>布局</dt>--%>
				<%--<dd>--%>
					<%--<c:forEach items="${frameInfoList}" var="frameInfo">--%>
						<%--<span style="white-space: nowrap; margin-right: 20px;"><input type="checkbox" name="frames" <c:if test="${frameInfo.checked}">checked</c:if> value="${frameInfo.id}"/>${frameInfo.frameName}</span>--%>
					<%--</c:forEach>--%>
				<%--</dd>--%>
			<%--</dl>--%>
			<%--<dl>--%>
				<%--<dt>：</dt>--%>
				<%--<dd>--%>
					<%--<input type="text" name="rank" value="${layoutInfo.rank}">--%>
				<%--</dd>--%>
			<%--</dl>--%>
			<dl>
				<dt>来源：</dt>
				<dd>
					<input maxlength="200" class="editor" name="sources" value= ${informationArticle.sources}>
				</dd>
			</dl>
			<dl>
				<dt>来源URL：</dt>
				<dd>
					<input maxlength="200" class="editor" name="originalText" value= ${informationArticle.originalText}>
				</dd>
			</dl>
			<dl>
				<dt>阅读量：</dt>
				<dd>
					<input class="editor" name="readingNum" value= ${informationArticle.readingNum}>
				</dd>
			</dl>
			<dl>
				<dt>作者：</dt>
				<dd>
					<input class="editor" name="authorName" value= ${informationArticle.authorName}>
				</dd>
			</dl>
			<dl>
				<dt>文章内容：</dt>
				<dd>
					<textarea class="editor" name="content" rows="40" cols="135"
						tools="mfull" upImgUrl="/ssadmin/upload.html"
						upImgExt="jpg,jpeg,gif,png">
						${informationArticle.content}
				</textarea>
			</dl>
		</div>
		<div class="formBar">
			<ul>
				<li><div class="buttonActive">
						<div class="buttonContent">
							<button type="submit">保存</button>
						</div>
					</div>
				</li>
				<li><div class="button">
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
function customvalidXxx(element){
	if ($(element).val() == "xxx") return false;
	return true;
}
</script>


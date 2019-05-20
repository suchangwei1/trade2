<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="comm/include.inc.jsp"%>
<h2 class="contentTitle">添加文章信息</h2>
<div class="pageContent">

	<form method="post" action="/ssadmin/saveInformationArticle.html"
		class="pageForm required-validate"
		onsubmit="return iframeCallback(this, dialogAjaxDone);" enctype="multipart/form-data">
		<div class="pageFormContent nowrap" layoutH="97">
			<dl>
				<dt>文章标题：</dt>
				<dd>
					<input type="text" name="title" maxlength="50"
						class="required" size="70" />
				</dd>
			</dl>

			<dl>
				<dt>文章类型：</dt>
				<dd>
					<c:forEach items="${typeNameMap}" var="entry">
						<span style="margin-right: 20px; white-space: nowrap"><input type="radio" class="classId" name="classId" value="${entry.key}" onchange="getkeywordList(${entry.key})">${entry.value}</span>
					</c:forEach>
				</dd>
			</dl>
			<%--<dl>--%>
				<%--<dt>类型：</dt>--%>
				<%--<dd>--%>
					<%--<input type="hidden" class="classId" name="informationArticleLookup.id" value=""/>--%>
				    <%--<input type="text" class="required" name="informationArticleLookup.informationArticleType" value=""--%>
						   <%--suggestFields="id,informationArticleType"--%>
						   <%--suggestUrl="/ssadmin/informationArticleTypeLookup.html"--%>
						   <%--lookupGroup="orgLookup" readonly="readonly" size="70"/>--%>
				    <%--<a class="btnLook" href="/ssadmin/informationArticleTypeLookup.html" lookupGroup="informationArticleLookup">查找带回</a>--%>
				<%--</dd>--%>
			<%--</dl>--%>
			<dl>
				<dt>关键字：</dt>
				<input type="hidden" class="keywordClassId" value="">
				<dd class="keyworddd"></dd>
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
				<input type="text" class="inputStyle" placeholder="图片地址" name="imgPath" id="imgPath" />
			</dl>
			<dl>
				<dt>上传图片：</dt>
				<dd>
					<input type="file" name="imgFile">
				</dd>
			</dl>
			<%--<dl>--%>
				<%--<dt>布局:</dt>--%>
				<%--<dd>--%>
					<%--<c:forEach items="${frameInfoList}" var="frameInfo">--%>
						<%--<span style="white-space: nowrap; margin-right: 20px;"><input type="checkbox" name="frames" value="${frameInfo.id}">${frameInfo.frameName}</span>--%>
					<%--</c:forEach>--%>
				<%--</dd>--%>
			<%--</dl>--%>
			<dl>
				<dt>来源：</dt>
				<dd>
					<input maxlength="220" type="text" class="inputStyle" value="" name="sources"/>
				</dd>
			</dl>
			<dl>
				<dt>来源URL：</dt>
				<dd>
					<input type="text" class="inputStyle" value="" name="originalText"/>
				</dd>
			</dl>
			<dl>
				<dt>作者：</dt>
				<dd>
					<input type="text" class="inputStyle" value="" name="authorName"/>
				</dd>
			</dl>
			<dl>
				<dt>阅读量：</dt>
				<dd>
					<input type="text" class="inputStyle" value="" name="readingNum"/>
				</dd>
			</dl>
			<dl>
				<dt>是否推送：</dt>
				<dd>
					<input type="checkbox" class="inputStyle" value="1" name="isPush" onclick="changePushContentRequired();"/>
				</dd>
			</dl>
			<dl>
				<dt>推送摘要：</dt>
				<dd>
					<input type="text" name="pushContent" maxlength="50"
						   size="70" />
				</dd>
			</dl>
			<dl>
				<dt>文章内容：</dt>
				<dd>
					<textarea class="editor" name="content" rows="40" cols="135"
						tools="mfull" upImgUrl="/ssadmin/upload.html"
						upImgExt="jpg,jpeg,gif,png">
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
function changePushContentRequired(){
	if($("input[name=isPush]").attr("checked") == "checked"){
		$("input[name=pushContent]").addClass("required");
	}else{
		$("input[name=pushContent]").removeClass("required");
	}
}
</script>

<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="comm/include.inc.jsp"%>
<h2 class="contentTitle">审核并发布文章</h2>
<div class="pageContent">
	<form method="post" action="/ssadmin/adoptContribution.html"
		  class="pageForm required-validate"
		  onsubmit="return iframeCallback(this, dialogAjaxDone);" enctype="multipart/form-data">
		<div class="pageFormContent nowrap" layoutH="97">
			<input type="hidden" name="contributionId" value="${contribution.id}">
			<input type="hidden" name="authorId" value="${empty contribution.userId ? "" : contribution.userId}">
			<dl>
				<dt>文章标题：</dt>
				<dd>
					<input type="text" name="title" maxlength="50"
						   class="required" size="70" value="${contribution.title}"/>
					<a style="color: blue" href="${cdn}/${contribution.path}" title="点击下载">点击下载投稿文件</a>
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
			<dl>
				<dt>来源：</dt>
				<dd>
					<input maxlength="220" type="text" class="inputStyle" value="投稿" name="sources"/>
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
					<input type="text" class="inputStyle" value="${contribution.nickName}" name="authorName"/>
				</dd>
			</dl>
			<dl>
				<dt>阅读量：</dt>
				<dd>
					<input type="text" class="inputStyle" value="0" name="readingNum"/>
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
</script>

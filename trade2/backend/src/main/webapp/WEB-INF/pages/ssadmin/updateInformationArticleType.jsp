<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="comm/include.inc.jsp"%>
<h2 class="contentTitle">修改文章信息</h2>
<div class="pageContent">

	<form method="post" action="/ssadmin/updateInformationArticleType.html"
		class="pageForm required-validate" enctype="multipart/form-data"
		onsubmit="return iframeCallback(this, dialogAjaxDone);">
		<div class="pageFormContent nowrap" layoutH="97">
			<dl>
				<dt>类型标题：</dt>
				<dd>
				    <input type="hidden" name="id" value="${informationArticleType.id}"/>
					<input type="text" name="title" maxlength="50"
						class="required" size="70" value="${informationArticleType.title}"/>
				</dd>
			</dl>
			<dl>
				<dt>文章ID：</dt>
				<dd>
				    <input type="text" name="headlineId" value="${informationArticleType.headlineId}">
					<button onclick="javascript:;">查询</button>
				</dd>
			</dl>

			<dl>
				<dt>文章标题：</dt>
				<dd>
					<input type="text" name="headlineTitle" value="${informationArticleType.headlineTitle}">
				</dd>
			</dl>

			<dl>
				<dt>图片URL：</dt>
				<dd>
					<input type="text" name="headlineTitle" value="${informationArticleType.imgPath}">
				</dd>
			</dl>

			<dl>
				<dt>本地上传：</dt>
				<dd>
					<input type="file" name="imgFile">
				</dd>
			</dl>

			<dl>
				<dt>文章内容：</dt>
				<dd>
					<textarea class="editor" name="content" rows="20" cols="105"
						tools="simple" upImgUrl="/ssadmin/upload.html"
						upImgExt="jpg,jpeg,gif,png">
						${informationArticleType.content}
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
	function lookupArticle() {
		var id = $("input[name='headlineId']").val();
		var url = "/ssadmin/lookupInformationArticleJson.html";
		var param = {"id":id};
		$.getJSON(url, param, function (data) {
			$("input[name='headlineTitle']").val(data.headlineTitle);
			$("input[name='imgPath']").val(data.imgPath);
			$("input[name='content']").val(data.content);
		});
	}
function customvalidXxx(element){
	if ($(element).val() == "xxx") return false;
	return true;
}
</script>


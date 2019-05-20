<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="comm/include.inc.jsp"%>
<h2 class="contentTitle">添加文章</h2>
<div class="pageContent">
	<form method="post" action="ssadmin/saveArticle.html"
		class="pageForm required-validate" enctype="multipart/form-data"
		onsubmit="return iframeCallback(this, dialogAjaxDone);">
		<div class="pageFormContent nowrap" layoutH="97">
			<dl>
				<dt>中文标题：</dt>
				<dd>
					<input type="text" name="ftitle" maxlength="50"
						class="required" size="70" />
				</dd>
			</dl>
			<dl>
				<dt>英文标题：</dt>
				<dd>
					<input type="text" name="en_title" maxlength="50"
						class="required" size="70" />
				</dd>
			</dl>
			<dl>
				<dt>类型：</dt>
				<dd>
					<input type="hidden" name="articleLookup.id" value="${articleLookup.id}"/>
				    <input type="text" class="required" name="articleLookup.articleType" value="" suggestFields="id,articleType"
				     suggestUrl="ssadmin/articleTypeLookup.html" lookupGroup="orgLookup" readonly="readonly" size="70"/>
				    <a class="btnLook" href="ssadmin/articleTypeLookup.html" lookupGroup="articleLookup">查找带回</a>	
				</dd>
			</dl>
			<dl>
				<dt>是否置顶：</dt>
				<dd>
					<input type="checkbox" class="inputStyle" value="1" name="isTop" />
				</dd>
			</dl>
			<dl>
				<dt>中文内容：</dt>
				<dd>
					<textarea class="editor" name="fcontent" rows="20" cols="105"
						tools="simple" upImgUrl="ssadmin/upload.html"
						upImgExt="jpg,jpeg,gif,png">
				</textarea>
			</dl>
			<dl>
				<dt>英文内容：</dt>
				<dd>
					<textarea class="editor" name="en_content" rows="20" cols="105"
						tools="simple" upImgUrl="ssadmin/upload.html"
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

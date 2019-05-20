<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="comm/include.inc.jsp"%>
<h2 class="contentTitle">添加路演图片</h2>
<div class="pageContent">

	<form method="post" action="/ssadmin/addRoadShowPictures.html" enctype="multipart/form-data"
		  class="pageForm required-validate"
		  onsubmit="return iframeCallback(this, dialogAjaxDone);">
		<div>
			为${roadshow.title}路演添加图片
		</div>
		<div class="pageFormContent nowrap" layoutH="97">
			<input type="hidden" value="${roadshow.id}" name="roadShowId">
			添加图片：<input type="file" multiple="multiple" name="files">
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

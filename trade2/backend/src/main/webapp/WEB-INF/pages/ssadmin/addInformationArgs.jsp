<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="comm/include.inc.jsp"%>
<h2 class="contentTitle">添加资讯平台参数</h2>
<div class="pageContent">

	<form method="post" action="/ssadmin/saveInformationArgs.html"
		class="pageForm required-validate"
		onsubmit="return iframeCallback(this, dialogAjaxDone);" enctype="multipart/form-data">
		<div class="pageFormContent nowrap" layoutH="97">
			<dl>
				<dt>KEY：</dt>
				<dd>
					<input type="text" name="key" maxlength="50"
						class="required" size="70" />
				</dd>
			</dl>
			<dl>
				<dt>VALUE：</dt>
				<dd>
					<textarea style="width: 445px; height: 115px" name="value" maxlength="512"></textarea>
				</dd>
			</dl>
			<dl>
				<dt>上传图片：</dt>
				<dd>
					<input type="file" name="file">
				</dd>
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

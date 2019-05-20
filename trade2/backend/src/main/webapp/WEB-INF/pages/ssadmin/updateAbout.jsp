<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="comm/include.inc.jsp"%>
<h2 class="contentTitle">修改关于信息</h2>


<div class="pageContent">

	<form method="post" action="ssadmin/updateAbout.html"
		class="pageForm required-validate"
		onsubmit="return validateCallback(this,dialogAjaxDone)">
		<div class="pageFormContent nowrap" layoutH="97">
			<dl>
				<dt>标题：</dt>
				<dd>
					<input type="hidden" name="fid" value="${fabout.fid }" /> <input
						type="text" name="ftitle" maxlength="50" class="required"
						size="70" value="${fabout.ftitle }" />
				</dd>
			</dl>
			<dl>
				<dt>内容：</dt>
				<dd>
					<textarea class="editor" name="fcontent" rows="20" cols="100"  upImgUrl="ssadmin/upload.html"
						upImgExt="jpg,jpeg,gif,png">
						${fabout.fcontent }
				</textarea>
				</dd>
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
function customvalidXxx(element){
	if ($(element).val() == "xxx") return false;
	return true;
}
</script>

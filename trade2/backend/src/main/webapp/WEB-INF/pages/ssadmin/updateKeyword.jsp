<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="comm/include.inc.jsp"%>
<h2 class="contentTitle">修改关键字</h2>
<div class="pageContent">

	<form method="post" action="/ssadmin/updateKeyword.html"
		class="pageForm required-validate" enctype="multipart/form-data"
		onsubmit="return iframeCallback(this, dialogAjaxDone);">
		<div class="pageFormContent nowrap" layoutH="97">
			<dl>
				<dt>关键字标题：</dt>
				<dd>
				    <input type="hidden" name="id" value="${keywordInfo.id}"/>
					<input type="text" name="keyName" maxlength="50"
						class="required" size="70" value="${keywordInfo.keyName}"/>
				</dd>
			</dl>
			<dl>
				<dt>类型：</dt>
				<dd>
					<input type="hidden" name="classId" value="${keywordInfo.classId}"/>
				    <input type="text" class="required" name="className" value="${keywordInfo.className}"
						   readonly="readonly"/>
				    <a class="btnLook" href="/ssadmin/informationArticleTypeLookup.html" lookupGroup="">查找带回</a>
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

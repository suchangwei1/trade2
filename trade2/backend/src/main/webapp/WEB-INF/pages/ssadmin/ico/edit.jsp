<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="../comm/include.inc.jsp"%>
<h2 class="contentTitle">编辑ICO信息</h2>


<div class="pageContent">

	<form method="post" action="/ssadmin/saveICO.html"
		class="pageForm required-validate" enctype="multipart/form-data"
		onsubmit="return iframeCallback(this,dialogAjaxDone)">
		<div class="pageFormContent nowrap" layoutH="97">
			<input type="hidden" name="id" value="${ico.id}">
			<dl>
				<dt>ICO名称：</dt>
				<dd>
					<input type="text" name="name" value="${ico.name}" maxlength="100" class="required" size="50" />
				</dd>
			</dl>
			<dl>
				<dt>ICO总份额：</dt>
				<dd>
					<input name="amount" class="number" value="<fmt:formatNumber value="${ico.amount}" pattern="#.####"/>" type="text" maxlength="20" class="required" size="30" />
				</dd>
			</dl>
			<dl>
				<dt>填充份额：</dt>
				<dd>
					<input name="supplyAmount" class="number" value="<fmt:formatNumber value="${ico.supplyAmount}" pattern="#.####"/>" type="text" maxlength="20" size="30" />若项目到期未满额，添加此参数达到100%进度
				</dd>
			</dl>
			<dl>
				<dt>已认购份额：</dt>
				<dd>
					<input name="rightAmount" class="number" value="<fmt:formatNumber value="${ico.rightAmount}" pattern="#.####"/>" type="text" maxlength="20" size="30" />
				</dd>
			</dl>
			<dl>
				<dt>限购份额(0不限制)：</dt>
				<dd>
					<input name="limitAmount" class="number" value="<fmt:formatNumber value="${ico.limitAmount}" pattern="#.####"/>" type="text" maxlength="20" size="30" />
				</dd>
			</dl>
			<dl>
				<dt>最小购买份额(0不限制)：</dt>
				<dd>
					<input name="minBuyAmount" class="number" value="<fmt:formatNumber value="${ico.minBuyAmount}" pattern="#.####"/>" type="text" maxlength="20" size="30" />
				</dd>
			</dl>
			<dl>
				<dt>单位回报比例：</dt>
				<dd>
					<input name="requiteRate" class="number" value="<fmt:formatNumber value="${ico.requiteRate}" pattern="#.####"/>" type="text" maxlength="20" size="30" />例如：1份额=0.01CTC
				</dd>
			</dl>
			<dl>
				<dt>支持次数：</dt>
				<dd>
					<input class="digits" name="supportCount" value="${ico.supportCount}" type="text" maxlength="20" size="30" />
				</dd>
			</dl>
			<dl>
				<dt>状态：</dt>
				<dd>
					<select name="status">
						<option <c:if test="${0 == ico.status.index}">selected</c:if> value="0">未完成</option>
						<option <c:if test="${1 == ico.status.index}">selected</c:if> value="1">已完成</option>
						<option <c:if test="${2 == ico.status.index}">selected</c:if> value="2">已失败</option>
					</select>
				</dd>
			</dl>
			<dl>
				<dt>开启时间：</dt>
				<dd>
					<input name="startTime" class="date required" readonly value="<fmt:formatDate value="${ico.startTime}" pattern="yyyy-MM-dd HH:mm:ss"/>" datefmt="yyyy-MM-dd HH:mm:ss" type="text" size="20" />
				</dd>
			</dl>
			<dl>
				<dt>结束时间：</dt>
				<dd>
					<input name="endTime" class="date required" readonly value="<fmt:formatDate value="${ico.endTime}" pattern="yyyy-MM-dd HH:mm:ss"/>" datefmt="yyyy-MM-dd HH:mm:ss" type="text" size="20" />
				</dd>
			</dl>
			<dl>
				<dt>图片地址：</dt>
				<input type="text" class="inputStyle" placeholder="图片地址" size="50" id="imgPath" value="${ico.imageUrl}" />
			</dl>
			<dl>
				<dt>上传图片：</dt>
				<dd>
					<input type="file" name="image">
				</dd>
			</dl>
			<dl>
				<dt>项目说明：</dt>
				<dd>
					<textarea class="editor required" name="declaration" tools="Cut,Copy,Paste,Pastetext,Fullscreen" rows="10" cols="120" maxlength="256">${ico.declaration}
					</textarea>
				</dd>
			</dl>
			<dl>
				<dt>项目介绍：</dt>
				<dd>
					<textarea class="editor required" name="description" rows="20" cols="120" maxlength="62500"
							  tools="mfull" upImgUrl="/ssadmin/upload.html"
							  upImgExt="jpg,jpeg,gif,png">${ico.description}
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

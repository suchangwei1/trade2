<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="comm/include.inc.jsp"%>
<h2 class="contentTitle">
	会员<font color="red">${fuser.fnickName}</font>证件照片查看
</h2>
<div class="pageContent">
	<div class="pageFormContent nowrap" layoutH="97">
		<dl>
			<dt>扫描件正面：</dt>
			<dd>
				<img src="${cdn}/${fuser.fIdentityPath }" width="500" />
			</dd>
		</dl>
		<dl>
			<dt>扫描件反面：</dt>
			<dd>
				<img src="${cdn}/${fuser.fIdentityPath2 }" width="500" />
			</dd>
		</dl>
		<dl>
			<dt>手持证件照片：</dt>
			<dd>
				<img src="${cdn}/${fuser.fIdentityPath3 }" width="500" />
			</dd>
		</dl>
	</div>
</div>

<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="comm/include.inc.jsp"%>
<h2 class="contentTitle">添加商品信息</h2>


<div class="pageContent">
	<form method="post" action="ssadmin/saveGoods.html"
		class="pageForm required-validate" enctype="multipart/form-data"
		onsubmit="return iframeCallback(this, dialogAjaxDone);">
		<div class="pageFormContent nowrap" layoutH="97">
			<dl>
				<dt>商品名称：</dt>
				<dd>
					<input type="text" name="fname" maxlength="20" class="required"
						size="70" />
				</dd>
			</dl>
			<dl>
				<dt>单价：</dt>
				<dd>
					<input type="text" name="fprice" maxlength="20" class="required number"
						size="20" />
				</dd>
			</dl>
 			<dl>
				<dt>每人限购数量：</dt>
				<dd>
					<input type="text" name="fcanBuyQty" maxlength="20" class="required number"
						size="20" />
					<span>0代表不限购</span>	
				</dd>
			</dl>
			<dl>
				<dt>市场价：</dt>
				<dd>
					<input type="text" name="fmarketPrice" maxlength="20" class="required number"
						size="20" />
				</dd>
			</dl>
			<dl>
				<dt>分类：</dt>
				<dd>
					<select type="combox" name="ftype">
					<c:forEach items="${fgoodtypes}" var="t">
						<option value="${t.fid}">${t.fname}</option>
					</c:forEach>
		            </select>
				</dd>
			</dl>
			<dl>
				<dt>总库存数量：</dt>
				<dd>
					<input type="text" name="ftotalQty" maxlength="20" class="required digits"
						size="20" />
				</dd>
			</dl>
			<dl>
				<dt>剩余数量：</dt>
				<dd>
					<input type="text" name="flastQty" maxlength="20" class="required digits"
						size="20" />
				</dd>
			</dl>
			<dl>
				<dt>商家号：</dt>
				<dd>
					<input type="text" name="fsupplierNo" maxlength="20"
						class="required" size="40" />
				</dd>
			</dl>
			<dl>
				<dt>QQ号：</dt>
				<dd>
					<input type="text" name="fqq" maxlength="20"
						class="required" size="40" />
				</dd>
			</dl>
			<dl>
				<dt>付款类型：</dt>
				<dd>
					<select type="combox" name="fcanpaytype">
					<c:forEach items="${payMap}" var="link">
						<option value="${link.key}">${link.value}</option>
					</c:forEach>
		            </select>
				</dd>
			</dl>
			<dl>
				<dt>图片链接：</dt>
				<dd>
					<input type="file" class="inputStyle" value="" name="filedata"
						id="filedata" />
				</dd>
			</dl>
			<dl>
				<dt>商品描述：</dt>
				<dd>
					<textarea class="editor" name="fdescription" rows="20" cols="105"
						tools="simple" upImgUrl="ssadmin/upload.html"
						upImgExt="jpg,jpeg,gif,png">
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
	function customvalidXxx(element) {
		if ($(element).val() == "xxx")
			return false;
		return true;
	}
</script>

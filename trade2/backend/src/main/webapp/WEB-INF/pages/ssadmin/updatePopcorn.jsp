<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="comm/include.inc.jsp"%>
<h2 class="contentTitle">修改游戏信息</h2>


<div class="pageContent">

	<form method="post" action="ssadmin/updatePopcorn.html"
		class="pageForm required-validate"
		onsubmit="return validateCallback(this,dialogAjaxDone)">
		<div class="pageFormContent nowrap" layoutH="97">
			<dl>
				<dt>简介：</dt>
				<dd>
					<input type="hidden" name="fid" value="${fpopcorn.fid }" /> 
					<textarea class="required" name="ftitle" rows="4" cols="70">${fpopcorn.ftitle }</textarea>
				</dd>
			</dl>
			<dl>
				<dt>开奖时间：</dt>
				<dd>
					<input type="text" name="ftime" class="required number"
						value="${fpopcorn.ftime }" /><span>分</span>
				</dd>
			</dl>
			<dl>
				<dt>开奖间隔时间：</dt>
				<dd>
					<input type="text" name="fspantime"class="required number"
					 value="${fpopcorn.fspantime }" /><span>分</span>
				</dd>
			</dl>
			<dl>
				<dt>手续费比例：</dt>
				<dd>
					<input type="text" name="frate" class="required number"
						value="${fpopcorn.frate }" /><span>%</span>
				</dd>
			</dl>
			<dl>
				<dt>公司下注比例：</dt>
				<dd>
					<input type="text" name="fbetRate" class="required number"
						value="${fpopcorn.fbetRate }" /><span>%</span>
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

<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="comm/include.inc.jsp"%>
<h2 class="contentTitle">添加认购信息</h2>

<div class="pageContent">

	<form method="post" action="ssadmin/saveSubscription1.html"
		class="pageForm required-validate"
		onsubmit="return validateCallback(this,dialogAjaxDone)">
		<div class="pageFormContent nowrap" layoutH="97">
			<dl>
				<dt>认购虚拟币名称：</dt>
				<dd>
					<select type="combox" name="vid" class="required">
						<c:forEach items="${allType}" var="type">
							<option value="${type.fid}">${type.fname}</option>
						</c:forEach>
					</select>
				</dd>
			</dl>
			<dl>
				<dt>简介：</dt>
				<dd>
					<textarea class="required" name="ftitle" rows="4" cols="70"></textarea>
				</dd>
			</dl>
			<dl>
				<dt>是否开放认购：</dt>
				<dd>
					<input type="checkbox" name="fisopen" />
				</dd>
			</dl>
			<dl>
				<dt>认购总数量：</dt>
				<dd>
					<input type="text" name="ftotal" class="required number" />
				</dd>
			</dl>
			<dl>
				<dt>认购价格：</dt>
				<dd>
					<input type="text" name="fprice" class="required number" />
				</dd>
			</dl>
			<dl>
				<dt>每人最大认购数量：</dt>
				<dd>
					<input type="text" name="fbuyCount" class="required digits" /><span>0为无限</span>
				</dd>
			</dl>
			<dl>
				<dt>每人最多认购次数：</dt>
				<dd>
					<input type="text" name="fbuyTimes" class="required digits" /><span>0为无限</span>
				</dd>
			</dl>
			<dl>
				<dt>冻结天数：</dt>
				<dd>
					<input type="text" name="fdays" class="required digits" /><span>0为不冻结</span>
				</dd>
			</dl>
			<dl>
				<dt>开始时间：</dt>
				<dd>
					<input type="text" name="fbeginTime" class="required date"
						readonly="true" dateFmt="yyyy-MM-dd HH:mm:ss" size="40" /> <a
						class="inputDateButton" href="javascript:;">选择</a>
				</dd>
			</dl>
			<dl>
				<dt>结束时间：</dt>
				<dd>
					<input type="text" name="fendTime" class="required date"
						readonly="true" dateFmt="yyyy-MM-dd HH:mm:ss" size="40" /> <a
						class="inputDateButton" href="javascript:;">选择</a>
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

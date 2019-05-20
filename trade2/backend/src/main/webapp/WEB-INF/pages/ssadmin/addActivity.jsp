<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="comm/include.inc.jsp"%>
<h2 class="contentTitle">添加活动信息</h2>

<div class="pageContent">

	<form method="post" action="ssadmin/saveActivity.html"
		class="pageForm required-validate"
		onsubmit="return validateCallback(this,dialogAjaxDone)">
		<div class="pageFormContent nowrap" layoutH="97">
			<dl>
				<dt>活动标题：</dt>
				<dd>
					<input type="text" name="ftitle" class="required" size="70" />
				</dd>
			</dl>
			<dl>
				<dt>活动类型：</dt>
				<dd>
					<input type="hidden" name="activityTypeLookup.id"
						value="${activityTypeLookup.id}" /> <input type="text"
						class="required" name="activityTypeLookup.activityType" value=""
						suggestFields="id,activityType"
						suggestUrl="ssadmin/activityTypeLookup.html"
						lookupGroup="orgLookup" readonly="readonly" size="70" /> <a
						class="btnLook" href="ssadmin/activityTypeLookup.html"
						lookupGroup=activityTypeLookup>查找带回</a>
				</dd>
			</dl>
			<dl>
				<dt>是否允许多次参与：</dt>
				<dd>
					<input type="checkbox" name="fisMultiple" />
				</dd>
			</dl>
			<dl>
				<dt>两次间隔时间：</dt>
				<dd>
					<input type="text" name="ftimeInterval" class="digits" /> <span>如果可以多次完成，两次获奖的间隔</span>
				</dd>
			</dl>
			<dl>
				<dt>获得奖励：</dt>
				<dd>
					<input type="text" name="frewardPerCount" class="required digits"
						value="1" /> <span>完成多少次可以获得奖励，一般是一次</span>
				</dd>
			</dl>
			<dl>
				<dt width="333">推广奖励：</dt>
				<dd>
					<input type="text" name="frewardPromotionPerCount"
						class="required digits" /> <span>完成多少次可以获得推广奖励</span>
				</dd>
			</dl>
			<dl>
				<dt>是否永久有效：</dt>
				<dd>
					<input type="checkbox" name="fisActiveForever" />
				</dd>
			</dl>
			<dl>
				<dt>开始时间：</dt>
				<dd>
					<input type="text" name="fBeginTime" class="date" readonly="true"
						dateFmt="yyyy-MM-dd HH:mm:ss" size="40" /> <a
						class="inputDateButton" href="javascript:;">选择</a> <span>非永久有效情况，开始时间</span>
				</dd>
			</dl>
			<dl>
				<dt>结束时间：</dt>
				<dd>
					<input type="text" name="fEndTime" class="date" readonly="true"
						dateFmt="yyyy-MM-dd HH:mm:ss" size="40" /> <a
						class="inputDateButton" href="javascript:;">选择</a> <span>非永久有效情况，开始时间</span>
				</dd>
			</dl>
			<dl>
				<dt>活动内容：</dt>
				<dd>
					<textarea name="fcontent" cols="70" rows="3" class="required" />
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

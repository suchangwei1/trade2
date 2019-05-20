<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="comm/include.inc.jsp"%>
<h2 class="contentTitle">
	会员<font color="red">${fuser.fnickName}</font>证件照片审核
</h2>

<div class="pageContent">

	<form method="post" action="ssadmin/auditIdentify.html"
		class="pageForm required-validate"
		onsubmit="return validateCallback(this,dialogAjaxDone)">
		<div class="pageFormContent nowrap" layoutH="97">
			<c:if test="${isAudit == 1 }">
				<dl>
					<dt>审核结果：</dt>
					<dd>
						<select id="sel_box" type="combox" name="status" class="required">

							<option value="2">通过</option>
							<option value="3">不通过</option>
						</select>
					</dd>
				</dl>
				<dl>
					<dt>通知消息语言：</dt>
					<dd>
						<select  id="sel_box_language"  type="combox" name="nation" class="required">

							<option value="0">汉语</option>
							<option value="1">英语</option>
						</select>
					</dd>
				</dl>
				<dl style="display:none;" id="reason_box_chinese">
					<dt>不通过理由：</dt>
					<dd>
						<%--<input type="text" size="70" name="reason" placeholder="不通过理由将以消息通知的方式发送给用户"/>--%>
						<input id="reason_chinese" type="text" style="width:354px;position:absolute" name="reason" placeholder="不通过理由将以消息通知的方式发送给用户"/>

							<span style="margin-left:339px;width:100px;overflow:hidden;" >
								<select style="width:380px" onchange="document.getElementById('reason_chinese').value=this.options[this.selectedIndex].text;">
									<option value="0">请选择</option>
									<option value="1">身份证信息是倒退的，请重新上传。</option>
									<option value="2">请手持身份证。</option>
									<option value="3">您的证书过期了。</option>
									<option value="4">身份证信息模糊不清，您可以使用更好的设备。</option>
									<option value="5">请不要阻塞识别信息。</option>
									<option value="6">试着让你的身份证靠近照相机，露出你的脸。</option>
								</select>
							</span>
					</dd>
				</dl>

				<dl style="display:none;" id="reason_box_english">
					<dt>不通过理由：</dt>
					<dd>
						<input id="reason_english" type="text" style="width:354px;position:absolute" name="reason" placeholder="不通过理由将以消息通知的方式发送给用户"/>
								<span style="margin-left:339px;width:100px;overflow:hidden;">
								<select style="width:380px" onchange="document.getElementById('reason_english').value=this.options[this.selectedIndex].text;">
									<option value="0">choose</option>
									<option value="1">The identity card information is backwards，please re-upload.</option>
									<option value="2">Please hold the id card.</option>
									<option value="3">Your certificate has expired.</option>
									<option value="4">The information of id card is blurred and you can use a better equipment.</option>
									<option value="5">Please don’t block the identification information.</option>
									<option value="6">Try to get your id card closer to the camera and expose your face.</option>
								</select>
							</span>
					</dd>
				</dl>
			</c:if>

			<dl>
				<dt>会员真实姓名：</dt>
				<dd>
					<input type="hidden" name="uid" value="${fuser.fid}" /> <input
						type="text" name="frealName" readonly="true" size="70"
						value="${fuser.frealName}" />
				</dd>
			</dl>
			<dl>
				<dt>证件类型：</dt>
				<dd>
					<select type="combox" name="fidentityType" readonly="true" disabled>
						<c:forEach items="${identityTypeMap}" var="identityType">
							<c:if test="${identityType.key == fuser.fidentityType}">
								<option value="${identityType.key}" selected="true">${identityType.value}</option>
							</c:if>
							<c:if test="${identityType.key != fuser.fidentityType}">
								<option value="${identityType.key}">${identityType.value}</option>
							</c:if>
						</c:forEach>
					</select>
				</dd>
			</dl>
			<dl>
				<dt>证件号码：</dt>
				<dd>
					<input type="text" name="fidentityNo" size="70" readonly="true"
						value="${fuser.fidentityNo}" />
				</dd>
			</dl>
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
					<img src="${cdn}/${fuser.fIdentityPath3}" width="500" />
				</dd>
			</dl>
		</div>
		<div class="formBar">
			<ul>
			<c:if test="${isAudit == 1 }">
				<li><div class="buttonActive">
						<div class="buttonContent">
							<button type="submit">保存</button>
						</div>
					</div></li>
			</c:if>		
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

$(".required").change(function(){
   if($("#sel_box").val()==3){//不通过
          //英文
          if($("#sel_box_language").val()==1){
              $("#reason_box_chinese").css("display","none");
              $("#reason_box_english").css("display","block");
          }else{
              //中文
              $("#reason_box_chinese").css("display","block");
              $("#reason_box_english").css("display","none");
		  }
   } else{
       $("#reason_box_chinese").hide();
       $("#reason_box_english").hide();
   }



});
</script>

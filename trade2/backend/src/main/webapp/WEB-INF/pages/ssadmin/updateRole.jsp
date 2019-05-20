<%@page import="com.trade.model.Fsecurity"%>
<%@page import="com.trade.model.Frole"%>
<%@page import="com.trade.model.FroleSecurity"%>
<%@page import="java.util.List"%>
<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="comm/include.inc.jsp"%>

<%!public String role_tree(Fsecurity security,Frole role, Integer[] securityIndex) {
	StringBuilder explandBuilder = null;
	
	if (security.getFsecurity() != null) {
		explandBuilder = new StringBuilder("<tr id=\"" + security.getFid() + "\" pId=\"" + security.getFsecurity().getFid() + "\">\n");
	} else {
		explandBuilder = new StringBuilder("<tr id=\"" + security.getFid() + "\">\n");
	}
	
	explandBuilder.append("<td><span ref=\"treeChk\" class=\"button chk checkbox_false_full\"/><span ref=\"allChk\" class=\"button chk checkbox_false_full setAll\"/>" + security.getFname() + "</td>\n");
	explandBuilder.append("<td>\n");
	
	explandBuilder.append("<span class='inputValueRole'>");
    explandBuilder.append(security.getFname() + "<input type='checkbox' name='role[" +  security.getFid() + "]' value='" + security.getFid() + "'");
			boolean isFind = false;
		for(FroleSecurity roleSecurities : role.getFroleSecurities()) {
			if (roleSecurities.getFsecurity().getFid().equals(security.getFid())) {
				explandBuilder.append("checked='checked'/>");
				isFind = true;
				break;
			}
		}
		
		if (isFind == false) {
			explandBuilder.append("/>");
		}
	securityIndex[0] = securityIndex[0] + 1;
	explandBuilder.append("</span>\n");
	
	explandBuilder.append("</td>\n");
	explandBuilder.append("</tr>\n");

	for(Fsecurity s : security.getFsecurities()) {
		explandBuilder.append(role_tree(s,role, securityIndex));				
	}
	
	return explandBuilder.toString();
}%>
<%
	Fsecurity security = (Fsecurity) request.getAttribute("security");
	Frole role = (Frole)request.getAttribute("role");
	String cPermissonTree = role_tree(security,role, new Integer[]{0});
%>
<script type="text/javascript">
<!--
	// top
	$(document).ready(function() {
		initRolePage();
	});
//-->
</script>

<div class="pageContent">
	<form method="post" action="ssadmin/updateRole.html"
		class="pageForm required-validate"
		onsubmit="return validateCallback(this,dialogAjaxDone)">
		<div layouth="56" class="pageFormContent">
			<dl>
				<dt>角色名称：</dt>
				<dd>
				    <input type="hidden" name="roleId" value="${role.fid}"/>
					<input type="text" name="fname" class="required" maxlength="64" value="${role.fname}"/>
				</dd>
			</dl>
			<dl>
				<dt>描述：</dt>
				<dd>
					<input type="text" name="fdescription" class="required"
						maxlength="256"  value="${role.fdescription}"/>
				</dd>
			</dl>
			<div class="divider"></div>
			<table class="treeTable list" width="100%">
				<tr>
					<td>模块名称</td>
					<td width="70%">操作权限</td>
				</tr>
				<%=cPermissonTree%>
			</table>
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
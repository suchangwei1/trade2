<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="comm/include.inc.jsp"%> 
<div class="pageContent"> 
	<div style="margin:0 auto;text-align:center;font-size:30px;">
		<c:if test="${synNum>=0}">
			<span>同步完成${synNum}位用户信息</span>
		</c:if>
		<c:if test="${synNum<0}">
			<span>用户同步任务正在进行，请勿重复操作！</span>
		</c:if>
	</div>
</div>

 

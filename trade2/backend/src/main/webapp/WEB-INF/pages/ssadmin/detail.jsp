<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="comm/include.inc.jsp"%>
<h2 class="contentTitle">交易详情</h2>
<div class="pageContent">
	<div class="pageFormContent nowrap" layoutH="97">
		<dl>
			<dt>交易号：</dt>
			<dd>
				${map.result.txid[0]}
			</dd>
		</dl>
		<dl>
			<dt>币种：</dt>
			<dd>
				${coinType}
			</dd>
		</dl>
		<dl>
			<dt>充值地址：</dt>
			<dd>
				${map.result.details[0].address}
			</dd>
		</dl>
		<dl>
			<dt>提现地址：</dt>
			<dd>
				${map.result.details[1].address}
				<%--<input type="text"  size="100" value="${map.result.details[1].address}"/>--%>
			</dd>
		</dl>
		<dl>
			<dt>金额：</dt>
			<dd>
				<fmt:formatNumber value="${map.result.details[1].amount}" pattern="##.##" maxIntegerDigits="10" maxFractionDigits="6"/></td>
				<%--<input type="text"  maxlength="30"--%>
					   <%--class="required number" size="40"  value="${map.result.details[1].amount}" disabled="true"/>--%>
			</dd>
		</dl>
		<dl>
			<dt>手续费：</dt>
			<dd>
				<fmt:formatNumber value="${fee}" pattern="##.##" maxIntegerDigits="10" maxFractionDigits="6"/></td>
			</dd>
		</dl>
		<dl>
			<dt>确认次数：</dt>
			<dd>
				${map.result.confirmations}
			</dd>
		</dl>
		<dl>
			<dt>时间：</dt>
			<dd>
				<fmt:formatDate value="${date}" pattern="yyyy-MM-dd HH:mm:ss"/>
			</dd>
		</dl>
	</div>


</div>


<script type="text/javascript">
    function customvalidXxx(element){
        if ($(element).val() == "xxx") return false;
        return true;
    }
</script>

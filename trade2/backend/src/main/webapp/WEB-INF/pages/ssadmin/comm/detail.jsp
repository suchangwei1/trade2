<%@ page pageEncoding="UTF-8"%>
<div class="accordion" fillSpace="sidebar">
	<shiro:hasPermission name="user">
		<div class="accordionHeader">
			<h2>
				<span>Folder</span>会员管理
			</h2>
		</div>
		<div class="accordionContent">
			<ul class="tree treeFolder">
				<shiro:hasPermission name="ssadmin/userList.html">
					<li><a href="ssadmin/userList.html" target="navTab"
						rel="userList">会员列表</a>
					</li>
				</shiro:hasPermission>
				<shiro:hasPermission name="ssadmin/userAuditList.html">
					<li><a href="ssadmin/userAuditList.html" target="navTab"
						rel="userAuditList">待审核会员列表</a>
					</li>
				</shiro:hasPermission>
				<shiro:hasPermission name="ssadmin/logList.html">
					<li><a href="ssadmin/logList.html" target="navTab"
						rel="logList">会员登录日志列表</a>
					</li>
				</shiro:hasPermission>
				<shiro:hasPermission name="ssadmin/entrustlogList.html">
					<li><a href="ssadmin/userIntroList.html" target="navTab"
						   rel="userIntroList">会员推广列表</a>
					</li>
				</shiro:hasPermission>
				<shiro:hasPermission name="ssadmin/entrustlogList.html">
				<li><a href="ssadmin/profitList.html" target="navTab"
					   rel="profitList">会员分润列表</a>
				</li>
				</shiro:hasPermission>
			</ul>
		</div>
	</shiro:hasPermission>


	<shiro:hasPermission name="article">
		<div class="accordionHeader">
			<h2>
				<span>Folder</span>文章管理
			</h2>
		</div>
		<div class="accordionContent">
			<ul class="tree treeFolder">
				<shiro:hasPermission name="ssadmin/articleList.html">
					<li><a href="ssadmin/articleList.html" target="navTab"
						rel="articleList">文章列表</a>
					</li>
				</shiro:hasPermission>
				<shiro:hasPermission name="ssadmin/articleTypeList.html">
					<li><a href="ssadmin/articleTypeList.html" target="navTab"
						rel="articleTypeList">文章类型</a>
					</li>
				</shiro:hasPermission>
			</ul>
		</div>
	</shiro:hasPermission>

	<shiro:hasPermission name="virtualCoin">
		<div class="accordionHeader">
			<h2>
				<span>Folder</span>虚拟币管理
			</h2>
		</div>
		<div class="accordionContent">
			<ul class="tree treeFolder">
				<shiro:hasPermission name="ssadmin/virtualCoinTypeList.html">
					<li><a href="ssadmin/virtualCoinTypeList.html" target="navTab"
						rel="virtualCoinTypeList">虚拟币类型列表</a>
					</li>
				</shiro:hasPermission>
				<shiro:hasPermission name="ssadmin/marketList.html">
					<li><a href="ssadmin/marketList.html" target="navTab"
						   rel="marketList">交易市场列表</a>
					</li>
				</shiro:hasPermission>
				<%--<shiro:hasPermission name="ssadmin/virtualCoinOrder.html">--%>
					<%--<li><a href="ssadmin/virtualCoinOrder.html?type=5" target="navTab"--%>
						<%--rel="virtualOrderList">虚拟币排序列表</a>--%>
					<%--</li>--%>
				<%--</shiro:hasPermission>--%>
				<%--<shiro:hasPermission name="ssadmin/walletAddressList.html">
				<li><a href="ssadmin/walletAddressList.html" target="navTab"
					rel="walletAddressList" title="虚拟币可用地址列表">虚拟币可用地址列表</a>
				</li>
				</shiro:hasPermission>--%>
				<shiro:hasPermission name="ssadmin/userVirtualCoinAddressList.html">
					<li><a href="ssadmin/userVirtualCoinAddressList.html" target="navTab"
						   rel="userVirtualCoinAddressList">用户地址列表</a>
					</li>
				</shiro:hasPermission>
			</ul>
		</div>
	</shiro:hasPermission>

	<shiro:hasPermission name="capital">
		<div class="accordionHeader">
			<h2>
				<span>Folder</span>资金管理
			</h2>
		</div>
		<div class="accordionContent">
			<ul class="tree treeFolder">


				<shiro:hasPermission name="ssadmin/virtualCaptualoperationList.html">
					<li><a href="ssadmin/virtualCaptualoperationList.html"
						target="navTab" rel="virtualCaptualoperationList">虚拟币提现记录列表</a>
					</li>
				</shiro:hasPermission>
				<shiro:hasPermission name="ssadmin/virtualCapitalInList.html">
					<li><a href="ssadmin/virtualCapitalInList.html"
						target="navTab" rel="virtualCapitalInList">虚拟币充值记录列表</a>
					</li>
				</shiro:hasPermission>
				<shiro:hasPermission name="ssadmin/virtualCapitalOutList.html">
					<li><a href="ssadmin/virtualCapitalOutList.html"
						target="navTab" rel="virtualCapitalOutList">待审核虚拟币提现列表</a>
					</li>
				</shiro:hasPermission>



				<shiro:hasPermission name="ssadmin/virtualwalletList.html">
					<li><a href="ssadmin/virtualwalletList.html" target="navTab"
						rel="virtualwalletList">会员虚拟币列表</a>
					</li>
				</shiro:hasPermission>

				<shiro:hasPermission name="ssadmin/virtualoperationlogList.html">
					<li><a href="ssadmin/virtualoperationlogList.html"
						target="navTab" rel="virtualoperationlogList">会员虚拟币手工充值列表</a>
					</li>
				</shiro:hasPermission>
				<shiro:hasPermission name="ssadmin/entrustList.html">
					<li>
						<a href="ssadmin/entrustList.html" target="navTab" rel="entrustList">会员委托列表</a>
					</li>
				</shiro:hasPermission>
				<%--<shiro:hasPermission name="ssadmin/entrustList.html">
					<li>
						<a href="ssadmin/entrustList.html?entype=1" target="navTab" rel="entrustsellList">委托卖单列表</a>
					</li>
				</shiro:hasPermission>--%>
				<shiro:hasPermission name="ssadmin/entrustlogList.html">
					<li><a href="ssadmin/userFentrustlog.html" target="navTab"
						   rel="entrustlogList">会员成交记录列表</a>
					</li>
				</shiro:hasPermission>
				<%--<shiro:hasPermission name="ssadmin/entrustPlanList.html">--%>
					<%--<li><a href="ssadmin/entrustPlanList.html" target="navTab"--%>
						<%--rel="entrustPlanList">计划委托列表</a>--%>
					<%--</li>--%>
				<%--</shiro:hasPermission>--%>
				<shiro:hasPermission name="ssadmin/walletErrorList.html">
				<li><a href="ssadmin/walletErrorList.html" target="navTab"
					   rel="walletErrorList">会员资金警报</a>
				</li>
				</shiro:hasPermission>

				<%--<shiro:hasPermission name="ssadmin/detailJsp.html">--%>
					<%--<li>--%>
						<%--<a href="/ssadmin/detailJsp.html" height="400" width="750" target="dialog">查看交易详情</a>--%>
					<%--</li>--%>
				<%--</shiro:hasPermission>--%>
			</ul>
		</div>
	</shiro:hasPermission>

	<shiro:hasPermission name="cc">
		<div class="accordionHeader">
			<h2>
				<span>Folder</span>C2C管理
			</h2>
		</div>
		<div class="accordionContent">
			<ul class="tree treeFolder">
				<shiro:hasPermission name="ssadmin/cccheck.html">
					<li><a href="ssadmin/ccUserList.html"
						target="navTab" rel="virtualCaptualoperationList">商户管理</a>
					</li>
				</shiro:hasPermission>
				<shiro:hasPermission name="ssadmin/cccheck.html">
					<li><a href="ssadmin/ccList.html"
						target="navTab" rel="virtualCaptualoperationList">用户买卖单管理</a>
					</li>
				</shiro:hasPermission>
			</ul>
		</div>
	</shiro:hasPermission>

	<shiro:hasPermission name="otc">
		<div class="accordionHeader">
			<h2>
				<span>Folder</span>OTC管理
			</h2>
		</div>
		<div class="accordionContent">
			<ul class="tree treeFolder">
				<shiro:hasPermission name="ssadmin/otccheck.html">
					<li><a href="ssadmin/otcSetList.html"
						target="navTab">用户支付方式管理</a>
					</li>
				</shiro:hasPermission>
				<shiro:hasPermission name="ssadmin/otccheck.html">
					<li><a href="ssadmin/otcOrderList.html"
						target="navTab">挂单管理</a>
					</li>
				</shiro:hasPermission>
				<shiro:hasPermission name="ssadmin/otccheck.html">
					<li><a href="ssadmin/otcOrderLogList.html"
						target="navTab" >快速买卖单管理</a>
					</li>
				</shiro:hasPermission>
			</ul>
		</div>
	</shiro:hasPermission>


	<shiro:hasPermission name="ico">
		<div class="accordionHeader">
			<h2>
				<span>Folder</span>ICO管理
			</h2>
		</div>
		<div class="accordionContent">
			<ul class="tree treeFolder">
				<shiro:hasPermission name="/ssadmin/icoList.html">
					<li><a href="/ssadmin/icoList.html" target="navTab"
						   rel="icoList">ICO列表</a>
					</li>
				</shiro:hasPermission>
				<shiro:hasPermission name="/ssadmin/icoRecordList.html">
					<li><a href="/ssadmin/icoRecordList.html" target="navTab"
						   rel="icoRecordList">ICO认购列表</a>
					</li>
				</shiro:hasPermission>
				<shiro:hasPermission name="/ssadmin/icoSwapRateList.html">
					<li><a href="/ssadmin/icoSwapRateList.html" target="navTab"
						   rel="icoSwapRateList">ICO兑换率列表</a>
					</li>
				</shiro:hasPermission>
			</ul>
		</div>
	</shiro:hasPermission>

	<shiro:hasPermission name="report">
		<div class="accordionHeader">
			<h2>
				<span>Folder</span>报表统计
			</h2>
		</div>
		<div class="accordionContent">
			<ul class="tree treeFolder">
				<shiro:hasPermission name="ssadmin/userReport.html">
					<li><a
						href="ssadmin/userReport.html?startDate=<%=startDate%>&endDate=<%=endDate%>"
						target="navTab" rel="userReport">会员注册统计表</a>
					</li>
				</shiro:hasPermission>
				<shiro:hasPermission name="ssadmin/vcOperationInReport.html">
					<li><a
						href="ssadmin/vcOperationReport.html?type=1&status=3&url=ssadmin/vcOperationInReport&startDate=<%=startDate%>&endDate=<%=endDate%>"
						target="navTab" rel="vcOperationInReport">虚拟币充值统计表</a>
					</li>
				</shiro:hasPermission>
				<shiro:hasPermission name="ssadmin/vcOperationOutReport.html">
					<li><a
						href="ssadmin/vcOperationReport.html?type=2&status=3&url=ssadmin/vcOperationOutReport&startDate=<%=startDate%>&endDate=<%=endDate%>"
						target="navTab" rel="vcOperationOutReport">虚拟币提现统计表</a>
					</li>
				</shiro:hasPermission>
				<shiro:hasPermission name="ssadmin/virtualCaptualoperationTable.html">
					<li><a href="ssadmin/virtualCaptualoperationTable.html"
						   target="navTab" rel="virtualCaptualoperationTable">虚拟币日提现统计表</a>
					</li>
				</shiro:hasPermission>
				<shiro:hasPermission name="ssadmin/virtualCapitalInTableSum.html">
				<li><a
						href="ssadmin/virtualCapitalInTableSum.html"
						target="navTab" rel="virtualCapitalInTableSum">虚拟币日充值统计表</a>
				</li>
				</shiro:hasPermission>
				<shiro:hasPermission name="ssadmin/userFentrustlogSumTable.html">
				<li><a
						href="ssadmin/userFentrustlogSumTable.html"
						target="navTab" rel="userFentrustlogSumTable">交易数量日统计表</a>
				</li>
				</shiro:hasPermission>
			<%--	<shiro:hasPermission name="ssadmin/vcOperationInReport.html">
					<li><a
							href="ssadmin/vcOperationReport.html?type=1&status=3&url=ssadmin/vcOperationInReport&startDate=<%=startDate%>&endDate=<%=endDate%>"
							target="navTab" rel="vcOperationInReport">虚拟币日充值统计表</a>
					</li>
				</shiro:hasPermission>
--%>




				<shiro:hasPermission name="ssadmin/totalReport.html">
					<li><a href="ssadmin/totalReport.html" target="navTab"
						rel="totalReport">综合统计表</a>
					</li>
				</shiro:hasPermission>
				<shiro:hasPermission name="ssadmin/walletReport.html">
					<li><a href="ssadmin/walletReport.html" target="navTab" rel="walletReport">钱包统计表</a></li>
				</shiro:hasPermission>
				<shiro:hasPermission name="ssadmin/curEntrustReport.html">
					<li><a href="ssadmin/curEntrustReport.html" target="navTab" rel="curEntrustReport">当前委托统计表</a>
					</li>
				</shiro:hasPermission>
				<%--<shiro:hasPermission name="ssadmin/totalreportList.html">--%>
					<%--<li><a href="ssadmin/totalreportList.html" target="navTab"--%>
						<%--rel="totalreportList">平台资金汇总表</a>--%>
					<%--</li>--%>
				<%--</shiro:hasPermission>--%>
				<shiro:hasPermission name="ssadmin/viCoinTradeReport.html">
					<li><a href="/ssadmin/viCoinTradeReport.html" target="navTab"
						rel="viCoinTradeReport">虚拟币交易统计表</a>
					</li>
				</shiro:hasPermission>
				<shiro:hasPermission name="ssadmin/upanddownReport.html">
					<li><a href="/ssadmin/upanddownReport.html" target="navTab"
						rel="viCoinTradeReport">涨跌幅统计表</a>
					</li>
				</shiro:hasPermission>
				<%--<shiro:hasPermission name="ssadmin/turnoverReport.html">--%>
					<%--<li><a href="/ssadmin/turnoverReport.html" target="navTab"--%>
						<%--rel="turnoverReport">交易额统计表</a>--%>
					<%--</li>--%>
				<%--</shiro:hasPermission>--%>
				<%--<shiro:hasPermission name="ssadmin/dateWalletReport.html">--%>
					<%--<li><a href="/ssadmin/dateWalletReport.html" target="navTab"--%>
						   <%--rel="dateWalletReport">单日沉淀资金报表</a>--%>
					<%--</li>--%>
				<%--</shiro:hasPermission>--%>
				<shiro:hasPermission name="ssadmin/withdrawfeeReport.html">
					<li><a href="/ssadmin/withdrawfeeReport.html" target="navTab"
						   rel="withdrawfeeReport">手续费报表</a>
					</li>
				</shiro:hasPermission>
				<%--<shiro:hasPermission name="ssadmin/tradefeeReport.html">
					<li><a href="/ssadmin/tradefeeReport.html" target="navTab"
						   rel="tradefeeReport">交易手续费报表</a>
					</li>
				</shiro:hasPermission>--%>
			</ul>
		</div>
	</shiro:hasPermission>


	<shiro:hasPermission name="system">
		<div class="accordionHeader">
			<h2>
				<span>Folder</span>系统管理
			</h2>
		</div>
		<div class="accordionContent">
			<ul class="tree treeFolder">

				<shiro:hasPermission name="ssadmin/systemArgsList.html">
					<li><a href="ssadmin/systemArgsList.html" target="navTab"
						rel="systemArgsList">系统参数列表</a>
					</li>
				</shiro:hasPermission>

				<shiro:hasPermission name="ssadmin/securityTreeList.html">
					<li><a
						href="ssadmin/goSecurityJSP.html?url=ssadmin/securityTreeList&treeId=1"
						target="navTab" rel="securityTreeList">权限列表</a>
					</li>
				</shiro:hasPermission>
				<shiro:hasPermission name="ssadmin/roleList.html">
					<li><a href="ssadmin/roleList.html" target="navTab"
						rel="roleList">角色列表</a>
					</li>
				</shiro:hasPermission>
				<shiro:hasPermission name="ssadmin/adminList.html">
					<li><a href="ssadmin/adminList.html" target="navTab"
						rel="adminList">管理员列表</a>
					</li>
				</shiro:hasPermission>
				<shiro:hasPermission name="ssadmin/countLimitList.html">
					<li><a href="ssadmin/countLimitList.html" target="navTab"
						rel="countLimitList">限制管理列表</a>
					</li>
				</shiro:hasPermission>
				<li><a href="ssadmin/sqlJsp.html" target="navTab"  height="350" width="800"
					   rel="sqlJsp">导出数据表</a>
				</li>
			</ul>
		</div>
	</shiro:hasPermission>
</div>
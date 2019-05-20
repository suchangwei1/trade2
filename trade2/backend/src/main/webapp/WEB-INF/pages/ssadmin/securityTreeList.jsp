<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="comm/include.inc.jsp"%>
<script type="text/javascript">
<!--
	var setting = {
		view : {
		//showIcon: false
		},
		data : {
			simpleData : {
				enable : true,
				idKey : "id",
				pIdKey : "pId",
				rootPId : ""
			}
		},
		callback : {
			onClick : function(event, treeId, treeNode) {
				var $rel = $("#jbsxBox2moduleList");
				$rel.loadUrl(treeNode.url, {}, function() {
					$rel.find("[layoutH]").layoutH();
				});

				event.preventDefault();
			}
		}
	};

	var zNodes = ${perms};

	$(document).ready(function() {
		var t = $("#securityTree");
		t = $.fn.zTree.init(t, setting, zNodes);
		t.expandAll(true);
	});
//-->
</script>

<div class="pageContent">
	<div class="tabs">
		<div class="tabsContent">
			<div>
				<div layoutH="5" id="jbsxBox2moduleTree"
					style="float:left; display:block; overflow:auto; width:300px; border:solid 1px #CCC; line-height:21px; background:#fff">
					<ul id="securityTree" class="ztree"></ul>
				</div>

				<div layoutH="0" id="jbsxBox2moduleList" class="unitBox"
					style="margin-left:306px;">
					<c:import url="securityList1.jsp"></c:import>
				</div>
			</div>
		</div>
	</div>
</div>
<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="comm/include.inc.jsp"%>
<%--<form id="pagerForm" method="post"--%>
	  <%--action="ssadmin/export.html">--%>

	<span style="font-size: 18px;display: inherit;margin: 50px 10px -10px 50px;">sql语句：</span>
<textarea  type="text" id="sql"  name="sql"  style="margin: 50px 10px -10px 50px;width:95%;height:200px;font-size: 20px"></textarea>
<%--</form>--%>
<div class="">
	<ul class="">
		<%--<li><a class="icon"--%>
		<%--href="ssadmin/export.html"--%>
		<%--target="dwzExport" title="确定要导出操作记录吗？" ref="sqlJsp">--%>
		<button style="font-size: 18px;margin: 50px" onclick="exporp_sql()">导出操作记录</button>
		<%--</a></li>--%>
	</ul>
</div>


<script>


    function exporp_sql() {
        var sql=$("#sql").val();
        var url="ssadmin/export.html";
        var title="确定导出数据表吗？"

        alertMsg.confirm(title, {
            okCall: function(){
                $.ajax({
                    url:url,
					data:"sql="+sql,
                    type: 'POST',
					success:function (data) {
//                        alert(data)

						if(data=="ok"){
                            window.location="ssadmin/export_table.html";
						}else {
                            alertMsg.info(data)
						}

                    }
				})


			}
        });

    }



</script>
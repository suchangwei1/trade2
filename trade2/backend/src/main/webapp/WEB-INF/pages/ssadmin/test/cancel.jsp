<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="com.trade.util.SpringContextUtils" %>
<%@ page import="com.trade.service.front.FrontUserService" %>
<%@ page import="com.trade.service.front.FrontTradeService" %>
<%@ page import="org.springframework.jdbc.core.JdbcTemplate" %>
<%@ page import="java.util.Map" %>
<%@ page import="java.util.List" %>
<%@ page import="com.trade.model.Fuser" %>
<%@ page import="com.trade.model.Fentrust" %>
<%@ page import="com.trade.Enum.EntrustStatusEnum" %>
<%@ page import="com.trade.cache.data.RealTimeDataService" %>
<%@ page import="com.trade.service.front.FmessageService" %>
<%@ page import="com.trade.model.Fmessage" %>
<%@ page import="com.trade.model.Fadmin" %>
<%@ page import="java.sql.Timestamp" %>
<%@ page import="net.sf.json.JSON" %>
<%@ page import="net.sf.json.JSONArray" %><%

    //out.write("123");

    FrontUserService frontUserService = SpringContextUtils.getBean(FrontUserService.class);
    FrontTradeService frontTradeService = SpringContextUtils.getBean(FrontTradeService.class);
    RealTimeDataService realTimeDataService = SpringContextUtils.getBean(RealTimeDataService.class);
    FmessageService fmessageService = SpringContextUtils.getBean(FmessageService.class);

    JdbcTemplate jdbc = SpringContextUtils.getBean(JdbcTemplate.class);
    List<Map<String, Object>> list = jdbc.queryForList("SELECT a.fid, a.FUs_fId FROM fentrust a WHERE a.fVi_fId = 27 AND (a.fStatus = 1 or a.fStatus = 2)");

    System.out.println(JSONArray.fromObject(list));

    for (Map<String, Object> map : list) {
        Fuser fuser = frontUserService.findById(Integer.valueOf(map.get("FUs_fId").toString()));
        Fentrust fentrust = frontUserService.findByFid(Integer.valueOf(map.get("fid").toString()));

        if (fentrust != null && (fentrust.getFstatus() == EntrustStatusEnum.Going || fentrust.getFstatus() == EntrustStatusEnum.PartDeal) && fentrust.getFuser().getFid() == fuser.getFid()) {
            try {
                /*更新挂单*/
                //frontTradeService.updateCancelFentrust(fentrust, fuser);
                //realTimeDataService.removeEntrustBuyMap(fentrust.getMarket().getId(), fentrust);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }


%>
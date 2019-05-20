package com.trade.controller.admin;


import com.trade.model.Fentrust;
import com.trade.model.Fentrustlog;
import com.trade.model.Fvirtualcointype;
import com.trade.model.Market;
import com.trade.service.admin.EntrustlogService;
import com.trade.service.admin.VirtualCoinService;
import com.trade.service.front.MarketService;
import com.trade.util.*;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
public class EntrustlogController extends BaseController {
	@Autowired
	private EntrustlogService entrustlogService;
	//每页显示多少条数据
	private int numPerPage = Utils.getNumPerPage();

	@Autowired
	private MarketService marketService;

	@Autowired
	private VirtualCoinService virtualCoinService;

	@RequestMapping("/ssadmin/entrustlogList")
	@RequiresPermissions("ssadmin/entrustlogList.html")
	public ModelAndView Index(HttpServletRequest request) throws Exception{
		ModelAndView modelAndView = new ModelAndView() ;
		modelAndView.setViewName("ssadmin/entrustlogList") ;
		
		//当前页
		int currentPage = 1;
		//搜索关键字
		String keyWord = request.getParameter("keywords");
		String startDate = request.getParameter("startDate");
		String endDate = request.getParameter("endDate");
		if(request.getParameter("pageNum") != null){
			currentPage = Integer.parseInt(request.getParameter("pageNum"));
		}
		
		if(StringUtils.hasText(startDate) && StringUtils.hasText(endDate)){
            Date start = DateUtils.formatDate(startDate);
            Date end = DateUtils.formatDate(endDate);
            int days = DateUtils.getDays(start, end);
            if(days > 7){
            	modelAndView.addObject("startDate", startDate);
            	modelAndView.addObject("endDate", endDate);
            	modelAndView.addObject("statusCode", 300);
            	modelAndView.addObject("message", "日期间隔不能大于7天");
            	modelAndView.setViewName("ssadmin/comm/ajaxDone");
                return modelAndView;
            }
        }else{
        	endDate = DateUtils.formatDate(new Date());
        	startDate = DateUtils.formatDate(DateUtils.getDaysBefore(7));
        }
		
		StringBuilder filter = new StringBuilder();
		filter.append("where 1=1 \n");
//		filter.append(" and (c.fid != 15184) and (c.fid != 11101) and (c.fid != "+ Constants.RobotID +")  \n");
		// 排除机器人
		filter.append(" and b.robotStatus=0 ");
		if(keyWord != null && keyWord.trim().length() >0){
			filter.append("and (LOCATE(c.fNickName, '" + keyWord + "')>0 \n");
			filter.append("or LOCATE(c.fRealName, '" + keyWord + "')>0 \n");
			filter.append("or LOCATE(c.floginName, '" + keyWord + "')>0 ) \n");
			modelAndView.addObject("keywords", keyWord);
		}
		if(StringUtils.hasText(startDate)){
			filter.append("and a.fCreateTime>=").append("'" + startDate + "' ");
			modelAndView.addObject("startDate", startDate);
		}
		if(StringUtils.hasText(endDate)){
			filter.append("and a.fCreateTime<=").append("'" + endDate + "' ");
			modelAndView.addObject("endDate", endDate);
		}
		List list = this.entrustlogService.list((currentPage-1)*numPerPage, numPerPage, filter+"",true);
		modelAndView.addObject("entrustlogList", list);
		modelAndView.addObject("numPerPage", numPerPage);
		modelAndView.addObject("currentPage", currentPage);
		modelAndView.addObject("rel", "entrustlogList");
		
		if(StringUtils.hasText(request.getParameter("totalCount"))){
			modelAndView.addObject("totalCount", Integer.valueOf(request.getParameter("totalCount")));
		}else{
			modelAndView.addObject("totalCount",entrustlogService.countForList(filter.toString()));
		}
		return modelAndView ;
	}

	@RequestMapping("/ssadmin/userFentrustlogSumTable")
	@RequiresPermissions("ssadmin/userFentrustlogSumTable.html")
	public ModelAndView userFentrustlogSumTable(HttpServletRequest request) throws Exception{
		ModelAndView modelAndView = new ModelAndView() ;
		modelAndView.setViewName("ssadmin/userFentrustlogSumTable") ;
		//当前页
		int currentPage = 1;
		//搜索关键字
		String keyWord = request.getParameter("keywords");
		String notContainkeyWords=request.getParameter("notContainKeyWords");
		String orderField = request.getParameter("orderField");
		String orderDirection = request.getParameter("orderDirection");
		if(request.getParameter("pageNum") != null){
			currentPage = Integer.parseInt(request.getParameter("pageNum"));
		}
       //交易数量日统计
		StringBuffer filter1=new StringBuffer();
		filter1.append( "select l.fPrize,SUM(l.fCount) as fCount ,sum(l.fAmount) as fAmount,sum(l.ffees) AS fee,l.fEntrustType,\n");
		filter1.append(" DATE_FORMAT(l.fCreateTime, '%Y-%m-%d') as fCreateTime,   l.FVI_type AS mid, m.id as cid,m.sell_id,\n");
		filter1.append("m.buy_id from fentrustlog l, fentrust f, fuser u , market m where u.fid= f.FUs_fId and l.FEn_fId= f.fid\n");
		filter1.append(" and m.id = f.fVi_fId \n");

		String filter2 ="select count(1) from fentrustlog l, fentrust f, fuser u , market m where u.fid= f.FUs_fId and l.FEn_fId= f.fid and m.id = f.fVi_fId and  1=1  \n";
		StringBuffer filter = new StringBuffer();
		if(keyWord != null && keyWord.trim().length() >0){
			filter.append("and (u.floginName like '%"+keyWord+"%' OR \n");
			filter.append("u.fRealName like '%"+keyWord+"%' OR \n");
			filter.append("u.fNickName like '%"+keyWord+"%' ) \n");
			modelAndView.addObject("keywords", keyWord);
		}

		//把中文逗号替换成英文逗号
		if(  StringUtils.hasText(notContainkeyWords)  ){
			notContainkeyWords=notContainkeyWords.replace("，",",");
		}
		if (notContainkeyWords != null && notContainkeyWords.trim().length() > 0) {
			filter.append("AND NOT EXISTS ( \n");
			filter.append(" SELECT 1 FROM   fuser fu  WHERE   (\n");
			filter.append(" fu.floginName IN ('"+notContainkeyWords+"')\n");
			filter.append(" OR fu.fnickName  IN  ('"+notContainkeyWords+"') \n");
			filter.append(" OR fu.frealName IN  ('"+notContainkeyWords+"')\n");
			filter.append("  )\n");
			filter.append(" and fu.fid = f.FUs_fId\n");
			filter.append("  )\n");
			modelAndView.addObject("notContainKeyWords", notContainkeyWords);
		}

		if(request.getParameter("ftype") != null){
			int type = Integer.parseInt(request.getParameter("ftype"));
			if(type != 0){
				filter.append("and m.id="+type+"\n");
			}
			modelAndView.addObject("ftype", type);
		}else{
			modelAndView.addObject("ftype", 0);
		}

		//类型，0 买  1卖
		String entype = request.getParameter("entype");
		if(entype != null && entype.trim().length() >0 && !"-1".equals(entype)){
			filter.append("and l.fEntrustType="+entype+" \n");
			modelAndView.addObject("entype", entype);
		}else{
			modelAndView.addObject("entype", -1);
		}

		String startDate = request.getParameter("startDate");
		String endDate = request.getParameter("endDate");

		if(StringUtils.hasText(startDate)){
			filter.append("and DATE_FORMAT(l.fCreateTime, '%Y-%m-%d') >= ").append("'" + startDate + "' ");
			modelAndView.addObject("startDate", startDate);
		}
		if(StringUtils.hasText(endDate)){
			filter.append("and DATE_FORMAT(l.fCreateTime, '%Y-%m-%d') <= ").append("'" + endDate + "' ");
			modelAndView.addObject("endDate", endDate);
		}

		String price = request.getParameter("price");
		if(price != null && price.trim().length() >0){
			double p = Double.valueOf(price);
			filter.append("and l.fPrize="+p+" \n");
		}
		modelAndView.addObject("price", price);

		filter.append("GROUP BY DATE_FORMAT(l.fCreateTime,'%Y-%m-%d'),  l.FVI_type,l.fEntrustType \n");

		if(orderField != null && orderField.trim().length() >0){
			filter.append("order by "+orderField+"\n");
		}else{
			filter.append("order by l.fCreateTime \n");
		}

		if(orderDirection != null && orderDirection.trim().length() >0){
			filter.append(orderDirection+"\n");
		}else{
			filter.append("desc \n");
		}

		List<Fvirtualcointype> type = this.virtualCoinService.findAll();
		Map typeMap = new HashMap();
		for (Fvirtualcointype fvirtualcointype : type) {
			typeMap.put(fvirtualcointype.getFid(), fvirtualcointype.getfShortName());
		}
		List<Market> markets = marketService.findAll();
		Map marketMap = new HashMap();
		for(Market market: markets){
			marketMap.put(market.getId(), typeMap.get(market.getSellId()) + "/" + typeMap.get(market.getBuyId()));
		}
		marketMap.put(0, "全部");
		modelAndView.addObject("typeMap", marketMap);

		List list = this.entrustlogService.findAllList((currentPage-1)*numPerPage, numPerPage, filter1.toString()+filter+"",true);
		int count = this.entrustlogService.findAllListCount(filter2+filter+"");
		modelAndView.addObject("entrustList", list);
		modelAndView.addObject("numPerPage", numPerPage);
		modelAndView.addObject("rel", "entrustList");
		modelAndView.addObject("currentPage", currentPage);
		//总数量
		modelAndView.addObject("totalCount",count);
		return modelAndView;
	}

	/**
	 * 导出日交易成交表
	 *
	 * @param request
	 * @param response
	 */
	@RequestMapping(value = "/ssadmin/exportUserFentrustlogSumTable")
	@RequiresPermissions("ssadmin/exportUserFentrustlogSumTable.html")
	public void exportUserFentrustlogSumTable(HttpServletRequest request, HttpServletResponse response) {

		String keyWord = request.getParameter("keywords");
		String orderField = request.getParameter("orderField");
		String orderDirection = request.getParameter("orderDirection");
		String notContainkeyWords=request.getParameter("notContainKeyWords");
		//交易数量日统计
		StringBuffer filter1=new StringBuffer();
		filter1.append( "select l.fPrize,SUM(l.fCount) as fCount ,sum(l.fAmount) as fAmount,sum(l.ffees) AS fee,l.fEntrustType,\n");
		filter1.append("DATE_FORMAT(l.fCreateTime, '%Y-%m-%d') as fCreateTime,   l.FVI_type AS mid, m.id as cid,m.sell_id,\n");
		filter1.append("m.buy_id from fentrustlog l, fentrust f, fuser u , market m where u.fid= f.FUs_fId and l.FEn_fId= f.fid\n");
		filter1.append(" and m.id = f.fVi_fId \n");
		StringBuffer filter = new StringBuffer();
		if (keyWord != null && keyWord.trim().length() > 0) {
			filter.append("and (u.floginName like '%" + keyWord + "%' OR \n");
			filter.append("u.fRealName like '%" + keyWord + "%' OR \n");
			filter.append("u.fNickName like '%" + keyWord + "%' ) \n");
		}

		if (request.getParameter("ftype") != null) {
			int type = Integer.parseInt(request.getParameter("ftype"));
			if (type != 0) {
				filter.append("and m.id=" + type + "\n");
			}
		}
//把中文逗号替换成英文逗号
		if(  StringUtils.hasText(notContainkeyWords)  ){
			notContainkeyWords=notContainkeyWords.replace("，",",");
		}
		if (notContainkeyWords != null && notContainkeyWords.trim().length() > 0) {
			filter.append("AND NOT EXISTS ( \n");
			filter.append(" SELECT 1 FROM   fuser fu  WHERE   (\n");
			filter.append(" fu.floginName IN ('"+notContainkeyWords+"')\n");
			filter.append(" OR fu.fnickName  IN  ('"+notContainkeyWords+"') \n");
			filter.append(" OR fu.frealName IN  ('"+notContainkeyWords+"')\n");
			filter.append("  )\n");
			filter.append(" and fu.fid = f.FUs_fId\n");
			filter.append("  )\n");

		}
		//类型，0 买  1卖
		String entype = request.getParameter("entype");
		if (entype != null && entype.trim().length() > 0 && !"-1".equals(entype)) {
			filter.append("and l.fEntrustType=" + entype + " \n");
		} else {
		}

		String startDate = request.getParameter("startDate");
		String endDate = request.getParameter("endDate");

		if(StringUtils.hasText(startDate)){
			filter.append("and DATE_FORMAT(l.fCreateTime, '%Y-%m-%d') >= ").append("'" + startDate + "' ");
		}
		if(StringUtils.hasText(endDate)){
			filter.append("and DATE_FORMAT(l.fCreateTime, '%Y-%m-%d') <= ").append("'" + endDate + "' ");
		}

		String price = request.getParameter("price");
		if(price != null && price.trim().length() >0){
			double p = Double.valueOf(price);
			filter.append("and l.fPrize="+p+" \n");
		}
		filter.append("GROUP BY DATE_FORMAT(l.fCreateTime,'%Y-%m-%d'),  l.FVI_type,l.fEntrustType \n");
		if (orderField != null && orderField.trim().length() > 0) {
			filter.append("order by " + orderField + "\n");
		} else {
			filter.append("order by l.fCreateTime \n");
		}

		if (orderDirection != null && orderDirection.trim().length() > 0) {
			filter.append(orderDirection + "\n");
		} else {
			filter.append("desc \n");
		}

		List list = this.entrustlogService.findAllList(0, 0, filter1.toString() + filter + "", false);

		List<Fvirtualcointype> type = this.virtualCoinService.findAll();
		Map typeMap = new HashMap();
		for (Fvirtualcointype fvirtualcointype : type) {
			typeMap.put(fvirtualcointype.getFid(), fvirtualcointype.getfShortName());
		}
		// 标题
		XlsExport xls = new XlsExport();
		xls.createRow(0);


	/*	xls.setCell(0, "会员手机号");
		xls.setCell(1, "会员真实姓名");*/
		xls.setCell(0, "交易对");
		xls.setCell(1, "交易类型");
        xls.setCell(2, "数量");
	/*	xls.setCell(2, "单价");
		xls.setCell(3, "数量");
		xls.setCell(4, "成交金额");*/
		xls.setCell(3, "手续费");
		xls.setCell(4, "时间");

		// 填入数据
		for (int i = 1, len = list.size(); i <= len; i++) {
			Map obj = (HashMap)list.get(i-1);
			xls.createRow(i);

			xls.setCell(0, typeMap.get(obj.get("sell_id")) + "/" + typeMap.get(obj.get("buy_id")));
			xls.setCell(1, Integer.valueOf(obj.get("fEntrustType").toString()) == 0 ? "买入": "卖出");
            xls.setCell(2, obj.get("fCount").toString());
		/*	xls.setCell(2, obj.get("fPrize").toString());
			xls.setCell(3, obj.get("fCount").toString());
			xls.setCell(4, obj.get("fAmount").toString());*/
			xls.setCell(3, obj.get("fee").toString());//手续费
			xls.setCell(4, (obj.get("fCreateTime").toString()));

		}

		try {
			SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
			response.setContentType("application/vnd.ms-excel");
			response.setHeader("Content-Disposition", "attachment;filename=" + URLEncoder.encode("日交易成交统计表-", "utf-8") + format.format(new Date()) + ".xls");
			xls.exportXls(response);
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
	}
	@RequestMapping("/ssadmin/userFentrustlog")
	@RequiresPermissions("ssadmin/entrustlogList.html")
	public ModelAndView userFentrustlog(HttpServletRequest request) throws Exception{
		ModelAndView modelAndView = new ModelAndView() ;
		modelAndView.setViewName("ssadmin/userFentrustlog") ;
		//当前页
		int currentPage = 1;
		//搜索关键字
		String keyWord = request.getParameter("keywords");
		String orderField = request.getParameter("orderField");
		String orderDirection = request.getParameter("orderDirection");
		if(request.getParameter("pageNum") != null){
			currentPage = Integer.parseInt(request.getParameter("pageNum"));
		}
		String filter1= "select l.*, u.fLoginName, u.fNickName,u.fEmail,u.fTelephone,u.fRealName,m.id as cid,m.buy_id from fentrustlog l, fentrust f, fuser u , market m where u.fid= f.FUs_fId and l.FEn_fId= f.fid and m.id = f.fVi_fId and  1=1 \n";
		String filter2 ="select count(1) from fentrustlog l, fentrust f, fuser u , market m where u.fid= f.FUs_fId and l.FEn_fId= f.fid and m.id = f.fVi_fId and  1=1 \n";
		StringBuffer filter = new StringBuffer();
		if(keyWord != null && keyWord.trim().length() >0){
			filter.append("and (u.floginName like '%"+keyWord+"%' OR \n");
			filter.append("u.fRealName like '%"+keyWord+"%' OR \n");
			filter.append("u.fNickName like '%"+keyWord+"%' ) \n");
			modelAndView.addObject("keywords", keyWord);
		}

		if(request.getParameter("ftype") != null){
			int type = Integer.parseInt(request.getParameter("ftype"));
			if(type != 0){
				filter.append("and m.id="+type+"\n");
			}
			modelAndView.addObject("ftype", type);
		}else{
			modelAndView.addObject("ftype", 0);
		}

		//类型，0 买  1卖
		String entype = request.getParameter("entype");
		if(entype != null && entype.trim().length() >0 && !"-1".equals(entype)){
			filter.append("and l.fEntrustType="+entype+" \n");
			modelAndView.addObject("entype", entype);
		}else{
			modelAndView.addObject("entype", -1);
		}

		String startDate = request.getParameter("startDate");
		String endDate = request.getParameter("endDate");

		if(StringUtils.hasText(startDate)){
			filter.append("and DATE_FORMAT(l.fCreateTime, '%Y-%m-%d') >= ").append("'" + startDate + "' ");
			modelAndView.addObject("startDate", startDate);
		}
		if(StringUtils.hasText(endDate)){
			filter.append("and DATE_FORMAT(l.fCreateTime, '%Y-%m-%d') <= ").append("'" + endDate + "' ");
			modelAndView.addObject("endDate", endDate);
		}

		String price = request.getParameter("price");
		if(price != null && price.trim().length() >0){
			double p = Double.valueOf(price);
			filter.append("and l.fPrize="+p+" \n");
		}
		modelAndView.addObject("price", price);

		if(orderField != null && orderField.trim().length() >0){
			filter.append("order by "+orderField+"\n");
		}else{
			filter.append("order by l.fCreateTime \n");
		}

		if(orderDirection != null && orderDirection.trim().length() >0){
			filter.append(orderDirection+"\n");
		}else{
			filter.append("desc \n");
		}

		List<Fvirtualcointype> type = this.virtualCoinService.findAll();
		Map typeMap = new HashMap();
		for (Fvirtualcointype fvirtualcointype : type) {
			typeMap.put(fvirtualcointype.getFid(), fvirtualcointype.getfShortName());
		}
		List<Market> markets = marketService.findAll();
		Map marketMap = new HashMap();
		for(Market market: markets){
			marketMap.put(market.getId(), typeMap.get(market.getSellId()) + "/" + typeMap.get(market.getBuyId()));
		}
		marketMap.put(0, "全部");
		modelAndView.addObject("typeMap", marketMap);

		List list = this.entrustlogService.findAllList((currentPage-1)*numPerPage, numPerPage, filter1+filter+"",true);
		int count = this.entrustlogService.findAllListCount(filter2+filter+"");
		modelAndView.addObject("entrustList", list);
		modelAndView.addObject("numPerPage", numPerPage);
		modelAndView.addObject("rel", "entrustList");
		modelAndView.addObject("currentPage", currentPage);
		//总数量
		modelAndView.addObject("totalCount",count);
		return modelAndView;
	}


	/**
	 * 导出成交历史表
	 *
	 * @param request
	 * @param response
	 */
	@RequestMapping(value = "/ssadmin/exportEntrustLogList")
	@RequiresPermissions("ssadmin/exportEntrustList.html")
	public void exportEntrustLogList(HttpServletRequest request, HttpServletResponse response) {

		String keyWord = request.getParameter("keywords");
		String orderField = request.getParameter("orderField");
		String orderDirection = request.getParameter("orderDirection");
		String filter1 = "select l.*, u.fLoginName, u.fNickName,u.fEmail,u.fTelephone,u.fRealName,m.id as cid,m.sell_id,m.buy_id from fentrustlog l, fentrust f, fuser u , market m where u.fid= f.FUs_fId and l.FEn_fId= f.fid and m.id = f.fVi_fId and  1=1 \n";
		StringBuffer filter = new StringBuffer();
		if (keyWord != null && keyWord.trim().length() > 0) {
			filter.append("and (u.floginName like '%" + keyWord + "%' OR \n");
			filter.append("u.fRealName like '%" + keyWord + "%' OR \n");
			filter.append("u.fNickName like '%" + keyWord + "%' ) \n");
		}

		if (request.getParameter("ftype") != null) {
			int type = Integer.parseInt(request.getParameter("ftype"));
			if (type != 0) {
				filter.append("and m.id=" + type + "\n");
			}
		}

		//类型，0 买  1卖
		String entype = request.getParameter("entype");
		if (entype != null && entype.trim().length() > 0 && !"-1".equals(entype)) {
			filter.append("and l.fEntrustType=" + entype + " \n");
		} else {
		}

		String startDate = request.getParameter("startDate");
		String endDate = request.getParameter("endDate");

		if(StringUtils.hasText(startDate)){
			filter.append("and DATE_FORMAT(l.fCreateTime, '%Y-%m-%d') >= ").append("'" + startDate + "' ");
		}
		if(StringUtils.hasText(endDate)){
			filter.append("and DATE_FORMAT(l.fCreateTime, '%Y-%m-%d') <= ").append("'" + endDate + "' ");
		}

		String price = request.getParameter("price");
		if(price != null && price.trim().length() >0){
			double p = Double.valueOf(price);
			filter.append("and l.fPrize="+p+" \n");
		}

		if (orderField != null && orderField.trim().length() > 0) {
			filter.append("order by " + orderField + "\n");
		} else {
			filter.append("order by l.fCreateTime \n");
		}

		if (orderDirection != null && orderDirection.trim().length() > 0) {
			filter.append(orderDirection + "\n");
		} else {
			filter.append("desc \n");
		}

		List list = this.entrustlogService.findAllList(0, 0, filter1 + filter + "", false);

		List<Fvirtualcointype> type = this.virtualCoinService.findAll();
		Map typeMap = new HashMap();
		for (Fvirtualcointype fvirtualcointype : type) {
			typeMap.put(fvirtualcointype.getFid(), fvirtualcointype.getfShortName());
		}
		// 标题
		XlsExport xls = new XlsExport();
		xls.createRow(0);
		xls.setCell(0, "会员登录名");
		xls.setCell(1, "会员昵称");
		xls.setCell(2, "会员邮箱");
		xls.setCell(3, "会员手机号");
		xls.setCell(4, "会员真实姓名");
		xls.setCell(5, "交易对");
		xls.setCell(6, "交易类型");
		xls.setCell(7, "单价");
		xls.setCell(8, "数量");
		xls.setCell(9, "成交金额");
		xls.setCell(10, "创建时间");

		// 填入数据
		for (int i = 1, len = list.size(); i <= len; i++) {
			Map obj = (HashMap)list.get(i-1);
			xls.createRow(i);
			xls.setCell(0, obj.get("fLoginName").toString());
			xls.setCell(1, obj.get("fNickName").toString());
			xls.setCell(2, null == obj.get("fEmail") ? "" : obj.get("fEmail").toString());
			xls.setCell(3, null == obj.get("fTelephone") ? "" : obj.get("fTelephone").toString());

			xls.setCell(4, null == obj.get("fRealName") ? "" : obj.get("fRealName").toString());
			xls.setCell(5, typeMap.get(obj.get("sell_id")) + "/" + typeMap.get(obj.get("buy_id")));
			xls.setCell(6, Integer.valueOf(obj.get("fEntrustType").toString()) == 0 ? "买入": "卖出");
			xls.setCell(7, obj.get("fPrize").toString());
			xls.setCell(8, obj.get("fCount").toString());
			xls.setCell(9, obj.get("fAmount").toString());
			xls.setCell(10, (obj.get("fCreateTime").toString()));
		}

		try {
			SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
			response.setContentType("application/vnd.ms-excel");
			response.setHeader("Content-Disposition", "attachment;filename=" + URLEncoder.encode("委托成交记录列表-", "utf-8") + format.format(new Date()) + ".xls");
			xls.exportXls(response);
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
	}
}

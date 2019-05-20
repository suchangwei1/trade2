package com.trade.controller.admin;

import com.trade.Enum.EntrustStatusEnum;
import com.trade.Enum.EntrustTypeEnum;

import com.trade.model.Fentrust;
import com.trade.model.Fvirtualcointype;
import com.trade.model.Market;
import com.trade.service.admin.AdminService;
import com.trade.service.admin.EntrustService;
import com.trade.service.admin.VirtualCoinService;
import com.trade.service.front.MarketService;
import com.trade.util.*;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
public class EntrustController extends BaseController {
	@Autowired
	private EntrustService entrustService;
	@Autowired
	private AdminService adminService ;
	@Autowired
	private VirtualCoinService virtualCoinService;
	@Autowired
	private MarketService marketService;
	//每页显示多少条数据
	private int numPerPage = Utils.getNumPerPage();
	
	@RequestMapping("/ssadmin/entrustList")
	@RequiresPermissions("ssadmin/entrustList.html")
	public ModelAndView Index(HttpServletRequest request) throws Exception{
		ModelAndView modelAndView = new ModelAndView() ;
		modelAndView.setViewName("ssadmin/entrustList") ;
		//当前页
		int currentPage = 1;
		//搜索关键字
		String keyWord = request.getParameter("keywords");
		String orderField = request.getParameter("orderField");
		String orderDirection = request.getParameter("orderDirection");
		if(request.getParameter("pageNum") != null){
			currentPage = Integer.parseInt(request.getParameter("pageNum"));
		}

		StringBuffer filter = new StringBuffer();
		filter.append("where 1=1 \n");
		if(keyWord != null && keyWord.trim().length() >0){
			filter.append("and (fuser.floginName like '%"+keyWord+"%' OR \n");
			filter.append("fuser.frealName like '%"+keyWord+"%' OR \n");
			filter.append("fuser.fnickName like '%"+keyWord+"%' ) \n");
			modelAndView.addObject("keywords", keyWord);
		}
		
		if(request.getParameter("ftype") != null){
			int type = Integer.parseInt(request.getParameter("ftype"));
			if(type != 0){
				filter.append("and market.id="+type+"\n");
			}
			modelAndView.addObject("ftype", type);
		}else{
			modelAndView.addObject("ftype", 0);
		}
		
		String status = request.getParameter("status");
		if(status != null && status.trim().length() >0){
			if(!status.equals("0")){
				filter.append("and fstatus="+status+" \n");
			}
			modelAndView.addObject("status", status);
		}else{
			modelAndView.addObject("status", 0);
		}
		
		String entype = request.getParameter("entype");
		if(entype != null && !"".equals(entype)){
			filter.append("and fentrustType="+entype+" \n");
			modelAndView.addObject("entype", entype);
		}else{
			modelAndView.addObject("entype", "");
		}

		String beginDate = request.getParameter("beginDate");
		if(beginDate != null && beginDate.trim().length() >0){
			filter.append("and  DATE_FORMAT(fcreateTime,'%Y-%m-%d') >= '"+beginDate+"' \n");
			modelAndView.addObject("beginDate", beginDate);
		}

		String endDate = request.getParameter("endDate");
		if(endDate != null && endDate.trim().length() >0){
			filter.append("and  DATE_FORMAT(fcreateTime,'%Y-%m-%d') <= '"+endDate+"' \n");
			modelAndView.addObject("endDate", endDate);
		}

		String price = request.getParameter("price");
		if(price != null && price.trim().length() >0){
			double p = Double.valueOf(price);
			filter.append("and fprize="+p+" \n");
		}
		modelAndView.addObject("price", price);
		
		if(orderField != null && orderField.trim().length() >0){
			filter.append("order by "+orderField+"\n");
		}else{
			filter.append("order by fcreateTime \n");
		}
		
		if(orderDirection != null && orderDirection.trim().length() >0){
			filter.append(orderDirection+"\n");
		}
		else{
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
		
		Map statusMap = new HashMap();
		statusMap.put(EntrustStatusEnum.AllDeal, EntrustStatusEnum.getEnumString(EntrustStatusEnum.AllDeal));
		statusMap.put(EntrustStatusEnum.Cancel, EntrustStatusEnum.getEnumString(EntrustStatusEnum.Cancel));
		statusMap.put(EntrustStatusEnum.Going, EntrustStatusEnum.getEnumString(EntrustStatusEnum.Going));
		statusMap.put(EntrustStatusEnum.PartDeal, EntrustStatusEnum.getEnumString(EntrustStatusEnum.PartDeal));
		statusMap.put(0,"全部");
		modelAndView.addObject("statusMap", statusMap);
		
		Map entypeMap = new HashMap();
		entypeMap.put("","全部");
		entypeMap.put(EntrustTypeEnum.BUY, EntrustTypeEnum.getEnumString(EntrustTypeEnum.BUY));
		entypeMap.put(EntrustTypeEnum.SELL, EntrustTypeEnum.getEnumString(EntrustTypeEnum.SELL));
		modelAndView.addObject("entypeMap", entypeMap);
		
		
		List<Fentrust> list = this.entrustService.list((currentPage-1)*numPerPage, numPerPage, filter+"",true);
		modelAndView.addObject("entrustList", list);
		modelAndView.addObject("numPerPage", numPerPage);
		modelAndView.addObject("rel", "entrustListlog");
		modelAndView.addObject("currentPage", currentPage);
		//总数量
		modelAndView.addObject("totalCount",this.adminService.getAllCount("Fentrust", filter+""));
		return modelAndView ;
	}
	
	/**
	 * 导出委托订单列表
	 * 
	 * @param request
	 * @param response
	 */
	@RequestMapping(value = "/ssadmin/exportEntrustList")
	@RequiresPermissions("ssadmin/exportEntrustList.html")
	public void exportEntrustList(HttpServletRequest request, HttpServletResponse response){

		String keyWord = request.getParameter("keywords");
		String orderField = request.getParameter("orderField");
		String orderDirection = request.getParameter("orderDirection");
		StringBuffer filter = new StringBuffer();
		filter.append("where 1=1 \n");
		if(keyWord != null && keyWord.trim().length() >0){
			filter.append("and (fuser.floginName like '%"+keyWord+"%' OR \n");
			filter.append("fuser.frealName like '%"+keyWord+"%' OR \n");
			filter.append("fuser.fnickName like '%"+keyWord+"%' ) \n");
		}

		if(request.getParameter("ftype") != null) {
			int type = Integer.parseInt(request.getParameter("ftype"));
			if (type != 0) {
				filter.append("and market.id=" + type + "\n");
			}
		}
		String status = request.getParameter("status");
		if(status != null && status.trim().length() >0){
			if(!status.equals("0")){
				filter.append("and fstatus="+status+" \n");
			}
		}

		String entype = request.getParameter("entype");
		if(entype != null && !"".equals(entype)){
			filter.append("and fentrustType="+entype+" \n");
		}

		String beginDate = request.getParameter("beginDate");
		if(beginDate != null && beginDate.trim().length() >0){
			filter.append("and  DATE_FORMAT(fcreateTime,'%Y-%m-%d') >= '"+beginDate+"' \n");
		}

		String endDate = request.getParameter("endDate");
		if(endDate != null && endDate.trim().length() >0){
			filter.append("and  DATE_FORMAT(fcreateTime,'%Y-%m-%d') <= '"+endDate+"' \n");
		}

		String price = request.getParameter("price");
		if(price != null && price.trim().length() >0){
			double p = Double.valueOf(price);
			filter.append("and fprize="+p+" \n");
		}

		if(orderField != null && orderField.trim().length() >0){
			filter.append("order by "+orderField+"\n");
		}else{
			filter.append("order by fcreateTime \n");
		}

		if(orderDirection != null && orderDirection.trim().length() >0){
			filter.append(orderDirection+"\n");
		}else{
			filter.append("desc \n");
		}

		List<Fentrust> list = this.entrustService.list(0, 0, filter+"",false);

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
		xls.setCell(7, "状态");
		xls.setCell(8, "单价");
		xls.setCell(9, "数量");
		xls.setCell(10, "未成交数量");
		xls.setCell(11, "已成交数量");
		xls.setCell(12, "总金额");
		xls.setCell(13, "成交总金额");
		xls.setCell(14, "生成时间");

		// 填入数据
		for(int i=1, len=list.size(); i<=len; i++){
			Fentrust obj = list.get(i-1);
			xls.createRow(i);
			xls.setCell(0, obj.getFuser().getFloginName());
			xls.setCell(1, obj.getFuser().getFnickName());
			xls.setCell(2, null == obj.getFuser().getFemail() ? "" : obj.getFuser().getFemail());
			xls.setCell(3, null == obj.getFuser().getFtelephone() ? "" : obj.getFuser().getFtelephone());

			xls.setCell(4, null == obj.getFuser().getFrealName() ? "" : obj.getFuser().getFrealName());
			xls.setCell(5, typeMap.get(obj.getMarket().getSellId()) + "/" + typeMap.get(obj.getMarket().getBuyId()));
			xls.setCell(6, obj.getFstatus_s());
			xls.setCell(7, obj.getFentrustType_s());
			xls.setCell(8, obj.getFprize().toString());
			xls.setCell(9, obj.getFcount() + "");
			xls.setCell(10, obj.getFleftCount() + "");
			xls.setCell(11, (obj.getFcount() - obj.getFleftCount()) + "");
			xls.setCell(12, obj.getFamount() + "");
			xls.setCell(13, obj.getFsuccessAmount() + "");
			xls.setCell(14, (DateUtils.formatDate(obj.getFcreateTime(), "yyyy-MM-dd HH:mm:ss")));
		}

		try {
			SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
			response.setContentType("application/vnd.ms-excel");
			response.setHeader("Content-Disposition", "attachment;filename=" + URLEncoder.encode("委托交易列表-", "utf-8") + format.format(new Date()) + ".xls");
			xls.exportXls(response);
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
	}
}













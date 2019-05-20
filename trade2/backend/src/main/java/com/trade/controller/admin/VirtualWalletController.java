package com.trade.controller.admin;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.trade.util.DateUtils;
import org.apache.commons.collections.map.HashedMap;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;


import com.trade.model.Fvirtualcointype;
import com.trade.model.Fvirtualwallet;
import com.trade.service.admin.AdminService;
import com.trade.service.admin.VirtualCoinService;
import com.trade.service.admin.VirtualWalletService;
import com.trade.util.StringUtils;
import com.trade.util.Utils;
import com.trade.util.XlsExport;

@Controller
public class VirtualWalletController extends BaseController {
	@Autowired
	private VirtualWalletService virtualWalletService;
	@Autowired
	private AdminService adminService ;
	@Autowired
	private VirtualCoinService virtualCoinService;
	//每页显示多少条数据
	private int numPerPage = Utils.getNumPerPage();
	
	@RequestMapping("/ssadmin/virtualwalletList")
	@RequiresPermissions("ssadmin/virtualwalletList.html")
	public ModelAndView Index(HttpServletRequest request) throws Exception{
		ModelAndView modelAndView = new ModelAndView() ;
		modelAndView.setViewName("ssadmin/virtualwalletList") ;
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
			keyWord = keyWord.trim();
			filter.append("and (fuser.floginName like '%"+keyWord+"%' or \n");
			filter.append("fuser.fnickName like '%"+keyWord+"%' or \n");
			filter.append("fuser.frealName like '%"+keyWord+"%') \n");
			modelAndView.addObject("keywords", keyWord);
		}
		
		if(StringUtils.hasText(request.getParameter("ftype"))){
			int type = Integer.parseInt(request.getParameter("ftype"));
			if(type != 0){
				filter.append("and fvirtualcointype.fid="+type+"\n");
			}
			modelAndView.addObject("ftype", type);
		}else{
			modelAndView.addObject("ftype", 0);
		}
		
		List<Fvirtualcointype> type = this.virtualCoinService.findAll();
		Map typeMap = new HashMap();
		for (Fvirtualcointype fvirtualcointype : type) {
			typeMap.put(fvirtualcointype.getFid(), fvirtualcointype.getFname());
		}
		typeMap.put(0, "全部");
		modelAndView.addObject("typeMap", typeMap);
		
		if(orderField != null && orderField.trim().length() >0){
			filter.append("order by "+orderField+"\n");
		}else{
			filter.append("order by fid \n");
		}
		if(orderDirection != null && orderDirection.trim().length() >0){
			filter.append(orderDirection+"\n");
		}else{
			filter.append("desc \n");
		}
		List<Fvirtualwallet> list = this.virtualWalletService.list((currentPage-1)*numPerPage, numPerPage, filter+"",true);
		modelAndView.addObject("virtualwalletList", list);
		modelAndView.addObject("numPerPage", numPerPage);
		modelAndView.addObject("currentPage", currentPage);
		modelAndView.addObject("rel", "virtualwalletList");
		//总数量
		modelAndView.addObject("totalCount",this.adminService.getAllCount("Fvirtualwallet", filter+""));
		return modelAndView ;
	}
	
	@RequestMapping(value = "/ssadmin/exportCoinWalletList")
	@RequiresPermissions("ssadmin/exportCoinWalletList.html")
	public void exportCoinWalletList(HttpServletRequest request, HttpServletResponse response){
		String symbolStr = request.getParameter("ftype");
		String keyWord = request.getParameter("keywords");
		Integer symbol = StringUtils.hasText(symbolStr) ? Integer.valueOf(symbolStr) : null;

		List<Fvirtualcointype> coins = virtualCoinService.findAll();
		Map<String, Fvirtualcointype> coinMap = new HashedMap(coins.size());
		for(Fvirtualcointype coin : coins){
			coinMap.put(coin.getFid() + "", coin);
		}

		List<Map<String, Object>> list = virtualWalletService.findViWalletForExport(keyWord, symbol);
		XlsExport xls = new XlsExport();
		
		// 标题
		xls.createRow(0);
		xls.setCell(0, "会员登录名");
		xls.setCell(1, "会员昵称");
		xls.setCell(2, "会员邮箱");
		xls.setCell(3, "会员手机号");
		xls.setCell(4, "会员真实姓名");
		xls.setCell(5, "会员手机号");
		xls.setCell(6, "虚拟币类型");
		xls.setCell(7, "会员可用虚拟币量");
		xls.setCell(8, "会员冻结资金量");
		xls.setCell(9, "会员资金总量");
		
		// 填入数据
		for(int i=1, len=list.size(); i<=len; i++){
			Map<String, Object> map = list.get(i-1);
			xls.createRow(i);
			xls.setCell(0, map.get("floginName").toString());
			xls.setCell(1, map.get("fNickName").toString());
			xls.setCell(2, StringUtils.null2EmptyString(map.get("fEmail").toString()));
			xls.setCell(3, StringUtils.null2EmptyString(map.get("fTelephone").toString()));
			xls.setCell(4, map.get("fRealName").toString());
			xls.setCell(5, map.get("fTelephone").toString());
			xls.setCell(6, coinMap.get(map.get("fVi_fId").toString()).getFname());
			xls.setCell(7, (double) map.get("fTotal"));
			xls.setCell(8, (double) map.get("fFrozen"));
			xls.setCell(9, (double) map.get("total"));
		}
		
		try {
			SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
			response.setContentType("application/vnd.ms-excel");
			response.setHeader("Content-Disposition", "attachment;filename=" + URLEncoder.encode("会员虚拟币列表-", "utf-8") + format.format(new Date()) + ".xls");
			xls.exportXls(response);
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
	}

	@RequestMapping("ssadmin/goFwalletJSP")
	public ModelAndView goFwalletJSP(HttpServletRequest request) throws Exception{
		String url = request.getParameter("url");
		ModelAndView modelAndView = new ModelAndView();
		modelAndView.setViewName(url) ;
		if(request.getParameter("fid") != null){
			int id = Integer.parseInt(request.getParameter("fid"));
			Fvirtualwallet wallet = this.virtualWalletService.findById(id);

			modelAndView.addObject("wallet", wallet);
		}
		return modelAndView;
	}

}

















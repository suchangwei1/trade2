package com.trade.controller.admin;

import com.trade.Enum.OperationlogEnum;

import com.trade.model.Fadmin;
import com.trade.model.Fuser;
import com.trade.model.Fvirtualcointype;
import com.trade.model.Fvirtualoperationlog;
import com.trade.model.Fvirtualwallet;
import com.trade.service.admin.AdminService;
import com.trade.service.admin.UserService;
import com.trade.service.admin.VirtualCoinService;
import com.trade.service.admin.VirtualOperationLogService;
import com.trade.service.admin.VirtualWalletService;
import com.trade.util.MathUtils;
import com.trade.util.Utils;
import com.trade.util.XlsExport;

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
import java.util.List;

@Controller
public class VirtualOperationLogController extends BaseController {
	@Autowired
	private VirtualOperationLogService virtualOperationLogService;
	@Autowired
	private AdminService adminService ;
	@Autowired
	private UserService userService ;
	@Autowired
	private VirtualWalletService virtualWalletService;
	@Autowired
	private VirtualCoinService virtualCoinService ;
	//每页显示多少条数据
	private int numPerPage = Utils.getNumPerPage();
	
	@RequestMapping("/ssadmin/virtualoperationlogList")
	@RequiresPermissions("ssadmin/virtualoperationlogList.html")
	public ModelAndView Index(HttpServletRequest request) throws Exception{
		ModelAndView modelAndView = new ModelAndView() ;
		modelAndView.setViewName("ssadmin/virtualoperationlogList") ;
		//当前页
		int currentPage = 1;
		//搜索关键字
		String keyWord = request.getParameter("keywords");
		String orderField = request.getParameter("orderField");
		String orderDirection = request.getParameter("orderDirection");
		String logDate = request.getParameter("logDate");
		if(request.getParameter("pageNum") != null){
			currentPage = Integer.parseInt(request.getParameter("pageNum"));
		}
		StringBuffer filter = new StringBuffer();
		filter.append("where 1=1 \n");
		if(keyWord != null && keyWord.trim().length() >0){
			filter.append("and (fuser.floginName like '%"+keyWord+"%' or \n");
			filter.append("fuser.fnickName like '%"+keyWord+"%' or \n");
		//	filter.append("fcreator.fname like '%"+keyWord+"%' or \n");
			filter.append("fuser.frealName like '%"+keyWord+"%' )\n");
			modelAndView.addObject("keywords", keyWord);
		}
		
		if(logDate != null && logDate.trim().length() >0){
			filter.append("and  DATE_FORMAT(fcreateTime,'%Y-%m-%d') = '"+logDate+"' \n");
			modelAndView.addObject("logDate", logDate);
		}
		
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
		List<Fvirtualoperationlog> list = this.virtualOperationLogService.list((currentPage-1)*numPerPage, numPerPage, filter+"",true);
		modelAndView.addObject("virtualoperationlogList", list);
		modelAndView.addObject("numPerPage", numPerPage);
		modelAndView.addObject("rel", "operationLogList");
		modelAndView.addObject("currentPage", currentPage);
		//总数量
		modelAndView.addObject("totalCount",this.adminService.getAllCount("Fvirtualoperationlog", filter+""));
		return modelAndView ;
	}
	
	/**
	 * 导出虚拟币手工充值记录
	 * 
	 * @param request
	 * @param response
	 */
	@RequestMapping("/ssadmin/exportVirtualOperationlogList")
	@RequiresPermissions("ssadmin/exportVirtualOperationlogList.html")
	public void exportVirtualOperationlogList(HttpServletRequest request, HttpServletResponse response){
		String keyWord = request.getParameter("keywords");
		String orderField = request.getParameter("orderField");
		String orderDirection = request.getParameter("orderDirection");
		String logDate = request.getParameter("logDate");
		StringBuffer filter = new StringBuffer();
		filter.append("where 1=1 \n");
		if(keyWord != null && keyWord.trim().length() >0){
			filter.append("and (fuser.floginName like '%"+keyWord+"%' or \n");
			filter.append("fuser.fnickName like '%"+keyWord+"%' or \n");
		//	filter.append("fcreator.fname like '%"+keyWord+"%' or \n");
			filter.append("fuser.frealName like '%"+keyWord+"%' )\n");
		}
		
		if(logDate != null && logDate.trim().length() >0){
			filter.append("and  DATE_FORMAT(fcreateTime,'%Y-%m-%d') = '"+logDate+"' \n");
		}
		
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
		List<Fvirtualoperationlog> list = this.virtualOperationLogService.list(0, 0, filter+"",false);
		
		// 标题
		XlsExport xls = new XlsExport();
		xls.createRow(0);
		xls.setCell(0, "会员登录名");
		xls.setCell(1, "会员昵称");
		xls.setCell(2, "会员邮箱");
		xls.setCell(3, "会员手机号");
		xls.setCell(4, "会员真实姓名");
		xls.setCell(5, "状态");
		xls.setCell(6, "虚拟币名称");
		xls.setCell(7, "数量");
		xls.setCell(8, "审核时间");
		xls.setCell(9, "审核人");

		// 填入数据
		for (int i = 1, len = list.size(); i <= len; i++) {
			Fvirtualoperationlog obj = list.get(i - 1);
			xls.createRow(i);
			xls.setCell(0, obj.getFuser().getFloginName());
			xls.setCell(1, obj.getFuser().getFnickName());
			xls.setCell(2, obj.getFuser().getFemail());
			xls.setCell(3, obj.getFuser().getFtelephone());
			xls.setCell(4, obj.getFuser().getFrealName());
			xls.setCell(5, obj.getFstatus_s());
			xls.setCell(6, obj.getFvirtualcointype().getFname());
			xls.setCell(7, obj.getFqty());
			xls.setCell(8, obj.getFcreateTime());
			xls.setCell(9, null != obj.getFcreator() ? obj.getFcreator().getFname() : "");
		}

		try {
			SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
			response.setContentType("application/vnd.ms-excel");
			response.setHeader("Content-Disposition", "attachment;filename=" + URLEncoder.encode("虚拟币人工充值列表-", "utf-8") + format.format(new Date()) + ".xls");
			xls.exportXls(response);
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
	}
	
	@RequestMapping("ssadmin/goVirtualOperationLogJSP")
	public ModelAndView goVirtualOperationLogJSP(HttpServletRequest request) throws Exception{
		String url = request.getParameter("url");
		ModelAndView modelAndView = new ModelAndView() ;
		if(request.getParameter("uid") != null){
			int fid = Integer.parseInt(request.getParameter("uid"));
			Fvirtualoperationlog virtualoperationlog = this.virtualOperationLogService.findById(fid);
			modelAndView.addObject("virtualoperationlog", virtualoperationlog);
		}
		List<Fvirtualcointype> allType = this.virtualCoinService.findAll();
		modelAndView.addObject("allType", allType);
		modelAndView.setViewName(url);
		return modelAndView;
	}
	
	@RequestMapping("ssadmin/saveVirtualOperationLog")
	@RequiresPermissions("ssadmin/saveVirtualOperationLog.html")
	public ModelAndView saveVirtualOperationLog(HttpServletRequest request) throws Exception{
		Fvirtualoperationlog virtualoperationlog = new Fvirtualoperationlog();
		int userId = Integer.parseInt(request.getParameter("userLookup.id"));
		Fuser user = this.userService.findById(userId);
		int vid = Integer.parseInt(request.getParameter("vid"));
		Fvirtualcointype coinType = this.virtualCoinService.findById(vid);
		Double fqty = Double.valueOf(request.getParameter("fqty"));
		virtualoperationlog.setFqty(fqty);
		virtualoperationlog.setFvirtualcointype(coinType);
		virtualoperationlog.setFuser(user);
		virtualoperationlog.setFstatus(OperationlogEnum.SAVE);
		if(request.getParameter("fisSendMsg") != null){
			virtualoperationlog.setFisSendMsg(1);
		}else{
			virtualoperationlog.setFisSendMsg(0);
		}
		this.virtualOperationLogService.saveObj(virtualoperationlog);
		
		ModelAndView modelAndView = new ModelAndView() ;
		modelAndView.setViewName("ssadmin/comm/ajaxDone") ;
		modelAndView.addObject("statusCode",200);
		modelAndView.addObject("message","新增成功");
		modelAndView.addObject("callbackType","closeCurrent");
		return modelAndView;
	}
	
	@RequestMapping("ssadmin/deleteVirtualOperationLog")
	@RequiresPermissions("ssadmin/deleteVirtualOperationLog.html")
	public ModelAndView deleteVirtualOperationLog(HttpServletRequest request) throws Exception{
		ModelAndView modelAndView = new ModelAndView() ;
		int fid = Integer.parseInt(request.getParameter("uid"));
		Fvirtualoperationlog virtualoperationlog = this.virtualOperationLogService.findById(fid);
		if(virtualoperationlog.getFstatus() == OperationlogEnum.AUDIT){
			modelAndView.setViewName("ssadmin/comm/ajaxDone") ;
			modelAndView.addObject("statusCode",300);
			modelAndView.addObject("message","删除失败，记录已审核");
			return modelAndView;
		}
		
		this.virtualOperationLogService.deleteObj(fid);
		modelAndView.setViewName("ssadmin/comm/ajaxDone") ;
		modelAndView.addObject("statusCode",200);
		modelAndView.addObject("message","删除成功");
		return modelAndView;
	}
	
	@RequestMapping("ssadmin/auditVirtualOperationLog")
	@RequiresPermissions("ssadmin/auditVirtualOperationLog.html")
	public ModelAndView auditVirtualOperationLog(HttpServletRequest request) throws Exception{
		ModelAndView modelAndView = new ModelAndView() ;
		int fid = Integer.parseInt(request.getParameter("uid"));
		boolean flag = false;
		Fvirtualoperationlog virtualoperationlog = this.virtualOperationLogService.findById(fid);
		
		if(virtualoperationlog.getFstatus() != OperationlogEnum.SAVE){
			modelAndView.setViewName("ssadmin/comm/ajaxDone") ;
			modelAndView.addObject("statusCode",300);
			modelAndView.addObject("message","已审核，不允许重复审核");
			return modelAndView;
		}

		try {
			double qty = virtualoperationlog.getFqty();
			int coinTypeId = virtualoperationlog.getFvirtualcointype().getFid();
			int userId = virtualoperationlog.getFuser().getFid();
			String sql =  "where fvirtualcointype.fid="+coinTypeId+"and fuser.fid="+userId;
			List<Fvirtualwallet> all = this.virtualWalletService.list(0, 0,sql, false);
			if(all != null && all.size() == 1){
				Fvirtualwallet virtualwallet = all.get(0);
				virtualwallet.setFtotal(MathUtils.add(virtualwallet.getFtotal(), qty));
				
				Fadmin sessionAdmin = (Fadmin)request.getSession().getAttribute("login_admin");
				virtualoperationlog.setFstatus(OperationlogEnum.AUDIT);
				virtualoperationlog.setFcreator(sessionAdmin);
				virtualoperationlog.setFcreateTime(Utils.getTimestamp());
				this.virtualOperationLogService.updateVirtualOperationLog(virtualwallet,virtualoperationlog);
			}else{
				modelAndView.setViewName("ssadmin/comm/ajaxDone") ;
				modelAndView.addObject("statusCode",300);
				modelAndView.addObject("message","会员钱包有误");
				return modelAndView;
			}
			flag = true;
		} catch (Exception e) {
			flag = false;
		}
		
		if(!flag){
			modelAndView.setViewName("ssadmin/comm/ajaxDone") ;
			modelAndView.addObject("statusCode",300);
			modelAndView.addObject("message","审核失败");
			return modelAndView;
		}
		modelAndView.setViewName("ssadmin/comm/ajaxDone") ;
		modelAndView.addObject("statusCode",200);
		modelAndView.addObject("message","审核成功");
		return modelAndView;
	}
}

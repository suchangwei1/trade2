package com.trade.controller.admin;

import com.trade.Enum.CountLimitTypeEnum;

import com.trade.model.Fcountlimit;
import com.trade.service.admin.AdminService;
import com.trade.service.admin.CountLimitService;
import com.trade.util.Constants;
import com.trade.util.Utils;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@Controller
public class CountLimitController extends BaseController {
	@Autowired
	private CountLimitService countLimitService;
	@Autowired
	private AdminService adminService ;
	//每页显示多少条数据
	private int numPerPage = Utils.getNumPerPage();
	
	@RequestMapping("/ssadmin/countLimitList")
	@RequiresPermissions("ssadmin/countLimitList.html")
	public ModelAndView Index(HttpServletRequest request) throws Exception{
		ModelAndView modelAndView = new ModelAndView() ;
		modelAndView.setViewName("ssadmin/countLimitList") ;
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
		filter.append("and ((ftype="+CountLimitTypeEnum.AdminLogin+" and fcount >= "+ Constants.ErrorCountAdminLimit+") \n");
		filter.append("or (ftype <>"+CountLimitTypeEnum.AdminLogin+" and fcount >= "+Constants.ErrorCountLimit+")) \n");
		filter.append("and (unix_timestamp(now())-unix_timestamp(fcreatetime)) <2*60*60 \n");
		if(keyWord != null && keyWord.trim().length() >0){
			filter.append("and fip like '%"+keyWord+"%' \n");
			modelAndView.addObject("keywords", keyWord);
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
		
		List<Fcountlimit> list = this.countLimitService.list((currentPage-1)*numPerPage, numPerPage, filter+"",true);
		modelAndView.addObject("countLimitList", list);
		modelAndView.addObject("numPerPage", numPerPage);
		modelAndView.addObject("currentPage", currentPage);
		modelAndView.addObject("rel", "countLimitList");
		//总数量
		modelAndView.addObject("totalCount",this.adminService.getAllCount("Fcountlimit", filter+""));
		return modelAndView ;
	}
	
	
	@RequestMapping("ssadmin/deleteCountLimit")
	@RequiresPermissions("ssadmin/deleteCountLimit.html")
	public ModelAndView deleteCountLimit(HttpServletRequest request) throws Exception{
		ModelAndView modelAndView = new ModelAndView();
        int fid = Integer.parseInt(request.getParameter("uid"));
        this.countLimitService.deleteObj(fid);
        
		modelAndView.setViewName("ssadmin/comm/ajaxDone") ;
		modelAndView.addObject("statusCode",200);
		modelAndView.addObject("message","删除成功");
		return modelAndView;
	}
	
}

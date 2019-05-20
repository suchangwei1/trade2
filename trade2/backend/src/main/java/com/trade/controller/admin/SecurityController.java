package com.trade.controller.admin;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import com.alibaba.fastjson.JSON;

import com.trade.model.Fsecurity;
import com.trade.service.admin.AdminService;
import com.trade.service.admin.SecurityService;
import com.trade.util.Utils;

@Controller
public class SecurityController extends BaseController {
	@Autowired
	private SecurityService securityService;
	@Autowired
	private AdminService adminService ;
	//每页显示多少条数据
	private int numPerPage = Utils.getNumPerPage();
	
	@RequestMapping("ssadmin/goSecurityJSP")
	public ModelAndView goSecurityJSP(HttpServletRequest request) throws Exception{
		String url = request.getParameter("url");
		ModelAndView modelAndView = new ModelAndView() ;
		modelAndView.setViewName(url) ;
		if(request.getParameter("treeId") != null){
			int fid = Integer.parseInt(request.getParameter("treeId"));
			Fsecurity security = this.securityService.findById(fid);
			modelAndView.addObject("security", security);
			modelAndView.addObject("treeId", fid);
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
				filter.append("and fname like '%"+keyWord+"%' \n");
				modelAndView.addObject("keywords", keyWord);
			}
			filter.append("and fsecurity.fid="+fid+" \n");
			if(orderField != null && orderField.trim().length() >0){
				filter.append("order by "+orderField+"\n");
			}
			if(orderDirection != null && orderDirection.trim().length() >0){
				filter.append(orderDirection+"\n");
			}
			List<Fsecurity> list = this.securityService.list((currentPage-1)*numPerPage, numPerPage, filter+"",true);
			modelAndView.addObject("securityList", list);
			modelAndView.addObject("numPerPage", numPerPage);
			modelAndView.addObject("currentPage", currentPage);
			//总数量
			modelAndView.addObject("totalCount",this.adminService.getAllCount("Fsecurity", filter+""));
			
			List<Object> ret = new ArrayList<>();
			permissionTree(security, ret);
			modelAndView.addObject("perms", JSON.toJSONString(ret));
		}else if(request.getParameter("treeId1") != null){//add
			if(request.getParameter("status").equals("update")){
				int uid = Integer.parseInt(request.getParameter("uid"));
				Fsecurity oldSecurity = this.securityService.findById(uid);
				modelAndView.addObject("oldSecurity", oldSecurity);
			}
		}
		return modelAndView;
	}
	
	private void permissionTree(Fsecurity security, List<Object> ret){
		Map<String, Object> nodes = new HashMap<>(10);
		nodes.put("id", security.getFid());
		nodes.put("pId", null == security.getFsecurity() ? 0 : security.getFsecurity().getFid());
		nodes.put("name", security.getFname());
		nodes.put("url", "ssadmin/goSecurityJSP.html?url=ssadmin/securityList1&treeId=" + security.getFid());
		nodes.put("target", "ajax");
		nodes.put("rel", "jbsxBox2moduleList");
		ret.add(nodes);
		
		for(Fsecurity sub : security.getFsecurities()){
			permissionTree(sub, ret);
		}
	}
	
	@RequestMapping("ssadmin/saveSecurity")
	public ModelAndView saveSecurity(HttpServletRequest request) throws Exception{
		ModelAndView modelAndView = new ModelAndView() ;
		int fparentid = Integer.parseInt(request.getParameter("fparentid"));
		int fpriority = Integer.parseInt(request.getParameter("fpriority"));
		Fsecurity security = new Fsecurity();
		Fsecurity parent = this.securityService.findById(fparentid);
		security.setFsecurity(parent);
		security.setFname(request.getParameter("fname"));
		security.setFdescription(request.getParameter("fdescription"));
		security.setFpriority(fpriority+1);
		security.setFurl(request.getParameter("furl"));
		this.securityService.saveObj(security);
		
		modelAndView.setViewName("ssadmin/comm/ajaxDone");
		modelAndView.addObject("statusCode", 200);
		modelAndView.addObject("message", "新增成功");
		modelAndView.addObject("rel", "jbsxBox2moduleList");
		modelAndView.addObject("callbackType", "closeCurrent");
		return modelAndView;
	}
	
	@RequestMapping("ssadmin/updateSecurity")
	@RequiresPermissions("ssadmin/updateSecurity.html")
	public ModelAndView updateSecurity(HttpServletRequest request) throws Exception{
		ModelAndView modelAndView = new ModelAndView() ;
		int fid = Integer.parseInt(request.getParameter("fid"));
		Fsecurity security = this.securityService.findById(fid);
		security.setFname(request.getParameter("fname"));
		security.setFdescription(request.getParameter("fdescription"));
		security.setFurl(request.getParameter("furl"));
		this.securityService.updateObj(security);
		
		modelAndView.setViewName("ssadmin/comm/ajaxDone");
		modelAndView.addObject("statusCode", 200);
		modelAndView.addObject("message", "修改成功");
		modelAndView.addObject("rel", "jbsxBox2moduleList");
		modelAndView.addObject("callbackType", "closeCurrent");
		return modelAndView;
	}
	
	@RequestMapping("ssadmin/deleteSecurity")
	@RequiresPermissions("ssadmin/deleteSecurity.html")
	public ModelAndView deleteSecurity(HttpServletRequest request) throws Exception {
		int fid = Integer.parseInt(request.getParameter("uid"));
		this.securityService.deleteObj(fid);

		ModelAndView modelAndView = new ModelAndView();
		modelAndView.setViewName("ssadmin/comm/ajaxDone");
		modelAndView.addObject("statusCode", 200);
		modelAndView.addObject("rel", "jbsxBox2moduleList");
		modelAndView.addObject("message", "删除成功");
		return modelAndView;
	}
	
}

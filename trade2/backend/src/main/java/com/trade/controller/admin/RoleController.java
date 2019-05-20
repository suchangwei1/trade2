package com.trade.controller.admin;


import com.trade.model.Fadmin;
import com.trade.model.Frole;
import com.trade.model.FroleSecurity;
import com.trade.model.Fsecurity;
import com.trade.service.admin.AdminService;
import com.trade.service.admin.RoleSecurityService;
import com.trade.service.admin.RoleService;
import com.trade.service.admin.SecurityService;
import com.trade.util.Utils;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@Controller
public class RoleController extends BaseController {
	@Autowired
	private RoleService roleService;
	@Autowired
	private SecurityService securityService;
	@Autowired
	private RoleSecurityService roleSecurityService;
	@Autowired
	private AdminService adminService ;
	//每页显示多少条数据
	private int numPerPage = Utils.getNumPerPage();
	
	@RequestMapping("/ssadmin/roleList")
	@RequiresPermissions("ssadmin/roleList.html")
	public ModelAndView roleList(HttpServletRequest request) throws Exception{
		ModelAndView modelAndView = new ModelAndView() ;
		modelAndView.setViewName("ssadmin/roleList") ;
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
		List<Frole> list = this.roleService.list((currentPage-1)*numPerPage, numPerPage, filter+"",true);
		modelAndView.addObject("roleList", list);
		modelAndView.addObject("numPerPage", numPerPage);
		modelAndView.addObject("currentPage", currentPage);
		modelAndView.addObject("rel", "roleList");
		//总数量
		modelAndView.addObject("totalCount",this.adminService.getAllCount("Frole", filter+""));
		return modelAndView ;
	}
	
	@RequestMapping("ssadmin/goRoleJSP")
	public ModelAndView goRoleJSP(HttpServletRequest request) throws Exception{
		String url = request.getParameter("url");
		ModelAndView modelAndView = new ModelAndView() ;
		modelAndView.setViewName(url) ;
		if(request.getParameter("uid") != null){
			int fid = Integer.parseInt(request.getParameter("uid"));
			Fsecurity security = this.securityService.findById(fid);
			modelAndView.addObject("security", security);
		}
		if(request.getParameter("roleId") != null){
			int roleId = Integer.parseInt(request.getParameter("roleId"));
			Frole role = this.roleService.findById(roleId);
			modelAndView.addObject("role", role);
		}
		return modelAndView;
	}
	
	@RequestMapping("ssadmin/saveRole")
	@RequiresPermissions("ssadmin/saveRole.html")
	public ModelAndView saveRole(HttpServletRequest request) throws Exception{
		ModelAndView modelAndView = new ModelAndView() ;
		List<Fsecurity> all = this.securityService.findAll();
		Frole role = new Frole();
		role.setFdescription(request.getParameter("fdescription"));
		role.setFname(request.getParameter("fname"));
		this.roleService.saveObj(role);
		
		for (Fsecurity fsecurity : all) {
			int fid = fsecurity.getFid();
			String key = "role["+fid+"]";
			String value = request.getParameter(key);
			if(value != null){
				FroleSecurity roleSecurity = new FroleSecurity();
				roleSecurity.setFrole(role);
				roleSecurity.setFsecurity(fsecurity);
				this.roleSecurityService.saveObj(roleSecurity);
			}
		}

		modelAndView.setViewName("ssadmin/comm/ajaxDone") ;
		modelAndView.addObject("statusCode",200);
		modelAndView.addObject("message","保存成功");
		modelAndView.addObject("callbackType","closeCurrent");
		return modelAndView;
	}
	
	@RequestMapping("ssadmin/updateRole")
	@RequiresPermissions("ssadmin/updateRole.html")
	public ModelAndView updateRole(HttpServletRequest request) throws Exception{
		ModelAndView modelAndView = new ModelAndView() ;
		List<Fsecurity> all = this.securityService.findAll();
		int roleId = Integer.parseInt(request.getParameter("roleId"));
		Frole role = this.roleService.findById(roleId);
		role.setFdescription(request.getParameter("fdescription"));
		role.setFname(request.getParameter("fname"));
		this.roleService.updateObj(role);
		
		this.roleSecurityService.deleteByRoleId(roleId);
		for (Fsecurity fsecurity : all) {
			int fid = fsecurity.getFid();
			String key = "role["+fid+"]";
			String value = request.getParameter(key);
			if(value != null){
				FroleSecurity roleSecurity = new FroleSecurity();
				roleSecurity.setFrole(role);
				roleSecurity.setFsecurity(fsecurity);
				this.roleSecurityService.saveObj(roleSecurity);
			}
		}

		// 更新权限缓存
		Fadmin fadmin = (Fadmin) SecurityUtils.getSubject().getSession().getAttribute("login_admin");
		fadmin = adminService.findById(fadmin.getFid());
		if(role.getFname().equals(fadmin.getFrole().getFname())){
			// 删除缓存
			SecurityUtils.getSubject().getSession().removeAttribute("permissions");
		}

		modelAndView.setViewName("ssadmin/comm/ajaxDone") ;
		modelAndView.addObject("statusCode",200);
		modelAndView.addObject("message","更新成功");
		modelAndView.addObject("callbackType","closeCurrent");
		return modelAndView;
	}
	
}

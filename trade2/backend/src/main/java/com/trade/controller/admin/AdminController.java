package com.trade.controller.admin;

import com.trade.Enum.AdminStatusEnum;
import com.trade.comm.ParamArray;

import com.trade.model.Fadmin;
import com.trade.model.Frole;
import com.trade.service.admin.AdminService;
import com.trade.service.admin.RoleService;
import com.trade.util.CollectionUtils;
import com.trade.util.Constants;
import com.trade.util.StringUtils;
import com.trade.util.Utils;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import java.security.MessageDigest;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
public class AdminController extends BaseController {
	@Autowired
	private AdminService adminService ;
	@Autowired
	private RoleService roleService;
	//每页显示多少条数据
	private int numPerPage = Utils.getNumPerPage();
	
	@RequestMapping("/ssadmin/adminList")
	@RequiresPermissions("ssadmin/adminList.html")
	public ModelAndView Index(HttpServletRequest request) throws Exception{
		ModelAndView modelAndView = new ModelAndView() ;
		modelAndView.setViewName("ssadmin/adminList") ;
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
			filter.append("order by fcreateTime \n");
		}
		
		if(orderDirection != null && orderDirection.trim().length() >0){
			filter.append(orderDirection+"\n");
		}else{
			filter.append("desc \n");
		}
		List<Fadmin> list = this.adminService.list((currentPage-1)*numPerPage, numPerPage, filter+"",true);
		modelAndView.addObject("adminList", list);
		modelAndView.addObject("numPerPage", numPerPage);
		modelAndView.addObject("currentPage", currentPage);
		modelAndView.addObject("rel", "adminList");
		//总数量
		modelAndView.addObject("totalCount",this.adminService.getAllCount("Fadmin", filter+""));
		return modelAndView ;
	}
	
	@RequestMapping("ssadmin/goAdminJSP")
	public ModelAndView goAdminJSP(HttpServletRequest request) throws Exception{
		String url = request.getParameter("url");
		ModelAndView modelAndView = new ModelAndView() ;
		modelAndView.setViewName(url) ;
		if(request.getParameter("uid") != null){
			int fid = Integer.parseInt(request.getParameter("uid"));
			Fadmin admin = this.adminService.findById(fid);
			modelAndView.addObject("fadmin", admin);
		}
		if(request.getSession().getAttribute("login_admin") != null){
			Fadmin admin = (Fadmin)request.getSession().getAttribute("login_admin");
			modelAndView.addObject("login_admin", admin);
		}
		
		List<Frole> roleList = this.roleService.findAll();
		Map map = new HashMap();
		for (Frole frole : roleList) {
			map.put(frole.getFid(),frole.getFname());
		}
		modelAndView.addObject("roleMap", map);
		return modelAndView;
	}
	
	@RequestMapping("ssadmin/saveAdmin")
	@RequiresPermissions("ssadmin/saveAdmin.html")
	public ModelAndView saveAdmin(HttpServletRequest request, ParamArray param) throws Exception{
		ModelAndView modelAndView = new ModelAndView() ;
		Fadmin fadmin = param.getFadmin() ;
		String fname = fadmin.getFname();

		List<Fadmin> all = this.adminService.findByProperty("fname", fname);
		if(all != null && all.size() >0){
			modelAndView.setViewName("ssadmin/comm/ajaxDone") ;
			modelAndView.addObject("statusCode",300);
			modelAndView.addObject("message","管理员登陆名已存在！");
			return modelAndView;
		}

		if(!StringUtils.isMobile(fadmin.getMobile())){
			modelAndView.setViewName("ssadmin/comm/ajaxDone") ;
			modelAndView.addObject("statusCode",300);
			modelAndView.addObject("message","手机号不正确！");
			return modelAndView;
		}

		all = this.adminService.findByProperty("mobile", fadmin.getMobile());
		if(all != null && all.size() >0){
			modelAndView.setViewName("ssadmin/comm/ajaxDone") ;
			modelAndView.addObject("statusCode",300);
			modelAndView.addObject("message","手机号已存在！");
			return modelAndView;
		}

		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:sss");
		String dateString = sdf.format(new java.util.Date());
		fadmin.setFcreateTime(Timestamp.valueOf(dateString));
		String passWord = fadmin.getFpassword();
		fadmin.setFpassword(Utils.MD5(passWord));
		fadmin.setFstatus(AdminStatusEnum.NORMAL_VALUE);
		
		int roleId = Integer.parseInt(request.getParameter("frole.fid"));
		Frole role = this.roleService.findById(roleId);
		fadmin.setFrole(role);
		
		this.adminService.saveObj(fadmin);	
		
		modelAndView.setViewName("ssadmin/comm/ajaxDone") ;
		modelAndView.addObject("statusCode",200);
		modelAndView.addObject("message","新增成功");
		modelAndView.addObject("callbackType","closeCurrent");
		return modelAndView;
	}
	
	@RequestMapping("ssadmin/forbbinAdmin")
	@RequiresPermissions({"ssadmin/forbbinAdmin.html?status=1", "ssadmin/forbbinAdmin.html?status=2"})
	public ModelAndView forbbinAdmin(HttpServletRequest request) throws Exception{
		ModelAndView modelAndView = new ModelAndView() ;
		int fid = Integer.parseInt(request.getParameter("uid"));
		if(request.getSession().getAttribute("login_admin") != null){
			Fadmin sessionAdmin = (Fadmin)request.getSession().getAttribute("login_admin");
			if(fid == sessionAdmin.getFid()){
				modelAndView.setViewName("ssadmin/comm/ajaxDone") ;
				modelAndView.addObject("statusCode",300);
				modelAndView.addObject("message","不允许禁用当前登陆的管理员！");
				return modelAndView;
			}
		}
		Fadmin admin = this.adminService.findById(fid);
		int status = Integer.parseInt(request.getParameter("status"));
		if(status == 1){
			admin.setFstatus(AdminStatusEnum.FORBBIN_VALUE);
		}else if (status == 2){
			admin.setFstatus(AdminStatusEnum.NORMAL_VALUE);
		}
		this.adminService.updateObj(admin);
		
		modelAndView.setViewName("ssadmin/comm/ajaxDone") ;
		modelAndView.addObject("statusCode",200);
		if(status == 1){
			modelAndView.addObject("message","禁用成功");	
		}else if(status == 1){
			modelAndView.addObject("message","解除禁用成功");	
		}
		return modelAndView;
	}
	
	@RequestMapping("ssadmin/updateAdmin")
	@RequiresPermissions("ssadmin/updateAdmin.html")
	public ModelAndView updateAdmin(HttpServletRequest request) throws Exception{
		ModelAndView modelAndView = new ModelAndView() ;
		int fid = Integer.parseInt(request.getParameter("fadmin.fid"));
		Fadmin fadmin = this.adminService.findById(fid);
		if(request.getSession().getAttribute("login_admin") != null){
			Fadmin sessionAdmin = (Fadmin)request.getSession().getAttribute("login_admin");
			if(fid == sessionAdmin.getFid()){
				modelAndView.setViewName("ssadmin/comm/ajaxDone") ;
				modelAndView.addObject("statusCode",300);
				modelAndView.addObject("message","不允许修改当前登陆的管理员密码！");
				return modelAndView;
			}
		}
		String passWord = request.getParameter("fadmin.fpassword");
		fadmin.setFpassword(Utils.MD5(passWord));
	
		this.adminService.updateObj(fadmin);
		
		modelAndView.setViewName("ssadmin/comm/ajaxDone") ;
		modelAndView.addObject("statusCode",200);
		modelAndView.addObject("message","修改密码成功");
		modelAndView.addObject("callbackType","closeCurrent");
		return modelAndView;
	}
	
	@RequestMapping("ssadmin/updateAdminPassword")
	public ModelAndView updateAdminPassword(HttpServletRequest request) throws Exception{
		MessageDigest md5 = MessageDigest.getInstance("MD5");
		ModelAndView modelAndView = new ModelAndView() ;
		int fid = Integer.parseInt(request.getParameter("fadmin.fid"));
		Fadmin fadmin = this.adminService.findById(fid);
		String truePassword = fadmin.getFpassword();
		String newPassWord = request.getParameter("fadmin.fpassword");
		String oldPassword = request.getParameter("oldPassword");
		String newPasswordMD5 = Utils.MD5(newPassWord);
		String oldPasswordMD5 = Utils.MD5(oldPassword);
        if(!truePassword.equals(oldPasswordMD5)){
    		modelAndView.setViewName("ssadmin/comm/ajaxDone") ;
    		modelAndView.addObject("statusCode",300);
    		modelAndView.addObject("message","原密码输入有误，请重新输入");
    		return modelAndView;
        }
        fadmin.setFpassword(newPasswordMD5);
		this.adminService.updateObj(fadmin);
		modelAndView.setViewName("ssadmin/comm/ajaxDone") ;
		modelAndView.addObject("statusCode",200);
		modelAndView.addObject("message","修改密码成功");
		modelAndView.addObject("callbackType","closeCurrent");
		return modelAndView;
	}
	
	@RequestMapping("ssadmin/backDatabase")
	@RequiresPermissions("ssadmin/backDatabase.html")
	public ModelAndView backDatabase(HttpServletRequest request) throws Exception{
		ModelAndView modelAndView = new ModelAndView() ;
		String password = request.getParameter("fpassword");
		String name = request.getParameter("fname");
		String dataBase = request.getParameter("dataBase");
		String port = request.getParameter("fport");
		String ip = request.getParameter("fip");
		String dir = getRequest().getSession().getServletContext().getRealPath("/")+ Constants.DataBaseDirectory;
		String fileName = Utils.getRandomImageName()+".sql";
		boolean flag = false;
        try {
			flag = this.adminService.updateDatabase(ip,port,dataBase,name,password,dir,fileName);
		} catch (Exception e) {
			flag = false;
		}
		if(!flag){
			modelAndView.setViewName("ssadmin/comm/ajaxDone") ;
			modelAndView.addObject("statusCode",300);
			modelAndView.addObject("message","备份失败！");
			return modelAndView;
		}
		modelAndView.setViewName("ssadmin/comm/ajaxDone") ;
		modelAndView.addObject("statusCode",200);
		modelAndView.addObject("message","备份成功,文件名称:"+fileName);
		modelAndView.addObject("callbackType","closeCurrent");
		return modelAndView;
	}

	@RequestMapping("ssadmin/loginOut")
	public ModelAndView loginOut() throws Exception{
		ModelAndView modelAndView = new ModelAndView() ;
		Subject admin = SecurityUtils.getSubject();
		admin.getSession().removeAttribute("permissions");
		admin.logout();
		modelAndView.setViewName("ssadmin/login");
		return modelAndView;
	}
	
	
	@RequestMapping("ssadmin/updateAdminRole")
	@RequiresPermissions("ssadmin/updateAdminRole.html")
	public ModelAndView updateAdminRole(HttpServletRequest request) throws Exception{
		ModelAndView modelAndView = new ModelAndView() ;
		int fid = Integer.parseInt(request.getParameter("fadmin.fid"));
		Fadmin fadmin = this.adminService.findById(fid);
		if(fadmin.getFname().equals("admin")){
			modelAndView.setViewName("ssadmin/comm/ajaxDone") ;
			modelAndView.addObject("statusCode",300);
			modelAndView.addObject("message","超级管理员角色不允许修改！");
			return modelAndView;
		}
		
		int roleId = Integer.parseInt(request.getParameter("frole.fid"));
		Frole role = this.roleService.findById(roleId);
		fadmin.setFrole(role);
		this.adminService.updateObj(fadmin);
		
		modelAndView.setViewName("ssadmin/comm/ajaxDone") ;
		modelAndView.addObject("statusCode",200);
		modelAndView.addObject("message","修改成功");
		modelAndView.addObject("callbackType","closeCurrent");
		return modelAndView;
	}

	@RequestMapping("ssadmin/updateAdminMobile")
	public ModelAndView updateAdminMobile(HttpServletRequest request) throws Exception{
		ModelAndView modelAndView = new ModelAndView() ;
		String mobile = request.getParameter("fadmin.mobile");
		if(!StringUtils.isMobile(mobile)){
			modelAndView.setViewName("ssadmin/comm/ajaxDone") ;
			modelAndView.addObject("statusCode",300);
			modelAndView.addObject("message","请输入正确的手机号！");
			return modelAndView;
		}

		List list = this.adminService.findByProperty("mobile", mobile);
		if(!CollectionUtils.isEmpty(list)){
			modelAndView.setViewName("ssadmin/comm/ajaxDone") ;
			modelAndView.addObject("statusCode",300);
			modelAndView.addObject("message","手机号已存在！");
			return modelAndView;
		}

		int fid = Integer.valueOf(request.getParameter("fadmin.fid"));
		Fadmin fadmin = this.adminService.findById(fid);
		fadmin.setMobile(mobile);
		this.adminService.updateObj(fadmin);
		modelAndView.setViewName("ssadmin/comm/ajaxDone") ;
		modelAndView.addObject("statusCode",200);
		modelAndView.addObject("message","修改手机号成功");
		modelAndView.addObject("callbackType","closeCurrent");
		return modelAndView;
	}
}

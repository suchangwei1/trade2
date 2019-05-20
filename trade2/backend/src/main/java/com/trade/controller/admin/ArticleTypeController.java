package com.trade.controller.admin;


import com.trade.model.Farticle;
import com.trade.model.Farticletype;
import com.trade.service.admin.AdminService;
import com.trade.service.admin.ArticleService;
import com.trade.service.admin.ArticleTypeService;
import com.trade.util.Utils;

import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.List;

@Controller
public class ArticleTypeController extends BaseController {
	@Autowired
	private ArticleTypeService articleTypeService ;
	@Autowired
	private AdminService adminService ;
	@Autowired
	private ArticleService articleService;
	//每页显示多少条数据
	private int numPerPage = Utils.getNumPerPage();
	
	@RequestMapping("/ssadmin/articleTypeList")
	@RequiresPermissions("ssadmin/articleTypeList.html")
	public ModelAndView Index(HttpServletRequest request) throws Exception{
		ModelAndView modelAndView = new ModelAndView() ;
		modelAndView.setViewName("ssadmin/articleTypeList") ;
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
			filter.append("and (fname like '%"+keyWord+"%' or \n");
			filter.append("fkeywords like '%"+keyWord+"%' or \n");
			filter.append("fdescription like '%"+keyWord+"%' )\n");
			modelAndView.addObject("keywords", keyWord);
		}
		
		if(orderField != null && orderField.trim().length() >0){
			filter.append("order by "+orderField+"\n");
		}else{
			filter.append("order by flastCreateDate \n");
		}
		
		if(orderDirection != null && orderDirection.trim().length() >0){
			filter.append(orderDirection+"\n");
		}else{
			filter.append("desc \n");
		}
		List<Farticletype> list = this.articleTypeService.list((currentPage-1)*numPerPage, numPerPage, filter+"",true);
		modelAndView.addObject("articleTypeList", list);
		modelAndView.addObject("numPerPage", numPerPage);
		modelAndView.addObject("rel", "articleTypeList");
		modelAndView.addObject("currentPage", currentPage);
		//总数量
		modelAndView.addObject("totalCount",this.adminService.getAllCount("Farticletype", filter+""));
		return modelAndView ;
	}
	
	@RequestMapping("ssadmin/goArticleTypeJSP")
	public ModelAndView goAricleJSP(HttpServletRequest request) throws Exception{
		String url = request.getParameter("url");
		ModelAndView modelAndView = new ModelAndView() ;
		modelAndView.setViewName(url) ;
		if(request.getParameter("uid") != null){
			int fid = Integer.parseInt(request.getParameter("uid"));
			Farticletype articletype = this.articleTypeService.findById(fid);
			modelAndView.addObject("farticleType", articletype);
		}
		return modelAndView;
	}
	
	@RequestMapping("ssadmin/saveArticleType")
	@RequiresPermissions("ssadmin/saveArticleType.html")
	public ModelAndView saveArticleType(HttpServletRequest request) throws Exception{
        Farticletype articleType = new Farticletype();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:sss");
		String dateString = sdf.format(new java.util.Date());
		Timestamp tm = Timestamp.valueOf(dateString);
		articleType.setFlastCreateDate(tm);
		articleType.setFlastModifyDate(tm);
		articleType.setFname(request.getParameter("fname"));
		articleType.setFdescription(request.getParameter("fdescription"));
		articleType.setFkeywords(request.getParameter("fkeywords"));
		this.articleTypeService.saveObj(articleType);
		
		ModelAndView modelAndView = new ModelAndView() ;
		modelAndView.setViewName("ssadmin/comm/ajaxDone") ;
		modelAndView.addObject("statusCode",200);
		modelAndView.addObject("message","新增成功");
		modelAndView.addObject("callbackType","closeCurrent");
		return modelAndView;
	}
	
	@RequestMapping("ssadmin/updateArticleType")
	@RequiresPermissions("ssadmin/updateArticleType.html")
	public ModelAndView updateArticleType(HttpServletRequest request) throws Exception{
		int fid = Integer.parseInt(request.getParameter("fid"));
		String name = request.getParameter("fname");
        Farticletype articleType = this.articleTypeService.findById(fid);
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:sss");
		String dateString = sdf.format(new java.util.Date());
		Timestamp tm = Timestamp.valueOf(dateString);
		articleType.setFlastModifyDate(tm);
		articleType.setFname(name);
		articleType.setFdescription(request.getParameter("fdescription"));
		articleType.setFkeywords(request.getParameter("fkeywords"));
		this.articleTypeService.updateObj(articleType);
		
		ModelAndView modelAndView = new ModelAndView() ;
		modelAndView.setViewName("ssadmin/comm/ajaxDone") ;
		modelAndView.addObject("statusCode",200);
		modelAndView.addObject("message","更新成功");
		modelAndView.addObject("callbackType","closeCurrent");
		return modelAndView;
	}
	
	@RequestMapping("ssadmin/deleteArticleType")
	@RequiresPermissions("ssadmin/deleteArticleType.html")
	public ModelAndView deleteArticleType(HttpServletRequest request) throws Exception{
		int fid = Integer.parseInt(request.getParameter("uid"));
		ModelAndView modelAndView = new ModelAndView() ;
		List<Farticle> all = this.articleService.findByProperty("farticletype.fid", fid);
		if(all != null && all.size() >0){
			modelAndView.setViewName("ssadmin/comm/ajaxDone") ;
			modelAndView.addObject("statusCode",300);
			modelAndView.addObject("message","删除失败，该文章类型已被引用");
			return modelAndView;
		}
		this.articleTypeService.deleteObj(fid);
		modelAndView.setViewName("ssadmin/comm/ajaxDone") ;
		modelAndView.addObject("statusCode",200);
		modelAndView.addObject("message","删除成功");
		return modelAndView;
	}
	
	@RequestMapping("/ssadmin/articleTypeLookup")
	public ModelAndView forLookUp(HttpServletRequest request) throws Exception{
		ModelAndView modelAndView = new ModelAndView() ;
		modelAndView.setViewName("ssadmin/articleTypeLookup") ;
		//当前页
		int currentPage = 1;
		//搜索关键字
		String keyWord = request.getParameter("keywords");
		
		if(request.getParameter("pageNum") != null){
			currentPage = Integer.parseInt(request.getParameter("pageNum"));
		}
		StringBuffer filter = new StringBuffer();
		filter.append("where 1=1 \n");
		if(keyWord != null && keyWord.trim().length() >0){
			filter.append("and (fname like '%"+keyWord+"%' or \n");
			filter.append("fkeywords like '%"+keyWord+"%' or \n");
			filter.append("fdescription like '%"+keyWord+"%' )\n");
			modelAndView.addObject("keywords", keyWord);
		}
		List<Farticletype> list = this.articleTypeService.list((currentPage-1)*numPerPage, numPerPage, filter+"",true);
		modelAndView.addObject("articleTypeList", list);
		modelAndView.addObject("numPerPage", numPerPage);
		modelAndView.addObject("rel", "articleTypeList");
		modelAndView.addObject("currentPage", currentPage);
		//总数量
		modelAndView.addObject("totalCount",this.articleTypeService.list(0, 0, filter+"", false).size());
		return modelAndView ;
	}
	
}

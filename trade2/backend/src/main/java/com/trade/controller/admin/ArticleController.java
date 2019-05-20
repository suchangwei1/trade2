package com.trade.controller.admin;

import com.trade.comm.ParamArray;
import com.trade.model.Farticle;
import com.trade.model.Farticletype;
import com.trade.service.admin.AdminService;
import com.trade.service.admin.ArticleService;
import com.trade.service.admin.ArticleTypeService;
import com.trade.util.Constants;
import com.trade.util.StringUtils;
import com.trade.util.Utils;
import net.sf.json.JSONObject;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import java.io.InputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
public class ArticleController extends BaseController {
	@Autowired
	private ArticleService articleService ;
	@Autowired
	private AdminService adminService ;
	@Autowired
	private ArticleTypeService articleTypeService ;
	@Value("${oss.cdn}")
	private String cdn;
	//每页显示多少条数据
	private int numPerPage = Utils.getNumPerPage();
	
	@RequestMapping("/ssadmin/articleList")
	@RequiresPermissions("ssadmin/articleList.html")
	public ModelAndView Index(HttpServletRequest request) throws Exception{
		ModelAndView modelAndView = new ModelAndView() ;
		modelAndView.setViewName("ssadmin/articleList") ;
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
			keyWord=keyWord.trim();
			filter.append("and (fTitle like '%"+keyWord+"%' OR \n");
			filter.append(" enTitle like '%"+keyWord+"%' ) \n");
			modelAndView.addObject("keywords", keyWord);
		}
		if(StringUtils.hasText(request.getParameter("ftype"))){
			int type = Integer.parseInt(request.getParameter("ftype"));
			if(type != 0){
				filter.append("and farticletype.fid="+request.getParameter("ftype")+"\n");
			}
			modelAndView.addObject("ftype", request.getParameter("ftype"));
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
		
		Map typeMap = new HashMap();
		typeMap.put(0, "全部");
		List<Farticletype> all = this.articleTypeService.findAll();
		for (Farticletype farticletype : all) {
			typeMap.put(farticletype.getFid(), farticletype.getFname());
		}
		modelAndView.addObject("typeMap", typeMap);
		
		List<Farticle> list = this.articleService.list((currentPage-1)*numPerPage, numPerPage, filter+"",true);
		modelAndView.addObject("articleList", list);
		modelAndView.addObject("numPerPage", numPerPage);
		modelAndView.addObject("rel", "articleList");
		modelAndView.addObject("currentPage", currentPage);
		//总数量
		modelAndView.addObject("totalCount",this.adminService.getAllCount("Farticle", filter+""));
		return modelAndView ;
	}
	
	@RequestMapping("ssadmin/goArticleJSP")
	public ModelAndView goArticleJSP(HttpServletRequest request) throws Exception{
		String url = request.getParameter("url");
		ModelAndView modelAndView = new ModelAndView() ;
		modelAndView.setViewName(url) ;
		if(request.getParameter("uid") != null){
			int fid = Integer.parseInt(request.getParameter("uid"));
			Farticle article = this.articleService.findById(fid);
			modelAndView.addObject("farticle", article);
		}
		return modelAndView;
	}
	
	@RequestMapping(value="ssadmin/upload")
	@ResponseBody
	public String upload(HttpServletRequest request, ParamArray param) throws Exception{
		MultipartFile multipartFile = param.getFiledata() ;
		InputStream inputStream = multipartFile.getInputStream() ;
		String realName = multipartFile.getOriginalFilename() ;
		
		if(realName!=null && realName.trim().toLowerCase().endsWith("jsp")){
			return "" ;
		}
		
		String[] nameSplit = realName.split("\\.") ;
		String ext = nameSplit[nameSplit.length-1] ;
		String realPath = getRequest().getSession().getServletContext().getRealPath("/")+ Constants.AdminArticleDirectory;
		String fileName = Utils.getRandomImageName()+"."+ext;
		boolean flag = Utils.saveFile(realPath,fileName, inputStream) ;
		String result = "";
		if(!flag){
			result = "上传失败";
		}
		JSONObject resultJson = new JSONObject() ;
		resultJson.accumulate("err",result) ;
//		String path = request.getContextPath();
//		String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
		String basePath = cdn + "/";
		resultJson.accumulate("msg",basePath+Constants.AdminArticleDirectory+"/"+fileName) ;
		return resultJson.toString();
	}
	
	@RequestMapping("ssadmin/saveArticle")
	@RequiresPermissions("ssadmin/saveArticle.html")
	public ModelAndView saveArticle(
			@RequestParam("articleLookup.id") int articleTypeId,
			@RequestParam(required=false) String ftitle,
			@RequestParam(required=false) String en_title,
			@RequestParam(required=false) String fcontent,
			@RequestParam(required=false) String en_content,
			@RequestParam(required=false) String isTop
			) throws Exception{
		ModelAndView modelAndView = new ModelAndView() ;
		modelAndView.setViewName("ssadmin/comm/ajaxDone") ;
		Farticle article = new Farticle();
		Farticletype articletype = this.articleTypeService.findById(articleTypeId);
		article.setFarticletype(articletype);
		article.setFtitle(ftitle);
		article.setFcontent(fcontent);
		article.setEnTitle(en_title);
		article.setEnContent(en_content);
		article.setFlastModifyDate(Utils.getTimestamp());
		article.setFcreateDate(Utils.getTimestamp());
		if(StringUtils.hasText(isTop)){
			article.setTop(true);
		}
		this.articleService.saveObj(article);
		modelAndView.addObject("statusCode",200);
		modelAndView.addObject("message","新增成功");
		modelAndView.addObject("callbackType","closeCurrent");
		return modelAndView;
	}
	
	@RequestMapping("ssadmin/deleteArticle")
	@RequiresPermissions("ssadmin/deleteArticle.html")
	public ModelAndView deleteArticle(HttpServletRequest request) throws Exception{
		int fid = Integer.parseInt(request.getParameter("uid"));
		Farticle article = articleService.findById(fid);
		this.articleService.deleteObj(fid);
		
		ModelAndView modelAndView = new ModelAndView() ;
		modelAndView.setViewName("ssadmin/comm/ajaxDone") ;
		modelAndView.addObject("statusCode",200);
		modelAndView.addObject("message","删除成功");

		return modelAndView;
	}
	
	@RequestMapping("ssadmin/updateArticle")
	@RequiresPermissions("ssadmin/updateArticle.html")
	public ModelAndView updateArticle(
			@RequestParam("articleLookup.id") int articleTypeId,
			@RequestParam(required=false) String ftitle,
			@RequestParam(required=false) String en_title,
			@RequestParam(required=false) String fcontent,
			@RequestParam(required=false) String en_content,
			@RequestParam(required=false) String isTop,
			@RequestParam Integer fid
			) throws Exception{
		ModelAndView modelAndView = new ModelAndView() ;
		modelAndView.setViewName("ssadmin/comm/ajaxDone") ;
		Farticle article = this.articleService.findById(fid);
		Farticletype articletype = this.articleTypeService.findById(articleTypeId);
		article.setFarticletype(articletype);
		article.setFtitle(ftitle);
		article.setFcontent(fcontent);
		article.setEnTitle(en_title);
		article.setEnContent(en_content);
		article.setFlastModifyDate(Utils.getTimestamp());
		if(StringUtils.hasText(isTop)){
			article.setTop(true);
		}else{
			article.setTop(false);
		}
		this.articleService.updateObj(article);
		
		modelAndView.addObject("statusCode",200);
		modelAndView.addObject("message","修改成功");
		modelAndView.addObject("callbackType","closeCurrent");

		return modelAndView;
	}
}

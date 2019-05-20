package com.trade.controller.admin;

import com.trade.Enum.CcLogStatusEnum;
import com.trade.Enum.CcLogTypeEnum;
import com.trade.model.CcLog;
import com.trade.model.CcUser;
import com.trade.service.admin.AdminService;
import com.trade.service.admin.CcUserService;
import com.trade.service.front.CcLogService;
import com.trade.util.StringUtils;
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
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
public class CcController extends BaseController {

	@Autowired
	CcUserService ccUserService;
	@Autowired
	CcLogService ccLogService;
	@Autowired
	AdminService adminService;

	private int numPerPage = Utils.getNumPerPage();

	@RequestMapping("ssadmin/goCcJSP")
	public ModelAndView goLinkJSP(HttpServletRequest request) throws Exception{
		String url = request.getParameter("url");
		ModelAndView modelAndView = new ModelAndView() ;
		modelAndView.setViewName(url) ;
		return modelAndView;
	}

	@RequestMapping("ssadmin/ccList")
	@RequiresPermissions("ssadmin/cccheck.html")
	public ModelAndView ccList(HttpServletRequest request) throws Exception {
		ModelAndView modelAndView = new ModelAndView();
		modelAndView.setViewName("ssadmin/cc/ccList");
		int currentPage = 1;
		String keyWord = request.getParameter("keywords");
		String startDate = request.getParameter("startDate");
		String endDate = request.getParameter("endDate");
		String orderField = request.getParameter("orderField");
		String orderDirection = request.getParameter("orderDirection");
		if (request.getParameter("pageNum") != null) {
			currentPage = Integer.parseInt(request.getParameter("pageNum"));
		}
		StringBuffer filter = new StringBuffer();
		filter.append("where 1=1 \n");
		if (keyWord != null && keyWord.trim().length() > 0) {
			filter.append("and (buyyer.floginName like '%" + keyWord + "%' or \n");
			filter.append("buyyer.fid = '" + keyWord + "'  or \n");
			filter.append("buyyer.fnickName like '%" + keyWord + "%'  or \n");
			filter.append("buyyer.frealName like '%" + keyWord + "%'  or \n");
			filter.append("buyyer.ftelephone like '%" + keyWord + "%'  or \n");
			filter.append("buyyer.femail like '%" + keyWord + "%'  or \n");
			filter.append("buyyer.bankAccount like '%" + keyWord + "%'  or \n");
			filter.append("buyyer.fidentityNo like '%" + keyWord + "%' )\n");
			modelAndView.addObject("keywords", keyWord);
		}

		Map typeMap = new HashMap();
		typeMap.put(CcLogTypeEnum.BUY.getIndex(), CcLogTypeEnum.BUY.getName());
		typeMap.put(CcLogTypeEnum.SELL.getIndex(), CcLogTypeEnum.SELL.getName());
		modelAndView.addObject("typeMap", typeMap);
		Map statusMap = new HashMap();
		statusMap.put(CcLogStatusEnum.UN_CHECK.getIndex(), CcLogStatusEnum.UN_CHECK.getName());
		statusMap.put(CcLogStatusEnum.PASSED.getIndex(), CcLogStatusEnum.PASSED.getName());
		statusMap.put(CcLogStatusEnum.NO_PASSED.getIndex(), CcLogStatusEnum.NO_PASSED.getName());
		modelAndView.addObject("statusMap", statusMap);

		String type = request.getParameter("ftype");
		if (type != null
				&& type.trim().length() > 0) {
			filter.append("and type=" + Integer.parseInt(type)
					+ " \n");
			modelAndView.addObject("ftype", Integer.parseInt(type));
		}

		String status = request.getParameter("fstatus");
		if (status != null
				&& status.trim().length() > 0) {
			filter.append("and status=" + Integer.parseInt(status)
					+ " \n");
			modelAndView.addObject("fstatus", Integer.parseInt(status));
		}

		if(StringUtils.hasText(startDate)){
			filter.append("and createTime>=").append("'" + startDate + "' ");
			modelAndView.addObject("startDate", startDate);
		}
		if(StringUtils.hasText(endDate)){
			filter.append("and createTime<=").append("'" + endDate + "' ");
			modelAndView.addObject("endDate", endDate);
		}

		if (orderField != null && orderField.trim().length() > 0) {
			filter.append("order by " + orderField + "\n");
		} else {
			filter.append("order by id \n");
		}

		if (orderDirection != null && orderDirection.trim().length() > 0) {
			filter.append(orderDirection + "\n");
		} else {
			filter.append("desc \n");
		}

		List<CcLog> list = this.ccLogService.list(
				(currentPage - 1) * numPerPage, numPerPage, filter + "", true);
		modelAndView.addObject("userList", list);
		modelAndView.addObject("numPerPage", numPerPage);
		modelAndView.addObject("currentPage", currentPage);
		modelAndView.addObject("rel", "listCc");
		// 总数量
		modelAndView.addObject("totalCount",
				this.adminService.getAllCount("CcLog", filter + ""));
		return modelAndView;
	}

	private static enum ExportFiled {
		订单ID,类型,用户名,商户名,币种,数量,价格,状态,生成时间,更新时间
	}

	@RequestMapping("ssadmin/ccExport")
	@RequiresPermissions("ssadmin/cccheck.html")
	public void ccExport(HttpServletRequest request, HttpServletResponse response) throws Exception {
		ModelAndView modelAndView = new ModelAndView();
		response.setContentType("Application/excel");
		response.addHeader("Content-Disposition",
				"attachment;filename=userList.xls");
		XlsExport e = new XlsExport();
		int rowIndex = 0;

		// header
		e.createRow(rowIndex++);
		for (CcController.ExportFiled filed : CcController.ExportFiled.values()) {
			e.setCell(filed.ordinal(), filed.toString());
		}

		String keyWord = request.getParameter("keywords");
		String startDate = request.getParameter("startDate");
		String endDate = request.getParameter("endDate");
		String orderField = request.getParameter("orderField");
		String orderDirection = request.getParameter("orderDirection");
		StringBuffer filter = new StringBuffer();
		filter.append("where 1=1 \n");
		if (keyWord != null && keyWord.trim().length() > 0) {
			filter.append("and (buyyer.floginName like '%" + keyWord + "%' or \n");
			filter.append("buyyer.fid = '" + keyWord + "'  or \n");
			filter.append("buyyer.fnickName like '%" + keyWord + "%'  or \n");
			filter.append("buyyer.frealName like '%" + keyWord + "%'  or \n");
			filter.append("buyyer.ftelephone like '%" + keyWord + "%'  or \n");
			filter.append("buyyer.femail like '%" + keyWord + "%'  or \n");
			filter.append("buyyer.fidentityNo like '%" + keyWord + "%' )\n");
			modelAndView.addObject("keywords", keyWord);
		}

		String type = request.getParameter("ftype");
		if (type != null
				&& type.trim().length() > 0) {
			filter.append("and type=" + Integer.parseInt(type)
					+ " \n");
		}

		String status = request.getParameter("fstatus");
		if (status != null
				&& status.trim().length() > 0) {
			filter.append("and status=" + Integer.parseInt(status)
					+ " \n");
		}

		if(StringUtils.hasText(startDate)){
			filter.append("and createTime>=").append("'" + startDate + "' ");
		}
		if(StringUtils.hasText(endDate)){
			filter.append("and createTime<=").append("'" + endDate + "' ");
		}

		if (orderField != null && orderField.trim().length() > 0) {
			filter.append("order by " + orderField + "\n");
		} else {
			filter.append("order by id \n");
		}

		if (orderDirection != null && orderDirection.trim().length() > 0) {
			filter.append(orderDirection + "\n");
		} else {
			filter.append("desc \n");
		}

		List<CcLog> list = this.ccLogService.list(
				0, 0, filter + "", false);

		for (CcLog user : list) {
			e.createRow(rowIndex++);
			for (CcController.ExportFiled filed : CcController.ExportFiled.values()) {
				switch (filed) {
					case 订单ID:
						e.setCell(filed.ordinal(), user.getId());
						break;
					case 类型:
						e.setCell(filed.ordinal(), user.getType().getName());
						break;
					case 用户名:
						e.setCell(filed.ordinal(), user.getBuyyer().getFrealName());
						break;
					case 商户名:
						e.setCell(filed.ordinal(), user.getSeller().getName());
						break;
					case 币种:
						e.setCell(filed.ordinal(), user.getCoin().getfShortName());
						break;
					case 数量:
						e.setCell(filed.ordinal(), user.getAmount() + "");
						break;
					case 价格:
						e.setCell(filed.ordinal(), user.getPrice()+"");
						break;
					case 状态:
						e.setCell(filed.ordinal(), user.getStatus().getName());
						break;
					case 生成时间:
						e.setCell(filed.ordinal(), user.getCreateTime());
						break;
					case 更新时间:
						e.setCell(filed.ordinal(), user.getUpdateTime());
						break;
					default:
						break;
				}
			}
		}

		try {
			SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
			response.setContentType("application/vnd.ms-excel");
			response.setHeader("Content-Disposition", "attachment;filename=" + URLEncoder.encode("C2C记录表-", "utf-8") + format.format(new Date()) + ".xls");
			e.exportXls(response);
		} catch (UnsupportedEncodingException re) {
			re.printStackTrace();
		}
	}

	@RequestMapping("ssadmin/ccUserList")
	@RequiresPermissions("ssadmin/cccheck.html")
	public ModelAndView ccUserList(HttpServletRequest request) throws Exception {
		ModelAndView modelAndView = new ModelAndView();
		modelAndView.setViewName("ssadmin/cc/ccUserList");
		int currentPage = 1;
		String orderField = request.getParameter("orderField");
		String orderDirection = request.getParameter("orderDirection");
		if (request.getParameter("pageNum") != null) {
			currentPage = Integer.parseInt(request.getParameter("pageNum"));
		}
		StringBuffer filter = new StringBuffer();
		filter.append("where 1=1 \n");

		if (orderField != null && orderField.trim().length() > 0) {
			filter.append("order by " + orderField + "\n");
		} else {
			filter.append("order by id \n");
		}

		if (orderDirection != null && orderDirection.trim().length() > 0) {
			filter.append(orderDirection + "\n");
		} else {
			filter.append("desc \n");
		}

		List<CcUser> list = this.ccUserService.list(
				(currentPage - 1) * numPerPage, numPerPage, filter + "", true);
		modelAndView.addObject("userList", list);
		modelAndView.addObject("numPerPage", numPerPage);
		modelAndView.addObject("currentPage", currentPage);
		modelAndView.addObject("rel", "listCcUser");
		// 总数量
		modelAndView.addObject("totalCount",
				this.adminService.getAllCount("CcUser", filter + ""));
		return modelAndView;
	}

	@RequestMapping("ssadmin/ccUserSave")
	@RequiresPermissions("ssadmin/cccheck.html")
	public ModelAndView ccUserSave(HttpServletRequest request) throws Exception {
		CcUser user = new CcUser(
				request.getParameter("user.name"),
				request.getParameter("user.contactWay"),
				request.getParameter("user.branch"),
				request.getParameter("user.account")
		);
		this.ccUserService.saveObj(user);
		ModelAndView modelAndView = new ModelAndView() ;
		modelAndView.setViewName("ssadmin/comm/ajaxDone") ;
		modelAndView.addObject("statusCode",200);
		modelAndView.addObject("message","新增成功");
		modelAndView.addObject("callbackType","closeCurrent");
		return modelAndView;
	}

	@RequestMapping("ssadmin/ccUserDelet")
	@RequiresPermissions("ssadmin/cccheck.html")
	public ModelAndView ccUserDelet(HttpServletRequest request) throws Exception {
		int id = Integer.valueOf(request.getParameter("uid"));
		this.ccUserService.deleteObj(id);
		ModelAndView modelAndView = new ModelAndView() ;
		modelAndView.setViewName("ssadmin/comm/ajaxDone") ;
		modelAndView.addObject("statusCode",200);
		modelAndView.addObject("message","删除成功");
		return modelAndView;
	}

	@RequestMapping("ssadmin/ccUpdate")
	@RequiresPermissions("ssadmin/cccheck.html")
	public ModelAndView ccUpdate(HttpServletRequest request) throws Exception {
		ModelAndView modelAndView = new ModelAndView() ;
		modelAndView.setViewName("ssadmin/comm/ajaxDone") ;
		int uid = Integer.valueOf(request.getParameter("uid"));
		int status = Integer.valueOf(request.getParameter("status"));
		CcLog ccLog = ccLogService.findById(uid);
		if(ccLog.getStatus() != CcLogStatusEnum.UN_CHECK){
			modelAndView.addObject("statusCode",300);
			modelAndView.addObject("message","状态只有未处理的订单才能取消或通过");
			return modelAndView;
		}
		try{
			this.ccLogService.updateStatus(ccLog, status);
			modelAndView.addObject("statusCode",200);
			modelAndView.addObject("message","操作成功");
			return modelAndView;
		}catch (Exception e){
			e.printStackTrace();
			modelAndView.addObject("statusCode",300);
			modelAndView.addObject("message","用户资金异常");
			return modelAndView;
		}
	}
}

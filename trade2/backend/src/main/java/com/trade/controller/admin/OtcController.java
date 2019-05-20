package com.trade.controller.admin;

import com.trade.Enum.CcLogStatusEnum;
import com.trade.model.*;
import com.trade.service.admin.AdminService;
import com.trade.service.front.FrontVirtualCoinService;
import com.trade.service.front.OtcAccountService;
import com.trade.service.front.OtcOrderLogService;
import com.trade.service.front.OtcOrderService;
import com.trade.util.*;
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
public class OtcController extends BaseController {

	@Autowired
	AdminService adminService;
	@Autowired
	OtcOrderLogService otcOrderLogService;
	@Autowired
	OtcAccountService otcAccountService;
	@Autowired
	OtcOrderService otcOrderService;
	@Autowired
	FrontVirtualCoinService frontVirtualCoinService;

	private int numPerPage = Utils.getNumPerPage();

	/*----------以下是挂单----------*/
	@RequestMapping("ssadmin/otcOrderList")
	@RequiresPermissions("ssadmin/otccheck.html")
	public ModelAndView otcList(HttpServletRequest request) throws Exception {
		ModelAndView modelAndView = new ModelAndView();
		modelAndView.setViewName("ssadmin/otc/otcOrderList");
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
			filter.append("and (fuser.floginName like '%" + keyWord + "%' or \n");
			filter.append("fuser.fid = '" + keyWord + "'  or \n");
			filter.append("fuser.fnickName like '%" + keyWord + "%'  or \n");
			filter.append("fuser.frealName like '%" + keyWord + "%'  or \n");
			filter.append("fuser.ftelephone like '%" + keyWord + "%'  or \n");
			filter.append("fuser.femail like '%" + keyWord + "%'  or \n");
			filter.append("fuser.fidentityNo like '%" + keyWord + "%' )\n");
			modelAndView.addObject("keywords", keyWord);
		}
		String id = request.getParameter("id");
		if(StringUtils.hasText(id)){
			filter.append("and id=" + Integer.parseInt(id) + " \n");
			modelAndView.addObject("id", Integer.parseInt(id));
		}
		Map typeMap = new HashMap();
		typeMap.put(0, "买入");
		typeMap.put(1,"卖出");
		modelAndView.addObject("typeMap", typeMap);
		String type = request.getParameter("ftype");
		if (type != null
				&& type.trim().length() > 0) {
			filter.append("and type=" + Integer.parseInt(type)
					+ " \n");
			modelAndView.addObject("ftype", Integer.parseInt(type));
		}

		Map statusMap = new HashMap();
		statusMap.put(0,"待成交");
		statusMap.put(1,"已完成");
		statusMap.put(2,"已撤销");
		modelAndView.addObject("statusMap", statusMap);
		String status = request.getParameter("fstatus");
		if (status != null
				&& status.trim().length() > 0) {
			filter.append("and status=" + Integer.parseInt(status)
					+ " \n");
			modelAndView.addObject("fstatus", Integer.parseInt(status));
		}

		Map coinMap = new HashMap();
		List<Fvirtualcointype> coins = frontVirtualCoinService.findOtcCoin();
		for(Fvirtualcointype coin: coins){
			coinMap.put(coin.getFid(), coin.getfShortName());
		}
		modelAndView.addObject("coinMap", coinMap);
		String coin = request.getParameter("coin");
		if (coin != null
				&& coin.trim().length() > 0) {
			filter.append("and fvirtualcointype.fid=" + Integer.parseInt(coin)
					+ " \n");
			modelAndView.addObject("coin", Integer.parseInt(coin));
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

		List<OtcOrder> list = this.otcOrderService.list(
				(currentPage - 1) * numPerPage, numPerPage, filter + "", true);
		modelAndView.addObject("list", list);
		modelAndView.addObject("numPerPage", numPerPage);
		modelAndView.addObject("currentPage", currentPage);
		modelAndView.addObject("rel", "listOtc");
		// 总数量
		modelAndView.addObject("totalCount",
				this.adminService.getAllCount("OtcOrder", filter + ""));
		return modelAndView;
	}

	private static enum ExportFiled {
		订单ID,币种,挂单用户,类型,价格,数量,数量范围,已成交数量,冻结数量,状态,支付方式,生成时间,更新时间
	}

	@RequestMapping("ssadmin/otcOrderExport")
	@RequiresPermissions("ssadmin/otccheck.html")
	public void otcExport(HttpServletRequest request, HttpServletResponse response) throws Exception {
		ModelAndView modelAndView = new ModelAndView();
		response.setContentType("Application/excel");
		response.addHeader("Content-Disposition",
				"attachment;filename=otcOrder.xls");
		XlsExport e = new XlsExport();
		int rowIndex = 0;

		// header
		e.createRow(rowIndex++);
		for (OtcController.ExportFiled filed : OtcController.ExportFiled.values()) {
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
			filter.append("and (fuser.floginName like '%" + keyWord + "%' or \n");
			filter.append("fuser.fid = '" + keyWord + "'  or \n");
			filter.append("fuser.fnickName like '%" + keyWord + "%'  or \n");
			filter.append("fuser.frealName like '%" + keyWord + "%'  or \n");
			filter.append("fuser.ftelephone like '%" + keyWord + "%'  or \n");
			filter.append("fuser.femail like '%" + keyWord + "%'  or \n");
			filter.append("fuser.fidentityNo like '%" + keyWord + "%' )\n");
		}

		String id = request.getParameter("id");
		if(StringUtils.hasText(id)){
			filter.append("and id=" + Integer.parseInt(id) + " \n");
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

		String coin = request.getParameter("coin");
		if (coin != null
				&& coin.trim().length() > 0) {
			filter.append("and fvirtualcointype.fid=" + Integer.parseInt(coin)
					+ " \n");
			modelAndView.addObject("coin", Integer.parseInt(coin));
		}


		if(StringUtils.hasText(startDate)){
			filter.append("and createTime>=").append("'" + startDate + "' ");
			modelAndView.addObject("startDate", startDate);
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


		List<OtcOrder> list = this.otcOrderService.list(
				0, 0, filter + "", false);

		for (OtcOrder item : list) {
			e.createRow(rowIndex++);
			for (OtcController.ExportFiled filed : OtcController.ExportFiled.values()) {
				switch (filed) {
					case 订单ID:
						e.setCell(filed.ordinal(), item.getId());
						break;
					case 币种:
						e.setCell(filed.ordinal(), item.getFvirtualcointype().getfShortName());
						break;
					case 挂单用户:
						e.setCell(filed.ordinal(), item.getFuser().getFrealName());
						break;
					case 类型:
						e.setCell(filed.ordinal(), item.getType() == 1? "卖出": "买入");
						break;
					case 价格:
						e.setCell(filed.ordinal(), FormatUtils.formatCNY(item.getPrice()));
						break;
					case 数量:
						e.setCell(filed.ordinal(), FormatUtils.formatCNY(item.getAmount()));
						break;
					case 数量范围:
						e.setCell(filed.ordinal(), FormatUtils.formatCNY(item.getMinAmount())+" - " + FormatUtils.formatCNY(item.getMaxAmount()));
						break;
					case 已成交数量:
						e.setCell(filed.ordinal(), FormatUtils.formatCNY(item.getSuccessAmount()));
						break;
					case 冻结数量:
						e.setCell(filed.ordinal(), FormatUtils.formatCNY(item.getFrozenAmount()));
						break;
					case 状态:
						e.setCell(filed.ordinal(), item.getStatus_s());
						break;
					case 支付方式:
						e.setCell(filed.ordinal(), item.getPay_s());
						break;
					case 生成时间:
						e.setCell(filed.ordinal(),DateUtils.formatDate(item.getCreateTime(), "yyyy-MM-dd HH:mm:ss"));
						break;
					case 更新时间:
						e.setCell(filed.ordinal(),DateUtils.formatDate(item.getUpdateTime(), "yyyy-MM-dd HH:mm:ss"));
						break;
					default:
						break;
				}
			}
		}

		try {
			SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
			response.setContentType("application/vnd.ms-excel");
			response.setHeader("Content-Disposition", "attachment;filename=" + URLEncoder.encode("OTC挂单表-", "utf-8") + format.format(new Date()) + ".xls");
			e.exportXls(response);
		} catch (UnsupportedEncodingException re) {
			re.printStackTrace();
		}
	}

	/*---------以下是账号信息---------*/
	@RequestMapping("ssadmin/otcSetList")
	@RequiresPermissions("ssadmin/otccheck.html")
	public ModelAndView otcSetList(HttpServletRequest request) throws Exception {
		ModelAndView modelAndView = new ModelAndView();
		modelAndView.setViewName("ssadmin/otc/otcSetList");
		int currentPage = 1;
		if (request.getParameter("pageNum") != null) {
			currentPage = Integer.parseInt(request.getParameter("pageNum"));
		}
		String keyWord = request.getParameter("keywords");
		StringBuffer filter = new StringBuffer();
		filter.append("where 1=1 \n");
		if (keyWord != null && keyWord.trim().length() > 0) {
			filter.append("and (fuser.floginName like '%" + keyWord + "%' or \n");
			filter.append("fuser.fid = '" + keyWord + "'  or \n");
			filter.append("fuser.fnickName like '%" + keyWord + "%'  or \n");
			filter.append("fuser.frealName like '%" + keyWord + "%'  or \n");
			filter.append("fuser.ftelephone like '%" + keyWord + "%'  or \n");
			filter.append("fuser.femail like '%" + keyWord + "%'  or \n");
			filter.append("fuser.fidentityNo like '%" + keyWord + "%' )\n");
			modelAndView.addObject("keywords", keyWord);
		}
		List<OtcAccount> list = this.otcAccountService.list(
				(currentPage - 1) * numPerPage, numPerPage, filter + "", true);
		modelAndView.addObject("list", list);
		modelAndView.addObject("numPerPage", numPerPage);
		modelAndView.addObject("currentPage", currentPage);
		// 总数量
		modelAndView.addObject("totalCount",
				this.adminService.getAllCount("OtcAccount", filter + ""));
		return modelAndView;
	}


	private static enum ExportAccountFiled {
		用户名,银行,支行,银行账号,支付宝账号,微信账号
	}

	@RequestMapping("ssadmin/otcSetExport")
	@RequiresPermissions("ssadmin/otccheck.html")
	public void otcSetExport(HttpServletRequest request, HttpServletResponse response) throws Exception {
		ModelAndView modelAndView = new ModelAndView();
		response.setContentType("Application/excel");
		response.addHeader("Content-Disposition",
				"attachment;filename=otcAccount.xls");
		XlsExport e = new XlsExport();
		int rowIndex = 0;

		// header
		e.createRow(rowIndex++);
		for (OtcController.ExportAccountFiled filed : OtcController.ExportAccountFiled.values()) {
			e.setCell(filed.ordinal(), filed.toString());
		}

		String keyWord = request.getParameter("keywords");

		StringBuffer filter = new StringBuffer();
		filter.append("where 1=1 \n");
		if (keyWord != null && keyWord.trim().length() > 0) {
			filter.append("and (fuser.floginName like '%" + keyWord + "%' or \n");
			filter.append("fuser.fid = '" + keyWord + "'  or \n");
			filter.append("fuser.fnickName like '%" + keyWord + "%'  or \n");
			filter.append("fuser.frealName like '%" + keyWord + "%'  or \n");
			filter.append("fuser.ftelephone like '%" + keyWord + "%'  or \n");
			filter.append("fuser.femail like '%" + keyWord + "%'  or \n");
			filter.append("fuser.fidentityNo like '%" + keyWord + "%' )\n");
		}

		List<OtcAccount> list = this.otcAccountService.list(
				0, 0, filter + "", false);

		for (OtcAccount item : list) {
			e.createRow(rowIndex++);
			for (OtcController.ExportAccountFiled filed : OtcController.ExportAccountFiled.values()) {
				switch (filed) {
					case 用户名:
						e.setCell(filed.ordinal(), item.getFuser().getFrealName());
						break;
					case 银行:
						e.setCell(filed.ordinal(), item.getBankName());
						break;
					case 支行:
						e.setCell(filed.ordinal(), item.getBankBranch());
						break;
					case 银行账号:
						e.setCell(filed.ordinal(), item.getBankAccount());
						break;
					case 支付宝账号:
						e.setCell(filed.ordinal(), item.getAliAccount());
						break;
					case 微信账号:
						e.setCell(filed.ordinal(), item.getWechatAccount());
						break;
					default:
						break;
				}
			}
		}

		try {
			SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
			response.setContentType("application/vnd.ms-excel");
			response.setHeader("Content-Disposition", "attachment;filename=" + URLEncoder.encode("OTC支付方式表-", "utf-8") + format.format(new Date()) + ".xls");
			e.exportXls(response);
		} catch (UnsupportedEncodingException re) {
			re.printStackTrace();
		}
	}


	/*-------以下是快速单--------*/
	@RequestMapping("ssadmin/otcOrderLogList")
	@RequiresPermissions("ssadmin/otccheck.html")
	public ModelAndView otcOrderLogList(HttpServletRequest request) throws Exception {
		ModelAndView modelAndView = new ModelAndView();
		modelAndView.setViewName("ssadmin/otc/otcOrderLogList");
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
			filter.append("and (fuser.floginName like '%" + keyWord + "%' or \n");
			filter.append("fuser.fid = '" + keyWord + "'  or \n");
			filter.append("fuser.fnickName like '%" + keyWord + "%'  or \n");
			filter.append("fuser.frealName like '%" + keyWord + "%'  or \n");
			filter.append("fuser.ftelephone like '%" + keyWord + "%'  or \n");
			filter.append("fuser.femail like '%" + keyWord + "%'  or \n");
			filter.append("fuser.fidentityNo like '%" + keyWord + "%' )\n");
			modelAndView.addObject("keywords", keyWord);
		}

		String id = request.getParameter("id");
		if(StringUtils.hasText(id)){
			filter.append("and id=" + Integer.parseInt(id) + " \n");
			modelAndView.addObject("id", Integer.parseInt(id));
		}

		Map typeMap = new HashMap();
		typeMap.put(1, "买入");
		typeMap.put(0,"卖出");
		modelAndView.addObject("typeMap", typeMap);
		Map statusMap = new HashMap();
		statusMap.put(0,"待成交");
		statusMap.put(1,"待付款");
		statusMap.put(2,"已成功");
		statusMap.put(3,"已撤销");
		statusMap.put(4,"冻结中");
		statusMap.put(5,"申诉中");
		modelAndView.addObject("statusMap", statusMap);

		String type = request.getParameter("ftype");
		if (type != null
				&& type.trim().length() > 0) {
			filter.append("and otcOrder.type=" + Integer.parseInt(type)
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

		Map coinMap = new HashMap();
		List<Fvirtualcointype> coins = frontVirtualCoinService.findOtcCoin();
		for(Fvirtualcointype coin: coins){
			coinMap.put(coin.getFid(), coin.getfShortName());
		}
		modelAndView.addObject("coinMap", coinMap);
		String coin = request.getParameter("coin");
		if (coin != null
				&& coin.trim().length() > 0) {
			filter.append("and otcOrder.fvirtualcointype.fid=" + Integer.parseInt(coin)
					+ " \n");
			modelAndView.addObject("coin", Integer.parseInt(coin));
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

		List<OtcOrderLog> list = this.otcOrderLogService.list(
				(currentPage - 1) * numPerPage, numPerPage, filter + "", true);
		modelAndView.addObject("list", list);
		modelAndView.addObject("numPerPage", numPerPage);
		modelAndView.addObject("currentPage", currentPage);
		// 总数量
		modelAndView.addObject("totalCount",
				this.adminService.getAllCount("OtcOrderLog", filter + ""));
		return modelAndView;
	}

	private static enum ExportLogFiled {
		订单ID,对应挂单ID,挂单用户,币种,用户,类型,价格,数量,支付方式,买方申诉理由,卖方申诉理由,状态,生成时间,更新时间
	}

	@RequestMapping("ssadmin/otcOrderLogExport")
	@RequiresPermissions("ssadmin/otccheck.html")
	public void otcOrderLogExport(HttpServletRequest request, HttpServletResponse response) throws Exception {
		ModelAndView modelAndView = new ModelAndView();
		response.setContentType("Application/excel");
		response.addHeader("Content-Disposition",
				"attachment;filename=otcAccount.xls");
		XlsExport e = new XlsExport();
		int rowIndex = 0;

		// header
		e.createRow(rowIndex++);
		for (OtcController.ExportLogFiled filed : OtcController.ExportLogFiled.values()) {
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
			filter.append("and (fuser.floginName like '%" + keyWord + "%' or \n");
			filter.append("fuser.fid = '" + keyWord + "'  or \n");
			filter.append("fuser.fnickName like '%" + keyWord + "%'  or \n");
			filter.append("fuser.frealName like '%" + keyWord + "%'  or \n");
			filter.append("fuser.ftelephone like '%" + keyWord + "%'  or \n");
			filter.append("fuser.femail like '%" + keyWord + "%'  or \n");
			filter.append("fuser.fidentityNo like '%" + keyWord + "%' )\n");
		};

		String id = request.getParameter("id");
		if(StringUtils.hasText(id)){
			filter.append("and id=" + Integer.parseInt(id) + " \n");
		}

		String type = request.getParameter("ftype");
		if (type != null
				&& type.trim().length() > 0) {
			filter.append("and otcOrder.type=" + Integer.parseInt(type)
					+ " \n");
		}

		String status = request.getParameter("fstatus");
		if (status != null
				&& status.trim().length() > 0) {
			filter.append("and status=" + Integer.parseInt(status)
					+ " \n");
		}

		String coin = request.getParameter("coin");
		if (coin != null
				&& coin.trim().length() > 0) {
			filter.append("and otcOrder.fvirtualcointype.fid=" + Integer.parseInt(coin)
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

		List<OtcOrderLog> list = this.otcOrderLogService.list(
				0, 0, filter + "", false);

		for (OtcOrderLog item : list) {
			e.createRow(rowIndex++);
			for (OtcController.ExportLogFiled filed : OtcController.ExportLogFiled.values()) {
				switch (filed) {
					case 订单ID:
						e.setCell(filed.ordinal(), item.getId());
						break;
					case 对应挂单ID:
						e.setCell(filed.ordinal(), item.getOtcOrder().getId());
						break;
					case 挂单用户:
						e.setCell(filed.ordinal(), item.getOtcOrder().getFuser().getFrealName());
						break;
					case 币种:
						e.setCell(filed.ordinal(), item.getOtcOrder().getFvirtualcointype().getfShortName());
						break;
					case 用户:
						e.setCell(filed.ordinal(), item.getFuser().getFrealName());
						break;
					case 类型:
						e.setCell(filed.ordinal(), item.getOtcOrder().getType() == 0 ? "卖出": "买入");
						break;
					case 价格:
						e.setCell(filed.ordinal(), item.getOtcOrder().getPrice());
						break;
					case 数量:
						e.setCell(filed.ordinal(), item.getAmount());
						break;
					case 支付方式:
						e.setCell(filed.ordinal(), item.getPay_s());
						break;
					case 买方申诉理由:
						e.setCell(filed.ordinal(), item.getBuyerNote());
						break;
					case 卖方申诉理由:
						e.setCell(filed.ordinal(), item.getSllerNote());
						break;
					case 状态:
						e.setCell(filed.ordinal(), item.getStatus_s());
						break;
					case 生成时间:
						e.setCell(filed.ordinal(),DateUtils.formatDate(item.getCreateTime(), "yyyy-MM-dd HH:mm:ss"));
						break;
					case 更新时间:
						e.setCell(filed.ordinal(),DateUtils.formatDate(item.getUpdateTime(),"yyyy-MM-dd HH:mm:ss"));
						break;
					default:
						break;
				}
			}
		}

		try {
			SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
			response.setContentType("application/vnd.ms-excel");
			response.setHeader("Content-Disposition", "attachment;filename=" + URLEncoder.encode("OTC快速单表-", "utf-8") + format.format(new Date()) + ".xls");
			e.exportXls(response);
		} catch (UnsupportedEncodingException re) {
			re.printStackTrace();
		}
	}

	@RequestMapping("ssadmin/otcUpdate")
	@RequiresPermissions("ssadmin/otccheck.html")
	public ModelAndView ccUpdate(HttpServletRequest request) throws Exception {
		ModelAndView modelAndView = new ModelAndView() ;
		modelAndView.setViewName("ssadmin/comm/ajaxDone") ;
		int uid = Integer.valueOf(request.getParameter("uid"));
		int status = Integer.valueOf(request.getParameter("status"));

		OtcOrderLog otcOrderLog = otcOrderLogService.findById(uid);
		if(otcOrderLog == null || (otcOrderLog.getStatus() !=4 && otcOrderLog.getStatus() != 5)){
			modelAndView.addObject("statusCode",300);
			modelAndView.addObject("message","只能处理冻结中或申诉中的订单");
			return modelAndView;
		}
		try{
			if(status == 2){
				otcOrderLogService.updateSuccess(otcOrderLog);
			}else {
				otcOrderLogService.updateCancel(otcOrderLog);
			}
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

package com.trade.controller.admin;

import com.trade.Enum.CountLimitTypeEnum;
import com.trade.Enum.ValidateMessageStatusEnum;
import com.trade.code.AuthCode;
import com.trade.comm.ConstantMap;
import com.trade.model.Fadmin;
import com.trade.model.Fvalidatemessage;
import com.trade.model.Fvirtualcointype;
import com.trade.service.admin.*;
import com.trade.service.front.FrontValidateService;
import com.trade.util.*;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.authz.annotation.Logical;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import java.text.SimpleDateFormat;
import java.util.*;

@Controller
public class IndexController extends BaseController {
	@Autowired
	private UserService userService;;
	@Autowired
	private FrontValidateService frontValidateService;
	@Autowired
	private EntrustService entrustService;
	@Autowired
	private VirtualCapitaloperationService virtualCapitaloperationService;
	@Autowired
	private VirtualCoinService virtualCoinService;
	@Autowired
	private AdminService adminService;
	@Autowired
	private ConstantMap constantMap;

	@RequestMapping("/ssadmin/index")
	public ModelAndView Index(HttpServletRequest request) throws Exception {
		SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		ModelAndView modelAndView = new ModelAndView();
		modelAndView.setViewName("ssadmin/index");
//		boolean showLend = this.frontLendService.findFlendsystemargsById(FlendSystemargsIds.ISOPENED).getBooleanValue() ;
//		modelAndView.addObject("showLend", showLend);
		modelAndView.addObject("dateTime", sdf1.format(new Date()));
		modelAndView.addObject("login_admin", request.getSession()
				.getAttribute("login_admin"));
		
		return modelAndView;
	}

	@RequestMapping("/ssadmin/userReport")
	@RequiresPermissions("ssadmin/userReport.html")
	public ModelAndView userReport(HttpServletRequest request) throws Exception {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		ModelAndView modelAndView = new ModelAndView();
		modelAndView.setViewName("ssadmin/userReport");
		String startDate = request.getParameter("startDate");
		String endDate = request.getParameter("endDate");
		StringBuffer key = new StringBuffer();
		StringBuffer value = new StringBuffer();
		boolean hasError = false;
		String msg = "";
		if (startDate == null || startDate.trim().length() == 0
				|| endDate == null || endDate.trim().length() == 0) {
			hasError = true;
			msg = "起始日期不能为空";
		} else {
			Date sd = sdf.parse(startDate);
			Date ed = sdf.parse(endDate);
			if (ed.getTime() - sd.getTime() > 31L * 24 * 60 * 60 * 1000) {
				hasError = true;
				msg = "日期间隔不能大于31天";
			}
		}
		if (hasError) {
			if (key.length() == 0) {
				key.append("\"0\"");
			}
			if (value.length() == 0) {
				value.append("0");
			}
			modelAndView.addObject("key", "[" + key + "]");
			modelAndView.addObject("value", "[" + value + "]");
			modelAndView.addObject("startDate", startDate);
			modelAndView.addObject("endDate", endDate);
			if (request.getParameter("isSearch") != null) {
				modelAndView.setViewName("ssadmin/comm/ajaxDone");
				modelAndView.addObject("statusCode", 300);
				modelAndView.addObject("message", msg);
			}
			return modelAndView;
		}

		StringBuffer filter = new StringBuffer();
		filter.append("where 1=1 \n");
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(sdf.parse(endDate));
		calendar.set(Calendar.DAY_OF_MONTH,
				calendar.get(Calendar.DAY_OF_MONTH) + 1);
		filter.append("and Fregistertime >='" + startDate + "' \n");
		filter.append("and Fregistertime <'" + sdf.format(calendar.getTime())
				+ "' \n");
		List all = this.userService.getUserGroup(filter + "");
		Iterator it = all.iterator();
		double total = 0;
		while (it.hasNext()) {
			Object[] o = (Object[]) it.next();
			if (key.length() == 0) {
				key.append("\"" + o[1] + "\"");
			} else {
				key.append(",\"" + o[1] + "\"");
			}
			if (value.length() == 0) {
				value.append(o[0]);
			} else {
				value.append("," + o[0]);
			}
			total = total + Double.valueOf(o[0].toString());
		}
		if (key.length() == 0) {
			key.append("\"0\"");
		}
		if (value.length() == 0) {
			value.append("0");
		}
		modelAndView.addObject("key", "[" + key + "]");
		modelAndView.addObject("value", "[" + value + "]");
		modelAndView.addObject("startDate", startDate);
		modelAndView.addObject("endDate", endDate);
		modelAndView.addObject("total", total);
		return modelAndView;
	}

	@RequestMapping("/ssadmin/vcOperationReport")
	@RequiresPermissions(value = {"ssadmin/vcOperationInReport.html", "ssadmin/vcOperationOutReport.html"}, logical = Logical.OR)
	public ModelAndView vcOperationReport(HttpServletRequest request) throws Exception {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		ModelAndView modelAndView = new ModelAndView();
		List<Fvirtualcointype> allType = this.virtualCoinService.findAll();
		modelAndView.addObject("allType", allType);
		if (request.getParameter("vid") != null) {
			int vid = Integer.parseInt(request.getParameter("vid"));
			Fvirtualcointype coinType = this.virtualCoinService.findById(vid);
			modelAndView.addObject("vid", vid);
			modelAndView.addObject("coinType", coinType.getFname());
		}
		modelAndView.setViewName(request.getParameter("url"));
		String startDate = request.getParameter("startDate");
		String endDate = request.getParameter("endDate");
		StringBuffer key = new StringBuffer();
		StringBuffer value = new StringBuffer();
		boolean hasError = false;
		String msg = "";
		if (startDate == null || startDate.trim().length() == 0
				|| endDate == null || endDate.trim().length() == 0) {
			hasError = true;
			msg = "起始日期不能为空";
		} else {
			Date sd = sdf.parse(startDate);
			Date ed = sdf.parse(endDate);
			if (ed.getTime() - sd.getTime() > 31L * 24 * 60 * 60 * 1000) {
				hasError = true;
				msg = "日期间隔不能大于31天";
			}
		}
		if (hasError) {
			if (key.length() == 0) {
				key.append("\"0\"");
			}
			if (value.length() == 0) {
				value.append("0");
			}
			modelAndView.addObject("key", "[" + key + "]");
			modelAndView.addObject("value", "[" + value + "]");
			modelAndView.addObject("startDate", startDate);
			modelAndView.addObject("endDate", endDate);
			if (request.getParameter("isSearch") != null) {
				modelAndView.setViewName("ssadmin/comm/ajaxDone");
				modelAndView.addObject("statusCode", 300);
				modelAndView.addObject("message", msg);
			}
			return modelAndView;
		}

		StringBuffer filter = new StringBuffer();
		filter.append("where 1=1 \n");
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(sdf.parse(endDate));
		calendar.set(Calendar.DAY_OF_MONTH,
				calendar.get(Calendar.DAY_OF_MONTH) + 1);
		filter.append("and flastUpdateTime >='" + startDate + "' \n");
		filter.append("and flastUpdateTime <'" + sdf.format(calendar.getTime())
				+ "' \n");
		filter.append("and ftype=" + request.getParameter("type") + "\n");
		filter.append("and fUs_fId2 is not null " + "\n");
		filter.append("and fstatus =" + request.getParameter("status") + "\n");
		filter.append("and fVi_fId2 =" + request.getParameter("vid") + "\n");
		List all = new ArrayList();

		if (request.getParameter("vid") != null) {
			all = this.virtualCapitaloperationService
					.getTotalGroup(filter + "");
		}

		double total = 0;
		Iterator it = all.iterator();
		while (it.hasNext()) {
			Object[] o = (Object[]) it.next();
			if (key.length() == 0) {
				key.append("\"" + o[1] + "\"");
			} else {
				key.append(",\"" + o[1] + "\"");
			}
			if (value.length() == 0) {
				value.append(o[0]);
			} else {
				value.append("," + o[0]);
			}
			total = total + Double.valueOf(o[0].toString());
		}
		if (key.length() == 0) {
			key.append("\"0\"");
		}
		if (value.length() == 0) {
			value.append("0");
		}
		modelAndView.addObject("key", "[" + key + "]");
		modelAndView.addObject("value", "[" + value + "]");
		modelAndView.addObject("startDate", startDate);
		modelAndView.addObject("endDate", endDate);
		modelAndView.addObject("total", total);
		return modelAndView;
	}

	@RequestMapping("/ssadmin/plj32_dsjds23")
	public ModelAndView login() throws Exception {
		ModelAndView modelAndView = new ModelAndView();
		modelAndView.setViewName("ssadmin/login");
		return modelAndView;
	}

	@RequestMapping("/ssadmin/submitLogin__zhg")
	public ModelAndView submitLogin__zhg(HttpServletRequest request, @RequestParam(required = true) String name,
			@RequestParam(required = true) String password,
			@RequestParam(required = true) String vcode) throws Exception {

		ModelAndView modelAndView = new ModelAndView();

		if (name == null || "".equals(name.trim()) || password == null
				|| "".equals(password.trim()) || vcode == null
				|| "".equals(vcode.trim())) {
			modelAndView.setViewName("redirect:/ssadmin/login_zhg.html");
		} else {
			String ip = Utils.getIpAddr(request);
			int admin_limit = this.frontValidateService.getLimitCount(ip,
					CountLimitTypeEnum.AdminLogin);
			if (admin_limit <= 0) {
				modelAndView.addObject("error", "连续登陆错误5次，为安全起见，禁止登陆2小时！");
				modelAndView.setViewName("/ssadmin/login");
				return modelAndView;
			}

			if (!vcode.equalsIgnoreCase((String) getSession().getAttribute(
					"checkcode"))) {
				modelAndView.addObject("error", "验证码错误！");
				modelAndView.setViewName("/ssadmin/login");
				return modelAndView;
			}

			Subject admin = SecurityUtils.getSubject();
			UsernamePasswordToken token = new UsernamePasswordToken(name,
					Utils.MD5(password));
			token.setRememberMe(true);
			try {
				admin.login(token);
			} catch (Exception e) {
				token.clear();
				this.frontValidateService.updateLimitCount(ip,
						CountLimitTypeEnum.AdminLogin);
				modelAndView.addObject("error", e.getMessage());
				modelAndView.setViewName("/ssadmin/login");
				return modelAndView;
			}
		}
		modelAndView.setViewName("redirect:/ssadmin/index.html");
		return modelAndView;
	}
	
	@ResponseBody
	@RequestMapping("/ssadmin/sendLoginSmsCode")
	public Object sendLoginSmsCode(String mobile){
		if(!StringUtils.isMobile(mobile)){
			return 101;
		}

		List<Fadmin> fadmins = adminService.findByProperty("mobile", mobile);
		if(CollectionUtils.isEmpty(fadmins)){
			return 102;
		}

		AuthCode authCode = new AuthCode(mobile, Utils.randomInteger(6), CountLimitTypeEnum.TELEPHONE);
		getSession().setAttribute(Constants.SESSION_CAPTCHA_CODE, authCode);

		// 保存发送记录
		Fvalidatemessage fvalidatemessage = new Fvalidatemessage() ;
		fvalidatemessage.setFcontent("【" + constantMap.getString("fromName") + "】您的后台登陆验证码为：" + authCode.getCode());
		fvalidatemessage.setFcreateTime(authCode.getDateTime()) ;
		fvalidatemessage.setFphone("0086"+mobile) ;
		fvalidatemessage.setFstatus(ValidateMessageStatusEnum.Not_send) ;
		this.frontValidateService.addFvalidateMessage(fvalidatemessage) ;
		//发送短信队列通知
		//messageQueueService.publish(QueueConstants.MESSAGE_COMMON_QUEUE, fvalidatemessage);

		return 200;
	}

	@RequestMapping("/ssadmin/smsLogin")
	public Object smsLogin(HttpServletRequest request, String mobile, String password, String code, ModelMap modelMap){
		if(StringUtils.isEmpty(mobile) || StringUtils.isEmpty(password) || StringUtils.isEmpty(code)){
			modelMap.put("error", "请输入完整登录信息！");
			return "ssadmin/login";
		}

		String ip = Utils.getIpAddr(request);
		int admin_limit = this.frontValidateService.getLimitCount(ip, CountLimitTypeEnum.AdminLogin);
		if (admin_limit <= 0) {
			modelMap.put("error", "连续登陆错误5次，为安全起见，禁止登陆2小时！");
			return "ssadmin/login";
		}

		AuthCode authCode = (AuthCode) getSession().getAttribute(Constants.SESSION_CAPTCHA_CODE);
		//System.out.println(authCode.isEnabled(mobile, code, CountLimitTypeEnum.TELEPHONE));
		if(null == authCode){
			modelMap.put("error", "未发送验证码！");
			return "ssadmin/login";
		}
		if(!authCode.isEnabled(mobile, code, CountLimitTypeEnum.TELEPHONE)){
			modelMap.put("error", "短信验证码错误！");
			return "ssadmin/login";
		}

		Subject admin = SecurityUtils.getSubject();
		UsernamePasswordToken token = new UsernamePasswordToken(mobile, Utils.MD5(password));
		token.setRememberMe(true);

		try {
			admin.login(token);
		} catch (Exception e) {
			token.clear();
			this.frontValidateService.updateLimitCount(ip, CountLimitTypeEnum.AdminLogin);
			modelMap.put("error", e.getMessage());
			return "ssadmin/login";
		}

		return "redirect:/ssadmin/index.html";
	}

	/**
	 * 交易手续费报表
	 * @param start
	 * @param end
	 * @param symbol
	 * @return
	 * @throws Exception
	 */
	@RequiresPermissions("ssadmin/tradefeeReport.html")
	@RequestMapping("/ssadmin/tradefeeReport.html")
	public ModelAndView tradefeeReport(
			@RequestParam(required = false, name = "startDate") String start,
			@RequestParam(required = false, name = "endDate") String end,
			@RequestParam(required = false, defaultValue = "0") int symbol
	)throws Exception{
		ModelAndView modelAndView = new ModelAndView();
		modelAndView.setViewName("ssadmin/comm/ajaxDone");
		modelAndView.setViewName("ssadmin/tradefeeReport");
		if(!org.springframework.util.StringUtils.hasText(start)){
			start = DateUtils.formatDate(DateUtils.getMonthBefore(), "yyyy-MM-dd");
		}
		if(!org.springframework.util.StringUtils.hasText(end)) {
			end = DateUtils.formatDate(new Date(), "yyyy-MM-dd");
		}
		Date startDate = DateUtils.formatDate(start, "yyyy-MM-dd");
		Date endDate = DateUtils.formatDate(end, "yyyy-MM-dd");
		endDate.setTime(endDate.getTime() + 24 * 60 * 60 * 1000);//包含endDate
		modelAndView.addObject("symbol", symbol);
		if(symbol != 0){
			Fvirtualcointype fvirtualcointype = virtualCoinService.findById(symbol);
			modelAndView.addObject("coinName", fvirtualcointype.getFname());
		}else{
			modelAndView.addObject("coinName", "全部");
		}
		List<String[]> list = entrustService.listForTradeFeeReport(startDate, endDate, symbol);
		StringBuilder key = new StringBuilder();
		StringBuilder value = new StringBuilder();
		double total = 0D;
		for (String[] strings : list) {
			key.append("\"" + strings[0] + "\"").append(",");
			value.append(strings[1]).append(",");
			total += Double.parseDouble(strings[1]);
		}
		if(list.size() == 0){
			key.append("\"0\"");
			value.append(0);

		}else{
			key.deleteCharAt(key.length() - 1);
			value.deleteCharAt(value.length() - 1);
		}
		modelAndView.addObject("key", "[" + key + "]");
		modelAndView.addObject("value", "[" + value + "]");
		List<Fvirtualcointype> allType = this.virtualCoinService.findAll();
		modelAndView.addObject("allType", allType);
		modelAndView.addObject("startDate", start);
		modelAndView.addObject("endDate", end);
		modelAndView.addObject("total", total);
		return modelAndView;
	}
}

package com.trade.controller.admin;
import com.trade.model.*;
import com.trade.Enum.IdentityTypeEnum;
import com.trade.Enum.MessageStatusEnum;
import com.trade.Enum.UserGradeEnum;
import com.trade.Enum.UserStatusEnum;
import com.trade.dto.OperateUserActionDTO;
import com.trade.mq.MessageQueueService;
import com.trade.mq.QueueConstants;
import com.trade.service.admin.AdminService;
import com.trade.service.admin.UserService;
import com.trade.service.admin.VirtualCoinService;
import com.trade.service.front.FmessageService;
import com.trade.service.front.MarketService;
import com.trade.service.front.SpreadLogService;
import com.trade.service.front.TradeFeesShareService;
import com.trade.util.StringUtils;
import com.trade.util.Utils;
import com.trade.util.XlsExport;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.regex.Pattern;

@Controller
public class UserController {
	@Autowired
	private UserService userService;
	@Autowired
	private AdminService adminService;
	@Autowired
	private MessageQueueService messageQueueService;
	@Autowired
	private FmessageService fmessageService;
	@Autowired
	private SpreadLogService spreadLogService;
	@Autowired
    private TradeFeesShareService tradeFeesShareService;
	@Autowired
	private MarketService marketService;
	@Autowired
	private VirtualCoinService virtualCoinService;
	// 每页显示多少条数据
	private int numPerPage = Utils.getNumPerPage();

	@RequestMapping("/ssadmin/cancelTest2017ByYJ")
	@RequiresPermissions("ssadmin/userList.html")
	public ModelAndView cancel(HttpServletRequest request) throws Exception{
		ModelAndView modelAndView = new ModelAndView() ;
		modelAndView.setViewName("ssadmin/test/cancel") ;
		return modelAndView ;
	}


	@RequestMapping("/ssadmin/userList")
	@RequiresPermissions("ssadmin/userList.html")
	public ModelAndView Index(HttpServletRequest request) throws Exception {
		ModelAndView modelAndView = new ModelAndView();
		modelAndView.setViewName("ssadmin/userList");
		// 当前页
		int currentPage = 1;
		// 搜索关键字
		String keyWord = request.getParameter("keywords");
		String startDate = request.getParameter("startDate");
		String orderField = request.getParameter("orderField");
		String orderDirection = request.getParameter("orderDirection");
		if (request.getParameter("pageNum") != null) {
			currentPage = Integer.parseInt(request.getParameter("pageNum"));
		}
		StringBuffer filter = new StringBuffer();
		filter.append("where 1=1 \n");
		if (keyWord != null && keyWord.trim().length() > 0) {
			filter.append("and (floginName like '%" + keyWord + "%' or \n");
			filter.append("fid = '" + keyWord + "'  or \n");
			filter.append("fnickName like '%" + keyWord + "%'  or \n");
			filter.append("frealName like '%" + keyWord + "%'  or \n");
			filter.append("ftelephone like '%" + keyWord + "%'  or \n");
			filter.append("femail like '%" + keyWord + "%'  or \n");
			filter.append("fidentityNo like '%" + keyWord + "%' )\n");
			modelAndView.addObject("keywords", keyWord);
		}

		Map typeMap = new HashMap();
		typeMap.put(0, "全部");
		typeMap.put(UserStatusEnum.NORMAL_VALUE,
				UserStatusEnum.getEnumString(UserStatusEnum.NORMAL_VALUE));
		typeMap.put(UserStatusEnum.FORBBIN_VALUE,
				UserStatusEnum.getEnumString(UserStatusEnum.FORBBIN_VALUE));
		modelAndView.addObject("typeMap", typeMap);

		if (request.getParameter("ftype") != null
				&& request.getParameter("ftype").trim().length() > 0) {
			int type = Integer.parseInt(request.getParameter("ftype"));
			if (type != 0) {
				filter.append("and fstatus=" + request.getParameter("ftype")
						+ " \n");
			}
			modelAndView.addObject("ftype", request.getParameter("ftype"));
		}
		if (request.getParameter("fstatus") != null
				&& request.getParameter("fstatus").trim().length() > 0){
			int fstatus =  Integer.valueOf(request.getParameter("fstatus"));
			if(fstatus !=-1){
				filter.append("and fIdentityStatus=" + fstatus +"\n");
				modelAndView.addObject("fstatus", fstatus);
			}
		}


		try {
			if (request.getParameter("troUid") != null
					&& request.getParameter("troUid").trim().length() > 0) {
				int troUid = Integer.parseInt(request.getParameter("troUid"));
				filter.append("and fIntroUser_id.fid=" + troUid + " \n");
				modelAndView.addObject("troUid", troUid);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		if (orderField != null && orderField.trim().length() > 0) {
			filter.append("order by " + orderField + "\n");
		} else {
			filter.append("order by fid \n");
		}

		if (orderDirection != null && orderDirection.trim().length() > 0) {
			filter.append(orderDirection + "\n");
		} else {
			filter.append("desc \n");
		}

		List<Fuser> list = this.userService.specialList(
				(currentPage - 1) * numPerPage, numPerPage, filter + "", true,startDate);
		modelAndView.addObject("startDate", startDate);
		modelAndView.addObject("userList", list);
		modelAndView.addObject("numPerPage", numPerPage);
		modelAndView.addObject("currentPage", currentPage);
		modelAndView.addObject("rel", "listUser");
		// 总数量
		modelAndView.addObject("totalCount",
				this.adminService.getAllCount("Fuser", filter + ""));
		return modelAndView;
	}

	@RequestMapping("/ssadmin/userGradeDetail")
	@RequiresPermissions("ssadmin/userGradeDetail.html")
	public ModelAndView userGradeDetail(HttpServletRequest request) throws Exception {
		ModelAndView modelAndView = new ModelAndView();
		modelAndView.setViewName("ssadmin/userGradeDetail");
		// 当前页
		int currentPage = 1;
		// 搜索关键字
		String keyWord = request.getParameter("keywords");
		String orderField = request.getParameter("orderField");
		String orderDirection = request.getParameter("orderDirection");
		if (request.getParameter("pageNum") != null) {
			currentPage = Integer.parseInt(request.getParameter("pageNum"));
		}
		StringBuffer filter = new StringBuffer();
		filter.append("where 1=1 \n");
		if (keyWord != null && keyWord.trim().length() > 0) {
			filter.append("and (floginName like '%" + keyWord + "%' or \n");
			filter.append("fnickName like '%" + keyWord + "%'  or \n");
			filter.append("frealName like '%" + keyWord + "%'  or \n");
			filter.append("ftelephone like '%" + keyWord + "%'  or \n");
			filter.append("femail like '%" + keyWord + "%'  or \n");
			filter.append("fidentityNo like '%" + keyWord + "%' )\n");
			modelAndView.addObject("keywords", keyWord);
		}

		try {
			if (request.getParameter("parentId") != null
					&& request.getParameter("parentId").trim().length() > 0) {
				int parentId = Integer.parseInt(request.getParameter("parentId"));
				filter.append("and fIntroUser_id.fid=" + parentId + " \n");
				modelAndView.addObject("parentId", parentId);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		if (orderField != null && orderField.trim().length() > 0) {
			filter.append("order by " + orderField + "\n");
		}

		if (orderDirection != null && orderDirection.trim().length() > 0) {
			filter.append(orderDirection + "\n");
		}

		List<Fuser> list = this.userService.list(
				(currentPage - 1) * numPerPage, numPerPage, filter + "", true);
		modelAndView.addObject("userGradeDetail", list);
		modelAndView.addObject("numPerPage", numPerPage);
		modelAndView.addObject("currentPage", currentPage);
		modelAndView.addObject("rel", "userGradeDetail");
		// 总数量
		modelAndView.addObject("totalCount",
				this.adminService.getAllCount("Fuser", filter + ""));
		return modelAndView;
	}

//	@RequestMapping("/ssadmin/usergameList")
//	public ModelAndView usergameList() throws Exception {
//		ModelAndView modelAndView = new ModelAndView();
//		modelAndView.setViewName("ssadmin/usergameList");
//		// 当前页
//		int currentPage = 1;
//		// 搜索关键字
//		String keyWord = request.getParameter("keywords");
//		if (request.getParameter("pageNum") != null) {
//			currentPage = Integer.parseInt(request.getParameter("pageNum"));
//		}
//		StringBuffer filter = new StringBuffer();
//		filter.append("where 1=1 \n");
//		if (keyWord != null && keyWord.trim().length() > 0) {
//			filter.append("and (b.floginName like '%" + keyWord + "%' or \n");
//			filter.append("b.fnickName like '%" + keyWord + "%'  or \n");
//			filter.append("b.frealName like '%" + keyWord + "%'  or \n");
//			filter.append("b.ftelephone like '%" + keyWord + "%'  or \n");
//			filter.append("b.femail like '%" + keyWord + "%'  or \n");
//			filter.append("b.fidentityNo like '%" + keyWord + "%' )\n");
//			modelAndView.addObject("keywords", keyWord);
//		}
//
//		List list = this.gameOperateLogService.getUserInfo((currentPage - 1) * numPerPage, numPerPage, filter + "", true);
//		modelAndView.addObject("usergameList", list);
//		modelAndView.addObject("numPerPage", numPerPage);
//		modelAndView.addObject("currentPage", currentPage);
//		modelAndView.addObject("rel", "usergameList");
//		// 总数量
//		modelAndView.addObject("totalCount",
//				this.adminService.getAllCount("Fuser b", filter + ""));
//		return modelAndView;
//	}

	public static boolean isNumeric(String str){
		if(str == null ||str.trim().length() ==0) return false;
		Pattern pattern = Pattern.compile("[0-9]*");
		return pattern.matcher(str).matches();
	}

	@RequestMapping("/ssadmin/userGradeList")
	@RequiresPermissions("ssadmin/userGradeList.html")
	public ModelAndView userGradeList(HttpServletRequest request) throws Exception {
		ModelAndView modelAndView = new ModelAndView();
		modelAndView.setViewName("ssadmin/userGradeList");
		// 当前页
		int currentPage = 1;
		// 搜索关键字
		String keyWord = request.getParameter("keywords");
		if (request.getParameter("pageNum") != null) {
			currentPage = Integer.parseInt(request.getParameter("pageNum"));
		}
		StringBuffer filter = new StringBuffer();
		filter.append("where 1=1 \n");
		if (keyWord != null && keyWord.trim().length() > 0) {
			filter.append("and (t.floginName like '%" + keyWord + "%' or \n");
			filter.append("t.frealName like '%" + keyWord + "%' )\n");
			modelAndView.addObject("keywords", keyWord);
		}
		String ftype = request.getParameter("ftype");
		if(ftype != null && ftype.trim().length() >0){
			if(!ftype.equals("-1")){
				int t = Integer.parseInt(ftype);
				if(t == UserGradeEnum.LEVEL1){
					filter.append("and t.fownerGrade='"+UserGradeEnum.getEnumString(UserGradeEnum.LEVEL1)+"' \n");
				}else if(t == UserGradeEnum.LEVEL2){
					filter.append("and t.fownerGrade='"+UserGradeEnum.getEnumString(UserGradeEnum.LEVEL2)+"' \n");
				}else if(t == UserGradeEnum.LEVEL3){
					filter.append("and t.fownerGrade='"+UserGradeEnum.getEnumString(UserGradeEnum.LEVEL3)+"' \n");
				}else if(t == UserGradeEnum.LEVEL4){
					filter.append("and t.fownerGrade='"+UserGradeEnum.getEnumString(UserGradeEnum.LEVEL4)+"' \n");
				}else if(t == UserGradeEnum.LEVEL5){
					filter.append("and t.fownerGrade='"+UserGradeEnum.getEnumString(UserGradeEnum.LEVEL5)+"' \n");
				}else if(t == UserGradeEnum.LEVEL0){
					filter.append("and t.fownerGrade='"+UserGradeEnum.getEnumString(UserGradeEnum.LEVEL0)+"' \n");
				}
			}
			modelAndView.addObject("ftype", ftype);
		}else{
			modelAndView.addObject("ftype", "-1");
		}

		String level1 = request.getParameter("level1");
		String level2 = request.getParameter("level2");
		String level3 = request.getParameter("level3");
		String level4 = request.getParameter("level4");
		if(level1 != null && level1.trim().length() >0 && isNumeric(level1)){
			filter.append("and t.level1 >="+level1+" \n");
			modelAndView.addObject("level1", level1);
		}
		if(level2 != null && level2.trim().length() >0 && isNumeric(level2)){
			filter.append("and t.level2 >="+level2+" \n");
			modelAndView.addObject("level2", level2);
		}
		if(level3 != null && level3.trim().length() >0 && isNumeric(level3)){
			filter.append("and t.level3 >="+level3+" \n");
			modelAndView.addObject("level3", level3);
		}
		if(level4 != null && level4.trim().length() >0 && isNumeric(level4)){
			filter.append("and t.level4 >="+level4+" \n");
			modelAndView.addObject("level4", level4);
		}

		Map<Integer,String> map = new HashMap<Integer,String>();
		map.put(-1, "全部");
		map.put(UserGradeEnum.LEVEL0, UserGradeEnum.getEnumString(UserGradeEnum.LEVEL0));
		map.put(UserGradeEnum.LEVEL1, UserGradeEnum.getEnumString(UserGradeEnum.LEVEL1));
		map.put(UserGradeEnum.LEVEL2, UserGradeEnum.getEnumString(UserGradeEnum.LEVEL2));
		map.put(UserGradeEnum.LEVEL3, UserGradeEnum.getEnumString(UserGradeEnum.LEVEL3));
		map.put(UserGradeEnum.LEVEL4, UserGradeEnum.getEnumString(UserGradeEnum.LEVEL4));
		map.put(UserGradeEnum.LEVEL5, UserGradeEnum.getEnumString(UserGradeEnum.LEVEL5));
		modelAndView.addObject("map", map);

		List list = this.userService.listUsers((currentPage - 1) * numPerPage, numPerPage, filter + "", true);
		modelAndView.addObject("userGradeList", list);
		modelAndView.addObject("numPerPage", numPerPage);
		modelAndView.addObject("currentPage", currentPage);
		modelAndView.addObject("rel", "userGradeList");
		// 总数量
		modelAndView.addObject("totalCount",this.userService.listUsers(0, 0, filter + "", false).size());
		return modelAndView;
	}

	@RequestMapping("/ssadmin/userLookup")
	public ModelAndView userLookup(HttpServletRequest request) throws Exception {
		ModelAndView modelAndView = new ModelAndView();
		modelAndView.setViewName("ssadmin/userLookup");
		// 当前页
		int currentPage = 1;
		// 搜索关键字
		String keyWord = request.getParameter("keywords");

		if (request.getParameter("pageNum") != null) {
			currentPage = Integer.parseInt(request.getParameter("pageNum"));
		}
		StringBuffer filter = new StringBuffer();
		filter.append("where 1=1 \n");
		if (keyWord != null && keyWord.trim().length() > 0) {
			filter.append("and (floginName like '%" + keyWord + "%' or \n");
			filter.append("fnickName like '%" + keyWord + "%'  or \n");
			filter.append("frealName like '%" + keyWord + "%'  or \n");
			filter.append("ftelephone like '%" + keyWord + "%'  or \n");
			filter.append("femail like '%" + keyWord + "%'  or \n");
			filter.append("fidentityNo like '%" + keyWord + "%' )\n");
			modelAndView.addObject("keywords", keyWord);
		}

		List<Fuser> list = this.userService.list(
				(currentPage - 1) * numPerPage, numPerPage, filter + "", true);
		modelAndView.addObject("userList1", list);
		modelAndView.addObject("numPerPage", numPerPage);
		modelAndView.addObject("rel", "operationLogList");
		modelAndView.addObject("currentPage", currentPage);
		// 总数量
		modelAndView.addObject("totalCount",
				this.adminService.getAllCount("Fuser", filter + ""));
		return modelAndView;
	}

	@RequestMapping("/ssadmin/userAuditList")
	@RequiresPermissions("ssadmin/userAuditList.html")
	public ModelAndView userAuditList(HttpServletRequest request) throws Exception {
		ModelAndView modelAndView = new ModelAndView();
		modelAndView.setViewName("ssadmin/userAuditList");
		// 当前页
		int currentPage = 1;
		// 搜索关键字
		String keyWord = request.getParameter("keywords");
		String orderField = request.getParameter("orderField");
		String orderDirection = request.getParameter("orderDirection");
		if (request.getParameter("pageNum") != null) {
			currentPage = Integer.parseInt(request.getParameter("pageNum"));
		}
		StringBuffer filter = new StringBuffer();
		filter.append("where 1=1 \n");
		if (keyWord != null && keyWord.trim().length() > 0) {
			filter.append("and (floginName like '%" + keyWord + "%' or \n");
			filter.append("fnickName like '%" + keyWord + "%'  or \n");
			filter.append("frealName like '%" + keyWord + "%'  or \n");
			filter.append("ftelephone like '%" + keyWord + "%'  or \n");
			filter.append("femail like '%" + keyWord + "%'  or \n");
			filter.append("fidentityNo like '%" + keyWord + "%' )\n");
			modelAndView.addObject("keywords", keyWord);
		}
		filter.append("and ((fpostRealValidate=1 and fhasRealValidate=0) or fIdentityStatus=1) \n");

		if (orderField != null && orderField.trim().length() > 0) {
			filter.append("order by " + orderField + "\n");
		} else {
			filter.append("order by fid \n");
		}

		if (orderDirection != null && orderDirection.trim().length() > 0) {
			filter.append(orderDirection + "\n");
		} else {
			filter.append("asc \n");
		}
		List<Fuser> list = this.userService.listUserForAudit((currentPage - 1)
				* numPerPage, numPerPage, filter + "", true);
		modelAndView.addObject("userList", list);
		modelAndView.addObject("numPerPage", numPerPage);
		modelAndView.addObject("currentPage", currentPage);
		modelAndView.addObject("rel", "listUser");
		// 总数量
		modelAndView.addObject("totalCount",
				this.adminService.getAllCount("Fuser", filter + ""));
		return modelAndView;
	}

	@RequestMapping("/ssadmin/ajaxDone")
	public ModelAndView ajaxDone() throws Exception {
		ModelAndView modelAndView = new ModelAndView();
		modelAndView.setViewName("ssadmin/comm/ajaxDone");
		return modelAndView;
	}

	@RequestMapping("/ssadmin/userForbbin")
	@RequiresPermissions({"ssadmin/userForbbin.html?status=1", "ssadmin/userForbbin.html?status=2", "ssadmin/userForbbin.html?status=3", "ssadmin/userForbbin.html?status=4"})
	public ModelAndView userForbbin(HttpServletRequest request) throws Exception {
		ModelAndView modelAndView = new ModelAndView();
		modelAndView.setViewName("ssadmin/comm/ajaxDone");
		// modelAndView.setViewName("redirect:/pages/ssadmin/comm/ajaxDone.jsp")
		// ;
		int fid = Integer.parseInt(request.getParameter("uid"));
		int status = Integer.parseInt(request.getParameter("status"));
		Fuser user = this.userService.findById(fid);
		if (status == 1) {
			if (user.getFstatus() == UserStatusEnum.FORBBIN_VALUE) {
				modelAndView.addObject("statusCode", 300);
				modelAndView.addObject("message", "会员已禁用，无需做此操作");
				return modelAndView;
			}
			modelAndView.addObject("statusCode", 200);
			modelAndView.addObject("message", "禁用成功");
			user.setFstatus(UserStatusEnum.FORBBIN_VALUE);

			// 强制用户下线
			messageQueueService.bpublish(QueueConstants.FORCE_OFFLINE_QUEUE, new OperateUserActionDTO(fid, UserStatusEnum.FORBBIN_VALUE, Utils.getTimestamp()));
		} else if (status == 2) {
			if (user.getFstatus() == UserStatusEnum.NORMAL_VALUE) {
				modelAndView.addObject("statusCode", 300);
				modelAndView.addObject("message", "会员状态为正常，无需做此操作");
				return modelAndView;
			}
			modelAndView.addObject("statusCode", 200);
			modelAndView.addObject("message", "解除禁用成功");
			user.setFstatus(UserStatusEnum.NORMAL_VALUE);

			// 接触限制
			messageQueueService.bpublish(QueueConstants.FORCE_OFFLINE_QUEUE, new OperateUserActionDTO(fid, UserStatusEnum.NORMAL_VALUE, Utils.getTimestamp()));
		} else if (status == 3) {// 重设登陆密码
			modelAndView.addObject("statusCode", 200);
			modelAndView.addObject("message", "重设登陆密码成功，密码为:ABC123");
			user.setFloginPassword(Utils.MD5("ABC123"));

		} else if (status == 4) {// 重设交易密码
			modelAndView.addObject("statusCode", 200);
			modelAndView.addObject("message", "重设登陆密码成功，密码为:ABC123");
			user.setFtradePassword(Utils.MD5("ABC123"));
		}

		this.userService.updateObj(user);
		return modelAndView;
	}

	@RequestMapping("/ssadmin/auditUser")
	@RequiresPermissions("ssadmin/auditUser.html")
	public ModelAndView auditUser(HttpServletRequest request) throws Exception {
		ModelAndView modelAndView = new ModelAndView();
		modelAndView.setViewName("ssadmin/comm/ajaxDone");
		int status = Integer.parseInt(request.getParameter("status"));
		int fid = Integer.parseInt(request.getParameter("uid"));

		Fuser user = this.userService.findById(fid);
		if (status == 1) {
			user.setFhasRealValidate(true);
			user.setFhasRealValidateTime(Utils.getTimestamp());
			Fmessage fmessage = new Fmessage();
//			fmessage.setFcontent("恭喜您的实名认证审核通过！");
			fmessage.setFcontent("Congratulations on the approval of your KYC Level 1!");
			fmessage.setFcreateTime(Utils.getTimestamp());
			fmessage.setFreceiver(user);
			fmessage.setFstatus(MessageStatusEnum.NOREAD_VALUE);
//			fmessage.setFtitle("实名认证审核通知");
			fmessage.setFtitle("KYC Level 1 audit notification");
			fmessageService.saveMessage(fmessage);

//			try{
//				String activityTime = constantMap.getString("activityTime");
//				if(Utils.isActivityTime(activityTime)){
//					Map<String, Integer> mapAuth = new HashMap<String, Integer>();
//					mapAuth.put("userId", fid);
//					mapAuth.put("origin", AwardOrDeductTicketOriginEnum.RegisterOrigin);
//					mapAuth.put("addedCount", 1);
//					messageQueueService.publish(QueueConstants.AWARD_TICKET_QUEUE, mapAuth);
//					Fuser recomment = user.getfIntroUser_id();
//					if(recomment != null && recomment.getFid() != 0){
//						Map<String, Integer> mapRcm = new HashMap<String, Integer>();
//						mapRcm.put("userId", recomment.getFid());
//						mapRcm.put("origin", AwardOrDeductTicketOriginEnum.RecommendOrigin);
//						mapRcm.put("addedCount", 1);
//						messageQueueService.publish(QueueConstants.AWARD_TICKET_QUEUE, mapRcm);
//					}
//				}
//			}catch (Exception e){
//				e.printStackTrace();
//			}
		} else {
			user.setFrealName(null);
			user.setFidentityNo(null);
			user.setFhasRealValidate(false);
			user.setFpostRealValidate(false);
			Fmessage fmessage = new Fmessage();
//			fmessage.setFcontent("您的实名认证审核不通过！请如实填写(上传)您的证件信息。");
			fmessage.setFcontent("Your KYC Level 1  does not pass! Please fill in (upload) your document information.");
			fmessage.setFcreateTime(Utils.getTimestamp());
			fmessage.setFreceiver(user);
			fmessage.setFstatus(MessageStatusEnum.NOREAD_VALUE);
			fmessage.setFtitle("KYC Level 1 audit notification");
			fmessageService.saveMessage(fmessage);

		}
		this.userService.updateObj(user);

		modelAndView.addObject("statusCode", 200);
		modelAndView.addObject("callbackType", "closeCurrent");
		modelAndView.addObject("message", "审核成功");

		return modelAndView;
	}

	/**
	 * 修改证件信息
	 * @param request
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("/ssadmin/modifyIdentity")
	@RequiresPermissions("ssadmin/modifyIdentity.html")
	public ModelAndView modifyIdentity(HttpServletRequest request) throws Exception {
		ModelAndView modelAndView = new ModelAndView();
		modelAndView.setViewName("ssadmin/comm/ajaxDone");
		int fid = Integer.parseInt(request.getParameter("uid"));
		String frealName = request.getParameter("frealName");
		String fidentityNo = request.getParameter("fidentityNo");

		if(StringUtils.isEmpty(frealName)){
			modelAndView.addObject("statusCode", 300);
			modelAndView.addObject("callbackType", "closeCurrent");
			modelAndView.addObject("message", "真实姓名不能为空");
			return modelAndView;
		}
		if(StringUtils.isEmpty(fidentityNo)){
			modelAndView.addObject("statusCode", 300);
			modelAndView.addObject("callbackType", "closeCurrent");
			modelAndView.addObject("message", "身份证号不能为空");
			return modelAndView;
		}

		Fuser user = this.userService.findById(fid);
		if(fidentityNo.equals(user.getFidentityNo()) && frealName.equals(user.getFrealName())){
			modelAndView.addObject("statusCode", 300);
			modelAndView.addObject("callbackType", "closeCurrent");
			modelAndView.addObject("message", "并未修改");
			return modelAndView;
		}
		user.setFrealName(frealName);
		user.setFidentityNo(fidentityNo);
		userService.updateObj(user);

		modelAndView.addObject("statusCode", 200);
		modelAndView.addObject("callbackType", "closeCurrent");
		modelAndView.addObject("message", "修改成功");

		return modelAndView;
	}

	@RequestMapping("/ssadmin/auditIdentify")
	@RequiresPermissions("ssadmin/auditIdentify.html")
	public ModelAndView auditIdentify(HttpServletRequest request) throws Exception {
		int status = Integer.parseInt(request.getParameter("status"));
		int fid = Integer.parseInt(request.getParameter("uid"));
		int nation = Integer.parseInt(request.getParameter("nation"));
		String reason = request.getParameter("reason");
		return this.userService.updateIdentify(fid, status, nation, reason);
	}

	@RequestMapping("ssadmin/goUserJSP")
	public ModelAndView goUserJSP(HttpServletRequest request) throws Exception {
		String url = request.getParameter("url");
		ModelAndView modelAndView = new ModelAndView();
		modelAndView.setViewName(url);
		if (request.getParameter("uid") != null) {
			int fid = Integer.parseInt(request.getParameter("uid"));
			Fuser user = this.userService.findById(fid);
			modelAndView.addObject("fuser", user);

			Map map = new HashMap();
			map.put(IdentityTypeEnum.SHENFENZHENG, IdentityTypeEnum
					.getEnumString(IdentityTypeEnum.SHENFENZHENG));
			map.put(IdentityTypeEnum.JUNGUANGZHEN, IdentityTypeEnum
					.getEnumString(IdentityTypeEnum.JUNGUANGZHEN));
			map.put(IdentityTypeEnum.HUZHAO,
					IdentityTypeEnum.getEnumString(IdentityTypeEnum.HUZHAO));
			map.put(IdentityTypeEnum.TAIWAN,
					IdentityTypeEnum.getEnumString(IdentityTypeEnum.TAIWAN));
			map.put(IdentityTypeEnum.GANGAO,
					IdentityTypeEnum.getEnumString(IdentityTypeEnum.GANGAO));
			modelAndView.addObject("identityTypeMap", map);
		}

		Map<Integer,String> map = new HashMap();
		map.put(UserGradeEnum.LEVEL0, UserGradeEnum.getEnumString(UserGradeEnum.LEVEL0));
		map.put(UserGradeEnum.LEVEL1, UserGradeEnum.getEnumString(UserGradeEnum.LEVEL1));
		map.put(UserGradeEnum.LEVEL2, UserGradeEnum.getEnumString(UserGradeEnum.LEVEL2));
		map.put(UserGradeEnum.LEVEL3, UserGradeEnum.getEnumString(UserGradeEnum.LEVEL3));
		map.put(UserGradeEnum.LEVEL4, UserGradeEnum.getEnumString(UserGradeEnum.LEVEL4));
		map.put(UserGradeEnum.LEVEL5, UserGradeEnum.getEnumString(UserGradeEnum.LEVEL5));
		modelAndView.addObject("typeMap", map);

		if (request.getParameter("status") != null) {
			int isAudit = 0;
			if (request.getParameter("status").equals("audit")) {
				isAudit = 1;
			}
			modelAndView.addObject("isAudit", isAudit);
		}
		return modelAndView;
	}

	@RequestMapping("ssadmin/updateIntroPerson")
	@RequiresPermissions("ssadmin/updateIntroPerson.html")
	public ModelAndView updateIntroPerson(HttpServletRequest request)
			throws Exception {
		ModelAndView modelAndView = new ModelAndView();
		modelAndView.setViewName("ssadmin/comm/ajaxDone");
		int fid = Integer.parseInt(request.getParameter("uid"));
		Fuser user = this.userService.findById(fid);
		int fintrolId = Integer.parseInt(request.getParameter("fintrolId"));
		Fuser fintrolUser = this.userService.findById(fintrolId);
		if(fintrolUser == null){
			modelAndView.addObject("statusCode", 300);
			modelAndView.addObject("message", "用户不存在");
			return modelAndView;
		}
		user.setfIntroUser_id(fintrolUser);
		this.userService.updateObj(user);

		modelAndView.addObject("statusCode", 200);
		modelAndView.addObject("callbackType", "closeCurrent");
		modelAndView.addObject("message", "修改成功");
		return modelAndView;
	}

	@RequestMapping("ssadmin/updateUserScore")
	@RequiresPermissions("ssadmin/updateUserScore.html")
	public ModelAndView updateUserScore(HttpServletRequest request)
			throws Exception {
		ModelAndView modelAndView = new ModelAndView();
		int fid = Integer.parseInt(request.getParameter("uid"));

		modelAndView.setViewName("ssadmin/comm/ajaxDone");
		modelAndView.addObject("statusCode", 200);
		modelAndView.addObject("callbackType", "closeCurrent");
		modelAndView.addObject("message", "修改成功");
		return modelAndView;
	}


	private static enum ExportFiled {
		会员UID,推荐人UID,会员登陆名,会员状态,证件是否提交,证件是否已审,昵称,真实姓名,累计推荐注册数,是否已手机验证,电话号码,
		邮箱地址,证件类型,证件号码,注册时间,上次登陆时间;
	}

	private List<Fuser> getUserList(HttpServletRequest request) {
		String keyWord = request.getParameter("keywords");
		String orderField = request.getParameter("orderField");
		String orderDirection = request.getParameter("orderDirection");
		StringBuffer filter = new StringBuffer();
		filter.append("where 1=1 \n");
		try {
			if (keyWord != null && keyWord.trim().length() > 0) {
				keyWord = new String(keyWord.getBytes("iso8859-1"),"utf-8");
				filter.append("and (floginName like '%" + keyWord + "%' or \n");
				filter.append("fnickName like '%" + keyWord + "%'  or \n");
				filter.append("frealName like '%" + keyWord + "%'  or \n");
				filter.append("ftelephone like '%" + keyWord + "%'  or \n");
				filter.append("femail like '%" + keyWord + "%'  or \n");
				filter.append("fidentityNo like '%" + keyWord + "%' )\n");
			}
		} catch (UnsupportedEncodingException e1) {
			e1.printStackTrace();
		}

		if (request.getParameter("ftype") != null
				&& request.getParameter("ftype").trim().length() > 0) {
			int type = Integer.parseInt(request.getParameter("ftype"));
			if (type != 0) {
				filter.append("and fstatus=" + request.getParameter("ftype") + " \n");
			}
		}

		try {
			if (request.getParameter("troUid") != null
					&& request.getParameter("troUid").trim().length() > 0) {
				int troUid = Integer.parseInt(request.getParameter("troUid"));
				filter.append("and fIntroUser_id.fid=" + troUid + " \n");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		if (orderField != null && orderField.trim().length() > 0) {
			filter.append("order by " + orderField + "\n");
		} else {
			filter.append("order by fregisterTime \n");
		}

		if (orderDirection != null && orderDirection.trim().length() > 0) {
			filter.append(orderDirection + "\n");
		} else {
			filter.append("desc \n");
		}

		List<Fuser> list = this.userService.list(0, 0, filter + "", false);
		return list;
	}

	@RequestMapping("ssadmin/userExport")
	@RequiresPermissions("ssadmin/userExport.html")
	public ModelAndView userExport(HttpServletRequest request, HttpServletResponse response) throws Exception {
		ModelAndView modelAndView = new ModelAndView();
		response.setContentType("Application/excel");
		response.addHeader("Content-Disposition",
				"attachment;filename=userList.xls");
		XlsExport e = new XlsExport();
		int rowIndex = 0;

		// header
		e.createRow(rowIndex++);
		for (ExportFiled filed : ExportFiled.values()) {
			e.setCell(filed.ordinal(), filed.toString());
		}

		List<Fuser> userList = getUserList(request);
		for (Fuser user : userList) {
			e.createRow(rowIndex++);
			for (ExportFiled filed : ExportFiled.values()) {
				switch (filed) {
					case 会员UID:
						e.setCell(filed.ordinal(), user.getFid());
						break;
					case 推荐人UID:
						if(user.getfIntroUser_id() != null)
							e.setCell(filed.ordinal(), user.getfIntroUser_id().getFid());
						break;
					case 会员登陆名:
						e.setCell(filed.ordinal(), user.getFloginName());
						break;
					case 会员状态:
						e.setCell(filed.ordinal(), user.getFstatus_s());
						break;
					case 证件是否提交:
						e.setCell(filed.ordinal(), user.getFpostRealValidate() + "");
						break;
					case 证件是否已审:
						e.setCell(filed.ordinal(), user.getFhasRealValidate() + "");
						break;
					case 昵称:
						e.setCell(filed.ordinal(), user.getFnickName());
						break;
					case 真实姓名:
						e.setCell(filed.ordinal(), user.getFrealName());
						break;
					case 电话号码:
						e.setCell(filed.ordinal(), user.getFtelephone());
						break;
					case 邮箱地址:
						e.setCell(filed.ordinal(), user.getFemail());
						break;
					case 证件类型:
						e.setCell(filed.ordinal(), user.getFidentityType_s());
						break;
					case 证件号码:
						e.setCell(filed.ordinal(), user.getFidentityNo());
						break;
					case 注册时间:
						e.setCell(filed.ordinal(), user.getFregisterTime());
						break;
					case 上次登陆时间:
						e.setCell(filed.ordinal(), user.getFlastLoginTime());
						break;
					default:
						break;
				}
			}
		}

		e.exportXls(response);

		modelAndView.setViewName("ssadmin/comm/ajaxDone");
		modelAndView.addObject("statusCode", 200);
		modelAndView.addObject("message", "导出成功");
		return modelAndView;
	}

	private static enum GradeDetailFiled {
		会员UID,会员账号,会员姓名,会员电话,自身级别,推荐有效总人数,有效用户,金豆初级玩家,金豆资深玩家,金豆圈子成员,金豆圈子领袖,其他;
	}

	public List getUserGradeList(HttpServletRequest request) throws Exception {
		// 当前页
		int currentPage = 1;
		// 搜索关键字
		String keyWord = request.getParameter("keywords");
		if (request.getParameter("pageNum") != null) {
			currentPage = Integer.parseInt(request.getParameter("pageNum"));
		}
		StringBuffer filter = new StringBuffer();
		filter.append("where 1=1 \n");
		if (keyWord != null && keyWord.trim().length() > 0) {
			filter.append("and (t.floginName like '%" + keyWord + "%' or \n");
			filter.append("t.frealName like '%" + keyWord + "%' )\n");
		}
		String ftype = request.getParameter("ftype");
		if(ftype != null && ftype.trim().length() >0){
			if(!ftype.equals("-1")){
				int t = Integer.parseInt(ftype);
				if(t == UserGradeEnum.LEVEL1){
					filter.append("and t.fownerGrade='"+UserGradeEnum.getEnumString(UserGradeEnum.LEVEL1)+"' \n");
				}else if(t == UserGradeEnum.LEVEL2){
					filter.append("and t.fownerGrade='"+UserGradeEnum.getEnumString(UserGradeEnum.LEVEL2)+"' \n");
				}else if(t == UserGradeEnum.LEVEL3){
					filter.append("and t.fownerGrade='"+UserGradeEnum.getEnumString(UserGradeEnum.LEVEL3)+"' \n");
				}else if(t == UserGradeEnum.LEVEL4){
					filter.append("and t.fownerGrade='"+UserGradeEnum.getEnumString(UserGradeEnum.LEVEL4)+"' \n");
				}else if(t == UserGradeEnum.LEVEL5){
					filter.append("and t.fownerGrade='"+UserGradeEnum.getEnumString(UserGradeEnum.LEVEL5)+"' \n");
				}else if(t == UserGradeEnum.LEVEL0){
					filter.append("and t.fownerGrade='"+UserGradeEnum.getEnumString(UserGradeEnum.LEVEL0)+"' \n");
				}
			}
		}

		String level1 = request.getParameter("level1");
		String level2 = request.getParameter("level2");
		String level3 = request.getParameter("level3");
		String level4 = request.getParameter("level4");
		if(level1 != null && level1.trim().length() >0 && isNumeric(level1)){
			filter.append("and t.level1 >="+level1+" \n");
		}
		if(level2 != null && level2.trim().length() >0 && isNumeric(level2)){
			filter.append("and t.level2 >="+level2+" \n");
		}
		if(level3 != null && level3.trim().length() >0 && isNumeric(level3)){
			filter.append("and t.level3 >="+level3+" \n");
		}
		if(level4 != null && level4.trim().length() >0 && isNumeric(level4)){
			filter.append("and t.level4 >="+level4+" \n");
		}

		List list = this.userService.listUsers((currentPage - 1) * numPerPage, numPerPage, filter + "", true);
		return list;
	}

	@RequestMapping("ssadmin/userGradeDetailExport")
	@RequiresPermissions("ssadmin/userGradeDetailExport.html")
	public ModelAndView userGradeDetailExport(HttpServletRequest request, HttpServletResponse response) throws Exception {
		ModelAndView modelAndView = new ModelAndView();
		response.setContentType("Application/excel");
		response.addHeader("Content-Disposition",
				"attachment;filename=userGradeDetail.xls");
		XlsExport e = new XlsExport();
		int rowIndex = 0;

		// header
		e.createRow(rowIndex++);
		for (GradeDetailFiled filed : GradeDetailFiled.values()) {
			e.setCell(filed.ordinal(), filed.toString());
		}

		List gradeList = getUserGradeList(request);
		Iterator it = gradeList.iterator();
		while(it.hasNext()){
			Object[] o = (Object[])it.next();
			e.createRow(rowIndex++);
			for (GradeDetailFiled filed : GradeDetailFiled.values()) {
				switch (filed) {
					case 会员UID:
						e.setCell(filed.ordinal(), o[0]+"");
						break;
					case 会员账号:
						e.setCell(filed.ordinal(), o[1]+"");
						break;
					case 会员姓名:
						e.setCell(filed.ordinal(), o[2]+"");
						break;
					case 会员电话:
						e.setCell(filed.ordinal(), o[3]+"");
						break;
					case 自身级别:
						e.setCell(filed.ordinal(), o[4]+"");
						break;
					case 推荐有效总人数:
						e.setCell(filed.ordinal(), o[5]+"");
						break;
					case 有效用户:
						e.setCell(filed.ordinal(), o[6]+"");
						break;
					case 金豆初级玩家:
						e.setCell(filed.ordinal(),o[7]+"");
						break;
					case 金豆资深玩家:
						e.setCell(filed.ordinal(), o[8]+"");
						break;
					case 金豆圈子成员:
						e.setCell(filed.ordinal(), o[9]+"");
						break;
					case 金豆圈子领袖:
						e.setCell(filed.ordinal(), o[10]+"");
						break;
					case 其他:
						e.setCell(filed.ordinal(), o[11]+"");
						break;
					default:
						break;
				}
			}
		}

		e.exportXls(response);

		modelAndView.setViewName("ssadmin/comm/ajaxDone");
		modelAndView.addObject("statusCode", 200);
		modelAndView.addObject("message", "导出成功");
		return modelAndView;
	}

	@RequestMapping("/ssadmin/setUserNo")
	@RequiresPermissions("ssadmin/setUserNo.html")
	public ModelAndView setUserNo(HttpServletRequest request) throws Exception {
		ModelAndView modelAndView = new ModelAndView();
		modelAndView.setViewName("ssadmin/comm/ajaxDone");
		int fid = Integer.parseInt(request.getParameter("fid"));
		Fuser user = this.userService.findById(fid);
		String userNo = request.getParameter("fuserNo");
		if(userNo == null || userNo.trim().length() ==0){
			modelAndView.addObject("statusCode", 300);
			modelAndView.addObject("message", "商家号不能为空");
			return modelAndView;
		}else if(userNo.trim().length() >100){
			modelAndView.addObject("statusCode", 300);
			modelAndView.addObject("message", "商家号长度不能大于100个字符");
			return modelAndView;
		}

		String filter = "where fuserNo='"+userNo+"'";
		List<Fuser> fusers = this.userService.list(0, 0, filter, false);
		if(fusers.size() >0){
			modelAndView.addObject("statusCode", 300);
			modelAndView.addObject("message", "该商家号已存在！");
			return modelAndView;
		}

		this.userService.updateObj(user);

		modelAndView.addObject("statusCode", 200);
		modelAndView.addObject("callbackType","closeCurrent");
		modelAndView.addObject("message", "商家号设置成功");
		return modelAndView;
	}

	@RequestMapping("/ssadmin/cancelPhone")
	@RequiresPermissions("ssadmin/cancelPhone.html")
	public ModelAndView cancelPhone(HttpServletRequest request) throws Exception {

		ModelAndView modelAndView = new ModelAndView();
		modelAndView.setViewName("ssadmin/comm/ajaxDone");
		int fid = Integer.parseInt(request.getParameter("uid"));
		Fuser fuser = this.userService.findById(fid);
		fuser.setFtelephone(null);
		fuser.setFareaCode(null);
		this.userService.updateObj(fuser);
		modelAndView.addObject("statusCode", 200);
		modelAndView.addObject("message", "操作成功");
		return modelAndView;
	}

	/**
	 * 会员推荐列表
	 * @param request
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("/ssadmin/userIntroList")
	@RequiresPermissions("ssadmin/entrustlogList.html")
	public ModelAndView userIntroList(HttpServletRequest request) throws Exception {
		ModelAndView modelAndView = new ModelAndView();
		modelAndView.setViewName("ssadmin/userIntroList");
		// 当前页
		int currentPage = 1;
		// 搜索关键字
		String keyWord = request.getParameter("keywords");
		String orderField = request.getParameter("orderField");
		String orderDirection = request.getParameter("orderDirection");
		if (request.getParameter("pageNum") != null) {
			currentPage = Integer.parseInt(request.getParameter("pageNum"));
		}
		StringBuffer filter = new StringBuffer();
		filter.append("where 1=1 \n");
		if (keyWord != null && keyWord.trim().length() > 0) {
			filter.append("and (parent.frealName like '%" + keyWord + "%'  or \n");
			filter.append("parent.floginName like '%" + keyWord + "%'  or \n");
			filter.append("parent.fid like '%" + keyWord + "%'  ) \n");
			modelAndView.addObject("keywords", keyWord);
		}

		if (orderField != null && orderField.trim().length() > 0) {
			filter.append("order by " + orderField + "\n");
		} else {
			filter.append("order by createTime \n");
		}

		if (orderDirection != null && orderDirection.trim().length() > 0) {
			filter.append(orderDirection + "\n");
		} else {
			filter.append("desc \n");
		}
		List<SpreadLog> list = this.spreadLogService.list((currentPage - 1)
				* numPerPage, numPerPage, filter + "", true);
		modelAndView.addObject("list", list);
		modelAndView.addObject("numPerPage", numPerPage);
		modelAndView.addObject("currentPage", currentPage);
		// 总数量
		modelAndView.addObject("totalCount",
				this.adminService.getAllCount("SpreadLog", filter + ""));
		return modelAndView;
	}

	/**
	 * 分润列表
	 * @param request
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("/ssadmin/profitList")
	@RequiresPermissions("ssadmin/entrustlogList.html")
	public ModelAndView profitList(HttpServletRequest request) throws Exception {
		ModelAndView modelAndView = new ModelAndView();
		modelAndView.setViewName("ssadmin/profitList");
		// 当前页
		int currentPage = 1;
		// 搜索关键字
		String keyWord = request.getParameter("keywords");//上级用户信息
		String orderField = request.getParameter("orderField");
		String orderDirection = request.getParameter("orderDirection");
		String createTime=request.getParameter("createTime");//创建时间
		String shareTime=request.getParameter("shareTime");//分润时间

		if (request.getParameter("pageNum") != null) {
			currentPage = Integer.parseInt(request.getParameter("pageNum"));
		}
		StringBuffer filter = new StringBuffer();
		filter.append("where 1=1 \n");
		if (keyWord != null && keyWord.trim().length() > 0) {
			filter.append(" and parent.fid like '%"+keyWord+"'");

			modelAndView.addObject("keywords", keyWord);
		}
		//币种
        if(request.getParameter("coin")!=null && request.getParameter("coin").length() > 0){
			String  coin =request.getParameter("coin");
			filter.append("and coin='"+coin+"'\n");

			modelAndView.addObject("coin", coin);
	    }else{
			modelAndView.addObject("coin", "");
		}


		//类型，0 买  1卖
		String typeKey = request.getParameter("type");
		if(typeKey != null && typeKey.trim().length() >0 && !"-1".equals(typeKey)){
			filter.append("and type="+typeKey+" \n");
			modelAndView.addObject("type", typeKey);
		}else{
			modelAndView.addObject("type", -1);
		}
     //交易对
		if(request.getParameter("market") != null){
			int marketKey = Integer.parseInt(request.getParameter("market"));
			if(marketKey != 0){
				filter.append("and market="+marketKey+"\n");
			}
			modelAndView.addObject("market", marketKey);
		}else{
			modelAndView.addObject("market", 0);
		}

		if (StringUtils.hasText(createTime)) {
			filter.append("and DATE_FORMAT(createTime, '%Y-%m-%d') = ").append("'" + createTime + "' ");
			modelAndView.addObject("createTime", createTime);
		}
		if (StringUtils.hasText(shareTime)) {
			filter.append("and DATE_FORMAT(shareTime, '%Y-%m-%d') = ").append("'" + shareTime + "' ");
			modelAndView.addObject("shareTime", shareTime);
		}
		if (orderField != null && orderField.trim().length() > 0) {
			filter.append("order by " + orderField + "\n");
		} else {
			filter.append("order by createTime \n");
		}

		if (orderDirection != null && orderDirection.trim().length() > 0) {
			filter.append(orderDirection + "\n");
		} else {
			filter.append("desc \n");
		}
		//币种list
		List<Fvirtualcointype> type = this.virtualCoinService.findAll();
		Map typeMap = new HashMap();
		for (Fvirtualcointype fvirtualcointype : type) {
			typeMap.put(fvirtualcointype.getFid(), fvirtualcointype.getfShortName());
		}
		//typeMap.put(0, "全部");
		modelAndView.addObject("fvirtualcointypeMap", typeMap);
		//交易对list
		List<Market> markets = marketService.findAll();
		Map marketMap = new HashMap();
		for(Market market: markets){
			marketMap.put(market.getId(), typeMap.get(market.getSellId()) + "/" + typeMap.get(market.getBuyId()));
		}
		marketMap.put(0, "全部");
		modelAndView.addObject("typeMap", marketMap);

		List<TradeFeesShare> list = this.tradeFeesShareService.list((currentPage - 1) * numPerPage, numPerPage, filter
				+ "", true);
		modelAndView.addObject("list", list);
		modelAndView.addObject("numPerPage", numPerPage);
		modelAndView.addObject("currentPage", currentPage);
		// 总数量
		modelAndView.addObject("totalCount",
				this.adminService.getAllCount("TradeFeesShare", filter + ""));
		return modelAndView;
	}

	/**
	 * 导出分润列表
	 * @param request
	 * @return
	 * @throws Exception
	 */

	@RequestMapping("/ssadmin/exportProfitListTable")
	@RequiresPermissions("ssadmin/entrustlogList.html")
	public void exportProfitList(HttpServletRequest request, HttpServletResponse response) throws Exception {
		ModelAndView modelAndView = new ModelAndView();
		modelAndView.setViewName("ssadmin/profitList");
		// 当前页
		int currentPage = 1;
		// 搜索关键字
		String keyWord = request.getParameter("keywords");//上级用户信息
		String orderField = request.getParameter("orderField");
		String orderDirection = request.getParameter("orderDirection");
		String createTime=request.getParameter("createTime");//创建时间
		String shareTime=request.getParameter("shareTime");//分润时间

		if (request.getParameter("pageNum") != null) {
			currentPage = Integer.parseInt(request.getParameter("pageNum"));
		}
		StringBuffer filter = new StringBuffer();
		filter.append("where 1=1 \n");
		if (keyWord != null && keyWord.trim().length() > 0) {
			filter.append(" and parent.fid like '%"+keyWord+"'");

			modelAndView.addObject("keywords", keyWord);
		}
		//币种
		if(request.getParameter("coin")!=null && request.getParameter("coin").length() > 0){
			String  coin =request.getParameter("coin");
			filter.append("and coin='"+coin+"'\n");

			modelAndView.addObject("coin", coin);
		}else{
			modelAndView.addObject("coin", "");
		}


		//类型，0 买  1卖
		String typeKey = request.getParameter("type");
		if(typeKey != null && typeKey.trim().length() >0 && !"-1".equals(typeKey)){
			filter.append("and type="+typeKey+" \n");
			modelAndView.addObject("type", typeKey);
		}else{
			modelAndView.addObject("type", -1);
		}
		//交易对
		if(request.getParameter("market") != null){
			int marketKey = Integer.parseInt(request.getParameter("market"));
			if(marketKey != 0){
				filter.append("and market="+marketKey+"\n");
			}
			modelAndView.addObject("market", marketKey);
		}else{
			modelAndView.addObject("market", 0);
		}

		if (StringUtils.hasText(createTime)) {
			filter.append("and DATE_FORMAT(createTime, '%Y-%m-%d') = ").append("'" + createTime + "' ");
			modelAndView.addObject("createTime", createTime);
		}
		if (StringUtils.hasText(shareTime)) {
			filter.append("and DATE_FORMAT(shareTime, '%Y-%m-%d') = ").append("'" + shareTime + "' ");
			modelAndView.addObject("shareTime", shareTime);
		}
		if (orderField != null && orderField.trim().length() > 0) {
			filter.append("order by " + orderField + "\n");
		} else {
			filter.append("order by createTime \n");
		}

		if (orderDirection != null && orderDirection.trim().length() > 0) {
			filter.append(orderDirection + "\n");
		} else {
			filter.append("desc \n");
		}
		//币种list
		List<Fvirtualcointype> type = this.virtualCoinService.findAll();
		Map typeMap = new HashMap();
		for (Fvirtualcointype fvirtualcointype : type) {
			typeMap.put(fvirtualcointype.getFid(), fvirtualcointype.getfShortName());
		}
		//typeMap.put(0, "全部");
		modelAndView.addObject("fvirtualcointypeMap", typeMap);
		//交易对list
		List<Market> markets = marketService.findAll();
		Map marketMap = new HashMap();
		for(Market market: markets){
			marketMap.put(market.getId(), typeMap.get(market.getSellId()) + "/" + typeMap.get(market.getBuyId()));
		}
		marketMap.put(0, "全部");
		modelAndView.addObject("typeMap", marketMap);

		List<TradeFeesShare> list = this.tradeFeesShareService.list((currentPage - 1) * numPerPage, numPerPage, filter
				+ "", false);

		// 标题
		XlsExport xls = new XlsExport();
		xls.createRow(0);

		xls.setCell(0, "上级用户");
      /*  xls.setCell(1, "手续费");*/
		xls.setCell(1, "下级用户");
		xls.setCell(2, "交易对");
		xls.setCell(3, "交易类型");
		xls.setCell(4, "分润数量");
		xls.setCell(5, "分润币种");
		xls.setCell(6, "分润时间");
		xls.setCell(7, "生成时间");

		// 填入数据
		for (int i = 1, len = list.size(); i <= len; i++) {
			TradeFeesShare tradeFeesShare  = list.get(i - 1);
			xls.createRow(i);
			xls.setCell(0, tradeFeesShare.getParent().getFid());
         /*   xls.setCell(1, map.get("ffees") == null ? "" : map.get("ffees").toString());*/
			xls.setCell(1,  tradeFeesShare.getChild().getFid());
			xls.setCell(2, marketMap.get(tradeFeesShare.getMarket()).toString());
			xls.setCell(3,tradeFeesShare.getType()==0?"买入":"卖出");
			xls.setCell(4,tradeFeesShare.getAmount());
			xls.setCell(5,tradeFeesShare.getCoin());
			xls.setCell(6,tradeFeesShare.getShareTime());
			xls.setCell(7,tradeFeesShare.getCreateTime());
		}

		try {
			SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
			response.setContentType("application/vnd.ms-excel");
			response.setHeader("Content-Disposition", "attachment;filename=" + URLEncoder.encode("会员分润表-", "utf-8") + format.format(new Date()) + ".xls");
			xls.exportXls(response);
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}



	}


	@RequestMapping("/ssadmin/cancelEmail")
	@RequiresPermissions("ssadmin/cancelEmail.html")
	public ModelAndView cancelEmail(HttpServletRequest request){
		ModelAndView modelAndView = new ModelAndView();
		modelAndView.setViewName("ssadmin/comm/ajaxDone");
		int fid = Integer.parseInt(request.getParameter("uid"));
		Fuser fuser = this.userService.findById(fid);
		fuser.setFemail(null);
		this.userService.updateObj(fuser);

		modelAndView.addObject("statusCode", 200);
		modelAndView.addObject("message", "操作成功");
		return modelAndView;
	}

	@RequestMapping("/ssadmin/setNeedFee")
	@ResponseBody
	@RequiresPermissions({"ssadmin/setNeedFee.html?fneedFee=false", "ssadmin/setNeedFee.html?fneedFee=true"})
	public Object setNeedFee(int uid, boolean fneedFee) {
		// 更新用户手续费设置
		Fuser fuser = this.userService.findById(uid);
		fuser.setFneedFee(fneedFee);
		this.userService.updateObj(fuser);

		Map<String, Object> ret = new HashMap<String, Object>();
		ret.put("statusCode", 200);
		ret.put("message", "手续费设置成功");
		return ret;
	}

	@RequestMapping("/ssadmin/setCanOtc")
	@ResponseBody
	@RequiresPermissions({"ssadmin/setNeedFee.html?fneedFee=false", "ssadmin/setNeedFee.html?fneedFee=true"})
	public Object setCanOtc(int uid, boolean canOtc) {
		Fuser fuser = this.userService.findById(uid);
		fuser.setCanOtc(canOtc);
		this.userService.updateObj(fuser);

		Map<String, Object> ret = new HashMap();
		ret.put("statusCode", 200);
		ret.put("message", "OTC功能设置成功");
		return ret;
	}

}

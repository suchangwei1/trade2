package com.trade.service.admin;

import com.trade.Enum.MessageStatusEnum;
import com.trade.comm.ConstantMap;
import com.trade.dao.FuserDAO;
import com.trade.dao.FvirtualwalletDAO;
import com.trade.model.Fmessage;
import com.trade.model.Fuser;
import com.trade.model.SpreadLog;
import com.trade.service.front.FmessageService;
import com.trade.service.front.SpreadLogService;
import com.trade.util.Utils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;
import java.util.Map;

@Service
public class UserService {
	@Autowired
	private FuserDAO fuserDAO;
	@Autowired
	private FvirtualwalletDAO fvirtualwalletDAO;
	@Autowired
	private ConstantMap constantMap;
	@Autowired
	private FmessageService fmessageService;
	@Autowired
	private SpreadLogService spreadLogService;

	public Fuser findById(int id) {
		Fuser fuser = this.fuserDAO.findById(id);
		if (fuser.getfIntroUser_id() != null) {
			fuser.getfIntroUser_id().getFlastLoginTime();
		}
		return fuser;
	}

	public void saveObj(Fuser obj) {
		this.fuserDAO.save(obj);
	}

	public void deleteObj(int id) {
		Fuser obj = this.fuserDAO.findById(id);
		this.fuserDAO.delete(obj);
	}

	public void updateObj(Fuser obj) {
		this.fuserDAO.attachDirty(obj);
	}

	public ModelAndView updateIdentify(Integer fid, int status, int nation, String reason) {
		ModelAndView modelAndView = new ModelAndView();
		modelAndView.setViewName("ssadmin/comm/ajaxDone");
		String title = "KYC Level 2 audit notification";
		String yes_content = "Congratulations on the approval of your KYC Level 2!";
		String no_content = "Your KYC Level 2 is not approved! The reason is: ";
		if(nation == 0){
			title = "高级身份认证通知";
			yes_content = "恭喜你，通过了高级身份认证！";
			no_content = "高级身份认证未通过，理由为：";
		}
		Fuser user = this.findById(fid);
		if (status == 2) {
			user.setfIdentityStatus(2);
			Fmessage fmessage = new Fmessage();
			fmessage.setFcontent(yes_content);
			fmessage.setFcreateTime(Utils.getTimestamp());
			fmessage.setFreceiver(user);
			fmessage.setFstatus(MessageStatusEnum.NOREAD_VALUE);
			fmessage.setFtitle(title);
			fmessageService.saveMessage(fmessage);

			if(user.getfIntroUser_id() != null && "1".equals(constantMap.getString("frontInvite"))){
				String title1 = "Invite Rewards";
				String invite = "Invite success for UID: ";
				String invite1 = "! Rewards ";
				String reg = "Sign up success! Rewards ";
				String reg2 = ", inviter's UID: ";
				if(nation == 0){
					title1 = "推广奖励通知";
					invite = "成功推荐下线UID：";
					invite1 = "！奖励";
					reg = "推广注册成功！奖励";
					reg2= "，上线UID：";
				}

				String args1 = constantMap.getString("inviteCoin").trim();
				String args2 = constantMap.getString("regCoin").trim();
				String[] tt1 = args1.split("#");
				String[] tt2 = args2.split("#");
				//被推人消息
				Fmessage fmessage1 = new Fmessage();
				fmessage1.setFcontent(reg + tt2[2] + tt2[1] + reg2 +user.getfIntroUser_id().getFid());
				fmessage1.setFcreateTime(Utils.getTimestamp());
				fmessage1.setFreceiver(user);
				fmessage1.setFstatus(MessageStatusEnum.NOREAD_VALUE);
				fmessage1.setFtitle(title1);
				fmessageService.saveMessage(fmessage);
				fvirtualwalletDAO.updateTotal(user.getFid(), Integer.valueOf(tt2[0]), Integer.valueOf(tt2[2]), Utils.getTimestamp());

				//推荐人消息
				Fmessage fmessage2 = new Fmessage();
				fmessage2.setFcreateTime(Utils.getTimestamp());
				fmessage2.setFstatus(MessageStatusEnum.NOREAD_VALUE);
				fmessage2.setFtitle(title1);
				fmessage2.setFcontent(invite +user.getFid()+ invite1 + tt1[2] + tt1[1]);
				fmessage2.setFreceiver(user.getfIntroUser_id());
				fmessageService.saveMessage(fmessage2);
				fvirtualwalletDAO.updateTotal(user.getfIntroUser_id().getFid(), Integer.valueOf(tt1[0]) , Double.valueOf(tt1[2]), Utils.getTimestamp());
				SpreadLog spreadLog = new SpreadLog(user.getfIntroUser_id(), user, tt1[1], Double.valueOf(tt1[2]), tt2[1], Double.valueOf(tt2[2]));
				spreadLogService.saveObj(spreadLog);
			}

		} else {
			if("".equals(reason)){
				modelAndView.addObject("statusCode", 300);
				modelAndView.addObject("callbackType", "closeCurrent");
				modelAndView.addObject("message", "请填写不通过理由");
				return modelAndView;
			}
			user.setfIdentityPath(null);
			user.setfIdentityPath2(null);
			user.setfIdentityStatus(3);
			Fmessage fmessage = new Fmessage();
			fmessage.setFcontent(no_content + reason);
			fmessage.setFcreateTime(Utils.getTimestamp());
			fmessage.setFreceiver(user);
			fmessage.setFstatus(MessageStatusEnum.NOREAD_VALUE);
			fmessage.setFtitle(title);
			fmessageService.saveMessage(fmessage);
		}
		this.fuserDAO.attachDirty(user);
		modelAndView.addObject("statusCode", 200);
		modelAndView.addObject("callbackType", "closeCurrent");
		modelAndView.addObject("message", "审核成功");
		return modelAndView;
	}

	public List<Fuser> findByProperty(String name, Object value) {
		return this.fuserDAO.findByProperty(name, value);
	}

	public List<Fuser> findAll() {
		return this.fuserDAO.findAll();
	}

	public List<Fuser> list(int firstResult, int maxResults, String filter,
			boolean isFY) {
		List<Fuser> all = this.fuserDAO.list(firstResult, maxResults, filter,
				isFY);
		for (Fuser fuser : all) {
			if(fuser.getfIntroUser_id() != null){
		        fuser.getfIntroUser_id().getFnickName();
			}
		}
		return all;
	}
	
	public List<Fuser> specialList(int firstResult, int maxResults, String filter,
			boolean isFY,String startDate) {
		List<Fuser> all = this.fuserDAO.list(firstResult, maxResults, filter,
				isFY);
		return all;
	}


	public List getUserGroup(String filter) {
		return this.fuserDAO.getUserGroup(filter);
	}

	public List<Fuser> listUserForAudit(int firstResult, int maxResults,
			String filter, boolean isFY) {
		return this.fuserDAO.listUserForAudit(firstResult, maxResults, filter,
				isFY);
	}
	
	public List listUsers(int firstResult, int maxResults,
			String filter, boolean isFY) {
		return this.fuserDAO.listUsers(firstResult, maxResults, filter,
				isFY);
	}
	
}
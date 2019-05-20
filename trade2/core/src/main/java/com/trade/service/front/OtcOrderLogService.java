package com.trade.service.front;

import com.alibaba.fastjson.JSON;
import com.trade.Enum.*;
import com.trade.code.AuthCode;
import com.trade.comm.ConstantMap;
import com.trade.dao.FvirtualwalletDAO;
import com.trade.dao.OtcOrderDao;
import com.trade.dao.OtcOrderLogDao;
import com.trade.model.*;
import com.trade.util.FluentHashMap;
import com.trade.util.MathUtils;
import com.trade.util.StringUtils;
import com.trade.util.Utils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

import java.sql.Timestamp;
import java.util.Date;
import java.util.List;
import java.util.Map;

@Service
public class OtcOrderLogService {
	@Autowired
	private OtcOrderLogDao OtcOrderLogDao;
	@Autowired
	private OtcOrderDao otcOrderDao;
	@Autowired
	private FvirtualwalletDAO fvirtualwalletDAO;
	@Autowired
	private ConstantMap constantMap;
	@Autowired
	private FmessageService fmessageService;
	@Autowired
	private FrontValidateService frontValidateService;
	@Autowired
	private FrontUserService frontUserService;
	@Autowired
	private OtcOrderService otcOrderService;
	@Autowired
	private JedisPool jedisPool;

	public void saveObj(OtcOrderLog obj) {
		this.OtcOrderLogDao.save(obj);
	}

	public void saveOrderLog(OtcOrder obj, OtcOrderLog log) {
		if(obj.getType() == 0){//撮合单子为买单
			int row = fvirtualwalletDAO.updateRmb(log.getFuser().getFid(), obj.getFvirtualcointype().getFid(), log.getAmount(), Utils.getTimestamp());
			if(row != 1){
				throw new RuntimeException();
			}
		}
		obj.setFrozenAmount(MathUtils.add(obj.getFrozenAmount(), log.getAmount()));
		this.otcOrderDao.attachDirty(obj);
		this.OtcOrderLogDao.save(log);
		redisPublish(log);
	}

	private void redisPublish(OtcOrderLog log){
		try (Jedis jedis = jedisPool.getResource()) {
			String channel = "otc:message:" + log.getOtcOrder().getFvirtualcointype().getFid() + ":" + log.getOtcOrder().getFuser().getFid() + ":" + log.getFuser().getFid();
			jedis.publish(StringUtils.string2UTF8Bytes(channel), StringUtils.string2UTF8Bytes(JSON.toJSONString("")));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}


	public void deleteObj(int id) {
		OtcOrderLog obj = this.OtcOrderLogDao.findById(id);
		this.OtcOrderLogDao.delete(obj);
	}

	public void updateObj(OtcOrderLog obj) {
		this.OtcOrderLogDao.attachDirty(obj);
	}

	public void updatePay(OtcOrderLog obj) {
		//给卖方发短信通知
		updateSendCode(obj.getOtcOrder().getFuser(), "" + obj.getId());
		this.OtcOrderLogDao.attachDirty(obj);
		redisPublish(obj);
	}

	public void updateCancel(OtcOrderLog obj) {
		OtcOrder otcOrder = otcOrderService.findById(obj.getOtcOrder().getId());
//        if(otcOrder.getFrozenAmount() - otcOrderLog.getAmount() < 0){
//            return forFailureResult(3);
//        }
		if(obj.getOtcOrder().getType() == 0){
			int row = fvirtualwalletDAO.updateRefund(obj.getFuser().getFid(), obj.getOtcOrder().getFvirtualcointype().getFid(), obj.getAmount(), Utils.getTimestamp());
			if(row != 1){
				throw new RuntimeException();
			}
		}
		otcOrder.setFrozenAmount(otcOrder.getFrozenAmount() - obj.getAmount());
		otcOrder.setUpdateTime(Utils.getTimestamp());
		obj.setUpdateTime(Utils.getTimestamp());
		obj.setStatus(3);//取消

		//退款给挂单
		otcOrderDao.attachDirty(otcOrder);
		//给卖方发短信通知
		//sendCode(obj.getOtcOrder().getFuser(), "" + obj.getId());
		redisPublish(obj);
		this.OtcOrderLogDao.attachDirty(obj);
	}

	public void updateSuccess(OtcOrderLog obj) {
		OtcOrder otcOrder = otcOrderService.findById(obj.getOtcOrder().getId());

		int uid = otcOrder.getType() == 0 ? obj.getFuser().getFid(): otcOrder.getFuser().getFid();
		int buyer_id = otcOrder.getType() == 1 ? obj.getFuser().getFid(): otcOrder.getFuser().getFid();
		//买家加币
		int buyer_row = fvirtualwalletDAO.updateTotal(buyer_id, otcOrder.getFvirtualcointype().getFid(), obj.getAmount() * (1 - otcOrder.getFvirtualcointype().getOtcRate()), Utils.getTimestamp());
		if(buyer_row !=1){
			throw new RuntimeException();
		}
		//卖家扣币
		int row = fvirtualwalletDAO.updateDeduct(uid, otcOrder.getFvirtualcointype().getFid(), obj.getAmount(), Utils.getTimestamp());
		if(row != 1){
			throw new RuntimeException();
		}
		//更新挂单
		otcOrder.setSuccessAmount(otcOrder.getSuccessAmount() + obj.getAmount());
		if(otcOrder.getSuccessAmount() >= otcOrder.getAmount()){
			otcOrder.setStatus(1);//已成功
		}
		otcOrder.setFrozenAmount(otcOrder.getFrozenAmount() - obj.getAmount());
		otcOrder.setUpdateTime(Utils.getTimestamp());
		obj.setUpdateTime(Utils.getTimestamp());
		obj.setStatus(2);//成功
		otcOrderDao.attachDirty(otcOrder);
		//给买卖双方发送站内消息
		Fmessage msg = new Fmessage();
		msg.setFcreateTime(Utils.getTimestamp());
		msg.setFcontent("您完成一笔C2C订单，单号为：" +  obj.getId());
		msg.setFreceiver(otcOrder.getFuser());
		//msg.setFcreator((Fadmin)request.getSession().getAttribute("login_admin"));
		msg.setFtitle("C2C订单提醒");
		msg.setFstatus(MessageStatusEnum.NOREAD_VALUE);
		this.fmessageService.saveMessage(msg);

		Fmessage fmessage = new Fmessage();
		fmessage.setFcreateTime(Utils.getTimestamp());
		fmessage.setFcontent("您完成一笔C2C订单，单号为：" +  obj.getId());
		fmessage.setFreceiver(obj.getFuser());
		//fmessage.setFcreator((Fadmin)request.getSession().getAttribute("login_admin"));
		fmessage.setFtitle("C2C订单提醒");
		fmessage.setFstatus(MessageStatusEnum.NOREAD_VALUE);
		this.fmessageService.saveMessage(fmessage);
		/*给挂单方增加成交次数*/
		Fuser user = frontUserService.findById(otcOrder.getFuser().getFid());
		user.setOtcTimes(user.getOtcTimes() + 1);
		frontUserService.updateFuser(user);
		/*更新快速单*/
		this.OtcOrderLogDao.attachDirty(obj);
		redisPublish(obj);
	}

	public void updateSendCode(Fuser fuser, String message) {
		if(StringUtils.isEmpty(fuser.getFtelephone())){
			Fvalidateemail fvalidateemail = new Fvalidateemail();
			fvalidateemail.setEmail(fuser.getFemail());
			fvalidateemail.setFtitle("C2C邮箱通知");
			Map params = new FluentHashMap<>().
					fluentPut("templ", constantMap.get("otcEmailTemplate")).
					fluentPut("fromName", constantMap.get("fromName")).
					fluentPut("params",
							new FluentHashMap<>().fluentPut("to", fuser.getFemail()).
									fluentPut("sub",
											new FluentHashMap<>().fluentPut("%code%", message)));
			fvalidateemail.setFcontent(JSON.toJSONString(params));
			fvalidateemail.setFcreateTime(Utils.getTimestamp());
			fvalidateemail.setFstatus(ValidateMailStatusEnum.Not_send);
			fvalidateemail.setFuser(fuser);
			this.frontValidateService.addFvalidateemail(fvalidateemail);
		}else{
			// 保存发送记录
			Fvalidatemessage fvalidatemessage = new Fvalidatemessage() ;
			if((fuser.getFareaCode()+"").indexOf("86") != -1){
				fvalidatemessage.setFcontent(constantMap.getString("otcMessageSign").replace("{s10}", message));
			}else{
				fvalidatemessage.setFcontent(constantMap.getString("otcMessageEnSign").replace("{s10}", message));
			}
			fvalidatemessage.setFcreateTime(Utils.getTimestamp()) ;
			fvalidatemessage.setFphone(fuser.getFareaCode() + fuser.getFtelephone()) ;
			fvalidatemessage.setFstatus(ValidateMessageStatusEnum.Not_send) ;
			this.frontValidateService.addFvalidateMessage(fvalidatemessage) ;
		}
	}

	public OtcOrderLog findById(int id) {
		return OtcOrderLogDao.findById(id);
	}

	public List<OtcOrderLog> list(int firstResult, int maxResults, String filter,
			boolean isFY) {
		return this.OtcOrderLogDao.list(firstResult, maxResults, filter, isFY);
	}

	public int count(String filter){
		return OtcOrderLogDao.count(filter);
	}
}
package com.trade.service.front;

import com.trade.dao.OtcAccountDao;
import com.trade.model.Fuser;
import com.trade.model.OtcAccount;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpSession;
import java.util.List;

@Service
public class OtcAccountService {
	@Autowired
	private OtcAccountDao OtcAccountDao;
	@Autowired
	private FrontUserService frontUserService;

	public void saveObj(OtcAccount obj) {
		this.OtcAccountDao.save(obj);
	}

	public void saveAccount(OtcAccount obj, Fuser fuser, HttpSession session) {
		if(!fuser.isHasOtcSet()){
			Fuser user = frontUserService.findById(fuser.getFid());
			user.setHasOtcSet(true);
			frontUserService.updateFUser(user, session);
		}
		this.OtcAccountDao.attachDirty(obj);
	}

//	public void saveLog(OtcAccount obj) {
//		if(obj.getType() == OtcAccountTypeEnum.SELL){
//			fvirtualwalletDAO.updateRmb(obj.getBuyyer().getFid(), obj.getCoin().getFid(), obj.getAmount(), Utils.getTimestamp());
//		}
//		this.OtcAccountDao.save(obj);
//	}

	public void deleteObj(int id) {
		OtcAccount obj = this.OtcAccountDao.findById(id);
		this.OtcAccountDao.delete(obj);
	}

	public void updateObj(OtcAccount obj) {
		this.OtcAccountDao.attachDirty(obj);
	}

	public OtcAccount findById(int id) {
		return OtcAccountDao.findById(id);
	}

	public OtcAccount findByUserId(int uid) {
		List<OtcAccount> list = this.list(0, 0, "where fuser.fid = " + uid, false);
		if(list != null && list.size() > 0){
			return list.get(0);
		}else{
			return null;
		}
	}

//	public void updateStatus(OtcAccount OtcAccount, int status) {
//		OtcAccount.setStatus(OtcAccountStatusEnum.get(status));
//		if(OtcAccount.getType() == OtcAccountTypeEnum.BUY){
//			if(status == OtcAccountStatusEnum.PASSED.getIndex()){
//				//增加ftotal
//				fvirtualwalletDAO.updateTotal(OtcAccount.getBuyyer().getFid(), OtcAccount.getCoin().getFid(), OtcAccount.getAmount(), Utils.getTimestamp());
//			}
//		}else{
//			if(status == OtcAccountStatusEnum.PASSED.getIndex()){
//				//扣除ffrozen
//				fvirtualwalletDAO.updateDeduct(OtcAccount.getBuyyer().getFid(), OtcAccount.getCoin().getFid(), OtcAccount.getAmount(), Utils.getTimestamp());
//			}else{
//				//扣除ffrozen并加ftotal
//				fvirtualwalletDAO.updateRefund(OtcAccount.getBuyyer().getFid(), OtcAccount.getCoin().getFid(), OtcAccount.getAmount(), Utils.getTimestamp());
//			}
//		}
//		OtcAccountDao.save(OtcAccount);
//	}

	public List<OtcAccount> list(int firstResult, int maxResults, String filter,
			boolean isFY) {
		return this.OtcAccountDao.list(firstResult, maxResults, filter, isFY);
	}

//	public List<OtcAccount> listMyLog(int uid) {
//		StringBuffer filter = new StringBuffer();
//		filter.append("where buyyer.fid = " + uid + " order by id desc");
//		return this.OtcAccountDao.list(0, 10, filter + "", true);
//	}
}
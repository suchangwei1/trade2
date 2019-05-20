package com.trade.service.front;

import com.trade.dao.FvirtualwalletDAO;
import com.trade.dao.OtcOrderDao;
import com.trade.model.Fuser;
import com.trade.model.OtcOrder;
import com.trade.util.MathUtils;
import com.trade.util.Utils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpSession;
import java.util.List;

@Service
public class OtcOrderService {
	@Autowired
	private OtcOrderDao OtcOrderDao;
	@Autowired
	private FvirtualwalletDAO fvirtualwalletDAO;

	public void saveObj(OtcOrder obj) {
		this.OtcOrderDao.save(obj);
	}

	public void saveOrder(OtcOrder obj) {
		if(obj.getType() == 1){//卖单
			int row = fvirtualwalletDAO.updateRmb(obj.getFuser().getFid(), obj.getFvirtualcointype().getFid(), obj.getAmount(), Utils.getTimestamp());
			if(row != 1){
				throw new RuntimeException();
			}
		}
		this.OtcOrderDao.save(obj);
	}


	public void deleteObj(int id) {
		OtcOrder obj = this.OtcOrderDao.findById(id);
		this.OtcOrderDao.delete(obj);
	}

	public void updateObj(OtcOrder obj) {
		this.OtcOrderDao.attachDirty(obj);
	}


	public void updateOrderStatus(OtcOrder obj) {
		if(obj.getType() == 1){
			int row = fvirtualwalletDAO.updateRefund(obj.getFuser().getFid(), obj.getFvirtualcointype().getFid(), MathUtils.subtract(obj.getAmount(),obj.getSuccessAmount()), Utils.getTimestamp());
			if(row != 1){
				throw new RuntimeException();
			}
		}
		this.updateObj(obj);
	}

	public OtcOrder findById(int id) {
		return OtcOrderDao.findById(id);
	}

	public OtcOrder findByUserId(int uid) {
		List<OtcOrder> list = this.list(0, 0, " where fuser.fid = " + uid, false);
		if(list != null && list.size() > 0){
			return list.get(0);
		}else{
			return null;
		}
	}

	public List<OtcOrder> findByUserIdAndCoin(int uid, int cid) {
		List<OtcOrder> list = this.list(0, 0, " where status = 0 and fvirtualcointype.fid = " + cid +" and fuser.fid = " + uid, false);
		return list;
	}

	public List<OtcOrder> list(int firstResult, int maxResults, String filter,
			boolean isFY) {
		return this.OtcOrderDao.list(firstResult, maxResults, filter, isFY);
	}

	public int count(String filter){
		return OtcOrderDao.count(filter);
	}
}
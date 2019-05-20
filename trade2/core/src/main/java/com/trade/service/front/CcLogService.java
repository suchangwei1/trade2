package com.trade.service.front;

import com.trade.Enum.CcLogStatusEnum;
import com.trade.Enum.CcLogTypeEnum;
import com.trade.dao.CcLogDAO;
import com.trade.dao.FvirtualwalletDAO;
import com.trade.model.CcLog;
import com.trade.util.Utils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.rmi.CORBA.Util;
import java.util.List;

@Service
public class CcLogService {
	@Autowired
	private CcLogDAO CcLogDAO;
	@Autowired
	private FvirtualwalletDAO fvirtualwalletDAO;

	public void saveObj(CcLog obj) {
		this.CcLogDAO.save(obj);
	}

	public void saveLog(CcLog obj) {
		if(obj.getType() == CcLogTypeEnum.SELL){
			int row = fvirtualwalletDAO.updateRmb(obj.getBuyyer().getFid(), obj.getCoin().getFid(), obj.getAmount(), Utils.getTimestamp());
			if(row != 1){
				throw new RuntimeException();
			}
		}
		this.CcLogDAO.save(obj);
	}

	public void deleteObj(int id) {
		CcLog obj = this.CcLogDAO.findById(id);
		this.CcLogDAO.delete(obj);
	}

	public void updateObj(CcLog obj) {
		this.CcLogDAO.attachDirty(obj);
	}

	public CcLog findById(int id) {
		return CcLogDAO.findById(id);
	}

	public void updateStatus(CcLog ccLog, int status) {
		ccLog.setStatus(CcLogStatusEnum.get(status));
		if(ccLog.getType() == CcLogTypeEnum.BUY){
			if(status == CcLogStatusEnum.PASSED.getIndex()){
				//增加ftotal
				int row = fvirtualwalletDAO.updateTotal(ccLog.getBuyyer().getFid(), ccLog.getCoin().getFid(), ccLog.getAmount(), Utils.getTimestamp());
				if(row !=1 ){
					throw new RuntimeException();
				}
			}
		}else{
			if(status == CcLogStatusEnum.PASSED.getIndex()){
				//扣除ffrozen
				int row = fvirtualwalletDAO.updateDeduct(ccLog.getBuyyer().getFid(), ccLog.getCoin().getFid(), ccLog.getAmount(), Utils.getTimestamp());
				if(row !=1 ){
					throw new RuntimeException();
				}
			}else{
				//扣除ffrozen并加ftotal
				int row = fvirtualwalletDAO.updateRefund(ccLog.getBuyyer().getFid(), ccLog.getCoin().getFid(), ccLog.getAmount(), Utils.getTimestamp());
				if(row !=1 ){
					throw new RuntimeException();
				}
			}
		}
		CcLogDAO.save(ccLog);
	}

	public List<CcLog> list(int firstResult, int maxResults, String filter,
			boolean isFY) {
		return this.CcLogDAO.list(firstResult, maxResults, filter, isFY);
	}

	public List<CcLog> listMyLog(int uid) {
		StringBuffer filter = new StringBuffer();
		filter.append("where buyyer.fid = " + uid + " order by id desc");
		return this.CcLogDAO.list(0, 10, filter + "", true);
	}
}
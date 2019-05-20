package com.trade.service.admin;

import java.util.List;

import com.trade.Enum.MessageStatusEnum;
import com.trade.dao.FmessageDAO;
import com.trade.dao.FvirtualoperationlogDAO;
import com.trade.dao.FvirtualwalletDAO;
import com.trade.model.Fmessage;
import com.trade.model.Fvirtualoperationlog;
import com.trade.util.Utils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.trade.model.Fvirtualwallet;

@Service
public class VirtualOperationLogService {
	@Autowired
	private FvirtualoperationlogDAO virtualoperationlogDAO;
	@Autowired
	private FvirtualwalletDAO virtualwalletDao;
	@Autowired
	private FmessageDAO messageDAO;

	public Fvirtualoperationlog findById(int id) {
		Fvirtualoperationlog operationLog = this.virtualoperationlogDAO.findById(id);;
		return operationLog;
	}

	public void saveObj(Fvirtualoperationlog obj) {
		this.virtualoperationlogDAO.save(obj);
	}

	public void deleteObj(int id) {
		Fvirtualoperationlog obj = this.virtualoperationlogDAO.findById(id);
		this.virtualoperationlogDAO.delete(obj);
	}

	public void updateObj(Fvirtualoperationlog obj) {
		this.virtualoperationlogDAO.attachDirty(obj);
	}

	public List<Fvirtualoperationlog> findByProperty(String name, Object value) {
		return this.virtualoperationlogDAO.findByProperty(name, value);
	}

	public List<Fvirtualoperationlog> findAll() {
		return this.virtualoperationlogDAO.findAll();
	}

	public List<Fvirtualoperationlog> list(int firstResult, int maxResults,
			String filter,boolean isFY) {
		return this.virtualoperationlogDAO.list(firstResult, maxResults, filter,isFY);
	}
	
	public void updateVirtualOperationLog(Fvirtualwallet virtualwallet,Fvirtualoperationlog virtualoperationlog) throws RuntimeException{
		this.virtualwalletDao.attachDirty(virtualwallet);
		this.virtualoperationlogDAO.attachDirty(virtualoperationlog);
		if(virtualoperationlog.getFisSendMsg() == 1){
			String title = "System had recharged "+virtualoperationlog.getFqty()+virtualoperationlog.getFvirtualcointype().getfShortName()+". Please pay attention to check!";
			Fmessage msg = new Fmessage();
			msg.setFcreateTime(Utils.getTimestamp());
			msg.setFcontent(title);
			msg.setFreceiver(virtualoperationlog.getFuser());
			msg.setFcreator(virtualoperationlog.getFcreator());
			msg.setFtitle("Deposit Notification");
			msg.setFstatus(MessageStatusEnum.NOREAD_VALUE);
			this.messageDAO.save(msg);
		}
	}

}
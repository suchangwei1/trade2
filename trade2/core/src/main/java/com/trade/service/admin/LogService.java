package com.trade.service.admin;

import java.util.List;

import com.trade.Enum.LogTypeEnum;
import com.trade.model.Fuser;
import com.trade.mq.MessageQueueService;
import com.trade.util.Utils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.trade.dao.FlogDAO;
import com.trade.model.Flog;

@Service
public class LogService {
	@Autowired
	private FlogDAO logDAO;
	@Autowired
	private MessageQueueService messageQueueService;

	public Flog findById(int id) {
		return this.logDAO.findById(id);
	}

	public void saveObj(Flog obj) {
		this.logDAO.save(obj);
	}

	public void deleteObj(int id) {
		Flog obj = this.logDAO.findById(id);
		this.logDAO.delete(obj);
	}

	public void updateObj(Flog obj) {
		this.logDAO.attachDirty(obj);
	}

	public List<Flog> findByProperty(String name, Object value) {
		return this.logDAO.findByProperty(name, value);
	}

	public List<Flog> findAll() {
		return this.logDAO.findAll();
	}

	public List<Flog> list(int firstResult, int maxResults,
			String filter,boolean isFY) {
		return this.logDAO.list(firstResult, maxResults, filter,isFY);
	}

	public void addLoginLog(Fuser fuser, String ip, String browser, String deviceValue, String sessionId){
		Flog flog = new Flog() ;
		flog.setFcreateTime(Utils.getTimestamp()) ;
		flog.setFtype(LogTypeEnum.User_LOGIN) ;
		flog.setFkey1(String.valueOf(fuser.getFid())) ;
		flog.setFkey2(fuser.getFloginName()) ;
		flog.setFkey3(ip);
		flog.setFkey4(browser);
		flog.setFkey5(deviceValue);
		flog.setFkey6(sessionId);
		this.saveObj(flog) ;

	}

	public Flog findLast(Integer id, String fkey5){
		String fkey1 = id.toString();
		return logDAO.findLast(fkey1, fkey5);
	}

	/*public boolean findByUserAndDevice(Integer id, String fkey5){
		String fkey1 = id.toString();
		List<Flog> flogs = logDAO.findGroupByUser(fkey1);
		for (Flog log: flogs) {
			if(fkey5.equals(log.getFkey5())){
				return true;
			}
		}
		return false;
	}*/

	public List<Flog> findGroupByUser(Integer id){
		List<Flog> flogs = logDAO.findGroupByUser(id);
		return flogs;
	}

	/*public boolean findByUser(Integer id){
		String fkey1 = id.toString();
		List<Flog> flogs = logDAO.findGroupByUser(fkey1);
		return StringUtils.isEmpty(flogs) ? false : true;
	}*/

	public Flog findLast(String fkey5){
		return logDAO.findLast(fkey5);
	}

	public List<Flog> findByDevice(String uuid){
		return findByProperty("fkey5", uuid);
	}

	/*public List<Flog> findByUser(Integer id){
		String fkey1 = id.toString();
		return findByProperty("fkey1", fkey1);
	}*/

}
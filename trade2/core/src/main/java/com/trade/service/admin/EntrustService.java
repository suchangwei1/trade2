package com.trade.service.admin;

import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Map;

import com.trade.dao.FentrustDAO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.trade.model.Fentrust;

@Service
public class EntrustService {
	@Autowired
	private FentrustDAO entrustDAO;

	public Fentrust findById(int id) {
		return this.entrustDAO.findById(id);
	}

	public void saveObj(Fentrust obj) {
		this.entrustDAO.save(obj);
	}

	public void deleteObj(int id) {
		Fentrust obj = this.entrustDAO.findById(id);
		this.entrustDAO.delete(obj);
	}

	public void updateObj(Fentrust obj) {
		this.entrustDAO.attachDirty(obj);
	}

	public List<Fentrust> findByProperty(String name, Object value) {
		return this.entrustDAO.findByProperty(name, value);
	}

	public List<Fentrust> findAll() {
		return this.entrustDAO.findAll();
	}

	public List<Fentrust> list(int firstResult, int maxResults,
			String filter,boolean isFY) {
		return this.entrustDAO.list(firstResult, maxResults, filter,isFY);
	}
	
	public List<Map> getTotalQty(int fentrustType, String startDate, String endDate) {
		return this.entrustDAO.getTotalQty(fentrustType, startDate, endDate);
	}
	
	public Map getTotalBuyFees(int fentrustType,String startDate,String endDate) {
		return this.entrustDAO.getTotalBuyFees(fentrustType, startDate, endDate);
	}
	
	/**
	 * 导出委托列表
	 * 
	 * @param keyWord		登录名、昵称、真实姓名
	 * @param coinId		币种id
	 * @param logDate		创建日期 yyyy-MM-dd
	 * @param price			单价
	 * @param entrustType	委托类型
	 * @param status		委托状态	
	 * @param firstResult   
	 * @param maxResult
	 * @return				二维数组
	 */
	public List<Object> findForExport(String keyWord, Integer coinId, String logDate, Double price, Short entrustType, Short status, Integer firstResult, Integer maxResult){
		return entrustDAO.findForExport(keyWord, coinId, logDate, price, entrustType, status, firstResult, maxResult);
	}

	/**
	 * 交易手续费报表
	 * @param startDate
	 * @param endDate
	 * @param symbol
	 * @return
	 */
	public List<String[]> listForTradeFeeReport(Date startDate, Date endDate, int symbol){
		return entrustDAO.listForTradeFeeReport(startDate, endDate, symbol);
	}

	public Collection getOrders(int uid) {
		return entrustDAO.getOrders(uid);
	}

	public Collection getOrders(int uid, int fvifid) {
		return entrustDAO.getOrders(uid, fvifid);
	}
}



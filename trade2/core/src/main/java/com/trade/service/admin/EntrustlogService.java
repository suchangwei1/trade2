package com.trade.service.admin;

import com.trade.dao.FentrustlogDAO;
import com.trade.dao.FvirtualwalletDAO;
import com.trade.model.*;
import com.trade.service.front.TradeFeesShareService;
import com.trade.util.DateUtils;
import com.trade.util.Utils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.management.RuntimeMBeanException;
import java.util.Date;
import java.util.List;
import java.util.Map;

@Service
public class EntrustlogService {
	@Autowired
	private FentrustlogDAO entrustlogDAO;
	@Autowired
	private TradeFeesShareService tradeFeesShareService;
	@Autowired
	private FvirtualwalletDAO fvirtualwalletDAO;

	public Fentrustlog findById(int id) {
		return this.entrustlogDAO.findById(id);
	}

	public List list(int firstResult, int maxResults,
			String filter,boolean isFY) {
		List all = this.entrustlogDAO.list(firstResult, maxResults, filter,isFY);
		return all;
	}

	public void update(Fentrustlog log){
		this.entrustlogDAO.attachDirty(log);
	}


	public void updateShareFees(Fentrustlog fentrustlog, Map<String, Object> map, SpreadLog spreadLog, Map<Integer, Market> marketMap, double amount, Map<Integer, Fvirtualcointype> coinMap ){
		fentrustlog.setIsShared(1);
		this.entrustlogDAO.attachDirty(fentrustlog);
		int shareCoinId;
		if(Integer.valueOf(map.get("type").toString()) == 0){
			shareCoinId = marketMap.get(Integer.valueOf(map.get("market").toString())).getSellId();
		}else{
			shareCoinId = marketMap.get(Integer.valueOf(map.get("market").toString())).getBuyId();
		}
		int row = fvirtualwalletDAO.updateTotal(spreadLog.getParent().getFid(), shareCoinId , amount, Utils.getTimestamp());
		if(row != 1){
			throw new RuntimeException();
		}
		TradeFeesShare tradeFeesShare = new TradeFeesShare(spreadLog.getParent(),
				spreadLog.getChild(),
				Integer.valueOf(map.get("market").toString()),
				Integer.valueOf(map.get("type").toString()),
				fentrustlog.getFcreateTime(),
				coinMap.get(shareCoinId).getfShortName(),
				amount);
		tradeFeesShareService.saveObj(tradeFeesShare);
	}
	
	public int countForList(String filter){
		return entrustlogDAO.countForList(filter);
	}
	
	/**
	 * 统计每天成交量
	 * 
	 * @param coinId
	 * @param entrustType
	 * @param startDate
	 * @param endDate
	 * @return
	 */
	public List<Object> findCountByDay(Integer coinId, Short entrustType, String startDate, String endDate){
		return entrustlogDAO.findCountByDay(coinId, entrustType, startDate, endDate);
	}

	/**
	 * 跌涨幅
	 * @param coinId
	 * @param start
	 * @param end
     * @return
     */
	public double getUpAndDown(int coinId, Date start, Date end){

		return entrustlogDAO.getUpAndDown(coinId, start, end);
	}

	/**
	 * 交易额
	 * @param coinId
	 * @param start
	 * @param end
     * @return
     */
	public double getTradeCount(int coinId, Date start, Date end){

		return entrustlogDAO.getTradeCount(coinId, start, end);
	}

	public List<Fentrustlog> findByProperties(Map<String, Object> propValues, Date startTime, Date endTime, Integer offset, Integer length) {
		return this.entrustlogDAO.findByProperties(propValues, startTime, endTime, offset, length);
	}

	public List<Map<String, Object>> findUnsharedLogs(String beginDate){
		return this.entrustlogDAO.findUnsharedLogs(beginDate);
	}


	public List findAllList(int i, int numPerPage, String s, boolean b) {
		return this.entrustlogDAO.findAllList(i, numPerPage, s, b);
	}

	public int findAllListCount(String s) {
		return this.entrustlogDAO.findAllListCount(s);
	}
}
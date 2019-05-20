package com.trade.service.front;

import com.trade.dao.TradeFeesShareDao;
import com.trade.model.TradeFeesShare;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class TradeFeesShareService {
	@Autowired
	private TradeFeesShareDao TradeFeesShareDao;

	public List<TradeFeesShare> findByProperties(Map obj, Integer offset, Integer length) {
		return this.TradeFeesShareDao.findByProperties(obj, offset, length);
	}

	public void saveObj(TradeFeesShare obj) {
		this.TradeFeesShareDao.save(obj);
	}

	public List<TradeFeesShare> list(int firstResult, int maxResults, String filter,
			boolean isFY) {
		return this.TradeFeesShareDao.list(firstResult, maxResults, filter, isFY);
	}

	public List<Map<String, Object>> listSum(int id) {
		return this.TradeFeesShareDao.listSum(id);
	}

	public int count(String filter){
		return TradeFeesShareDao.count(filter);
	}
}
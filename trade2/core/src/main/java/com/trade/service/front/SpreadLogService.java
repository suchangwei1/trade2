package com.trade.service.front;

import com.trade.dao.SpreadLogDao;
import com.trade.model.Fuser;
import com.trade.model.SpreadLog;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class SpreadLogService {
	@Autowired
	private SpreadLogDao SpreadLogDao;

	public List<SpreadLog> findByProperties(Map obj, Integer offset, Integer length) {
		return this.SpreadLogDao.findByProperties(obj, offset, length);
	}

	public void saveObj(SpreadLog obj) {
		this.SpreadLogDao.save(obj);
	}

	public List<SpreadLog> list(int firstResult, int maxResults, String filter,
			boolean isFY) {
		return this.SpreadLogDao.list(firstResult, maxResults, filter, isFY);
	}

	public List<Map<String, String>> listSum(int id) {
		return this.SpreadLogDao.listSum(id);
	}

	public int count(String filter){
		return SpreadLogDao.count(filter);
	}
}
package com.trade.service.admin;

import java.util.List;

import com.trade.dao.FlimittradeDAO;
import com.trade.model.Flimittrade;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class LimittradeService {
	@Autowired
	private FlimittradeDAO limittradeDAO;

	public Flimittrade findById(int id) {
		return this.limittradeDAO.findById(id);
	}

	public void saveObj(Flimittrade obj) {
		this.limittradeDAO.save(obj);
	}

	public void deleteObj(int id) {
		Flimittrade obj = this.limittradeDAO.findById(id);
		this.limittradeDAO.delete(obj);
	}

	public void updateObj(Flimittrade obj) {
		this.limittradeDAO.attachDirty(obj);
	}

	public List<Flimittrade> findByProperty(String name, Object value) {
		return this.limittradeDAO.findByProperty(name, value);
	}

	public List<Flimittrade> findAll() {
		return this.limittradeDAO.findAll();
	}

	public List<Flimittrade> list(int firstResult, int maxResults,
			String filter,boolean isFY) {
		return this.limittradeDAO.list(firstResult, maxResults, filter,isFY);
	}
}
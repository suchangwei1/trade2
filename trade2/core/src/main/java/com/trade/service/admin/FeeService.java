package com.trade.service.admin;

import java.util.List;

import com.trade.dao.FfeesDAO;
import com.trade.model.Ffees;
import com.trade.util.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class FeeService {
	@Autowired
	private FfeesDAO feesDAO;

	public Ffees findById(int id) {
		return this.feesDAO.findById(id);
	}

	public void saveObj(Ffees obj) {
		this.feesDAO.save(obj);
	}

	public void deleteObj(int id) {
		Ffees obj = this.feesDAO.findById(id);
		this.feesDAO.delete(obj);
	}

	public void updateObj(Ffees obj) {
		this.feesDAO.attachDirty(obj);
	}

	public List<Ffees> findByProperty(String name, Object value) {
		return this.feesDAO.findByProperty(name, value);
	}

	public Ffees findByCoin(int coinId) {
		List<Ffees> list = this.findByProperty("fvirtualcointype.fid", coinId);
		return CollectionUtils.isEmpty(list) ? null : list.get(0);
	}

	public List<Ffees> findAll() {
		return this.feesDAO.findAll();
	}
	
	public List<Ffees> list(int firstResult, int maxResults,
			String filter,boolean isFY) {
		List<Ffees> all = this.feesDAO.list(firstResult, maxResults, filter,isFY);
		return all;
	}
}
package com.trade.service.admin;

import java.util.List;

import com.trade.dao.FcountlimitDAO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.trade.model.Fcountlimit;

@Service
public class CountLimitService {
	@Autowired
	private FcountlimitDAO countlimitDAO;

	public Fcountlimit findById(int id) {
		return this.countlimitDAO.findById(id);
	}

	public void saveObj(Fcountlimit obj) {
		this.countlimitDAO.save(obj);
	}

	public void deleteObj(int id) {
		Fcountlimit obj = this.countlimitDAO.findById(id);
		this.countlimitDAO.delete(obj);
	}

	public void updateObj(Fcountlimit obj) {
		this.countlimitDAO.attachDirty(obj);
	}

	public List<Fcountlimit> findByProperty(String name, Object value) {
		return this.countlimitDAO.findByProperty(name, value);
	}

	public List<Fcountlimit> findAll() {
		return this.countlimitDAO.findAll();
	}

	public List<Fcountlimit> list(int firstResult, int maxResults,
			String filter,boolean isFY) {
		return this.countlimitDAO.list(firstResult, maxResults, filter,isFY);
	}

}
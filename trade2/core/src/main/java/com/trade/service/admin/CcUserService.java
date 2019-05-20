package com.trade.service.admin;

import com.trade.dao.CcUserDAO;
import com.trade.model.CcUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CcUserService {
	@Autowired
	private CcUserDAO CcUserDAO;

	public void saveObj(CcUser obj) {
		this.CcUserDAO.save(obj);
	}

	public void deleteObj(int id) {
		CcUser obj = this.CcUserDAO.findById(id);
		this.CcUserDAO.delete(obj);
	}

	public CcUser findById(int id) {
		return CcUserDAO.findById(id);
	}

	public CcUser findByPhone(String phone){return CcUserDAO.findByPhone(phone);}

	public void updateObj(CcUser obj) {
		this.CcUserDAO.attachDirty(obj);
	}

	public List<CcUser> list(int firstResult, int maxResults, String filter,
			boolean isFY) {
		return this.CcUserDAO.list(firstResult, maxResults, filter, isFY);
	}
}
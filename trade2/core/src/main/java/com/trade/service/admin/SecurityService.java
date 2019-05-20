package com.trade.service.admin;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.trade.dao.FsecurityDAO;
import com.trade.model.Fsecurity;

@Service
public class SecurityService {
	@Autowired
	private FsecurityDAO securityDAO;

	public Fsecurity findById(int id) {
		return this.securityDAO.findById(id);
	}

	public void saveObj(Fsecurity obj) {
		this.securityDAO.save(obj);
	}

	public void deleteObj(int id) {
		Fsecurity obj = this.securityDAO.findById(id);
		this.securityDAO.delete(obj);
	}

	public void updateObj(Fsecurity obj) {
		this.securityDAO.attachDirty(obj);
	}

	public List<Fsecurity> findByProperty(String name, Object value) {
		return this.securityDAO.findByProperty(name, value);
	}

	public List<Fsecurity> findAll() {
		return this.securityDAO.findAll();
	}

	public List<Fsecurity> list(int firstResult, int maxResults,
			String filter,boolean isFY) {
		return this.securityDAO.list(firstResult, maxResults, filter,isFY);
	}
}
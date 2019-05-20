package com.trade.service.admin;

import java.util.List;

import com.trade.dao.FroleDAO;
import com.trade.model.FroleSecurity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.trade.model.Frole;

@Service
public class RoleService {
	@Autowired
	private FroleDAO roleDAO;

	public Frole findById(int id) {
		Frole role = this.roleDAO.findById(id);
		if(role.getFroleSecurities() != null && role.getFroleSecurities().size() >0){
			for (FroleSecurity roleSecurity: role.getFroleSecurities()) {
				roleSecurity.getFrole().getFname();
				roleSecurity.getFsecurity().getFdescription();
			}
		}
		return role;
	}

	public void saveObj(Frole obj) {
		this.roleDAO.save(obj);
	}

	public void deleteObj(int id) {
		Frole obj = this.roleDAO.findById(id);
		this.roleDAO.delete(obj);
	}

	public void updateObj(Frole obj) {
		this.roleDAO.attachDirty(obj);
	}

	public List<Frole> findByProperty(String name, Object value) {
		return this.roleDAO.findByProperty(name, value);
	}

	public List<Frole> findAll() {
		return this.roleDAO.findAll();
	}

	public List<Frole> list(int firstResult, int maxResults,
			String filter,boolean isFY) {
		return this.roleDAO.list(firstResult, maxResults, filter,isFY);
	}
}
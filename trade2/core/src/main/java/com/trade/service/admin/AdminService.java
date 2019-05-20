package com.trade.service.admin;

import java.util.List;

import com.trade.dao.FadminDAO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.trade.model.Fadmin;

@Service
public class AdminService {
	@Autowired
	private FadminDAO fadminDAO;

	public Fadmin findById(int id) {
		return this.fadminDAO.findById(id);
	}

	public void saveObj(Fadmin obj) {
		this.fadminDAO.save(obj);
	}

	public void deleteObj(int id) {
		Fadmin obj = this.fadminDAO.findById(id);
		this.fadminDAO.delete(obj);
	}

	public void updateObj(Fadmin obj) {
		this.fadminDAO.attachDirty(obj);
	}

	public List<Fadmin> findByProperty(String name, Object value) {
		List<Fadmin> all = this.fadminDAO.findByProperty(name, value);
		for (Fadmin fadmin : all) {
			if(fadmin.getFrole() != null){
				fadmin.getFrole().getFname();
			}
		}
		return all;
	}

	public List<Fadmin> findAll() {
		return this.fadminDAO.findAll();
	}

	public List<Fadmin> list(int firstResult, int maxResults,
			String filter,boolean isFY) {
		List<Fadmin> list = this.fadminDAO.list(firstResult, maxResults, filter,isFY);
		for (Fadmin fadmin : list) {
		     if(fadmin.getFrole() != null){
		    	 fadmin.getFrole().getFname();
		     }
		}
		return list;
	}
	
	public boolean updateDatabase(String ip,String port,String dataBase,String name,String password,String dir,String fileName){
		boolean flag = this.fadminDAO.backDatabase(ip,port,dataBase,name,password,dir,fileName);
		return flag;
	}
	
	public List<Fadmin> login(Fadmin fadmin) throws Exception{
		return this.fadminDAO.login(fadmin) ;
	}
	
	public int getAllCount(String tableName,String filter){
		return this.fadminDAO.getAllCount(tableName,filter);
	}
	
	public void updateSql(String SQL){
		this.fadminDAO.updateSql(SQL);
	}
}
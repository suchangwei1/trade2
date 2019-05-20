package com.trade.service.admin;

import com.trade.dao.FvirtualaddressDAO;
import com.trade.dao.FvirtualcointypeDAO;
import com.trade.model.Ffees;
import com.trade.model.Fvirtualcointype;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;


@Service
public class VirtualCoinService {
	@Autowired
	private FvirtualcointypeDAO virtualcointypeDAO;
	@Autowired
	private FvirtualaddressDAO virtualaddressDAO;

	public Fvirtualcointype findById(int id) {
		return this.virtualcointypeDAO.findById(id);
	}

	public void saveObj(Fvirtualcointype obj) {
		this.virtualcointypeDAO.save(obj);
	}

	public void deleteObj(int id) {
		Fvirtualcointype obj = this.virtualcointypeDAO.findById(id);
		this.virtualcointypeDAO.delete(obj);
	}

	public void updateObj(Fvirtualcointype obj) {
		this.virtualcointypeDAO.attachDirty(obj);
	}

	public List<Fvirtualcointype> findByProperty(String name, Object value) {
		return this.virtualcointypeDAO.findByProperty(name, value);
	}

	public List<Fvirtualcointype> findAll() {
		return this.virtualcointypeDAO.findAll();
	}

	public List<Fvirtualcointype> list(int firstResult, int maxResults,
			String filter,boolean isFY) {
		List<Fvirtualcointype> all = this.virtualcointypeDAO.list(firstResult, maxResults, filter,isFY);
		for (Fvirtualcointype fvirtualcointype : all) {
			Set<Ffees> set = fvirtualcointype.getFfees();
			for (Ffees ffees : set) {
				ffees.getWithdraw();
			}
		}
		return all;
	}

	public Map<Long, Fvirtualcointype> findCoinMap() {
		List<Fvirtualcointype> list = this.findAll();
		Map<Long, Fvirtualcointype> map = new HashMap<>();
		for(Fvirtualcointype fvirtualcointype : list) {
			Fvirtualcointype v = new Fvirtualcointype();
			v.setFname(fvirtualcointype.getFname());
			v.setfShortName(fvirtualcointype.getfShortName());
			map.put((long)fvirtualcointype.getFid(), v);
		}

		return map;
	}
	
	public void updateCoinType(Fvirtualcointype virtualcointype) throws RuntimeException{
		this.virtualcointypeDAO.attachDirty(virtualcointype);
	}


	public List<Map<String, Object>> findUserVirtualCoinAddress(String keyword, Integer symbol, String orderField, String orderDirection, int offset, int length, boolean isPage){
		return virtualaddressDAO.findUserVirtualCoinAddress(keyword, symbol, orderField, orderDirection, offset, length, isPage);
	}

	public int countUserVirtualCoinAddress(String keyword, Integer symbol){
		return virtualaddressDAO.countUserVirtualCoinAddress(keyword, symbol);
	}
}













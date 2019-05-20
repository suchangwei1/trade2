package com.trade.service.admin;

import com.trade.dao.FmessageDAO;
import com.trade.dao.FvirtualcointypeDAO;
import com.trade.dao.FvirtualwalletDAO;
import com.trade.model.Fvirtualwallet;
import com.trade.util.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class VirtualWalletService {
	@Autowired
	private FvirtualwalletDAO virtualwalletDAO;
	@Autowired
	private FmessageDAO messageDAO;
	@Autowired
	private FvirtualcointypeDAO virtualcointypeDAO;

	public Fvirtualwallet findById(int id) {
		return this.virtualwalletDAO.findById(id);
	}

	public void saveObj(Fvirtualwallet obj) {
		this.virtualwalletDAO.save(obj);
	}

	public void deleteObj(int id) {
		Fvirtualwallet obj = this.virtualwalletDAO.findById(id);
		this.virtualwalletDAO.delete(obj);
	}

	public void updateObj(Fvirtualwallet obj) {
		this.virtualwalletDAO.attachDirty(obj);
	}

	public List<Fvirtualwallet> findByProperty(String name, Object value) {
		return this.virtualwalletDAO.findByProperty(name, value);
	}
	
	public List findByTwoProperty(String propertyName1, Object value1,String propertyName2, Object value2) {
		return this.virtualwalletDAO.findByTwoProperty(propertyName1,value1,propertyName2, value2);
	}

	public List<Fvirtualwallet> findAll() {
		return this.virtualwalletDAO.findAll();
	}

	public List<Fvirtualwallet> list(int firstResult, int maxResults,
			String filter,boolean isFY) {
		return this.virtualwalletDAO.list(firstResult, maxResults, filter,isFY);
	}
	
	public List<Map> getTotalQty() {
		return this.virtualwalletDAO.getTotalQty();
	}
	
	/**
	 * 导出虚拟币钱包大于0.01的会员信息
	 * 
	 * @param symbol
	 * @return
	 */
	public List<Map<String, Object>> findViWalletForExport(String keyword, Integer symbol){
		return virtualwalletDAO.findForExport(keyword, symbol, 0, 65535, true);
	}

	public List<Integer> findNotAssign(int coinType, Integer lastUserId, Integer offset, Integer length) {
		return this.virtualwalletDAO.findNotAssign(coinType, lastUserId, offset, length);
	}

	public int countNotAssign(int coinType) {
		return this.virtualwalletDAO.countNotAssign(coinType);
	}

	public void insertAll(List<Map> list) {
		this.virtualwalletDAO.insertAll(list);
	}

	public Fvirtualwallet findByUser(int userId, int coinType) {
		List<Fvirtualwallet> list = this.findByTwoProperty("fuser.fid", userId, "fvirtualcointype.fid", coinType);
		return CollectionUtils.isEmpty(list) ? null : list.get(0);
	}

}

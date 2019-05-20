package com.trade.service.admin;

import com.trade.dao.FwalletDAO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class WalletService {
	@Autowired
	private FwalletDAO fwalletDAO;

	public List<Map<String, Object>> findWalletErrorList(Integer symbol, String keyword, String orderField, String orderDirection, int offset, int length, boolean isPage){
		return fwalletDAO.findWalletErrorList(symbol, keyword, orderField, orderDirection, offset, length, isPage);
	}

	public int countWalletErrorList(Integer symbol, String keyword){
		int count = 0;
		if(null == symbol || symbol > 0){
			count += fwalletDAO.countCoinWalletError(symbol, keyword);
		}
		return count;
	}
}



















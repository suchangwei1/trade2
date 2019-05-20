package com.trade.service;

import com.trade.dao.FentrustDAO;
import com.trade.dao.FentrustlogDAO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.trade.model.Fentrust;

@Service
public class BaseService {
	
	@Autowired
    FentrustDAO fentrustDAO;
	@Autowired
    FentrustlogDAO fentrustlogDAO;
	
	public Fentrust findByFid(int id){
		Fentrust fentrust = fentrustDAO.findById(id);
		return fentrust;
	}
}

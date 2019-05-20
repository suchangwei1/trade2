package com.trade.service.front;

import com.trade.dao.FmessageDAO;
import com.trade.model.Fmessage;
import com.trade.service.BaseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class FrontQuestionService extends BaseService {

	@Autowired
	private FmessageDAO fmessageDAO ;

	public int findFmessageByParamCount(Map<String, Object> param) {
		return this.fmessageDAO.findFmessageByParamCount(param) ;
	}
	
	public List<Fmessage> findFmessageByParam(Map<String, Object> param,int firstResult,int maxResult,String order) {
		return this.fmessageDAO.findFmessageByParam(param, firstResult, maxResult, order) ;
	}

	public Fmessage findFmessageById(int id){
		return this.fmessageDAO.findById(id) ;
	}
	public void updateFmessage(Fmessage fmessage){
		this.fmessageDAO.attachDirty(fmessage) ;
	}

}

package com.trade.service.front;

import com.trade.dao.FarticleDAO;
import com.trade.model.Farticle;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class FrontOthersService {

	@Autowired
	private FarticleDAO farticleDAO ;

	public List<Farticle> findArticleByCoinId(int coinId, int firstResult, int maxResults){
		return farticleDAO.findArticleByCoinId(coinId, firstResult, maxResults);
	}

	public int countArticleByCoinId(int coinId){
		return farticleDAO.countByCoinId(coinId);
	}

}

package com.trade.auto;

import com.trade.cache.data.RealTimeDataService;
import org.springframework.beans.factory.annotation.Autowired;

public class OneDayData {

	@Autowired
	private RealTimeDataService realTimeDataService;
	
	public double getLowest(int id){
		return realTimeDataService.getLatestDealData(id).getLowestPrize24();
	}

	public double getHighest(int id){
		return realTimeDataService.getLatestDealData(id).getHighestPrize24();
	}

	public double getTotal(int id){
		return realTimeDataService.getLatestDealData(id).getTotalDeal24();
	}

}

package com.trade.auto;

import com.trade.cache.data.RealTimeDataService;
import com.trade.dto.FentrustData;
import com.trade.model.Fentrust;
import org.springframework.beans.factory.annotation.Autowired;

//实时数据
public class RealTimeData {

	@Autowired
    private RealTimeDataService realTimeDataService;

	public double getLatestDealPrize(int id){
		return realTimeDataService.getLatestDealPrize(id);
	}
	public double getLowestSellPrize(int id){
		return realTimeDataService.getLowestSellPrize(id);
	}

	public void init(){
	}


	public void clear(){
	}

	public void addEntrustBuyMap(int id,Fentrust fentrust) {
		realTimeDataService.addEntrustBuyMap(id, fentrust);
	}

	public void removeEntrustBuyMap(int id,Fentrust fentrust){
		realTimeDataService.removeEntrustBuyMap(id, fentrust);
	}

	/**
	 * 移除机器人订单
	 * @param id
	 * @param fentrustData
	 */
	public void removeEntrustBuyMap(int id, FentrustData fentrustData){
		realTimeDataService.removeEntrustBuyMap(id, fentrustData);
	}


}



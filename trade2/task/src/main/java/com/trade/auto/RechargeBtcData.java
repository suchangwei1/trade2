package com.trade.auto;

import com.trade.Enum.VirtualCapitalOperationInStatusEnum;
import com.trade.Enum.VirtualCapitalOperationTypeEnum;
import com.trade.Enum.VirtualCoinTypeStatusEnum;
import com.trade.model.Fvirtualcaptualoperation;
import com.trade.model.Fvirtualcointype;
import com.trade.service.front.FrontVirtualCoinService;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class RechargeBtcData {
	@Autowired
	private FrontVirtualCoinService frontVirtualCoinService ;

	//保存最后一个交易记录 virtualCoinId-->address
	private Map<Integer, String> tradeRecordMap = new HashMap<Integer, String>() ;
	public String getLastTradeRecordMap(Integer key){
		return this.tradeRecordMap.get(key) ;
	}
	public void setTradeRecordMap(Integer key,String value){
		synchronized (tradeRecordMap) {
			this.tradeRecordMap.put(key, value) ;
		}
	}
	
	private Map<Integer, Map<String, Fvirtualcaptualoperation>> map = new HashMap<Integer, Map<String,Fvirtualcaptualoperation>>() ;
	
	public Integer[] getKeys(){
		Object[] objs = map.keySet().toArray() ;
		Integer[] ints = new Integer[objs.length] ;
		for (int i = 0; i < objs.length; i++) {
			ints[i] = (Integer) objs[i] ;
		}
		return ints ;
	}
	
	public String[] getSubKeys(int id){
		Map<String, Fvirtualcaptualoperation> subMap = this.map.get(id) ;
		if(subMap==null){
			subMap = new HashMap<String, Fvirtualcaptualoperation>() ;
		}
		Object[] objs = subMap.keySet().toArray() ;
		String[] rets = new String[objs.length] ;
		for (int i = 0; i < objs.length; i++) {
			rets[i] = (String)objs[i] ;
		}
		return rets ;
	}
	
	public void put(int id,Map<String, Fvirtualcaptualoperation> subMap){
		synchronized (this) {
			this.map.put(id, subMap) ;
		}
	}
	
	public Fvirtualcaptualoperation subGet(int id,String key){
		Map<String, Fvirtualcaptualoperation> subMap = this.map.get(id) ;
		if(subMap!=null){
			return subMap.get(key) ;
		}
		
		return null ;
	}
	
	public void subPut(int id,String key,Fvirtualcaptualoperation Fvirtualcaptualoperation){
		synchronized (this) {
			Map<String, Fvirtualcaptualoperation> subMap = this.map.get(id) ;
			if(subMap==null){
				subMap = new HashMap<String, Fvirtualcaptualoperation>() ;
			}
			subMap.put(key, Fvirtualcaptualoperation) ;
			this.map.put(id, subMap) ;
		}
	}
	
	public void subRemove(int id,String key){
		synchronized (this) {
			Map<String, Fvirtualcaptualoperation> subMap = this.map.get(id) ;
			if(subMap!=null){
				subMap.remove(key) ;
				this.map.put(id, subMap) ;
			}
		}
	}
	
	
	public void init(){
		readData() ;
	}
	private void readData(){
		List<Fvirtualcointype> fvirtualcointypes = this.frontVirtualCoinService.findFvirtualCoinType(VirtualCoinTypeStatusEnum.Normal) ;
		for (Fvirtualcointype fvirtualcointype : fvirtualcointypes) {
			Map<String, Fvirtualcaptualoperation> tMap = new HashMap<String, Fvirtualcaptualoperation>() ;
			List<Fvirtualcaptualoperation> fvirtualcaptualoperations = 
					this.frontVirtualCoinService.findFvirtualcaptualoperation(
							null, 
							new int[]{VirtualCapitalOperationTypeEnum.COIN_IN}, 
							new int[]{VirtualCapitalOperationInStatusEnum.WAIT_0,VirtualCapitalOperationInStatusEnum.WAIT_1,VirtualCapitalOperationInStatusEnum.WAIT_2}, 
							new Fvirtualcointype[]{fvirtualcointype}, 
							null,-1, -1) ;
			for (Fvirtualcaptualoperation fvirtualcaptualoperation : fvirtualcaptualoperations) {
				tMap.put(fvirtualcaptualoperation.getFtradeUniqueNumber(), fvirtualcaptualoperation) ;
				System.out.print(fvirtualcaptualoperation.getFtradeUniqueNumber());
			}
			this.put(fvirtualcointype.getFid(), tMap) ;
		}
	}
	
	public void clear(){
		this.map = null ;
	}
}

package com.trade.auto;

import java.util.HashMap;
import java.util.Map;
import java.util.TimerTask;

import com.trade.util.Utils;

/*
 * 防CC攻击
 * */
public class DefendCCAttacker extends TimerTask {
	private Map<String, Integer> acccessMap = new HashMap<String, Integer>() ;
	private Map<String, Long> blockMap = new HashMap<String, Long>() ;
	
	public void addBlockMap(String ip){
		synchronized (blockMap) {
			blockMap.put(ip, Utils.getTimestamp().getTime()) ;
		}
	}
	
	public boolean isBlock(String ip){
		if(blockMap.get(ip)!=null){
			return true ;
		}else{
			return false ;
		}
	}
	
	public void removeBlockMap(){
		synchronized (blockMap) {
			Long now = Utils.getTimestamp().getTime() ;
			for (Map.Entry<String, Long> entry : blockMap.entrySet()) {
				Long time = entry.getValue() ;
				if(now-time>30*60*1000L){//30分钟
					blockMap.remove(entry.getKey()) ;
				}
			}
		}
	}
	
	public void addAccessMap(String ip){
		synchronized (acccessMap) {
			String key = ip;
			Integer value = acccessMap.get(key) ;
			if(value==null){
				acccessMap.put(ip, 1) ;
			}else{
				acccessMap.put(ip, value.intValue()+1) ;
			}
		}
	}
	
	public void checkCCAccess(){
		synchronized (acccessMap) {
			for (Map.Entry<String, Integer> entry : acccessMap.entrySet()) {
				String key = entry.getKey() ;
				Integer value = entry.getValue() ;
				
				if(value.intValue()>50){
					this.addBlockMap(key) ;
				}
			}
			acccessMap.clear() ;
		}
	}

	@Override
	public void run() {
		// TODO Auto-generated method stub
		this.checkCCAccess() ;
	}
}

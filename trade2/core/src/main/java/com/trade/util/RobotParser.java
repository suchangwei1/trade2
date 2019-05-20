package com.trade.util;

import java.util.List;

import com.trade.model.Fsystemargs;
import com.trade.service.admin.SystemArgsService;
import org.springframework.beans.factory.annotation.Autowired;

public class RobotParser { 
	@Autowired
	private SystemArgsService systemArgsService;
	
	public  String getValue(String key){
		List<Fsystemargs> args = systemArgsService.findByProperty("fkey",key);
		if(args.size()>0)
			return args.get(0).getFvalue();
		
		return null;
	}
	
	public boolean isOpen(String key){
		String val = getValue(key);
		if(val == null)
			return false;
		
		String[] params = val.split("#"); 
		boolean flag = false;
		if(params.length>=1)
			flag = Boolean.parseBoolean(params[0]);
		return flag;
	}
	
	public double getAmount(String key){
		String val = getValue(key);
		if(val == null)
			return 0;
		
		String[] params = val.split("#"); 
		
		double ret = 0;
		if(params.length>=2)
			ret = Double.parseDouble(params[1]);
		
		return ret;
	}
	
	public double getIdleAmount(String key){
		String val = getValue(key);
		if(val == null)
			return 0;
		
		String[] params = val.split("#"); 
		
		double ret = 0;
		if(params.length>=3)
			ret = Double.parseDouble(params[2]);
		
		return ret;
	}
	
	public int[] getIdleTimeZone(String key){
		String val = getValue(key);
		if(val == null)
			return null;
		
		String[] params = val.split("#"); 
		
		if(params.length>=5){
			int[] rets = new int[2];
			rets[0] = Integer.parseInt(params[3]);
			rets[1] = Integer.parseInt(params[4]);
			return rets;
		}else
			return null;
	}
	
	public double getInterval(String key){
		String val = getValue(key);
		if(val == null)
			return 0;
		
		String[] params = val.split("#"); 
		
		double ret = 0;
		if(params.length>=6)
			ret = Double.parseDouble(params[5]);
		
		return ret;
	}
	
	public double getIdleInterval(String key){
		String val = getValue(key);
		if(val == null)
			return 0;
		
		String[] params = val.split("#"); 
		
		double ret = 0;
		if(params.length>=7)
			ret = Double.parseDouble(params[6]);
		
		return ret;
	}
	
	public double getAdd(String key){
		String val = getValue(key);
		if(val == null)
			return 0;
		
		String[] params = val.split("#"); 
		
		double ret = 0;
		if(params.length>=8)
			ret = Double.parseDouble(params[7]);
		
		return ret; 
	}

}

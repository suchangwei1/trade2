package com.trade.service.front;

import com.trade.dao.FsystemargsDAO;
import com.trade.model.Fsystemargs;
import com.trade.util.Constants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class FrontSystemArgsService {

	@Autowired
	private FsystemargsDAO fsystemargsDAO ;
	
	public String getSystemArgs(String key){
		String value = null ;
		List<Fsystemargs> list = this.fsystemargsDAO.findByFkey(key) ;
		if(list.size()>0){
			value = list.get(0).getFvalue() ;
		}
		return value ;
	}
	
	public Map<String, Object> findAllMap(){
		Map<String, Object> map = new HashMap<>() ;
		List<Fsystemargs> list = this.fsystemargsDAO.findAll() ;
		List<Object> bannerArr = new ArrayList<>();
		List<Object> enBannerArr = new ArrayList<>();
		HashMap<String, String> tempMap = null;
		for (Fsystemargs fsystemargs : list) {
			String key = fsystemargs.getFkey();
			String value = fsystemargs.getFvalue();

			if(key.indexOf(Constants.BIGIMAGE) != -1){
				if(tempMap == null){
					tempMap = new HashMap();
				}else{
					tempMap = (HashMap)tempMap.clone();
				}
				tempMap.put("value", value);
				tempMap.put("url", fsystemargs.getFurl() == null ? "#": fsystemargs.getFurl());
				bannerArr.add(tempMap);
			}else if(key.indexOf(Constants.ENBIGIMAGE) != -1) {
				if(tempMap == null){
					tempMap = new HashMap();
				}else{
					tempMap = (HashMap)tempMap.clone();
				}
				tempMap.put("value", value);
				tempMap.put("url", fsystemargs.getFurl() == null ? "#": fsystemargs.getFurl());
				enBannerArr.add(tempMap);
			}else{
				map.put(key, value) ;
			}
		}
		map.put(Constants.BIGIMAGE, bannerArr) ;
		map.put(Constants.ENBIGIMAGE, enBannerArr) ;
		return map ;
	}

}

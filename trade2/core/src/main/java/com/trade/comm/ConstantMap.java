package com.trade.comm;

//import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import javax.annotation.PostConstruct;

//import com.trade.model.Question;
//import com.trade.service.front.QuestionService;
import com.trade.util.HttpClientUtils;
import com.trade.util.SpringContextUtils;
import com.trade.model.Fvirtualcointype;
import com.trade.service.admin.SystemArgsService;
import com.trade.util.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.trade.Enum.VirtualCoinTypeStatusEnum;
import com.trade.service.front.FrontSystemArgsService;
import com.trade.service.front.FrontVirtualCoinService;

public class ConstantMap {
	private static final Logger log = LoggerFactory.getLogger(ConstantMap.class);
	@Autowired
	private FrontSystemArgsService frontSystemArgsService ;
	@Autowired
	private FrontVirtualCoinService frontVirtualCoinService ;
//	@Autowired
//	private FriendLinkService friendLinkService;
	@Autowired
	private SystemArgsService systemArgsService;
//	@Autowired
//	private QuestionService questionService;

	private Map<String, Object> map = new HashMap<String, Object>() ;

	private Map<String,String> ethPrice=new HashMap();
	public Map<String,String> getEth(){
		return this.ethPrice;
	}

	public void syncEthPrice(){
		/*String price = HttpClientUtils.get("https://www.okcoin.com/api/v1/ticker.do?symbol=eth_usd");
		String rate = HttpClientUtils.get("https://www.okex.com/api/v1/exchange_rate.do");
		this.ethPrice.put("price",price);
		this.ethPrice.put("rate",rate);*/
	}

	private static ConstantMap GLOBAL_INSTANCE = null;
//	private static Comparator<Question> comparator = (q1, q2) ->{
//		if(q1.hashCode() > q2.hashCode()){
//			return 1;
//		}else {
//			return -1;
//		}
//	};

	@PostConstruct
	public void init(){
		log.info("Init SystemArgs ==> ConstantMap.") ;
		Map<String, Object> tMap = this.frontSystemArgsService.findAllMap() ;
		Map frontMap = new HashMap();
		for (Map.Entry<String, Object> entry : tMap.entrySet()) {
			this.put(entry.getKey(), entry.getValue()) ;
			if((entry.getKey().indexOf("article") != -1)){
				frontMap.put(entry.getKey(), entry.getValue()) ;
			}
			if((entry.getKey().indexOf("front") != -1)){
				frontMap.put(entry.getKey(), entry.getValue()) ;
			}
		}
		map.put("frontMap", frontMap) ;
		log.info("Init virtualCoinType ==> ConstantMap.") ;
		List<Fvirtualcointype> fvirtualcointypes= this.frontVirtualCoinService.findFvirtualCoinType(VirtualCoinTypeStatusEnum.Normal) ;
		map.put("virtualCoinType", fvirtualcointypes) ;
	}
	
	public Map<String, Object> getMap(){
		return this.map ;
	}

	
	public synchronized void put(String key,Object value){
		log.info("ConstantMap put key:"+key+",value:"+value+".") ;
		map.put(key, value) ;
	}

	public static Object getProperty(String key) {
		if (GLOBAL_INSTANCE == null) {
			GLOBAL_INSTANCE = SpringContextUtils.getBean(ConstantMap.class);
		}
		if (GLOBAL_INSTANCE != null) {
			return GLOBAL_INSTANCE.map.get(key);
		}
		return null;
	}

	public Object get(String key){
		return map.get(key) ;
	}
	
	public String getString(String key){
		return String.valueOf(map.get(key)) ;
	}

	public String getString(String key, String value){
		String val = getString(key);
		return Objects.isNull(val) ? value : val;
	}

	public int getInt(String key){
		return Integer.valueOf(this.getString(key));
	}

	public int getInt(String key, int value){
		String valStr = this.getString(key);
		return StringUtils.hasText(valStr) ? Integer.valueOf(valStr) : value;
	}

	public double getDouble(String key){
		return Double.valueOf(this.getString(key));
	}

	public double getDouble(String key, double value){
		String valStr = this.getString(key);
		return StringUtils.hasText(valStr) ? Double.valueOf(valStr) : value;
	}
}

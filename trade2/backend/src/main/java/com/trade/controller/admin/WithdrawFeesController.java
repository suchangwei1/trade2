package com.trade.controller.admin;

import com.trade.dao.FentrustlogDAO;
import com.trade.dao.FvirtualcaptualoperationDAO;
import com.trade.dao.FvirtualcointypeDAO;
import com.trade.model.Fvirtualcointype;
import com.trade.model.Market;
import com.trade.service.front.MarketService;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
public class WithdrawFeesController extends BaseController {
	@Autowired
	private FvirtualcaptualoperationDAO fvirtualcaptualoperationDAO;
	@Autowired
	private FvirtualcointypeDAO fvirtualcointypeDAO;
	@Autowired
	private FentrustlogDAO fentrustlogDAO;
	@Autowired
	private MarketService marketService;

	@RequestMapping("ssadmin/withdrawfeeReport.html")
	@RequiresPermissions("ssadmin/withdrawfeeReport.html")
	public ModelAndView withdrawfeeReport() throws Exception {
		ModelAndView modelAndView = new ModelAndView();
		modelAndView.setViewName("ssadmin/withdrawfeeReport");
		String startDate = getRequest().getParameter("startDate");
		String endDate = getRequest().getParameter("endDate");
		List<Map> list = fvirtualcaptualoperationDAO.countFees(startDate, endDate);
		List<Fvirtualcointype> coins = fvirtualcointypeDAO.findAll();
		Map map = new HashMap();
		for(Fvirtualcointype coin: coins){
			map.put(coin.getFid(), coin.getfShortName());
		}
		List<Map> tradeList = fentrustlogDAO.countFees(startDate, endDate);
		List<Market> markets = marketService.findAll();
		Map marketMap = new HashMap();
		for(Market market: markets){
			marketMap.put(market.getId(), map.get(market.getSellId()) + "/" + map.get(market.getBuyId()));
		}
		modelAndView.addObject("endDate", endDate);
		modelAndView.addObject("startDate", startDate);
		modelAndView.addObject("coinMap", map);
		modelAndView.addObject("marketMap", marketMap);
		modelAndView.addObject("feeList", list);
		modelAndView.addObject("tradeList", tradeList);
		return modelAndView;
	}
}

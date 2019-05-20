package com.trade.controller.admin;


import com.trade.model.Fvirtualcointype;
import com.trade.service.admin.VirtualCoinService;
import com.trade.service.admin.WalletService;
import com.trade.util.StringUtils;
import com.trade.util.Utils;
import com.trade.util.XlsExport;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
public class WalletController extends BaseController {
	@Autowired
	private WalletService walletService ;
	@Autowired
	private VirtualCoinService virtualCoinService;
	//每页显示多少条数据
	private int numPerPage = Utils.getNumPerPage();

	/**
	 * 会员资金问题
	 *
	 * @param request
	 * @param symbol   0:rmb
	 * @param keyword
     * @return
     */
	@RequestMapping("ssadmin/walletErrorList")
	@RequiresPermissions("ssadmin/walletErrorList.html")
	public String walletErrorList(HttpServletRequest request, Map<String, Object> map,
								  @RequestParam(required = false, defaultValue = "-1") Integer symbol,
								  @RequestParam(required = false) String keyword,
								  @RequestParam(value = "pageNum", required = false, defaultValue = "1")int page,
								  @RequestParam(required = false, defaultValue = "0")Integer total){
		map.put("symbol", symbol);
		if(symbol < 0){
			symbol = null;
		}

		String orderField = request.getParameter("orderField");
		String orderDirection = request.getParameter("orderDirection");
		List<Map<String, Object>> list = walletService.findWalletErrorList(symbol, keyword, orderField, orderDirection, (page - 1) * numPerPage, numPerPage , true);
		if(1 == page){
			total = walletService.countWalletErrorList(symbol, keyword);
		}

		List<Fvirtualcointype> coins = virtualCoinService.findAll();
		Map<String, Fvirtualcointype> coinMap = new HashMap<>(coins.size());
		for(Fvirtualcointype coin : coins){
			coinMap.put(coin.getFid() + "", coin);
		}
		map.put("coins", coinMap);
		map.put("list", list);
		map.put("keyword", keyword);
		map.put("page", page);
		map.put("total", total);
		map.put("numPerPage", numPerPage);
		return "ssadmin/walletErrorList";
	}

	@RequestMapping(value = "/ssadmin/exportWalletErrorList", method = RequestMethod.GET)
	@RequiresPermissions("ssadmin/exportWalletErrorList.html")
	public void exportWalletErrorList(HttpServletResponse response,
									  @RequestParam(required = false, defaultValue = "-1") Integer symbol,
									  @RequestParam(required = false) String keyword){

		if(symbol < 0){
			symbol = null;
		}
		List<Map<String, Object>> list = walletService.findWalletErrorList(symbol, keyword, null, null, 0, 0 , false);

		List<Fvirtualcointype> coins = virtualCoinService.findAll();
		Map<String, Fvirtualcointype> coinMap = new HashMap<>(coins.size());
		for(Fvirtualcointype coin : coins){
			coinMap.put(coin.getFid() + "", coin);
		}

		XlsExport xls = new XlsExport();

		// 标题
		xls.createRow(0);
		xls.setCell(0, "会员登录名");
		xls.setCell(1, "会员真实姓名");
		xls.setCell(2, "会员手机号");
		xls.setCell(3, "会员邮箱号");
		xls.setCell(4, "会员钱包类型");
		xls.setCell(5, "会员钱包余额");
		xls.setCell(6, "会员钱包冻结额");

		// 填入数据
		for(int i=1, len=list.size(); i<=len; i++){
			Map<String, Object> map = list.get(i-1);
			xls.createRow(i);
			xls.setCell(0, StringUtils.null2EmptyString(map.get("floginName")));
			xls.setCell(1, StringUtils.null2EmptyString(map.get("fRealName")));
			xls.setCell(2, StringUtils.null2EmptyString(map.get("fTelephone")));
			xls.setCell(3, StringUtils.null2EmptyString(map.get("fEmail")));
			xls.setCell(4, "0".equals(map.get("fvid").toString()) ? "人民币" : coinMap.get(map.get("fvid").toString()).getFname());
			xls.setCell(5, map.get("fTotal").toString());
			xls.setCell(6, map.get("fFrozen").toString());
		}

		try {
			SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
			response.setContentType("application/vnd.ms-excel");
			response.setHeader("Content-Disposition", "attachment;filename=" + URLEncoder.encode("会员钱包警报表-", "utf-8") + format.format(new Date()) + ".xls");
			xls.exportXls(response);
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
	}
}
















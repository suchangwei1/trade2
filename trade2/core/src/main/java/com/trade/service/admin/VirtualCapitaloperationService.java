package com.trade.service.admin;

import java.sql.Timestamp;
import java.util.List;
import java.util.Map;

import com.trade.Enum.PushOpenTyepEnum;
import com.trade.Enum.PushTypeEnum;
import com.trade.Enum.VirtualCapitalOperationInStatusEnum;
import com.trade.Enum.VirtualCapitalOperationTypeEnum;
import com.trade.comm.ConstantMap;
import com.trade.model.*;
import com.trade.service.front.FrontVirtualCoinService;
import com.trade.util.MathUtils;
import com.trade.util.StringUtils;
import com.trade.dao.FvirtualcaptualoperationDAO;
import com.trade.dao.FvirtualwalletDAO;
import com.trade.util.BTCUtils;
import com.trade.util.Utils;
import org.apache.shiro.SecurityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import org.springframework.util.Assert;

@Service
public class VirtualCapitaloperationService {
	@Autowired
	private FvirtualcaptualoperationDAO virtualcaptualoperationDAO;
	@Autowired
	private FvirtualwalletDAO virtualwalletDAO;
	@Autowired
	private ConstantMap constantMap;
	@Autowired
	private FrontVirtualCoinService  frontVirtualCoinService;
	@Autowired
	private FvirtualwalletDAO fvirtualwalletDAO;

	private static Logger logger = LoggerFactory.getLogger(VirtualCapitaloperationService.class);

	public Fvirtualcaptualoperation findById(int id) {
		Fvirtualcaptualoperation info = this.virtualcaptualoperationDAO.findById(id);
		info.getFuser().getFnickName();
		info.getFvirtualcointype().getFname();
		return info;
	}

	public void saveObj(Fvirtualcaptualoperation obj) {
		this.virtualcaptualoperationDAO.save(obj);
	}

	public void deleteObj(int id) {
		Fvirtualcaptualoperation obj = this.virtualcaptualoperationDAO
				.findById(id);
		this.virtualcaptualoperationDAO.delete(obj);
	}

	public void updateObj(Fvirtualcaptualoperation obj) {
		this.virtualcaptualoperationDAO.attachDirty(obj);
	}

	public List<Fvirtualcaptualoperation> findByProperty(String name,
			Object value) {
		return this.virtualcaptualoperationDAO.findByProperty(name, value);
	}

	public List<Fvirtualcaptualoperation> findAll() {
		return this.virtualcaptualoperationDAO.findAll();
	}

	public List<Fvirtualcaptualoperation> list(int firstResult, int maxResults,
			String filter, boolean isFY) {
		List<Fvirtualcaptualoperation> all = this.virtualcaptualoperationDAO
				.list(firstResult, maxResults, filter, isFY);
		return all;
	}

	//
	public List<Map> sumUserCaptualoperation(int firstResult, int maxResults,
											   String filter, boolean isFY) {
		List<Map> all = this.virtualcaptualoperationDAO
				.sumUserCaptualoperation(firstResult, maxResults, filter, isFY);
		return all;
	}

	/**
	 * 查询出所有的记录数
	 * @param s
	 * @return
	 */
	public int findAllListCount(String s){
		return this.virtualcaptualoperationDAO.findAllListCount(s);
	}
	public void updateOperateAndWallet(Fvirtualcaptualoperation virtualcaptualoperation, Fvirtualwallet virtualwallet){
		try {
			this.virtualcaptualoperationDAO.attachDirty(virtualcaptualoperation);
			this.virtualwalletDAO.attachDirty(virtualwallet);
		}catch (Exception e){
			e.printStackTrace();
			throw new RuntimeException();
		}
	}

	public void updateCapital(Fvirtualcaptualoperation virtualcaptualoperation, BTCUtils btcUtils) {
		Assert.isTrue(this.virtualwalletDAO.updateDeduct(virtualcaptualoperation.getFuser().getFid(), virtualcaptualoperation.getFvirtualcointype().getFid(), virtualcaptualoperation.getFamount(), Utils.getTimestamp()) > 0, "账户冻结资金异常");
		if (virtualcaptualoperation.getFfees() > 0 && virtualcaptualoperation.getFeeCoinType() > 0 && !virtualcaptualoperation.getFvirtualcointype().getFid().equals(virtualcaptualoperation.getFeeCoinType())) {
			// 手续费另收
			Assert.isTrue(this.virtualwalletDAO.updateDeduct(virtualcaptualoperation.getFuser().getFid(), virtualcaptualoperation.getFeeCoinType(), virtualcaptualoperation.getFfees(), Utils.getTimestamp()) > 0, "手续费账户冻结资金异常");
		}
		updateCapitalRe(virtualcaptualoperation, btcUtils);
	}

	public void updateCapitalRe(Fvirtualcaptualoperation virtualcaptualoperation, BTCUtils btcUtils) {
		String address = virtualcaptualoperation.getWithdraw_virtual_address();
		double amount = virtualcaptualoperation.getFamount();
		if (virtualcaptualoperation.getFvirtualcointype().getFid().equals(virtualcaptualoperation.getFeeCoinType())) {
			// 扣除部分手续费
			amount = MathUtils.subtract(amount, virtualcaptualoperation.getFfees());
		}
		if(address.indexOf(constantMap.getString("edtMainAddress")) != -1){
			//如果是EDT内部转账
			//增加充值记录
			try{
				net.sf.json.JSONObject json = btcUtils.walletpassphrase(30);
				if ("500".equals(json.getString("error"))) {
					//System.out.println("error pass=" + PASSWORD + "!");
					throw new Exception("钱包解锁失败");
				}
			}catch (Exception e){
				throw new RuntimeException("钱包解锁失败");
			}
			Fvirtualcaptualoperation fvirtualcaptualoperation = new Fvirtualcaptualoperation();
			List<Fvirtualaddress> fvirtualaddresses = this.frontVirtualCoinService.findFvirtualaddress(virtualcaptualoperation.getFvirtualcointype(), address) ;
			if(fvirtualaddresses.size()==0){
				throw new RuntimeException();
			}else if(fvirtualaddresses.size()>1){
				throw new RuntimeException();
			}else{
				Fvirtualaddress fvirtualaddress = fvirtualaddresses.get(0) ;
				Fuser fuser = fvirtualaddress.getFuser() ;
				fvirtualcaptualoperation.setFuser(fuser) ;
			}
			fvirtualcaptualoperation.setFhasOwner(true) ;
			fvirtualcaptualoperation.setFamount(amount) ;
			fvirtualcaptualoperation.setFcreateTime(Utils.getTimestamp()) ;
			fvirtualcaptualoperation.setFfees(0F) ;
			fvirtualcaptualoperation.setFlastUpdateTime(Utils.getTimestamp()) ;
			fvirtualcaptualoperation.setFconfirmations(virtualcaptualoperation.getFvirtualcointype().getConfirmTimes()) ;
			fvirtualcaptualoperation.setFstatus(VirtualCapitalOperationInStatusEnum.SUCCESS) ;
			fvirtualcaptualoperation.setFtradeUniqueNumber("inner transaction for id " + virtualcaptualoperation.getFid()) ;
			fvirtualcaptualoperation.setRecharge_virtual_address(address) ;
			fvirtualcaptualoperation.setFtype(VirtualCapitalOperationTypeEnum.COIN_IN);
			fvirtualcaptualoperation.setFvirtualcointype(virtualcaptualoperation.getFvirtualcointype());
			this.frontVirtualCoinService.addFvirtualcaptualoperation(fvirtualcaptualoperation) ;
			//更新钱包
			Fvirtualwallet fvirtualwallet = this.fvirtualwalletDAO.findVirtualWallet(fvirtualcaptualoperation.getFuser().getFid(), virtualcaptualoperation.getFvirtualcointype().getFid()) ;
			double totalCoin = Utils.getDouble((constantMap.getDouble("rechargeCOIN") + 1) * amount, 4);
			fvirtualwallet.setFtotal(fvirtualwallet.getFtotal()+ totalCoin) ;
			fvirtualwallet.setFlastUpdateTime(Utils.getTimestamp()) ;
			this.fvirtualwalletDAO.attachDirty(fvirtualwallet) ;
		}else{
			try {
				// 优先更新数据库，再转账，保证最终一致性问题
				String flag = btcUtils.sendtoaddressValue(address,amount,virtualcaptualoperation.getFid().toString());
				if(StringUtils.hasText(flag)){
					try{
						virtualcaptualoperation = this.virtualcaptualoperationDAO.findById(virtualcaptualoperation.getFid());
						virtualcaptualoperation.setFtradeUniqueNumber(flag);
						this.virtualcaptualoperationDAO.attachDirty(virtualcaptualoperation);
					}catch (Exception e){
						e.printStackTrace();
						if(logger.isErrorEnabled()){
							logger.error(virtualcaptualoperation.getFvirtualcointype().getFname() + "提现订单" + virtualcaptualoperation.getFid() + "更新交易号" + flag + "失败", e);
						}
					}

				}else{
					throw new RuntimeException("提现失败");
				}
			} catch (Exception e) {
				e.printStackTrace();
				String msg = e.getMessage();
				if (StringUtils.isEmpty(msg)) {
					msg = "系统繁忙";
				}
				throw new RuntimeException(msg);
			}
		}
	}
	
	public List<Map> getTotalAmount(int type,String fstatus,boolean isToday) {
		return this.virtualcaptualoperationDAO.getTotalQty(type, fstatus,isToday);
	}
	
	public List getTotalGroup(String filter) {
		return this.virtualcaptualoperationDAO.getTotalGroup(filter);
	}

}
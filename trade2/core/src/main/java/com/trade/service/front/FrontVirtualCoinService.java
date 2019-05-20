package com.trade.service.front;

import com.trade.Enum.VirtualCapitalOperationInStatusEnum;
import com.trade.Enum.VirtualCapitalOperationOutStatusEnum;
import com.trade.Enum.VirtualCapitalOperationTypeEnum;
import com.trade.Enum.VirtualCoinTypeStatusEnum;
import com.trade.dao.*;
import com.trade.model.*;
import com.trade.util.StringUtils;
import com.trade.util.Utils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class FrontVirtualCoinService {
	@Autowired
	private FvirtualcointypeDAO fvirtualcointypeDAO ;
	@Autowired
	private FfeesDAO ffeesDAO ;
	@Autowired
	private FvirtualaddressDAO fvirtualaddressDAO ;
	@Autowired
	private FvirtualaddressWithdrawDAO fvirtualaddressWithdrawDAO ;
	@Autowired
	private FvirtualcaptualoperationDAO fvirtualcaptualoperationDAO ;
	@Autowired
	private FvirtualwalletDAO fvirtualwalletDAO ;
	@Autowired
	private FsystemargsDAO systemargsDAO;

	public List<Fvirtualcointype> findFvirtualCoinType(int status){
		List<Fvirtualcointype> list = this.fvirtualcointypeDAO.findByParam(0, 0, " where fstatus="+status+" order by fid asc ", false, "Fvirtualcointype") ;
		return list ;
	}

	public List<Fvirtualcointype> findOtcCoin(){
		List<Fvirtualcointype> list = this.fvirtualcointypeDAO.findByParam(0, 0, " where isOtcActive = 1 order by fid asc ", false, "Fvirtualcointype") ;
		for (Fvirtualcointype coin: list){
			coin.setFfees(null);
			coin.setCcLogs(null);
			coin.setFentrusts(null);
			coin.setFvirtualcaptualoperations(null);
			coin.setFvirtualwallets(null);
		}
		return list ;
	}

	public List<Fvirtualcointype> list(){
		return this.fvirtualcointypeDAO.list(0, 0, " where 1=1", false);
	}
	
	public Fvirtualcointype findFvirtualCoinById(int id){
		Fvirtualcointype fvirtualcointype = this.fvirtualcointypeDAO.findById(id) ;
		return fvirtualcointype ;
	}
	
	public Fvirtualaddress findFvirtualaddress(Fuser fuser, Fvirtualcointype fvirtualcointype){
		return this.fvirtualaddressDAO.findFvirtualaddress(fuser, fvirtualcointype) ;
	}
	
	public List<Fvirtualaddress> findFvirtualaddress(Fvirtualcointype fvirtualcointype,String address){
		return this.fvirtualaddressDAO.findFvirtualaddress(fvirtualcointype, address) ;
	}

	
	public List<Fvirtualcaptualoperation> findFvirtualcaptualoperation(
			Fuser fuser,int type[],int status[],Fvirtualcointype[] fvirtualcointypes,String order,
			int firstResult,int maxResult){
		List<Fvirtualcaptualoperation> list = this.fvirtualcaptualoperationDAO.findFvirtualcaptualoperation(fuser, type, status, fvirtualcointypes, order, firstResult, maxResult) ;
		for (Fvirtualcaptualoperation fvirtualcaptualoperation : list) {
			fvirtualcaptualoperation.getFvirtualcointype().getFname() ;
		}
		return list ;
	}

	public List<Object> findFvirtualaddressInitWithdraws(Fuser fuser,Integer id){
		return this.fvirtualaddressWithdrawDAO.findFvirtualaddressInitWithdraws(fuser, id) ;
	}

	public FvirtualaddressWithdraw findWithdrawAddressById(int id){
		return this.fvirtualaddressWithdrawDAO.findWithdrawAddressById(id) ;
	}

	public int countFvirtualcaptualoperation(Fuser fuser,Integer type[],Integer status[],Fvirtualcointype[] fvirtualcointypes){
		return this.fvirtualcaptualoperationDAO.countFvirtualcaptualoperation(fuser, type, status, fvirtualcointypes) ;
	}

	
	public void updateFvirtualaddressWithdraw(FvirtualaddressWithdraw fvirtualaddressWithdraw){
		this.fvirtualaddressWithdrawDAO.attachDirty(fvirtualaddressWithdraw) ;
	}

	public FvirtualaddressWithdraw getFvirtualaddressWithdraw(Integer id){
		return this.fvirtualaddressWithdrawDAO.getFvirtualaddressWithdraw(id) ;
	}

	public void deleteFvirtualaddressWithdraw(FvirtualaddressWithdraw f){
		this.fvirtualaddressWithdrawDAO.deleteFvirtualaddressWithdraw(f) ;
	}
	
	public Ffees findFfees(int virtualCoinTypeId){
		return this.ffeesDAO.findFfee(virtualCoinTypeId) ;
	}

	public int updateWithdrawBtc(String address, Fvirtualcointype fvirtualcointype, double withdrawAmount, Fuser fuser){
		Ffees ffees = this.ffeesDAO.findFfee(fvirtualcointype.getFid());
		double feeAmount = ffees.getWithdrawFee(withdrawAmount);

		// 手续费收取方式 同种币种在提取总金额中扣除，否则在手续费币种扣除
		Assert.isTrue(this.fvirtualwalletDAO.updateRmb(fuser.getFid(), fvirtualcointype.getFid(), withdrawAmount, Utils.getTimestamp()) > 0, "账户资金不足");
		if (feeAmount > 0 && ffees.getWithdrawFeeType() > 0 && !fvirtualcointype.getFid().equals(ffees.getWithdrawFeeType())) {
			// 手续费
			Assert.isTrue(this.fvirtualwalletDAO.updateRmb(fuser.getFid(), ffees.getWithdrawFeeType(), feeAmount, Utils.getTimestamp()) > 0, "手续费账户资金不足");
		}

		Fvirtualcaptualoperation fvirtualcaptualoperation = new Fvirtualcaptualoperation();
		fvirtualcaptualoperation.setFcreateTime(Utils.getTimestamp());
		fvirtualcaptualoperation.setFamount(withdrawAmount);
		fvirtualcaptualoperation.setFfees(feeAmount);
		fvirtualcaptualoperation.setFeeCoinType(ffees.getWithdrawFeeType());
		fvirtualcaptualoperation.setFlastUpdateTime(Utils.getTimestamp()) ;
		fvirtualcaptualoperation.setFstatus(VirtualCapitalOperationOutStatusEnum.WaitForOperation) ;
		fvirtualcaptualoperation.setFtype(VirtualCapitalOperationTypeEnum.COIN_OUT) ;
		fvirtualcaptualoperation.setFuser(fuser) ;
		fvirtualcaptualoperation.setFvirtualcointype(fvirtualcointype) ;
		fvirtualcaptualoperation.setWithdraw_virtual_address(address) ;
		this.fvirtualcaptualoperationDAO.save(fvirtualcaptualoperation) ;
		return fvirtualcaptualoperation.getFid();
	}


	public void addFvirtualcaptualoperation(Fvirtualcaptualoperation fvirtualcaptualoperation){
		this.fvirtualcaptualoperationDAO.save(fvirtualcaptualoperation) ;
	}

	public List<Fvirtualcaptualoperation> findFvirtualcaptualoperationByProperties(String[] keys,Object[] values){
		return this.fvirtualcaptualoperationDAO.findByProperties(keys, values) ;
	}

	public Fvirtualcaptualoperation findFvirtualcaptualoperationById(int id){
		return this.fvirtualcaptualoperationDAO.findById(id) ;
	}
	
	//比特币自动充值并加币
	public void updateFvirtualcaptualoperationCoinIn(Fvirtualcaptualoperation fvirtualcaptualoperation) throws Exception{
		try {
			Fvirtualcaptualoperation real = this.fvirtualcaptualoperationDAO.findById(fvirtualcaptualoperation.getFid()) ;
			if(real!=null && real.getFstatus()!= VirtualCapitalOperationInStatusEnum.SUCCESS){
				real.setFstatus(fvirtualcaptualoperation.getFstatus()) ;
				real.setFconfirmations(fvirtualcaptualoperation.getFconfirmations()) ;
				real.setFlastUpdateTime(Utils.getTimestamp()) ;
				this.fvirtualcaptualoperationDAO.attachDirty(real) ;
				
				if(real.getFstatus()==VirtualCapitalOperationInStatusEnum.SUCCESS && real.isFhasOwner()){
					Fvirtualcointype fvirtualcointype = this.fvirtualcointypeDAO.findById(real.getFvirtualcointype().getFid()) ;
					Fvirtualwallet fvirtualwallet = this.fvirtualwalletDAO.findVirtualWallet(real.getFuser().getFid(), fvirtualcointype.getFid()) ;
					List<Fsystemargs> list = this.systemargsDAO.findByFkey("rechargeCOIN");
					double totalCoin = Utils.getDouble(Double.valueOf(list.get(0).getFvalue())*real.getFamount()+real.getFamount(), 4);
					fvirtualwallet.setFtotal(fvirtualwallet.getFtotal()+ totalCoin) ;
					fvirtualwallet.setFlastUpdateTime(Utils.getTimestamp()) ;
					this.fvirtualwalletDAO.attachDirty(fvirtualwallet) ;
				}
			}
		} catch (Exception e) {
			throw e;
		}
	}

	public Fvirtualaddress updateAssignWalletAddress(Fuser fuser, Fvirtualcointype fvirtualcointype) {
		String address = fvirtualaddressDAO.getAssignAddress(fvirtualcointype.getFid());
		if(StringUtils.isEmpty(address)){
			return null;
		}

		Fvirtualaddress fvirtualaddress = new Fvirtualaddress() ;
		fvirtualaddress.setFadderess(address) ;
		fvirtualaddress.setFcreateTime(Utils.getTimestamp()) ;
		fvirtualaddress.setFuser(fuser) ;
		fvirtualaddress.setFvirtualcointype(fvirtualcointype) ;
		fvirtualaddressDAO.save(fvirtualaddress);

		return fvirtualaddress;
	}

	public Map<Long, Fvirtualcointype> findOnSellMap() {
		List<Fvirtualcointype> list = this.findFvirtualCoinType(VirtualCoinTypeStatusEnum.Normal);
		Map map = new HashMap();
		for (Fvirtualcointype fvirtualcointype : list) {
			map.put(Long.valueOf(fvirtualcointype.getFid()), fvirtualcointype);
		}
		return map;
	}



	public void saveAddress(Fvirtualaddress fvirtualaddress){
	    this.fvirtualaddressDAO.save(fvirtualaddress);
    }

	public Map<Long, Fvirtualcointype> listMap() {
		List<Fvirtualcointype> list = this.fvirtualcointypeDAO.findAll();
		Map<Long, Fvirtualcointype> map = Utils.newHashMap(list.size());
		list.forEach(e -> map.put(Long.valueOf(e.getFid()), e));
		return map;
	}

	public Map<Long, Fvirtualcointype> listOnSellMap() {
		List<Fvirtualcointype> list = this.fvirtualcointypeDAO.findByProperty("fstatus", VirtualCoinTypeStatusEnum.Normal);
		Map<Long, Fvirtualcointype> map = Utils.newHashMap(list.size());
		list.forEach(e -> map.put(Long.valueOf(e.getFid()), e));
		return map;
	}
}

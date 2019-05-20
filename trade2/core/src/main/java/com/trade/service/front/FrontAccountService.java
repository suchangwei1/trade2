package com.trade.service.front;

import com.trade.Enum.VirtualCapitalOperationOutStatusEnum;
import com.trade.dao.FvirtualcaptualoperationDAO;
import com.trade.dao.FvirtualwalletDAO;
import com.trade.model.Fuser;
import com.trade.model.Fvirtualcaptualoperation;
import com.trade.model.Fvirtualcointype;
import com.trade.util.Utils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

@Service
public class FrontAccountService {
	private static final Logger log = LoggerFactory.getLogger(FrontAccountService.class);
	@Autowired
	private FvirtualwalletDAO fvirtualwalletDAO ;
	@Autowired
	private FvirtualcaptualoperationDAO fvirtualcaptualoperationDAO ;
	

	public void updateCancelWithdrawBtc(Fvirtualcaptualoperation fvirtualcaptualoperation){
		fvirtualcaptualoperation.setFstatus(VirtualCapitalOperationOutStatusEnum.Cancel) ;
		fvirtualcaptualoperation.setFlastUpdateTime(Utils.getTimestamp());

		Fvirtualcointype fvirtualcointype = fvirtualcaptualoperation.getFvirtualcointype();
		Assert.isTrue(fvirtualwalletDAO.updateRefund(fvirtualcaptualoperation.getFuser().getFid(), fvirtualcointype.getFid(), fvirtualcaptualoperation.getFamount(), Utils.getTimestamp()) > 0, "账户冻结资金异常");
		if (fvirtualcaptualoperation.getFfees() > 0 && fvirtualcaptualoperation.getFeeCoinType() > 0 && !fvirtualcointype.getFid().equals(fvirtualcaptualoperation.getFeeCoinType())) {
			Assert.isTrue(fvirtualwalletDAO.updateRefund(fvirtualcaptualoperation.getFuser().getFid(), fvirtualcaptualoperation.getFeeCoinType(), fvirtualcaptualoperation.getFfees(), Utils.getTimestamp()) > 0, "手续费冻结资金异常");
		}

		this.fvirtualcaptualoperationDAO.attachDirty(fvirtualcaptualoperation) ;
	}

	public int getTodayVirtualCoinWithdrawTimes(Fuser fuser){
		return this.fvirtualcaptualoperationDAO.getTodayVirtualCoinWithdrawTimes(fuser) ;
	}
		
}

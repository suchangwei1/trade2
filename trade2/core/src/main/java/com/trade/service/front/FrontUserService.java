package com.trade.service.front;

import com.trade.Enum.UserStatusEnum;
import com.trade.Enum.VirtualCoinTypeStatusEnum;
import com.trade.dao.FuserDAO;
import com.trade.dao.FvirtualcointypeDAO;
import com.trade.dao.FvirtualwalletDAO;
import com.trade.model.Fuser;
import com.trade.model.Fvirtualwallet;
import com.trade.service.BaseService;
import com.trade.util.CollectionUtils;
import com.trade.util.Constants;
import com.trade.util.Utils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import javax.servlet.http.HttpSession;
import java.util.*;

@Service
public class FrontUserService extends BaseService {
	private static final Logger log = LoggerFactory.getLogger(FrontUserService.class);
	@Autowired
	private FuserDAO fuserDAO ;
	@Autowired
	private FvirtualcointypeDAO fvirtualcointypeDAO ;
	@Autowired
	private FvirtualwalletDAO fvirtualwalletDAO ;

	public boolean saveRegister(Fuser fuser) throws Exception{
		fuser.setFregisterTime(Utils.getTimestamp());
		fuser.setFstatus(UserStatusEnum.NORMAL_VALUE);
		fuser.setFlastLoginTime(Utils.getTimestamp());
		fuser.setFlastUpdateTime(Utils.getTimestamp());
		fuser.setFneedFee(true);
		this.fuserDAO.save(fuser) ;
		updateInitAccount(fuser);
		return true ;
	}

	public void updateInitAccount(Fuser fuser){
		//虚拟钱包
		HashMap walletMap = null;
		List<Integer> coins = fvirtualcointypeDAO.findIds(VirtualCoinTypeStatusEnum.Normal);
		List<Map> walletList = new ArrayList<>(coins.size());
		for (Integer coinId : coins) {

			if (Objects.isNull(walletMap)) {
				walletMap = new HashMap();
			} else if (!CollectionUtils.isEmpty(walletMap)) {
				walletMap = (HashMap) walletMap.clone();
			}
			walletMap.put("fVi_fId", coinId);
			walletMap.put("fTotal", 0);
			walletMap.put("fFrozen", 0);
			walletMap.put("fLastUpdateTime", Utils.getTimestamp());
			walletMap.put("fuid", fuser.getFid());
			walletMap.put("version", 0);
			walletList.add(walletMap);
		}
		if (!CollectionUtils.isEmpty(walletList)) {
			fvirtualwalletDAO.insertAll(walletList);
		}
		this.fuserDAO.attachDirty(fuser);
	}

	public Fuser findForLogin(String loginName, String password) {
		Map<String, Object> map = new HashMap();
		map.put("floginName", loginName);
		map.put("floginPassword", Utils.MD5(password));
		List<Fuser> list = this.fuserDAO.findByMap(map);
		return CollectionUtils.isEmpty(list) ? null : list.get(0);
	}
	
	public Map<Integer, Fvirtualwallet> findVirtualWallet(int fuid){
		TreeMap<Integer, Fvirtualwallet> treeMap = new TreeMap<Integer, Fvirtualwallet>(new Comparator<Integer>() {

			public int compare(Integer o1, Integer o2) {
				return o1.compareTo(o2) ;
			}

		}) ;
		List<Fvirtualwallet> fvirtualwallets = this.fvirtualwalletDAO.find(fuid, VirtualCoinTypeStatusEnum.Normal) ;
		for (Fvirtualwallet fvirtualwallet : fvirtualwallets) {
			treeMap.put(fvirtualwallet.getFvirtualcointype().getFid(), fvirtualwallet) ;
		}
		return treeMap ;
	}

	public Fvirtualwallet findVirtualWalletNative(int fuid,int fcoinId) {
		return fvirtualwalletDAO.findVirtualWalletNative(fuid, fcoinId);
	}

	public Fvirtualwallet findVirtualWalletByUser(int fuid,int virtualCoinId){
		Fvirtualwallet fvirtualwallet = this.fvirtualwalletDAO.findVirtualWallet(fuid, virtualCoinId) ;
		return fvirtualwallet ;
	}
	
	public Fuser findById(int id){
		Fuser fuser = this.fuserDAO.findById(id) ;
		return fuser ;
	}

	public boolean isEmailExist(String userName){
		return !CollectionUtils.isEmpty(this.fuserDAO.findByProperty("femail", userName));
	}
	public boolean isMobileExists(String userName){
		return !CollectionUtils.isEmpty(this.fuserDAO.findByProperty("ftelephone", userName));
	}

	public void updateFUser(Fuser fuser,HttpSession session){
		this.fuserDAO.attachDirty(fuser) ;
		if(session!=null && session.getAttribute(Constants.USER_LOGIN_SESSION)!=null){
			session.setAttribute(Constants.USER_LOGIN_SESSION, fuser) ;
		}
	}
	
	public void updateFuser(Fuser fuser){
		this.fuserDAO.attachDirty(fuser) ;
	}

	public List<Fuser> findUserByProperty(String key,Object value){
		return this.fuserDAO.findByProperty(key, value) ;
	}

	public Fuser findByLoginName(String name){
		Assert.hasText(name);
		List<Fuser> users = fuserDAO.findByFloginName(name);
		if(CollectionUtils.isEmpty(users)){
			return null;
		}
		return users.get(0);
	}

	public boolean isNormal(int userId) {
		return this.fuserDAO.isNormal(userId);
	}
}

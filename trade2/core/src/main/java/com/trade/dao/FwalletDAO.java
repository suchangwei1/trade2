package com.trade.dao;

import com.trade.dao.comm.HibernateDaoSupport;
import com.trade.util.StringUtils;
import org.hibernate.SQLQuery;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * A data access object (DAO) providing persistence and search support for
 * Fwallet entities. Transaction control of the save(), update() and delete()
 * operations can directly support Spring container-managed transactions or they
 * can be augmented to handle user-managed Spring transactions. Each of these
 * methods provides additional information for how to configure it for the
 * desired type of transaction control.
 * 
 * @author MyEclipse Persistence Tools
 */
@Repository
public class FwalletDAO extends HibernateDaoSupport {
	private static final Logger log = LoggerFactory.getLogger(FwalletDAO.class);

	/**
	 *	获取有冻结资金问题的记录
	 * @param symbol 0：rmb
	 * @param keyword
	 * @param orderField
	 * @param orderDirection
	 * @param offset
     * @param length
     * @return
     */
	public List<Map<String, Object>> findWalletErrorList(Integer symbol, String keyword, String orderField, String orderDirection, int offset, int length, boolean isPage){
		StringBuilder sqlBuf = new StringBuilder();
		if(null == symbol || symbol > 0){
			sqlBuf.append("select w.fVi_fId fvid,u.floginName,u.fRealName,u.fTelephone,u.fEmail,w.fTotal,w.fFrozen from fvirtualwallet w");
			sqlBuf.append(" left join fuser u on u.fid = w.fuid where w.fFrozen < 0");
			if(StringUtils.hasText(keyword)){
				sqlBuf.append(" and (u.floginName like :keyword or u.fTelephone like :keyword or u.fEmail like :keyword)");
			}
			if(null != symbol && symbol > 0){
				sqlBuf.append(" and w.fVi_fId =:symbol");
			}
		}
		if(StringUtils.hasText(orderDirection) && StringUtils.hasText(orderDirection)){
			sqlBuf.append(" order by ").append(orderField).append(" ").append(orderDirection);
		}else{
			sqlBuf.append(" order by fFrozen");
		}

		SQLQuery query = getSession().createSQLQuery(sqlBuf.toString());
		if(null != symbol && symbol > 0){
			query.setParameter("symbol", symbol);
		}
		if(StringUtils.hasText(keyword)){
			query.setParameter("keyword", "%" + keyword + "%");
		}
		if(isPage){
			query.setFirstResult(offset);
			query.setMaxResults(length);
		}

		List list = query.list();
		List<Map<String, Object>> retList = new ArrayList<>(list.size());
		HashMap<String, Object> objMap = new HashMap(10);
		for(Object obj : list){
			Object[] objArr = (Object[]) obj;
			objMap.put("fvid", objArr[0]);
			objMap.put("floginName", objArr[1]);
			objMap.put("fRealName", objArr[2]);
			objMap.put("fTelephone", objArr[3]);
			objMap.put("fEmail", objArr[4]);
			objMap.put("fTotal", objArr[5]);
			objMap.put("fFrozen", objArr[6]);
			retList.add((HashMap)objMap.clone());
		}

		return retList;
	}

	public int countRMBWalletrError(String keyword){
		StringBuilder sqlBuf = new StringBuilder();
		sqlBuf.append("select count(1) from fwallet w");
		sqlBuf.append(" left join fuser u on u.FWa_fId = w.fId where w.fFrozenRMB < 0");
		if(StringUtils.hasText(keyword)){
			sqlBuf.append(" and (u.floginName like :keyword or u.fTelephone like :keyword or u.fEmail like :keyword or u.fRealName like :keyword)");
		}

		SQLQuery query = getSession().createSQLQuery(sqlBuf.toString());
		if(StringUtils.hasText(keyword)){
			query.setParameter("keyword", "%" + keyword + "%");
		}
		return ((BigInteger)query.uniqueResult()).intValue();
	}

	public int countCoinWalletError(Integer symbol, String keyword){
		StringBuilder sqlBuf = new StringBuilder();
		sqlBuf.append("select count(1) from fvirtualwallet w");
		sqlBuf.append(" left join fuser u on u.fid = w.fuid where w.fTotal < 0");
		if(StringUtils.hasText(keyword)){
			sqlBuf.append(" and (u.floginName like :keyword or u.fTelephone like :keyword or u.fEmail like :keyword or u.fRealName like :keyword)");
		}
		if(null != symbol && symbol > 0){
			sqlBuf.append(" and w.fVi_fId =:symbol");
		}
		SQLQuery query = getSession().createSQLQuery(sqlBuf.toString());
		if(null != symbol && symbol > 0){
			query.setParameter("symbol", symbol);
		}
		if(StringUtils.hasText(keyword)){
			query.setParameter("keyword", "%" + keyword + "%");
		}
		return ((BigInteger)query.uniqueResult()).intValue();
	}

}












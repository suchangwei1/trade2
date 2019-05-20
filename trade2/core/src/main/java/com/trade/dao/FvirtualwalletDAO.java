package com.trade.dao;

import com.trade.dao.comm.HibernateDaoSupport;
import com.trade.model.Fvirtualwallet;
import com.trade.util.DateUtils;
import com.trade.util.StringUtils;
import com.trade.util.Utils;
import org.hibernate.HibernateException;
import org.hibernate.LockMode;
import org.hibernate.Query;
import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.orm.hibernate3.HibernateCallback;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import static org.hibernate.criterion.Example.create;

/**
 * A data access object (DAO) providing persistence and search support for
 * Fvirtualwallet entities. Transaction control of the save(), update() and
 * delete() operations can directly support Spring container-managed
 * transactions or they can be augmented to handle user-managed Spring
 * transactions. Each of these methods provides additional information for how
 * to configure it for the desired type of transaction control.
 * 
 * @see Fvirtualwallet
 * @author MyEclipse Persistence Tools
 */
@Repository
public class FvirtualwalletDAO extends HibernateDaoSupport {
	private static final Logger log = LoggerFactory
			.getLogger(FvirtualwalletDAO.class);
	// property constants
	public static final String FTOTAL = "ftotal";
	public static final String FFROZEN = "ffrozen";

	@Autowired
	private JdbcTemplate jdbcTemplate;

	public void save(Fvirtualwallet transientInstance) {
		log.debug("saving Fvirtualwallet instance");
		try {
			getSession().save(transientInstance);
			log.debug("save successful");
		} catch (RuntimeException re) {
			log.error("save failed", re);
			throw re;
		}
	}

	public void delete(Fvirtualwallet persistentInstance) {
		log.debug("deleting Fvirtualwallet instance");
		try {
			getSession().delete(persistentInstance);
			log.debug("delete successful");
		} catch (RuntimeException re) {
			log.error("delete failed", re);
			throw re;
		}
	}

	public Fvirtualwallet findById(java.lang.Integer id) {
		log.debug("getting Fvirtualwallet instance with id: " + id);
		try {
			Fvirtualwallet instance = (Fvirtualwallet) getSession().get(
					"com.trade.model.Fvirtualwallet", id);
			return instance;
		} catch (RuntimeException re) {
			log.error("get failed", re);
			throw re;
		}
	}

	public List<Fvirtualwallet> findByExample(Fvirtualwallet instance) {
		log.debug("finding Fvirtualwallet instance by example");
		try {
			List<Fvirtualwallet> results = (List<Fvirtualwallet>) getSession()
					.createCriteria("com.trade.model.Fvirtualwallet")
					.add(create(instance)).list();
			log.debug("find by example successful, result size: "
					+ results.size());
			return results;
		} catch (RuntimeException re) {
			log.error("find by example failed", re);
			throw re;
		}
	}

	public List findByProperty(String propertyName, Object value) {
		log.debug("finding Fvirtualwallet instance with property: "
				+ propertyName + ", value: " + value);
		try {
			String queryString = "from Fvirtualwallet as model where model."
					+ propertyName + "= ?";
			Query queryObject = getSession().createQuery(queryString);
			queryObject.setParameter(0, value);
			return queryObject.list();
		} catch (RuntimeException re) {
			log.error("find by property name failed", re);
			throw re;
		}
	}
	
	public List findByTwoProperty(String propertyName1, Object value1,String propertyName2, Object value2) {
		log.debug("finding Fvirtualwallet instance with property");
		try {
			String queryString = "from Fvirtualwallet as model where model."
					+ propertyName1 + "= ? and model."+propertyName2+"= ?";
			Query queryObject = getSession().createQuery(queryString);
			queryObject.setParameter(0, value1);
			queryObject.setParameter(1, value2);
			return queryObject.list();
		} catch (RuntimeException re) {
			log.error("find by property name failed", re);
			throw re;
		}
	}

	public List<Fvirtualwallet> findByFtotal(Object ftotal) {
		return findByProperty(FTOTAL, ftotal);
	}

	public List<Fvirtualwallet> findByFfrozen(Object ffrozen) {
		return findByProperty(FFROZEN, ffrozen);
	}

	public List findAll() {
		log.debug("finding all Fvirtualwallet instances");
		try {
			String queryString = "from Fvirtualwallet";
			Query queryObject = getSession().createQuery(queryString);
			return queryObject.list();
		} catch (RuntimeException re) {
			log.error("find all failed", re);
			throw re;
		}
	}

	public Fvirtualwallet merge(Fvirtualwallet detachedInstance) {
		log.debug("merging Fvirtualwallet instance");
		try {
			Fvirtualwallet result = (Fvirtualwallet) getSession().merge(
					detachedInstance);
			log.debug("merge successful");
			return result;
		} catch (RuntimeException re) {
			log.error("merge failed", re);
			throw re;
		}
	}

	public void attachDirty(Fvirtualwallet instance) {
		log.debug("attaching dirty Fvirtualwallet instance");
		try {
			getSession().saveOrUpdate(instance);
			log.debug("attach successful");
		} catch (RuntimeException re) {
			log.error("attach failed", re);
			throw re;
		}
	}

	public int updateRmb(final int fuid, final int fcoinId, final double ftotal, final Timestamp flastUpdateTime) {
		return getHibernateTemplate().execute(new HibernateCallback<Integer>() {
			@Override
			public Integer doInHibernate(Session session) throws HibernateException, SQLException {
				// set 用户金额 = 用户金额 - 交易金额，冻结金额 = 冻结金额 - 交易金额，where 用户金额 >= 交易金额
				Query query = session.createQuery("update Fvirtualwallet set ftotal = ftotal - ?, ffrozen = ffrozen + ?, version=version+1, flastUpdateTime = ? where fuser.fid = ? and fvirtualcointype.fid = ? and ftotal >= ?");
				query.setDouble(0, ftotal);
				query.setDouble(1, ftotal);
				query.setTimestamp(2, flastUpdateTime);
				query.setInteger(3, fuid);
				query.setInteger(4, fcoinId);
				query.setDouble(5, ftotal);
				int updateRow = query.executeUpdate();
				return updateRow;
			}
		});
	}

	public int updateTotal(final int fuid, final int fcoinId, final double ftotal, final Timestamp flastUpdateTime) {
		return getHibernateTemplate().execute(new HibernateCallback<Integer>() {
			@Override
			public Integer doInHibernate(Session session) throws HibernateException, SQLException {
				Query query = session.createQuery("update Fvirtualwallet set ftotal = ftotal + ?,  version=version+1, flastUpdateTime = ? where fuser.fid = ? and fvirtualcointype.fid = ? ");
				query.setDouble(0, ftotal);
				query.setTimestamp(1, flastUpdateTime);
				query.setInteger(2, fuid);
				query.setInteger(3, fcoinId);
				int updateRow = query.executeUpdate();
				return updateRow;
			}
		});
	}

	public int updateRefund(final int fuid, final int fcoinId, final double ftotal, final Timestamp flastUpdateTime) {
		return getHibernateTemplate().execute(new HibernateCallback<Integer>() {
			@Override
			public Integer doInHibernate(Session session) throws HibernateException, SQLException {
				// set 用户金额 = 用户金额 + 交易金额，冻结金额 = 冻结金额 - 交易金额，where 用户金额 >= 交易金额
				Query query = session.createQuery("update Fvirtualwallet set ftotal = ftotal + ?, ffrozen = ffrozen - ?, version=version+1, flastUpdateTime = ? where fuser.fid = ? and fvirtualcointype.fid = ? and ffrozen + 0.00000002 >= ?");
				query.setDouble(0, ftotal);
				query.setDouble(1, ftotal);
				query.setTimestamp(2, flastUpdateTime);
				query.setInteger(3, fuid);
				query.setInteger(4, fcoinId);
				query.setDouble(5, ftotal);
				int updateRow = query.executeUpdate();
				return updateRow;
			}
		});
	}

	public int updateDeduct(final int fuid, final int fcoinId, final double ftotal, final Timestamp flastUpdateTime) {
		return getHibernateTemplate().execute(new HibernateCallback<Integer>() {
			@Override
			public Integer doInHibernate(Session session) throws HibernateException, SQLException {
				// set 用户金额 = 用户金额 + 交易金额，冻结金额 = 冻结金额 - 交易金额，where 用户金额 >= 交易金额
				Query query = session.createQuery("update Fvirtualwallet set ffrozen = ffrozen - ?, version=version+1, flastUpdateTime = ? where fuser.fid = ? and fvirtualcointype.fid = ? and ffrozen + 0.00000002 >= ?");
				query.setDouble(0, ftotal);
				query.setTimestamp(1, flastUpdateTime);
				query.setInteger(2, fuid);
				query.setInteger(3, fcoinId);
				query.setDouble(4, ftotal);
				int updateRow = query.executeUpdate();
				return updateRow;
			}
		});
	}

	public int updateBuyAndSellVirtualWalletCount(final int uid, final int fviFid, final double fCount){
		return getHibernateTemplate().execute(new HibernateCallback<Integer>() {

			@Override
			public Integer doInHibernate(Session session) throws HibernateException, SQLException {
				String sql = "update Fvirtualwallet set fTotal = fTotal + ?, flastUpdateTime = ? where fuser.fid = ? and fvirtualcointype.fid = ?";
				Query query = session.createQuery(sql);
				query.setDouble(0, fCount);
				query.setTimestamp(1, Utils.getTimestamp());
				query.setInteger(2, uid);
				query.setInteger(3, fviFid);
				return query.executeUpdate();
			}
		});
	}

	public int updateBuyAndSellVirtualWalletFrozen(final int uid, final int fviFid, final double fCount){
		return getHibernateTemplate().execute(new HibernateCallback<Integer>() {
			@Override
			public Integer doInHibernate(Session session) throws HibernateException, SQLException {
				String sql = "update Fvirtualwallet set fFrozen = fFrozen - ?, flastUpdateTime = ? where fuser.fid = ? and fvirtualcointype.fid = ? and fFrozen + 0.2 >= ?";
				Query query = session.createQuery(sql);
				query.setDouble(0, fCount);
				query.setTimestamp(1, Utils.getTimestamp());
				query.setInteger(2, uid);
				query.setInteger(3, fviFid);
				query.setDouble(4, fCount);
				return query.executeUpdate();
			}
		});
	}
	
	public int updateVirtualWalletCount(final int fwalletid, final double fCount){
		return getHibernateTemplate().execute(new HibernateCallback<Integer>() {

			@Override
			public Integer doInHibernate(Session session) throws HibernateException, SQLException {
				String sql = "update Fvirtualwallet set fTotal = fTotal + ?, flastUpdateTime = ? where fId = ?";
				Query query = session.createQuery(sql);
				query.setDouble(0, fCount);
				query.setTimestamp(1, Utils.getTimestamp());
				query.setInteger(2, fwalletid);
				return query.executeUpdate();
			}
		});
	}
	//卖单完成时，应该解除冻结的虚拟币
	public int updateVirtualWalletFrozen(final int fwalletid, final double fFrozenCount){
		return getHibernateTemplate().execute(new HibernateCallback<Integer>() {

			@Override
			public Integer doInHibernate(Session session) throws HibernateException, SQLException {
				String sql = "update fvirtualwallet set fFrozen = fFrozen - ?, flastUpdateTime = ? where fId = ? and fFrozen + 0.0002 >= ?";
				Query query = session.createSQLQuery(sql);
				query.setDouble(0, fFrozenCount);
				query.setTimestamp(1, Utils.getTimestamp());
				query.setInteger(2, fwalletid);
				query.setDouble(3, fFrozenCount);
				return query.executeUpdate();
			}
		});
	}

	public void attachClean(Fvirtualwallet instance) {
		log.debug("attaching clean Fvirtualwallet instance");
		try {
			getSession().lock(instance, LockMode.NONE);
			log.debug("attach successful");
		} catch (RuntimeException re) {
			log.error("attach failed", re);
			throw re;
		}
	}
	
	public List<Fvirtualwallet> find(int fuid,int status){
		log.debug("finding all Fvirtualwallet instances");
		try {
			String queryString = "select  w.* from fvirtualwallet w LEFT JOIN  fvirtualcointype c on w.fVi_fId =c.fId where w.fuid=? and c.fstatus=?  order by (fTotal+fFrozen) desc ";
//			String queryString = " from Fvirtualwallet where fuser.fid=? and fvirtualcointype.fstatus=?  ";
//			Query queryObject = getSession().createQuery(queryString);
			SQLQuery queryObject = getSession().createSQLQuery(queryString);
			queryObject.setParameter(0, fuid) ;
			queryObject.setParameter(1, status) ;
			queryObject.addEntity(Fvirtualwallet.class);
			return queryObject.list();
		} catch (RuntimeException re) {
			log.error("find all failed", re);
			throw re;
		}
	}
//
	public Fvirtualwallet findVirtualWalletNative(int fuid,int fcoinId) {
		Fvirtualwallet wallet = null;
		try {
			wallet = jdbcTemplate.queryForObject("select fid, fTotal, fFrozen FROM fvirtualwallet WHERE fuid = ? AND fVi_fId = ?", new RowMapper<Fvirtualwallet>() {
				@Override
				public Fvirtualwallet mapRow(ResultSet rs, int rowNum) throws SQLException {
					Fvirtualwallet wallet = new Fvirtualwallet();
					wallet.setFid(rs.getInt("fid"));
					wallet.setFtotal(rs.getDouble("fTotal"));
					wallet.setFfrozen(rs.getDouble("fFrozen"));
					return wallet;
				}
			}, fuid, fcoinId);
		} catch (Exception e) {
		}
		if (wallet == null) {
			wallet = new Fvirtualwallet();
		}
		return wallet;
	}
	
	public Fvirtualwallet findVirtualWallet(int fuid,int fcoinId){
		log.debug("finding all Fvirtualwallet instances");
		try {
			String queryString = "from Fvirtualwallet where fuser.fid=? and fvirtualcointype.fid=?";
			Query queryObject = getSession().createQuery(queryString);
			queryObject.setParameter(0, fuid);
			queryObject.setParameter(1, fcoinId);
			List<Fvirtualwallet> list = queryObject.list();
			if(list.size()==1){
				return list.get(0) ;
			}else{
				log.error("Fvirtualwallet count:"+list.size()) ;
				return null ;
			}
		} catch (RuntimeException re) {
			log.error("find all failed", re);
			throw re;
		}
	}
	
	public List<Fvirtualwallet> list(int firstResult, int maxResults, String filter,boolean isFY) {
		List<Fvirtualwallet> list = null;
		log.debug("finding Fvirtualwallet instance with filter");
		try {
			String queryString = "from Fvirtualwallet "+filter;
			Query queryObject = getSession().createQuery(queryString);
			if(isFY){
				queryObject.setFirstResult(firstResult);
				queryObject.setMaxResults(maxResults);
			}
			list = queryObject.list();
		} catch (RuntimeException re) {
			log.error("find by filter name failed", re);
			throw re;
		}
		return list;
	}
	
	public List<Map> getTotalQty() {
		List<Map> all = new ArrayList();
		StringBuffer sql = new StringBuffer();
		sql.append("SELECT sum(ifnull(a.ftotal,0)+ifnull(a.fFrozen,0)) totalQty, \n");
		sql.append("sum(ifnull(a.fFrozen,0)) frozenQty,b.fName,b.fid \n");
		sql.append("FROM fvirtualwallet a left outer join fvirtualcointype b \n");
		sql.append("on a.fVi_fId = b.fId group by b.fName \n");
		SQLQuery queryObject = getSession().createSQLQuery(sql.toString());
		List allList = queryObject.list();
		Iterator it = allList.iterator();
		while(it.hasNext()) {
			Map map = new HashMap();
			Object[] o = (Object[]) it.next();
			map.put("totalQty", o[0]);
			map.put("frozenQty", o[1]);
			map.put("fName", o[2]);
			map.put("fVid", o[3]);
			all.add(map);
		}
		return all;
	}

	public BigDecimal getTotalQty(int vid) {
		List<Map> all = new ArrayList();
		StringBuffer sql = new StringBuffer();
		sql.append("SELECT sum(ifnull(ftotal,0)+ifnull(fFrozen,0)) totalQty \n");
		sql.append("FROM fvirtualwallet \n");
		sql.append("where fVi_fId = "+vid+" \n");
		SQLQuery queryObject = getSession().createSQLQuery(sql.toString());
		List allList = queryObject.list();
		Iterator it = allList.iterator();
		return new BigDecimal(it.next().toString());
	}

	/**
	 * 获取总额大于0.01的
	 *
	 * @param firstResult
	 * @param maxResult
	 * @param toPage
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	public List<Map<String, Object>> findForExport(String keyword, Integer coinId, int firstResult, int maxResult, boolean toPage){
		StringBuilder sqlBuf = new StringBuilder();
		sqlBuf.append("select u.floginName, u.fNickName, u.fRealName, u.fEmail, u.fTelephone, v.fTotal, v.fFrozen, (v.fTotal + v.fFrozen) total, v.fVi_fId from fvirtualwallet v left join fuser u on v.fuid=u.fId where 1=1");

		if(StringUtils.hasText(keyword)){
			sqlBuf.append(" and (u.floginName like :keyword or u.fRealName like :keyword or u.fTelephone like :keyword or u.fEmail like :keyword)");
		}
		if(null != coinId && coinId != 0){
			sqlBuf.append(" and v.fVi_fId=").append(coinId);
		}
		sqlBuf.append(" and v.fTotal+v.fFrozen >= 0 order by total desc");
		Query query = getSession().createSQLQuery(sqlBuf.toString());
		if(StringUtils.hasText(keyword)){
			query.setParameter("keyword", "%" + keyword.trim() + "%");
		}
		if(toPage){
			query.setFirstResult(firstResult);
			query.setMaxResults(maxResult);
		}

		List list = query.list();
		List<Map<String, Object>> ret = new ArrayList<>(list.size());
		for(int i=0, len=list.size(); i<len; i++){
			Object[] obj = (Object[]) list.get(i);
			Map<String, Object> map = new HashMap<>(obj.length);
			map.put("floginName", null == obj[0] ? "" : obj[0].toString());
			map.put("fNickName", null == obj[1] ? "" : obj[1].toString());
			map.put("fRealName", null == obj[2] ? "" : obj[2].toString());
			map.put("fEmail", null == obj[3] ? "" : obj[3].toString());
			map.put("fTelephone", null == obj[4] ? "" : obj[4].toString());
			map.put("fTotal", ((BigDecimal)obj[5]).doubleValue());
			map.put("fFrozen", ((BigDecimal)obj[6]).doubleValue());
			map.put("total", ((BigDecimal)obj[7]).doubleValue());
			map.put("fVi_fId", obj[8]);

			ret.add(map);
		}

		return ret;
	}

	/**
	 * 使用用户ID和虚拟币ID更新虚拟币钱包
	 * @param uid
	 * @param cid
	 * @param money
	 * @return
	 */
	public int updateFwalletMoney(int cid, int uid, double money, int awardPoolTotal, int origin){
		String sql = "update Fvirtualwallet set ftotal = ftotal + ?, flastUpdateTime = ? where fVi_fId = ? and fuid = ?";
//				"(select ifnull(sum(amount), 0) + ? from Btdlog where origin = ?) <= ?";
		Query query = getSession().createQuery(sql);
		query.setDouble(0, money);
		query.setTimestamp(1, Utils.getTimestamp());
		query.setInteger(2, cid);
		query.setInteger(3, uid);
		return query.executeUpdate();
//		query.setDouble(4, money);
//		query.setInteger(5, origin);
//		query.setInteger(6, awardPoolTotal);
//		return 11;
	}

	/**
	 * 更新用个虚拟币重量
	 * @param userId
	 * @param coinId
	 * @param amount
     * @return
     */
	public int updateVirtualWalletTotal(int userId, int coinId, double amount){
		return updateFwalletMoney(coinId, userId, amount, 0, 0);
	}

	public void insertAll(List<Map> list) {
		StringBuilder sqlBuf = new StringBuilder("insert ignore into fvirtualwallet (`fVi_fId`, `fTotal`, `fFrozen`, `fLastUpdateTime`, `fuid`, `version`) values");
		for (Map map : list) {
			sqlBuf.append("(");
			sqlBuf.append("'").append(map.get("fVi_fId")).append("',");
			sqlBuf.append("'").append(map.get("fTotal")).append("',");
			sqlBuf.append("'").append(map.get("fFrozen")).append("',");
			sqlBuf.append("'").append(map.get("fLastUpdateTime")).append("',");
			sqlBuf.append("'").append(map.get("fuid")).append("',");
			sqlBuf.append("'").append(map.get("version")).append("'");
			sqlBuf.append("),");
		}
		SQLQuery query = getSession().createSQLQuery(sqlBuf.deleteCharAt(sqlBuf.length() - 1).toString());
		query.executeUpdate();
	}

	public List<Integer> findNotAssign(int coinType, Integer lastUserId, Integer offset, Integer length) {
		StringBuilder sqlBuf = new StringBuilder("select u.fid from fuser u where not exists (select 1 from fvirtualwallet w where w.fVi_fId = :coinType and w.fuid = u.fid) ");
		if (Objects.nonNull(lastUserId)) {
			sqlBuf.append("and u.fid > :lastUserId ");
		}
		sqlBuf.append("order by u.fid");

		SQLQuery query = getSession().createSQLQuery(sqlBuf.toString());

		query.setParameter("coinType", coinType);
		if (Objects.nonNull(lastUserId)) {
			query.setParameter("lastUserId", lastUserId);
		}
		if (Objects.nonNull(offset) && Objects.nonNull(length)) {
			query.setFirstResult(offset);
			query.setMaxResults(length);
		} else if (Objects.nonNull(length)) {
			query.setMaxResults(length);
		}
		return query.list();
	}

	public int countNotAssign(int coinType) {
		String sql = "select count(u.fid) from fuser u where not exists (select 1 from fvirtualwallet w where w.fVi_fId = :coinType and w.fuid = u.fid)";
		SQLQuery query = getSession().createSQLQuery(sql);
		query.setParameter("coinType", coinType);
		return ((BigInteger)query.uniqueResult()).intValue();
	}
}



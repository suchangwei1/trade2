package com.trade.dao;

import com.trade.dao.comm.HibernateDaoSupport;
import com.trade.model.Fentrustlog;
import com.trade.util.CollectionUtils;
import com.trade.util.DateUtils;
import com.trade.util.StringUtils;
import com.trade.util.Utils;
import org.hibernate.*;
import org.hibernate.transform.Transformers;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.orm.hibernate3.HibernateCallback;
import org.springframework.stereotype.Repository;

import java.math.BigInteger;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.*;

import static org.hibernate.criterion.Example.create;

/**
 * A data access object (DAO) providing persistence and search support for
 * Fentrustlog entities. Transaction control of the save(), update() and
 * delete() operations can directly support Spring container-managed
 * transactions or they can be augmented to handle user-managed Spring
 * transactions. Each of these methods provides additional information for how
 * to configure it for the desired type of transaction control.
 * 
 * @see com.trade.com.trade.model.Fentrustlog
 * @author MyEclipse Persistence Tools
 */
@Repository
public class FentrustlogDAO extends HibernateDaoSupport {
	private static final Logger log = LoggerFactory
			.getLogger(FentrustlogDAO.class);
	// property constants
	public static final String FAMOUNT = "famount";
	public static final String FPRIZE = "fprize";
	public static final String FCOUNT = "fcount";

	public void save(Fentrustlog transientInstance) {
		log.debug("saving Fentrustlog instance");
		try {
			getHibernateTemplate().save(transientInstance);
			log.debug("save successful"); 
		} catch (RuntimeException re) {
			log.error("save failed", re);
			throw re;
		}
	}

	public void delete(Fentrustlog persistentInstance) {
		log.debug("deleting Fentrustlog instance");
		try {
			getHibernateTemplate().delete(persistentInstance);
			log.debug("delete successful");
		} catch (RuntimeException re) {
			log.error("delete failed", re);
			throw re;
		}
	}

	public Fentrustlog findById(java.lang.Integer id) {
		log.debug("getting Fentrustlog instance with id: " + id);
		try {
			Fentrustlog instance = (Fentrustlog) getSession().get(
					"com.trade.model.Fentrustlog", id);
			return instance;
		} catch (RuntimeException re) {
			log.error("get failed", re);
			throw re;
		}
	}

	public List<Fentrustlog> findByExample(Fentrustlog instance) {
		log.debug("finding Fentrustlog instance by example");
		try {
			List<Fentrustlog> results = (List<Fentrustlog>) getSession()
					.createCriteria("com.trade.model.Fentrustlog").add(create(instance))
					.list();
			log.debug("find by example successful, result size: "
					+ results.size());
			return results;
		} catch (RuntimeException re) {
			log.error("find by example failed", re);
			throw re;
		}
	}

	public List findByProperty(String propertyName, Object value) {
		log.debug("finding Fentrustlog instance with property: " + propertyName
				+ ", value: " + value);
		try {
			String queryString = "from Fentrustlog as model where model."
					+ propertyName + "= ?";
			Query queryObject = getSession().createQuery(queryString);
			queryObject.setParameter(0, value);
			return queryObject.list();
		} catch (RuntimeException re) {
			log.error("find by property name failed", re);
			throw re;
		}
	}

	public List findAll() {
		log.debug("finding all Fentrustlog instances");
		try {
			String queryString = "from Fentrustlog";
			Query queryObject = getSession().createQuery(queryString);
			return queryObject.list();
		} catch (RuntimeException re) {
			log.error("find all failed", re);
			throw re;
		}
	}

	public Fentrustlog merge(Fentrustlog detachedInstance) {
		log.debug("merging Fentrustlog instance");
		try {
			Fentrustlog result = (Fentrustlog) getSession().merge(
					detachedInstance);
			log.debug("merge successful");
			return result;
		} catch (RuntimeException re) {
			log.error("merge failed", re);
			throw re;
		}
	}

	public void attachDirty(Fentrustlog instance) {
		log.debug("attaching dirty Fentrustlog instance");
		try {
			getSession().saveOrUpdate(instance);
			log.debug("attach successful");
		} catch (RuntimeException re) {
			log.error("attach failed", re);
			throw re;
		}
	}

	public void attachClean(Fentrustlog instance) {
		log.debug("attaching clean Fentrustlog instance");
		try {
			getSession().lock(instance, LockMode.NONE);
			log.debug("attach successful");
		} catch (RuntimeException re) {
			log.error("attach failed", re);
			throw re;
		}
	}
	
	public List<Fentrustlog> findLatestSuccessDeal24(int coinTypeId,int hour){
		log.debug("findLatestSuccessDeal all Fentrustlog instances");
		try {
			String queryString = "from Fentrustlog where "+
					"fvirtualcointype.fid=? and " +
					"isactive=? and " +
					"fentrust!=null and " +
					"fcreateTime>?  " +
					"order by fid desc";
			Query queryObject = getSession().createQuery(queryString);
			queryObject.setParameter(0, coinTypeId) ;
			queryObject.setParameter(1, true) ;
			queryObject.setParameter(2, new Date(Utils.getTimestamp().getTime()-hour*60L*60*1000)) ;
			return queryObject.list();
		} catch (RuntimeException re) {
			log.error("find all failed", re);
			throw re;
		}
	}
	
	public Fentrustlog getLastFpeFentrustlog(int fvirtualcointype){
		log.debug("getLastFpeFentrustlog all Fentrustlog instances");
		try {
			String queryString = "from Fentrustlog where "+
					"fvirtualcointype.fid=? " +
					"order by fid desc";
			Query queryObject = getSession().createQuery(queryString);
			queryObject.setParameter(0, fvirtualcointype) ;
			queryObject.setFirstResult(0) ;
			queryObject.setMaxResults(1) ;
			List<Fentrustlog> fentrustlogs = queryObject.list();
			if(fentrustlogs.size()==0){
				return null ;
			}else{
				return fentrustlogs.get(0) ;
			}
		} catch (RuntimeException re) {
			log.error("find all failed", re);
			throw re;
		}
	}
	
	public double getFentrustAmt(){
		double amt = 0d;
		try {
			String queryString = "select sum(famount) from Fentrustlog ";
			Query queryObject = getSession().createSQLQuery(queryString);
			List list = queryObject.list();
            if(list != null && list.size() >0 && list.get(0) != null){
            	amt = Double.valueOf(list.get(0).toString());
            }
		} catch (RuntimeException re) {
			log.error("find all failed", re);
			throw re;
		}
		return amt;
	}
	
	public List list(int firstResult, int maxResults, String filter,
			boolean isFY) {
		List list = null;
		log.debug("finding Fentrustlog instance with filter");
		try {
			StringBuffer sf = new StringBuffer();
//			sf.append("select * from ( \n");
			sf.append("select \n");
			sf.append("c.fNickName,c.fRealName,c.floginName \n");
			sf.append(",sum(case WHEN a.fEntrustType=0 then a.fCount ELSE 0 END) totalBuy \n");
			sf.append(",sum(case WHEN a.fEntrustType=1 then a.fCount ELSE 0 END) totalSell \n");
			sf.append(",(sum(case WHEN a.fEntrustType=0 then a.fCount ELSE 0 END)+sum(case WHEN a.fEntrustType=1 then a.fCount ELSE 0 END)) total \n");
			sf.append(",date_format(a.fCreateTime,'%Y-%m-%d') createDate \n");
			sf.append("from fentrustlog a LEFT OUTER JOIN fentrust b on a.FEn_fId = b.fId \n");
			sf.append("left outer join fuser c on b.FUs_fId=c.fId \n");
			sf.append(filter +"\n");
			sf.append("GROUP BY createDate,b.`FUs_fId` \n");
			sf.append("order by createDate,total desc");
			Query queryObject = getSession().createSQLQuery(sf.toString());
			if (isFY) {
				queryObject.setFirstResult(firstResult);
				queryObject.setMaxResults(maxResults);
			}
			list = queryObject.list();
		} catch (RuntimeException re) {
			log.error("find Fentrustlog by filter name failed", re);
			throw re;
		}
		return list;
	}
	
	public int countForList(String filter) {
		StringBuffer sf = new StringBuffer();
		sf.append("select count(*) from ( \n");
		sf.append("select \n");
		sf.append("c.fNickName \n");
		sf.append(",date_format(a.fCreateTime,'%Y-%m-%d') createDate \n");
		sf.append("from fentrustlog a LEFT OUTER JOIN fentrust b on a.FEn_fId = b.fId \n");
		sf.append("left outer join fuser c on b.FUs_fId=c.fId \n");
		sf.append(filter +"\n");
		sf.append("GROUP BY createDate,b.`FUs_fId` \n");
		sf.append(")as t");
		Query queryObject = getSession().createSQLQuery(sf.toString());
		return ((BigInteger)queryObject.uniqueResult()).intValue();
	}
	

	public List<Fentrustlog> listxx(int firstResult, int maxResults,
			String filter,boolean isFY) {
		List<Fentrustlog> list = null;
		log.debug("finding Fentrustlog instance with filter");
		try {
			String queryString = "from Fentrustlog "+filter;
			Query queryObject = getSession().createQuery(queryString);
			queryObject.setCacheable(true);
			if(isFY){
				queryObject.setFirstResult(firstResult);
				queryObject.setMaxResults(maxResults);
			}
			list = queryObject.list();
		} catch (RuntimeException re) {
			log.error("find Fentrustlog by filter name failed", re);
			throw re;
		}
		return list;
	}
	
	public List<Fentrustlog> findByViCoin(int coinId, int sinceId, int maxResults, boolean isFY) {
		List<Fentrustlog> list = null;
		log.debug("finding Fentrustlog instance");
		try {
			String queryString = "from Fentrustlog where fvirtualcointype.fid = ? and fid > ? order by fcreateTime desc";
			Query queryObject = getSession().createQuery(queryString);
			queryObject.setCacheable(true);
			if(isFY){
				queryObject.setMaxResults(maxResults);
			}
			queryObject.setParameter(0, coinId);
			queryObject.setParameter(1, sinceId);
			list = queryObject.list();
		} catch (RuntimeException re) {
			log.error("find Fentrustlog by name failed", re);
			throw re;
		}
		return list;
	}
	
	public Fentrustlog getClosingEntrust(final int fvirtualcointype){
		log.debug("get Closing Entrust ");
		try { 
			String queryString = "from Fentrustlog where " +
					"fvirtualcointype.fid=? and " +
					"TO_DAYS(fcreateTime) = TO_DAYS(?) " +
					"order by fcreateTime desc" ;
			Query queryObject = getSession().createQuery(queryString) ;
			queryObject.setParameter(0, fvirtualcointype) ;
			queryObject.setTimestamp( 1, new Timestamp( System.currentTimeMillis() - 24*60*60*1000 ) ) ;
			queryObject.setFirstResult(0) ;
			queryObject.setMaxResults(1) ;
			List<Fentrustlog> fentrustlogs = queryObject.list();
			if(fentrustlogs.size()==0){
				return null ;
			}else{
				return fentrustlogs.get(0) ;
			}

//			return getHibernateTemplate().execute(new HibernateCallback<Fentrustlog>() {
//				@Override
//				public Fentrustlog doInHibernate(Session session) throws HibernateException, SQLException {
//					// 查询优化，原來的查询需要5秒以上，优化后只需要不到几百豪秒
//					String queryString = "from Fentrustlog where fid = (SELECT MAX(fid) as fid from Fentrustlog where " +
//							"fvirtualcointype.fid=? and " +
//							"TO_DAYS(fcreateTime) = TO_DAYS(?))" ;
//					Query queryObject = session.createQuery(queryString);
//					queryObject.setParameter(0, fvirtualcointype) ;
//					queryObject.setTimestamp( 1, new Timestamp( System.currentTimeMillis() - 24*60*60*1000 ) ) ;
//					return (Fentrustlog) queryObject.uniqueResult();
//				}
//			});

		} catch (RuntimeException re) {
			log.error("get Closing Entrust failed", re);
			throw re;
		}
	}
	
	public Fentrustlog getOpenningEntrust(final int fvirtualcointype){
		log.debug("get Stopping Entrust ");
		try { 
			String queryString = "from Fentrustlog where "+
					"fvirtualcointype.fid=? and " +
					"TO_DAYS(fcreateTime) = TO_DAYS(?) " +
					"order by fcreateTime asc";
			Query queryObject = getSession().createQuery(queryString);
			queryObject.setParameter(0, fvirtualcointype) ;
			queryObject.setTimestamp(1, new Timestamp(System.currentTimeMillis())) ;
			queryObject.setFirstResult(0) ;
			queryObject.setMaxResults(1) ;
			List<Fentrustlog> fentrustlogs = queryObject.list();
			if(fentrustlogs.size()==0){
				return null ;
			}else{
				return fentrustlogs.get(0) ;
			}

//			return getHibernateTemplate().execute(new HibernateCallback<Fentrustlog>() {
//				@Override
//				public Fentrustlog doInHibernate(Session session) throws HibernateException, SQLException {
//					// 查询优化，原來的查询需要5秒以上，优化后只需要不到几百豪秒
//					String queryString = "from Fentrustlog where fid = (SELECT MIN(fid) as fid from Fentrustlog where " +
//							"fvirtualcointype.fid=? and " +
//							"TO_DAYS(fcreateTime) = TO_DAYS(?))" ;
//					Query queryObject = session.createQuery(queryString);
//					queryObject.setParameter(0, fvirtualcointype) ;
//					queryObject.setTimestamp(1, new Timestamp(System.currentTimeMillis())) ;
//					return (Fentrustlog) queryObject.uniqueResult();
//				}
//			});

		} catch (RuntimeException re) {
			log.error("getStoppingEntrust failed", re);
			throw re;
		}
	}
	
	public Fentrustlog getEntrustBeforeWeek(final int fvirtualcointype){
		log.debug("get Entrust  before week ");
		try { 
			String queryString = "from Fentrustlog where " +
					"fvirtualcointype.fid=? and " +
					"TO_DAYS(fcreateTime) = TO_DAYS(?) " +
					"order by fcreateTime asc" ;

//			return getHibernateTemplate().execute(new HibernateCallback<Fentrustlog>() {
//				@Override
//				public Fentrustlog doInHibernate(Session session) throws HibernateException, SQLException {
//					// 查询优化，原來的查询需要5秒以上，优化后只需要不到几百豪秒
//					String queryString = "from Fentrustlog where fid = (" + "SELECT MIN(fid) as fid from Fentrustlog where " +
//							"fvirtualcointype.fid=? and " +
//							"TO_DAYS(fcreateTime) = TO_DAYS(?))" ;
//					Query queryObject = session.createQuery(queryString);
//					queryObject.setParameter(0, fvirtualcointype) ;
//					queryObject.setTimestamp( 1, new Timestamp( System.currentTimeMillis() - 7*24*60*60*1000 ) ) ;
//					return (Fentrustlog) queryObject.uniqueResult();
//				}
//			});


			Query queryObject = getSession().createQuery(queryString) ;
			queryObject.setParameter(0, fvirtualcointype) ;
			queryObject.setTimestamp( 1, new Timestamp( System.currentTimeMillis() - 7*24*60*60*1000 ) ) ;
			queryObject.setFirstResult(0) ;
			queryObject.setMaxResults(1) ;
			List<Fentrustlog> fentrustlogs = queryObject.list();
			if(fentrustlogs.size()==0){
				return null ;
			}else{
				return fentrustlogs.get(0) ;
			}
		} catch (RuntimeException re) {
			log.error("get Entrust before week failed", re);
			throw re;
		}
	}
	
	/**
	 * 统计每天成金额
	 * 
	 * @param coinId
	 * @param entrustType
	 * @param startDate
	 * @param endDate
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<Object> findAmountByDay(Integer coinId, Short entrustType, String startDate, String endDate){
		StringBuilder sqlBuf = new StringBuilder();
		sqlBuf.append("select DATE_FORMAT(log.fCreateTime, '%Y-%m-%d') as date, sum(log.fAmount) as num from fentrustlog log left join fentrust e on e.fId=log.FEn_fId where 1=1");
		
		if(null != coinId){
			sqlBuf.append(" and log.FVI_type=").append(coinId);
		}
		if(null != entrustType){
			sqlBuf.append(" and log.fEntrustType=").append(entrustType);
		}
		if(StringUtils.hasText(startDate)){
			sqlBuf.append(" and log.fCreateTime>=:start");
		}
		if(StringUtils.hasText(endDate)){
			sqlBuf.append(" and log.fCreateTime<=:end");
		}
		sqlBuf.append(" and e.robotStatus=0");
		sqlBuf.append(" group by date order by date");
		
		Query query = getSession().createSQLQuery(sqlBuf.toString());
		
		if(StringUtils.hasText(startDate)){
			query.setParameter("start", startDate);
		}
		if(StringUtils.hasText(endDate)){
			query.setParameter("end", endDate);
		}
		
		return query.list();
	}
	
	/**
	 * 统计每天成金额成交量
	 * 
	 * @param coinId
	 * @param entrustType
	 * @param startDate
	 * @param endDate
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<Object> findCountByDay(Integer coinId, Short entrustType, String startDate, String endDate){
		StringBuilder sqlBuf = new StringBuilder();
		sqlBuf.append("select DATE_FORMAT(log.fCreateTime, '%Y-%m-%d') as date, sum(log.fCount) as num from fentrustlog log left join fentrust e on e.fId=log.FEn_fId where 1=1");
		
		if(null != coinId){
			sqlBuf.append(" and log.FVI_type=").append(coinId);
		}
		if(null != entrustType){
			sqlBuf.append(" and log.fEntrustType=").append(entrustType);
		}
		if(StringUtils.hasText(startDate)){
			sqlBuf.append(" and log.fCreateTime>=:start");
		}
		if(StringUtils.hasText(endDate)){
			sqlBuf.append(" and log.fCreateTime<=:end");
		}
		sqlBuf.append(" and e.robotStatus=0");
		sqlBuf.append(" group by date order by date");
		
		Query query = getSession().createSQLQuery(sqlBuf.toString());
		
		if(StringUtils.hasText(startDate)){
			query.setParameter("start", startDate);
		}
		if(StringUtils.hasText(endDate)){
			query.setParameter("end", endDate);
		}
		
		return query.list();
	}

	public double avgSuccessPrice(int entrustId){
		log.debug("ang success price");
		String hql = "select sum(fprize)/count(fid) from com.trade.model.Fentrustlog where fentrust.fid=" + entrustId;
		Query query = getSession().createQuery(hql);
		Object res = query.uniqueResult();
		if(null == res) return 0;
		return ((Double)query.uniqueResult()).doubleValue();
	}

	/**
	 * 统计每小时成交平均价格
	 *
	 * @param fVid
	 * @param startDate
	 * @param endDate
     * @return
     */
	public Map findAvgPriceByHour(int fVid, Date startDate, Date endDate){
		StringBuilder sqlBuf = new StringBuilder();
		sqlBuf.append("select DATE_FORMAT(fCreateTime, '%Y-%m-%d %H') _t, sum(fPrize)/count(fid) from fentrustlog where 1=1");
		sqlBuf.append(" and FVI_type=").append(fVid);
		if(null != startDate){
			sqlBuf.append(" and fCreateTime >=:start");
		}
		if(null != endDate){
			sqlBuf.append(" and fCreateTime <:end");
		}
		sqlBuf.append(" group by _t order by _t");

		Query query = getSession().createSQLQuery(sqlBuf.toString());

		if(null != startDate){
			query.setParameter("start", startDate);
		}
		if(null != endDate){
			query.setParameter("end", startDate);
		}
		List list = query.list();
		if(CollectionUtils.isEmpty(list)){
			return Collections.emptyMap();
		}

		Map<Object, Object> map = new TreeMap<>();
		for(Object obj : list){
			Object[] objs = (Object[]) obj;
			map.put(DateUtils.formatDate(objs[0].toString(), "yyyy-MM-dd HH").getTime(), objs[1]);
		}

		return map;
	}

	public double getUpAndDown(int coinId, Date start, Date end){
		double lastestPrice = getLastestDealPrice(coinId, start, end);
		double startPrice = getStartPrice(coinId, start, end);

		if(lastestPrice != 0 && startPrice != 0){
			return (lastestPrice - startPrice) / startPrice;
		}
		return 0;
	}

	public double getLastestDealPrice(int coinId, Date start, Date end){
		StringBuilder sqlBuf = new StringBuilder("select fprize from fentrustlog where FVI_type =:type");
		if(null != start){
			sqlBuf.append(" and fcreateTime >= FROM_UNIXTIME(:start)");
		}
		if(null != end){
			sqlBuf.append(" and fcreateTime <= FROM_UNIXTIME(:end)");
		}
		sqlBuf.append(" order by fcreateTime desc limit 1");
		Query query = getSession().createSQLQuery(sqlBuf.toString());
		query.setParameter("type", coinId);
		if(null != start){
			query.setParameter("start", start.getTime()/1000);
		}
		if(null != end){
			query.setLong("end", end.getTime()/1000);
		}
		Object obj = query.uniqueResult();
		if(obj == null) return 0;
		return Double.valueOf(obj + "");
	}

	public double getStartPrice(final int fvirtualcointype, Date start, Date end){

		String sql = "select fprize from fentrustlog where FVI_type = ? AND fcreateTime >= FROM_UNIXTIME(?) and fcreateTime <= FROM_UNIXTIME(?) order by fcreateTime limit 1";
		Query query = getSession().createSQLQuery(sql);
		query.setInteger(0, fvirtualcointype);
		query.setLong(1, start.getTime()/1000);
		query.setLong(2, end.getTime()/1000);
		Object object = query.uniqueResult();
		if(object == null){
			return 0;
		}
		return Double.valueOf(object + "");
	}


	/**
	 * 交易额
	 * @param coinId
	 * @param start
	 * @param end
     * @return
     */
	public double getTradeCount(int coinId, Date start, Date end){
		String sql = "select SUM(fAmount) from fentrustlog where FVI_type = ? AND fEntrustType = ? AND fcreateTime >= FROM_UNIXTIME(?) and fcreateTime <= FROM_UNIXTIME(?)";
		Query query = getSession().createSQLQuery(sql);
		query.setInteger(0, coinId);
		query.setInteger(1, 1);
		query.setLong(2, start.getTime()/1000);
		query.setLong(3, end.getTime()/1000);
		Object object = query.uniqueResult();
		if(object == null){
			return 0;
		}
		return Double.valueOf(object + "");
	}

	public List<Fentrustlog> findByProperties(Map<String, Object> propValues, Date startTime, Date endTime, Integer offset, Integer length) {
		StringBuilder sqlBuf = new StringBuilder("from Fentrustlog as m where 1=1 ");

		for(String prop : propValues.keySet()) {
			sqlBuf.append("and m.").append(prop).append("=? ");
		}
		if(!Objects.isNull(startTime)) {
			sqlBuf.append("and m.fcreateTime>=:startTime ");
		}
		if(!Objects.isNull(endTime)) {
			sqlBuf.append("and m.fcreateTime<=:endTime ");
		}
		sqlBuf.append("order by fid desc");

		Query query = getSession().createQuery(sqlBuf.toString());

		Object[] vals = propValues.values().toArray();
		for(int i=0; i<vals.length; i++) {
			query.setParameter(i, vals[i]);
		}
		if(!Objects.isNull(startTime)) {
			query.setParameter("startTime", startTime);
		}
		if(!Objects.isNull(endTime)) {
			query.setParameter("endTime", endTime);
		}
		if(!Objects.isNull(offset) && !Objects.isNull(length)) {
			query.setFirstResult(offset);
			query.setMaxResults(length);
		} else if(!Objects.isNull(length)) {
			query.setMaxResults(length);
		}

		List list = query.list();
		return list;
	}


	public List<Map<String, Object>> findUnsharedLogs(String beginDate) {
		StringBuilder sqlBuf = new StringBuilder("select l.fid as id, e.FUs_fId as uid, l.ffees as fees, l.fCreateTime as createTime, l.fEntrustType as type, l.fVI_type as market  from fentrustlog l  left join fentrust e on e.fId = l.fEn_fId where l.isShared = 0 and l.fCreateTime > '" + beginDate+"'");
		Query query = getSession().createSQLQuery(sqlBuf.toString());
		List list = query.setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP).list();
		return list;
	}


	public List<Map> countFees(String startDate, String endDate){
		try {
			String filter = "select sum(ffees) as fee, FVI_type as mid, fEntrustType as type from fentrustlog where 1 = 1";
			if(StringUtils.hasText(startDate)){
				filter += " and fCreateTime >= '" + startDate + "'";
			}
			if(StringUtils.hasText(endDate)){
				filter += " and fCreateTime <= '" + endDate + "'";
			}
			filter += " group by FVI_type, fEntrustType";
			StringBuffer queryString = new StringBuffer(filter);
			Query queryObject = getSession().createSQLQuery(queryString.toString());
			return queryObject.setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP).list();
		} catch (RuntimeException re) {
			log.error("count all failed", re);
			throw re;
		}
	}

	public int countByProperties(Map<String, Object> propValues, Date startTime, Date endTime) {
		StringBuilder sqlBuf = new StringBuilder("select count(m.fid) from Fentrustlog as m where 1=1 ");

		for(String prop : propValues.keySet()) {
			sqlBuf.append("and m.").append(prop).append("=? ");
		}
		if(!Objects.isNull(startTime)) {
			sqlBuf.append("and m.fcreateTime>=:startTime ");
		}
		if(!Objects.isNull(endTime)) {
			sqlBuf.append("and m.fcreateTime<=:endTime ");
		}

		Query query = getSession().createQuery(sqlBuf.toString());

		Object[] vals = propValues.values().toArray();
		for(int i=0; i<vals.length; i++) {
			query.setParameter(i, vals[i]);
		}
		if(!Objects.isNull(startTime)) {
			query.setParameter("startTime", startTime);
		}
		if(!Objects.isNull(endTime)) {
			query.setParameter("endTime", endTime);
		}

		return ((Long)query.uniqueResult()).intValue();
	}

	public List findAllList(int firstResult, int maxResults,
							   String filter, boolean isFY) {
		List list = null;
		log.debug("finding Fentrustlog instance with filter");
		try {
//			String queryString = "from Fentrustlog " + filter;
			Query queryObject = getSession().createSQLQuery(filter);
			if (isFY) {
				queryObject.setFirstResult(firstResult);
				queryObject.setMaxResults(maxResults);
			}
			list = queryObject.setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP).list();
		} catch (RuntimeException re) {
			log.error("find Fentrustlog by filter name failed", re);
			throw re;
		}
		return list;
	}

	/*public int findAllListCount(String s){
		Query queryObject = getSession().createSQLQuery(s);
		List list ;
		list = queryObject.list();
		return ((BigInteger)list.get(0)).intValue();
	}*/
	public int findAllListCount(String s){
		Query queryObject = getSession().createSQLQuery(s);
		List list ;
		list = queryObject.list();
         if(list!=null&& list.size()!=0){
			return ((BigInteger)list.get(0)).intValue();
		}
		return 0;
}
}








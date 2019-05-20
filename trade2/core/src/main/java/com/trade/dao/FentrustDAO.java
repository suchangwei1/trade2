package com.trade.dao;

import com.trade.Enum.EntrustStatusEnum;
import com.trade.Enum.EntrustTypeEnum;
import com.trade.dao.comm.HibernateDaoSupport;
import com.trade.model.Fentrust;
import com.trade.model.Fentrustlog;
import com.trade.util.FormatUtils;
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
import org.springframework.orm.hibernate3.HibernateCallback;
import org.springframework.stereotype.Repository;
import org.springframework.util.ObjectUtils;

import java.math.BigInteger;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.TreeMap;

import static org.hibernate.criterion.Example.create;

/**
 * A data access object (DAO) providing persistence and search support for
 * Fentrust entities. Transaction control of the save(), update() and delete()
 * operations can directly support Spring container-managed transactions or they
 * can be augmented to handle user-managed Spring transactions. Each of these
 * methods provides additional information for how to configure it for the
 * desired type of transaction control.
 *
 * @author MyEclipse Persistence Tools
 */
@Repository
public class FentrustDAO extends HibernateDaoSupport {
	private static final Logger log = LoggerFactory
			.getLogger(FentrustDAO.class);
	// property constants
	public static final String FENTRUST_TYPE = "fentrustType";
	public static final String FPRIZE = "fprize";
	public static final String FAMOUNT = "famount";
	public static final String FCOUNT = "fcount";
	public static final String FSTATUS = "fstatus";

	@Autowired
	private JdbcTemplate jdbcTemplate;

	public void save(Fentrust transientInstance) {
		log.debug("saving Fentrust instance");
		try {
			getSession().save(transientInstance);
			log.debug("save successful");
		} catch (RuntimeException re) {
			log.error("save failed", re);
			throw re;
		}
	}

	public void delete(Fentrust persistentInstance) {
		log.debug("deleting Fentrust instance");
		try {
			getSession().delete(persistentInstance);
			log.debug("delete successful");
		} catch (RuntimeException re) {
			log.error("delete failed", re);
			throw re;
		}
	}

	public Fentrust findById(int id) {
		log.debug("getting Fentrust instance with id: " + id);
		try {
			String hql = "from Fentrust f where f.fid = ?";
			Fentrust instance = (Fentrust) getHibernateTemplate().find(hql, id).get(0);
			return instance;
		} catch (RuntimeException re) {
			log.error("get failed", re);
			System.out.println("挂单ID：" + id);
			throw re;
		}
	}

	public List<Fentrust> findByExample(Fentrust instance) {
		log.debug("finding Fentrust instance by example");
		try {
			List<Fentrust> results = (List<Fentrust>) getSession()
					.createCriteria("com.trade.model.Fentrust").add(create(instance))
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
		log.debug("finding Fentrust instance with property: " + propertyName
				+ ", value: " + value);
		try {
			String queryString = "from Fentrust as model where model."
					+ propertyName + "= ?";
			Query queryObject = getSession().createQuery(queryString);
			queryObject.setParameter(0, value);
			return queryObject.list();
		} catch (RuntimeException re) {
			log.error("find by property name failed", re);
			throw re;
		}
	}

	public List<Fentrust> findByFentrustType(Object fentrustType) {
		return findByProperty(FENTRUST_TYPE, fentrustType);
	}

	public List<Fentrust> findByFprize(Object fprize) {
		return findByProperty(FPRIZE, fprize);
	}

	public List<Fentrust> findByFamount(Object famount) {
		return findByProperty(FAMOUNT, famount);
	}

	public List<Fentrust> findByFcount(Object fcount) {
		return findByProperty(FCOUNT, fcount);
	}

	public List<Fentrust> findByFstatus(Object fstatus) {
		return findByProperty(FSTATUS, fstatus);
	}

	public List findAll() {
		log.debug("finding all Fentrust instances");
		try {
			String queryString = "from Fentrust";
			Query queryObject = getSession().createQuery(queryString);
			return queryObject.list();
		} catch (RuntimeException re) {
			log.error("find all failed", re);
			throw re;
		}
	}

	public Fentrust merge(Fentrust detachedInstance) {
		log.debug("merging Fentrust instance");
		try {
			Fentrust result = (Fentrust) getSession().merge(detachedInstance);
			log.debug("merge successful");
			return result;
		} catch (RuntimeException re) {
			log.error("merge failed", re);
			throw re;
		}
	}

	public void attachDirty(Fentrust instance) {
		log.debug("attaching dirty Fentrust instance");
		try {
			getSession().saveOrUpdate(instance);
			log.debug("attach successful");
		} catch (RuntimeException re) {
			log.error("attach failed", re);
			throw re;
		}
	}

	public void attachClean(Fentrust instance) {
		log.debug("attaching clean Fentrust instance");
		try {
			getSession().lock(instance, LockMode.NONE);
			log.debug("attach successful");
		} catch (RuntimeException re) {
			log.error("attach failed", re);
			throw re;
		}
	}
	
	public Fentrust findLatestDeal(int coinTypeId){
		log.debug("findLatestSuccessDeal all Fentrust instances");
		try {
			String queryString = "from Fentrust where  fstatus=? and fvirtualcointype.fid=? and fentrustType=?  order by flastUpdatTime desc";
			Query queryObject = getSession().createQuery(queryString);
			queryObject.setParameter(0, EntrustStatusEnum.AllDeal) ;
			queryObject.setParameter(1, coinTypeId) ;
			queryObject.setParameter(2, EntrustTypeEnum.SELL) ;
			
			queryObject.setFirstResult(0) ;
			queryObject.setMaxResults(1) ;
			List<Fentrust> fentrusts = queryObject.list();
			if(fentrusts.size()>0 && fentrusts != null){
				if(fentrusts.get(0) != null){
					return fentrusts.get(0) ;
				}
				return null ;
			}else{
				return null ;
			}
		} catch (RuntimeException re) {
			log.error("find all failed", re);
			throw re;
		}
	}
	
	public List<Fentrust> findLatestSuccessDeal(int coinTypeId,int fentrustType,int count){
		log.debug("findLatestSuccessDeal all Fentrust instances");
		try {
			String queryString = "from Fentrust where fstatus=? and fvirtualcointype.fid=? and fentrustType=?  order by flastUpdatTime desc";
			Query queryObject = getSession().createQuery(queryString);
			queryObject.setParameter(0, EntrustStatusEnum.AllDeal) ;
			queryObject.setParameter(1, coinTypeId) ;
			queryObject.setParameter(2, fentrustType) ;
			
			queryObject.setFirstResult(0) ;
			queryObject.setMaxResults(count) ;
			return queryObject.list();
		} catch (RuntimeException re) {
			log.error("find all failed", re);
			throw re;
		}
	}

	/**
	 * 查询所有未成交的挂单
	 * 买单卖单一起查
	 * @param coinTypeId
	 * @param isLimit
     * @return
     */
	public List<Fentrust> findAllGoingFentrust(int coinTypeId, boolean isLimit) {
		log.debug("findAllGoingFentrust all Fentrust instances");
		try {
			String queryString = "from Fentrust where (fstatus=? or fstatus=?)" +
					" and fvirtualcointype.fid=?" +
					" and fisLimit=?";
			Query queryObject = getSession().createQuery(queryString);
			queryObject.setParameter(0, EntrustStatusEnum.Going) ;
			queryObject.setParameter(1, EntrustStatusEnum.PartDeal) ;
			queryObject.setParameter(2, coinTypeId);
			queryObject.setParameter(3, isLimit);

			return queryObject.list();
		} catch (RuntimeException re) {
			log.error("find all failed", re);
			throw re;
		}
	}
	
	public List<Fentrust> findAllGoingFentrust(int coinTypeId,int fentrustType,boolean isLimit){
		log.debug("findAllGoingFentrust all Fentrust instances");
		try {
			String queryString = "from Fentrust where (fstatus=? or fstatus=?)" +
					" and fvirtualcointype.fid=?" +
					" and fentrustType=? and fisLimit=?";
			Query queryObject = getSession().createQuery(queryString);
			queryObject.setParameter(0, EntrustStatusEnum.Going) ;
			queryObject.setParameter(1, EntrustStatusEnum.PartDeal) ;
			queryObject.setParameter(2, coinTypeId) ;
			queryObject.setParameter(3, fentrustType) ;
			queryObject.setParameter(4, isLimit) ;
			
			return queryObject.list();
		} catch (RuntimeException re) {
			log.error("find all failed", re);
			throw re;
		}
	}
	
	public List<Fentrust> list(int firstResult, int maxResults,
			String filter, boolean isFY) {
		List<Fentrust> list = null;
		log.debug("finding Fentrust instance with filter");
		try {
			String queryString = "from Fentrust " + filter;
			Query queryObject = getSession().createQuery(queryString);
			if (isFY) {
				queryObject.setFirstResult(firstResult);
				queryObject.setMaxResults(maxResults);
			}
			list = queryObject.list();
		} catch (RuntimeException re) {
			log.error("find Fentrust by filter name failed", re);
			throw re;
		}
		return list;
	}

	public List getFentrustHistory(int fuid, int fviFid, int first, int max) {
		List<Map<String, Object>> list = jdbcTemplate.queryForList("select fEntrustType, fPrize, fleftCount, fId, fCreateTime, fStatus from fentrust where FUs_fId = ? and fVi_fId = ? and (fStatus = ? or fStatus = ?) order by fid desc limit ?, ?", fuid, fviFid, EntrustStatusEnum.Going, EntrustStatusEnum.PartDeal, first, max);
		List ret = new ArrayList<>(list.size());
		list.forEach(map -> {
			ret.add(new Object[]{map.get("fEntrustType"), map.get("fPrize"), FormatUtils.formatCoin(Double.valueOf(map.get("fleftCount").toString()),"0.0000"), map.get("fId"), map.get("fCreateTime"), map.get("fStatus")});
		});
		return ret;
//		SQLQuery query = getSession().createSQLQuery("select fEntrustType, fPrize, fleftCount, fId from fentrust where FUs_fId = ? and fVi_fId = ? and (fStatus = ? or fStatus = ?) order by fid desc");
//		query.setParameter(0, fuid);
//		query.setParameter(1, fviFid);
//		query.setParameter(2, EntrustStatusEnum.Going);
//		query.setParameter(3, EntrustStatusEnum.PartDeal);
//		query.setFirstResult(first);
//		query.setMaxResults(max);
//		List list = query.list();
//		return list;
	}

	public List<Fentrust> findFentrustHistory(int fuid, int fviFid, int first, int max) {
		List<Map<String, Object>> list = jdbcTemplate.queryForList("select fEntrustType, fPrize, (fCount-fleftCount) fCount, fId, fStatus,fsuccessAmount, fCreateTime from fentrust where FUs_fId = ? and fVi_fId = ? AND fsuccessAmount > 0 and (fStatus = ? or fStatus = ? ) order by fid desc limit ?, ?", fuid, fviFid, EntrustStatusEnum.AllDeal, EntrustStatusEnum.Cancel, first, max);
		List ret = new ArrayList<>(list.size());
		list.forEach(map -> {
			ret.add(new Object[]{map.get("fEntrustType"), map.get("fPrize"), FormatUtils.formatCoin(Double.valueOf(map.get("fCount").toString()),"0.0000") , map.get("fId"), map.get("fStatus"),   FormatUtils.formatCoin(Double.valueOf(map.get("fsuccessAmount").toString()),"0.0000"), map.get("fCreateTime")});
		});
		return ret;
//		SQLQuery query = getSession().createSQLQuery("select fEntrustType, fPrize, fCount, fId, fStatus from fentrust where FUs_fId = ? and fVi_fId = ? and (fStatus = ?) order by fid desc");
//		query.setParameter(0, fuid);
//		query.setParameter(1, fviFid);
//		query.setParameter(2, EntrustStatusEnum.AllDeal);
//		query.setFirstResult(first);
//		query.setMaxResults(max);
//		List list = query.list();
//		return list;
	}
	
	public List<Fentrust> getFentrustHistory(
			int fuid,int fvirtualCoinTypeId,int[] entrust_type,
			int first_result,int max_result,String order,
			int entrust_status[]){
		
		log.debug("getFentrustHistory all Fentrust instances");
		try {
			StringBuffer queryString = new StringBuffer("from Fentrust where fuser.fid=? " +
					" and fvirtualcointype.fid=? and  ") ;
			
			if(entrust_type!=null){
				queryString.append(" ( ") ;
				for (int i=0;i<entrust_type.length;i++) {
					if(i==0){
						queryString.append(" fentrustType=? ") ;
					}else{
						queryString.append(" or fentrustType=? ") ;
					}
				}
				queryString.append(" ) and ") ;
			}
			
			if(entrust_status!=null){
				queryString.append(" ( ") ;
				for (int i=0;i<entrust_status.length;i++) {
					if(i==0){
						queryString.append(" fstatus=? ") ;
					}else{
						queryString.append(" or fstatus=? ") ;
					}
				}
				queryString.append(" ) and ") ;
			}
			
			queryString.append(" 1=1 ") ;
			
			if(order!=null){
				queryString.append(" order by "+order) ;
			}
			
			Query queryObject = getSession().createQuery(queryString.toString());
			queryObject.setParameter(0, fuid) ;
			queryObject.setParameter(1, fvirtualCoinTypeId) ;
			
			int index = 2 ;
			if(entrust_type!=null){
				for (int i = 0; i < entrust_type.length; i++) {
					queryObject.setParameter(index+i, entrust_type[i]) ;
				}
				index+=entrust_type.length ;
			}
			
			if(entrust_status!=null){
				for (int i=0;i<entrust_status.length;i++) {
					queryObject.setParameter(index+i, entrust_status[i]) ;
				}
			}

			queryObject.setFirstResult(first_result) ;
			queryObject.setMaxResults(max_result) ;
			return queryObject.list();
		} catch (RuntimeException re) {
			log.error("find all failed", re);
			throw re;
		}
		
	}
	
	public int getFentrustHistoryCount(
			int fuid,int fvirtualCoinTypeId,int[] entrust_type,
			int entrust_status[]){
		
		log.debug("getFentrustHistory all Fentrust instances");
		try {
			StringBuffer queryString = new StringBuffer("select count(fid) from Fentrust where fuser.fid=? " +
					" and fvirtualcointype.fid=? and  ") ;
			
			if(entrust_type!=null){
				queryString.append(" ( ") ;
				for (int i=0;i<entrust_type.length;i++) {
					if(i==0){
						queryString.append(" fentrustType=? ") ;
					}else{
						queryString.append(" or fentrustType=? ") ;
					}
				}
				queryString.append(" ) and ") ;
			}
			
			if(entrust_status!=null){
				queryString.append(" ( ") ;
				for (int i=0;i<entrust_status.length;i++) {
					if(i==0){
						queryString.append(" fstatus=? ") ;
					}else{
						queryString.append(" or fstatus=? ") ;
					}
				}
				queryString.append(" ) and ") ;
			}
			
			queryString.append(" 1=1 ") ;
			
			Query queryObject = getSession().createQuery(queryString.toString());
			queryObject.setParameter(0, fuid) ;
			queryObject.setParameter(1, fvirtualCoinTypeId) ;
			
			int index = 2 ;
			if(entrust_type!=null){
				for (int i = 0; i < entrust_type.length; i++) {
					queryObject.setParameter(index+i, entrust_type[i]) ;
				}
				index+=entrust_type.length ;
			}
			
			if(entrust_status!=null){
				for (int i=0;i<entrust_status.length;i++) {
					queryObject.setParameter(index+i, entrust_status[i]) ;
				}
			}
			
			long l = (Long)queryObject.list().get(0);
			return (int) l;
		} catch (RuntimeException re) {
			log.error("find all failed", re);
			throw re;
		}
		
	}
	
	public List<Fentrust> getFentrustHistory(
			int fuid,int fvirtualCoinTypeId,int[] entrust_type,
			int first_result,int max_result,String order,
			int entrust_status[],
			Date beginDate,Date endDate){
		
		log.debug("getFentrustHistory all Fentrust instances");
		try {
			StringBuffer queryString = new StringBuffer("from Fentrust where fuser.fid=? " +
					" and fvirtualcointype.fid=? and  ") ;
			
			if(beginDate!=null && endDate!=null){
				SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd") ;
				queryString.append(" fcreateTime>='"+sdf.format(beginDate)+"' and fcreateTime<='"+sdf.format(endDate)+"' and ") ;
			}
			
			if(entrust_type!=null){
				queryString.append(" ( ") ;
				for (int i=0;i<entrust_type.length;i++) {
					if(i==0){
						queryString.append(" fentrustType=? ") ;
					}else{
						queryString.append(" or fentrustType=? ") ;
					}
				}
				queryString.append(" ) and ") ;
			}
			
			if(entrust_status!=null){
				queryString.append(" ( ") ;
				for (int i=0;i<entrust_status.length;i++) {
					if(i==0){
						queryString.append(" fstatus=? ") ;
					}else{
						queryString.append(" or fstatus=? ") ;
					}
				}
				queryString.append(" ) and ") ;
			}
			
			queryString.append(" 1=1 ") ;
			
			if(order!=null){
				queryString.append(" order by "+order) ;
			}
			
			Query queryObject = getSession().createQuery(queryString.toString());
			queryObject.setParameter(0, fuid) ;
			queryObject.setParameter(1, fvirtualCoinTypeId) ;
			
			int index = 2 ;
			if(entrust_type!=null){
				for (int i = 0; i < entrust_type.length; i++) {
					queryObject.setParameter(index+i, entrust_type[i]) ;
				}
				index+=entrust_type.length ;
			}
			
			if(entrust_status!=null){
				for (int i=0;i<entrust_status.length;i++) {
					queryObject.setParameter(index+i, entrust_status[i]) ;
				}
			}

			queryObject.setFirstResult(first_result) ;
			queryObject.setMaxResults(max_result) ;
			return queryObject.list();
		} catch (RuntimeException re) {
			log.error("find all failed", re);
			throw re;
		}
		
	}
	
	public int getFentrustHistoryCount(
			int fuid,int fvirtualCoinTypeId,int[] entrust_type,
			int entrust_status[],
			Date beginDate,Date endDate){
		
		log.debug("getFentrustHistory all Fentrust instances");
		try {
			StringBuffer queryString = new StringBuffer("select count(fid) from Fentrust where fuser.fid=? " +
					" and fvirtualcointype.fid=? and  ") ;
			
			if(beginDate!=null && endDate!=null){
				SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd") ;
				queryString.append(" fcreateTime>='"+sdf.format(beginDate)+"' and fcreateTime<='"+sdf.format(endDate)+"' and ") ;
			}
			
			if(entrust_type!=null){
				queryString.append(" ( ") ;
				for (int i=0;i<entrust_type.length;i++) {
					if(i==0){
						queryString.append(" fentrustType=? ") ;
					}else{
						queryString.append(" or fentrustType=? ") ;
					}
				}
				queryString.append(" ) and ") ;
			}
			
			if(entrust_status!=null){
				queryString.append(" ( ") ;
				for (int i=0;i<entrust_status.length;i++) {
					if(i==0){
						queryString.append(" fstatus=? ") ;
					}else{
						queryString.append(" or fstatus=? ") ;
					}
				}
				queryString.append(" ) and ") ;
			}
			
			queryString.append(" 1=1 ") ;
			
			Query queryObject = getSession().createQuery(queryString.toString());
			queryObject.setParameter(0, fuid) ;
			queryObject.setParameter(1, fvirtualCoinTypeId) ;
			
			int index = 2 ;
			if(entrust_type!=null){
				for (int i = 0; i < entrust_type.length; i++) {
					queryObject.setParameter(index+i, entrust_type[i]) ;
				}
				index+=entrust_type.length ;
			}
			
			if(entrust_status!=null){
				for (int i=0;i<entrust_status.length;i++) {
					queryObject.setParameter(index+i, entrust_status[i]) ;
				}
			}
			
			long l = (Long)queryObject.list().get(0);
			return (int) l;
		} catch (RuntimeException re) {
			log.error("find all failed", re);
			throw re;
		}
		
	}
	
	public List<Map> getTotalQty(int fentrustType, String startDate, String endDate) {
		List<Map> all = new ArrayList();
		StringBuffer sql = new StringBuffer();
		sql.append("select sum(ifnull(a.fleftCount,0)) total,b.fShortName sellName, c.fShortName buyName from fentrust a  \n");
		sql.append("left outer join market m on a.fVi_fId=m.id  \n");
		sql.append("left outer join fvirtualcointype b on m.sell_id=b.fId  \n");
		sql.append("left outer join fvirtualcointype c on m.buy_id=c.fId  \n");
		sql.append("where (a.fstatus=1 or a.fstatus=2) and fentrustType="+fentrustType+" \n");
		if(StringUtils.hasText(startDate)){
			sql.append(" and a.fcreateTime>=").append("'" + startDate + "' ");
		}
		if(StringUtils.hasText(endDate)){
			sql.append(" and a.fcreateTime<=").append("'" + endDate + "' ");
		}
		sql.append("group by b.fid  \n");
		SQLQuery queryObject = getSession().createSQLQuery(sql.toString());
		List allList = queryObject.list();
		Iterator it = allList.iterator();
		while(it.hasNext()) {
			Map map = new HashMap();
			Object[] o = (Object[]) it.next();
			map.put("total", o[0]);
			map.put("fName", o[1] + " / " + o[2]);
			all.add(map);
		}
//		if(all.size() ==0){
//			queryObject = getSession().createSQLQuery("select fname from fvirtualcointype");
//			allList = queryObject.list();
//			it = allList.iterator();
//			while(it.hasNext()) {
//				Map map = new HashMap();
//				map.put("fName",it.next());
//				all.add(map);
//			}
//		}
		return all;
	}
	
	public Map getTotalBuyFees(int fentrustType,String startDate,String endDate) {
		Map map = new HashMap();
		StringBuffer sql = new StringBuffer();
		sql.append("select sum(ifnull(a.ffees,0)-ifnull(a.fleftfees,0)) total from Fentrust a  \n");
		sql.append("left outer join fvirtualcointype b on a.fVi_fId=b.fId  \n");
		sql.append("where a.fstatus in(2,3) and fentrustType="+fentrustType+" \n");
		sql.append("and DATE_FORMAT(fcreateTime,'%Y-%m-%d') >='"+startDate+"' \n");
		sql.append("and DATE_FORMAT(fcreateTime,'%Y-%m-%d') <='"+endDate+"' \n");
		SQLQuery queryObject = getSession().createSQLQuery(sql.toString());
		List allList = queryObject.list();
		if(allList != null && allList.size() >0){
			map.put("totalAmount",allList.get(0));
		}
		return map;
	}
	
	/**
	 *
	 * @return 所有的机器人订单
	 */
	@SuppressWarnings("unchecked")
//	public List<Fentrust> findByRobotStatus(int id, int robotStatus){
//
//		String sql = "select fe from Fentrust fe,Fvirtualcointype fv where fe.fstatus != 4 and fe.fvirtualcointype.fid = fv.fid and fe.robotStatus = ? and fe.fvirtualcointype.fid = ?";
//		return (List<Fentrust>) getHibernateTemplate().find(sql, robotStatus, id);
//	}

	//查询用户所有成交记录
	public List<Fentrustlog> findAllLog(Integer fuserId, Date beginDate, Date endDate, Integer viCoinTypeId, Integer entrustType){
		StringBuilder hqlBuf = new StringBuilder("from com.trade.model.Fentrustlog where 1=1");
		if(null != fuserId){
			hqlBuf.append(" and fentrust.fuser.fid=:fuid");
		}
		if(null != viCoinTypeId){
			hqlBuf.append(" and fvirtualcointype.fid=:coinTypeId");
		}
		if(null != entrustType){
			hqlBuf.append(" and fEntrustType=:entrustType");
		}
		if(null != beginDate){
			hqlBuf.append(" and fcreateTime>=:beginDate");
		}
		if(null != endDate){
			hqlBuf.append(" and fcreateTime<=:endDate");
		}
		Query query = getSession().createQuery(hqlBuf.toString());
		if(null != fuserId){
			query.setParameter("fuid", fuserId);
		}
		if(null != viCoinTypeId){
			query.setParameter("coinTypeId", viCoinTypeId);
		}
		if(null != entrustType){
			query.setParameter("entrustType", entrustType);
		}

		if(null != beginDate){
			query.setParameter("beginDate", beginDate);
		}
		if(null != endDate){
			query.setParameter("endDate", endDate);
		}
		return query.list();
	}
	/**
	 * 统计记录数
	 * @param fuserId
	 * @param entrustType
	 * @param status
	 * @param beginDate
	 * @param endDate
	 * @return
	 */
	public int countHistory(Integer fuserId, Integer market, Integer[] entrustType, Integer[] status, Date beginDate, Date endDate){
		StringBuilder hqlBuf = new StringBuilder("select count(*) from com.trade.model.Fentrust where 1=1");

		if(null != fuserId){
			hqlBuf.append(" and fuser.fid=:fuid");
		}
		if(null != market){
			hqlBuf.append(" and market.id=:coinTypeId");
		}
		if(!ObjectUtils.isEmpty(entrustType)){
			if(1 == entrustType.length){
				hqlBuf.append(" and fentrustType=:type");
			}else if(2 == entrustType.length){
				hqlBuf.append(" and (fentrustType=:type1 or fentrustType=:type2)");
			}else{
				hqlBuf.append(" and fentrustType in(:types)");
			}
		}
		if(!ObjectUtils.isEmpty(status)){
			if(1 == status.length){
				hqlBuf.append(" and fstatus=:status");
			}else if(2 == status.length){
				hqlBuf.append(" and (fstatus=:status1 or fstatus=:status2)");
			}else{
				hqlBuf.append(" and fstatus in(:statuses)");
			}
		}
		if(null != beginDate){
			hqlBuf.append(" and fcreateTime>=:beginDate");
		}
		if(null != endDate){
			hqlBuf.append(" and fcreateTime<=:endDate");
		}

		Query query = getSession().createQuery(hqlBuf.toString());

		if(null != fuserId){
			query.setParameter("fuid", fuserId);
		}
		if(null != market){
			query.setParameter("coinTypeId", market);
		}
		if(!ObjectUtils.isEmpty(entrustType)){
			if(1 == entrustType.length){
				query.setParameter("type", entrustType[0]);
			}else if(2 == entrustType.length){
				query.setParameter("type1", entrustType[0]);
				query.setParameter("type2", entrustType[1]);
			}else{
				query.setParameterList("types", entrustType);
			}
		}
		if(!ObjectUtils.isEmpty(status)){
			if(1 == status.length){
				query.setParameter("status", status[0]);
			}else if(2 == status.length){
				query.setParameter("status1", status[0]);
				query.setParameter("status2", status[1]);
			}else{
				query.setParameterList("statuses", status);
			}
		}
		if(null != beginDate){
			query.setParameter("beginDate", beginDate);
		}
		if(null != endDate){
			query.setParameter("endDate", endDate);
		}

		return ((Long)query.uniqueResult()).intValue();
	}


//	/**
//	 * 订单历史记录
//	 *
//	 * @param fuserId
//	 * @param viCoinTypeId
//	 * @param entrustType
//	 * @param status
//	 * @param beginDate
//	 * @param endDate
//	 * @param firstResult
//	 * @param maxResult
//	 * @return
//	 */
//	@SuppressWarnings("unchecked")
//	public List<Fentrust> findHistory(Integer fuserId, Integer viCoinTypeId, Integer entrustType, Integer[] status, Date beginDate, Date endDate, int firstResult, int maxResult){
//		StringBuilder hqlBuf = new StringBuilder("from com.trade.model.Fentrust where 1=1");
//
//		if(null != fuserId){
//			hqlBuf.append(" and fuser.fid=:fuid");
//		}
//		if(null != viCoinTypeId){
//			hqlBuf.append(" and fvirtualcointype.fid=:coinTypeId");
//		}
//		if(Objects.nonNull(entrustType)){
//			hqlBuf.append(" and fentrustType=:type");
//		}
//		if(!ObjectUtils.isEmpty(status)){
//			if(1 == status.length){
//				hqlBuf.append(" and fstatus=:status");
//			}else if(2 == status.length){
//				hqlBuf.append(" and (fstatus=:status1 or fstatus=:status2)");
//			}else{
//				hqlBuf.append(" and fstatus in(:statuses)");
//			}
//		}
//		if(null != beginDate){
//			hqlBuf.append(" and fcreateTime>=:beginDate");
//		}
//		if(null != endDate){
//			hqlBuf.append(" and fcreateTime<=:endDate");
//		}
//		hqlBuf.append(" order by fid desc");
//
//		Query query = getSession().createQuery(hqlBuf.toString());
//
//		if(null != fuserId){
//			query.setParameter("fuid", fuserId);
//		}
//		if(null != viCoinTypeId){
//			query.setParameter("coinTypeId", viCoinTypeId);
//		}
//		if(Objects.nonNull(entrustType)){
//			query.setParameter("type", entrustType);
//		}
//		if(!ObjectUtils.isEmpty(status)){
//			if(1 == status.length){
//				query.setParameter("status", status[0]);
//			}else if(2 == status.length){
//				query.setParameter("status1", status[0]);
//				query.setParameter("status2", status[1]);
//			}else{
//				query.setParameterList("statuses", status);
//			}
//		}
//		if(null != beginDate){
//			query.setParameter("beginDate", beginDate);
//		}
//		if(null != endDate){
//			query.setParameter("endDate", endDate);
//		}
//		if (-1 != firstResult) {
//			query.setFirstResult(firstResult);
//		}
//		if (-1 != maxResult) {
//			query.setMaxResults(maxResult);
//		}
//
//		return query.list();
//	}



	/**
	 * 订单历史记录
	 *
	 * @param fuserId
	 * @param market
	 * @param entrustType
	 * @param status
	 * @param beginDate
	 * @param endDate
	 * @param firstResult
	 * @param maxResult
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<Fentrust> findHistory(Integer fuserId, Integer market, Integer[] entrustType, Integer[] status, Date beginDate, Date endDate, int firstResult, int maxResult){
		StringBuilder hqlBuf = new StringBuilder("from Fentrust where 1=1");

		if(null != fuserId){
			hqlBuf.append(" and fuser.fid=:fuid");
		}
		if(null != market){
			hqlBuf.append(" and market.id=:coinTypeId");
		}
		if(!ObjectUtils.isEmpty(entrustType)){
			if(1 == entrustType.length){
				hqlBuf.append(" and fentrustType=:type");
			}else if(2 == entrustType.length){
				hqlBuf.append(" and (fentrustType=:type1 or fentrustType=:type2)");
			}else{
				hqlBuf.append(" and fentrustType in(:types)");
			}
		}
		if(!ObjectUtils.isEmpty(status)){
			if(1 == status.length){
				hqlBuf.append(" and fstatus=:status");
			}else if(2 == status.length){
				hqlBuf.append(" and (fstatus=:status1 or fstatus=:status2)");
			}else{
				hqlBuf.append(" and fstatus in(:statuses)");
			}
		}
		if(!StringUtils.isEmpty(beginDate)){
			hqlBuf.append(" and fcreateTime>=:beginDate");
		}
		if(!StringUtils.isEmpty(endDate)){
			hqlBuf.append(" and fcreateTime<=:endDate");
		}
		hqlBuf.append(" order by fcreateTime desc");

		Query query = getSession().createQuery(hqlBuf.toString());

		if(null != fuserId){
			query.setParameter("fuid", fuserId);
		}
		if(null != market){
			query.setParameter("coinTypeId", market);
		}
		if(!ObjectUtils.isEmpty(entrustType)){
			if(1 == entrustType.length){
				query.setParameter("type", entrustType[0]);
			}else if(2 == entrustType.length){
				query.setParameter("type1", entrustType[0]);
				query.setParameter("type2", entrustType[1]);
			}else{
				query.setParameterList("types", entrustType);
			}
		}
		if(!ObjectUtils.isEmpty(status)){
			if(1 == status.length){
				query.setParameter("status", status[0]);
			}else if(2 == status.length){
				query.setParameter("status1", status[0]);
				query.setParameter("status2", status[1]);
			}else{
				query.setParameterList("statuses", status);
			}
		}
		if(!StringUtils.isEmpty(beginDate)){
			query.setParameter("beginDate", beginDate);
		}
		if(!StringUtils.isEmpty(endDate)){
			query.setParameter("endDate", endDate);
		}
		query.setFirstResult(firstResult);
		query.setMaxResults(maxResult);

		return query.list();
	}

	/**
	 * 订单历史记录
	 *
	 * @param fuserId
	 * @param viCoinTypeId
	 * @param entrustType
	 * @param beginDate
	 * @param endDate
	 * @param firstResult
	 * @param maxResult
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<Fentrust> findSuccessHistory(Integer fuserId, Integer viCoinTypeId, Integer entrustType, Integer status,  String beginDate, String endDate, int firstResult, int maxResult){
		StringBuilder hqlBuf = new StringBuilder("from Fentrust where 1=1");

		if(null != fuserId){
			hqlBuf.append(" and fuser.fid=:fuid");
		}
		if(null != viCoinTypeId&&viCoinTypeId!=-1){
			hqlBuf.append(" and market.id=:coinTypeId");
		}
		if(Objects.nonNull(entrustType)&&entrustType!=-1){
			hqlBuf.append(" and fentrustType=:type");
		}
		if(Objects.nonNull(status)&&status!=-1){
			hqlBuf.append(" and fstatus=:status");
		}
		if(!StringUtils.isEmpty(beginDate)){
			hqlBuf.append(" and  DATE_FORMAT(fcreateTime, '%Y-%m-%d') >=:beginDate");
		}
    	if(!StringUtils.isEmpty(endDate)){
			hqlBuf.append(" and  DATE_FORMAT(fcreateTime, '%Y-%m-%d') <=:endDate");
		}

		hqlBuf.append(" order by fid desc");

		Query query = getSession().createQuery(hqlBuf.toString());

		if(null != fuserId){
			query.setParameter("fuid", fuserId);
		}
		if(null != viCoinTypeId&&viCoinTypeId!=-1){
			query.setParameter("coinTypeId", viCoinTypeId);
		}
		if(Objects.nonNull(entrustType)&&entrustType!=-1){
			query.setParameter("type", entrustType);
		}
		if(Objects.nonNull(status)&&status!=-1){
			query.setParameter("status", status);
		}

		if(!StringUtils.isEmpty(beginDate)){
			query.setParameter("beginDate", beginDate);
		}

		if(!StringUtils.isEmpty(endDate)){
			query.setParameter("endDate", endDate);
		}
		if (-1 != firstResult) {
			query.setFirstResult(firstResult);
		}
		if (-1 != maxResult) {
			query.setMaxResults(maxResult);
		}

		return query.list();
	}

	public int countSuccessHistory(Integer fuserId, Integer viCoinTypeId, Integer entrustType, Integer status, String beginDate, String endDate){
		StringBuilder hqlBuf = new StringBuilder("select count(1) from fentrust where 1=1");
		if(null != fuserId){
			hqlBuf.append(" and FUs_fId=:fuid");
		}
		if(null != viCoinTypeId&&viCoinTypeId!=-1){
			hqlBuf.append(" and fVi_fId=:coinTypeId");
		}
		if(Objects.nonNull(entrustType)&&entrustType!=-1){
			hqlBuf.append(" and fEntrustType=:type");
		}
		if(Objects.nonNull(status)&&status!=-1){
			hqlBuf.append(" and fStatus=:status");
		}
		if(!StringUtils.isEmpty(beginDate)){
			hqlBuf.append(" and  DATE_FORMAT(fcreateTime, '%Y-%m-%d') >=:beginDate");
		}
		if(!StringUtils.isEmpty(endDate)){
			hqlBuf.append(" and  DATE_FORMAT(fcreateTime, '%Y-%m-%d') <=:endDate");
		}
		Query query = getSession().createSQLQuery(hqlBuf.toString());
		if(null != fuserId){
			query.setParameter("fuid", fuserId);
		}
		if(null != viCoinTypeId&&viCoinTypeId!=-1 ){
			query.setParameter("coinTypeId", viCoinTypeId);
		}
		if(Objects.nonNull(entrustType)&&entrustType!=-1){
			query.setParameter("type", entrustType);
		}
		if(!StringUtils.isEmpty(beginDate)){
			query.setParameter("beginDate", beginDate);
		}
		if(!StringUtils.isEmpty(endDate)){
			query.setParameter("endDate", endDate);
		}
		if(Objects.nonNull(status)&&status!=-1){
			query.setParameter("status", status);
		}
		return ((BigInteger)query.uniqueResult()).intValue();
	}

	/**
	 * 统计记录数
	 * @param fuserId
	 * @param viCoinTypeId
	 * @param entrustType
	 * @param status
	 * @param beginDate
	 * @param endDate
	 * @return
	 */
	public int CountHistory(Integer fuserId, Integer viCoinTypeId, Integer entrustType, Integer[] status, Date beginDate, Date endDate){
		StringBuilder hqlBuf = new StringBuilder("select count(*) from com.trade.model.Fentrust where 1=1");
		
		if(null != fuserId){
			hqlBuf.append(" and fuser.fid=:fuid");
		}
		if(null != viCoinTypeId){
			hqlBuf.append(" and fvirtualcointype.fid=:coinTypeId");
		}
		if(Objects.nonNull(entrustType)){
			hqlBuf.append(" and fentrustType=:type");
		}
		if(!ObjectUtils.isEmpty(status)){
			if(1 == status.length){
				hqlBuf.append(" and fstatus=:status");
			}else if(2 == status.length){
				hqlBuf.append(" and (fstatus=:status1 or fstatus=:status2)");
			}else{
				hqlBuf.append(" and fstatus in(:statuses)");
			}
		}
		if(null != beginDate){
			hqlBuf.append(" and fcreateTime>=:beginDate");
		}
		if(null != endDate){
			hqlBuf.append(" and fcreateTime<=:endDate");
		}
		
		Query query = getSession().createQuery(hqlBuf.toString());
		
		if(null != fuserId){
			query.setParameter("fuid", fuserId);
		}
		if(null != viCoinTypeId){
			query.setParameter("coinTypeId", viCoinTypeId);
		}
		if(Objects.nonNull(entrustType)){
			query.setParameter("type", entrustType);
		}
		if(!ObjectUtils.isEmpty(status)){
			if(1 == status.length){
				query.setParameter("status", status[0]);
			}else if(2 == status.length){
				query.setParameter("status1", status[0]);
				query.setParameter("status2", status[1]);
			}else{
				query.setParameterList("statuses", status);
			}
		}
		if(null != beginDate){
			query.setParameter("beginDate", beginDate);
		}
		if(null != endDate){
			query.setParameter("endDate", endDate);
		}
		
		return ((Long)query.uniqueResult()).intValue();
	}
	
	/**
	 * 导出委托列表
	 * 
	 * @param keyWord		登录名、昵称、真实姓名
	 * @param coinId		币种id
	 * @param logDate		创建日期 yyyy-MM-dd
	 * @param price			单价
	 * @param entrustType	委托类型
	 * @param status		委托状态	
	 * @param firstResult   
	 * @param maxResult
	 * @return				二维数组
	 */
	@SuppressWarnings("unchecked")
	public List<Object> findForExport(String keyWord, Integer coinId, String logDate, Double price, Short entrustType, Short status, Integer firstResult, Integer maxResult){
		StringBuilder sqlBuf = new StringBuilder();
		
		sqlBuf.append("select u.floginName, u.fNickName, u.fRealName, v.fName, e.fEntrustType, e.fStatus, e.fPrize, e.fCount, e.fleftCount, e.fAmount, e.fsuccessAmount, u.fEmail, u.fTelephone from fentrust e");
		sqlBuf.append(" left join fvirtualcointype v on v.fId=e.fVi_fId");
		sqlBuf.append(" left join fuser u on u.fid=e.FUs_fId where 1=1");
		
		if(null != coinId){
			sqlBuf.append(" and e.fVi_fId=").append(coinId);
		}
		if(StringUtils.hasText(logDate)){
			sqlBuf.append(" and DATE_FORMAT(e.fCreateTime,'%Y-%m-%d')=:logDate");
		}
		if(null != price){
			sqlBuf.append(" and e.fPrize=").append(price);
		}
		if(null != entrustType){
			sqlBuf.append(" and e.fEntrustType=").append(entrustType);
		}
		if(null != status){
			sqlBuf.append(" and e.fStatus=").append(status);
		}
		if(StringUtils.hasText(keyWord)){
			sqlBuf.append(" and (");
//			sqlBuf.append("u.floginName like :keyWord");
			sqlBuf.append("locate(:keyWord, u.floginName)>0");
//			sqlBuf.append(" or u.fNickName like :keyWord");
			sqlBuf.append(" or locate(:keyWord, u.fNickName)>0");
//			sqlBuf.append(" or u.fRealName like :keyWord)");
			sqlBuf.append(" or locate(:keyWord, u.fRealName)>0)");
		}
		// 过滤机器人挂单
		sqlBuf.append(" and e.robotStatus = 0");
		sqlBuf.append(" order by e.fCreateTime desc");
		
		Query query = getSession().createSQLQuery(sqlBuf.toString());
		
		if(StringUtils.hasText(logDate)){
			query.setParameter("logDate", logDate);
		}
		if(StringUtils.hasText(keyWord)){
//			query.setParameter("keyWord", "'%" + keyWord + "%'");
			query.setParameter("keyWord", keyWord);
		}
		if(null != firstResult){
			query.setFirstResult(firstResult);
		}
		if(null != maxResult){
			query.setMaxResults(maxResult);
		}
		
		return query.list();
	}
	
	public int updateDealMakingBuyAndSell(final Fentrust buy, final Fentrust sell){
		return getHibernateTemplate().execute(new HibernateCallback<Integer>() {

			@Override
			public Integer doInHibernate(Session session) throws HibernateException, SQLException {
				
				String sql = "update Fentrust set fsuccessAmount = ? , fleftCount = ?, fstatus = ?, flastUpdatTime = ?, fleftfees = ? where fid = ? and fstatus != ? and fleftCount > 0";
				Query queryBuy = session.createQuery(sql);
				Query querySell = session.createQuery(sql);
				
				queryBuy.setDouble(0, buy.getFsuccessAmount());
				queryBuy.setDouble(1, buy.getFleftCount());
				queryBuy.setInteger(2, buy.getFstatus());
				queryBuy.setTimestamp(3, Utils.getTimestamp());
				queryBuy.setDouble(4, 0);
				queryBuy.setInteger(5, buy.getFid());
				queryBuy.setInteger(6, EntrustStatusEnum.Cancel);
				
				querySell.setDouble(0, sell.getFsuccessAmount());
				querySell.setDouble(1, sell.getFleftCount());
				querySell.setInteger(2, sell.getFstatus());
				querySell.setTimestamp(3, Utils.getTimestamp());
				querySell.setDouble(4, sell.getFleftfees());
				querySell.setInteger(5, sell.getFid());
				querySell.setInteger(6, EntrustStatusEnum.Cancel);
				
				return queryBuy.executeUpdate() + querySell.executeUpdate();
			}
		});
	}

	/**
	 * 交易手续费报表
	 * @param startDate
	 * @param endDate
	 * @param symbol
	 * @return
	 */
	public List<String[]> listForTradeFeeReport(Date startDate, Date endDate, int symbol){
		List<String[]> result =  new ArrayList<String[]>();
		StringBuilder sqlBuilder = new StringBuilder("select DATE_FORMAT(fcreateTime, '%m-%d') as date, sum(ffees) as ffees from fentrustlog where fcreateTime > ? and fcreateTime < ?");
		if(symbol != 0){
			sqlBuilder.append("and fVi_type = " + symbol);
		}
		sqlBuilder.append(" group by DATE_FORMAT(fcreateTime, '%y-%m-%d') order by fcreateTime");
		Query query = getSession().createSQLQuery(sqlBuilder.toString());
		query.setDate(0, startDate);
		query.setDate(1, endDate);
		List list = query.list();
		list.forEach(rows -> {
			Object[] objects = (Object[]) rows;
			result.add(new String[]{String.valueOf(objects[0]), String.valueOf(objects[1])});
		});
		return result;
	}

	public Collection getOrders(int uid) {
		List<Map<String, Object>> list = jdbcTemplate.queryForList("select b.fid vid, b.fName, b.fShortName, b.fSymbol, a.ffees, a.fEntrustType, a.fPrize, a.fleftCount, a.fId, a.fCreateTime, c.fShortName as buyName from fentrust a, fvirtualcointype b, market m, fvirtualcointype c where a.FUs_fId = ? and a.fVi_fId = m.id and m.sell_id = b.fId and m.buy_id = c.fId and (a.fStatus = ? or a.fStatus = ?) ORDER by b.fid, a.fid", uid, EntrustStatusEnum.Going, EntrustStatusEnum.PartDeal);
		Map<Integer, List> listMap = new TreeMap<>();
		list.forEach(map -> {
			List vlist = listMap.get(map.get("vid"));
			if (vlist == null) {
				vlist = new ArrayList();
				listMap.put((Integer) map.get("vid"), vlist);
			}
			vlist.add(new Object[]{map.get("vid"), map.get("fName"), map.get("fShortName"), map.get("fSymbol"), map.get("fEntrustType"), map.get("fPrize"), map.get("fleftCount"), map.get("fId"), map.get("fCreateTime"), map.get("ffees"), map.get("buyName")});
		});
		return listMap.values();
	}

	public Collection getOrders(int uid, int symbol) {
		StringBuilder sql = new StringBuilder("select b.id vid, b.sell_id, b.buy_id, a.ffees, a.fEntrustType, a.fPrize, a.fleftCount, a.fId, a.fCreateTime from fentrust a, market b where a.FUs_fId = ? and a.fVi_fId = b.id and (a.fStatus = ? or a.fStatus = ?)");
		if (symbol > 0) {
			sql.append(" and b.id = " + symbol);
		}
		sql.append(" ORDER by b.id, a.fid");
		List<Map<String, Object>> list = jdbcTemplate.queryForList(sql.toString(), uid, EntrustStatusEnum.Going, EntrustStatusEnum.PartDeal);
		Map<Integer, List> listMap = new TreeMap<>();
		list.forEach(map -> {
			List vlist = listMap.get(map.get("vid"));
			if (vlist == null) {
				vlist = new ArrayList();
				listMap.put((Integer) map.get("vid"), vlist);
			}
			vlist.add(new Object[]{map.get("vid"), map.get("sell_id"), map.get("buy_id"), "", map.get("fEntrustType"), map.get("fPrize"), map.get("fleftCount"), map.get("fId"), map.get("fCreateTime"), map.get("ffees")});
		});
		return listMap.values();
	}
}

























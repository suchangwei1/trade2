package com.trade.dao;

import static org.hibernate.criterion.Example.create;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.trade.util.StringUtils;
import com.trade.dao.comm.HibernateDaoSupport;
import com.trade.model.Fvirtualcointype;
import org.hibernate.LockMode;
import org.hibernate.Query;
import org.hibernate.SQLQuery;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

import com.trade.model.Fuser;
import com.trade.model.Fvirtualaddress;

/**
 	* A data access object (DAO) providing persistence and search support for Fvirtualaddress entities.
 			* Transaction control of the save(), update() and delete() operations
		can directly support Spring container-managed transactions or they can be augmented	to handle user-managed Spring transactions.
		Each of these methods provides additional information for how to configure it for the desired type of transaction control.
	 * @see ztmp.Fvirtualaddress
  * @author MyEclipse Persistence Tools
 */

@Repository
public class FvirtualaddressDAO extends HibernateDaoSupport {
	     private static final Logger log = LoggerFactory.getLogger(FvirtualaddressDAO.class);
		//property constants
	public static final String FADDERESS = "fadderess";
	public static final String FINOUT_TYPE = "finoutType";




    public void save(Fvirtualaddress transientInstance) {
        log.debug("saving Fvirtualaddress instance");
        try {
            getSession().save(transientInstance);
            log.debug("save successful");
        } catch (RuntimeException re) {
            log.error("save failed", re);
            throw re;
        }
    }

	public void delete(Fvirtualaddress persistentInstance) {
        log.debug("deleting Fvirtualaddress instance");
        try {
            getSession().delete(persistentInstance);
            log.debug("delete successful");
        } catch (RuntimeException re) {
            log.error("delete failed", re);
            throw re;
        }
    }

    public Fvirtualaddress findById( java.lang.Integer id) {
        log.debug("getting Fvirtualaddress instance with id: " + id);
        try {
            Fvirtualaddress instance = (Fvirtualaddress) getSession()
                    .get("com.trade.model.Fvirtualaddress", id);
            return instance;
        } catch (RuntimeException re) {
            log.error("get failed", re);
            throw re;
        }
    }


    public List<Fvirtualaddress> findByExample(Fvirtualaddress instance) {
        log.debug("finding Fvirtualaddress instance by example");
        try {
            List<Fvirtualaddress> results = (List<Fvirtualaddress>) getSession()
                    .createCriteria("com.trade.model.Fvirtualaddress")
                    .add( create(instance) )
            .list();
            log.debug("find by example successful, result size: " + results.size());
            return results;
        } catch (RuntimeException re) {
            log.error("find by example failed", re);
            throw re;
        }
    }

    public List findByProperty(String propertyName, Object value) {
      log.debug("finding Fvirtualaddress instance with property: " + propertyName
            + ", value: " + value);
      try {
         String queryString = "from Fvirtualaddress as model where model."
         						+ propertyName + "= ?";
         Query queryObject = getSession().createQuery(queryString);
		 queryObject.setParameter(0, value);
		 return queryObject.list();
      } catch (RuntimeException re) {
         log.error("find by property name failed", re);
         throw re;
      }
	}

	public List<Fvirtualaddress> findByFadderess(Object fadderess
	) {
		return findByProperty(FADDERESS, fadderess
		);
	}

	public List<Fvirtualaddress> findByFinoutType(Object finoutType
	) {
		return findByProperty(FINOUT_TYPE, finoutType
		);
	}


	public List findAll() {
		log.debug("finding all Fvirtualaddress instances");
		try {
			String queryString = "from Fvirtualaddress";
	         Query queryObject = getSession().createQuery(queryString);
			 return queryObject.list();
		} catch (RuntimeException re) {
			log.error("find all failed", re);
			throw re;
		}
	}

    public Fvirtualaddress merge(Fvirtualaddress detachedInstance) {
        log.debug("merging Fvirtualaddress instance");
        try {
            Fvirtualaddress result = (Fvirtualaddress) getSession()
                    .merge(detachedInstance);
            log.debug("merge successful");
            return result;
        } catch (RuntimeException re) {
            log.error("merge failed", re);
            throw re;
        }
    }

    public void attachDirty(Fvirtualaddress instance) {
        log.debug("attaching dirty Fvirtualaddress instance");
        try {
            getSession().saveOrUpdate(instance);
            log.debug("attach successful");
        } catch (RuntimeException re) {
            log.error("attach failed", re);
            throw re;
        }
    }

    public void attachClean(Fvirtualaddress instance) {
        log.debug("attaching clean Fvirtualaddress instance");
        try {
            getSession().lock(instance, LockMode.NONE);
            log.debug("attach successful");
        } catch (RuntimeException re) {
            log.error("attach failed", re);
            throw re;
        }
    }

    public Fvirtualaddress findFvirtualaddress(Fuser fuser,Fvirtualcointype fvirtualcointype){
        return findFvirtualaddress(fuser.getFid(), fvirtualcointype.getFid());
    }

    public Fvirtualaddress findFvirtualaddress(Integer userId, Integer fviFid){
        log.debug("findFvirtualaddress all Fvirtualaddress instances");
        try {
            String queryString = "from Fvirtualaddress where fuser.fid=? and fvirtualcointype.fid=? ";
            Query queryObject = getSession().createQuery(queryString);
            queryObject.setParameter(0, userId) ;
            queryObject.setParameter(1, fviFid) ;
            List<Fvirtualaddress> fvirtualaddresses = queryObject.list();
            if(fvirtualaddresses.size()>0){
                return fvirtualaddresses.get(0) ;
            }else{
                return null ;
            }
        } catch (RuntimeException re) {
            log.error("find all failed", re);
            throw re;
        }
    }

	public List<Fvirtualaddress> findFvirtualaddress(Fvirtualcointype fvirtualcointype,String address){
		log.debug("findFvirtualaddress all Fvirtualaddress instances");
		try {
			String queryString = "from Fvirtualaddress where fadderess=? and fvirtualcointype.fid=? ";
	         Query queryObject = getSession().createQuery(queryString);
	         queryObject.setParameter(0, address) ;
	         queryObject.setParameter(1, fvirtualcointype.getFid()) ;
			 List<Fvirtualaddress> fvirtualaddresses = queryObject.list();
			 return fvirtualaddresses ;
		} catch (RuntimeException re) {
			log.error("find all failed", re);
			throw re;
		}
	}

    public List<Map<String, Object>> findUserVirtualCoinAddress(String keyword, Integer symbol, String orderField, String orderDirection, int offset, int length, boolean isPage){
        StringBuilder sqlBuf = new StringBuilder();
        sqlBuf.append("select u.floginName,u.fRealName,u.fTelephone,u.fEmail,w.fVi_fId,w.fAdderess,w.fCreateTime from fvirtualaddress w");
        sqlBuf.append(" left join fuser u on w.fuid = u.fid where 1=1");
        if(null != symbol){
            sqlBuf.append(" and w.fVi_fId=:symbol");
        }
        if(StringUtils.hasText(keyword)){
            sqlBuf.append(" and (u.floginName like :keyword or u.fRealName like :keyword or u.fEmail like :keyword or u.fTelephone like :keyword or w.fAdderess like :keyword)");
        }
        if(StringUtils.hasText(orderDirection) && StringUtils.hasText(orderDirection)){
            sqlBuf.append(" order by ").append(orderField).append(" ").append(orderDirection);
        }else{
            sqlBuf.append(" order by w.fVi_fId");
        }
        SQLQuery query = getSession().createSQLQuery(sqlBuf.toString());
        if(null != symbol){
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
            objMap.put("floginName", objArr[0]);
            objMap.put("fRealName", objArr[1]);
            objMap.put("fTelephone", objArr[2]);
            objMap.put("fEmail", objArr[3]);
            objMap.put("fVi_fId", objArr[4]);
            objMap.put("fAdderess", objArr[5]);
            objMap.put("fCreateTime", objArr[6]);
            retList.add((HashMap)objMap.clone());
        }

        return retList;
    }

    public int countUserVirtualCoinAddress(String keyword, Integer symbol){
        StringBuilder sqlBuf = new StringBuilder();
        sqlBuf.append("select count(1) from fvirtualaddress w");
        sqlBuf.append(" left join fuser u on w.fuid = u.fid where 1=1");
        if(null != symbol){
            sqlBuf.append(" and w.fVi_fId=:symbol");
        }
        if(StringUtils.hasText(keyword)){
            sqlBuf.append(" and (u.floginName like :keyword or u.fRealName like :keyword or u.fEmail like :keyword or u.fTelephone like :keyword)");
        }
        SQLQuery query = getSession().createSQLQuery(sqlBuf.toString());
        if(null != symbol){
            query.setParameter("symbol", symbol);
        }
        if(StringUtils.hasText(keyword)){
            query.setParameter("keyword", "%" + keyword + "%");
        }

        return ((BigInteger)query.uniqueResult()).intValue();
    }

    public String getAssignAddress(int symbol){
        SQLQuery query = getSession().createSQLQuery("{call getAddress(?)}");
        query.setParameter(0, symbol);
        return (String) query.uniqueResult();
    }
}























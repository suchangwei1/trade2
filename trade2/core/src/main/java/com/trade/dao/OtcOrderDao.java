package com.trade.dao;

import com.trade.dao.comm.HibernateDaoSupport;
import com.trade.model.OtcOrder;
import org.hibernate.LockMode;
import org.hibernate.Query;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class OtcOrderDao extends HibernateDaoSupport {
	private final Logger log = LoggerFactory.getLogger(getClass());

	public OtcOrder findById( Integer id) {
		log.debug("getting OtcOrder instance with id: " + id);
		try {
			OtcOrder instance = (OtcOrder) getSession()
					.get("com.trade.model.OtcOrder", id);
			return instance;
		} catch (RuntimeException re) {
			log.error("get failed", re);
			throw re;
		}
	}

	public void save(OtcOrder transientInstance) {
		log.debug("saving OtcOrder instance");
		try {
			getSession().save(transientInstance);
			log.debug("save OtcOrder successful");
		} catch (RuntimeException re) {
			log.error("save OtcOrder failed", re);
			throw re;
		}
	}

	public void delete(OtcOrder persistentInstance) {
		log.debug("deleting OtcOrder instance");
		try {
			getSession().delete(persistentInstance);
			log.debug("delete OtcOrder successful");
		} catch (RuntimeException re) {
			log.error("delete OtcOrder failed", re);
			throw re;
		}
	}

	public OtcOrder merge(OtcOrder detachedInstance) {
		log.debug("merging OtcOrder instance");
		try {
			OtcOrder result = (OtcOrder) getSession()
					.merge(detachedInstance);
			log.debug("merge successful");
			return result;
		} catch (RuntimeException re) {
			log.error("merge failed", re);
			throw re;
		}
	}

	public void attachDirty(OtcOrder instance) {
		log.debug("attaching dirty OtcOrder instance");
		try {
			getSession().saveOrUpdate(instance);
			log.debug("attach successful");
		} catch (RuntimeException re) {
			log.error("attach failed", re);
			throw re;
		}
	}

	public void attachClean(OtcOrder instance) {
		log.debug("attaching clean OtcOrder instance");
		try {
			getSession().lock(instance, LockMode.NONE);
			log.debug("attach successful");
		} catch (RuntimeException re) {
			log.error("attach failed", re);
			throw re;
		}
	}


	public List<OtcOrder> list(int firstResult, int maxResults,
							 String filter,boolean isFY) {
		List<OtcOrder> list = null;
		log.debug("finding OtcOrder instance with filter");
		try {
			String queryString = "from OtcOrder "+filter;
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

	public int count(String filter) {
		log.debug("finding OtcOrder instance with filter");
		try {
			String queryString = "select count(id) from OtcOrder "+filter;
			return (int)(long)(getSession()).createQuery(queryString).uniqueResult();
		} catch (RuntimeException re) {
			log.error("find by filter name failed", re);
			throw re;
		}
	}

}

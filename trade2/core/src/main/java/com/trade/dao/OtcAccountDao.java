package com.trade.dao;

import com.trade.dao.comm.HibernateDaoSupport;
import com.trade.model.OtcAccount;
import org.hibernate.LockMode;
import org.hibernate.Query;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class OtcAccountDao extends HibernateDaoSupport {
	private final Logger log = LoggerFactory.getLogger(getClass());

	public OtcAccount findById( java.lang.Integer id) {
		log.debug("getting OtcAccount instance with id: " + id);
		try {
			OtcAccount instance = (OtcAccount) getSession()
					.get("com.trade.model.OtcAccount", id);
			return instance;
		} catch (RuntimeException re) {
			log.error("get failed", re);
			throw re;
		}
	}

	public void save(OtcAccount transientInstance) {
		log.debug("saving OtcAccount instance");
		try {
			getSession().save(transientInstance);
			log.debug("save OtcAccount successful");
		} catch (RuntimeException re) {
			log.error("save OtcAccount failed", re);
			throw re;
		}
	}

	public void delete(OtcAccount persistentInstance) {
		log.debug("deleting OtcAccount instance");
		try {
			getSession().delete(persistentInstance);
			log.debug("delete OtcAccount successful");
		} catch (RuntimeException re) {
			log.error("delete OtcAccount failed", re);
			throw re;
		}
	}

	public OtcAccount merge(OtcAccount detachedInstance) {
		log.debug("merging OtcAccount instance");
		try {
			OtcAccount result = (OtcAccount) getSession()
					.merge(detachedInstance);
			log.debug("merge successful");
			return result;
		} catch (RuntimeException re) {
			log.error("merge failed", re);
			throw re;
		}
	}

	public void attachDirty(OtcAccount instance) {
		log.debug("attaching dirty OtcAccount instance");
		try {
			getSession().saveOrUpdate(instance);
			log.debug("attach successful");
		} catch (RuntimeException re) {
			log.error("attach failed", re);
			throw re;
		}
	}

	public void attachClean(OtcAccount instance) {
		log.debug("attaching clean OtcAccount instance");
		try {
			getSession().lock(instance, LockMode.NONE);
			log.debug("attach successful");
		} catch (RuntimeException re) {
			log.error("attach failed", re);
			throw re;
		}
	}


	public List<OtcAccount> list(int firstResult, int maxResults,
							 String filter,boolean isFY) {
		List<OtcAccount> list = null;
		log.debug("finding OtcAccount instance with filter");
		try {
			String queryString = "from OtcAccount "+filter;
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

}

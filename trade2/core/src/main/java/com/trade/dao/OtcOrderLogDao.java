package com.trade.dao;

import com.trade.dao.comm.HibernateDaoSupport;
import com.trade.model.OtcOrderLog;
import org.hibernate.LockMode;
import org.hibernate.Query;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class OtcOrderLogDao extends HibernateDaoSupport {
	private final Logger log = LoggerFactory.getLogger(getClass());

	public OtcOrderLog findById( Integer id) {
		log.debug("getting OtcOrderLog instance with id: " + id);
		try {
			OtcOrderLog instance = (OtcOrderLog) getSession()
					.get("com.trade.model.OtcOrderLog", id);
			return instance;
		} catch (RuntimeException re) {
			log.error("get failed", re);
			throw re;
		}
	}

	public void save(OtcOrderLog transientInstance) {
		log.debug("saving OtcOrderLog instance");
		try {
			getSession().save(transientInstance);
			log.debug("save OtcOrderLog successful");
		} catch (RuntimeException re) {
			log.error("save OtcOrderLog failed", re);
			throw re;
		}
	}

	public void delete(OtcOrderLog persistentInstance) {
		log.debug("deleting OtcOrderLog instance");
		try {
			getSession().delete(persistentInstance);
			log.debug("delete OtcOrderLog successful");
		} catch (RuntimeException re) {
			log.error("delete OtcOrderLog failed", re);
			throw re;
		}
	}

	public OtcOrderLog merge(OtcOrderLog detachedInstance) {
		log.debug("merging OtcOrderLog instance");
		try {
			OtcOrderLog result = (OtcOrderLog) getSession()
					.merge(detachedInstance);
			log.debug("merge successful");
			return result;
		} catch (RuntimeException re) {
			log.error("merge failed", re);
			throw re;
		}
	}

	public void attachDirty(OtcOrderLog instance) {
		log.debug("attaching dirty OtcOrderLog instance");
		try {
			getSession().saveOrUpdate(instance);
			log.debug("attach successful");
		} catch (RuntimeException re) {
			log.error("attach failed", re);
			throw re;
		}
	}

	public void attachClean(OtcOrderLog instance) {
		log.debug("attaching clean OtcOrderLog instance");
		try {
			getSession().lock(instance, LockMode.NONE);
			log.debug("attach successful");
		} catch (RuntimeException re) {
			log.error("attach failed", re);
			throw re;
		}
	}


	public List<OtcOrderLog> list(int firstResult, int maxResults,
							 String filter,boolean isFY) {
		List<OtcOrderLog> list = null;
		log.debug("finding OtcOrderLog instance with filter");
		try {
			String queryString = "from OtcOrderLog "+filter;
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
		log.debug("finding OtcOrderLog instance with filter");
		try {
			String queryString = "select count(id) from OtcOrderLog "+filter;
			return (int)(long)(getSession()).createQuery(queryString).uniqueResult();
		} catch (RuntimeException re) {
			log.error("find by filter name failed", re);
			throw re;
		}
	}

}

package com.trade.dao;

import com.trade.dao.comm.HibernateDaoSupport;
import com.trade.model.CcLog;
import org.hibernate.LockMode;
import org.hibernate.Query;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class CcLogDAO extends HibernateDaoSupport {

	private static final Logger log = LoggerFactory.getLogger(CcLogDAO.class);

	public CcLog findById( Integer id) {
		log.debug("getting CcLog instance with id: " + id);
		try {
			CcLog instance = (CcLog) getSession()
					.get("com.trade.model.CcLog", id);
			return instance;
		} catch (RuntimeException re) {
			log.error("get failed", re);
			throw re;
		}
	}
	
    public void save(CcLog transientInstance) {
        log.debug("saving CcLog instance");
        try {
            getSession().save(transientInstance);
            log.debug("save CcLog successful");
        } catch (RuntimeException re) {
            log.error("save CcLog failed", re);
            throw re;
        }
    }
    
	public void delete(CcLog persistentInstance) {
        log.debug("deleting CcLog instance");
        try {
            getSession().delete(persistentInstance);
            log.debug("delete CcLog successful");
        } catch (RuntimeException re) {
            log.error("delete CcLog failed", re);
            throw re;
        }
    }

    public CcLog merge(CcLog detachedInstance) {
        log.debug("merging CcLog instance");
        try {
            CcLog result = (CcLog) getSession()
                    .merge(detachedInstance);
            log.debug("merge successful");
            return result;
        } catch (RuntimeException re) {
            log.error("merge failed", re);
            throw re;
        }
    }

    public void attachDirty(CcLog instance) {
        log.debug("attaching dirty CcLog instance");
        try {
            getSession().saveOrUpdate(instance);
            log.debug("attach successful");
        } catch (RuntimeException re) {
            log.error("attach failed", re);
            throw re;
        }
    }
    
    public void attachClean(CcLog instance) {
        log.debug("attaching clean CcLog instance");
        try {
            getSession().lock(instance, LockMode.NONE);
            log.debug("attach successful");
        } catch (RuntimeException re) {
            log.error("attach failed", re);
            throw re;
        }
    }
    

	public List<CcLog> list(int firstResult, int maxResults,
			String filter,boolean isFY) {
		List<CcLog> list = null;
		log.debug("finding CcLog instance with filter");
		try {
			String queryString = "from CcLog "+filter;
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
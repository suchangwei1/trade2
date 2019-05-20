package com.trade.dao;

import com.trade.dao.comm.HibernateDaoSupport;
import com.trade.model.CcUser;
import org.hibernate.LockMode;
import org.hibernate.Query;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;

@Repository
public class CcUserDAO extends HibernateDaoSupport {
	
	private static final Logger log = LoggerFactory.getLogger(CcUserDAO.class);

	public CcUser findById( java.lang.Integer id) {
		log.debug("getting CcUser instance with id: " + id);
		try {
			CcUser instance = (CcUser) getSession()
					.get("com.trade.model.CcUser", id);
			return instance;
		} catch (RuntimeException re) {
			log.error("get failed", re);
			throw re;
		}
	}
    public CcUser findByPhone( String phone) {
        log.debug("getting CcUser instance with id: " + phone);
        try {
            List<CcUser> CcUserList=new ArrayList<CcUser>();

            String queryString = "from CcUser where contactWay=? ";
            Query queryObject = getSession().createQuery(queryString);
            queryObject.setCacheable(true);
            queryObject.setParameter(0, phone);
            CcUserList = queryObject.list();
            if(CcUserList!=null&&CcUserList.size()!=0){
                return CcUserList.get(0);
            }
        } catch (RuntimeException re) {
            log.error("get failed", re);
            throw re;
        }
           return null;
    }
    public void save(CcUser transientInstance) {
        log.debug("saving CcUser instance");
        try {
            getSession().save(transientInstance);
            log.debug("save CcUser successful");
        } catch (RuntimeException re) {
            log.error("save CcUser failed", re);
            throw re;
        }
    }
    
	public void delete(CcUser persistentInstance) {
        log.debug("deleting CcUser instance");
        try {
            getSession().delete(persistentInstance);
            log.debug("delete CcUser successful");
        } catch (RuntimeException re) {
            log.error("delete CcUser failed", re);
            throw re;
        }
    }

    public CcUser merge(CcUser detachedInstance) {
        log.debug("merging CcUser instance");
        try {
            CcUser result = (CcUser) getSession()
                    .merge(detachedInstance);
            log.debug("merge successful");
            return result;
        } catch (RuntimeException re) {
            log.error("merge failed", re);
            throw re;
        }
    }

    public void attachDirty(CcUser instance) {
        log.debug("attaching dirty CcUser instance");
        try {
            getSession().saveOrUpdate(instance);
            log.debug("attach successful");
        } catch (RuntimeException re) {
            log.error("attach failed", re);
            throw re;
        }
    }
    
    public void attachClean(CcUser instance) {
        log.debug("attaching clean CcUser instance");
        try {
            getSession().lock(instance, LockMode.NONE);
            log.debug("attach successful");
        } catch (RuntimeException re) {
            log.error("attach failed", re);
            throw re;
        }
    }
    

	public List<CcUser> list(int firstResult, int maxResults,
			String filter,boolean isFY) {
		List<CcUser> list = null;
		log.debug("finding CcUser instance with filter");
		try {
			String queryString = "from CcUser "+filter;
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
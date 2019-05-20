package com.trade.dao;

import com.trade.dao.comm.HibernateDaoSupport;
import com.trade.model.SpreadLog;
import org.hibernate.LockMode;
import org.hibernate.Query;
import org.hibernate.transform.Transformers;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;
import java.util.Objects;

@Repository
public class SpreadLogDao extends HibernateDaoSupport {
	private final Logger log = LoggerFactory.getLogger(getClass());
	
	public void save(SpreadLog transientInstance) {
		log.debug("saving SpreadLog instance");
		try {
			getSession().save(transientInstance);
			log.debug("save SpreadLog successful");
		} catch (RuntimeException re) {
			log.error("save SpreadLog failed", re);
			throw re;
		}
	}

	public List<SpreadLog> findByProperties(Map<String, Object> propValMap, Integer offset, Integer length) {
		StringBuilder hqlBuf = new StringBuilder("from com.trade.model.SpreadLog as model where 1=1 ");
		for(String key : propValMap.keySet()) {
			hqlBuf.append("and model.").append(key).append("=? ");
		}
		hqlBuf.append("order by model.id desc");

		Query query = getSession().createQuery(hqlBuf.toString());

		Object[] vals = propValMap.values().toArray();
		for(int i=0; i<vals.length; i++) {
			query.setParameter(i, vals[i]);
		}
		if(Objects.nonNull(offset) && Objects.nonNull(length)) {
			query.setFirstResult(offset);
			query.setMaxResults(length);
		} else if (Objects.nonNull(length)) {
			query.setMaxResults(length);
		}

		return query.list();
	}

	public void delete(SpreadLog persistentInstance) {
		log.debug("deleting SpreadLog instance");
		try {
			getSession().delete(persistentInstance);
			log.debug("delete SpreadLog successful");
		} catch (RuntimeException re) {
			log.error("delete SpreadLog failed", re);
			throw re;
		}
	}

	public SpreadLog merge(SpreadLog detachedInstance) {
		log.debug("merging SpreadLog instance");
		try {
			SpreadLog result = (SpreadLog) getSession()
					.merge(detachedInstance);
			log.debug("merge successful");
			return result;
		} catch (RuntimeException re) {
			log.error("merge failed", re);
			throw re;
		}
	}

	public void attachDirty(SpreadLog instance) {
		log.debug("attaching dirty SpreadLog instance");
		try {
			getSession().saveOrUpdate(instance);
			log.debug("attach successful");
		} catch (RuntimeException re) {
			log.error("attach failed", re);
			throw re;
		}
	}

	public void attachClean(SpreadLog instance) {
		log.debug("attaching clean SpreadLog instance");
		try {
			getSession().lock(instance, LockMode.NONE);
			log.debug("attach successful");
		} catch (RuntimeException re) {
			log.error("attach failed", re);
			throw re;
		}
	}


	public List<SpreadLog> list(int firstResult, int maxResults,
							 String filter,boolean isFY) {
		List<SpreadLog> list = null;
		log.debug("finding SpreadLog instance with filter");
		try {
			String queryString = "from SpreadLog "+filter;
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

	public List<Map<String, String>> listSum(int id) {
		log.debug("finding SpreadLog instance with filter");
		try {
			String queryString = "select sum(parent_reward_num) as num , parent_reward_coin as coin from spread_log where parent_id = "+id+" GROUP BY parent_reward_coin";
			Query queryObject = getSession().createSQLQuery(queryString);
			return queryObject.setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP).list();
		} catch (RuntimeException re) {
			log.error("find by filter name failed", re);
			throw re;
		}
	}

	public int count(String filter) {
		log.debug("finding SpreadLog instance with filter");
		try {
			String queryString = "select count(id) from SpreadLog "+filter;
			return (int)(long)(getSession()).createQuery(queryString).uniqueResult();
		} catch (RuntimeException re) {
			log.error("find by filter name failed", re);
			throw re;
		}
	}

}

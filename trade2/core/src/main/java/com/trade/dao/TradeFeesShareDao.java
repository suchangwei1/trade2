package com.trade.dao;

import com.trade.dao.comm.HibernateDaoSupport;
import com.trade.model.TradeFeesShare;
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
public class TradeFeesShareDao extends HibernateDaoSupport {
	private final Logger log = LoggerFactory.getLogger(getClass());
	
	public void save(TradeFeesShare transientInstance) {
		log.debug("saving TradeFeesShare instance");
		try {
			getSession().save(transientInstance);
			log.debug("save TradeFeesShare successful");
		} catch (RuntimeException re) {
			log.error("save TradeFeesShare failed", re);
			throw re;
		}
	}

	public List<TradeFeesShare> findByProperties(Map<String, Object> propValMap, Integer offset, Integer length) {
		StringBuilder hqlBuf = new StringBuilder("from com.trade.model.TradeFeesShare as model where 1=1 ");
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

	public void delete(TradeFeesShare persistentInstance) {
		log.debug("deleting TradeFeesShare instance");
		try {
			getSession().delete(persistentInstance);
			log.debug("delete TradeFeesShare successful");
		} catch (RuntimeException re) {
			log.error("delete TradeFeesShare failed", re);
			throw re;
		}
	}

	public TradeFeesShare merge(TradeFeesShare detachedInstance) {
		log.debug("merging TradeFeesShare instance");
		try {
			TradeFeesShare result = (TradeFeesShare) getSession()
					.merge(detachedInstance);
			log.debug("merge successful");
			return result;
		} catch (RuntimeException re) {
			log.error("merge failed", re);
			throw re;
		}
	}

	public void attachDirty(TradeFeesShare instance) {
		log.debug("attaching dirty TradeFeesShare instance");
		try {
			getSession().saveOrUpdate(instance);
			log.debug("attach successful");
		} catch (RuntimeException re) {
			log.error("attach failed", re);
			throw re;
		}
	}

	public void attachClean(TradeFeesShare instance) {
		log.debug("attaching clean TradeFeesShare instance");
		try {
			getSession().lock(instance, LockMode.NONE);
			log.debug("attach successful");
		} catch (RuntimeException re) {
			log.error("attach failed", re);
			throw re;
		}
	}


	public List<TradeFeesShare> list(int firstResult, int maxResults,
							 String filter,boolean isFY) {
		List<TradeFeesShare> list = null;
		log.debug("finding TradeFeesShare instance with filter");
		try {
			String queryString = "from TradeFeesShare "+filter;
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

	public List<Map<String, Object>> listSum(int id) {
		log.debug("finding SpreadLog instance with filter");
		try {
			String queryString = "select sum(amount) as num , coin from trade_fees_share where parent_id = "+id+" GROUP BY coin";
			Query queryObject = getSession().createSQLQuery(queryString);
			return queryObject.setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP).list();
		} catch (RuntimeException re) {
			log.error("find by filter name failed", re);
			throw re;
		}
	}

	public int count(String filter) {
		log.debug("finding TradeFeesShare instance with filter");
		try {
			String queryString = "select count(id) from TradeFeesShare "+filter;
			return (int)(long)(getSession()).createQuery(queryString).uniqueResult();
		} catch (RuntimeException re) {
			log.error("find by filter name failed", re);
			throw re;
		}
	}

}

package com.trade.dao;

import com.trade.Enum.TradeApiType;
import com.trade.Enum.UseStatus;
import com.trade.dao.comm.HibernateDaoSupport;
import com.trade.model.TradeApi;
import com.trade.util.CollectionUtils;
import com.trade.util.StringUtils;
import org.hibernate.LockMode;
import org.hibernate.Query;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository
public class TradeApiDao extends HibernateDaoSupport {
    private static final Logger log = LoggerFactory.getLogger(TradeApiDao.class);

    public void save(TradeApi transientInstance) {
        log.debug("saving TradeApi instance");
        try {
            getSession().save(transientInstance);
            log.debug("save successful");
        } catch (RuntimeException re) {
            log.error("save failed", re);
            throw re;
        }
    }

    public TradeApi findById(int id){
        Query query = getSession().createQuery("from com.trade.model.TradeApi where id = " + id);
        List list = query.list();
        if(CollectionUtils.isEmpty(list)){
            return null;
        }
        return (TradeApi) list.get(0);
    }

    public TradeApi findByKey(String key){
        Query queryObject = getSession().createQuery("from com.trade.model.TradeApi as model where model.apiKey =:key");
        queryObject.setParameter("key", key);
        List list = queryObject.list();
        if(CollectionUtils.isEmpty(list)){
            return null;
        }
        return (TradeApi) list.get(0);
    }

    public void update(TradeApi transientInstance){
        getSession().update(transientInstance);
    }

    public void delete(TradeApi persistentInstance) {
        log.debug("deleting TradeApi instance");
        try {
            getSession().delete(persistentInstance);
            log.debug("delete successful");
        } catch (RuntimeException re) {
            log.error("delete failed", re);
            throw re;
        }
    }

    public TradeApi merge(TradeApi detachedInstance) {
        log.debug("merging TradeApi instance");
        try {
            TradeApi result = (TradeApi) getSession()
                    .merge(detachedInstance);
            log.debug("merge successful");
            return result;
        } catch (RuntimeException re) {
            log.error("merge failed", re);
            throw re;
        }
    }

    public void attachDirty(TradeApi instance) {
        log.debug("attaching dirty TradeApi instance");
        try {
            getSession().saveOrUpdate(instance);
            log.debug("attach successful");
        } catch (RuntimeException re) {
            log.error("attach failed", re);
            throw re;
        }
    }

    public void attachClean(TradeApi instance) {
        log.debug("attaching clean TradeApi instance");
        try {
            getSession().lock(instance, LockMode.NONE);
            log.debug("attach successful");
        } catch (RuntimeException re) {
            log.error("attach failed", re);
            throw re;
        }
    }

    public List<TradeApi> listByUser(int userId){
        Query queryObject = getSession().createQuery("from com.trade.model.TradeApi where userId =:userId order by id desc");
        queryObject.setInteger("userId", userId);
        queryObject.setMaxResults(50);
        return queryObject.list();
    }

    public List<TradeApi> list(int firstResult, int maxResults, String filter, boolean isFY) {
        List<TradeApi> list = null;
        log.debug("finding TradeApi instance with filter");
        try {
            String queryString = "from TradeApi " + filter;
            Query queryObject = getSession().createQuery(queryString);
            if (isFY) {
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

    public int countByUser(int userId){
        Query queryObject = getSession().createQuery("select count(model.id) from com.trade.model.TradeApi as model where model.userId =:userId");
        queryObject.setParameter("userId", userId);
        return ((Long)queryObject.uniqueResult()).intValue();
    }

    public int countByUserAndType(int userId, TradeApiType type){
        Query queryObject = getSession().createQuery("select count(model.id) from com.trade.model.TradeApi as model where model.userId =:userId and model.type =:type");
        queryObject.setParameter("userId", userId);
        queryObject.setParameter("type", type);
        return ((Long)queryObject.uniqueResult()).intValue();
    }

    public List<Map<String, Object>> list(String keyword, TradeApiType type, UseStatus status, String order, String desc, int firstResult, int maxResults){
        StringBuilder sqlBuf = new StringBuilder("select a.id, u.floginName, u.frealName, a.name, a.type, a.api_key, a.secret, a.status, a.create_time, a.update_time from ftrade_api as a left join fuser as u on u.fid = a.user_id where 1=1");
        if(StringUtils.hasText(keyword)){
            sqlBuf.append(" and (u.floginName like :keyword or u.frealName like :keyword)");
        }
        if(null != type){
            sqlBuf.append(" and a.type =:type");
        }
        if(null != status){
            sqlBuf.append(" and a.status =:status");
        }
        if(StringUtils.hasText(order) && StringUtils.hasText(desc)){
            sqlBuf.append(" order by ").append(order).append(" ").append(desc);
        }
        Query query = getSession().createSQLQuery(sqlBuf.toString());
        if(StringUtils.hasText(keyword)){
            query.setParameter("keyword", "%" + keyword + "%");
        }
        if(null != type){
            query.setParameter("type", type.getIndex());
        }
        if(null != status){
            query.setParameter("status", status.getIndex());
        }
        query.setFirstResult(firstResult);
        query.setMaxResults(maxResults);

        List list = query.list();
        List<Map<String, Object>> retList = new ArrayList<>();
        for(Object arrObj : list){
            Object[] arr = (Object[]) arrObj;
            Map<String, Object> map = new HashMap(15);
            map.put("id", arr[0]);
            map.put("floginName", arr[1]);
            map.put("frealName", arr[2]);
            map.put("name", arr[3]);
            map.put("type", TradeApiType.get((byte) arr[4]));
            map.put("apiKey", arr[5]);
            map.put("secret", arr[6]);
            map.put("status", UseStatus.get((byte)arr[7]));
            map.put("createTime", arr[8]);
            map.put("updateTime", arr[9]);
            retList.add(map);
        }

        return retList;
    }
    public int count(String keyword, TradeApiType type, UseStatus status) {
        StringBuilder sqlBuf = new StringBuilder("select count(*) from ftrade_api as a left join fuser as u on u.fid = a.user_id where 1=1");
        if (StringUtils.hasText(keyword)) {
            sqlBuf.append(" and (u.floginName like :keyword or u.frealName like :keyword)");
        }
        if (null != type) {
            sqlBuf.append(" and a.type =:type");
        }
        if (null != status) {
            sqlBuf.append(" and a.status =:status");
        }

        Query query = getSession().createSQLQuery(sqlBuf.toString());
        if (StringUtils.hasText(keyword)) {
            query.setParameter("keyword", "%" + keyword + "%");
        }
        if (null != type) {
            query.setParameter("type", type.getIndex());
        }
        if (null != status) {
            query.setParameter("status", status.getIndex());
        }

        return ((BigInteger)query.uniqueResult()).intValue();
    }

}

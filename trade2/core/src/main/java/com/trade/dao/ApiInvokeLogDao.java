package com.trade.dao;

import com.trade.api.APIResultCode;
import com.trade.dao.comm.HibernateDaoSupport;
import com.trade.model.ApiInvokeLog;
import com.trade.util.StringUtils;
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
public class ApiInvokeLogDao extends HibernateDaoSupport {
    private static Logger logger = LoggerFactory.getLogger(ApiInvokeLogDao.class);

    public void save(ApiInvokeLog entity){
        getSession().save(entity);
    }

    public ApiInvokeLog get(int id){
        return (ApiInvokeLog) getSession().get(ApiInvokeLog.class, id);
    }

    public List<ApiInvokeLog> findByUser(int userId, int firstResult, int maxResult){
        Query query = getSession().createQuery("from com.trade.model.ApiInvokeLog where userId = " + userId + " order by id desc");

        query.setFirstResult(firstResult);
        query.setMaxResults(maxResult);

        return query.list();
    }

    public List<ApiInvokeLog> findByApi(int apiId, int firstResult, int maxResult){
        Query query = getSession().createQuery("from com.trade.model.ApiInvokeLog where apiId = " + apiId + " order by id desc");

        query.setFirstResult(firstResult);
        query.setMaxResults(maxResult);

        return query.list();
    }

    public List<Map<String, Object>> list(String keyword, Integer isSucceed, int firstResult, int maxResult) {
        StringBuilder sqlBuf = new StringBuilder("select l.id, u.floginName, u.frealName, l.api_id, l.ip, l.url, l.params, l.result_code, l.is_succeed, l.create_time from fapi_invoke_log l left join fuser u on u.fid = l.user_id where 1=1");

        if(StringUtils.hasText(keyword)){
            sqlBuf.append(" and (u.floginName like :keyword or u.frealName like :keyword)");
        }
        if(null != isSucceed){
            sqlBuf.append(" and l.is_succeed =:isSucceed");
        }
        sqlBuf.append(" order by l.id desc");

        Query query = getSession().createSQLQuery(sqlBuf.toString());

        if(StringUtils.hasText(keyword)){
            query.setParameter("keyword", "%" + keyword + "%");
        }
        if(null != isSucceed){
            query.setParameter("isSucceed", isSucceed);
        }
        query.setFirstResult(firstResult);
        query.setMaxResults(maxResult);

        List<Object[]> objArr = query.list();
        List<Map<String, Object>> retList = new ArrayList<>(objArr.size());
        for(Object[] obj : objArr){
            Map<String, Object> map = new HashMap<>();
            map.put("id", obj[0]);
            map.put("floginName", obj[1]);
            map.put("frealName", obj[2]);
            map.put("apiId", obj[3]);
            map.put("ip", obj[4]);
            map.put("apiUrl", obj[5]);
            map.put("params", obj[6]);
            map.put("code", null != obj[7] ? APIResultCode.get(obj[7].toString()) : null);
            map.put("isSucceed", obj[8]);
            map.put("createTime", obj[9]);
            retList.add(map);
        }

        return retList;
    }

    public int count (String keyword, Integer isSucceed){
        StringBuilder sqlBuf = new StringBuilder("select count(l.id) from fapi_invoke_log l left join fuser u on u.fid = l.user_id where 1=1");

        if(StringUtils.hasText(keyword)){
            sqlBuf.append(" and (u.floginName like :keyword or u.frealName like :keyword)");
        }
        if(null != isSucceed){
            sqlBuf.append(" and l.is_succeed =:isSucceed");
        }

        Query query = getSession().createSQLQuery(sqlBuf.toString());

        if(StringUtils.hasText(keyword)){
            query.setParameter("keyword", "%" + keyword + "%");
        }
        if(null != isSucceed){
            query.setParameter("isSucceed", isSucceed);
        }

        return ((BigInteger)query.uniqueResult()).intValue();
    }
}

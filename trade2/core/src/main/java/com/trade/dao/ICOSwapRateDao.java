package com.trade.dao;

import com.trade.model.ICOSwapRate;
import com.trade.util.Utils;
import com.trade.dao.comm.HibernateUserDaoSupport;
import org.hibernate.Query;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.math.BigInteger;
import java.util.*;

/**
 * Author:xuelin
 * Company:招股金服
 * Date:2017/6/22
 * Desc:
 */
@Repository
public class ICOSwapRateDao extends HibernateUserDaoSupport {
    private Logger logger = LoggerFactory.getLogger(ICOSwapRateDao.class);

    public void save(ICOSwapRate entity) {
        getSession().save(entity);
        logger.info("insert ico swap rate success");
    }

    public void update(ICOSwapRate entity) {
        getSession().update(entity);
        logger.info("update ico swap rate success");
    }

    public void delete(ICOSwapRate entity) {
        getSession().delete(entity);
        logger.info("delete ico swap rate success");
    }

    public ICOSwapRate findById(int id) {
        return (ICOSwapRate) getSession().get(ICOSwapRate.class, id);
    }

    public List<ICOSwapRate> findCurrent(Integer icoId, Integer coinType, Integer length) {
        StringBuilder hqlBuf = new StringBuilder("from com.trade.model.ICOSwapRate as model where 1=1 ");
        if(Objects.nonNull(icoId)) {
            hqlBuf.append("and model.icoId = :icoId ");
        }
        if(Objects.nonNull(coinType)) {
            hqlBuf.append("and model.coinType = :coinType ");
        }
        hqlBuf.append(" group by model.coinType order by model.coinType ");

        Query query = getSession().createQuery(hqlBuf.toString());

        if(Objects.nonNull(icoId)) {
            query.setParameter("icoId", icoId);
        }
        if(Objects.nonNull(coinType)) {
            query.setParameter("coinType", coinType);
        }
        if (Objects.nonNull(length)) {
            query.setMaxResults(length);
        }

        return query.list();
    }

    public List<Map> find(String keyword, Integer icoId, Integer coinType, Integer offset, Integer length) {
        StringBuilder sqlBuf = new StringBuilder("select sr.id, sr.ico_id, sr.coin_type, sr.amount, sr.start_time, sr.end_time, sr.create_time, sr.update_time, i.name from ico_swap_rate sr left join ico i on i.id = sr.ico_id where 1=1 ");
        if(StringUtils.hasText(keyword)) {
            sqlBuf.append("and i.name like :keyword ");
        }
        if(Objects.nonNull(icoId)) {
            sqlBuf.append("and sr.ico_id = :icoId ");
        }
        if(Objects.nonNull(coinType)) {
            sqlBuf.append("and sr.coin_type = :coinType ");
        }
        sqlBuf.append("order by sr.ico_id desc");

        Query query = getSession().createSQLQuery(sqlBuf.toString());

        if(StringUtils.hasText(keyword)) {
            query.setParameter("keyword", "%" + keyword + "%");
        }
        if(Objects.nonNull(icoId)) {
            query.setParameter("icoId", icoId);
        }
        if(Objects.nonNull(coinType)) {
            query.setParameter("coinType", coinType);
        }
        if(Objects.nonNull(offset) && Objects.nonNull(length)) {
            query.setFirstResult(offset);
            query.setMaxResults(length);
        } else if (Objects.nonNull(length)) {
            query.setMaxResults(length);
        }

        List list = query.list();
        List<Map> resultList = new ArrayList<>(list.size());
        HashMap map = null;
        for(Object obj : list) {
            if(CollectionUtils.isEmpty(map)) {
                map = new HashMap();
            } else {
                map = (HashMap) map.clone();
            }

            Object[] objArr = (Object[]) obj;
            map.put("id", objArr[0]);
            map.put("icoId", objArr[1]);
            map.put("coinType", objArr[2]);
            map.put("amount", objArr[3]);
            map.put("startTime", objArr[4]);
            map.put("endTime", objArr[5]);
            map.put("createTime", objArr[6]);
            map.put("updateTime", objArr[7]);
            map.put("icoName", objArr[8]);
            resultList.add(map);
        }

        return resultList;
    }

    public int count(String keyword, Integer icoId, Integer coinType) {
        StringBuilder sqlBuf = new StringBuilder("select count(sr.id) from ico_swap_rate sr left join ico i on i.id = sr.ico_id where 1=1 ");
        if(StringUtils.hasText(keyword)) {
            sqlBuf.append("and i.name like :keyword ");
        }
        if(Objects.nonNull(icoId)) {
            sqlBuf.append("and sr.ico_id = :icoId ");
        }
        if(Objects.nonNull(coinType)) {
            sqlBuf.append("and sr.coin_type = :coinType ");
        }

        Query query = getSession().createSQLQuery(sqlBuf.toString());

        if(StringUtils.hasText(keyword)) {
            query.setParameter("keyword", "%" + keyword + "%");
        }
        if(Objects.nonNull(icoId)) {
            query.setParameter("icoId", icoId);
        }
        if(Objects.nonNull(coinType)) {
            query.setParameter("coinType", coinType);
        }

        return ((BigInteger)query.uniqueResult()).intValue();
    }

    public List<ICOSwapRate> findSupportCoin(int icoId) {
        StringBuilder hqlBuf = new StringBuilder("select new com.trade.model.ICOSwapRate(coinType) from com.trade.model.ICOSwapRate model where icoId = :icoId group by model.coinType order by model.coinType");
        Query query = getSession().createQuery(hqlBuf.toString());
        query.setParameter("icoId", icoId);
        return query.list();
    }

}

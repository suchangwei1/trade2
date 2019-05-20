package com.trade.dao;

import com.trade.model.ICO;
import com.trade.util.StringUtils;
import com.trade.dao.comm.HibernateUserDaoSupport;
import org.hibernate.Query;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;
import java.util.Objects;

/**
 * Author:xuelin
 * Company:招股金服
 * Date:2017/6/21
 * Desc:
 */
@Repository
public class ICODao extends HibernateUserDaoSupport {
    private Logger logger = LoggerFactory.getLogger(ICODao.class);

    public void save(ICO entity) {
        getSession().save(entity);
        logger.info("insert ico success");
    }

    public void update(ICO entity) {
        getSession().update(entity);
        logger.info("insert ico success");
    }

    public ICO findById(int id) {
        return (ICO) getSession().get(ICO.class, id);
    }

    public List<ICO> find(String keyword, Integer id, Date startTime, Date endTime, Integer offset, Integer length) {
        StringBuilder hqlBuf = new StringBuilder("select new com.trade.model.ICO(id, name, amount, supplyAmount, rightAmount, limitAmount, minBuyAmount, requiteRate, supportCount, imageUrl, declaration, status, jsonExt, createTime, updateTime, startTime, endTime, version) from com.trade.model.ICO as model where model.delete=false ");
        if(StringUtils.hasText(keyword)) {
            hqlBuf.append("and (model.name like :keyword or model.declaration like :keyword) ");
        }
        if(Objects.nonNull(id)) {
            hqlBuf.append("and model.id = :id ");
        }
        if(Objects.nonNull(startTime)) {
            hqlBuf.append("and model.startTime >= :startTime ");
        }
        if(Objects.nonNull(endTime)) {
            hqlBuf.append("and model.endTime < :endTime ");
        }
        hqlBuf.append("order by model.id desc");

        Query query = getSession().createQuery(hqlBuf.toString());

        if(StringUtils.hasText(keyword)) {
            query.setParameter("keyword", "%" + keyword + "%");
        }
        if(Objects.nonNull(id)) {
            query.setParameter("id", id);
        }
        if(Objects.nonNull(startTime)) {
            query.setParameter("startTime", startTime);
        }
        if(Objects.nonNull(endTime)) {
            query.setParameter("endTime", endTime);
        }
        if(Objects.nonNull(offset) && Objects.nonNull(length)) {
            query.setFirstResult(offset);
            query.setMaxResults(length);
        } else if(Objects.nonNull(length)) {
            query.setMaxResults(length);
        }

        return query.list();
    }

    public int count(String keyword, Integer id, Date startTime, Date endTime) {
        StringBuilder hqlBuf = new StringBuilder("select count(id) from com.trade.model.ICO as model where model.delete=false ");
        if(StringUtils.hasText(keyword)) {
            hqlBuf.append("and (model.name like :keyword or model.declaration like :keyword) ");
        }
        if(Objects.nonNull(id)) {
            hqlBuf.append("and model.id = :id ");
        }
        if(Objects.nonNull(startTime)) {
            hqlBuf.append("and model.startTime >= :startTime ");
        }
        if(Objects.nonNull(endTime)) {
            hqlBuf.append("and model.endTime < :endTime ");
        }

        Query query = getSession().createQuery(hqlBuf.toString());

        if(StringUtils.hasText(keyword)) {
            query.setParameter("keyword", "%" + keyword + "%");
        }
        if(Objects.nonNull(id)) {
            query.setParameter("id", id);
        }
        if(Objects.nonNull(startTime)) {
            query.setParameter("startTime", startTime);
        }
        if(Objects.nonNull(endTime)) {
            query.setParameter("endTime", endTime);
        }

        return ((Long)query.uniqueResult()).intValue();
    }

}

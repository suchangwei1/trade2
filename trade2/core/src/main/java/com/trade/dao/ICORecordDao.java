package com.trade.dao;

import com.trade.Enum.ICORecordStatusEnum;
import com.trade.model.ICORecord;
import com.trade.util.CollectionUtils;
import com.trade.util.StringUtils;
import com.trade.dao.comm.HibernateUserDaoSupport;
import org.hibernate.Query;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

import java.math.BigInteger;
import java.util.*;

/**
 * Author:xuelin
 * Company:招股金服
 * Date:2017/6/21
 * Desc:
 */
@Repository
public class ICORecordDao extends HibernateUserDaoSupport {
    private Logger logger = LoggerFactory.getLogger(ICORecordDao.class);

    public void save(ICORecord entity) {
        getSession().save(entity);
        logger.info("insert ico record success");
    }

    public void update(ICORecord entity) {
        getSession().update(entity);
        logger.info("update ico record success");
    }

    public ICORecord findById(int id) {
        return (ICORecord) getSession().get(ICORecord.class, id);
    }

    public double sumAmount(Map<String, Object> propValMap) {
        StringBuilder hqlBuf = new StringBuilder("select sum(model.amount) from com.trade.model.ICORecord as model where model.delete = false ");
        if(!CollectionUtils.isEmpty(propValMap)) {
            for(String key : propValMap.keySet()) {
                hqlBuf.append("and model.").append(key).append("=? ");
            }
        }

        Query query = getSession().createQuery(hqlBuf.toString());

        if(!CollectionUtils.isEmpty(propValMap)) {
            Object[] vals = propValMap.values().toArray();
            for(int i=0; i<vals.length; i++) {
                query.setParameter(i, vals[i]);
            }
        }

        Double num = (Double) query.uniqueResult();
        return Objects.nonNull(num) ? num.doubleValue() : 0;
    }

    public double sumSwapAmount(Map<String, Object> propValMap) {
        StringBuilder hqlBuf = new StringBuilder("select sum(model.swapAmount) from com.trade.model.ICORecord as model where model.delete = false ");
        if(!CollectionUtils.isEmpty(propValMap)) {
            for(String key : propValMap.keySet()) {
                hqlBuf.append("and model.").append(key).append("=? ");
            }
        }

        Query query = getSession().createQuery(hqlBuf.toString());

        if(!CollectionUtils.isEmpty(propValMap)) {
            Object[] vals = propValMap.values().toArray();
            for(int i=0; i<vals.length; i++) {
                query.setParameter(i, vals[i]);
            }
        }

        Double num = (Double) query.uniqueResult();
        return Objects.nonNull(num) ? num.doubleValue() : 0;
    }

    public List<ICORecord> findByProperties(Map<String, Object> propValMap, Integer offset, Integer length) {
        StringBuilder hqlBuf = new StringBuilder("from com.trade.model.ICORecord as model where 1=1 ");
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

    public List<Map> find(String keyword, Integer userId, Integer icoId, Integer coinType, ICORecordStatusEnum statusEnum, Integer offset, Integer length) {
        StringBuilder sqlBuf = new StringBuilder("select ir.id, ir.amount, ir.swap_coin_type, ir.per_swap_amount, ir.swap_amount, ir.status, ir.remark, ir.create_time, ir.update_time, u.fTelephone, u.fEmail, u.fRealName, i.name, ir.ico_id from ico_record as ir ");
        sqlBuf.append("left join fuser as u on u.fId = ir.user_id left join ico as i on i.id = ir.ico_id where ir.is_delete = 0 ");

        if(StringUtils.hasText(keyword)) {
            sqlBuf.append("and (u.fTelephone like :keyword or u.fEmail like :keyword or u.fRealName like :keyword or i.name like :keyword) ");
        }
        if(Objects.nonNull(coinType)) {
            sqlBuf.append("and ir.swap_coin_type = :coinType ");
        }
        if(Objects.nonNull(icoId)) {
            sqlBuf.append("and ir.ico_id = :icoId ");
        }
        if(Objects.nonNull(userId)) {
            sqlBuf.append("and ir.user_id = :userId ");
        }
        if(Objects.nonNull(statusEnum)) {
            sqlBuf.append("and ir.status = :status ");
        }
        sqlBuf.append("order by ir.id desc");

        Query query = getSession().createSQLQuery(sqlBuf.toString());

        if(StringUtils.hasText(keyword)) {
            query.setParameter("keyword", "%" + keyword + "%");
        }
        if(Objects.nonNull(userId)) {
            query.setParameter("userId", userId);
        }
        if(Objects.nonNull(coinType)) {
            query.setParameter("coinType", coinType);
        }
        if(Objects.nonNull(icoId)) {
            query.setParameter("icoId", icoId);
        }
        if(Objects.nonNull(statusEnum)) {
            query.setParameter("status", statusEnum.getIndex());
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
            Object[] objArr = (Object[]) obj;
            if(CollectionUtils.isEmpty(map)) {
                map = new HashMap();
            } else {
                map = (HashMap) map.clone();
            }

            map.put("id", objArr[0]);
            map.put("amount", objArr[1]);
            map.put("swapCoinType", objArr[2]);
            map.put("perSwapAmount", objArr[3]);
            map.put("swapAmount", objArr[4]);
            map.put("status", ICORecordStatusEnum.valueOf((byte)objArr[5]));
            map.put("remark", objArr[6]);
            map.put("createTime", objArr[7]);
            map.put("updateTime", objArr[8]);
            map.put("ftelephone", objArr[9]);
            map.put("femail", objArr[10]);
            map.put("frealName", objArr[11]);
            map.put("icoName", objArr[12]);
            map.put("icoId", objArr[13]);

            resultList.add(map);
        }

        return resultList;
    }

    public int count(String keyword, Integer userId, Integer icoId, Integer coinType, ICORecordStatusEnum statusEnum) {
        StringBuilder sqlBuf = new StringBuilder("select count(ir.id) from ico_record as ir ");
        sqlBuf.append("left join fuser as u on u.fId = ir.user_id left join ico as i on i.id = ir.ico_id where ir.is_delete = 0 ");

        if(StringUtils.hasText(keyword)) {
            sqlBuf.append("and (u.fTelephone like :keyword or u.fEmail like :keyword or u.fRealName like :keyword or i.name like :keyword) ");
        }
        if(Objects.nonNull(coinType)) {
            sqlBuf.append("and ir.swap_coin_type = :coinType ");
        }
        if(Objects.nonNull(icoId)) {
            sqlBuf.append("and ir.ico_id = :icoId ");
        }
        if(Objects.nonNull(userId)) {
            sqlBuf.append("and ir.user_id = :userId ");
        }
        if(Objects.nonNull(statusEnum)) {
            sqlBuf.append("and ir.status = :status ");
        }
        sqlBuf.append("order by ir.id desc");

        Query query = getSession().createSQLQuery(sqlBuf.toString());

        if(StringUtils.hasText(keyword)) {
            query.setParameter("keyword", "%" + keyword + "%");
        }
        if(Objects.nonNull(userId)) {
            query.setParameter("userId", userId);
        }
        if(Objects.nonNull(coinType)) {
            query.setParameter("coinType", coinType);
        }
        if(Objects.nonNull(icoId)) {
            query.setParameter("icoId", icoId);
        }
        if(Objects.nonNull(statusEnum)) {
            query.setParameter("status", statusEnum.getIndex());
        }

        return ((BigInteger)query.uniqueResult()).intValue();
    }

}














package com.trade.dao;

import com.trade.dao.comm.HibernateDaoSupport;
import com.trade.model.Fentrustlog;
import com.trade.model.FuserCointype;
import com.trade.util.Constants;
import org.hibernate.Query;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by Administrator on 2018/6/12 0012.
 */
@Repository
public class FuserCoinTypeDao extends HibernateDaoSupport {
    private static final Logger log = LoggerFactory
            .getLogger(FuserCoinTypeDao.class);

    public List findAll() {
        log.debug("**********查询所有的自选交易对**********");
        try {
            String queryString = "from FuserCointype ";
            Query queryObject = getSession().createQuery(queryString);
            return queryObject.list();
        } catch (RuntimeException re) {
            log.error("find all failed", re);
            throw re;
        }
    }

    /**
     * 保存自选交易对对象
     * @param fuserCointype
     */
    public void save(FuserCointype fuserCointype) {
        log.debug("保存自选交易对对象");
        try {
            getSession().save(fuserCointype);
            log.debug("保存成功");
        } catch (RuntimeException re) {
            log.error("保存自选交易对对象 failed", re);
            throw re;
        }
    }


    /**
     * 根据用户获取用户收藏的交易对
     * @param userId
     * @param maxResults
     * @param isFY
     * @return
     */
    public List<FuserCointype> findByUser(int userId,  int maxResults, boolean isFY) {
        List<FuserCointype> list = null;
        log.debug("根据用户获取用户收藏的交易对");
        try {

            String queryString = "from FuserCointype where fuser.fid=? and fstatus='"+ Constants.collectSelf+"' order by fcreateTime desc";
            Query queryObject = getSession().createQuery(queryString);
            queryObject.setCacheable(true);
            if(isFY){
                queryObject.setMaxResults(maxResults);
            }
           queryObject.setParameter(0, userId);
            list = queryObject.list();
        } catch (RuntimeException re) {
            log.error("根据用户获取用户收藏的交易对失败：", re);
            throw re;
        }
        return list;
    }

    /**
     * 根据用户和交易对ID获取用户收藏的具体交易对
     * @param userId
     * @param maxResults
     * @param isFY
     * @return
     */
    public FuserCointype findByUserIdAndTypeId(int userId, int typeId, int maxResults, boolean isFY) {
        List<FuserCointype> list = null;
        log.debug("根据用户获取用户收藏的交易对");
        try {

            String queryString = "from FuserCointype where fuser.fid=? and market.id=? order by fcreateTime desc";
            Query queryObject = getSession().createQuery(queryString);
            queryObject.setCacheable(true);
            if(isFY){
                queryObject.setMaxResults(maxResults);
            }
            queryObject.setParameter(0, userId);
            queryObject.setParameter(1, typeId);
            list = queryObject.list();
            if(list!=null&&list.size()!=0){
                return list.get(0);
            }
        } catch (RuntimeException re) {
            log.error("根据用户获取用户收藏的交易对失败：", re);
            throw re;
        }

         return null;
    }

    //   收藏/取消收藏
    public FuserCointype merge(FuserCointype detachedInstance) {
        log.debug("merging Fentrustlog instance");
        try {
            FuserCointype result = (FuserCointype) getSession().merge(
                    detachedInstance);
            log.debug("merge successful");
            return result;
        } catch (RuntimeException re) {
            log.error("merge failed", re);
            throw re;
        }
    }

}

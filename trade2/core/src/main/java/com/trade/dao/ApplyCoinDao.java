package com.trade.dao;

import com.trade.dao.comm.HibernateDaoSupport;
import com.trade.model.ApplyCoin;
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
public class ApplyCoinDao extends HibernateDaoSupport {
    private static final Logger log = LoggerFactory
            .getLogger(ApplyCoinDao.class);



    /**
     * 保存自选交易对对象bi
     * @param fuserCointype
     */
    public void save(ApplyCoin ApplyCoin) {
        log.debug("保存上币申请对象");
        try {
            getSession().save(ApplyCoin);
            log.debug("保存成功");
        } catch (RuntimeException re) {
            log.error("保存上币申请对象 failed", re);
            throw re;
        }
    }




}

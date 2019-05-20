package com.trade.dao;

import java.math.BigInteger;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import com.trade.dao.comm.HibernateDaoSupport;
import com.trade.dto.ArticleItemDTO;
import com.trade.util.CollectionUtils;
import org.hibernate.LockMode;
import org.hibernate.Query;

import static org.hibernate.criterion.Example.create;

import org.hibernate.SQLQuery;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

import com.trade.model.Farticle;

/**
 * A data access object (DAO) providing persistence and search support for Farticle entities.
 * Transaction control of the save(), update() and delete() operations
 * can directly support Spring container-managed transactions or they can be augmented	to handle user-managed Spring transactions.
 * Each of these methods provides additional information for how to configure it for the desired type of transaction control.
 *
 * @author MyEclipse Persistence Tools
 * @see Farticle
 */
@Repository
public class FarticleDAO extends HibernateDaoSupport {
    private static final Logger log = LoggerFactory.getLogger(FarticleDAO.class);
    //property constants
    public static final String FTITLE = "ftitle";
    public static final String FKEYWORD = "fkeyword";
    public static final String FDESCRIPTION = "fdescription";
    public static final String FCONTENT = "fcontent";


    public void save(Farticle transientInstance) {
        log.debug("saving Farticle instance");
        try {
            getSession().save(transientInstance);
            log.debug("save successful");
        } catch (RuntimeException re) {
            log.error("save failed", re);
            throw re;
        }
    }

    public void delete(Farticle persistentInstance) {
        log.debug("deleting Farticle instance");
        try {
            getSession().delete(persistentInstance);
            log.debug("delete successful");
        } catch (RuntimeException re) {
            log.error("delete failed", re);
            throw re;
        }
    }

    public Farticle findById(java.lang.Integer id) {

        log.debug("getting Farticle instance with id: " + id);
        String sql = "from Farticle where fid = ?";
        Query query = getSession().createQuery(sql);
        query.setCacheable(true);
        query.setInteger(0, id);
        try {
            if(query.list()!=null && query.list().size() > 0){
                return (Farticle) query.list().get(0);
            }else{
                return null;
            }
        } catch (RuntimeException re) {

            log.error("get failed", re);
            throw re;
        }
//		try {
//			Farticle instance = (Farticle) getSession()
//                    .get("com.trade.model.Farticle", id);
//			return instance;
//		} catch (RuntimeException re) {
//			log.error("get failed", re);
//			throw re;
//        }
    }


    public List<Farticle> findByExample(Farticle instance) {
        log.debug("finding Farticle instance by example");
        try {
            List<Farticle> results = (List<Farticle>) getSession()
                    .createCriteria("com.trade.model.Farticle")
                    .add(create(instance))
                    .list();
            log.debug("find by example successful, result size: " + results.size());
            return results;
        } catch (RuntimeException re) {
            log.error("find by example failed", re);
            throw re;
        }
    }

    public List findByProperty(String propertyName, Object value) {
        log.debug("finding Farticle instance with property: " + propertyName
                + ", value: " + value);
        try {
            String queryString = "from Farticle as model where model."
                    + propertyName + "= ?";
            Query queryObject = getSession().createQuery(queryString);
            queryObject.setCacheable(true);
            queryObject.setParameter(0, value);
            return queryObject.list();
        } catch (RuntimeException re) {
            log.error("find by property name failed", re);
            throw re;
        }
    }

    public List<Farticle> findByFtitle(Object ftitle
    ) {
        return findByProperty(FTITLE, ftitle
        );
    }

    public List<Farticle> findByFkeyword(Object fkeyword
    ) {
        return findByProperty(FKEYWORD, fkeyword
        );
    }

    public List<Farticle> findByFdescription(Object fdescription
    ) {
        return findByProperty(FDESCRIPTION, fdescription
        );
    }

    public List<Farticle> findByFcontent(Object fcontent
    ) {
        return findByProperty(FCONTENT, fcontent
        );
    }


    public List findAll() {
        log.debug("finding all Farticle instances");
        try {
            String queryString = "from Farticle";
            Query queryObject = getSession().createQuery(queryString);
            return queryObject.list();
        } catch (RuntimeException re) {
            log.error("find all failed", re);
            throw re;
        }
    }

    public Farticle merge(Farticle detachedInstance) {
        log.debug("merging Farticle instance");
        try {
            Farticle result = (Farticle) getSession()
                    .merge(detachedInstance);
            log.debug("merge successful");
            return result;
        } catch (RuntimeException re) {
            log.error("merge failed", re);
            throw re;
        }
    }

    public void attachDirty(Farticle instance) {
        log.debug("attaching dirty Farticle instance");
        try {
            getSession().saveOrUpdate(instance);
            log.debug("attach successful");
        } catch (RuntimeException re) {
            log.error("attach failed", re);
            throw re;
        }
    }

    public void attachClean(Farticle instance) {
        log.debug("attaching clean Farticle instance");
        try {
            getSession().lock(instance, LockMode.NONE);
            log.debug("attach successful");
        } catch (RuntimeException re) {
            log.error("attach failed", re);
            throw re;
        }
    }

    public List<Farticle> list(int firstResult, int maxResults,
                               String filter, boolean isFY) {
        List<Farticle> list = null;
        log.debug("finding Farticle instance with filter");
        try {
            String queryString = "from Farticle " + filter;
            Query queryObject = getSession().createQuery(queryString);
            if (isFY) {
                queryObject.setFirstResult(firstResult);
                queryObject.setMaxResults(maxResults);
            }
            list = queryObject.list();
        } catch (RuntimeException re) {
            log.error("find Farticle by filter name failed", re);
            throw re;
        }
        return list;
    }

    public List<Farticle> findFarticles(int[] type, int firstResult, int maxResult, String order) {
        log.debug("findFarticles all Farticle instances");
        try {
            StringBuffer queryString = new StringBuffer("from Farticle  ");

            if (type != null) {

                for (int i = 0; i < type.length; i++) {
                    if (i == 0) {
                        queryString.append(" where ftype=? ");
                    } else {
                        queryString.append(" or ftype=? ");
                    }

                }
            }
            queryString.append(" order by " + order);
            Query queryObject = getSession().createQuery(queryString.toString());
            queryObject.setFirstResult(firstResult);
            queryObject.setMaxResults(maxResult);
            return queryObject.list();
        } catch (RuntimeException re) {
            log.error("find all failed", re);
            throw re;
        }
    }

    public List<Farticle> findFarticle(int farticletype, int firstResult, int maxResult) {
        log.debug("findFarticle all Farticle instances");
        try {
            String queryString = "from Farticle where farticletype.fid=? order by id desc";
            Query queryObject = getSession().createQuery(queryString);
            queryObject.setCacheable(true);
            queryObject.setParameter(0, farticletype);
            queryObject.setFirstResult(firstResult);
            queryObject.setMaxResults(maxResult);
            return queryObject.list();
        } catch (RuntimeException re) {
            log.error("find all failed", re);
            throw re;
        }
    }

    public int findFarticleCount(int farticletype) {
        log.debug("findFarticleCount all Farticle instances");
        try {
            String queryString = "select count(*) from Farticle where farticletype.fid=?";
            Query queryObject = getSession().createQuery(queryString);
            queryObject.setCacheable(true);
            queryObject.setParameter(0, farticletype);
            long l = (Long) queryObject.list().get(0);
            return (int) l;
        } catch (RuntimeException re) {
            log.error("find all failed", re);
            throw re;
        }
    }


    /**
     * 列出分页相关虚拟币的文章
     * @param coinId
     * @param firstResult
     * @param maxResults
     * @return
     */
    public List<Farticle> findArticleByCoinId(int coinId, int firstResult, int maxResults){
        String sql = "from Farticle where fvirtualcointype.fid = ? order by id desc";
        Query query = getSession().createQuery(sql);
        query.setInteger(0, coinId);
        query.setCacheable(true);
        query.setFirstResult(firstResult);
        query.setMaxResults(maxResults);
        return query.list();
    }
    /**
     * 计算相关虚拟币的文章个数
     * @param coinId
     * @return
     */
    public int countByCoinId(int coinId){
        String sql = "select count(*) from Farticle where fvirtualcointype.fid = ?";
        Query query = getSession().createQuery(sql);
        query.setInteger(0, coinId);
        Object object = query.uniqueResult();
        if(object == null){
            return 0;
        }else{
            return Integer.valueOf(object + "");
        }
    }

    /**
     * 通过搜索关键字查询文章
     * @param keyword
     * @return
     */
    public List<Farticle> findByKeyword(int firstResult, int maxResults, String keyword){
        String sql = "select * from Farticle where fcontent REGEXP ? or ftitle REGEXP ? order by flastModifyDate desc ";
        Query query = getSession().createSQLQuery(sql).addEntity(Farticle.class);
        query.setString(0, keyword);
        query.setString(1, keyword);
        query.setFirstResult(firstResult);
        query.setMaxResults(maxResults);
        return query.list();
    }

    public List<Farticle> findByKeywordsAnd(int firstResult, int maxResults, String[] keywords){
        String sql = "from Farticle where fcontent like ? or ftitle like ? order by flastModifyDate desc ";
        Query query = getSession().createQuery(sql);
        StringBuilder param = new StringBuilder("%");
        for (String keyword:keywords) {
            if(!"".equals(keyword)){
                param.append("|" + keyword);
            }
        }
        query.setString(0, "%" + param + "%");
        query.setString(1, "%" + param + "%");
        query.setFirstResult(firstResult);
        query.setMaxResults(maxResults);
        return query.list();
    }

    public List<Farticle> findByKeywords(int firstResult, int maxResults, String[] keywords){
        String sql = "select * from farticle where fcontent REGEXP ? or ftitle REGEXP ? order by flastModifyDate DESC ";
        Query query = getSession().createSQLQuery(sql).addEntity(Farticle.class);
        query.setCacheable(true);
        StringBuilder param = new StringBuilder();
        for (String keyword:keywords) {
            if(!"".equals(keyword)){
                param.append("|" + keyword);
            }
        }
        query.setString(0, param.substring(1));
        query.setString(1, param.substring(1));
        query.setFirstResult(firstResult);
        query.setMaxResults(maxResults);
        return query.list();
    }

    public int countByKeyword(String keyword){
        String sql = "select count(*) from Farticle where fcontent REGEXP ? or ftitle REGEXP ? ";
        Query query = getSession().createSQLQuery(sql);
        query.setCacheable(true);
        query.setString(0, keyword);
        query.setString(1, keyword);
        return Integer.parseInt(query.uniqueResult() + "");
    }

    public int countByKeywords(String[] keywords){
        String sql = "select COUNT(*) from farticle where fcontent REGEXP ? or ftitle REGEXP ?";
        Query query = getSession().createSQLQuery(sql);
        StringBuilder param = new StringBuilder();
        for (String keyword:keywords) {
            if(!"".equals(keyword)){
                param.append("|" + keyword);
            }
        }
        query.setString(0, param.substring(1));
        query.setString(1, param.substring(1));
        return Integer.parseInt(query.uniqueResult() + "");
    }

    public int findLastId(int curId, int type, int afterSize , int lang) {
        SQLQuery query = getSession().createSQLQuery("select fId from farticle where fId >= :id and fArticleType = :type and lang = :lang order by fId");
        query.setParameter("id", curId);
        query.setParameter("type", type);
        query.setParameter("lang", lang);
        query.setMaxResults(afterSize);
        List list = query.list();
        if (CollectionUtils.isEmpty(list)) {
            return curId;
        }

        return (Integer)list.get(list.size() - 1);
    }

    public List<ArticleItemDTO> findForMenu(int lastId, int type, int length , int lang) {
        SQLQuery query = getSession().createSQLQuery("select fId, fTitle, isTop, fLastModifyDate from farticle where fId <= :id and fArticleType = :type and lang = :lang order by fId desc");
        query.setParameter("id", lastId);
        query.setParameter("type", type);
        query.setParameter("lang", lang);
        query.setMaxResults(length);
        List list = query.list();

        ArticleItemDTO dto = null;
        List<ArticleItemDTO> items = new ArrayList<>(list.size());
        for (Object obj : list) {
            Object[] objArr = (Object[]) obj;
            if (Objects.isNull(dto)) {
                dto = new ArticleItemDTO();
            } else {
                try {
                    dto = (ArticleItemDTO) dto.clone();
                } catch (CloneNotSupportedException e) {
                    e.printStackTrace();
                }
            }
            if (Objects.nonNull(dto)) {
                dto.setId(((Integer)objArr[0]).intValue());
                dto.setTitle(objArr[1].toString());
                dto.setTop(objArr[2].toString().equals("1"));
                dto.setCreateTime((Timestamp)objArr[3]);
                items.add(dto);
            }
        }

        return items;
    }

}
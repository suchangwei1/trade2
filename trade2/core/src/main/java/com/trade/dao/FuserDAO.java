package com.trade.dao;

import static org.hibernate.criterion.Example.create;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.trade.Enum.UserGradeEnum;
import com.trade.dao.comm.HibernateDaoSupport;
import com.trade.model.Fuser;
import org.hibernate.LockMode;
import org.hibernate.Query;
import org.hibernate.SQLQuery;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

/**
 	* A data access object (DAO) providing persistence and search support for Fuser entities.
 			* Transaction control of the save(), update() and delete() operations 
		can directly support Spring container-managed transactions or they can be augmented	to handle user-managed Spring transactions. 
		Each of these methods provides additional information for how to configure it for the desired type of transaction control. 	
	 * @see Fuser
  * @author MyEclipse Persistence Tools 
 */
@Repository
public class FuserDAO extends HibernateDaoSupport {
	     private static final Logger log = LoggerFactory.getLogger(FuserDAO.class);
		//property constants
	public static final String FLOGIN_NAME = "floginName";
	public static final String FLOGIN_PASSWORD = "floginPassword";
	public static final String FTRADE_PASSWORD = "ftradePassword";
	public static final String FNICK_NAME = "fnickName";
	public static final String FREAL_NAME = "frealName";
	public static final String FTELEPHONE = "ftelephone";
	public static final String FEMAIL = "femail";
	public static final String FIDENTITY_NO = "fidentityNo";
	public static final String FGOOGLE_AUTHENTICATOR = "fgoogleAuthenticator";
	public static final String FMOBIL_MESSAGE_CODE = "fmobilMessageCode";
	public static final String FQQ_VERIFY_CODE = "fqqVerifyCode";
	public static final String FWEIBO_VERIFY_CODE = "fweiboVerifyCode";
	public static final String FSTATUS = "fstatus";
	public static final String FIS_TEL_VALIDATE = "fisTelValidate";
	public static final String FIS_MAIL_VALIDATE = "fisMailValidate";
	public static final String FGOOGLE_VALIDATE = "fgoogleValidate";


    
    public void save(Fuser transientInstance) {
        log.debug("saving Fuser instance");
        try {
            getSession().save(transientInstance);
            log.debug("save successful");
        } catch (RuntimeException re) {
            log.error("save failed", re);
            throw re;
        }
    }
    
	public void delete(Fuser persistentInstance) {
        log.debug("deleting Fuser instance");
        try {
            getSession().delete(persistentInstance);
            log.debug("delete successful");
        } catch (RuntimeException re) {
            log.error("delete failed", re);
            throw re;
        }
    }
    
    public Fuser findById( java.lang.Integer id) {
        log.debug("getting Fuser instance with id: " + id);
        try {
            Fuser instance = (Fuser) getSession()
                    .get("com.trade.model.Fuser", id);
            return instance;
        } catch (RuntimeException re) {
            log.error("get failed", re);
            throw re;
        }
    }
    
    
    public List<Fuser> findByExample(Fuser instance) {
        log.debug("finding Fuser instance by example");
        try {
            List<Fuser> results = (List<Fuser>) getSession()
                    .createCriteria("com.trade.model.Fuser")
                    .add( create(instance) )
            .list();
            log.debug("find by example successful, result size: " + results.size());
            return results;
        } catch (RuntimeException re) {
            log.error("find by example failed", re);
            throw re;
        }
    }    
    
    public List findByProperty(String propertyName, Object value) {
      log.debug("finding Fuser instance with property: " + propertyName
            + ", value: " + value);
      try {
         String queryString = "from Fuser as model where model." 
         						+ propertyName + "= ?";
         Query queryObject = getSession().createQuery(queryString);
		 queryObject.setParameter(0, value);
		 return queryObject.list();
      } catch (RuntimeException re) {
         log.error("find by property name failed", re);
         throw re;
      }
	}
    
    public List findByDate(String propertyName, Date value) {
        log.debug("finding Fuser instance with Date: " + propertyName
              + ", value: " + value);
        try {
           String queryString = "from Fuser as model where model." 
           						+ propertyName + ">= ?";
           Query queryObject = getSession().createQuery(queryString);
  		   queryObject.setParameter(0, value);
  		 return queryObject.list();
        } catch (RuntimeException re) {
           log.error("find by property name failed", re);
           throw re;
        }
  	}

	public List<Fuser> findByFloginName(Object floginName
	) {
		return findByProperty(FLOGIN_NAME, floginName
		);
	}
	
	public List<Fuser> findByFloginPassword(Object floginPassword
	) {
		return findByProperty(FLOGIN_PASSWORD, floginPassword
		);
	}
	
	public List<Fuser> findByFtradePassword(Object ftradePassword
	) {
		return findByProperty(FTRADE_PASSWORD, ftradePassword
		);
	}
	
	public List<Fuser> findByFnickName(Object fnickName
	) {
		return findByProperty(FNICK_NAME, fnickName
		);
	}
	
	public List<Fuser> findByFrealName(Object frealName
	) {
		return findByProperty(FREAL_NAME, frealName
		);
	}
	
	public List<Fuser> findByFtelephone(Object ftelephone
	) {
		return findByProperty(FTELEPHONE, ftelephone
		);
	}
	
	public List<Fuser> findByFemail(Object femail
	) {
		return findByProperty(FEMAIL, femail
		);
	}
	
	public List<Fuser> findByFidentityNo(Object fidentityNo
	) {
		return findByProperty(FIDENTITY_NO, fidentityNo
		);
	}
	
	public List<Fuser> findByFgoogleAuthenticator(Object fgoogleAuthenticator
	) {
		return findByProperty(FGOOGLE_AUTHENTICATOR, fgoogleAuthenticator
		);
	}
	
	public List<Fuser> findByFmobilMessageCode(Object fmobilMessageCode
	) {
		return findByProperty(FMOBIL_MESSAGE_CODE, fmobilMessageCode
		);
	}
	
	public List<Fuser> findByFqqVerifyCode(Object fqqVerifyCode
	) {
		return findByProperty(FQQ_VERIFY_CODE, fqqVerifyCode
		);
	}
	
	public List<Fuser> findByFweiboVerifyCode(Object fweiboVerifyCode
	) {
		return findByProperty(FWEIBO_VERIFY_CODE, fweiboVerifyCode
		);
	}
	
	public List<Fuser> findByFstatus(Object fstatus
	) {
		return findByProperty(FSTATUS, fstatus
		);
	}
	

	public List findAll() {
		log.debug("finding all Fuser instances");
		try {
			String queryString = "from Fuser";
	         Query queryObject = getSession().createQuery(queryString);
			 return queryObject.list();
		} catch (RuntimeException re) {
			log.error("find all failed", re);
			throw re;
		}
	}
	
    public Fuser merge(Fuser detachedInstance) {
        log.debug("merging Fuser instance");
        try {
            Fuser result = (Fuser) getSession()
                    .merge(detachedInstance);
            log.debug("merge successful");
            return result;
        } catch (RuntimeException re) {
            log.error("merge failed", re);
            throw re;
        }
    }

    public void attachDirty(Fuser instance) {
        log.debug("attaching dirty Fuser instance");
        try {
            getSession().saveOrUpdate(instance);
            log.debug("attach successful");
        } catch (RuntimeException re) {
            log.error("attach failed", re);
            throw re;
        }
    }
    
    public void attachClean(Fuser instance) {
        log.debug("attaching clean Fuser instance");
        try {
            getSession().lock(instance, LockMode.NONE);
            log.debug("attach successful");
        } catch (RuntimeException re) {
            log.error("attach failed", re);
            throw re;
        }
    }
    

	public List<Fuser> list(int firstResult, int maxResults,
			String filter,boolean isFY) {
		List<Fuser> list = null;
		log.debug("finding Fuser instance with filter");
		try {
			String queryString = "from Fuser "+filter;
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
	
	public List getUserGroup(String filter) {
		List<Fuser> list = null;
		log.debug("finding Fuser instance with filter");
		try {
			String queryString = "select count(*) cou,DATE_FORMAT(fregistertime,'%Y-%c-%d') fregistertime from " +
					"fuser "+filter+" group by DATE_FORMAT(fregistertime,'%Y-%c-%d')";
			SQLQuery sqlQuery = getSession().createSQLQuery(queryString);
			list = sqlQuery.list();
		} catch (RuntimeException re) {
			log.error("find by filter name failed", re);
			throw re;
		}
		
		return list;
	}
	
	public List<Fuser> findByMap(Map<String, Object> param) {
        log.debug("getting Fuser instance with param");
        try {
            StringBuffer queryString = new StringBuffer("from Fuser as model where " ) ;
            Object[] keys = null ;
            if(param!=null){
            	keys = param.keySet().toArray() ;
            	for (Object object : keys) {
					String keystr = (String)object ;
					queryString.append(keystr+"= ? and ") ;
				}
            	
            }

            queryString.append(" 1=1 ") ;
            
            Query queryObject = getSession().createQuery(queryString.toString());
            if(keys!=null){
            	for (int i=0;i<keys.length;i++) {
					Object value = param.get(keys[i]) ;
					queryObject.setParameter(i, value) ;
				}
            }
   		 return queryObject.list();
         } catch (RuntimeException re) {
            log.error("find by property name failed", re);
            throw re;
         }
    }
	
	public List<Fuser> listUserForAudit(int firstResult, int maxResults,
			String filter,boolean isFY) {
		List<Fuser> list = null;
		log.debug("finding Fuser instance with filter");
		try {
			String queryString = "from Fuser "+filter;
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
	
	
	public List listUsers(int firstResult, int maxResults,
			String filter,boolean isFY) {
		List list = null;
		StringBuffer sf = new StringBuffer();
		sf.append("select * from (select \n");
		sf.append("b.fid, \n");
		sf.append("b.floginName, \n");
		sf.append("b.fRealName, \n");
		sf.append("b.fTelephone, \n");
		sf.append("case b.fgrade \n");
		sf.append("when 1 then '有效用户' \n");
		sf.append("when 2 then '金豆初级玩家' \n");
		sf.append("when 3 then '豆资深玩家' \n");
		sf.append("when 4 then '金豆圈子成员' \n");
		sf.append("when 5 then '金豆圈子领袖' \n");
		sf.append("ELSE '无' \n");
		sf.append("end fownerGrade, \n");
		sf.append("count(a.fid) total, \n");
		sf.append("sum(case when a.fgrade=1 then 1 ELSE 0 end) level1, \n");
		sf.append("sum(case when a.fgrade=2 then 1 ELSE 0 end) level2, \n");
		sf.append("sum(case when a.fgrade=3 then 1 ELSE 0 end) level3, \n");
		sf.append("sum(case when a.fgrade=4 then 1 ELSE 0 end) level4, \n");
		sf.append("sum(case when a.fgrade=5 then 1 ELSE 0 end) level5, \n");
		sf.append("sum(case when a.fgrade IS NULL then 1 ELSE 0 end) \n");
		sf.append(" from fuser a LEFT outer  \n");
		sf.append("join fuser b on a.fIntroUser_id=b.fid where b.floginname is not null \n");
		sf.append("GROUP BY b.fid,b.floginName,b.fRealName,b.fTelephone,b.fgrade)as t \n");
		sf.append(filter+" \n");
		try {
			Query queryObject = getSession().createSQLQuery(sf.toString());
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

	public boolean isNormal(int userId) {
		return !getSession().createSQLQuery("select fid from fuser where fid = " + userId + " and fStatus = 1").list().isEmpty();
	}
}
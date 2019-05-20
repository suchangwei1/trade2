package com.trade.service.front;

import com.trade.dao.FuserCoinTypeDao;
import com.trade.model.FuserCointype;
import com.trade.service.BaseService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * Created by Administrator on 2018/6/13 0013.
 */
@Service
public class FuserCointypeService extends BaseService{
     @Resource
     private FuserCoinTypeDao fuserCoinTypeDao;

    /**
     *
     * @return
     */
    public List findAll() {
     return    fuserCoinTypeDao.findAll();
    }

    /**
     * 根据用户id得到交易对
     * @param userId
     * @param maxResults
     * @param isFY
     * @return
     */
    public List<FuserCointype> findByUser(int userId, int maxResults, boolean isFY) {

      return  fuserCoinTypeDao.findByUser(userId,maxResults,isFY);
    }

    /**
     * 保存自选交易对
     * @param fuserCointype
     */
    public void save(FuserCointype fuserCointype){
          fuserCoinTypeDao.save(fuserCointype);
    }
    /**
     *  根据用户和交易对ID获取用户收藏的具体交易对
     * @param userId
     * @param typeId
     * @param maxResults
     * @param isFY
     * @return
     */
    public FuserCointype findByUserIdAndTypeId(int userId, int typeId, int maxResults, boolean isFY) {
        return  fuserCoinTypeDao.findByUserIdAndTypeId(userId,typeId,maxResults,isFY);
    }
    /**
     * 收藏/取消收藏
     * @param fuserCointype
     * @return
     */
    public FuserCointype updateFuserCointype(FuserCointype fuserCointype){
        return  fuserCoinTypeDao.merge(fuserCointype);
    }

}

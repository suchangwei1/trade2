package com.trade.service.front;

import com.trade.dao.ApplyCoinDao;
import com.trade.model.ApplyCoin;
import com.trade.service.BaseService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * Created by Administrator on 2018/6/13 0013.
 */
@Service
public class ApplyCoinService extends BaseService{
     @Resource
     private ApplyCoinDao applyCoinDao;

     /**
      * 保存申请对象
      * @param ApplyCoin
      */
     public void save(ApplyCoin ApplyCoin){
          applyCoinDao.save(ApplyCoin);
     }

}

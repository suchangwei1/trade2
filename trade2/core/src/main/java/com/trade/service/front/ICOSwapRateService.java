package com.trade.service.front;

import com.trade.dao.ICOSwapRateDao;
import com.trade.model.ICOSwapRate;
import com.trade.util.CollectionUtils;
import com.trade.util.Utils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * Author:xuelin
 * Company:招股金服
 * Date:2017/6/22
 * Desc:
 */
@Service
public class ICOSwapRateService {
    @Autowired
    private ICOSwapRateDao icoSwapRateDao;

    public void save(ICOSwapRate icoSwapRate) {
        icoSwapRate.setCreateTime(Utils.getTimestamp());
        icoSwapRateDao.save(icoSwapRate);
    }

    public void update(ICOSwapRate icoSwapRate) {
        icoSwapRate.setUpdateTime(Utils.getTimestamp());
        icoSwapRateDao.update(icoSwapRate);
    }

    public void delete(ICOSwapRate icoSwapRate) {
        icoSwapRateDao.delete(icoSwapRate);
    }

    public ICOSwapRate findById(int id) {
        return this.icoSwapRateDao.findById(id);
    }

    public ICOSwapRate findCurrent(int icoId, int coinType) {
        List<ICOSwapRate> list = this.icoSwapRateDao.findCurrent(icoId, coinType, 1);
        return CollectionUtils.isEmpty(list) ? null : list.get(0);
    }

    public List<ICOSwapRate> findCurrent(int icoId) {
        return this.icoSwapRateDao.findCurrent(icoId, null, null);
    }

    public List<Map> find(String keyword, Integer icoId, Integer coinType, Integer offset, Integer length) {
        return this.icoSwapRateDao.find(keyword, icoId, coinType, offset, length);
    }
    public int count(String keyword, Integer icoId, Integer coinType) {
        return this.icoSwapRateDao.count(keyword, icoId, coinType);
    }

    public List<ICOSwapRate> findSupportCoin(int icoId) {
        return this.icoSwapRateDao.findSupportCoin(icoId);
    }

}

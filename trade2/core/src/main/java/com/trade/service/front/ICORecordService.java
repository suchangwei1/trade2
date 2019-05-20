package com.trade.service.front;

import com.trade.Enum.ICORecordStatusEnum;
import com.trade.dao.FuserDAO;
import com.trade.dao.FvirtualwalletDAO;
import com.trade.dao.ICORecordDao;
import com.trade.dto.FluentHashMap;
import com.trade.model.ICO;
import com.trade.model.ICORecord;
import com.trade.util.Utils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * Author:xuelin
 * Company:招股金服
 * Date:2017/6/22
 * Desc:
 */
@Service
public class ICORecordService {
    @Autowired
    private ICORecordDao icoRecordDao;
    @Autowired
    private FvirtualwalletDAO fvirtualwalletDAO;
    @Autowired
    private FuserDAO fuserDAO;

    public void save(ICORecord icoRecord) {
        icoRecord.setCreateTime(Utils.getTimestamp());
        icoRecordDao.save(icoRecord);
    }

    public void update(ICORecord icoRecord) {
        icoRecord.setUpdateTime(Utils.getTimestamp());
        icoRecordDao.update(icoRecord);
    }

    public ICORecord findById(int id) {
        return icoRecordDao.findById(id);
    }

    public double sumAmountByUser(int userId, int icoId) {
        return this.icoRecordDao.sumAmount(new FluentHashMap().fluentPut("icoId", icoId).fluentPut("userId", userId).fluentPut("status", ICORecordStatusEnum.SUCCESS));
    }

    public double sumSwapAmount(int icoId, int coinType) {
        return this.icoRecordDao.sumSwapAmount(new FluentHashMap().fluentPut("icoId", icoId).fluentPut("swapCoinType", coinType).fluentPut("status", ICORecordStatusEnum.SUCCESS));
    }

    public double sumAmount(int icoId) {
        return this.icoRecordDao.sumAmount(new FluentHashMap().fluentPut("icoId", icoId).fluentPut("status", ICORecordStatusEnum.SUCCESS));
    }

    public List<Map> list(Integer userId, Integer offset, Integer length) {
        return this.icoRecordDao.find(null, userId, null, null, null, offset, length);
    }

    public int count(Integer userId) {
        return this.icoRecordDao.count(null, userId, null, null, null);
    }

    public List<Map> find(String keyword, Integer userId, Integer icoId, Integer coinType, ICORecordStatusEnum statusEnum, Integer offset, Integer length) {
        return this.icoRecordDao.find(keyword, userId, icoId, coinType, statusEnum, offset, length);
    }

    public int count(String keyword, Integer userId, Integer icoId, Integer coinType, ICORecordStatusEnum statusEnum) {
        return this.icoRecordDao.count(keyword, userId, icoId, coinType, statusEnum);
    }

    public List<ICORecord> findByProperties(Map<String, Object> propValMap, Integer offset, Integer length) {
        if(Objects.isNull(propValMap)) {
            propValMap = Collections.emptyMap();
        }
        return this.icoRecordDao.findByProperties(propValMap, offset, length);
    }

    public void updateRequite(ICORecord icoRecord, ICO ico, int coinType) {
        if(ICORecordStatusEnum.SUCCESS.getIndex() != icoRecord.getStatus().getIndex()) {
            return;
        }

        double amount = Utils.getDouble(icoRecord.getAmount() * ico.getRequiteRate(), 4);
        Utils.assertTrueForService(fvirtualwalletDAO.updateVirtualWalletTotal(icoRecord.getUserId(), coinType, amount) > 0, "ICO记录" + icoRecord.getId() + "发放回报失败");
        icoRecord.setStatus(ICORecordStatusEnum.REQUITE);
        this.update(icoRecord);
    }

    public void updateRefund(ICORecord icoRecord, String remark) {
        if(ICORecordStatusEnum.SUCCESS.getIndex() != icoRecord.getStatus().getIndex()) {
            return;
        }
        Utils.assertTrueForService(fvirtualwalletDAO.updateVirtualWalletTotal(icoRecord.getUserId(), icoRecord.getSwapCoinType(), icoRecord.getSwapAmount()) > 0, "ICO记录" + icoRecord.getId() + "退款失败");
        icoRecord.setStatus(ICORecordStatusEnum.REFUND);
        icoRecord.setRemark(remark);
        this.update(icoRecord);
    }
}

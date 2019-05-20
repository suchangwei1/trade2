package com.trade.service.front;

import com.alibaba.fastjson.JSONObject;
import com.trade.Enum.ICORecordStatusEnum;
import com.trade.Enum.ICOStatusEnum;
import com.trade.dao.FvirtualwalletDAO;
import com.trade.dao.ICODao;
import com.trade.model.Fuser;
import com.trade.model.ICO;
import com.trade.model.ICORecord;
import com.trade.model.ICOSwapRate;
import com.trade.util.Utils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

/**
 * Author:xuelin
 * Company:招股金服
 * Date:2017/6/22
 * Desc:
 */
@Service
public class ICOService {
    @Autowired
    private FvirtualwalletDAO fvirtualwalletDAO;
    @Autowired
    private ICODao icoDao;
    @Autowired
    private ICORecordService icoRecordService;
    @Autowired
    private FrontUserService frontUserService;


    private Logger logger = LoggerFactory.getLogger(ICOService.class);

    public void save(ICO ico) {
        ico.setCreateTime(Utils.getTimestamp());
        JSONObject jsonExt = new JSONObject();
        jsonExt.put("version", 0);
        ico.setJsonExt(jsonExt.toJSONString());
        icoDao.save(ico);
    }

    public void update(ICO ico) {
        ico.setUpdateTime(Utils.getTimestamp());
        icoDao.update(ico);
    }

    public ICO findById(int id) {
        return icoDao.findById(id);
    }

    public List<ICO> find(Integer offset, Integer length) {
        return this.icoDao.find(null, null, null, null, offset, length);
    }

    public int count() {
        return this.icoDao.count(null, null, null, null);
    }

    public List<ICO> find(String keyword, Integer id, Date startTime, Date endTime, Integer offset, Integer length) {
        return this.icoDao.find(keyword, id, startTime, endTime, offset, length);
    }

    public int count(String keyword, Integer id, Date startTime, Date endTime) {
        return this.icoDao.count(keyword, id, startTime, endTime);
    }

    public int updateBuyICO(ICO ico, ICOSwapRate icoSwapRate, double amount, Fuser fuser) {
        // 扣款
        double swapAmount = Utils.getDouble(amount / icoSwapRate.getAmount(), 4);

        Utils.assertTrueForService(fvirtualwalletDAO.updateRmb(fuser.getFid(), icoSwapRate.getCoinType(), swapAmount, Utils.getTimestamp()) > 0, "冻结资金失败，userId：" + fuser.getFid() + " coinId:" + icoSwapRate.getCoinType());


        // 下单
        ICORecord record = new ICORecord();
        record.setUserId(fuser.getFid());
        record.setIcoId(ico.getId());
        record.setPerSwapAmount(icoSwapRate.getAmount());
        record.setSwapCoinType(icoSwapRate.getCoinType());
        record.setSwapAmount(icoSwapRate.getAmount());
        record.setAmount(amount);
        record.setSwapAmount(swapAmount);
        record.setStatus(ICORecordStatusEnum.IN_PROGRESS);
        icoRecordService.save(record);
        return record.getId();
    }

    private void recordRefund(ICORecord icoRecord, String remark) {
        // 退款
        logger.error("ico退款：{}", icoRecord.toString());
        Utils.assertTrueForService(fvirtualwalletDAO.updateRefund(icoRecord.getUserId(), icoRecord.getSwapCoinType(), icoRecord.getSwapAmount(), Utils.getTimestamp()) > 0,
                "冻结资金失败，userId：" + icoRecord.getUserId() + " coinId:" + icoRecord.getSwapCoinType() + "amount:" + icoRecord.getSwapAmount());
        icoRecord.setStatus(ICORecordStatusEnum.REFUND);
        icoRecord.setRemark(remark);
        this.icoRecordService.update(icoRecord);
    }

    /**
     * ico到账
     *
     * @param icoRecord
     */
    public void updateForBuy(ICORecord icoRecord) {
        ICO ico = this.findById(icoRecord.getIcoId());
        ico.setUpdateTime(Utils.getTimestamp());

        // 购买上限
        double haveAmount = icoRecordService.sumAmountByUser(icoRecord.getUserId(), icoRecord.getIcoId());
        if(ico.getLimitAmount() > 0 && icoRecord.getAmount() + haveAmount > ico.getLimitAmount()) {
            this.recordRefund(icoRecord, "您已或即将超过认购上限");
        }
        if (ico.getAmount() < ico.getRightAmount() + icoRecord.getAmount() + ico.getSupplyAmount()) {
            this.recordRefund(icoRecord, "ICO已或即将超出额度");
        }

        if(ICORecordStatusEnum.IN_PROGRESS.getIndex() != icoRecord.getStatus().getIndex()) {
            // 订单已达到限制
            return;
        }

        ico.setRightAmount(ico.getRightAmount() + icoRecord.getAmount());
        ico.setSupportCount(ico.getSupportCount() + 1);
        if(ico.getAmount() <= ico.getRightAmount() + ico.getSupplyAmount()) {
            // 已完成
            ico.setStatus(ICOStatusEnum.SUCCESS);
        }
        this.update(ico);

        Utils.assertTrueForService(fvirtualwalletDAO.updateBuyAndSellVirtualWalletFrozen(icoRecord.getUserId(), icoRecord.getSwapCoinType(), icoRecord.getSwapAmount()) > 0, "扣除冻结资金失败，userId：" + icoRecord.getUserId() + " coinId:" + icoRecord.getSwapCoinType());

        // 到账
        icoRecord.setStatus(ICORecordStatusEnum.SUCCESS);
        this.icoRecordService.update(icoRecord);
    }

}

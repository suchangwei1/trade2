package com.trade.service.admin;

import com.trade.dao.DateWalletStatisticDao;
import com.trade.model.DateWalletStatistic;
import com.trade.util.CollectionUtils;
import com.trade.util.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Map;

@Service
public class DateWalletStatisticService {
    @Autowired
    private VirtualWalletService virtualWalletService;
    @Autowired
    private DateWalletStatisticDao dateWalletStatisticDao;

    public void saveOrUpdate(DateWalletStatistic dateWalletStatistic){
        dateWalletStatisticDao.saveOrUpdate(dateWalletStatistic);
    }

    public List<DateWalletStatistic> findByDate(Date date){
        return dateWalletStatisticDao.findByProperty("date", date);
    }

    public DateWalletStatistic findOne(int coinType, Date date){
        List<DateWalletStatistic> list = dateWalletStatisticDao.findByProperties(new String[]{"coinType", "date"}, new Object[]{coinType, date});
        if(CollectionUtils.isEmpty(list)){
            return null;
        }

        return list.get(0);
    }

    public List<DateWalletStatistic> find(Integer coinType, Date startDate, Date endDate){
        return this.dateWalletStatisticDao.find(coinType, startDate, endDate);
    }

    /**
     * 生成钱包统计记录(非事务)
     *
     */
    public void nonBuildWalletStatisticLog(){
        Date curDate = DateUtils.formatDate(DateUtils.formatDate(new Date()));

        // 全站总人民币
//        DateWalletStatistic rmbLog = this.findOne(0, curDate);
//        if(null == rmbLog){
//            rmbLog = new DateWalletStatistic();
//            rmbLog.setDate(curDate);
//            rmbLog.setCoinType(0);
//            rmbLog.setUpdateTime(new Date());
//        }
//        Map walletMap = this.walletService.getTotalMoney();
//        rmbLog.setTotalBalance(((BigDecimal)walletMap.get("totalRmb")).doubleValue());
//        rmbLog.setTotalFreeze(((BigDecimal)walletMap.get("frozenRmb")).doubleValue());
//        this.saveOrUpdate(rmbLog);

        // 全站总币数量
        List<Map> virtualQtyList = this.virtualWalletService.getTotalQty();
        virtualQtyList.forEach(map -> {
            int coinType = (int)map.get("fVid");
            DateWalletStatistic log = this.findOne(coinType, curDate);
            if(null == log){
                log = new DateWalletStatistic();
                log.setDate(curDate);
                log.setCoinType(coinType);
                log.setUpdateTime(new Date());
            }
            log.setTotalBalance(((BigDecimal)map.get("totalQty")).doubleValue());
            log.setTotalFreeze(((BigDecimal)map.get("frozenQty")).doubleValue());
            this.saveOrUpdate(log);
        });
    }
}

package com.trade.service.front;

import com.trade.Enum.EntrustRobotStatusEnum;
import com.trade.Enum.EntrustStatusEnum;
import com.trade.Enum.EntrustTypeEnum;
import com.trade.auto.RealTimeData;
import com.trade.dao.FentrustDAO;
import com.trade.dao.FentrustlogDAO;
import com.trade.dao.FvirtualwalletDAO;
import com.trade.model.*;
import com.trade.util.MathUtils;
import com.trade.util.Utils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import javax.crypto.spec.SecretKeySpec;
import java.security.Key;
import java.util.*;
import java.util.concurrent.ExecutorService;

@Service
public class FrontTradeService {

    @Autowired
    private FentrustDAO fentrustDAO;
    @Autowired
    private FentrustlogDAO fentrustlogDAO;
    @Autowired
    private RealTimeData realTimeData;
    @Autowired
    private FvirtualwalletDAO fvirtualwalletDAO;
    @Autowired
    private ExecutorService executorService;


    public Fentrust findFentrustById(int id) {
        return this.fentrustDAO.findById(id);
    }

    // 委托买入，改进版
    public Fentrust updateEntrustBuy2(Market market, double tradeAmount, double tradeCnyPrice, Fuser fuser) throws Exception {

        // 检查用户是否需要手续费, 2017/04/05
        double ffee = 0d;
        if (fuser.getFneedFee()) {
            double ffeeRate = market.getBuyFee();
            ffee = MathUtils.multiply(tradeAmount, ffeeRate);
        }

        double totalTradePrice = MathUtils.multiply(tradeAmount, tradeCnyPrice);

        // 不查询直接更新，提高并发量
        // 如果更新失败，则返回，不再下单
        double tradeMonmey = totalTradePrice;
        int updateRow = fvirtualwalletDAO.updateRmb(fuser.getFid(), market.getBuyId(), tradeMonmey, Utils.getTimestamp());
        if (updateRow == 0) {
            return null;
        }

        Fentrust fentrust = new Fentrust();
        fentrust.setFcount(tradeAmount);
        fentrust.setFleftCount(tradeAmount);
        fentrust.setFamount(totalTradePrice);
        fentrust.setFfees(ffee);
        fentrust.setFleftfees(ffee);
        fentrust.setFcreateTime(Utils.getTimestamp());
        fentrust.setFentrustType(EntrustTypeEnum.BUY);
        fentrust.setFisLimit(false);
        fentrust.setFlastUpdatTime(Utils.getTimestamp());
        fentrust.setFprize(tradeCnyPrice);
        fentrust.setFstatus(EntrustStatusEnum.Going);
        fentrust.setFsuccessAmount(0F);
        fentrust.setFhasSubscription(false);
        fentrust.setFuser(fuser);
        fentrust.setMarket(market);
        fentrust.setRobotStatus(EntrustRobotStatusEnum.Normal);
        this.fentrustDAO.save(fentrust);

        return fentrust;
    }
    // 委托卖出，改进版
    public Fentrust updateEntrustSell2(Market market, double tradeAmount, double tradeCnyPrice, Fuser fuser) throws Exception {

        // 不查询直接更新，提高并发量
        // 如果更新失败，则返回，不再下单
        int updateRow = fvirtualwalletDAO.updateRmb(fuser.getFid(), market.getSellId(), tradeAmount, Utils.getTimestamp());
        if (updateRow == 0) {
            return null;
        }

        // 检查用户是否需要手续费, 2016/03/28
        double ffee = 0d;
        if (fuser.getFneedFee()) {
            double ffeeRate = market.getSellFee();
            ffee = MathUtils.multiply(MathUtils.multiply(tradeAmount, tradeCnyPrice), ffeeRate);
        }

        Fentrust fentrust = new Fentrust();
        fentrust.setFamount(MathUtils.multiply(tradeAmount, tradeCnyPrice));
        fentrust.setFcount(tradeAmount);
        fentrust.setFleftCount(tradeAmount);
        fentrust.setFfees(ffee);
        fentrust.setFleftfees(ffee);
        fentrust.setFcreateTime(Utils.getTimestamp());
        fentrust.setFentrustType(EntrustTypeEnum.SELL);
        fentrust.setFisLimit(false);
        fentrust.setFlastUpdatTime(Utils.getTimestamp());
        fentrust.setFprize(tradeCnyPrice);
        fentrust.setFstatus(EntrustStatusEnum.Going);
        fentrust.setFsuccessAmount(0F);
        fentrust.setFuser(fuser);
        fentrust.setFhasSubscription(false);
        fentrust.setMarket(market);
        fentrust.setRobotStatus(EntrustRobotStatusEnum.Normal);
        this.fentrustDAO.save(fentrust);

        return fentrust;

    }



    public void sendToQueue(final boolean fisLimit, final int coinTypeId, final Fentrust fentrust) {
        executorService.execute(new Runnable() {
            @Override
            public void run() {
                sentToMongoAndMQBuy(fisLimit, coinTypeId, fentrust);
            }
        });
    }

    //把买单发送到mongo和消息队列中
    public void sentToMongoAndMQBuy(boolean fisLimit, int coinTypeId, Fentrust buyFentrust) {
        realTimeData.addEntrustBuyMap(coinTypeId, buyFentrust);
    }

    public List getFentrustHistory(int fuid, int fviFid, int first, int max) {
        return fentrustDAO.getFentrustHistory(fuid, fviFid, first, max);
    }

    public List<Fentrust> findFentrustHistory(int fuid, int fviFid, int first, int max) {
        return fentrustDAO.findFentrustHistory(fuid, fviFid, first, max);
    }

    // 委托记录
    public List<Fentrust> findFentrustHistory(int fuid, int fvirtualCoinTypeId,
                                              int[] entrust_type, int first_result, int max_result, String order,
                                              int entrust_status[]) throws Exception {
        List<Fentrust> list = this.fentrustDAO.getFentrustHistory(fuid,
                fvirtualCoinTypeId, entrust_type, first_result, max_result,
                order, entrust_status);
        for (Fentrust fentrust : list) {
            fentrust.getFvirtualcointype().getFname();
        }
        return list;
    }

    public int findFentrustHistoryCount(int fuid, int fvirtualCoinTypeId,
                                        int[] entrust_type, int entrust_status[]) throws Exception {
        return this.fentrustDAO.getFentrustHistoryCount(fuid,
                fvirtualCoinTypeId, entrust_type, entrust_status);
    }

    public void updateCancelFentrust(Fentrust fentrust, Fuser fuser) {

        try {

            java.sql.Timestamp now = Utils.getTimestamp();

            fentrust.setFlastUpdatTime(now);
            fentrust.setFstatus(EntrustStatusEnum.Cancel);
            fentrustDAO.attachDirty(fentrust);

            if (fentrust.getFentrustType() == EntrustTypeEnum.BUY) {
                // 买
                double leftAmount = MathUtils.subtract(fentrust.getFamount(), fentrust.getFsuccessAmount());
                Market market = fentrust.getMarket();
                int count = fvirtualwalletDAO.updateRefund(fuser.getFid(), market.getBuyId(), leftAmount, Utils.getTimestamp());
                if(1 != count){
                    throw new RuntimeException();
                }
            } else {
                // 卖
                double leftCount = fentrust.getFleftCount();
                Market market = fentrust.getMarket();
                int count = fvirtualwalletDAO.updateRefund(fuser.getFid(), market.getSellId(), leftCount, Utils.getTimestamp());
                if(1 != count){
                    throw new RuntimeException();
                }
            }
        } catch (Exception e) {
            throw new RuntimeException();
        }
    }

    public List<Fentrust> findFentrustByParam(int firstResult, int maxResults,
                                              String filter, boolean isFY) {
        return this.fentrustDAO.findByParam(firstResult, maxResults, filter,
                isFY, "Fentrust");
    }

    public int findFentrustByParamCount(String filter) {
        return this.fentrustDAO.findByParamCount(filter, "Fentrust");
    }

    public void updateFeeLog(Fentrust entrust, Fvirtualwallet fvirtualwallet) {
        try {
            this.fentrustDAO.attachDirty(entrust);
            this.fvirtualwalletDAO.attachDirty(fvirtualwallet);
        } catch (Exception e) {
            throw new RuntimeException();
        }
    }



    // 加密
    private static final String KEY_ALGORITHM = "AES";
    private static final String CIPHER_ALGORITHM = "AES/ECB/PKCS5Padding";

    private static Key toKey(byte[] key) throws Exception {
        return new SecretKeySpec(key, KEY_ALGORITHM);
    }

    /**
     * 获取订单信息
     *
     * @return
     */
    public Map<String, Object> findFentrustInfo(int orderId, int fuserId) {
        List<Fentrust> list = findFentrustByParam(0, 50, " where fid=" + orderId + " and fuser.fid=" + fuserId, true);

        if (CollectionUtils.isEmpty(list)) {
            return Collections.emptyMap();
        }

        Fentrust fentrust = list.get(0);
        Map<String, Object> map = new HashMap<>(10);
        map.put("order_id", fentrust.getFid());
        map.put("create_date", fentrust.getFcreateTime());
        map.put("deal_amount", Utils.getDouble(fentrust.getFsuccessAmount(), 2));
        map.put("price", Utils.getDouble(fentrust.getFprize(), 2));
        map.put("status", fentrust.getFstatus());
        map.put("symbol", fentrust.getFvirtualcointype().getfShortName());
        map.put("type", 0 == fentrust.getFentrustType() ? "buy" : "sell");

        return map;
    }

    /**
     * 批量获取订单信息
     * @return
     */
    public List<Map<String, Object>> findFentrustInfo(Set<Integer> orderIds, int fuserId) {
        List<Map<String, Object>> list = new ArrayList<>(orderIds.size());
        for (Integer orderId : orderIds) {
            Map<String, Object> map = this.findFentrustInfo(orderId, fuserId);
            if (!CollectionUtils.isEmpty(map)) {
                list.add(this.findFentrustInfo(orderId, fuserId));
            }
        }
        return list;
    }

    /**
     * 获取历史订单
     *
     * @param fuserId
     * @param market
     * @param entrustType
     * @param status
     * @param beginDate
     * @param endDate
     * @param pageNow
     * @param pageSize
     * @return
     */
    public List<Fentrust> findFentrustHistory(Integer fuserId, Integer market, Integer[] entrustType, Integer[] status, Date beginDate, Date endDate, int pageNow, int pageSize) {
        if (pageNow < 1) {
            pageNow = 1;
        }
        if (pageSize < 0 || pageSize > 200) {
            pageSize = 20;
        }

        return fentrustDAO.findHistory(fuserId, market, entrustType, status, beginDate, endDate, (pageNow - 1) * pageSize, pageSize);
    }


    /**
     * 获取历史订单
     *
     * @param fuserId
     * @param viCoinTypeId
     * @param entrustType
     * @param beginDate
     * @param endDate
     * @param pageNow
     * @param pageSize
     * @return
     */
    public List<Fentrust> findSuccessHistory(Integer fuserId, Integer viCoinTypeId, Integer entrustType, Integer status, String beginDate, String endDate, Integer pageNow, Integer pageSize) {
        if (pageNow < 1) {
            pageNow = 1;
        }
        if (pageSize < 0 || pageSize > 200) {
            pageSize = 20;
        }

        List<Fentrust> list = fentrustDAO.findSuccessHistory(fuserId, viCoinTypeId, entrustType, status, beginDate, endDate, (pageNow - 1) * pageSize, pageSize);

        return list;
    }

    public int countSuccessHistory(Integer fuserId, Integer viCoinTypeId, Integer entrustType, Integer status, String beginDate, String endDate) {
        return fentrustDAO.countSuccessHistory(fuserId, viCoinTypeId, entrustType, status, beginDate, endDate);
    }

    /**
     * 获取用户的所用成交记录
     * @param fuserId
     * @param beginDate
     * @param endDate
     * @param viCoinTypeId
     * @param entrustType
     * @return
     */
    public List<Fentrustlog> findAllLog(Integer fuserId,Date beginDate, Date endDate,Integer viCoinTypeId,Integer entrustType){
        return fentrustDAO.findAllLog(fuserId,beginDate,endDate,viCoinTypeId,entrustType);
    }

//    /**
//     * 统计历史订单
//     *
//     * @param fuserId
//     * @param viCoinTypeId
//     * @param entrustType
//     * @param status
//     * @param beginDate
//     * @param endDate
//     * @return
//     */
//    public int countFentrustHistory(Integer fuserId, Integer viCoinTypeId, Integer entrustType, Integer[] status, Date beginDate, Date endDate) {
//        return fentrustDAO.CountHistory(fuserId, viCoinTypeId, entrustType, status, beginDate, endDate);
//    }
    /**
     * 统计历史订单
     *
     * @param fuserId
     * @param market
     * @param entrustType
     * @param status
     * @param beginDate
     * @param endDate
     * @return
     */
    public int countFentrustHistory(Integer fuserId, Integer market, Integer[] entrustType, Integer[] status, Date beginDate, Date endDate) {
        return fentrustDAO.countHistory(fuserId, market, entrustType, status, beginDate, endDate);
    }

    /**
     * 获取用户订单
     *
     * @param fuserId
     * @param fid
     * @return
     */
    public Fentrust findFentrustByUser(int fuserId, int fid) {
        List<Fentrust> list = this.findFentrustByParam(0, 0, " where fid = " + fid + " and fuser.fid = " + fuserId, false);
        for (Fentrust fentrust : list) {
            fentrust.getFvirtualcointype().getfShortName();
            return fentrust;
        }

        return null;
    }

    /**
     * 平均成交价
     *
     * @param entrustId
     * @return
     */
    public double avgSuccessPrice(int entrustId){
        return fentrustlogDAO.avgSuccessPrice(entrustId);
    }
}
















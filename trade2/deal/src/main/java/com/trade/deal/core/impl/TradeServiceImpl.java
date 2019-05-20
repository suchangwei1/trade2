package com.trade.deal.core.impl;

import com.trade.deal.Enum.EntrustStatusEnum;
import com.trade.deal.Enum.EntrustTypeEnum;
import com.trade.deal.cache.CacheManager;
import com.trade.deal.core.TradeService;
import com.trade.deal.model.FentrustData;
import com.trade.deal.model.FentrustlogData;
import com.trade.deal.util.MathUtils;
import com.trade.deal.util.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.Resource;
import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class TradeServiceImpl implements TradeService {

    private final Logger log = LoggerFactory.getLogger(getClass());

    @Resource
    private DataSource dataSource;

    @Resource
    private ObjectMapper objectMapper;

    public static final String FIND_ENTRUST_SQL = "select fid,fUs_fId,fVi_fId,fEntrustType,fPrize,fAmount,fsuccessAmount,fCount,fleftCount,fstatus,ffees,fleftfees,robotStatus,fCreateTime from fentrust where fid = ?";

    public static final String CALL_DEAL_MARKING = "call dealMarking(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";

    @Override
    public boolean updateDealMaking(final FentrustData buy, final FentrustData sell, FentrustlogData buyLog, FentrustlogData sellLog) {

        try (Connection conn = dataSource.getConnection(); PreparedStatement call = conn.prepareStatement(CALL_DEAL_MARKING)) {

            // 状态检查，如果买单，或者买单，已经取消，或者已经完全成交，则不能进行撮合更新动作
            if (buy.getFstatus() == EntrustStatusEnum.Cancel
                    || buy.getFstatus() == EntrustStatusEnum.AllDeal
                    || sell.getFstatus() == EntrustStatusEnum.Cancel
                    || sell.getFstatus() == EntrustStatusEnum.AllDeal) {
                return false;
            }

            double sellFeeRate = 0d;
            double buyFeeRate = 0d;
            boolean sellNeedFee = sell.isFneedFee();
            boolean buyNeedFee = buy.isFneedFee();

            if (sellNeedFee) {
                sellFeeRate = getFfee(sell.getFviFid(), EntrustTypeEnum.SELL);
            }

            if (buyNeedFee) {
                buyFeeRate = getFfee(buy.getFviFid(), EntrustTypeEnum.BUY);
            }

            log.trace("sell.isFneedFee = {}, getFfee = {}", sellNeedFee, sellFeeRate);

            double buyLeftAmount = 0D;

            double buyFee = MathUtils.multiply(buyLog.getFcount(), buyFeeRate);
            buy.setFleftCount(MathUtils.subtract(buy.getFleftCount(), buyLog.getFcount()));
            buy.setFsuccessAmount(MathUtils.add(buy.getFsuccessAmount(), buyLog.getFamount()));
            buy.setFlastUpdatTime(buyLog.getFcreateTime());
            buy.setFleftfees(buy.getFleftfees() - buyFee);
            if (buy.getFleftCount() < 0.00000001F) {
                buy.setFstatus(EntrustStatusEnum.AllDeal);
                buy.setFleftCount(0D);
            } else {
                buy.setFstatus(EntrustStatusEnum.PartDeal);
            }

            double sellFee = sellLog.getFcount() / sell.getFcount() * sell.getFfees();
            sell.setFleftCount(MathUtils.subtract(sell.getFleftCount(), sellLog.getFcount()));
            sell.setFsuccessAmount(MathUtils.add(sell.getFsuccessAmount(), sellLog.getFamount()));
            sell.setFleftfees(sell.getFleftfees() - sellFee);
            sell.setFlastUpdatTime(sellLog.getFcreateTime());
            if (sell.getFleftCount() < 0.00000001F) {
                sell.setFstatus(EntrustStatusEnum.AllDeal);
                sell.setFleftCount(0D);
            } else {
                sell.setFstatus(EntrustStatusEnum.PartDeal);
            }

            if (buy.getFstatus() == EntrustStatusEnum.AllDeal) {
                buyLeftAmount = MathUtils.subtract(buy.getFamount(), buy.getFsuccessAmount());
            }

            int i = 0;
            // buy
            call.setDouble(++i, buy.getFsuccessAmount());
            call.setDouble(++i, buy.getFleftCount());
            call.setInt(++i, buy.getFstatus());
            call.setTimestamp(++i, buy.getFlastUpdatTime());
            call.setDouble(++i, buyFee);                    // 买入交易手续费
            call.setInt(++i, buy.getFid());

            // sell
            call.setDouble(++i, sell.getFsuccessAmount());
            call.setDouble(++i, sell.getFleftCount());
            call.setInt(++i, sell.getFstatus());
            call.setTimestamp(++i, sell.getFlastUpdatTime());
            call.setDouble(++i, sell.getFleftfees());
            call.setInt(++i, sell.getFid());

            // log
            call.setDouble(++i, buyLog.getFamount());
            call.setDouble(++i, buyLog.getFprize());
            call.setDouble(++i, buyLog.getFcount());
            call.setBoolean(++i, buyLog.isactive());
            call.setBoolean(++i, sellLog.isactive());
            call.setInt(++i, buyLog.getFviFid());

            // wallet
            call.setInt(++i, buy.getFuid());
            call.setInt(++i, sell.getFuid());
            call.setDouble(++i, buyLeftAmount);
            call.setDouble(++i, MathUtils.multiply(sellLog.getFamount(), MathUtils.subtract(1, sellFeeRate)));

            // ret, success: 1, fails: -1
//            call.setString(23, "ret");

            ResultSet rs = call.executeQuery();

            int[] ret = new int[7];

            if (rs.next()) {
                ret[0] = rs.getInt(1);
                ret[1] = rs.getInt(2);
                ret[2] = rs.getInt(3);
                ret[3] = rs.getInt(4);
                ret[4] = rs.getInt(5);
                ret[5] = rs.getInt(6);
                ret[6] = rs.getInt(7);
            }

            long time = System.currentTimeMillis();
            log.info("call dealMarking({},{},{},{},{},{},{},{},{},{},{},{},{},{},{},{},{},{},{},{},{},{}), {} ms. ret={},{},{},{},{},{},{}",
                    // buy
                    buy.getFsuccessAmount(),
                    buy.getFleftCount(),
                    buy.getFstatus(),
                    buy.getFlastUpdatTime(),
                    buyFee,
                    buy.getFid(),
                    // sell
                    sell.getFsuccessAmount(),
                    sell.getFleftCount(),
                    sell.getFstatus(),
                    sell.getFlastUpdatTime(),
                    sell.getFleftfees(),
                    sell.getFid(),
                    // log
                    buyLog.getFamount(),
                    buyLog.getFprize(),
                    buyLog.getFcount(),
                    buyLog.isactive(),
                    sellLog.isactive(),
                    buyLog.getFviFid(),
                    // wallet
                    buy.getFuid(),
                    sell.getFuid(),
                    buyLeftAmount,
                    MathUtils.multiply(sellLog.getFamount(), MathUtils.subtract(1, sellFeeRate)),
                    // time
                    (System.currentTimeMillis() - time),
                    // ret
                    ret[0], ret[1], ret[2], ret[3], ret[4], ret[5], ret[6]
            );

            if (ret[0] == 1
                    && ret[1] == 1
                    && ret[2] == 2
                    && ret[3] == 1
                    && ret[4] == 1
                    && ret[5] == 1
                    && ret[6] == 1) {
                log.trace("deal success buy " + buy.getFid() + ", sell " + sell.getFid());
                return true;
            } else {
                log.error("deal fails buy {}, sell {} updated: {}, {}, {}, {}, {}, {}, {}", buy.getFid(), sell.getFid(), ret[0], ret[1], ret[2], ret[3], ret[4], ret[5], ret[6]);
                return false;
            }

        } catch (Exception e) {
            throw new RuntimeException("match fails " + e);
        }

    }

    @Override
    public FentrustData findByFid(int id) {
        FentrustData fentrust = null;

        log.debug("find entrust from db where fid = {}", id);

        try (Connection conn = dataSource.getConnection(); PreparedStatement ps = conn.prepareStatement(FIND_ENTRUST_SQL)) {
            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                fentrust = objectMapper.parse(rs, FentrustData.class);
            }

        } catch (Exception e) {
            log.error("error " + e);
        }

        return fentrust;
    }

    /**
     * 获取手续费
     * 默认缓存5分钟
     * @param fviFid
     * @return
     * @throws Exception
     */
    private double getFfee(int fviFid, int type) throws Exception {
        double feeRate = 0D;
        String key = fviFid + "-" + type;
        Object cacheFee = cacheManager.get(key);
        if (cacheFee != null) {
            feeRate = (double) cacheFee;
        } else {
            final String sql = "select sell_fee as sell_fee, buy_fee as buy_fee from market where id = ?";
            try (Connection conn = dataSource.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
                ps.setInt(1, fviFid);
                ResultSet rs = ps.executeQuery();
                if (rs.next()) {
                    if (type == EntrustTypeEnum.BUY) {
                        feeRate = rs.getDouble("buy_fee");
                    } else {
                        feeRate = rs.getDouble("sell_fee");
                    }
                    cacheManager.put(key, feeRate, cacheExpireTime);
                }
                rs.close();
            }
        }
        return feeRate;
    }

    private final CacheManager cacheManager = new CacheManager();

    private final long cacheExpireTime = 1000 * 60 * 5;

}

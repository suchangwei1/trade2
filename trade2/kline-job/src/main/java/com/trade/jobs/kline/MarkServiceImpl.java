package com.trade.jobs.kline;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.*;

public class MarkServiceImpl implements MarkService {

    public static final String getClosingPrice = "select fprize from Fentrustlog where FVI_type = ? and fCreateTime >= FROM_UNIXTIME(?) and TO_DAYS(fcreateTime) = TO_DAYS(?) order by fcreateTime desc limit 1";

    public static final String getOpenningPrice = "select fprize from Fentrustlog where FVI_type = ? and fCreateTime >= FROM_UNIXTIME(?) and TO_DAYS(fcreateTime) = TO_DAYS(?) order by fcreateTime asc limit 1";

    public static final String getEntrustBeforeWeek = "select fprize from Fentrustlog where FVI_type = ? and fCreateTime >= FROM_UNIXTIME(?) and TO_DAYS(fcreateTime) = TO_DAYS(?) order by fcreateTime asc limit 1";

    public static final String getEntrustInWeek = "select fprize from Fentrustlog where FVI_type = ? and fCreateTime >= FROM_UNIXTIME(?) and TO_DAYS(fcreateTime) > TO_DAYS(?) order by fcreateTime asc limit 1";

    public static final String getHighestBuyPrice = "SELECT IFNULL(MAX(fprize),0) max FROM fentrustlog WHERE FVI_type = ? AND fEntrustType = ? AND fCreateTime >= FROM_UNIXTIME(?)";

    public static final String getLowestSellPrice = "SELECT IFNULL(MIN(fprize),0) min FROM fentrustlog WHERE FVI_type = ? AND fEntrustType = ? AND fCreateTime >= FROM_UNIXTIME(?)";

    public static final String getSum24 = "SELECT IFNULL(SUM(fCount),0) fcount, iFNULL(SUM(fAmount),0) famount FROM fentrustlog WHERE isactive = 1 and FVI_type = ? AND fCreateTime >= FROM_UNIXTIME(?)";

    public static final String findFvirtualCoinType = "select c.fShortName group_name,a.*,b.* from market a, Fvirtualcointype b, Fvirtualcointype c WHERE a.sell_id = b.fid and a.buy_id = c.fid order by a.id asc";

    public static final String getLatestDealPrize = "select fprize FROM fentrustlog WHERE FVI_type = ? ORDER BY fid desc limit 1";

    @Resource
    private JdbcTemplate jdbcTemplate;

    public MarkServiceImpl() {
    }

    public MarkServiceImpl(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public double getClosingPrice(final int fvirtualcointype){
        Timestamp time = new Timestamp(System.currentTimeMillis() - 24 * 60 * 60 * 1000);
        Date date = new Date(time.getTime());

        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DATE, -1);
        calendar.add(Calendar.HOUR, -date.getHours());
        calendar.add(Calendar.MINUTE, -date.getMinutes());
        calendar.add(Calendar.SECOND, -date.getSeconds());
        calendar.add(Calendar.SECOND, -date.getSeconds());

        long fromTime = calendar.getTime().getTime() / 1000;

        Double price = 0D;
//        Double price = jdbcTemplate.queryForObject(getClosingPrice, Double.class, fvirtualcointype, fromTime, time);
        List<Map<String, Object>> list = jdbcTemplate.queryForList(getClosingPrice, fvirtualcointype, fromTime, time);
        if (list.size() > 0) {
            price = ((BigDecimal) list.get(0).get("fprize")).doubleValue();
        }
        return price == null ? 0D : price;
    }

    public double getOpenningPrice(final int fvirtualcointype){
        Timestamp time = new Timestamp(System.currentTimeMillis());

        Date date = new Date(time.getTime());

        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.HOUR, -date.getHours());
        calendar.add(Calendar.MINUTE, -date.getMinutes());
        calendar.add(Calendar.SECOND, -date.getSeconds());

        long fromTime = calendar.getTime().getTime() / 1000;

        Double price = 0D;
//        Double price = jdbcTemplate.queryForObject(getOpenningPrice, Double.class, fvirtualcointype, fromTime, time);
        List<Map<String, Object>> list = jdbcTemplate.queryForList(getOpenningPrice, fvirtualcointype, fromTime, time);
        if (list.size() > 0) {
            price = ((BigDecimal) list.get(0).get("fprize")).doubleValue();
        }
        return price == null ? 0D : price;
    }

    public double getEntrustBeforeWeek(final int fvirtualcointype){
        Timestamp time = new Timestamp(System.currentTimeMillis() - 7 * 24 * 60 * 60 * 1000);

        Date date = new Date(time.getTime());

        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DATE, -7);
        calendar.add(Calendar.HOUR, -date.getHours());
        calendar.add(Calendar.MINUTE, -date.getMinutes());
        calendar.add(Calendar.SECOND, -date.getSeconds());

        long fromTime = calendar.getTime().getTime() / 1000;
        Double price = 0D;
//        Double price = jdbcTemplate.queryForObject(getEntrustBeforeWeek, Double.class, fvirtualcointype, fromTime, time);
        List<Map<String, Object>> list = jdbcTemplate.queryForList(getEntrustBeforeWeek, fvirtualcointype, fromTime, time);
        if (list.size() > 0) {
            price = ((BigDecimal) list.get(0).get("fprize")).doubleValue();
        }
        return price == null ? 0D : price;
    }

    public double getHighestBuyPrice(int id, long time) {
        Double price = jdbcTemplate.queryForObject(getHighestBuyPrice, Double.class, id, EntrustTypeEnum.BUY, time);
        return price == null ? 0D : price;
    }

    public double getLowestSellPrice(int id, long time) {
        Double price = jdbcTemplate.queryForObject(getLowestSellPrice, Double.class, id, EntrustTypeEnum.SELL, time);
        return price == null ? 0D : price;
    }

    public Map<String, Object> getSum24(int id, long time) {
        Map<String, Object> sum24 = jdbcTemplate.queryForMap(getSum24, id, time);
        double fcount = ((BigDecimal) sum24.get("fcount")).setScale(4, BigDecimal.ROUND_HALF_UP).doubleValue();
        double famount = ((BigDecimal) sum24.get("famount")).setScale(4, BigDecimal.ROUND_HALF_UP).doubleValue();
        sum24.put("fcount", fcount);
        sum24.put("famount", famount);
        return sum24;
    }

    public double getUpanddown(int id, double latestDealPrice) {
        double upanddown = 0D;
        Double closingPrice= getClosingPrice(id);

        if (closingPrice != null && closingPrice > 0) {
            upanddown = (latestDealPrice - closingPrice) / closingPrice;
        } else {
            Double openningPrice = getOpenningPrice(id);
            if (openningPrice != null && openningPrice > 0) {
                upanddown = (latestDealPrice - openningPrice) / openningPrice;
            }
        }
        return upanddown;
    }

    public double getUpanddownweek(int id, double latestDealPrice) {
        double upanddownweek = 0D;
        Double entrustLogBeforeWeekPrice = getEntrustBeforeWeek(id);
        // 周正跌幅 = (最新成交价 - 上周收盘价) / 上周收盘价 or (最新成交价 - 本周开盘价) / 本周开盘价
        if (entrustLogBeforeWeekPrice != null && entrustLogBeforeWeekPrice > 0) {
            upanddownweek = (latestDealPrice - entrustLogBeforeWeekPrice) / entrustLogBeforeWeekPrice;
        } else {
            Double openPriceInWeek = getEntrustInWeek(id);
            if (openPriceInWeek != null && openPriceInWeek > 0) {
                upanddownweek = (latestDealPrice - openPriceInWeek) / openPriceInWeek;
            }
        }
        return upanddownweek;
    }

    private Double getEntrustInWeek(int fvirtualcointype) {
        Timestamp time = new Timestamp(System.currentTimeMillis() - 7 * 24 * 60 * 60 * 1000);

        Date date = new Date(time.getTime());

        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DATE, -7);
        calendar.add(Calendar.HOUR, -date.getHours());
        calendar.add(Calendar.MINUTE, -date.getMinutes());
        calendar.add(Calendar.SECOND, -date.getSeconds());

        long fromTime = calendar.getTime().getTime() / 1000;
        Double price = 0D;
        List<Map<String, Object>> list = jdbcTemplate.queryForList(getEntrustInWeek, fvirtualcointype, fromTime, time);
        if (list.size() > 0) {
            price = ((BigDecimal) list.get(0).get("fprize")).doubleValue();
        }
        return price == null ? 0D : price;
    }

    public List<LatestDealData> findFvirtualCoinType() {
        return jdbcTemplate.queryForObject(findFvirtualCoinType, new RowMapper<List<LatestDealData>>() {
            @Override
            public List<LatestDealData> mapRow(ResultSet rs, int rowNum) throws SQLException {
                List<LatestDealData> list = new ArrayList<>();
                do {
                    LatestDealData data = new LatestDealData();
                    data.setFid(rs.getInt("id"));
                    data.setVolumn(rs.getDouble("ftotalamount"));
                    data.setStatus(rs.getInt("status"));
                    data.setFname(rs.getString("fName"));
                    data.setfShortName(rs.getString("fShortName"));
                    data.setFisShare(rs.getBoolean("trade_status"));
                    data.setFurl(rs.getString("furl"));
                    data.setOpenTrade(rs.getString("trade_time"));
                    data.setCoinTradeType(rs.getInt("buy_id"));
                    data.setGroup(rs.getString("group_name"));
                    data.setHomeOrder(rs.getInt("homeOrder"));
                    data.setTypeOrder(rs.getInt("typeOrder"));
                    data.setTotalOrder(rs.getInt("totalOrder"));
                    data.setHomeShow(rs.getBoolean("homeShow"));
                    data.setFname_sn(data.getfShortName() + " / " + data.getGroup());

                    list.add(data);
                } while (rs.next());

                return list;
            }
        });

//        return jdbcTemplate.queryForObject(findFvirtualCoinType, rs -> {
//            LatestDealData data = new LatestDealData();
//            data.setFid(rs.getInt("fid"));
//            data.setVolumn(rs.getDouble("ftotalamount"));
//            data.setStatus(rs.getInt("fstatus"));
//            data.setFname(rs.getString("fName"));
//            data.setfShortName(rs.getString("fShortName"));
//            data.setFisShare(rs.getBoolean("FIsShare"));
//            data.setFname_sn(data.getFname()+"("+data.getfShortName()+")");
//            data.setFurl(rs.getString("furl"));
//            data.setOpenTrade(rs.getString("openTrade"));
//            data.setCoinTradeType(rs.getInt("coinTradeType"));
//            data.setEquityType(rs.getInt("equityType"));
//            data.setHomeOrder(rs.getInt("homeOrder"));
//            data.setTypeOrder(rs.getInt("typeOrder"));
//            data.setTotalOrder(rs.getInt("totalOrder"));
//            data.setHomeShow(rs.getBoolean("homeShow"));
//            return data;
//        });
    }

//    public LatestDealData getLatestDealData(int id, double ftotalamount, LatestDealData latestDealData) {
//        final Double price = getLatestDealPrize(id);
//        Calendar calendar = Calendar.getInstance();
//        calendar.add(Calendar.DATE, -1);
//        long time = calendar.getTime().getTime() / 1000;
//
//        Map<String, Object> sum24 = getSum24(id, time);
//        double fcount = (double) sum24.get("fcount");
//        double famount = (double) sum24.get("famount");
//
//        double totalDeal = fcount;
//        double lowest = getLowestSellPrice(id, time);
//        double highest = getHighestBuyPrice(id, time);
//        double entrustValue = famount;//成交额
//        double upanddown = getUpanddown(id, price);
//        double upanddownweek = getUpanddownweek(id, price);
//
//        // 更新24小时交易数据：日、周涨幅，市值，成交量
//        latestDealData.setFid(id);
//        latestDealData.setFupanddown(upanddown);
//        latestDealData.setFupanddownweek(upanddownweek);
//        latestDealData.setHighestPrize24(highest);
//        latestDealData.setLowestPrize24(lowest);
//        latestDealData.setTotalDeal24(totalDeal);
//        latestDealData.setFmarketValue(price * ftotalamount);
//        latestDealData.setEntrustValue24(entrustValue);
//
//        return latestDealData;
//    }

    public double getLatestDealPrize(int fid) {
        Double price = 0D;
        List<Map<String, Object>> list = jdbcTemplate.queryForList(getLatestDealPrize, fid);
        if (list.size() > 0) {
            price = ((BigDecimal) list.get(0).get("fprize")).doubleValue();
        }
        return price == null ? 0D : price;
    }
}

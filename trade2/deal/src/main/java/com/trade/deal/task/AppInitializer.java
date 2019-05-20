package com.trade.deal.task;

import com.trade.deal.core.MessageCenter;
import com.trade.deal.core.TradingSystem;
import com.trade.deal.market.CacheDataService;
import com.trade.deal.market.DepthEntrustService;
import com.trade.deal.model.FentrustData;
import com.trade.deal.model.FentrustlogData;
import com.trade.deal.util.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * 初始化
 * 加载数据, 清理数据
 * @author john
 * @version 1.0 2016/6/11 下午10:31
 */
public class AppInitializer {

    private final Logger log = LoggerFactory.getLogger(getClass());

    @Resource
    private MessageCenter messageCenter;
    @Resource
    private JedisPool jedisPool;
    @Resource
    private DataSource dataSource;
    @Resource
    private CacheDataService cacheDataService;
    @Resource
    private ObjectMapper objectMapper;
    @Resource
    private DepthEntrustService depthEntrustService;
    @Resource
    private TradingSystem tradingSystem;

    @PostConstruct
    public void init() throws Exception {
        log.info("clear cached depth data");
        clearCachedDepthData();
        log.trace("get all market");
        List<Integer> fids = getFviFidsFromDB();
        log.info("load latest deal price from db");
        loadLastestDealPriceFromDB(fids);
        log.info("load entrust from db");
        List<FentrustData> list = loadEntrustFromDB();
        log.info("load entrust log from db");
        loadEntrustLogFromDB(fids);
        log.info("send entrust to message center size " + list.size());
        list.forEach(messageCenter::onMessage);
    }

    private void loadEntrustLogFromDB(List<Integer> fids) throws Exception {
        for (Integer fid : fids) {
            try (Connection conn = dataSource.getConnection();
                 PreparedStatement ps = conn.prepareStatement("SELECT fid,FVI_type,fEntrustType,fPrize,fAmount,fCount,isactive,fCreateTime from Fentrustlog where FVI_type = ? and isactive = 1 order by fid desc limit 25")) {
                ps.setInt(1, fid);
                //ps.setObject(2, new Date(System.currentTimeMillis() - 24*60L*60*1000));
                ResultSet rs = ps.executeQuery();
                while (rs.next()) {
                    FentrustlogData log = objectMapper.parse(rs, FentrustlogData.class);
                    cacheDataService.addFentrustLogData(log);
                }
                rs.close();
            }
        }
    }

    private List<FentrustData> loadEntrustFromDB() throws Exception {
        List<FentrustData> list = new ArrayList<>();
        try (Connection conn = dataSource.getConnection();
             PreparedStatement ps = conn.prepareStatement("select b.fneedfee, a.fid, a.FUs_fId, a.fVi_fId, a.fCreateTime, a.fEntrustType, a.fPrize, a.fAmount, a.fsuccessAmount, a.fCount, a.fleftCount, a.fStatus, a.flastUpdatTime, a.ffees, a.fleftfees, a.robotStatus \n" +
                     "\tfrom fentrust a, fuser b where (a.fstatus = 1 or a.fstatus = 2) and a.fisLimit = 0 AND a.FUs_fId = b.fid")) {
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                FentrustData fentrustData = objectMapper.parse(rs, FentrustData.class);
                list.add(fentrustData);
            }
            rs.close();
        }
        return list;
    }

    private void loadLastestDealPriceFromDB(List<Integer> fids) throws SQLException {
        for (Integer fid : fids) {
            double price = 0D;
            try (Connection conn = dataSource.getConnection();
                 PreparedStatement ps = conn.prepareStatement("select fPrize from fentrustlog WHERE FVI_type = ? ORDER BY fid desc LIMIT 1")) {
                ps.setInt(1, fid);
                ResultSet rs = ps.executeQuery();
                while (rs.next()) {
                    price = rs.getDouble("fPrize");
                }
                rs.close();
            }
            tradingSystem.updateMarking(fid, price);
        }
    }

    private List<Integer> getFviFidsFromDB() throws SQLException {
        List<Integer> fids = new ArrayList<>();
        try (Connection conn = dataSource.getConnection();
             PreparedStatement ps = conn.prepareStatement("select id from market where status = 1")) {
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                fids.add(rs.getInt("id"));
            }
            rs.close();
        }
        return fids;
    }

    private void clearCachedDepthData() {
        // clear depth data
        depthEntrustService.clearDepthEntrust();
        try (Jedis jedis = jedisPool.getResource()) {
            jedis.del("depth");
        }
    }

}

package com.trade.deal.core;

import com.trade.deal.Enum.EntrustStatusEnum;
import com.trade.deal.Enum.EntrustTypeEnum;
import com.trade.deal.listener.DealMarkingListener;
import com.trade.deal.listener.EntrustListener;
import com.trade.deal.model.FentrustData;
import com.trade.deal.mq.MessageQueueService;
import com.trade.deal.util.FentrustComparator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.SortedSet;
import java.util.concurrent.BlockingDeque;
import java.util.concurrent.ConcurrentSkipListMap;
import java.util.concurrent.ConcurrentSkipListSet;
import java.util.concurrent.LinkedBlockingDeque;

public class TradingSystem implements Runnable {

    private final Logger log = LoggerFactory.getLogger(getClass());
    @Resource
    private MatchingEngine engine;
    @Resource
    private EntrustListener entrustListener;
    @Resource
    private DealMarkingListener dealMarkingListener;
    @Resource
    private TradeService tradeService;
    @Resource
    private MessageQueueService messageQueueService;

    private Map<Integer, SortedSet<FentrustData>> entrustBuyMap = new ConcurrentSkipListMap<>();
    private Map<Integer, SortedSet<FentrustData>> entrustSellMap = new ConcurrentSkipListMap<>();
    private Map<Integer, FentrustData> entrustMap = new ConcurrentSkipListMap<>();
    private BlockingDeque<FentrustData> entrustQueue = new LinkedBlockingDeque<>();

    private Map<Integer, Integer> errorCount = new ConcurrentSkipListMap<>();

    public TradingSystem() {}

    public Map<Integer, Integer> getBuyCount() {
        Map<Integer, Integer> map = new HashMap<>();
        entrustBuyMap.forEach((k, v) -> {
            map.put(k, v.size());
        });
        return map;
    }

    public Map<Integer, Integer> getSellCount() {
        Map<Integer, Integer> map = new HashMap<>();
        entrustSellMap.forEach((k, v) -> {
            map.put(k, v.size());
        });
        return map;
    }

    public FentrustData getEntrust(Integer id) {
        return entrustMap.get(id);
    }

    public int getCount() {
        return entrustMap.size();
    }

    public int getQueueCount() {
        return entrustQueue.size();
    }

    public TradingSystem(MatchingEngine engine, EntrustListener entrustListener, DealMarkingListener dealMarkingListener, TradeService tradeService, MessageQueueService messageQueueService) {
        this.engine = engine;
        this.entrustListener = entrustListener;
        this.dealMarkingListener = dealMarkingListener;
        this.tradeService = tradeService;
        this.messageQueueService = messageQueueService;

        buildSet(entrustBuyMap, 1, EntrustTypeEnum.BUY);
        buildSet(entrustSellMap, 1, EntrustTypeEnum.SELL);
    }

    public SortedSet<FentrustData> buildSet(Map<Integer, SortedSet<FentrustData>> map, int id, int type) {
        SortedSet<FentrustData> fentrusts = map.get(id);
        if (fentrusts == null) {
            synchronized (map) {
                fentrusts = map.get(id);
                if (fentrusts == null) {
                    if (type == EntrustTypeEnum.SELL) {
                        fentrusts = new ConcurrentSkipListSet<>(FentrustComparator.prizeComparatorASC);
                    } else {
                        fentrusts = new ConcurrentSkipListSet<>(FentrustComparator.prizeComparatorDESC);
                    }
                    map.put(id, fentrusts);
                }
            }
        }
        return fentrusts;
    }

    public void cancelEntrust(FentrustData fentrust) {
        FentrustData data = entrustMap.get(fentrust.getFid());
        if (data != null) {
            log.info("cancelEntrust already existing entrust fid {}", fentrust.getFid());

            data.setFstatus(EntrustStatusEnum.Cancel);
            entrustMap.remove(data.getFid());
            if (fentrust.getFentrustType() == EntrustTypeEnum.BUY) {
                removeEntrust(this.entrustBuyMap, data);
//                entrustBuyMap.get(data.getFviFid()).remove(data);
            } else {
                removeEntrust(this.entrustSellMap, data);
//                entrustSellMap.get(data.getFviFid()).remove(data);
            }
            entrustListener.onCancelEntrust(data);
        } else {
            log.info("cancelEntrust not existing entrust fid {}", fentrust.getFid());
            entrustMap.put(fentrust.getFid(), fentrust);
        }
        updateMarking(fentrust.getFviFid(), 0);
    }

    private void removeEntrust(Map<Integer, SortedSet<FentrustData>> map, FentrustData data) {
//        map.get(data.getFviFid()).remove(data);
        // 极端情况下
        // 即用户已经下了一个订单
        // 这个时候重启了撮合引擎，然后用户又取消了订单，这个时候队列里面有一条消息
        // 或者引擎启动完成，并受到这条消息，发现这条消息不在挂单列表里面，而这个时候又没有任何有效的挂单
        // 此时的挂单列表会为空，需要创建一个，否则，直接使用会出现空指针
        SortedSet<FentrustData> set = map.get(data.getFviFid());
        if (set != null) {
            set.remove(data);
        } else {
            buildSet(map, data.getFviFid(), data.getFentrustType());
        }
    }

    public void addBuyEntrust(int id, FentrustData fentrust) {
        addEntrust(id, fentrust, this.entrustBuyMap);
    }

    public void addSellEntrust(int id, FentrustData fentrust) {
        addEntrust(id, fentrust, this.entrustSellMap);
    }

    private void addEntrust(int id, FentrustData fentrust, Map<Integer, SortedSet<FentrustData>> map) {
        FentrustData exists = entrustMap.put(fentrust.getFid(), fentrust);

        // 如果还没进到队列，就已经被取消，则不再进队列
        if (exists != null && exists.getFstatus() == EntrustStatusEnum.Cancel) {
            entrustMap.remove(fentrust.getFid());
            return;
        }

        entrustQueue.add(fentrust);
        // 这里不要了，更新会有一定的几率获取不到最新的数据
//        updateMarking(id, 0D);
    }

    private void addToSortedSet(Map<Integer, SortedSet<FentrustData>> map, FentrustData fentrustData) {
        // 如果订单没有成交，或者没有取消，就加入到列表里面
        int fstatus = fentrustData.getFstatus();
        if (fstatus != EntrustStatusEnum.AllDeal && fstatus != EntrustStatusEnum.Cancel) {
            Integer id = fentrustData.getFviFid();
            Set<FentrustData> fentrusts = map.get(id);
            if (fentrusts == null) {
                fentrusts = buildSet(map, id, fentrustData.getFentrustType());
            }
            fentrusts.add(fentrustData);
            // 加入到挂单列表的时候，更新价格，在最新的买卖单在最低、最高价格且部分成交时，在将未成交的加到挂单列表，这时需要更新挂单数据
            updateMarking(fentrustData.getFviFid(), 0);
        }
    }

    private void matchBuy(FentrustData buy) {
        boolean flag = true;
        Integer id = buy.getFviFid();
        while (flag) {
            SortedSet<FentrustData> sellList = entrustSellMap.get(id);
            log.debug("matchBuy {}, sell list size = {}", buy.getFid(), (sellList == null ? 0 : sellList.size()));
            if (sellList == null || sellList.size() == 0) {
                flag = false;
            } else {
                FentrustData sell = sellList.first();
                Double sellFprize = sell.getFprize();
                Double buyFprize = buy.getFprize();
                if (sellFprize > buyFprize) {
                    log.debug("sell price = {} is greater than buy price = {} match end", sellFprize, buyFprize);
                    break;
                }
                // 成交量大于O，才代表撮合成功
                double successCount = engine.updateDealMaking(buy, sell, buy.getFviFid());
                log.debug("updateDealMaking result successCount = {}, buy = {}, leftCount = {}, status = {}, sell = {}, leftCount = {}, status = {}", successCount, buy.getFid(), buy.getFleftCount(), buy.getFstatus(), sell.getFid(), sell.getFleftCount(), sell.getFstatus());

                if (buy.getFstatus() == EntrustStatusEnum.AllDeal || buy.getFstatus() == EntrustStatusEnum.Cancel) {
                    log.debug("remove buy = {} from map match end.", buy.getFid());
                    flag = false;
                    entrustMap.remove(buy.getFid());
                }
                if (sell.getFstatus() == EntrustStatusEnum.AllDeal || sell.getFstatus() == EntrustStatusEnum.Cancel) {
                    log.debug("remove sell = {} from map", sell.getFid());
                    sellList.remove(sell);
                    entrustMap.remove(sell.getFid());
                }

                boolean success = updateEntrust(sell, buy, successCount);
                if (!success) {
                    break;
                }
            }
        }
        addToSortedSet(entrustBuyMap, buy);
    }

    private void matchSell(FentrustData sell) {
        boolean flag = true;
        Integer id = sell.getFviFid();
        while (flag) {
            SortedSet<FentrustData> buyList = entrustBuyMap.get(id);
            log.debug("matchSell {}, buy list size = {}", sell.getFid(), (buyList == null ? 0 : buyList.size()));
            if (buyList == null || buyList.size() == 0) {
                flag = false;
            } else {
                FentrustData buy = buyList.first();
                Double buyFprize = buy.getFprize();
                Double sellFprize = sell.getFprize();
                if (sellFprize > buyFprize) {
                    log.debug("sell price = {} is greater than buy price = {} match end.", sellFprize, buy.getFprize());
                    break;
                }
                // 成交量大于O，才代表撮合成功
                double successCount = engine.updateDealMaking(buy, sell, buy.getFviFid());
                log.debug("updateDealMaking result successCount = {}, buy = {}, leftCount = {}, status = {}, sell = {}, leftCount = {}, status = {}", successCount, buy.getFid(), buy.getFleftCount(), buy.getFstatus(), sell.getFid(), sell.getFleftCount(), sell.getFstatus());
                if (sell.getFstatus() == EntrustStatusEnum.AllDeal || sell.getFstatus() == EntrustStatusEnum.Cancel) {
                    log.debug("remove sell = {} from map match end.", sell.getFid());
                    flag = false;
                    entrustMap.remove(sell.getFid());
                }
                if (buy.getFstatus() == EntrustStatusEnum.AllDeal || buy.getFstatus() == EntrustStatusEnum.Cancel) {
                    log.debug("remove buy = {} from map", buy.getFid());
                    buyList.remove(buy);
                    entrustMap.remove(buy.getFid());
                }

                boolean success = updateEntrust(sell, buy, successCount);
                if (!success) {
                    break;
                }
            }
        }
        addToSortedSet(entrustSellMap, sell);
    }

    private boolean updateEntrust(FentrustData sell, FentrustData buy, double successCount) {
        if (successCount > 0) {
            log.debug("updateDepthEntrust(buy = {}, sell = {}, successCount = {})", buy, sell, successCount);
            updateDepthEntrust(buy, sell, successCount);
        } else {

            Integer buyId = buy.getFid();
            Integer sellId = sell.getFid();
            log.debug("match fails sync db to cache buy = {}, sell = {}", buyId, sellId);
            // 同步数据库状态
            // 如果匹配失败，则从数据库中重新查询
            // 错误三次，从挂单从移除，并报警
            Integer buyErrCount = errorCount.get(buyId);
            Integer sellErrCount = errorCount.get(sellId);
            if (buyErrCount != null && buyErrCount == 3) {
                removeAndSendEmail(entrustBuyMap, buy);
            } else {
                errorCount.put(buyId, buyErrCount == null ? 0 : buyErrCount + 1);
            }
            if (sellErrCount != null && sellErrCount == 3) {
                removeAndSendEmail(entrustSellMap, sell);
            } else {
                errorCount.put(sellId, sellErrCount == null ? 0 : sellErrCount + 1);
            }

            if (buyErrCount != null && buyErrCount == 3 || sellErrCount != null && sellErrCount == 3) {
                return false;
            }

            FentrustData dbBuy = tradeService.findByFid(buyId);
            FentrustData dbSell = tradeService.findByFid(sellId);
            Double buyLeftCount = buy.getFleftCount();
            Double sellLeftCount = sell.getFleftCount();
            buyLeftCount = copyData(buy, dbBuy, buyLeftCount);
            sellLeftCount = copyData(sell, dbSell, sellLeftCount);
            if (buyLeftCount != buy.getFleftCount()) {
                log.debug("buy left count not equals db left count id = {}, cache = {}, after = {}", buyId, buyLeftCount, buy.getFleftCount());
                updateDepthEntrust(buy, null, buyLeftCount);
            }
            if (sellLeftCount != sell.getFleftCount()) {
                log.debug("sell left count not equals db left count id = {}, cache = {}, after = {}", sellId, sellLeftCount, sell.getFleftCount());
                updateDepthEntrust(null, sell, sellLeftCount);
            }
        }
        return true;
    }

    private void removeAndSendEmail(Map<Integer, SortedSet<FentrustData>> map, FentrustData entrust) {
        entrust.setFstatus(EntrustStatusEnum.Cancel);
        entrustListener.onUpdateEntrust(entrust);
        entrustMap.remove(entrust.getFid());
        SortedSet<FentrustData> entrustList = map.get(entrust.getFid());
        if (entrustList != null) {
            entrustList.remove(entrust);
        }
        sendEmail(entrust);
    }

    public void sendEmail(FentrustData entrust) {
        Map<String, String> email = new HashMap<>();
        email.put("email", "362228416@qq.com");
        email.put("ftitle", "撮合引擎报警");
        email.put("fcontent", "撮合失败，账户可能出现异常，异常用户ID: " + entrust.getFuid() + ", 订单ID: " + entrust.getFid());

        //加入邮件队列
        log.debug("send email " + email);
        messageQueueService.publish("email.common", email);
    }

    private Double copyData(FentrustData buy, FentrustData dbBuy, Double buyLeftCount) {
        if (dbBuy != null) {
            buyLeftCount = buyLeftCount - dbBuy.getFleftCount();
            buy.setFstatus(dbBuy.getFstatus());
            buy.setFleftCount(dbBuy.getFleftCount());
            buy.setFsuccessAmount(dbBuy.getFsuccessAmount());
            buy.setFleftfees(dbBuy.getFleftfees());
        }
        return buyLeftCount;
    }

    public double getHighestBuy(Integer fid) {
        double highestBuy = 0D;
        SortedSet<FentrustData> buyList = entrustBuyMap.get(fid);
        if (buyList != null && buyList.size() > 0) {
            highestBuy = buyList.first().getFprize();
        }
        return highestBuy;
    }

    public double getLowestSell(Integer fid) {
        double lowestSell = 0D;
        SortedSet<FentrustData> sellList = entrustSellMap.get(fid);
        if (sellList != null && sellList.size() > 0) {
            lowestSell = sellList.first().getFprize();
        }
        return lowestSell;
    }

    public void updateDepthEntrust(FentrustData buy, FentrustData sell, double successCount) {
        // 更新深度挂单数据
        FentrustData buyDepth = null;
        FentrustData sellDepth = null;
        Integer fviFid = null;
        if (buy != null) {
            fviFid = buy.getFviFid();
            buyDepth = buy.clone();
        }
        if (sell != null) {
            fviFid = sell.getFviFid();
            sellDepth = sell.clone();
        }

        if (buyDepth != null) {
            buyDepth.setFleftCount(successCount);
            entrustListener.onUpdateEntrust(buyDepth);
        }

        if (sellDepth != null) {
            sellDepth.setFleftCount(successCount);
            entrustListener.onUpdateEntrust(sellDepth);
        }

        // 更新最新行情： 最新成交价，最高买价，最低卖价
        if (buy != null && sell != null) {
            double successPrice;
            if (buy.getFid() > sell.getFid()) {
                successPrice = sell.getFprize();
            } else {
                successPrice = buy.getFprize();
            }

            updateMarking(fviFid, successPrice);
        } else {
            updateMarking(fviFid, 0);
        }

//        FentrustData buyDepth = buy.clone();
//        FentrustData sellDepth = sell.clone();
//
//        buyDepth.setFleftCount(successCount);
//        sellDepth.setFleftCount(successCount);
//
//        entrustListener.onUpdateEntrust(buyDepth);
//        entrustListener.onUpdateEntrust(sellDepth);
//
//        // 更新最新行情： 最新成交价，最高买价，最低卖价
//        double successPrice;
//        if (buy.getFid() > sell.getFid()) {
//            successPrice = sell.getFprize();
//        } else {
//            successPrice = buy.getFprize();
//        }
//
//        updateMarking(sell.getFviFid(), successPrice);

    }

    public void updateMarking(int fid, double successPrice) {
        double highestBuy = getHighestBuy(fid);
        double lowestSell = getLowestSell(fid);

        dealMarkingListener.updateMarking(fid, successPrice, highestBuy, lowestSell);
    }

    @Override
    public void run() {
        while (true) {
            try {
                FentrustData fentrust = entrustQueue.take();
                if (fentrust.getFentrustType() == EntrustTypeEnum.BUY) {
                    matchBuy(fentrust);
                } else {
                    matchSell(fentrust);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public void setEngine(MatchingEngine engine) {
        this.engine = engine;
    }

    public void setEntrustListener(EntrustListener entrustListener) {
        this.entrustListener = entrustListener;
    }

    public void setDealMarkingListener(DealMarkingListener dealMarkingListener) {
        this.dealMarkingListener = dealMarkingListener;
    }

    public void setTradeService(TradeService tradeService) {
        this.tradeService = tradeService;
    }

    public void setMessageQueueService(MessageQueueService messageQueueService) {
        this.messageQueueService = messageQueueService;
    }
}

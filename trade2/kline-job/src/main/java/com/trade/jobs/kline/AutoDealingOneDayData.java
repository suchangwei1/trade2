package com.trade.jobs.kline;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import java.util.Calendar;
import java.util.List;
import java.util.Map;

public class AutoDealingOneDayData implements Runnable {

    @Resource
    private CacheDataService latestDealDataService;

    @Resource
    private MarkService markService;

    public AutoDealingOneDayData() {
    }

    public AutoDealingOneDayData(CacheDataService latestDealDataService, MarkService markService) {
        this.latestDealDataService = latestDealDataService;
        this.markService = markService;
    }

    @PostConstruct
    public void init() {
        new Thread(this).start();
    }

    public LatestDealData getLatestDealData(Integer fid, Double price, double ftotalamount, LatestDealData latestDealData) {

        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DATE, -1);
        long time = calendar.getTime().getTime() / 1000;

        Map<String, Object> sum24 = markService.getSum24(fid, time);
        double fcount = (double) sum24.get("fcount");
        double famount = (double) sum24.get("famount");

        double totalDeal = fcount;
        double lowest = markService.getLowestSellPrice(fid, time);
        double highest = markService.getHighestBuyPrice(fid, time);
        double entrustValue = famount;//成交额
        double upanddown = markService.getUpanddown(fid, price);
        double upanddownweek = markService.getUpanddownweek(fid, price);

        // 更新24小时交易数据：日、周涨幅，市值，成交量
        latestDealData.setFid(fid);
        latestDealData.setLastDealPrize(price);
        latestDealData.setFupanddown(upanddown);
        latestDealData.setFupanddownweek(upanddownweek);
        latestDealData.setHighestPrize24(highest);
        latestDealData.setLowestPrize24(lowest);
        latestDealData.setTotalDeal24(totalDeal);
        latestDealData.setFmarketValue(price * ftotalamount);
        latestDealData.setEntrustValue24(entrustValue);

        return latestDealData;
    }

    public void calcuate() {
        try {

//            System.out.println("update 24 hours deal data.... " + System.currentTimeMillis());

            List<LatestDealData> fvirtualcointypes = markService.findFvirtualCoinType();

            for (LatestDealData fvirtualcointype : fvirtualcointypes) {

                ensureExists(fvirtualcointype);

                if (fvirtualcointype.getStatus() == 1) {
                    final Integer fid = fvirtualcointype.getFid();
                    final Double price = markService.getLatestDealPrize(fid);
                    final double ftotalamount = fvirtualcointype.getVolumn();
                    getLatestDealData(fid, price, ftotalamount, fvirtualcointype);

                    latestDealDataService.updateLatestDealData(fvirtualcointype);
                }

            }

        } catch (Exception e1) {
            e1.printStackTrace();
        }
    }

    public void ensureExists(LatestDealData latestDealData) {
        if (latestDealDataService.getLatestDealData(latestDealData.getFid()) == null) {
            latestDealDataService.addLatestDealData(latestDealData);
        } else if (latestDealData.getStatus() != 1){
            latestDealDataService.removeLatestDealData(latestDealData);
        } else {
            latestDealDataService.updateLatestDealDataInfo(latestDealData);
        }
    }

    public void run() {
        long updateTime;
        int waitTime = 1000 * 10;

        while (true) {
            updateTime = System.currentTimeMillis();
            calcuate();
            if (System.currentTimeMillis() - updateTime < waitTime) {
                try {
                    Thread.sleep(waitTime);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }

}
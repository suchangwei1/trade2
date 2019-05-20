package com.trade.deal.market;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentSkipListMap;

public class DepthEntrustData {

    private final Logger log = LoggerFactory.getLogger(getClass());

    // depth: price, count
    private SortedMap<Integer, SortedMap<Double, Double>> buy = new ConcurrentSkipListMap<>();
    private Map<Integer, Map<Double, Double>> buyMoney = new ConcurrentHashMap<>();

    private SortedMap<Integer, SortedMap<Double, Double>> sell = new ConcurrentSkipListMap<>();
    private Map<Integer, Map<Double, Double>> sellMoney = new ConcurrentHashMap<>();

    public void incr(int type, int depth, double price, double count, double money) {
        if (type == 0) {
            _incr(buy, type, depth, price, count, money);
        } else {
            _incr(sell, type, depth, price, count, money);
        }
    }

    public List<Double[]> getDepthEntrustData(Integer type, Integer depth, Integer size) {
        List<Double[]> list = new ArrayList<>();
        SortedMap<Double, Double> map;
        if (type == 0) {
            map = buy.get(depth);
        } else {
            map = sell.get(depth);
        }
        if (map != null && map.size() > 0) {
            for (Map.Entry<Double, Double> entry : map.entrySet()) {
                // 调用方需要多少条数据，就生成多少条数据
                if (size-- == 0) {
                    break;
                }
                // 0: price, 1: leftCount, 2: amount
                Double[] data = new Double[]{
                        entry.getKey(),
                        entry.getValue(),
                        getMoney(type, depth, entry.getKey())
                };
                list.add(data);
            }
        }
        return list;
    }

    protected Map<Double, Double> createMoneyMap(Integer type, Integer depth) {
        Map<Double, Double> moneyMap = new ConcurrentHashMap<>();
        Map<Integer, Map<Double, Double>> buyOrSellMoneyMap = type == 0 ? buyMoney : sellMoney;
        buyOrSellMoneyMap.put(depth, moneyMap);
        return moneyMap;
    }

    protected void incrMoney(Integer type, Integer depth, Double price, Double money) {
        Map<Double, Double> moneyMap = type == 0 ? buyMoney.get(depth) : sellMoney.get(depth);
        if (moneyMap == null) {
            moneyMap = createMoneyMap(type, depth);
        }
        Double totalMoney = moneyMap.get(price);
        if (totalMoney == null) {
            totalMoney = 0D;
        }
        totalMoney += money;
        moneyMap.put(price, totalMoney);
    }

    protected void removeMoney(Integer type, Integer depth, Double price) {
        Map<Double, Double> moneyMap = type == 0 ? buyMoney.get(depth) : sellMoney.get(depth);
        if(moneyMap != null){
            moneyMap.remove(price);
        }
    }

    protected Double getMoney(Integer type, Integer depth, Double price) {
        Map<Double, Double> moneyMap = type == 0 ? buyMoney.get(depth) : sellMoney.get(depth);
        return moneyMap.get(price);
    }

    private void _incr(SortedMap<Integer, SortedMap<Double, Double>> sortedMap, Integer type, Integer depth, Double price, double count, Double money) {
        log.debug("incr " + type + " " + depth + " " + price + " " + count);

        SortedMap<Double, Double> map = sortedMap.get(depth);
        if (map == null) {
            if (type == 0) {
                map = createBuyMap();
            } else {
                map = createSellMap();
            }
            sortedMap.put(depth, map);
        }
        Double counts = map.get(price);
        counts = counts == null ? 0 : counts;
        counts += count;
        if (counts < 0.00000001) {
            map.remove(price);
            removeMoney(type, depth, price);
        } else {
            map.put(price, counts);
            incrMoney(type, depth, price, money);
        }
    }

    private ConcurrentSkipListMap createSellMap() {
        return new ConcurrentSkipListMap<>((Comparator) (o1, o2) -> {
            Double d1 = (Double) o1;
            Double d2 = (Double) o2;
            return d1.compareTo(d2);
        });
    }

    private ConcurrentSkipListMap createBuyMap() {
        return new ConcurrentSkipListMap<>((Comparator) (o1, o2) -> {
            Double d1 = (Double) o1;
            Double d2 = (Double) o2;
            return d2.compareTo(d1);
        });
    }


}

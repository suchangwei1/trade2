package com.trade.deal.market.impl;


import com.alibaba.fastjson.JSON;
import com.trade.deal.Enum.EntrustStatusEnum;
import com.trade.deal.Enum.EntrustTypeEnum;
import com.trade.deal.market.DepthCalculateQueue;
import com.trade.deal.market.DepthEntrustService;
import com.trade.deal.model.FentrustData;
import com.trade.util.FormatUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.Pipeline;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.BlockingDeque;
import java.util.concurrent.ConcurrentSkipListMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.LinkedBlockingDeque;

public class DepthCalculateQueueImpl implements DepthCalculateQueue {

    private final Logger log = LoggerFactory.getLogger(getClass());

    @Resource
    private DepthEntrustService depthEntrustService;

    @Resource
    private ExecutorService executorService;

    @Resource
    private JedisPool jedisPool;

    private BlockingDeque<FentrustData> depthFentrustQueue = new LinkedBlockingDeque<>();

    private Map<Integer, BlockingDeque<FentrustData>> depthBlockingDequeMap = new ConcurrentSkipListMap<>();

    // 待发送深度挂单数据
    private Map<String, double[][]> depthData = new ConcurrentSkipListMap<>();

    private BlockingDeque<String> depthDataQueue = new LinkedBlockingDeque<>();

    public DepthCalculateQueueImpl() {
    }

    public DepthCalculateQueueImpl(DepthEntrustService depthEntrustService, ExecutorService executorService, JedisPool jedisPool) {
        this.depthEntrustService = depthEntrustService;
        this.executorService = executorService;
        this.jedisPool = jedisPool;
    }

    @PostConstruct
    public void init() {
        // 启动一个线程来计算深度挂单数据，每次挂单、撤销、撮合成功都会计算
        executorService.execute(this);
        executorService.execute(new SyncDepthDataThread());
//        executorService.execute(new SyncMarketThread());
    }

    @Override
    public void run() {
        while (true) {
            try {
                /*
                 * 每个货币对应一条线程，并对应一个队列
                 * 如果没有的话，则创建一个
                 */
                FentrustData data = depthFentrustQueue.take();
                BlockingDeque<FentrustData> queue = depthBlockingDequeMap.get(data.getFviFid());
                if (queue == null) {
                    queue = new LinkedBlockingDeque<>();
                    depthBlockingDequeMap.put(data.getFviFid(), queue);
                    executorService.execute(new CalculateDepthThread(queue));
                }
                queue.put(data);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 计算深度挂单数据
     * @param data
     */
    public void calculateDepthEntrust(FentrustData data) {
        log.debug("calculate depth entrust vid = {}, status = {}, price = {}, count = {}", data.getFviFid(), data.getFstatus(), data.getFprize(), data.getFleftCount());
        // 加入到待计算队列
        try {
            depthFentrustQueue.put(data);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onMessage(FentrustData message) {
        handleMessage(message);
    }

    private void handleMessage(FentrustData message) {
        int status = message.getFstatus();
        log.debug("handleMessage status = {}", status);
        if (status == EntrustStatusEnum.Going || status == EntrustStatusEnum.PartDeal) {
            for (int i = 4; i >= 0; i--) {
                addPerEntrust(i, message);//添加深度
            }
        } else if (status == EntrustStatusEnum.Cancel) {
            for (int i = 4; i >= 0; i--) {
                removePerEntrust(i, message);//移除深度
            }
        } else {
            log.error("illegal entrust id = {}, uid = {}, fvfid = {}, fprice = {}, leftcount = {}, fstatus = {}", message.getFid(), message.getFuid(), message.getFviFid(), message.getFprize(), message.getFleftCount(), message.getFstatus());
        }
    }

    private void addPerEntrust(int i, FentrustData message) {
        updateDepthData(i, message, 1);
    }

    private void removePerEntrust(int i, FentrustData message) {
        updateDepthData(i, message, -1);
    }

    /**
     * 更新挂单数据
     * @param i
     * @param message
     * @param addOrMinus
     */
    private void updateDepthData(int i, FentrustData message, int addOrMinus) {
      /*  String format = deepFormat(i);
        String current_price = doubleToString(message.getFprize(), format);*/

        String current_price = FormatUtils.formatCoin(message.getFprize(),"0.00000000");
        final Double price = new Double(current_price);
        final int type = message.getFentrustType();
        final int deep = i;
        final int fviFid = message.getFviFid();
        String fleftCount_current=FormatUtils.formatCoin(message.getFleftCount() * addOrMinus,"0.00000000");
        final Double fleftCount =new Double(fleftCount_current); //message.getFleftCount() * addOrMinus;

        String money_current=FormatUtils.formatCoin(message.getFprize(),"0.00000000");

        final Double money = new Double(money_current);   // message.getFprize() * fleftCount;

        // 深度挂单，用redis实现，不用mongo，mongo高并发频繁更新会出现数据不一致的问题
//        long time = System.currentTimeMillis();
        log.debug("updateDepthEntrust(fviFid = {}, type = {}, deep = {}, price = {}, fleftCount = {})", fviFid, type, deep, price, fleftCount);
        depthEntrustService.updateDepthEntrust(fviFid, type, deep, price, fleftCount, money);
        log.debug("syncDepthEntrustToRedis(fviFid = {}, type = {}, deep = {})", fviFid, type, deep);
        syncDepthEntrustToRedis(fviFid, type, deep);
//        System.out.println("update depth " + (++count) + " " + (System.currentTimeMillis() - time) + "ms");

    }

//    int count = 0;

    private void syncDepthEntrustToRedis(int fviFid, int type, int deep) {
        List<FentrustData> depthEntrust = depthEntrustService.getDepthEntrust(fviFid, type, deep, 21);
        int size = depthEntrust.size() > 21 ? 21 : depthEntrust.size();
        List<FentrustData> message = depthEntrust.subList(0, size);
        double[][] messages = new double[size][3];
        for (int i = 0; i < size; i++) {
            FentrustData data = message.get(i);
            messages[i] = new double[]{Double.valueOf(FormatUtils.formatCoin(data.getFprize(),"0.00000000") ) ,Double.valueOf(FormatUtils.formatCoin(data.getFleftCount(),"0.00000000")) , Double.valueOf(FormatUtils.formatCoin(data.getFamount(),"0.00000000")) };
        }
        String key = fviFid + ":" + type + ":" + deep;
        depthData.put(key, messages);
        depthDataQueue.push(key);
    }

    class SyncDepthDataThread implements Runnable {
        @Override
        public void run() {

            SyncMarketThread syncMarketThread = new SyncMarketThread();

            // 使用消息队列，来更新行情，解决轮询集合大小导致cpu使用率高的问题
            while (true) {
                try {
                    String key = depthDataQueue.take();
                    double[][] data = depthData.remove(key);
                    if (data != null) {
                        saveToRedis(key, data);
                        syncMarketThread.run();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    log.error("error " + e);
                }
            }
        }

        private void saveToRedis(String key, double[][] data) {
            try (Jedis jedis = jedisPool.getResource(); Pipeline pip = jedis.pipelined()) {
                Long counter = jedis.hincrBy("counter", key, 1);
                pip.hset("depth", key, JSON.toJSONString(data));
                pip.publish(key, counter + "");
            } catch (Exception e) {
                log.error("error " + e);
            }
        }
    }

    class SyncMarketThread implements Runnable {
        @Override
        public void run() {
            try {
                if (depthData.size() > 0) {
                    Set<Integer> fviFid2 = depthEntrustService.getDepthKeys();
                    for (Integer fviFid : fviFid2) {
                        StringBuilder sb = new StringBuilder();
                        List<FentrustData> buyDepth = depthEntrustService.getDepthEntrust(fviFid, EntrustTypeEnum.BUY, 4, 50);
                        List<FentrustData> sellDepth = depthEntrustService.getDepthEntrust(fviFid, EntrustTypeEnum.SELL, 4, 50);
                        sb.append("[[");
                        generateJSON(sb, buyDepth);
                        sb.append("],[");
                        generateJSON(sb, sellDepth);
                        sb.append("]]");
                        saveToRedis(fviFid + "", sb.toString());
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
                log.error("error " + e);
            }
        }

        private void generateJSON(StringBuilder sb, List<FentrustData> depth) {
            double count = 0;
            for (int i = 0; i < depth.size(); i++) {
                FentrustData data = depth.get(i);
                if (i > 0) {
                    sb.append(",");
                }
                count += data.getFleftCount();
                sb.append("[" + FormatUtils.formatCoin(data.getFprize(),"0.00000000")  + "," + new BigDecimal(count).setScale(4, BigDecimal.ROUND_HALF_UP).doubleValue() + "]");
            }
        }

        private void saveToRedis(String key, String data) {
//            System.out.println(key + ", " + data);
            try (Jedis jedis = jedisPool.getResource()) {
                jedis.hset("market", key, data);
            } catch (Exception e) {
                log.error("error " + e);
            }
        }
    }

    class CalculateDepthThread implements Runnable {

        private BlockingDeque<FentrustData> blockingDeque;

        public CalculateDepthThread(BlockingDeque<FentrustData> blockingDeque) {
            this.blockingDeque = blockingDeque;
        }

        @Override
        public void run() {
            while (true) {
                try {
                    FentrustData data = blockingDeque.take();
                    handleMessage(data);
                } catch (Exception e) {
                    e.printStackTrace();
                    log.error("error " + e);
                }
            }
        }
    }

    private String deepFormat(int i){
        i = i + 4;
        String format="#";
        switch(i){
            case 0:format="#";break;
            case 1:format="#.#";break;
            case 2:format="#.##";break;
            case 3:format="#.###";break;
            case 4:format="#.####";break;
            case 5:format="#.#####";break;
            case 6:format="#.######";break;
            case 7:format="#.#######";break;
            case 8:format="#.########";break;
            default:format="#.####";break;
        }
        return format;
    }

    private String doubleToString(Double dou, String format) {
        DecimalFormat decimalFormat = new DecimalFormat(format);
        decimalFormat.setRoundingMode(RoundingMode.FLOOR);
        String string = decimalFormat.format(dou);// 四舍五入，逢五进一
        return string;
    }

}

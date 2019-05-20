package com.trade.push.data;

import com.alibaba.fastjson.JSON;
import com.trade.push.model.EntrustlogData;
import com.trade.util.FormatUtils;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

import javax.annotation.Resource;
import java.text.SimpleDateFormat;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;
import java.util.concurrent.ConcurrentHashMap;

public class RealDataServiceImpl implements RealDataService {

    public static final SimpleDateFormat SIMPLE_DATE_FORMAT = new SimpleDateFormat("MM-dd HH:mm:ss");

    @Resource
    private JedisPool pool;

    // key -> counter
    private Map<String, Long> countMap = new ConcurrentHashMap<>();
    // key -> message
    private Map<String, String> depthMap = new ConcurrentHashMap<>();

    @Override
    public String getEntrustLog(int fviFid) {
        final StringBuilder sb = new StringBuilder();
        sb.append("[");
        try (Jedis connection = pool.getResource()) {

            Set<EntrustlogData> set = new TreeSet<>();
            byte[] mkey = ("cache:fentrustlog:" + fviFid).getBytes();
            Set<byte[]> bytes = connection.zrevrange(mkey, 0, 20);
            int i = bytes.size();
            for (byte[] b : bytes) {
                EntrustlogData obj = JSON.parseObject(b, EntrustlogData.class);

                obj.setFprize(Double.valueOf(FormatUtils.formatCoin(obj.getFprize(),"0.00000000")));
                obj.setFamount(Double.valueOf(FormatUtils.formatCoin(obj.getFamount(),"0.00000000")));
                obj.setFcount(Double.valueOf(FormatUtils.formatCoin(obj.getFcount(),"0.00000000")));
                obj.setFid(i--);
                set.add(obj);
            }

            Object[] data = set.toArray();

            for (int j = 0; j < data.length; j++) {
                EntrustlogData row = (EntrustlogData) data[j];
                if (j > 0) {
                    sb.append(",");
                }
                sb.append("[");
                sb.append(row.getFprize());
                sb.append(",");
                sb.append(row.getFcount());
                sb.append(",\"");
                sb.append(SIMPLE_DATE_FORMAT.format(row.getFcreateTime()));
                sb.append("\",");
                sb.append(row.getfEntrustType() + 1);
                sb.append("]");
            }
        }
        sb.append("]");
        return sb.toString();
    }

    @Override
    public String getDepthEntrust(String key) {
        try (Jedis connection = pool.getResource()) {
            String data = connection.hget("depth", key);
            return data == null ? "[]" : data;
        }
    }

    @Override
    public String getReal(int key) {
        try (Jedis connection = pool.getResource()) {
            String data = connection.hget("cache:latest", "" + key);
            return data == null ? "[]": data;
        }
    }

    @Override
    public boolean isNewMessage(String key, String message) {
        // 判断本次消息是否是最新的消息，通过使用一个计数器来实现
        // 获取消息的时候，获取最新的计数值，假如后续消息的值小于这个最新的值，则表示这个消息已过期，不用处理
        long counter = Long.valueOf(message);
        Long oldCounter = countMap.get(key);
        if (oldCounter == null || oldCounter < counter) {
            try (Jedis jedis = pool.getResource()) {
                String sCount = jedis.hget("counter", key);
                countMap.put(key, sCount != null ? Long.valueOf(sCount) : 0);
            }
            return true;
        }
        return false;
    }

    @Override
    public String getNewMessage(String key) {
        // 获取最新的消息
        // 先从Redis中查询消息，然后与本地的对比，如果内容一致，则返回null，表示没有最新的消息
        String data = getDepthEntrust(key);
        String oldData = depthMap.get(key);
        if (data != null && !data.equals(oldData)) {
            depthMap.put(key, data);
            return data;
        }
        return null;
    }

}

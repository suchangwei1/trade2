package com.trade.cache.data.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.trade.cache.data.KlineDataService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

import javax.annotation.PostConstruct;
import java.text.ParseException;
import java.text.SimpleDateFormat;

@Service
public class KlineDataServiceImpl implements KlineDataService {

    @Autowired
    private JedisPool jedisPool;

    @PostConstruct
    private void init() {
    }

    @Override
    public String getJsonString(int id, int key) {
        String data = getJson(id, key);
        return substringData(data);
    }

    @Override
    public String getJsonString(String begin, String end, int id, int key) {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        long startTimestamp = 0;
        long endTimestamp = 0;
        try {

            //得到分钟
            startTimestamp = format.parse(begin).getTime() / 1000;
            endTimestamp = format.parse(end).getTime() / 1000;
        } catch (ParseException e) {
            e.printStackTrace();
        }

        String data = getJson(id, key);

        int startline = data.indexOf(startTimestamp + "");
        int endline = data.indexOf(endTimestamp + "");
        if(startline > -1 && endline > -1){
            return data.substring(startline, endline);
        }else {
            JSONArray jsonArray = JSON.parseArray(data);

            int startIndex = serach(jsonArray, startTimestamp);
            int endIndex = serach(jsonArray, endTimestamp);

            String startStr = jsonArray.getJSONArray(startIndex).getString(0);
            String endStr = jsonArray.getJSONArray(endIndex).getString(0);

            startline = data.indexOf(startStr);
            endIndex = data.indexOf(endStr);

            if(startIndex > -1 && endIndex > -1){

                String result = data.substring(startline, endIndex - 2);
                return "[[" + result + "]";
            }
            return data;

//            sb.append("[");
//
//            for (int i = 0; i < jsonArray.size(); i++){
//                JSONArray array = jsonArray.getJSONArray(i);
//                if(array.getLong(0) > startTimestamp && array.getLong(0) < endTimestamp){
//                    sb.append(array);
//                    sb.append(",");
//                }
//            }
//
//            if(sb.length() > 1){
//                sb.deleteCharAt(sb.length() - 1);
//            }
//            sb.append("]");

        }



    }

    private static int serach(JSONArray arry, long target){
        int start = 0;
        int end = arry.size() - 1;
        int middle = (end + start) >>> 1;

        while(start <= end){
            if(arry.getJSONArray(middle).getLong(0) > target){
                end = middle;
                if(start - end == -1){
                    return start;
                }
            }else if(arry.getJSONArray(middle).getLong(0) < target){
                start = middle;
                if(start - end == -1){
                    return end;
                }
            }else if(arry.getJSONArray(middle).getLong(0) == target){
                return middle;
            }

            middle = (end + start) >>> 1;
        }
        return -1;
    }

    private String getJson(int id, int key) {
        final byte[] mkey = ("cache:kline:" + id).getBytes();
        final byte[] jkey = ("" + key).getBytes();

        byte[] bytes;
        try (Jedis jedis = jedisPool.getResource()) {
            bytes = jedis.hget(mkey, jkey);
        }

        if (bytes != null) {
            return new String(bytes);
        }

        return "[]";
    }

    private static String substringData(String data){

        int length = data.length();
        int count = 0;
        for (int i = length - 1; i >= 0; i--) {
            if(data.charAt(i) == ']'){
                count ++;
            }

            if(count == 802){
                return "[" + data.substring(i + 2);
            }
        }
        return data;
    }

}

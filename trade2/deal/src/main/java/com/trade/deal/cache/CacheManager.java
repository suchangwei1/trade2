package com.trade.deal.cache;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class CacheManager {

    private Map<String, Cache> cacheMap = new ConcurrentHashMap<>();

    public Object get(String key) {
        Cache cache = cacheMap.get(key);
        if (cache != null && System.currentTimeMillis() > cache.expireTime) {
            cacheMap.remove(key);
            cache = null;
        }
        return cache == null ? null : cache.value;
    }

    public void put(String key, Object value, long expire) {
        Cache cache = new Cache();
        cache.value = value;
        cache.expireTime = System.currentTimeMillis() + expire;
        cacheMap.put(key, cache);
    }

    class Cache {
        Object value;
        long expireTime;
    }

}





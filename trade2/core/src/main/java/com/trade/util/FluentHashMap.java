package com.trade.util;

import java.util.HashMap;

public class FluentHashMap<K, V> extends HashMap<K, V> {

    public FluentHashMap fluentPut(K key, V value) {
        this.put(key, value);
        return this;
    }

}

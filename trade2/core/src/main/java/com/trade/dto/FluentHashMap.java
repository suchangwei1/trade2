package com.trade.dto;

import java.util.HashMap;

/**
 * Author:xuelin
 * Company:招股金服
 * Date:2017/6/22
 * Desc:
 */
public class FluentHashMap extends HashMap {

    public FluentHashMap fluentPut(Object key, Object value) {
        this.put(key, value);
        return this;
    }

}

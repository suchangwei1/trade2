package com.trade.auto;

import com.trade.cache.data.KlineDataService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;

public class KlinePeriodData {

    @Resource
    private KlineDataService klineDataService;

    @PostConstruct
    public void init() {
    }

    public String getJsonString(int id, int key) {
        return klineDataService.getJsonString(id, key);
    }

    public String getJsonString(String begin, String end, int id, int key) {
        return klineDataService.getJsonString(begin, end, id, key);
    }

}

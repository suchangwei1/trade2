package com.trade.cache.data;

public interface KlineDataService {

    /**
     * 获取K线
     * @param id
     * @param key
     * @return
     */
    String getJsonString(int id, int key);


    /**
     * 某段时间的K线
     * @param begin
     * @param end
     * @param id
     * @param key
     * @return
     */
    String getJsonString(String begin, String end, int id, int key);

}

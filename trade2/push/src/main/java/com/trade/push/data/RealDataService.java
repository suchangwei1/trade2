package com.trade.push.data;

public interface RealDataService {

    String getEntrustLog(int fviFid);

    String getDepthEntrust(String key);

    boolean isNewMessage(String key, String message);

    String getNewMessage(String key);

    String getReal(int key);

}

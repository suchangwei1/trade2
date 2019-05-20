package com.trade.deal.listener;

import com.trade.deal.model.FentrustlogData;

public interface DealMarkingListener {

    void writeLog(FentrustlogData log);

    void updateMarking(int fid, double dealPrice, double highestBuy, double lowestDell);

}

package com.trade.deal.core;

import com.trade.deal.model.FentrustData;

public interface MatchingEngine {

    double updateDealMaking(FentrustData _buyFentrust, FentrustData _sellFentrust, int fviFid);

}

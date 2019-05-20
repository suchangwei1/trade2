package com.trade.cache.data.impl;

import com.trade.Enum.EntrustTypeEnum;
import com.trade.cache.data.RealTimeCenter;
import com.trade.cache.data.RealTimeEntrustDepthService;
import com.trade.dto.FentrustData;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

@Service("realTimeEntrustDepthService")
public class RealTimeEntrustDepthServiceImpl implements RealTimeEntrustDepthService {

    @Resource
    private RealTimeCenter realTimeCenter;

    public RealTimeEntrustDepthServiceImpl() {
    }

	@Override
	public String getSellDepthMap(int id, int deep) {
        return realTimeCenter.getEntrustList(String.format("%d:%d:%d", id, EntrustTypeEnum.SELL, deep));
	}

    @Override
    public String getBuyDepthMap(int id, int deep) {
        return realTimeCenter.getEntrustList(String.format("%d:%d:%d", id, EntrustTypeEnum.BUY, deep));
    }

    @Override
    public String getMarketJSON(int fid) {
        return realTimeCenter.getMarketDepth(fid);
    }
}

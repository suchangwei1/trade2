package com.trade.cache.data;

import com.trade.dto.ArticleItemDTO;

import java.util.List;

public interface RealTimeArticleService {
    List<ArticleItemDTO> getArticleList(int type, int length);
}

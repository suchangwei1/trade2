package com.trade.cache.data.impl;

import com.trade.cache.data.RealTimeArticleService;
import com.trade.dto.ArticleItemDTO;
import com.trade.model.Farticle;
import com.trade.service.admin.ArticleService;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

public class RealTimeArticleServiceImpl implements RealTimeArticleService {
    @Autowired
    private ArticleService articleService;

    @Override
    public List<ArticleItemDTO> getArticleList(int type, int length) {
        List<Farticle> list = articleService.list(0, length, "where farticletype.fid = " + type + " order by isTop desc, fLastModifyDate desc", true);
        return ArticleItemDTO.convert(list);
    }
}

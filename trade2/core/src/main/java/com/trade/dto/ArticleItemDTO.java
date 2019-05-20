package com.trade.dto;

import com.trade.model.Farticle;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;

public class ArticleItemDTO implements Cloneable {

    private int id;
    private String title;
    private String enTitle;
    private boolean top;
    private Date createTime;

    public ArticleItemDTO() {
    }

    public ArticleItemDTO(int id, String title, String enTitle, boolean top, Date createTime) {
        this.id = id;
        this.title = title;
        this.top = top;
        this.enTitle = enTitle;
        this.createTime = createTime;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public boolean isTop() {
        return top;
    }

    public void setTop(boolean top) {
        this.top = top;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public String getEnTitle() {
        return enTitle;
    }

    public void setEnTitle(String enTitle) {
        this.enTitle = enTitle;
    }

    @Override
    public Object clone() throws CloneNotSupportedException {
        return super.clone();
    }

    public ArticleItemDTO clone(Farticle farticle) {
        ArticleItemDTO dto;
        try {
            dto = (ArticleItemDTO) this.clone();
        } catch (CloneNotSupportedException e) {
            e.printStackTrace();
            dto = new ArticleItemDTO();
        }

        dto.setId(farticle.getFid());
        dto.setTitle(farticle.getFtitle());
        dto.setTop(farticle.isTop());
        dto.setEnTitle(farticle.getEnTitle());
        dto.setCreateTime(farticle.getFlastModifyDate());
        return dto;
    }

    public final static List<ArticleItemDTO> convert(List<Farticle> list) {
        List<ArticleItemDTO> items = new ArrayList<>(list.size());
        ArticleItemDTO item = null;
        for (Farticle farticle : list) {
            if (Objects.isNull(item)) {
                item = new ArticleItemDTO(farticle.getFid(), farticle.getFtitle(), farticle.getEnTitle(), farticle.isTop(), farticle.getFlastModifyDate());
            } else {
                item = item.clone(farticle);
            }
            if (Objects.nonNull(item)) {
                items.add(item);
            }
        }

        return items;
    }

    @Override
    public String toString() {
        return "ArticleItemDTO{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", enTitle='" + enTitle + '\'' +
                ", top=" + top +
                ", createTime=" + createTime +
                '}';
    }
}

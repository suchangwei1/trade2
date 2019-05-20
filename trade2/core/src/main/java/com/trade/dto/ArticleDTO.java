package com.trade.dto;

import com.trade.model.Farticle;

import java.util.Date;

public class ArticleDTO {

    private int id;
    private String title;
    private boolean top;
    private int type;
    private String author;
    private String content;
    private String enContent;
    private String enTitle;
    private Date createTime;

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

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getEnContent() {
        return enContent;
    }

    public void setEnContent(String enContent) {
        this.enContent = enContent;
    }

    public String getEnTitle() {
        return enTitle;
    }

    public void setEnTitle(String enTitle) {
        this.enTitle = enTitle;
    }

    public static ArticleDTO convert(Farticle farticle) {
        ArticleDTO dto = new ArticleDTO();
        dto.setId(farticle.getFid());
        dto.setTitle(farticle.getFtitle());
        dto.setTop(farticle.isTop());
        dto.setCreateTime(farticle.getFlastModifyDate());
        dto.setType(farticle.getFarticletype().getFid());
        dto.setContent(farticle.getFcontent());
        dto.setEnContent(farticle.getEnContent());
        dto.setEnTitle(farticle.getEnTitle());
        return dto;
    }

    @Override
    public String toString() {
        return "ArticleDTO{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", top=" + top +
                ", type=" + type +
                ", author='" + author + '\'' +
                ", content='" + content + '\'' +
                ", createTime=" + createTime +
                '}';
    }
}

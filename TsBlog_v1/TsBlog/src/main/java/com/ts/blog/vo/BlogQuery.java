package com.ts.blog.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @BelongsProject: TsBlog
 * @BelongsPackage: com.ts.blog.vo
 * @Author: ts
 * @CreateTime: 2022-11-25  21:07
 * @Description: TODO
 * @Version: 1.0
 **/



public class BlogQuery {

    private String title;
    private Long typeId;
    private boolean recommend;

    @Override
    public String toString() {
        return "BlogQuery{" +
                "title='" + title + '\'' +
                ", typeId=" + typeId +
                ", recommend=" + recommend +
                '}';
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Long getTypeId() {
        return typeId;
    }

    public void setTypeId(Long typeId) {
        this.typeId = typeId;
    }

    public boolean isRecommend() {
        return recommend;
    }

    public void setRecommend(boolean recommend) {
        this.recommend = recommend;
    }

    public BlogQuery() {
    }

    public BlogQuery(String title, Long typeId, boolean recommend) {
        this.title = title;
        this.typeId = typeId;
        this.recommend = recommend;
    }
}



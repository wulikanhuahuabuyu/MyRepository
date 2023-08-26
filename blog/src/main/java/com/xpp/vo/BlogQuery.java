package com.xpp.vo;

/**
 * 博客详情列表搜索数据实体类
 */
public class BlogQuery {

    private String title;       //标题
    private Long categoryId;    //分类id
    private boolean recommend;  //是否推荐

    public BlogQuery() {
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Long getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(Long categoryId) {
        this.categoryId = categoryId;
    }

    public boolean isRecommend() {
        return recommend;
    }

    public void setRecommend(boolean recommend) {
        this.recommend = recommend;
    }
}

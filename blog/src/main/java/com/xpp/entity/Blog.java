package com.xpp.entity;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/*
@Temporal()注解的使用:
　　第一种：@Temporal(TemporalType.DATE)——>实体类会封装成日期“yyyy-MM-dd”的 Date类型。

　　第二种：@Temporal(TemporalType.TIME)——>实体类会封装成时间“hh-MM-ss”的 Date类型。

　　第三种：@Temporal(TemporalType.TIMESTAMP)——>实体类会封装成完整的时间“yyyy-MM-dd hh:MM:ss”的 Date类型。
 */

/**
 * 博客详情列表的实体类
 */
@Entity
@Table(name = "t_blog") //用来命名当前实体类对应数据库表的名字，并自动生成对应表
public class Blog {

    @Id                //标识为一个主键
    @GeneratedValue   //自动生成主键
    private Long id;                     //博客id
    private String title;                //标题
    //两者配合使用在数据库中生成LONGTEXT类型
    @Basic(fetch = FetchType.LAZY)  //懒加载
    @Lob    //指定持久属性或字段应作为大对象持久保存到数据库支持的大对象类型。
    private String content;              //内容
    private String firstPicture;         //首图
    private String flag;                 //标记
    private Integer views;               //浏览次数
    private boolean appreciation;        //赞赏是否开启
    private boolean reprintedStatement;  //转载声明是否开启
    private boolean commentAbled;        //评论功能是否启用
    private boolean published;           //是否发布
    private boolean recommend;           //是否推荐
    private String description;          //博客描述
    @Temporal(TemporalType.TIMESTAMP)
    private Date createdTime;            //创建时间
    @Temporal(TemporalType.TIMESTAMP)
    private Date updatedTime;            //更新时间

    //多个博客详情列表对应一个分类
    @ManyToOne
    private Category category;

    //多个博客详情列表对应多个标签
    @ManyToMany(cascade = {CascadeType.PERSIST})//级联新增(保存),每增加一个tags的同时，tags会保存到数据库中
    private List<Tag> tags = new ArrayList<>();

    //多个博客详情列表对应一个用户
    @ManyToOne
    private User user;

    //一个详情列表对应多条评论
    @OneToMany(mappedBy = "blog")
    private List<Comment> comments = new ArrayList<>();

    @Transient  //不会映射到数据库的表字段
    private String tagIds;


    public Blog() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getFirstPicture() {
        return firstPicture;
    }

    public void setFirstPicture(String firstPicture) {
        this.firstPicture = firstPicture;
    }

    public String getFlag() {
        return flag;
    }

    public void setFlag(String flag) {
        this.flag = flag;
    }

    public Integer getViews() {
        return views;
    }

    public void setViews(Integer views) {
        this.views = views;
    }

    public boolean isAppreciation() {
        return appreciation;
    }

    public void setAppreciation(boolean appreciation) {
        this.appreciation = appreciation;
    }

    public boolean isReprintedStatement() {
        return reprintedStatement;
    }

    public void setReprintedStatement(boolean reprintedStatement) {
        this.reprintedStatement = reprintedStatement;
    }

    public boolean isCommentAbled() {
        return commentAbled;
    }

    public void setCommentAbled(boolean commentAbled) {
        this.commentAbled = commentAbled;
    }

    public boolean isPublished() {
        return published;
    }

    public void setPublished(boolean published) {
        this.published = published;
    }

    public boolean isRecommend() {
        return recommend;
    }

    public void setRecommend(boolean recommend) {
        this.recommend = recommend;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Date getCreatedTime() {
        return createdTime;
    }

    public void setCreatedTime(Date createdTime) {
        this.createdTime = createdTime;
    }

    public Date getUpdatedTime() {
        return updatedTime;
    }

    public void setUpdatedTime(Date updatedTime) {
        this.updatedTime = updatedTime;
    }

    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        this.category = category;
    }

    public List<Tag> getTags() {
        return tags;
    }

    public void setTags(List<Tag> tags) {
        this.tags = tags;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public List<Comment> getComments() {
        return comments;
    }

    public void setComments(List<Comment> comments) {
        this.comments = comments;
    }

    public String getTagIds() {
        return tagIds;
    }

    public void setTagIds(String tagIds) {
        this.tagIds = tagIds;
    }

    //初始化一组标签id，使前端 th:value="*{tagIds}" 能够拿到并呈现在页面对应位置
    public void init() {
        this.tagIds = tagsToIds(this.getTags());
    }

    //将一组id用逗号("，")隔开,并去除最后一个","
    //1,2,3
    private String tagsToIds(List<Tag> tags) {
        if (!tags.isEmpty()) {
            StringBuffer ids = new StringBuffer();
            for (Tag tag : tags) {
                ids.append(tag.getId());
                ids.append(",");
            }
            ids.deleteCharAt(ids.length() - 1);
            return ids.toString();
        } else {
            return tagIds;
        }
    }

    @Override
    public String toString() {
        return "Blog{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", content='" + content + '\'' +
                ", firstPicture='" + firstPicture + '\'' +
                ", flag='" + flag + '\'' +
                ", views=" + views +
                ", appreciation=" + appreciation +
                ", reprintedStatement=" + reprintedStatement +
                ", commentAbled=" + commentAbled +
                ", published=" + published +
                ", recommend=" + recommend +
                ", description='" + description + '\'' +
                ", createdTime=" + createdTime +
                ", updatedTime=" + updatedTime +
                ", category=" + category +
                ", tags=" + tags +
                ", user=" + user +
                ", comments=" + comments +
                ", tagIds='" + tagIds + '\'' +
                '}';
    }
}

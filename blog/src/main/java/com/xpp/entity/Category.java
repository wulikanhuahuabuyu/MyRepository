package com.xpp.entity;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import java.util.ArrayList;
import java.util.List;

/**
 * 分类实体类
 */
@Entity
@Table(name = "t_type")
public class Category {

    @Id
    @GeneratedValue
    private Long id;        //分类id

    //@NotBlank:只用于String,不能为null且trim()之后size>0
    @NotBlank(message = "分类名称不能为空!")
    private String name;    //分类名字

    /*
    mappedBy表示关系被维护端，只有关系端有权去更新外键。
    OneToMany默认的加载方式是赖加载。
     */
    //一个分类包含多个博客详情列表
    @OneToMany(mappedBy = "category")
    private List<Blog> blogs = new ArrayList<>();

    public Category() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<Blog> getBlogs() {
        return blogs;
    }

    public void setBlogs(List<Blog> blogs) {
        this.blogs = blogs;
    }

    @Override
    public String toString() {
        return "Category{" +
                "id=" + id +
                ", name='" + name + '\'' +
                '}';
    }
}

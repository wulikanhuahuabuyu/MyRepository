package com.xpp.service;

import com.xpp.entity.Blog;
import com.xpp.entity.Tag;
import com.xpp.vo.BlogQuery;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Map;

/**
 * 博客详情列表Service层接口
 */
public interface BlogService {
    /**
     * 根据id查询博客详情列表数据
     *
     * @param id 博客id
     * @return 博客详情列表数据
     */
    Blog getBlogById(Long id);

    /**
     * 根据id查询已转换为HTML格式的博客内容
     *
     * @param id
     * @return 由Markdown格式转换为HTML格式的博客内容
     */
    Blog getConvertedBlog(Long id);

    /**
     * 博客列表分页动态多条件查询
     *
     * @param pageable
     * @param blog
     * @return
     */
    Page<Blog> listBlog(Pageable pageable, BlogQuery blog);

    /**
     * 查询分页数据
     *
     * @param pageable
     * @return
     */
    Page<Blog> listBlog(Pageable pageable);

    /**
     * 根据标签id分页查询博客详情
     *
     * @param pageable
     * @param tagId
     * @return
     */
    Page<Blog> listBlog(Pageable pageable, Long tagId);

    /**
     * 根据搜索框中输入的内容查询分页数据
     *
     * @param query
     * @param pageable
     * @return
     */
    Page<Blog> listBlogByQuery(String query, Pageable pageable);

    /**
     * 设置最新推荐的显示条数
     *
     * @param size
     * @return
     */
    List<Blog> listOfRecommendBlogTop(Integer size);

    /**
     * 封装查询的对应年份的博客信息
     *
     * @return
     */
    Map<String, List<Blog>> archiveOfBlog();

    /**
     * 封装了博客数量
     *
     * @return
     */
    Long countOfBlog();

    /**
     * 新增博客详情列表
     *
     * @param blog 封装了博客详情列表数据
     */
    Blog saveBlog(Blog blog);

    /**
     * 根据id修改博客详情列表数据
     *
     * @param id   博客id
     * @param blog 博客详情列表数据
     */
    Blog updateBlog(Long id, Blog blog);

    /**
     * 根据博客id删除博客详情列表数据
     *
     * @param id 博客id
     */
    void deleteBlogById(Long id);

}

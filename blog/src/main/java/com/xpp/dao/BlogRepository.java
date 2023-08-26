package com.xpp.dao;

import com.xpp.entity.Blog;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 博客详情列表DAO层接口
 */
public interface BlogRepository extends JpaRepository<Blog, Long>, JpaSpecificationExecutor<Blog> {

    /**
     * 查询博客分页数据
     */
    @Query("select b from Blog b where b.recommend=true")
    List<Blog> findTop(Pageable pageable);

    /**
     * 根据搜索框中输入的内容查询分页数据
     *
     * @param query
     * @param pageable
     * @return
     */
    @Query("select b from Blog b where b.title like ?1 or b.content like ?1")
    Page<Blog> findByQuery(String query, Pageable pageable);

    /**
     * 更新博客的浏览次数
     *
     * @param id
     * @return
     */
    @Transactional
    @Modifying
    @Query("update Blog b set b.views = b.views+1 where b.id=?1")
    int updateViews(Long id);

    /**
     * 按照年份从大到小查询年份
     *
     * @return
     */
    @Query("select function('date_format',updatedTime,'%Y') as year from Blog b group by function('date_format',updatedTime,'%Y') order by year desc")
    List<String> findGroupByYears();

    /**
     * 按照年份查询博客信息
     *
     * @param year
     * @return
     */
    @Query("select b from Blog b where function('date_format',b.updatedTime,'%Y') =?1 ")
    List<Blog> findByYear(String year);
}
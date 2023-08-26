package com.xpp.dao;

import com.xpp.entity.Category;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;


/**
 * 分类DAO层接口
 */
public interface CategoryRepository extends JpaRepository<Category, Long> {
    /**
     * 根据分类名称查询分类数据
     *
     * @param name 分类名称
     * @return 分类数据
     */
    Category findByName(String name);

    /**
     * 查询分类分页数据
     *
     * @param pageable
     * @return
     */
    @Query("select c from Category c")
    //自定义查询
    List<Category> findTop(Pageable pageable);

}

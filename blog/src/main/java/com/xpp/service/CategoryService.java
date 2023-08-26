package com.xpp.service;

import com.xpp.entity.Category;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

/**
 * 分类对应Service层
 */
public interface CategoryService {

    /**
     * 新增分类
     *
     * @param category 分类数据
     * @return
     */
    Category saveCategory(Category category);

    /**
     * 根据分类id查询分类数据
     *
     * @param id 分类id
     * @return 分类数据
     */
    Category getCategoryById(Long id);

    /**
     * 根据分类名称查询分类数据
     *
     * @param name 分类名称
     * @return 分类数据
     */
    Category getCategoryByName(String name);

    /**
     * 查询分页数据
     *
     * @param pageable 分页
     * @return 分类数据
     */
    Page<Category> listCategory(Pageable pageable);

    /**
     * 获取所有分类
     *
     * @return
     */
    List<Category> listCategory();

    /**
     * 设置分类显示的条数
     *
     * @param size
     * @return
     */
    List<Category> listCategoryTop(Integer size);

    /**
     * 根据id修改分类
     *
     * @param id       分类id
     * @param category 分类数据
     * @return
     */
    Category updateCategory(Long id, Category category);

    /**
     * 根据id删除分类
     *
     * @param id
     */
    void deleteCategoryById(Long id);

}

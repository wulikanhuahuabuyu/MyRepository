package com.xpp.service;

import com.xpp.NotFoundException;
import com.xpp.dao.CategoryRepository;
import com.xpp.entity.Category;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 分类接口实现类
 */
@Service
public class CategoryServiceImpl implements CategoryService {

    @Autowired
    private CategoryRepository categoryRepository;

    @Transactional
    @Override
    public Category saveCategory(Category category) {
        return categoryRepository.save(category);
    }

    @Transactional
    @Override
    public Category getCategoryById(Long id) {
        return categoryRepository.findById(id).get();
    }

    @Override
    public Category getCategoryByName(String name) {
        return categoryRepository.findByName(name);
    }

    @Transactional
    @Override
    public Page<Category> listCategory(Pageable pageable) {
        return categoryRepository.findAll(pageable);
    }

    @Override
    public List<Category> listCategory() {
        return categoryRepository.findAll();
    }

    @Override
    public List<Category> listCategoryTop(Integer size) {
        Sort sort = new Sort(Sort.Direction.DESC, "blogs.size");
        Pageable pageable = new PageRequest(0, size, sort);
        return categoryRepository.findTop(pageable);
    }

    /**
     * 将编辑完的分类数据保存到数据库
     */
    @Transactional
    @Override
    public Category updateCategory(Long id, Category category) {

        Category cg = getCategoryById(id);
        if (cg == null) {
            throw new NotFoundException("对不起，您请求的数据不存在！");
        }
        //将category中的数据复制到cg
        BeanUtils.copyProperties(category, cg);
        return saveCategory(cg);
    }

    @Transactional
    @Override
    public void deleteCategoryById(Long id) {
        categoryRepository.deleteById(id);
    }
}

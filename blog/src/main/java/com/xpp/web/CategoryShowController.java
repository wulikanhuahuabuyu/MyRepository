package com.xpp.web;

import com.xpp.entity.Category;
import com.xpp.service.BlogService;
import com.xpp.service.CategoryService;
import com.xpp.vo.BlogQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

/**
 * 页面中分类展示控制层
 */
@Controller
public class CategoryShowController {

    @Autowired
    private CategoryService categoryService;
    @Autowired
    private BlogService blogService;

    @GetMapping("/categories/{id}")
    public String categories(@PageableDefault(size = 8, sort = ("updatedTime"), direction = Sort.Direction.DESC) Pageable pageable, @PathVariable Long id, Model model) {
        List<Category> categories = categoryService.listCategoryTop(10000);
        //第一次进入分类页面时，并不知道分类id
        if (id == -1) {
            //获取页面中第一个分类的id
            id = categories.get(0).getId();
        }
        model.addAttribute("categories", categories);
        BlogQuery blogQuery = new BlogQuery();
        blogQuery.setCategoryId(id);
        model.addAttribute("page", blogService.listBlog(pageable, blogQuery));
        model.addAttribute("activeCid", id);
        return "category";
    }
}

package com.xpp.web;

import com.xpp.service.BlogService;
import com.xpp.service.CategoryService;
import com.xpp.service.TagService;
import com.xpp.vo.BlogQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * 首页控制器
 */
@Controller
public class IndexController {

    @Autowired
    private BlogService blogService;

    @Autowired
    private CategoryService categoryService;

    @Autowired
    private TagService tagService;

    /**
     * 访问首页以及设置右边分类所呈现的数量
     *
     * @param pageable
     * @param blog
     * @param model
     * @return
     */
    @GetMapping("/")
    public String index(@PageableDefault(size = 5, sort = {"updatedTime"}, direction = Sort.Direction.DESC)
                                Pageable pageable, BlogQuery blog, Model model) {

        model.addAttribute("page", blogService.listBlog(pageable));
        model.addAttribute("categories", categoryService.listCategoryTop(6));
        model.addAttribute("tags", tagService.listTagTop(10));
        model.addAttribute("recommendBlogs", blogService.listOfRecommendBlogTop(8));

        return "index";
    }

    /**
     * 实现搜索功能
     *
     * @param pageable
     * @param query    搜索框里所输入的内容
     * @param model
     * @return
     */
    @PostMapping("/search")
    public String search(@PageableDefault(size = 5, sort = {"updatedTime"}, direction = Sort.Direction.DESC)
                                 Pageable pageable, @RequestParam String query, Model model) {
        model.addAttribute("page", blogService.listBlogByQuery("%" + query + "%", pageable));
        model.addAttribute("query", query);
        return "search";
    }

    @GetMapping("/blog/{id}")
    public String blog(@PathVariable Long id, Model model) {
        model.addAttribute("blog", blogService.getConvertedBlog(id));
        return "blog";
    }

    @GetMapping("/footer/newBlog")
    public String newBlogs(Model model) {
        model.addAttribute("newBlogs", blogService.listOfRecommendBlogTop(3));
        return "_fragments :: newBlogList";
    }

}

package com.xpp.web.admin;

import com.xpp.dao.TagRepository;
import com.xpp.entity.Blog;
import com.xpp.entity.Category;
import com.xpp.entity.Tag;
import com.xpp.entity.User;
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
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpSession;
import java.util.List;
/*
RedirectAttributes 是Spring mvc 3.1版本之后出来的一个功能，
专门用于重定向之后还能带参数跳转的的工具类
 1.addFlashAttribute(String attributeName, @Nullable Object attributeValue);
 此方法显示参数，不安全
 2.addAttribute(String attributeName, @Nullable Object attributeValue);
 此方法隐藏参数，安全
 */

/**
 * 博客管理（详情列表）控制层
 */
@Controller
@RequestMapping("/admin")
public class BlogMController {

    private static final String RELEASE = "admin/blog-input";
    private static final String BLOG_LIST = "admin/blog-management";
    private static final String REDIRECT_LIST = "redirect:/admin/blogs";

    @Autowired
    private BlogService blogService;

    @Autowired
    private CategoryService categoryService;

    @Autowired
    private TagService tagService;

    /**
     * 博客详情列表
     */
    @GetMapping("/blogs")
    public String BlogManagement(
            @PageableDefault(size = 5, sort = {"updatedTime"}, direction = Sort.Direction.DESC)
                    Pageable pageable, BlogQuery blog, Model model) {
        model.addAttribute("categories", categoryService.listCategory());
        //将查询到的信息呈现在前端页面
        model.addAttribute("page", blogService.listBlog(pageable, blog));
        //返回博客管理（详情列表）页面
        return BLOG_LIST;

    }

    /**
     * 博客详情列表局部刷新
     */
    @PostMapping("/blogs/search")
    public String Search(
            @PageableDefault(size = 5, sort = {"updatedTime"}, direction = Sort.Direction.DESC)
                    Pageable pageable, BlogQuery blog, Model model) {
        //将查询到的信息呈现在前端页面
        model.addAttribute("page", blogService.listBlog(pageable, blog));
        //局部刷新博客详情列表
        return "admin/blog-management :: blogList";
    }

    /**
     * 博客新增
     */
    @GetMapping("/blogs/input")
    public String release(Model model) {
        setCategoryAndTag(model);
        model.addAttribute("blog", new Blog());
        //转到博客发布页面
        return RELEASE;
    }

    /**
     * 博客编辑
     */
    @GetMapping("/blogs/{id}/edit")
    public String editInput(@PathVariable Long id, Model model) {
        setCategoryAndTag(model);
        Blog blog = blogService.getBlogById(id);
        blog.init();
        model.addAttribute("blog", blog);
        //转到博客发布页面
        return RELEASE;
    }

    /**
     * 博客提交
     */
    @PostMapping("/blogs")
    public String post(Blog blog, RedirectAttributes attributes, HttpSession session) {
        blog.setUser((User) session.getAttribute("user"));
        blog.setCategory(categoryService.getCategoryById(blog.getCategory().getId()));
        blog.setTags(tagService.listTagByIds(blog.getTagIds()));
        Blog b = blogService.saveBlog(blog);
        if (b == null) {
            attributes.addFlashAttribute("message", "操作失败，提交数据不能为空！");
        } else {
            attributes.addFlashAttribute("message", "操作成功！");
        }
        //转到博客详情列表页面
        return REDIRECT_LIST;
    }

    private void setCategoryAndTag(Model model) {
        model.addAttribute("categories", categoryService.listCategory());
        model.addAttribute("tags", tagService.listTag());
    }

    /**
     * 博客删除
     */
    @GetMapping("/blogs/{id}/delete")
    public String delete(@PathVariable Long id, RedirectAttributes attributes) {
        blogService.deleteBlogById(id);
        attributes.addFlashAttribute("message", "删除成功！");
        return REDIRECT_LIST;
    }
}

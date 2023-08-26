package com.xpp.web.admin;

import com.xpp.entity.Category;
import com.xpp.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.validation.Valid;

/**
 * 分类控制层
 */
@Controller
@RequestMapping("/admin")
public class CategoryController {

    @Autowired
    private CategoryService categoryService;

    /**
     * 分页查询功能
     * Pageable:用于分页信息查询的抽象接口
     */
    @GetMapping("/categories")
    public String categories(
            @PageableDefault(size = 5, sort = {"id"}, direction = Sort.Direction.DESC)
                    Pageable pageable, Model model) {
        //将查询到的分页信息呈现在前端页面
        model.addAttribute("page", categoryService.listCategory(pageable));
        //转到分页管理页面
        return "admin/type-management";
    }


    /**
     * 分类新增功能
     */
    @GetMapping("/categories/new")
    public String newAppend(Model model) {
        //将错误信息呈现在前端页面
        model.addAttribute("category", new Category());
        //转到新增页面
        return "admin/type-new";
    }


    /**
     * 提交功能
     *
     * @return
     */
    @PostMapping("/categories")
    //@Valid 用于校验   BindingResult 用于接收校验结果
    public String post(@Valid Category category, BindingResult result, RedirectAttributes attributes) {

        Category categoryName = categoryService.getCategoryByName(category.getName());
        if (categoryName != null) {
            result.rejectValue("name", "nameError", "该分类已存在，请重新添加！");
        }

        //若校验不通过
        if (result.hasErrors()) {
            //责返回新增页面
            return "admin/type-new";
        }
        Category cg = categoryService.saveCategory(category);
        if (cg == null) {
            attributes.addFlashAttribute("message", "操作失败，提交数据不能为空！");
        } else {
            attributes.addFlashAttribute("message", "操作成功！");
        }

        //转到分类管理页面
        return "redirect:/admin/categories";
    }

    /**
     * 编辑分类
     */
    @GetMapping("categories/{id}/edit")
    public String editList(@PathVariable Long id, Model model) {
        model.addAttribute("category", categoryService.getCategoryById(id));
        return "/admin/type-new";
    }

    @PostMapping("/categories/{id}")
    //@Valid 用于校验   BindingResult 用于接收校验结果
    public String post(@Valid Category category, BindingResult result, @PathVariable Long id, RedirectAttributes attributes) {

        Category categoryName = categoryService.getCategoryByName(category.getName());
        if (categoryName != null) {
            result.rejectValue("name", "nameError", "该分类已存在，请重新添加！");
        }

        //若校验不通过
        if (result.hasErrors()) {
            //责返回新增页面
            return "admin/type-new";
        }
        Category cg = categoryService.updateCategory(id, category);
        if (cg == null) {
            attributes.addFlashAttribute("message", "更新失败,提交数据不能为空！");
        } else {
            attributes.addFlashAttribute("message", "更新成功！");
        }

        //转到分类管理页面
        return "redirect:/admin/categories";
    }

    /**
     * 删除分类
     */
    @GetMapping("/categories/{id}/delete")
    public String deleteList(@PathVariable Long id, RedirectAttributes attributes) {
        categoryService.deleteCategoryById(id);
        attributes.addFlashAttribute("message", "删除成功");
        return "redirect:/admin/categories";
    }


}

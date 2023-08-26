package com.xpp.web.admin;

import com.xpp.entity.Tag;
import com.xpp.service.TagService;
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
 * 标签控制层
 */
@Controller
@RequestMapping("/admin")
public class TagController {
    @Autowired
    private TagService tagService;

    /**
     * 分页查询功能
     * Pageable:用于分页信息查询的抽象接口
     */
    @GetMapping("/tags")
    public String tags(
            @PageableDefault(size = 3, sort = {"id"}, direction = Sort.Direction.DESC)
                    Pageable pageable, Model model) {
        //将查询到的分页信息呈现在前端页面
        model.addAttribute("page", tagService.listTag(pageable));
        //转到分页管理页面
        return "admin/tag-management";
    }


    /**
     * 标签新增功能
     */
    @GetMapping("/tags/new")
    public String newAppend(Model model) {
        //将错误信息呈现在前端页面
        model.addAttribute("tag", new Tag());
        //转到新增页面
        return "admin/tag-new";
    }

    /**
     * 提交功能
     */
    @PostMapping("/tags")
    //@Valid 用于校验   BindingResult 用于接收校验结果
    public String post(@Valid Tag tag, BindingResult result, RedirectAttributes attributes) {

        Tag tagName = tagService.getTagByName(tag.getName());
        if (tagName != null) {
            result.rejectValue("name", "nameError", "该标签已存在，请重新添加！");
        }

        //若校验不通过
        if (result.hasErrors()) {
            //责返回新增页面
            return "admin/tag-new";
        }
        Tag t = tagService.saveTag(tag);
        if (t == null) {
            attributes.addFlashAttribute("message", "操作失败，提交数据不能为空！");
        } else {
            attributes.addFlashAttribute("message", "操作成功！");
        }

        //转到分类管理页面
        return "redirect:/admin/tags";
    }

    /**
     * 编辑标签
     */
    @GetMapping("tags/{id}/edit")
    public String editList(@PathVariable Long id, Model model) {
        model.addAttribute("tag", tagService.getTagById(id));
        return "admin/tag-new";
    }

    @PostMapping("/tags/{id}")
    //@Valid 用于校验   BindingResult 用于接收校验结果
    public String post(@Valid Tag tag, BindingResult result, @PathVariable Long id, RedirectAttributes attributes) {

        Tag tagName = tagService.getTagByName(tag.getName());
        if (tagName != null) {
            result.rejectValue("name", "nameError", "该标签已存在，请重新添加！");
        }

        //若校验不通过
        if (result.hasErrors()) {
            //责返回新增页面
            return "admin/tag-new";
        }
        Tag t = tagService.updateTagById(id, tag);
        if (t == null) {
            attributes.addFlashAttribute("message", "更新失败,提交数据不能为空！");
        } else {
            attributes.addFlashAttribute("message", "更新成功！");
        }

        //返回标签管理页面
        return "redirect:/admin/tags";
    }

    /**
     * 删除标签
     */
    @GetMapping("/tags/{id}/delete")
    public String deleteList(@PathVariable Long id, RedirectAttributes attributes) {
        tagService.deleteTagById(id);
        attributes.addFlashAttribute("message", "删除成功");
        return "redirect:/admin/tags";
    }

}

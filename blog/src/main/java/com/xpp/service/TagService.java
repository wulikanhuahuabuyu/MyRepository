package com.xpp.service;

import com.xpp.entity.Tag;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

/**
 * 博客标签Service层接口
 */
public interface TagService {

    /**
     * 新增标签数据
     */
    Tag saveTag(Tag tag);

    /**
     * 通过标签id查询标签数据
     */
    Tag getTagById(Long id);

    /**
     * 通过标签名查询标签数据
     */
    Tag getTagByName(String name);

    /**
     * 分页查询标签数据
     */
    Page<Tag> listTag(Pageable pageable);

    /**
     * 获取所有标签数据
     */
    List<Tag> listTag();

    /**
     * 设置标签显示条数
     */
    List<Tag> listTagTop(Integer size);

    /**
     * 根据一组id获取标签数据
     */
    List<Tag> listTagByIds(String ids);

    /**
     * 通过标签id修改标签数据
     */
    Tag updateTagById(Long id, Tag tag);

    /**
     * 通过标签id删除标签数据
     */
    void deleteTagById(Long id);

}

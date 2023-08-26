package com.xpp.service;

import com.xpp.NotFoundException;
import com.xpp.dao.TagRepository;
import com.xpp.entity.Category;
import com.xpp.entity.Tag;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * 标签Service层接口实现类
 */
@Service
public class TagServiceImpl implements TagService {

    @Autowired
    TagRepository tagRepository;

    @Transactional
    @Override
    public Tag saveTag(Tag tag) {
        return tagRepository.save(tag);
    }

    @Transactional
    @Override
    public Tag getTagById(Long id) {
        return tagRepository.findById(id).get();
    }

    @Override
    public Tag getTagByName(String name) {
        return tagRepository.findByname(name);
    }

    @Transactional
    @Override
    public Page<Tag> listTag(Pageable pageable) {
        return tagRepository.findAll(pageable);
    }

    @Override
    public List<Tag> listTag() {
        return tagRepository.findAll();
    }

    @Override
    public List<Tag> listTagTop(Integer size) {
        Sort sort = new Sort(Sort.Direction.DESC, "tags.size");
        Pageable pageable = new PageRequest(0, size, sort);
        return tagRepository.findAll();
    }

    @Override
    public List<Tag> listTagByIds(String ids) {
        return tagRepository.findAllById(convertToList(ids));
    }

    private List<Long> convertToList(String ids) {
        List<Long> list = new ArrayList<>();
        if (!"".equals(ids) && ids != null) {
            String[] id_array = ids.split(",");
            for (int i = 0; i < id_array.length; i++) {
                list.add(new Long(id_array[i]));
            }
        }
        return list;
    }

    @Transactional
    @Override
    public Tag updateTagById(Long id, Tag tag) {
        Tag t = getTagById(id);
        if (t == null) {
            throw new NotFoundException("对不起，您请求的数据不存在！");
        }
        //将tag中的数据复制到t
        BeanUtils.copyProperties(tag, t);
        return saveTag(t);
    }

    @Transactional
    @Override
    public void deleteTagById(Long id) {
        tagRepository.deleteById(id);
    }
}

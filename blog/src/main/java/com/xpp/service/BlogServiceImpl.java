package com.xpp.service;

import com.xpp.NotFoundException;
import com.xpp.dao.BlogRepository;
import com.xpp.entity.Blog;
import com.xpp.entity.Category;
import com.xpp.entity.Tag;
import com.xpp.util.MarkdownUtils;
import com.xpp.vo.BlogQuery;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.criteria.*;
import java.util.*;

/**
 * 博客详情列表Service层接口实现类
 */
@Service
public class BlogServiceImpl implements BlogService {

    @Autowired
    BlogRepository blogRepository;

    @Override
    public Blog getBlogById(Long id) {
        return blogRepository.findById(id).get();
    }

    @Transactional
    @Override
    public Blog getConvertedBlog(Long id) {
        Blog blog = getBlogById(id);
        if (blog == null) {
            throw new NotFoundException("该博客不存在！");
        }
        //为了避免影响数据库中的content
        Blog b = new Blog();
        BeanUtils.copyProperties(blog, b);
        String content = b.getContent();
        b.setContent(MarkdownUtils.markToHtmlExtensions(content));
        blogRepository.updateViews(id);
        return b;
    }

    @Override
    public Page<Blog> listBlog(Pageable pageable, BlogQuery blog) {
        return blogRepository.findAll(new Specification<Blog>() {
            @Override
            public Predicate toPredicate(Root<Blog> root, CriteriaQuery<?> cq, CriteriaBuilder cb) {
                List<Predicate> predicates = new ArrayList<>();
                if (!"".equals(blog.getTitle()) && blog.getTitle() != null) {
                    //相当于数据库中的 like 模糊查询,查找与标题相对应的博客详情列表
                    predicates.add(cb.like(root.get("title"), "%" + blog.getTitle() + "%"));
                }
                if (blog.getCategoryId() != null) {
                    //相当于数据库中的 = 运算，同过分类id查找与之对应的博客详情列表
                    predicates.add(cb.equal(root.<Category>get("category").get("id"), blog.getCategoryId()));
                }
                if (blog.isRecommend()) {
                    //通过true/false判断是否推荐
                    predicates.add(cb.equal(root.<Boolean>get("recommend"), blog.isRecommend()));
                }
                //想当于数据库中的where，添加以上条件
                cq.where(predicates.toArray(new Predicate[predicates.size()]));
                return null;
            }
        }, pageable);
    }

    @Override
    public Page<Blog> listBlog(Pageable pageable) {
        return blogRepository.findAll(pageable);
    }

    @Override
    public Page<Blog> listBlog(Pageable pageable, Long tagId) {
        return blogRepository.findAll(new Specification<Blog>() {
            @Override
            public Predicate toPredicate(Root<Blog> root, CriteriaQuery<?> cq, CriteriaBuilder cb) {
                Join join = root.join("tags");
                return cb.equal(join.get("id"), tagId);
            }
        }, pageable);
    }

    @Override
    public Page<Blog> listBlogByQuery(String query, Pageable pageable) {
        return blogRepository.findByQuery(query, pageable);
    }

    @Override
    public List<Blog> listOfRecommendBlogTop(Integer size) {
        Sort sort = new Sort(Sort.Direction.DESC, "updatedTime");
        Pageable pageable = new PageRequest(0, size, sort);
        return blogRepository.findAll();
    }

    @Override
    public Map<String, List<Blog>> archiveOfBlog() {
        List<String> years = blogRepository.findGroupByYears();
        Map<String, List<Blog>> map = new HashMap<>();
        for (String year : years) {
            map.put(year, blogRepository.findByYear(year));
        }
        return map;
    }

    @Override
    public Long countOfBlog() {
        return blogRepository.count();
    }

    @Transactional
    @Override
    public Blog saveBlog(Blog blog) {
        //设置新增博客时的相关属性
        if (blog.getId() == null) {
            blog.setCreatedTime(new Date());
            blog.setUpdatedTime(new Date());
            blog.setViews(0);
        } else {//设置编辑博客后的相关属性
            blog.setCreatedTime(getBlogById(blog.getId()).getCreatedTime());
            blog.setUpdatedTime(new Date());
            blog.setViews(getBlogById(blog.getId()).getViews());
        }

        return blogRepository.save(blog);
    }

    @Transactional
    @Override
    public Blog updateBlog(Long id, Blog blog) {
        Blog b = getBlogById(id);
        if (b == null) {
            throw new NotFoundException("该博客不存在！");
        }
        BeanUtils.copyProperties(blog, b);
        return saveBlog(b);
    }

    @Transactional
    @Override
    public void deleteBlogById(Long id) {
        blogRepository.deleteById(id);
    }
}

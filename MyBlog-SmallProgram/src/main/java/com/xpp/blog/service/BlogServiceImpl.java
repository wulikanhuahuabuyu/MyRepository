package com.xpp.blog.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.xpp.blog.entity.Blog;
import com.xpp.blog.mapper.BlogMapper;
@Service
public class BlogServiceImpl implements BlogService{
	@Autowired
	BlogMapper mapper;
	
	@Override
	public void saveNewBlog(Blog blog) {
		 mapper.addNewBlog(blog);
	}

	@Override
	public List<Blog> getAllBlog() {
		return mapper.findAllBlog();
	}

	@Transactional
	@Override
	public void modifiedPraise(Long id,Integer praise,Integer isPraise) {
		Integer row = mapper.updatePraise(id,praise,isPraise);
	}

	@Override
	public Blog getBlogByIdAndOpenid(Long id, String openId) {
		return mapper.findBlogByIdAndOpenid(id, openId);
	}

	@Transactional
	@Override
	public void modifiedViews(Long id, String openId) {
		Integer row = mapper.updateViews(id, openId);
	}

	@Transactional
	@Override
	public void modifiedCollect(Long id, Integer isCollect) {
		Integer row = mapper.updateCollect(id, isCollect);
	}

	@Override
	public List<Blog> getCollectBlog(String openId) {
		return mapper.findCollectBlog(openId);
	}

}

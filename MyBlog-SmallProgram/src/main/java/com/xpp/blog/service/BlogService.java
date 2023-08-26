package com.xpp.blog.service;

import java.util.List;

import com.xpp.blog.entity.Blog;

public interface BlogService {
	void saveNewBlog(Blog blog);
	
	List<Blog> getAllBlog();
	
	Blog getBlogByIdAndOpenid(Long id, String openId);
	
	void modifiedViews(Long id,String openId);
	
	void modifiedPraise(Long id,Integer praise,Integer isPraise);
	
	void modifiedCollect(Long id,Integer isCollect);
	
	List<Blog> getCollectBlog(String openId);
}

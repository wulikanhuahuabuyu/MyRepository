package com.xpp.blog.mapper;

import java.util.List;

import com.xpp.blog.entity.Blog;

public interface BlogMapper {
	Integer addNewBlog(Blog blog);

	/**
	 * 根据用户唯一标识openid获取其博客列表
	 */
	List<Blog> findAllBlog();
	
	/**
	 * 根据博客id和用户openid获取对应博客详情
	 */
	Blog findBlogByIdAndOpenid(Long id,String openId);
	
	/**
	 * 根据博客id和用户openid更改博客的浏览数量
	 */
	Integer updateViews(Long id,String openId);
	
	Integer updatePraise(Long id,Integer praise,Integer isPraise);
	
	Integer updateCollect(Long id,Integer isCollect);
	
	//根据openid获取该用户收藏的blog
	List<Blog> findCollectBlog(String openId);
}

package com.xpp.blog.mapper;

import com.xpp.blog.entity.User;

public interface UserMapper {
	Integer addNewUser(User user);
	
	User findByOpenId(String openId);
	
}

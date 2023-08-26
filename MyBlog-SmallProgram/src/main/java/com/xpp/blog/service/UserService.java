package com.xpp.blog.service;

import com.xpp.blog.entity.User;

public interface UserService {
	void saveUser(User user);
	
	User getByOpenId(String openId);
	
	
}

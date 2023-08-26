package com.xpp.blog.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.xpp.blog.entity.User;
import com.xpp.blog.mapper.UserMapper;

@Service
public class UserServiceImpl implements UserService{
	
	@Autowired
	UserMapper mapper;
	
	@Override
	public void saveUser(User user) {
		//校验该用户是否已存在
		String openId = user.getOpenId();
			
		User result = getByOpenId(openId);
		
		if(result==null) {
			Integer row = mapper.addNewUser(user);
		}
		
	}

	@Override
	public User getByOpenId(String openId) {
		
		return mapper.findByOpenId(openId);
	}
	

}

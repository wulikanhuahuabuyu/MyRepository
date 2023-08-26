package com.xpp.blog.controller;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.xpp.blog.entity.User;
import com.xpp.blog.service.UserService;

@RestController
@RequestMapping("user")
public class UserController extends BaseController{

	@Autowired
	UserService service;

	/**
	 * 接收小程序传过来的用户数据
	 */
	@PostMapping("saveUser")
	public void getUserInfo(HttpServletRequest request) {
		User user = new User();
		user.setOpenId(request.getParameter("openId"));
		user.setNickName(request.getParameter("nickName"));
		user.setAvatar(request.getParameter("avatar"));
		user.setGender(Integer.valueOf(request.getParameter("gender")));
		user.setCity(request.getParameter("city"));
		user.setCountry(request.getParameter("country"));
		user.setProvince(request.getParameter("province"));
		user.setLanguage(request.getParameter("language"));
		user.setCreatedUser(request.getParameter("nickName"));
		user.setCreatedTime(new Date());
		service.saveUser(user);
	}
}

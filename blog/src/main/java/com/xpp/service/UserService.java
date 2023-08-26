package com.xpp.service;

import com.xpp.entity.User;

/**
 * 用户功能对应的Service层
 */
public interface UserService {
    /**
     * 检查用户登录
     *
     * @param username 登录用户名
     * @param password 登录密码
     * @return 封装了用户数据
     */
    User checkUser(String username, String password);
}

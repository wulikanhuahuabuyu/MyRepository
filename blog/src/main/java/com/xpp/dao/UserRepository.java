package com.xpp.dao;

import com.xpp.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * 用户功能的DAO层接口
 */
//JpaRepository<操作数据库对象, 主键类型>
public interface UserRepository extends JpaRepository<User, Long> {

    /**
     * 查询数据库中的用户名和密码
     *
     * @param username
     * @param password
     * @return
     */
    User findByUsernameAndPassword(String username, String password);


}

package com.atguigu.dao;

import com.atguigu.pojo.User;

/**
 * @Author ZhangRuJian
 * @Data 2022/1/12
 */

public interface UserDao {

    User findUserByUsername(String username);
}

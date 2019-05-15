package com.springboot.dao;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.springboot.model.User;
import org.apache.ibatis.annotations.Param;

import java.util.Set;

public interface UserDao extends BaseMapper<User> {
    Set<String> getRolesByUsername(@Param("username") String username);

    Set<String> getMenusByUsername(@Param("username") String username);
}
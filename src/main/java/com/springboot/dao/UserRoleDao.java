package com.springboot.dao;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.springboot.model.UserRole;
import org.apache.ibatis.annotations.Param;

public interface UserRoleDao extends BaseMapper<UserRole> {
    Integer[] getRoleIdsByUserId(@Param("userId") long userId);
}

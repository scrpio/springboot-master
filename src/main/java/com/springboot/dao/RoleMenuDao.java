package com.springboot.dao;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.springboot.model.RoleMenu;
import org.apache.ibatis.annotations.Param;

public interface RoleMenuDao extends BaseMapper<RoleMenu> {
    Integer[] getMenuIdsByRoleId(@Param("roleId") int roleId);
}

package com.springboot.service;

import com.baomidou.mybatisplus.service.IService;
import com.springboot.model.RoleMenu;

public interface IRoleMenuService extends IService<RoleMenu> {
    Integer[] getMenuIdsByRoleId(int roleId);
}

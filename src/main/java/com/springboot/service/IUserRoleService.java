package com.springboot.service;

import com.baomidou.mybatisplus.service.IService;
import com.springboot.model.UserRole;

public interface IUserRoleService extends IService<UserRole> {
    Integer[] getRoleIdsByUserId(long userId);
}

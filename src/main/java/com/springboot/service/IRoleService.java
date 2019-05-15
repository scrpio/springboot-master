package com.springboot.service;

import com.baomidou.mybatisplus.service.IService;
import com.springboot.model.Role;
import org.springframework.transaction.annotation.Transactional;

public interface IRoleService extends IService<Role> {
    @Transactional
    int addRole(Role role);

    @Transactional
    int editRole(Role role);

    @Transactional
    int removeRole(int id);
}

package com.springboot.service.impl;

import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.springboot.dao.RoleMenuDao;
import com.springboot.model.RoleMenu;
import com.springboot.service.IRoleMenuService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class RoleMenuServiceImpl extends ServiceImpl<RoleMenuDao, RoleMenu> implements IRoleMenuService {
    @Autowired
    private RoleMenuDao roleMenuDao;

    @Override
    public Integer[] getMenuIdsByRoleId(int roleId) {
        return roleMenuDao.getMenuIdsByRoleId(roleId);
    }
}

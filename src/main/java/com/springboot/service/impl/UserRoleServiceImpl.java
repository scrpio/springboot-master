package com.springboot.service.impl;

import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.springboot.dao.UserRoleDao;
import com.springboot.model.UserRole;
import com.springboot.service.IUserRoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserRoleServiceImpl extends ServiceImpl<UserRoleDao, UserRole> implements IUserRoleService {
    @Autowired
    private UserRoleDao userRoleDao;

    @Override
    public Integer[] getRoleIdsByUserId(long userId) {
        return userRoleDao.getRoleIdsByUserId(userId);
    }
}

package com.springboot.service.impl;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.mapper.Wrapper;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.springboot.common.enums.TransCode;
import com.springboot.common.exception.TransException;
import com.springboot.common.shiro.ShiroKit;
import com.springboot.dao.UserDao;
import com.springboot.dao.UserRoleDao;
import com.springboot.model.User;
import com.springboot.model.UserRole;
import com.springboot.service.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Set;

@Service
public class UserServiceImpl extends ServiceImpl<UserDao, User> implements IUserService {
    @Autowired
    private UserDao userDao;
    @Autowired
    private UserRoleDao userRoleDao;

    @Override
    public Set<String> getRolesByUsername(String username) {
        return userDao.getRolesByUsername(username);
    }

    @Override
    public Set<String> getMenusByUsername(String username) {
        return userDao.getMenusByUsername(username);
    }

    @Override
    public User getUserByUsername(String username) {
        EntityWrapper<User> entity = new EntityWrapper<>();
        Wrapper<User> wrapper = entity.eq("username", username).and().ne("status", 3);
        List<User> list = userDao.selectList(wrapper);
        if (list == null || list.size() == 0) {
            return null;
        } else {
            return list.get(0);
        }
    }

    @Override
    public User getUserByPhone(String phone) {
        EntityWrapper<User> entity = new EntityWrapper<>();
        Wrapper<User> wrapper = entity.eq("phone", phone).and().ne("status", 3);
        List<User> list = userDao.selectList(wrapper);
        if (list == null || list.size() == 0) {
            return null;
        } else {
            return list.get(0);
        }
    }
    public int addUser(User user) {
        user.setPassword(ShiroKit.encrypt(user.getUsername(), user.getPassword()));
        user.setCreateTime(new Date());
        int result = userDao.insert(user);
        if (result != 1) {
            throw new TransException(TransCode.INSERT_FAIL);
        }
        addUserRole(user.getId(), user.getRoleIds());
        return result;
    }

    public int editUser(User user) {
        if (!user.getPassword().isEmpty()) {
            user.setPassword(ShiroKit.encrypt(user.getUsername(), user.getPassword()));
        }
        int result = userDao.updateById(user);
        if (result != 1) {
            throw new TransException(TransCode.UPDATE_FAIL);
        }
        updateUserRole(user.getId(), user.getRoleIds());
        return result;
    }

    public int removeUser(long id) {
        EntityWrapper<UserRole> entity = new EntityWrapper<>();
        Wrapper<UserRole> wrapper = entity.eq("user_id", id);
        if (userRoleDao.selectCount(wrapper) > 0 && userRoleDao.delete(wrapper) <= 0) {
            throw new TransException(TransCode.DELETE_FAIL);
        }
        int result = userDao.deleteById(id);
        if (result != 1) {
            throw new TransException(TransCode.DELETE_FAIL);
        }
        return result;
    }

    private void addUserRole(long userId, Integer[] roleIds) {
        UserRole userRole = new UserRole();
        for (int id : roleIds) {
            userRole.setUserId(userId);
            userRole.setRoleId(id);
            if (userRoleDao.insert(userRole) != 1) {
                throw new TransException(TransCode.INSERT_FAIL);
            }
        }
    }

    private void updateUserRole(long userId, Integer[] roleIds) {
        EntityWrapper<UserRole> entity = new EntityWrapper<>();
        Wrapper<UserRole> wrapper = entity.eq("user_id", userId);
        if (userRoleDao.selectCount(wrapper) > 0 && userRoleDao.delete(wrapper) <= 0) {
            throw new TransException(TransCode.DELETE_FAIL);
        }
        addUserRole(userId, roleIds);
    }
}

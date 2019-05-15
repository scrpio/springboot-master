package com.springboot.service;

import com.baomidou.mybatisplus.service.IService;
import com.springboot.model.User;
import org.springframework.transaction.annotation.Transactional;

import java.util.Set;

public interface IUserService extends IService<User> {
    Set<String> getRolesByUsername(String username);

    Set<String> getMenusByUsername(String username);

    User getUserByUsername(String username);

    User getUserByPhone(String phone);

    @Transactional
    int addUser(User user);

    @Transactional
    int editUser(User user);

    @Transactional
    int removeUser(long id);
}

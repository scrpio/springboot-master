package com.springboot.service.impl;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.mapper.Wrapper;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.springboot.common.enums.TransCode;
import com.springboot.common.exception.TransException;
import com.springboot.dao.RoleDao;
import com.springboot.dao.RoleMenuDao;
import com.springboot.dao.UserRoleDao;
import com.springboot.model.Role;
import com.springboot.model.RoleMenu;
import com.springboot.model.UserRole;
import com.springboot.service.IRoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class RoleServiceImpl extends ServiceImpl<RoleDao, Role> implements IRoleService {
    @Autowired
    private RoleDao roleDao;
    @Autowired
    private RoleMenuDao roleMenuDao;
    @Autowired
    private UserRoleDao userRoleDao;

    @Override
    public int addRole(Role role) {
        int result = roleDao.insert(role);
        if (result != 1) {
            throw new TransException(TransCode.INSERT_FAIL);
        }
        addRoleMenu(role.getId(), role.getMenuIds());
        return result;
    }

    @Override
    public int editRole(Role role) {
        int result = roleDao.updateById(role);
        if (result != 1) {
            throw new TransException(TransCode.UPDATE_FAIL);
        }
        updateRoleMenu(role.getId(), role.getMenuIds());
        return result;
    }

    @Override
    public int removeRole(int id) {
        EntityWrapper<UserRole> entity = new EntityWrapper<>();
        EntityWrapper<RoleMenu> roleMenuEntity = new EntityWrapper<>();
        Wrapper<UserRole> wrapper = entity.eq("role_id", id);
        Wrapper<RoleMenu> roleMenuWrapper = roleMenuEntity.eq("role_id", id);
        if (userRoleDao.selectCount(wrapper) > 0 && userRoleDao.delete(wrapper) <= 0) {
            throw new TransException(TransCode.DELETE_FAIL);
        }
        if (roleMenuDao.selectCount(roleMenuWrapper) > 0 && roleMenuDao.delete(roleMenuWrapper) <= 0) {
            throw new TransException(TransCode.DELETE_FAIL);
        }
        int result = roleDao.deleteById(id);
        if (result != 1) {
            throw new TransException(TransCode.DELETE_FAIL);
        }
        return result;
    }

    private void addRoleMenu(int roleId, Integer[] menuIds) {
        for (int id : menuIds) {
            RoleMenu roleMenu = new RoleMenu();
            roleMenu.setMenuId(id);
            roleMenu.setRoleId(roleId);
            if (roleMenuDao.insert(roleMenu) != 1) {
                throw new TransException(TransCode.INSERT_FAIL);
            }
        }
    }

    private void updateRoleMenu(int roleId, Integer[] menuIds) {
        EntityWrapper<RoleMenu> entity = new EntityWrapper<>();
        Wrapper<RoleMenu> wrapper = entity.eq("role_id", roleId);
        if (roleMenuDao.selectCount(wrapper) > 0 && roleMenuDao.delete(wrapper) <= 0) {
            throw new TransException(TransCode.DELETE_FAIL);
        }
        addRoleMenu(roleId, menuIds);
    }
}

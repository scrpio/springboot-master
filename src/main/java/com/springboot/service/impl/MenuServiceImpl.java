package com.springboot.service.impl;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.mapper.Wrapper;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.google.gson.Gson;
import com.springboot.common.enums.TransCode;
import com.springboot.common.exception.TransException;
import com.springboot.common.shiro.ShiroKit;
import com.springboot.common.util.BuildTree;
import com.springboot.dao.MenuDao;
import com.springboot.dao.RoleMenuDao;
import com.springboot.model.Menu;
import com.springboot.model.RoleMenu;
import com.springboot.model.node.Router;
import com.springboot.model.node.Tree;
import com.springboot.service.IMenuService;
import com.springboot.service.IRedisService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
public class MenuServiceImpl extends ServiceImpl<MenuDao, Menu> implements IMenuService {
    @Autowired
    private MenuDao menuDao;
    @Autowired
    private RoleMenuDao roleMenuDao;
    @Autowired
    private IRedisService redisService;

    @Override
    public List<Menu> userMenus(Long userId) {
        return menuDao.listMenuByUserId(userId);
    }

    @Override
    public Tree<Menu> getTree() {
        List<Tree<Menu>> trees = new ArrayList<Tree<Menu>>();
        EntityWrapper<Menu> entity = new EntityWrapper<>();
        List<Menu> menus = menuDao.selectList(entity);
        for (Menu menu : menus) {
            Tree<Menu> tree = new Tree<Menu>();
            tree.setId(menu.getId().toString());
            tree.setParentId(menu.getParentId().toString());
            tree.setText(menu.getName());
            tree.setObject(menu);
            trees.add(tree);
        }
        // 默认顶级菜单为０，根据数据库实际情况调整
        Tree<Menu> t = BuildTree.build(trees);
        return t;
    }

    @Override
    public List<Router> RoutersByUserId(Long userId) {
        List<Menu> menus = userMenus(userId);
        List<Router> routers = new ArrayList<>();
        for (Menu menu : menus) {
            Router router = new Router();
            router.setId(menu.getId());
            router.setName(menu.getName());
            router.setPath(menu.getUrl());
            router.setIconCls(menu.getIcon());
            router.setParentId(menu.getParentId());
            router.setMenuShow(menu.isMenuShow());
            router.setChildren(new ArrayList<>());
            router.setLeaf(menu.isLeaf());
            routers.add(router);
        }
        return Router.buildList(routers, 0);
    }

    @Override
    public List<String> PermsByUserId(Long userId) {
        List<String> permsList = new ArrayList<>();
        List<Menu> menus = userMenus(userId);
        for (Menu menu : menus) {
            if (menu.getPerms() != null && "" != menu.getPerms()) {
                permsList.add(menu.getPerms());
            }
        }
        return permsList;
    }

    @Override
    public int addMenu(Menu menu) {
        menu.setCreateTime(new Date());
        int result = menuDao.insert(menu);
        if (result != 1) {
            throw new TransException(TransCode.INSERT_FAIL);
        }
        updateRouters();
        updatePerms();
        return result;
    }

    @Override
    public int editMenu(Menu menu) {
        menu.setModifiedTime(new Date());
        int result = menuDao.updateById(menu);
        if (result != 1) {
            throw new TransException(TransCode.UPDATE_FAIL);
        }
        updateRouters();
        updatePerms();
        return result;
    }

    @Override
    public int removeMenu(int id) {
        EntityWrapper<RoleMenu> entity = new EntityWrapper<>();
        Wrapper<RoleMenu> wrapper = entity.eq("menu_id", id);
        if (roleMenuDao.selectCount(wrapper) > 0 && roleMenuDao.delete(wrapper) <= 0) {
            throw new TransException(TransCode.DELETE_FAIL);
        }
        if (menuDao.deleteById(id) != 1) {
            throw new TransException(TransCode.DELETE_FAIL);
        }
        updateRouters();
        updatePerms();
        return 1;
    }

    private void updateRouters() {
        if (ShiroKit.getUser() != null) {
            Long userId = ShiroKit.getUser().getId();
            List<Router> routers = RoutersByUserId(userId);
            redisService.set("user:router:", new Gson().toJson(routers));
        }
    }

    private void updatePerms() {
        if (ShiroKit.getUser() != null) {
            Long userId = ShiroKit.getUser().getId();
            List<String> perms = PermsByUserId(userId);
            redisService.set("user:perms:", new Gson().toJson(perms));
        }
    }
}

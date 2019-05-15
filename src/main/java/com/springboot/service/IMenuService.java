package com.springboot.service;

import com.baomidou.mybatisplus.service.IService;
import com.springboot.model.Menu;
import com.springboot.model.node.Router;
import com.springboot.model.node.Tree;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface IMenuService extends IService<Menu> {
    List<Menu> userMenus(Long userId);

    Tree<Menu> getTree();

    List<Router> RoutersByUserId(Long userId);

    List<String> PermsByUserId(Long userId);

    @Transactional
    int addMenu(Menu menu);

    @Transactional
    int editMenu(Menu menu);

    @Transactional
    int removeMenu(int id);
}

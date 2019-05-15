package com.springboot.controller;

import com.springboot.common.annotation.SystemLog;
import com.springboot.common.util.ResultUtil;
import com.springboot.model.Menu;
import com.springboot.model.node.Tree;
import com.springboot.model.vo.Result;
import com.springboot.service.IMenuService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/menu")
public class MenuController {
    @Autowired
    private IMenuService menuService;

    @GetMapping("/list")
    public Result<List<Tree<Menu>>> getMenuList() {
        List<Tree<Menu>> list = menuService.getTree().getChildren();
        return new ResultUtil<List<Tree<Menu>>>().setData(list);
    }

    @SystemLog(value = "添加菜单")
    @PostMapping("/add")
    public Result<Menu> addMenu(@RequestBody Menu menu) {
        if (menuService.addMenu(menu) != 1) {
            return new ResultUtil<Menu>().setErrorMsg("添加菜单失败");
        }
        return new ResultUtil<Menu>().setSuccessMsg("添加菜单成功");
    }

    @SystemLog(value = "修改菜单信息")
    @PutMapping("/edit")
    public Result<Menu> editMenu(@RequestBody Menu menu) {
        if (menuService.editMenu(menu) != 1) {
            return new ResultUtil<Menu>().setErrorMsg("修改菜单失败");
        }
        return new ResultUtil<Menu>().setSuccessMsg("修改菜单成功");
    }

    @SystemLog(value = "删除菜单")
    @DeleteMapping("/remove")
    public Result<Menu> removeMenu(@RequestParam Integer id) {
        if (menuService.removeMenu(id) != 1) {
            return new ResultUtil<Menu>().setErrorMsg("删除菜单失败");
        }
        return new ResultUtil<Menu>().setSuccessMsg("删除菜单成功");
    }

}

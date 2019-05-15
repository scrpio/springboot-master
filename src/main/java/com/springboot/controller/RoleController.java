package com.springboot.controller;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.mapper.Wrapper;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.springboot.common.annotation.SystemLog;
import com.springboot.common.util.ResultUtil;
import com.springboot.model.Role;
import com.springboot.model.vo.Result;
import com.springboot.service.IRoleMenuService;
import com.springboot.service.IRoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/role")
public class RoleController {
    @Autowired
    private IRoleService roleService;
    @Autowired
    private IRoleMenuService roleMenuService;

    @GetMapping("/list")
    public Result<Map<String, Object>> getRoleList(@RequestParam int page, @RequestParam int limit, @RequestParam String condition) {
        Map<String, Object> result = new HashMap<>();
        PageHelper.startPage(page, limit);
        EntityWrapper<Role> entity = new EntityWrapper<>();
        Wrapper<Role> wrapper = entity.like("name", condition);
        List<Role> list = roleService.selectList(wrapper);
        PageInfo<Role> pageInfo = new PageInfo<>(list);
        result.put("list", list);
        result.put("total", pageInfo.getTotal());
        return new ResultUtil<Map<String, Object>>().setData(result);
    }

    @GetMapping("/allRole")
    public List<Role> getAllRole() {
        EntityWrapper<Role> entity = new EntityWrapper<>();
        return this.roleService.selectList(entity);
    }

    @GetMapping("/menuIds")
    public Integer[] getMenuIds(@RequestParam int roleId) {
        return roleMenuService.getMenuIdsByRoleId(roleId);
    }

    @SystemLog(value = "添加角色")
    @PostMapping("/add")
    public Result<Role> addRole(@RequestBody Role role) {
        if (roleService.addRole(role) != 1) {
            return new ResultUtil<Role>().setErrorMsg("添加角色失败");
        }
        return new ResultUtil<Role>().setSuccessMsg("添加角色成功");
    }

    @SystemLog(value = "修改角色信息")
    @PutMapping("/edit")
    public Result<Role> editRole(@RequestBody Role role) {
        if (roleService.editRole(role) != 1) {
            return new ResultUtil<Role>().setErrorMsg("修改角失败");
        }
        return new ResultUtil<Role>().setSuccessMsg("修改角成功");
    }

    @SystemLog(value = "删除角色")
    @DeleteMapping("/remove")
    public Result<Role> removeRole(@RequestParam Integer[] ids) {
        for (int id : ids) {
            if (roleService.removeRole(id) != 1) {
                return new ResultUtil<Role>().setErrorMsg("删除角色失败");
            }
        }
        return new ResultUtil<Role>().setSuccessMsg("删除角色成功");
    }
}

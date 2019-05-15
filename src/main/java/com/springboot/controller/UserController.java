package com.springboot.controller;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.mapper.Wrapper;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.springboot.common.annotation.SystemLog;
import com.springboot.common.shiro.ShiroKit;
import com.springboot.common.util.ResultUtil;
import com.springboot.model.User;
import com.springboot.model.node.Router;
import com.springboot.model.vo.Result;
import com.springboot.model.vo.UserVo;
import com.springboot.service.IMenuService;
import com.springboot.service.IRedisService;
import com.springboot.service.IUserRoleService;
import com.springboot.service.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/user")
public class UserController {
    @Autowired
    private IUserService userService;
    @Autowired
    private IUserRoleService userRoleService;
    @Autowired
    private IMenuService menuService;
    @Autowired
    private IRedisService redisService;

    private void updateCurrent(User user) {
        user.setPassword(null);
        redisService.set("current:user:", new Gson().toJson(user));
        redisService.expire("current:user:", 60 * 60 * 24);
    }

    @GetMapping("/currentUser")
    public Result<Map<String, Object>> currentUser(@RequestParam String token) {
        Map<String, Object> result = new HashMap<>();
        User user = new Gson().fromJson(redisService.get("current:user:"), User.class);
        List<Router> routers = new Gson().fromJson(redisService.get("user:router:"), new TypeToken<List<Router>>() {
        }.getType());
        List<String> perms = new Gson().fromJson(redisService.get("user:perms:"), new TypeToken<List<String>>() {
        }.getType());
        if (user == null) {
            user = userService.getUserByUsername(token);
            updateCurrent(user);
        }
        if (routers == null || routers.size() == 0) {
            routers = menuService.RoutersByUserId(user.getId());
            redisService.set("user:router:", new Gson().toJson(routers));
            redisService.expire("user:router:", 60 * 60 * 24);
        }
        if (perms == null || perms.size() == 0) {
            perms = menuService.PermsByUserId(user.getId());
            redisService.set("user:perms:", new Gson().toJson(perms));
            redisService.expire("user:perms:", 60 * 60 * 24);
        }
        result.put("user", user);
        result.put("router", routers);
        result.put("perms", perms);
        return new ResultUtil<Map<String, Object>>().setData(result);
    }

    @GetMapping("/list")
    public Result<Map<String, Object>> getUserList(@RequestParam int page, @RequestParam int limit, @RequestParam String condition) {
        Map<String, Object> result = new HashMap<>();
        PageHelper.startPage(page, limit);
        EntityWrapper<User> entity = new EntityWrapper<>();
        Wrapper<User> wrapper = entity.like("username", condition).or().like("nickname", condition);
        List<User> list = userService.selectList(wrapper);
        PageInfo<User> pageInfo = new PageInfo<>(list);
        result.put("list", list);
        result.put("total", pageInfo.getTotal());
        return new ResultUtil<Map<String, Object>>().setData(result);
    }

    @GetMapping("/all")
    public List<Map<String, Object>> getUsers() {
        EntityWrapper<User> entity = new EntityWrapper<>();
        Wrapper<User> wrapper = entity.setSqlSelect("id, username as `value`");
        return userService.selectMaps(wrapper);
    }

    @GetMapping("/roleIds")
    public Integer[] getRoleIds(@RequestParam long userId) {
        return userRoleService.getRoleIdsByUserId(userId);
    }

    @SystemLog(value = "添加用户")
    @PostMapping("/add")
    public Result<User> addUser(@RequestBody User user) {
        if (userService.getUserByUsername(user.getUsername()) != null) {
            return new ResultUtil<User>().setErrorMsg("该用户名已被注册");
        }
        if (userService.addUser(user) != 1) {
            return new ResultUtil<User>().setErrorMsg("添加用户失败");
        }
        return new ResultUtil<User>().setSuccessMsg("添加用户成功");
    }

    @SystemLog(value = "修改个人资料")
    @PatchMapping("/profile")
    public Result<Object> editProfile(@RequestBody User user) {
        if (ShiroKit.getUser() != null) {
            User oldUser = userService.selectById(ShiroKit.getUser().getId());
            user.setId(oldUser.getId());
            user.setUsername(oldUser.getUsername());
            user.setPassword(oldUser.getPassword());
            if (!this.userService.updateById(user)) {
                return new ResultUtil<Object>().setErrorMsg("修改个人资料失败");
            }
            updateCurrent(user);
            return new ResultUtil<Object>().setSuccessMsg("修改个人资料成功");
        } else {
            return new ResultUtil<Object>().setErrorMsg("登录已过期");
        }
    }

    @SystemLog(value = "修改个人密码")
    @PatchMapping("/changePwd")
    public Result<Object> changePwd(@RequestBody UserVo userVo) {
        if (ShiroKit.getUser() != null) {
            User user = userService.selectById(ShiroKit.getUser().getId());
            String credentials = ShiroKit.encrypt(userVo.getUsername(), userVo.getOldPwd());
            if (user.getPassword().equals(credentials)) {
                user.setPassword(ShiroKit.encrypt(userVo.getUsername(), userVo.getPassword()));
                if (!this.userService.updateById(user)) {
                    return new ResultUtil<Object>().setErrorMsg("修改个人密码失败");
                }
                return new ResultUtil<Object>().setSuccessMsg("修改个人密码成功");
            } else {
                return new ResultUtil<Object>().setErrorMsg("原密码错误");
            }
        } else {
            return new ResultUtil<Object>().setErrorMsg("登录已过期");
        }
    }

    @SystemLog(value = "修改用户信息")
    @PutMapping("/edit")
    public Result<User> editUser(@RequestBody User user) {
        if (userService.editUser(user) != 1) {
            return new ResultUtil<User>().setErrorMsg("修改用户失败");
        }
        return new ResultUtil<User>().setSuccessMsg("修改用户成功");
    }

    @SystemLog(value = "删除用户")
    @DeleteMapping("/remove")
    public Result<User> removeUser(@RequestParam Long[] ids) {
        for (Long id : ids) {
            if (userService.removeUser(id) != 1) {
                return new ResultUtil<User>().setErrorMsg("删除用户失败");
            }
        }
        return new ResultUtil<User>().setSuccessMsg("删除用户成功");
    }
}

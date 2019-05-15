package com.springboot.controller;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.mapper.Wrapper;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.springboot.common.annotation.SystemLog;
import com.springboot.common.util.ResultUtil;
import com.springboot.model.Dept;
import com.springboot.model.vo.Result;
import com.springboot.service.IDeptService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/dept")
public class DeptController {
    @Autowired
    private IDeptService deptService;

    @GetMapping("/list")
    public Result<Map<String, Object>> getDeptList(@RequestParam int page, @RequestParam int limit, @RequestParam String condition) {
        Map<String, Object> result = new HashMap<>();
        PageHelper.startPage(page, limit);
        EntityWrapper<Dept> entity = new EntityWrapper<>();
        Wrapper<Dept> wrapper = entity.like("name", condition);
        List<Dept> list = deptService.selectList(wrapper);
        PageInfo<Dept> pageInfo = new PageInfo<>(list);
        result.put("list", list);
        result.put("total", pageInfo.getTotal());
        return new ResultUtil<Map<String, Object>>().setData(result);
    }

    @GetMapping("/allDept")
    public List<Dept> getAllDept() {
        EntityWrapper<Dept> entity = new EntityWrapper<>();
        return this.deptService.selectList(entity);
    }

    @SystemLog(value = "添加部门")
    @PostMapping("/add")
    public Result<Dept> addDept(@RequestBody Dept dept) {
        if (deptService.addDept(dept) != 1) {
            return new ResultUtil<Dept>().setErrorMsg("添加部门失败");
        }
        return new ResultUtil<Dept>().setSuccessMsg("添加部门成功");
    }

    @SystemLog(value = "修改部门信息")
    @PutMapping("/edit")
    public Result<Dept> editDept(@RequestBody Dept dept) {
        if (deptService.editDept(dept) != 1) {
            return new ResultUtil<Dept>().setErrorMsg("修改部门失败");
        }
        return new ResultUtil<Dept>().setSuccessMsg("修改部门成功");
    }

    @SystemLog(value = "删除部门")
    @DeleteMapping("/remove")
    public Result<Dept> removeDept(@RequestParam Integer[] ids) {
        for (Integer id : ids) {
            if (deptService.removeDept(id) != 1) {
                return new ResultUtil<Dept>().setErrorMsg("删除部门失败");
            }
        }
        return new ResultUtil<Dept>().setSuccessMsg("删除部门成功");
    }
}

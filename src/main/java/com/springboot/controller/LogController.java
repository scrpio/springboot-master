package com.springboot.controller;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.springboot.common.util.ResultUtil;
import com.springboot.model.Log;
import com.springboot.model.vo.Result;
import com.springboot.service.ILogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/log")
public class LogController {
    @Autowired
    private ILogService logService;

    @GetMapping("/list")
    public Result<Map<String, Object>> getLogList(@RequestParam int page, @RequestParam int limit) {
        Map<String, Object> result = new HashMap<>();
        PageHelper.startPage(page, limit);
        EntityWrapper<Log> entity = new EntityWrapper<>();
        List<Log> list = logService.selectList(entity);
        PageInfo<Log> pageInfo = new PageInfo<>(list);
        result.put("list", list);
        result.put("total", pageInfo.getTotal());
        return new ResultUtil<Map<String, Object>>().setData(result);
    }

    @DeleteMapping("/remove")
    public Result<Log> removeLog(@RequestParam Long[] ids) {
        for (Long id : ids) {
            if (logService.removeLog(id) != 1) {
                return new ResultUtil<Log>().setErrorMsg("删除日志失败");
            }
        }
        return new ResultUtil<Log>().setSuccessMsg("删除日志成功");
    }
}

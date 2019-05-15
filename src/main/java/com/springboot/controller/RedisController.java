package com.springboot.controller;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.google.gson.Gson;
import com.springboot.common.annotation.SystemLog;
import com.springboot.common.util.ResultUtil;
import com.springboot.common.util.ToolUtil;
import com.springboot.model.vo.Operate;
import com.springboot.model.vo.Result;
import com.springboot.service.IRedisService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import redis.clients.util.Slowlog;

import java.util.*;

@RestController
@RequestMapping("/redis")
public class RedisController {
    @Autowired
    private IRedisService redisService;

    //获取Redis信息
    @GetMapping("/info")
    public Result<Object> getRedisInfo() {
        String str = redisService.info();
        String[] s = str.split("\r\n");
        Map<String, Object> map = new HashMap<>();
        for (String i : s) {

            String[] temp = i.split(":");
            if (temp.length > 1) {
                map.put(temp[0], temp[1]);
            }
        }
        return new ResultUtil<Object>().setData(map);
    }

    //获取key列表，使用正则来获取
    @GetMapping("/keys")
    public Result<Object> getKeys(String pattern) {
        Set<String> result = redisService.keys(pattern);
        return new ResultUtil<Object>().setData(result);
    }

    //统计key的个数
    @GetMapping("/keysCount")
    public Result<Object> getKeysCount() {
        Long result = redisService.keysCount();
        return new ResultUtil<Object>().setData(result);
    }

    //根据指定的key获取value
    @GetMapping("/value")
    public Result<Object> getValueByKey(String key) {
        Map<String, Object> result = redisService.getMap(key);
        return new ResultUtil<Object>().setData(result);
    }

    //获取key和value
    @GetMapping("/keysValue")
    public Result<Object> getKeysValue() {
        Map<String, Object> result = new HashMap<>();
        Set<String> keys = redisService.keys("*");
        for (String key : keys) {
            result.put(key, redisService.get(key));
        }
        return new ResultUtil<Object>().setData(result);
    }

    @SystemLog(value = "删除单个缓存")
    @DeleteMapping("/delKey")
    public Result<Object> del(String key) {
        redisService.del(key);
        return new ResultUtil<Object>().setData(null);
    }

    @SystemLog(value = "清空缓存")
    @DeleteMapping("/delKeys")
    public Result<Object> delAll() {
        redisService.flushAll();
        return new ResultUtil<Object>().setData(null);
    }

    //获取redis日志列表
    @GetMapping("/getLogs")
    public Result<Map<String, Object>> getLogs(@RequestParam int page, @RequestParam int limit) {
        Map<String, Object> result = new HashMap<>();
        PageHelper.startPage(page, limit);
        List<Slowlog> list = redisService.slowlogGet();
        List<Operate> opList = null;
        Operate op = null;
        if (list != null && list.size() > 0) {
            opList = new LinkedList<Operate>();
            for (Slowlog sl : list) {
                String args = new Gson().toJson(sl.getArgs().toString());
                if (args.equals("[\"PING\"]") || args.equals("[\"SLOWLOG\",\"get\"]")) {
                    continue;
                }
                op = new Operate();
                op.setId(sl.getId());
                op.setExecuteTime(ToolUtil.getDateFormat(sl.getTimeStamp() * 1000));
                op.setUsedTime(sl.getExecutionTime() / 1000.0 + "ms");
                op.setArgs(args);
                opList.add(op);
            }
        }
        PageInfo<Operate> pageInfo = new PageInfo<>(opList);
        result.put("list", opList);
        result.put("total", pageInfo.getTotal());
        return new ResultUtil<Map<String, Object>>().setData(result);
    }

    //清空日志
    @DeleteMapping("/clearLogs")
    public Result<Object> logEmpty() {
        redisService.logEmpty();
        return new ResultUtil<Object>().setData(null);
    }
}

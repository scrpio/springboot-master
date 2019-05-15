package com.springboot.controller;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.springboot.common.annotation.SystemLog;
import com.springboot.common.util.ResultUtil;
import com.springboot.model.Dict;
import com.springboot.model.vo.Result;
import com.springboot.service.IDictService;
import com.springboot.service.IRedisService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
public class DictController {
    @Autowired
    private IDictService dictService;
    @Autowired
    private IRedisService redisService;

    @GetMapping("/getDictList")
    public String getDictExtList(HttpServletResponse response) {
        String result = "";
        String v = redisService.get("DICT_EXT_KEY");
        if (StringUtils.isNotBlank(v)) {
            return v;
        }
        List<Dict> list = dictService.getDictList();
        for (Dict dict : list) {
            result += dict.getDict() + "\n";
        }
        if (StringUtils.isNotBlank(result)) {
            redisService.set("DICT_EXT_KEY", result);
        }
        response.addHeader("Last-Modified", redisService.get("Last-Modified"));
        response.addHeader("ETAG", redisService.get("ETAG"));
        return result;
    }

    @GetMapping("/getStopDictList")
    public String getStopDictList(HttpServletResponse response) {
        String result = "";
        String v = redisService.get("DICT_STOP_KEY");
        if (StringUtils.isNotBlank(v)) {
            return v;
        }
        List<Dict> list = dictService.getStopList();
        for (Dict dict : list) {
            result += dict.getDict() + "\n";
        }
        if (StringUtils.isNotBlank(result)) {
            redisService.set("DICT_STOP_KEY", result);
        }
        response.addHeader("Last-Modified", redisService.get("Last-Modified"));
        response.addHeader("ETAG", redisService.get("ETAG"));
        return result;
    }

    @GetMapping("/dict/list")
    public Result<Map<String, Object>> getDictList(@RequestParam int page, @RequestParam int limit) {
        Map<String, Object> result = new HashMap<>();
        PageHelper.startPage(page, limit);
        EntityWrapper<Dict> entity = new EntityWrapper<>();
        List<Dict> list = dictService.selectList(entity);
        PageInfo<Dict> pageInfo = new PageInfo<>(list);
        result.put("list", list);
        result.put("total", pageInfo.getTotal());
        return new ResultUtil<Map<String, Object>>().setData(result);
    }

    @SystemLog(value = "添加词典")
    @PostMapping("/dict/add")
    public Result<Object> addDict(@RequestBody Dict dict) {
        if (dictService.addDict(dict) != 1) {
            return new ResultUtil<Object>().setErrorMsg("添加词典失败");
        }
        update();
        return new ResultUtil<Object>().setSuccessMsg("添加词典成功");
    }

    @SystemLog(value = "修改词典")
    @PutMapping("/dict/edit")
    public Result<Object> editDict(@RequestBody Dict dict) {
        if (dictService.editDict(dict) != 1) {
            return new ResultUtil<Object>().setErrorMsg("修改词典失败");
        }
        update();
        return new ResultUtil<Object>().setSuccessMsg("修改词典成功");
    }

    @SystemLog(value = "删除词典")
    @DeleteMapping("/dict/remove")
    public Result<Object> removeDict(@RequestParam Integer[] ids) {
        for (int id : ids) {
            if (dictService.removeDict(id) != 1) {
                return new ResultUtil<Object>().setErrorMsg("删除词典失败");
            }
        }
        update();
        return new ResultUtil<Object>().setSuccessMsg("删除词典成功");
    }

    private void update() {
        //更新词典标识
        redisService.set("Last-Modified", String.valueOf(System.currentTimeMillis()));
        redisService.set("ETAG", String.valueOf(System.currentTimeMillis()));
        //更新缓存
        redisService.del("DICT_EXT_KEY");
        redisService.del("DICT_STOP_KEY");
    }
}

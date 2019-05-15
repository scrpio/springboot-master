package com.springboot.controller;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.mapper.Wrapper;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.springboot.common.annotation.SystemLog;
import com.springboot.common.util.ResultUtil;
import com.springboot.model.Blog;
import com.springboot.model.vo.Result;
import com.springboot.service.IBlogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/blog")
public class BlogController {
    @Autowired
    private IBlogService blogService;

    @GetMapping("/list")
    public Result<Map<String, Object>> getBlogList(@RequestParam int page, @RequestParam int limit) {
        Map<String, Object> result = new HashMap<>();
        PageHelper.startPage(page, limit);
        EntityWrapper<Blog> entityWrapper = new EntityWrapper<>();
        Wrapper<Blog> wrapper = entityWrapper.orderBy("publish_time", false);
        List<Blog> list = blogService.selectList(wrapper);
        PageInfo<Blog> pageInfo = new PageInfo<>(list);
        result.put("list", list);
        result.put("total", pageInfo.getTotal());
        return new ResultUtil<Map<String, Object>>().setData(result);
    }

    @GetMapping("/top5")
    public Result<List<Map<String, Object>>> getBlogTop5() {
        EntityWrapper<Blog> entityWrapper = new EntityWrapper<>();
        Wrapper<Blog> wrapper = entityWrapper.setSqlSelect("tb_blog.*, tb_user.avatar").last("LEFT JOIN tb_user ON tb_blog.author = tb_user.username LIMIT 5");
        List<Map<String, Object>> list = blogService.selectMaps(wrapper);
        return new ResultUtil<List<Map<String, Object>>>().setData(list);
    }

    @GetMapping("/{id}")
    public Blog getBlogById(@PathVariable Long id) {
        return this.blogService.selectById(id);
    }

    @SystemLog(value = "添加文章")
    @PostMapping("/add")
    public Result<Blog> addBlog(@RequestBody Blog blog) {
        if (blogService.addBlog(blog) != 1) {
            return new ResultUtil<Blog>().setErrorMsg("添加文章失败");
        }
        return new ResultUtil<Blog>().setData(blog, "添加文章成功");
    }

    @SystemLog(value = "修改文章信息")
    @PutMapping("/edit")
    public Result<Blog> editBlog(@RequestBody Blog blog) {
        if (blogService.editBlog(blog) != 1) {
            return new ResultUtil<Blog>().setErrorMsg("修改文章失败");
        }
        return new ResultUtil<Blog>().setSuccessMsg("修改文章成功");
    }

    @SystemLog(value = "删除文章")
    @DeleteMapping( "/remove")
    public Result<Blog> removeBlog(@RequestParam Long[] ids) {
        for (Long id : ids) {
            if (blogService.removeBlog(id) != 1) {
                return new ResultUtil<Blog>().setErrorMsg("删除文章失败");
            }
        }
        return new ResultUtil<Blog>().setSuccessMsg("删除文章成功");
    }
}

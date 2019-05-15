package com.springboot.service;

import com.baomidou.mybatisplus.service.IService;
import com.springboot.model.Blog;
import org.springframework.transaction.annotation.Transactional;

public interface IBlogService extends IService<Blog> {
    @Transactional
    int addBlog(Blog blog);

    @Transactional
    int editBlog(Blog blog);

    @Transactional
    int removeBlog(long id);
}

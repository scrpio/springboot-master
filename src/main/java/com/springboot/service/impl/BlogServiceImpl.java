package com.springboot.service.impl;

import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.springboot.common.enums.TransCode;
import com.springboot.common.exception.TransException;
import com.springboot.dao.BlogDao;
import com.springboot.model.Blog;
import com.springboot.service.IBlogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
public class BlogServiceImpl extends ServiceImpl<BlogDao, Blog> implements IBlogService {
    @Autowired
    private BlogDao blogDao;

    @Override
    public int addBlog(Blog blog) {
        blog.setCreated(new Date());
        int result = blogDao.insert(blog);
        if (result != 1) {
            throw new TransException(TransCode.INSERT_FAIL);
        }
        return result;
    }

    @Override
    public int editBlog(Blog blog) {
        int result = blogDao.updateById(blog);
        if (result != 1) {
            throw new TransException(TransCode.UPDATE_FAIL);
        }
        return result;
    }

    @Override
    public int removeBlog(long id) {
        int result = blogDao.deleteById(id);
        if (result != 1) {
            throw new TransException(TransCode.DELETE_FAIL);
        }
        return result;
    }
}

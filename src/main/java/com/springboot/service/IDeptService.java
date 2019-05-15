package com.springboot.service;

import com.baomidou.mybatisplus.service.IService;
import com.springboot.model.Dept;
import org.springframework.transaction.annotation.Transactional;

public interface IDeptService extends IService<Dept> {
    @Transactional
    int addDept(Dept dept);

    @Transactional
    int editDept(Dept dept);

    @Transactional
    int removeDept(int id);
}

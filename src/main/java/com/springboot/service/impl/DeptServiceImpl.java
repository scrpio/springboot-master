package com.springboot.service.impl;

import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.springboot.common.enums.TransCode;
import com.springboot.common.exception.TransException;
import com.springboot.dao.DeptDao;
import com.springboot.model.Dept;
import com.springboot.service.IDeptService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class DeptServiceImpl extends ServiceImpl<DeptDao, Dept> implements IDeptService {
    @Autowired
    private DeptDao deptDao;

    @Override
    public int addDept(Dept dept) {
        int result = deptDao.insert(dept);
        if (result != 1) {
            throw new TransException(TransCode.INSERT_FAIL);
        }
        return result;
    }

    @Override
    public int editDept(Dept dept) {
        int result = deptDao.updateById(dept);
        if (result != 1) {
            throw new TransException(TransCode.UPDATE_FAIL);
        }
        return result;
    }

    @Override
    public int removeDept(int id) {
        int result = deptDao.deleteById(id);
        if (result != 1) {
            throw new TransException(TransCode.DELETE_FAIL);
        }
        return result;
    }
}

package com.springboot.service.impl;

import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.springboot.common.enums.TransCode;
import com.springboot.common.exception.TransException;
import com.springboot.dao.LogDao;
import com.springboot.model.Log;
import com.springboot.service.ILogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class LogServiceImpl extends ServiceImpl<LogDao, Log> implements ILogService {
    @Autowired
    private LogDao logDao;

    @Override
    public int removeLog(long id) {
        int result = logDao.deleteById(id);
        if (result != 1) {
            throw new TransException(TransCode.DELETE_FAIL);
        }
        return result;
    }
}

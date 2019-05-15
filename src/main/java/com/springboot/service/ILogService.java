package com.springboot.service;

import com.baomidou.mybatisplus.service.IService;
import com.springboot.model.Log;
import org.springframework.transaction.annotation.Transactional;

public interface ILogService extends IService<Log> {
    @Transactional
    int removeLog(long id);
}

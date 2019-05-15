package com.springboot.service.impl;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.mapper.Wrapper;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.springboot.common.enums.TransCode;
import com.springboot.common.exception.TransException;
import com.springboot.dao.DictDao;
import com.springboot.model.Dict;
import com.springboot.service.IDictService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DictServiceImpl extends ServiceImpl<DictDao, Dict> implements IDictService {
    @Autowired
    private DictDao dictDao;

    @Override
    public List<Dict> getDictList() {
        EntityWrapper<Dict> entityWrapper = new EntityWrapper<>();
        Wrapper<Dict> wrapper = entityWrapper.eq("type", 1);
        List<Dict> list = dictDao.selectList(wrapper);
        return list;
    }

    @Override
    public List<Dict> getStopList() {
        EntityWrapper<Dict> entityWrapper = new EntityWrapper<>();
        Wrapper<Dict> wrapper = entityWrapper.eq("type", 0);
        List<Dict> list = dictDao.selectList(wrapper);
        return list;
    }

    @Override
    public int addDict(Dict dict) {
        int result = dictDao.insert(dict);
        if (result != 1) {
            throw new TransException(TransCode.INSERT_FAIL);
        }
        return result;
    }

    @Override
    public int editDict(Dict dict) {
        int result = dictDao.updateById(dict);
        if (result != 1) {
            throw new TransException(TransCode.UPDATE_FAIL);
        }
        return result;
    }

    @Override
    public int removeDict(int id) {
        int result = dictDao.deleteById(id);
        if (result != 1) {
            throw new TransException(TransCode.DELETE_FAIL);
        }
        return result;
    }
}

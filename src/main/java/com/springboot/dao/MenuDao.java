package com.springboot.dao;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.springboot.model.Menu;

import java.util.List;

public interface MenuDao extends BaseMapper<Menu> {
    List<Menu> listMenuByUserId(Long userId);
}

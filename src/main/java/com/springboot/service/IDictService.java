package com.springboot.service;

import com.baomidou.mybatisplus.service.IService;
import com.springboot.model.Dict;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface IDictService extends IService<Dict> {
    /**
     * 获取扩展词库列表
     *
     * @return
     */
    List<Dict> getDictList();

    /**
     * 获取停用词库列表
     *
     * @return
     */
    List<Dict> getStopList();

    /**
     * 添加
     *
     * @param dict
     * @return
     */
    @Transactional
    int addDict(Dict dict);

    /**
     * 更新
     *
     * @param dict
     * @return
     */
    @Transactional
    int editDict(Dict dict);

    /**
     * 删除
     *
     * @param id
     * @return
     */
    @Transactional
    int removeDict(int id);
}

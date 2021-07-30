package com.zmc.health.service;

import com.zmc.health.entity.PageResult;
import com.zmc.health.entity.QueryPageBean;
import com.zmc.health.exception.MyException;
import com.zmc.health.pojo.CheckItem;

import java.util.List;

/**
 * Package_Name: com.zmc.health.service
 *
 * @author zmc
 * Date: 2021/7/29
 */
public interface CheckItemService {
    /**
     * 查询所有的检查项
     * @return
     */
    List<CheckItem> findAll();

    /**
     * 新增检查项
     * @param checkItem
     */
    void add(CheckItem checkItem);

    /**
     *分页条件查询
     * @param queryPageBean
     * @return
     */
    PageResult<CheckItem> findPage(QueryPageBean queryPageBean);

    /**
     * 根据id查询
     * @param id
     * @return
     */
    CheckItem findById(int id);

    /**
     * 更新检查项
     * @param checkItem
     */
    void update(CheckItem checkItem);

    /**
     * 通过id删除
     * @param id
     */
    void deleteById(int id) throws MyException;
}

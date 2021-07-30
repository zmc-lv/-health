package com.zmc.health.dao;

import com.github.pagehelper.Page;
import com.zmc.health.pojo.CheckItem;

import java.util.List;

/**
 * Package_Name: com.zmc.health.dao
 *
 * @author zmc
 * Date: 2021/7/29
 */
public interface CheckItemDao {
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
     * 分页条件查询
     * @param queryString
     * @return
     */
    Page<CheckItem> findByCondition(String queryString);

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
     * 通过检查项id查询是否被检查组使用
     * @param id
     * @return
     */
    int findCountByCheckItemId(int id);

    /**
     * 通过id删除检查项
     * @param id
     */
    void deleteById(int id);
}

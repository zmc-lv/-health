package com.zmc.health.dao;

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
}

package com.zmc.health.service;

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
}

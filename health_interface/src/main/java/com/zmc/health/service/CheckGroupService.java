package com.zmc.health.service;

import com.zmc.health.entity.PageResult;
import com.zmc.health.entity.QueryPageBean;
import com.zmc.health.exception.MyException;
import com.zmc.health.pojo.CheckGroup;

import java.util.List;

/**
 * 包名: com.zmc.health.service
 *
 * @author zmc
 * 日期: 2021/7/31
 */
public interface CheckGroupService {
    /**
     * 添加检查组
     * @param checkGroup
     * @param checkitemIds
     */
    void add(CheckGroup checkGroup, Integer[] checkitemIds);

    /**
     * 分页条件查询
     * @param queryPageBean
     * @return
     */
    PageResult<CheckGroup> findPage(QueryPageBean queryPageBean);

    /**
     * 通过id查询
     * @param id
     * @return
     */
    CheckGroup findById(int id) throws MyException;

    /**
     * 通过id查询选中的检查项id集合
     * @param id
     * @return
     */
    List<Integer> findCheckItemIdsByCheckGroupId(int id);

    /**
     * 编辑检查组
     * @param checkGroup
     * @param checkitemIds
     */
    void update(CheckGroup checkGroup, Integer[] checkitemIds);

    /**
     * 通过id删除检查项
     * @param id
     */
    void deleteById(int id)throws MyException;
}

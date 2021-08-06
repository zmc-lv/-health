package com.zmc.health.dao;

import com.github.pagehelper.Page;
import com.zmc.health.pojo.CheckGroup;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 包名: com.zmc.health.dao
 *
 * @author zmc
 * 日期: 2021/7/31
 */
public interface CheckGroupDao {
    /**
     * 添加检查组
     * @param checkGroup
     */
    void add(CheckGroup checkGroup);

    /**
     *添加检查组与检查项的关系
     * @param checkGroupId
     * @param checkitemId
     */
    void addCheckGroupCheckItem(@Param("checkGroupId") Integer checkGroupId, @Param("checkitemId") Integer checkitemId);

    /**
     * 分页条件查询
     * @param queryString
     * @return
     */
    Page<CheckGroup> findByCondition(String queryString);

    /**
     * 通过id查询
     * @param id
     * @return
     */
    CheckGroup findById(int id);

    /**
     * 通过id查询选中的检查项id集合
     * @param id
     * @return
     */
    List<Integer> findCheckItemIdsByCheckGroupId(int id);

    /**
     * 更新检查组
     * @param checkGroup
     */
    void update(CheckGroup checkGroup);

    /**
     * 删除旧关系
     * @param id
     */
    void deleteCheckGroupCheckItem(Integer id);

    /**
     * 通过检查组id，查询使用这个检查组的套餐个数
     * @param id
     * @return
     */
    int findCountByCheckGroupId(int id);

    /**
     * 通过id删除检查组
     * @param id
     */
    void deleteById(int id);

    /**
     * 查询所有检查组
     * @return
     */
    List<CheckGroup> findAll();
}

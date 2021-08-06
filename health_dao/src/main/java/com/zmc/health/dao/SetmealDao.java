package com.zmc.health.dao;

import com.github.pagehelper.Page;
import com.zmc.health.pojo.Setmeal;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 包名: com.zmc.health.dao
 *
 * @author zmc
 * 日期: 2021/8/2
 */
public interface SetmealDao {
    /**
     * 添加套餐
     * @param setmeal
     */
    void add(Setmeal setmeal);

    /**
     *添加套餐和检查组的关系
     * @param setmealId
     * @param checkgroupId
     */
    void addSetmealCheckGroup(@Param("setmealId") Integer setmealId,@Param("checkgroupId") Integer checkgroupId);

    /**
     * 分页查询
     * @param queryString
     * @return
     */
    Page<Setmeal> findByCondition(String queryString);

    /**
     * 通过id查询
     * @param id
     * @return
     */
    Setmeal findById(int id);

    /**
     * 通过套餐id查询选中的检查组id集合
     * @param id
     * @return
     */
    List<Integer> findCheckgroupIdsBySetmealId(int id);

    /**
     * 更新套餐
     * @param setmeal
     */
    void update(Setmeal setmeal);

    /**
     * 删除套餐跟检查组的关系
     * @param id
     */
    void deleteSetmealCheckGroup(Integer id);

    /**
     * 通过套餐id统计订单的根数
     * @param id
     * @return
     */
    int findOrderCountBySetmealId(int id);

    /**
     * 通过套餐id删除套餐
     * @param id
     */
    void deleteById(int id);

    /**
     * 查询数据库中套餐的所有图片
     * @return
     */
    List<String> findImgs();

    /**
     * 套餐列表展示
     * @return
     */
    List<Setmeal> findAll();

    /**
     * 查询套餐详情
     * @param id
     * @return
     */
    Setmeal findDetailById(int id);
}

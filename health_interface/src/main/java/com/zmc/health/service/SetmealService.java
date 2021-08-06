package com.zmc.health.service;

import com.zmc.health.entity.PageResult;
import com.zmc.health.entity.QueryPageBean;
import com.zmc.health.pojo.Setmeal;

import java.util.List;

/**
 * 包名: com.zmc.health.service
 *
 * @author zmc
 * 日期: 2021/8/2
 */
public interface SetmealService {

    /**
     * 添加套餐
     * @param setmeal
     * @param checkgroupIds
     */
    void add(Setmeal setmeal, Integer[] checkgroupIds);

    /**
     * 分页查询
     * @param queryPageBean
     * @return
     */
    PageResult<Setmeal> findPage(QueryPageBean queryPageBean);

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
     * @param checkgroupIds
     */
    void update(Setmeal setmeal, Integer[] checkgroupIds);

    /**
     * 删除套餐
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

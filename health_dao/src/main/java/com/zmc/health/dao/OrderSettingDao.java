package com.zmc.health.dao;

import com.zmc.health.pojo.OrderSetting;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * 包名: com.zmc.health.dao
 *
 * @author zmc
 * 日期: 2021/8/3
 */
public interface OrderSettingDao {
    /**
     *通过日期查询预约设置表
     * @param orderDate
     * @return
     */
    OrderSetting findByOrderDate(Date orderDate);

    /**
     *更新最大预约数
     * @param orderSetting
     */
    void updateNumber(OrderSetting orderSetting);

    /**
     * 添加预约设置
     * @param orderSetting
     */
    void add(OrderSetting orderSetting);

    /**
     * 通过月份查询预约设置
     * @param month
     * @return
     */
    List<Map<String, Integer>> getDataByMoth(String month);

     int editReservationsByOrderDate(OrderSetting orderSetting);
}

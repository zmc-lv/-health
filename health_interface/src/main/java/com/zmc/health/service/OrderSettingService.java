package com.zmc.health.service;

import com.zmc.health.exception.MyException;
import com.zmc.health.pojo.OrderSetting;

import java.util.List;
import java.util.Map;

/**
 * 包名: com.zmc.health.service
 *
 * @author zmc
 * 日期: 2021/8/3
 */
public interface OrderSettingService {


    /**
     * 批量导入预约设置
     * @param orderSettingList
     */
    void add(List<OrderSetting> orderSettingList) throws MyException;

    /**
     * 通过月份查询预约设置
     * @param month
     * @return
     */
    List<Map<String, Integer>> getDataByMoth(String month);

    /**
     * 基于日历的预约设置
     * @param os
     */
    void editNumberByDate(OrderSetting os);
}

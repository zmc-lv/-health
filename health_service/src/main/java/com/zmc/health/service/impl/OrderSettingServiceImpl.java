package com.zmc.health.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.zmc.health.dao.OrderSettingDao;
import com.zmc.health.exception.MyException;
import com.zmc.health.pojo.OrderSetting;
import com.zmc.health.service.OrderSettingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

/**
 * 包名: com.zmc.health.service.impl
 *
 * @author zmc
 * 日期: 2021/8/3
 */
@Service(interfaceClass = OrderSettingService.class)
public class OrderSettingServiceImpl implements OrderSettingService {

    @Autowired
    private OrderSettingDao orderSettingDao;

    /**
     * 批量导入预约设置
     * @param orderSettingList
     */
    @Override
    @Transactional
    public void add(List<OrderSetting> orderSettingList) {
        //遍历List<OrderSetting>
        if (null !=orderSettingList) {
            for (OrderSetting orderSetting : orderSettingList) {
                //通过日期查询预约设置表
                OrderSetting osInDB = orderSettingDao.findByOrderDate(orderSetting.getOrderDate());
                //如果存在2预约设置
                if (null !=osInDB) {
                    //判断更新后的最大数是否大等于已预约数
                    if (orderSetting.getNumber() <osInDB.getReservations()) {
                        //小于,报错 已预约数超过最大预约数,接口异常声明
                        throw  new MyException("更新后的最大预约数，不能小于已预约数");
                    }
                    //大于,则可以更新最大预约数
                    orderSettingDao.updateNumber(orderSetting);
                }else {
                    //不存在,则添加预约设置
                    orderSettingDao.add(orderSetting);
                }
            }
        }

    }


    /**
     * 通过月份查询预约设置
     * @param month
     * @return
     */
    @Override
    public List<Map<String, Integer>> getDataByMoth(String month) {
        month+="%";
        return orderSettingDao.getDataByMoth(month);
    }

    /**
     * 基于日历的预约设置
     * @param orderSetting
     */
    @Override
    public void editNumberByDate(OrderSetting orderSetting) {
        //通过日期查询预约设置表
        OrderSetting osInDB = orderSettingDao.findByOrderDate(orderSetting.getOrderDate());
        //如果存在2预约设置
        if (null !=osInDB) {
            //判断更新后的最大数是否大等于已预约数
            if (orderSetting.getNumber() <osInDB.getReservations()) {
                //小于,报错 已预约数超过最大预约数,接口异常声明
                throw  new MyException("更新后的最大预约数，不能小于已预约数");
            }
            //大于,则可以更新最大预约数
            orderSettingDao.updateNumber(orderSetting);
        }else {
            //不存在,则添加预约设置
            orderSettingDao.add(orderSetting);
        }
    }
}


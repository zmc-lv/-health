package com.zmc.health.service;

import com.zmc.health.exception.MyException;

import java.util.Map;

/**
 * 包名: com.zmc.health.service
 *
 * @author zmc
 * 日期: 2021/8/5
 */
public interface OrderService {


    /**
     * 预约提交业务
     * @param parMap
     * @return
     */
    Integer submit(Map<String, String> parMap) throws MyException;


    /**
     * 订单详情
     * @param id
     * @return
     */
    Map<String, Object> findOrderDetailById(int id);

}

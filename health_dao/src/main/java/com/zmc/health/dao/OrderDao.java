package com.zmc.health.dao;

import com.zmc.health.pojo.Order;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

public interface OrderDao {
    public void add(Order order);
    public List<Order> findByCondition(Order order);
    public Map findById4Detail(Integer id);
    public Integer findOrderCountByDate(String date);
    public Integer findOrderCountAfterDate(String date);
    public Integer findVisitsCountByDate(String date);
    public Integer findVisitsCountAfterDate(String date);
    public List<Map> findHotSetmeal();

    /**
     * 通过日期范围查询预约数
     * @return
     */
    Integer findOrderCountBetweenDate(@Param("startDate") String startDate,@Param("endDate") String endDate);
}

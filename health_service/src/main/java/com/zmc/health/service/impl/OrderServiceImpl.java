package com.zmc.health.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.zmc.health.dao.MemberDao;
import com.zmc.health.dao.OrderDao;
import com.zmc.health.dao.OrderSettingDao;
import com.zmc.health.exception.MyException;
import com.zmc.health.pojo.Member;
import com.zmc.health.pojo.Order;
import com.zmc.health.pojo.OrderSetting;
import com.zmc.health.service.OrderService;
import com.zmc.health.utils.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * 包名: com.zmc.health.service.impl
 *
 * @author zmc
 * 日期: 2021/8/5
 */
@Service(interfaceClass = OrderService.class)
public class OrderServiceImpl implements OrderService {
    /**
     * 预约提交业务
     * @param parMap
     * @return
     */
    @Autowired
    private OrderSettingDao orderSettingDao;

    @Autowired
    private MemberDao memberDao;

    @Autowired
    private OrderDao orderDao;

    /**
     * 预约提交业务 实现
     * @param paraMap
     * @return
     */
    @Override
    @Transactional
    public Integer submit(Map<String, String> paraMap) {
        //- 通过日期查询预约设置表
        String orderDateString = paraMap.get("orderDate");
        Date orderDate = null;
        try {
            // 日期应该在controller做验证后才进来的service
            orderDate = DateUtils.parseString2Date(orderDateString);
        } catch (Exception e) {
            throw new MyException("预约日期格式不正确");
        }
        OrderSetting orderSetting = orderSettingDao.findByOrderDate(orderDate);
        if(null == orderSetting) {
            //  - 没记录，报错，所选日期不能预约，请选择其它日期
            throw new MyException("所选日期不能预约，请选择其它日期");
        }
        //  - 有记录，判断是否约满
        if(orderSetting.getReservations() >= orderSetting.getNumber()) {
            //    - reservations>=number, 报错，所选日期预约已满，请选择其它日期
            throw new MyException("所选日期预约已满，请选择其它日期");
        }
        // 构建重复预约查询条件
        Order order = new Order();
        order.setSetmealId(Integer.valueOf(paraMap.get("setmealId")));
        order.setOrderDate(orderDate);
        // 手机号码
        String telephone = paraMap.get("telephone");
        //- 通过手机号码查询会员是否存在
        Member member = memberDao.findByTelephone(telephone);
        //  - 不存在，
        if(null == member){
            //添加会新会员，插入记录时要返回主键
            member = new Member();
            member.setPhoneNumber(telephone);
            member.setRegTime(new Date());
            member.setRemark("微信预约注册");
            String idCardNo = paraMap.get("idCard");
            member.setPassword(idCardNo.substring(idCardNo.length()-6));
            member.setIdCard(idCardNo);
            member.setSex(paraMap.get("sex"));
            member.setName(paraMap.get("name"));
            memberDao.add(member);
            order.setMemberId(member.getId());
        }else{
            //- 获取会员id
            Integer memberId = member.getId();
            //- 判断是否重复预约
            order.setMemberId(memberId);
            //  - 通过预约日期，会员id,套餐id查询t_order
            List<Order> orderList = orderDao.findByCondition(order);
            //    - 存在，则报错，已经预约过，请忽重复预约
            if(null != orderList && orderList.size() > 0){
                throw new MyException("已经预约过了，请忽重复预约");
            }
        }
        //- 更新已预约人数
        int count = orderSettingDao.editReservationsByOrderDate(orderSetting);
        //  - 并发，加分布式锁（利用数据库的行锁） where reservations>number
        //  - 如果更新失败，则返回0，报错，预约已满，请选择其它日期
        if(count == 0){
            throw new MyException("预约已满，请选择其它日期");
        }
        // - 如果更新成功，mybatis则会返回1
        //- 添加订单信息，返回主键
        order.setOrderStatus(Order.ORDERSTATUS_NO);
        order.setOrderType(paraMap.get("orderType"));
        orderDao.add(order);
        //- 返回订单的id给controller
        return order.getId();
    }



    /**
     * 订单详情
     * @param id
     * @return
     */
    @Override
    public Map<String, Object> findOrderDetailById(int id) {
        return orderDao.findById4Detail(id);
    }


}

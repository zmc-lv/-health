package com.zmc.health.controller;

import com.alibaba.druid.util.StringUtils;
import com.alibaba.dubbo.config.annotation.Reference;
import com.zmc.health.constant.MessageConstant;
import com.zmc.health.constant.RedisMessageConstant;
import com.zmc.health.entity.Result;
import com.zmc.health.pojo.Order;
import com.zmc.health.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

import java.util.Map;

/**
 * 包名: com.zmc.health.controller
 *
 * @author zmc
 * 日期: 2021/8/5
 */
@RestController
@RequestMapping("/order")
public class OrderController {
    @Autowired
    private JedisPool jedisPool;

    @Reference
    private OrderService orderService;
    @PostMapping("/submit")
    public Result submit(@RequestBody Map<String,String> parMap){
        //      验证码校验
        Jedis jedis = jedisPool.getResource();
        // 获取手机号码
        String telephone = parMap.get("telephone");
        // 拼接redis的key
        String key = RedisMessageConstant.SENDTYPE_ORDER + ":" + telephone;
        //  再从redis取出验证码
        String codeInRedis = jedis.get(key);
        //  设值，用户可能没有点击获取验证，验证码过程，返回 重新获取验证
        if (StringUtils.isEmpty(codeInRedis)) {
            return new Result(false,"请重新获取验证码");
        }
        // 校验前端提交的验证码与redis中的验证码是否一致
        if (!codeInRedis.equals(parMap.get("validateCode"))) {
            //  不一致，返回，验证码错误
            return new Result(false, MessageConstant.VALIDATECODE_ERROR);
        }
        //  一致，删除验证码的key
        jedis.del(key);
        jedis.close();
        //设置预约类型,health_mobile微信预约
        parMap.put("orderType", Order.ORDERTYPE_WEIXIN);
        //调用服务进行预约
        Integer id = orderService.submit(parMap);
        return new Result(true,MessageConstant.ORDER_SUCCESS,id);
    }

    /**
     * 订单详情
     * @param id
     * @return
     */
    @GetMapping("/findById")
    public Result findById(int id){
        // 调用服务查询
        Map<String,String> orderInfo = orderService.findOrderDetailById(id);
        return new Result(true, MessageConstant.QUERY_ORDER_SUCCESS,orderInfo);
    }
}

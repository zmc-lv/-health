package com.zmc.health.controller;

import com.alibaba.druid.util.StringUtils;
import com.alibaba.dubbo.config.annotation.Reference;
import com.zmc.health.constant.MessageConstant;
import com.zmc.health.constant.RedisMessageConstant;
import com.zmc.health.entity.Result;
import com.zmc.health.pojo.Member;
import com.zmc.health.service.MemberService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import java.util.Date;
import java.util.Map;

/**
 * 包名: com.zmc.health.controller
 *
 * @author zmc
 * 日期: 2021/8/6
 */
@RestController
@RequestMapping("/login")
public class LoginController {

    @Reference
    private MemberService memberService;

    @Autowired
    private JedisPool jedisPool;

    @PostMapping("/check")
    public Result checkResult(@RequestBody Map<String,String> loginInfo, HttpServletResponse response){
        //验证码校验
        //      验证码校验
        Jedis jedis = jedisPool.getResource();
        // 获取手机号码
        String telephone = loginInfo.get("telephone");
        // 拼接redis的key
        String key = RedisMessageConstant.SENDTYPE_LOGIN + ":" + telephone;
        //  再从redis取出验证码
        String codeInRedis = jedis.get(key);
        //  设值，用户可能没有点击获取验证，验证码过程，返回 重新获取验证
        if (StringUtils.isEmpty(codeInRedis)) {
            return new Result(false,"请重新获取验证码");
        }
        // 校验前端提交的验证码与redis中的验证码是否一致
        if (!codeInRedis.equals(loginInfo.get("validateCode"))) {
            //  不一致，返回，验证码错误
            return new Result(false, MessageConstant.VALIDATECODE_ERROR);
        }
        //  一致，删除验证码的key
        jedis.del(key);
        jedis.close();
        //判断是否为会员
        Member member = memberService.findByTelephone(telephone);
        //非会员就添加为会员
        if (null == member) {
            member = new Member();
            member.setRemark("手机快速登录");
            member.setRegTime(new Date());
            member.setPhoneNumber(telephone);
            memberService.add(member);
        }
        //添加cookie跟踪
        Cookie cookie = new Cookie("login_member_telephone",telephone);
        cookie.setMaxAge(30*24*60*60);//存货30天
        cookie.setPath("/");
        response.addCookie(cookie);
        return new Result(true,MessageConstant.LOGIN_SUCCESS);
    }

}

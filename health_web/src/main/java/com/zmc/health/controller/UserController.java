package com.zmc.health.controller;

import com.zmc.health.constant.MessageConstant;
import com.zmc.health.entity.Result;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 包名: com.zmc.health.controller
 *
 * @author zmc
 * 日期: 2021/8/7
 */
@RestController
@RequestMapping("/user")
public class UserController {

    /**
     * 登录成功以后,进入main.html页面时获取登录用户名
     * @return
     */

    @GetMapping("getLoginUsername")
    public Result getLoginUsername(){
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String username = user.getUsername();
        return new Result(true, MessageConstant.GET_USERNAME_SUCCESS,username);
    }

    @RequestMapping("/loginFail")
    public Result loginFail(){
        return  new Result(false,"用户名或密码错误");
    }

    @RequestMapping("/loginSuccess")
    public Result loginSuccess(){
        return  new Result(true,"用户登录成功");
    }


}

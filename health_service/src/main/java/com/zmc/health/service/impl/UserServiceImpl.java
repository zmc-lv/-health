package com.zmc.health.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.zmc.health.dao.UserDao;
import com.zmc.health.pojo.User;
import com.zmc.health.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * 包名: com.zmc.health.service.impl
 *
 * @author zmc
 * 日期: 2021/8/6
 */
@Service(interfaceClass = UserService.class)
public class UserServiceImpl implements UserService {

    @Autowired
    private UserDao userDao;
    /**
     * 通过用户名查询用户信息 用户下的角色 角色下的权限， 5表关联查询 多关系映射
     * @param username
     * @return
     */
    @Override
    public User findByUsername(String username) {
        return userDao.findByUsername(username);
    }
}

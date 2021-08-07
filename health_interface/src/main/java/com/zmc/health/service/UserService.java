package com.zmc.health.service;

import com.zmc.health.pojo.User;

/**
 * 包名: com.zmc.health.service
 *
 * @author zmc
 * 日期: 2021/8/6
 */
public interface UserService {
    /**
     * 通过用户名查询用户信息 用户下的角色 角色下的权限， 5表关联查询 多关系映射
     * @param username
     * @return
     */
    User findByUsername(String username);
}

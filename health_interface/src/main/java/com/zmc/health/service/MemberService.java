package com.zmc.health.service;

import com.zmc.health.pojo.Member;

/**
 * 包名: com.zmc.health.service
 *
 * @author zmc
 * 日期: 2021/8/6
 */
public interface MemberService {
    /**
     * 通过手机号码查询
     * @param telephone
     * @return
     */
    Member findByTelephone(String telephone);

    /**
     * 添加新会员
     * @param member
     */
    void add(Member member);
}

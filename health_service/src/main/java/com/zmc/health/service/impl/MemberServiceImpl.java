package com.zmc.health.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.zmc.health.dao.MemberDao;
import com.zmc.health.pojo.Member;
import com.zmc.health.service.MemberService;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.List;

/**
 * 包名: com.zmc.health.service.impl
 *
 * @author zmc
 * 日期: 2021/8/6
 */
@Service(interfaceClass = MemberService.class)
public class MemberServiceImpl implements MemberService {
    @Autowired
    private MemberDao memberDao;

    @Override
    public Member findByTelephone(String telephone) {
        return memberDao.findByTelephone(telephone);
    }

    @Override
    public void add(Member member) {
        memberDao.add(member);
    }

    /**
     * 调用服务查询到每一个最后一天为止的会员总数量
     * @param months
     * @return
     */
    @Override
    public List<Integer> getMemberReport(List<String> months) {
        List<Integer> memberCount  = new ArrayList<Integer>(months.size());
        for (String month : months) {
            month+="-31";//拼接最后一天
            Integer count = memberDao.findMemberCountBeforeDate(month);
            memberCount.add(count);
        }
        return memberCount;
    }
}

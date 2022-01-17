package com.atguigu.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.atguigu.dao.MemberDao;
import com.atguigu.pojo.Member;
import com.atguigu.service.MemberService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

/**
 * @Author ZhangRuJian
 * @Data 2022/1/11
 */

@Service(interfaceClass = MemberService.class)
@Transactional
public class MemberServiceImpl implements MemberService {

    @Autowired
    private MemberDao memberDao;

    @Override
    public Member findByTelephone(String telephone) {
        return memberDao.getMemberByTelephone(telephone);
    }

    @Override
    public void add(Member member) {
        memberDao.add(member);
    }

    @Override
    public List<Integer> findMemberCountByMouth(List<String> months) {

        List<Integer> list = new ArrayList<>();
        if (months != null && !months.isEmpty()){
            for (String month : months) {
                int count = memberDao.findMemberCountByMouth(month);
                list.add(count);
            }
        }
        return list;
    }
}

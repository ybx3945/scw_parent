package com.yb.user.service;

import com.yb.user.pojo.TMember;
import com.yb.user.pojo.TMemberAddress;

import java.util.List;

public interface UserService {

    void registerUser(TMember member);

    TMember login(String loginacct, String password);

    TMember findTMemberById(Integer id);

    List<TMemberAddress> findAddressByMemberId(Integer memberId);
}

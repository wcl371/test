package com.fh.member.service;

import com.fh.common.ServerResponse;
import com.fh.member.model.User;

public interface UserService {

    ServerResponse checkMemberName(String name);

    ServerResponse checkMemberPhone(String phone);

    ServerResponse register(User user);

    ServerResponse login(User user);

    ServerResponse queryUserList();
}

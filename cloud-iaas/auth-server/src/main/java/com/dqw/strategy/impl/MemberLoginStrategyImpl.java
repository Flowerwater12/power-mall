package com.dqw.strategy.impl;

import com.dqw.constant.AuthConstants;
import com.dqw.strategy.LoginStrategy;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

@Service(AuthConstants.MEMBER_LOGIN)
public class MemberLoginStrategyImpl implements LoginStrategy {


    @Override
    public UserDetails realLogin(String username) {
        return null;
    }
}

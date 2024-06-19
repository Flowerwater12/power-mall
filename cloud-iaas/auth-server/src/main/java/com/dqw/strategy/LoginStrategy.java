package com.dqw.strategy;

import org.springframework.security.core.userdetails.UserDetails;

public interface LoginStrategy {

    /**
     * 真正处理登录的方法
     * @param username
     * @return
     */
    UserDetails realLogin(String username);
}

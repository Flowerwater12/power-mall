package com.dqw.impl;

import com.dqw.constant.AuthConstants;
import com.dqw.factory.LoginStrategyFactory;
import com.dqw.strategy.LoginStrategy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.InternalAuthenticationServiceException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.servlet.http.HttpServletRequest;

/**
 * 自定义的认证流程
 */
@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    @Autowired
    private LoginStrategyFactory loginStrategyFactory;

    @Autowired
    HttpServletRequest  request;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        // 获取请求对象
        // 从请求头中获取登录的类型
        String loginType = request.getHeader(AuthConstants.LOGIN_TYPE);
        // 判断是否有值
        if (!StringUtils.hasText(loginType)){
            throw new InternalAuthenticationServiceException(AuthConstants.ILLEGAL_LOGIN_TYPE);
        }
        // 从登录策略工厂类中根据登录类型来获取具体的登录策略
        LoginStrategy loginStrategy = loginStrategyFactory.getInstance(loginType);
        // 调用登录策略对象的具体登录方法
        return loginStrategy.realLogin(username);
    }
}

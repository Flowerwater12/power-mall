package com.dqw.strategy.impl;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.dqw.constant.AuthConstants;
import com.dqw.domain.LoginSysUser;
import com.dqw.mapper.LoginSysUserMapper;
import com.dqw.model.SecurityUser;
import com.dqw.strategy.LoginStrategy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.util.Set;

@Service(AuthConstants.SYS_USER_LOGIN)
public class SysUserLoginStrategyImpl implements LoginStrategy {

    @Autowired
    private LoginSysUserMapper loginSysUserMapper;

    @Override
    public UserDetails realLogin(String username) {
        //查询登录用户的信息
        LoginSysUser loginSysUser = loginSysUserMapper.selectOne(new LambdaQueryWrapper<LoginSysUser>()
                .eq(LoginSysUser::getUsername, username));
        //判断用户是否存在
        if (ObjectUtil.isNotEmpty(loginSysUser)){
            //将用户信息放入到security容器当中去
            //声明一个存储在security容器中的对象
            SecurityUser securityUser = SecurityUser.builder()
                    .userId(loginSysUser.getUserId())
                    .loginType(AuthConstants.SYS_USER_LOGIN)
                    .status(loginSysUser.getStatus())
                    .password(loginSysUser.getPassword())
                    .username(loginSysUser.getUsername())
                    .build();
            //根据用户标识查询用户的操作权限
            Set<String> perms = loginSysUserMapper.selectPermsByUserId(loginSysUser.getUserId());
            //判断登录用户的操作权限是否为空
            if (CollectionUtil.isNotEmpty(perms)&&perms.size()!=0){
                  securityUser.setPerms(perms);
            }
            return securityUser;
        }
        return null;
    }
}

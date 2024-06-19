package com.dqw.util;

import com.dqw.model.SecurityUser;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.Set;

/**
 * 认证授权工具类
 */
public class AuthUtils {


    /**
     * 获取登录安全认证用户对象SecurityUser
     * @return
     */
    public static SecurityUser getLoginSecurityUser() {
        return (SecurityUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    }

    /**
     * 获取登录安全认证用户对象的用户标识
     * @return
     */
    public static Long getLoginSecurityUserId() {
        return getLoginSecurityUser().getUserId();
    }


    /**
     * 商城后台管理系统：用户操作权限集合
     * @return
     */
    public static Set<String> getLoginSysUserPerms() {
        return getLoginSecurityUser().getPerms();
    }
}

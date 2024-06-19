package com.dqw.constant;

/**
 * 网关和认证服务模块常量类
 */
public interface AuthConstants {

    /**
     * 令牌TOKEN在请求头的KEY
     */
    String AUTHORIZATION = "Authorization";

    /**
     * 令牌TOKEN的前缀
     */
    String BEARER = "bearer ";


    /**
     * 令牌TOKEN存放到redis中的前缀
     */
    String LOGIN_TOKEN_PREFIX = "login_token:";

    /**
     * 商城后台管理系统,登录的URL
     */
    String LOGIN_URL = "/doLogin";

    /**
     * 商城后台管理系统，登出的URL
     */
    String LOGOUT_URL = "/doLogout";

    /**
     * 登录类型：商城后台管理系统
     */
    String SYS_USER_LOGIN = "sysUserLogin";

    /**
     * 登录类型：商城用户购物系统
     */
    String MEMBER_LOGIN = "memberLogin";

    /**
     * 登录类型KEY
     */
    String LOGIN_TYPE = "loginType";

    /**
     * TOKEN的有效时长：4个小时间（单位为：秒）
     */
    Long TOKEN_TIME = 14400L;

    /**
     * 非法登录,类型不匹配
     */
    String  ILLEGAL_LOGIN_TYPE="非法登录，登录类型不匹配";
}

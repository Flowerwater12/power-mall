package com.dqw.constant;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 业务响应状态码枚举类
 */
@AllArgsConstructor
public enum BusinessEnum {
    UN_AUTHORIZATION(401,"未授权"),
    ACCESS_DENY_FAIL(403,"权限不足，请联系管理员"),
    OPERATION_FAIL(-1,"操作失败")
    ;

    @Getter
    private Integer code;

    @Getter
    private String desc;

}

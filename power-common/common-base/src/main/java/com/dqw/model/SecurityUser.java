package com.dqw.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

/**
 * 安全认证用户对象
 */

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SecurityUser implements UserDetails {

    // 商城后台管理系统的用户对象相关属性
    private Long userId;
    private String username;
    private String password;
    private Integer status;
    private Long shopId;
    private String loginType;
    private Set<String> perms = new HashSet<>();

    // xxxx系统用户相关属性

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return null;
    }

    @Override
    public String getPassword() {
        return this.password;
    }

    @Override
    public String getUsername() {
        return this.loginType+this.userId;
    }

    @Override
    public boolean isAccountNonExpired() {
        return this.status == 1;
    }

    @Override
    public boolean isAccountNonLocked() {
        return this.status == 1;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return this.status == 1;
    }

    @Override
    public boolean isEnabled() {
        return this.status == 1;
    }

    public void setPerms(Set<String> perms) {
        HashSet<String> finalPermSet = new HashSet<>();
        // 循环遍历所有权限
        perms.forEach(perm -> {
            // 判断是否包含多个权限
            if (perm.contains(",")) {
                // 通过,号进行分隔
                String[] realPerms = perm.split(",");
                for (String realPerm : realPerms) {
                    finalPermSet.add(realPerm);
                }
            } else {
                // 只有一个权限
                finalPermSet.add(perm);
            }
        });

        this.perms = finalPermSet;
    }
}

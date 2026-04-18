package com.whh.hloveyq.security;

import com.whh.hloveyq.domain.User;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;

/**
 * 认证用户详情类
 * 实现 Spring Security 的 UserDetails 接口，用于用户认证和授权
 */
public class AuthUserDetails implements UserDetails {
    
    private final User user;

    public AuthUserDetails(User user) {
        this.user = user;
    }

    /**
     * 获取用户实体对象
     * @return User 实体
     */
    public User getUser() {
        return user;
    }

    /**
     * 获取用户 ID
     * @return 用户 ID
     */
    public Long getId() {
        return user.getId();
    }

    /**
     * 获取用户昵称
     * @return 用户昵称
     */
    public String getNickname() {
        return user.getNickname();
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        // 当前系统没有角色/权限区分，返回空列表
        return List.of();
    }

    @Override
    public String getPassword() {
        return user.getPasswordHash();
    }

    @Override
    public String getUsername() {
        return user.getEmail();
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}

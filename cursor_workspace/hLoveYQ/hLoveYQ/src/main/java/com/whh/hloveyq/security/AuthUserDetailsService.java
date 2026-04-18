package com.whh.hloveyq.security;

import com.whh.hloveyq.domain.User;
import com.whh.hloveyq.repo.UserRepository;
import com.whh.hloveyq.service.UserService;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

/**
 * 认证用户详情服务类
 * 实现 Spring Security 的 UserDetailsService 接口，用于加载用户信息进行认证
 */
@Service
public class AuthUserDetailsService implements UserDetailsService {
    
    private final UserRepository userRepository;

    public AuthUserDetailsService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    /**
     * 根据用户名（邮箱）加载用户详情
     * @param username 用户名（邮箱地址）
     * @return UserDetails 用户详情对象
     * @throws UsernameNotFoundException 当用户不存在时抛出
     */
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        String email = UserService.normalizeEmail(username);
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("用户不存在"));
        return new AuthUserDetails(user);
    }
}

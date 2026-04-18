package com.whh.hloveyq.service;

import com.whh.hloveyq.domain.User;
import com.whh.hloveyq.repo.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.Locale;

/**
 * 用户服务类
 * 负责用户注册、邮箱规范化等业务逻辑
 */
@Service
public class UserService {
    
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    /**
     * 用户注册
     * @param emailRaw 原始邮箱地址
     * @param passwordRaw 原始密码
     * @param nicknameRaw 原始昵称
     * @return 注册成功的用户
     * @throws IllegalArgumentException 当邮箱已存在或昵称为空时抛出
     */
    @Transactional
    public User register(String emailRaw, String passwordRaw, String nicknameRaw) {
        String email = normalizeEmail(emailRaw);
        if (userRepository.existsByEmail(email)) {
            throw new IllegalArgumentException("该邮箱已被注册");
        }

        String nickname = nicknameRaw == null ? "" : nicknameRaw.trim();
        if (nickname.isEmpty()) {
            throw new IllegalArgumentException("昵称不能为空");
        }

        String passwordHash = passwordEncoder.encode(passwordRaw);
        User user = new User(email, passwordHash, nickname, Instant.now());
        return userRepository.save(user);
    }

    /**
     * 邮箱规范化处理
     * - 去除首尾空格
     * - 转换为小写
     * @param emailRaw 原始邮箱地址
     * @return 规范化后的邮箱地址
     */
    public static String normalizeEmail(String emailRaw) {
        if (emailRaw == null) return "";
        return emailRaw.trim().toLowerCase(Locale.ROOT);
    }
}

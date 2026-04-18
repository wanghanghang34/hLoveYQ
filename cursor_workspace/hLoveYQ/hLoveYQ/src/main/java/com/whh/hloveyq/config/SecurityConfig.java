package com.whh.hloveyq.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

/**
 * Spring Security 配置类
 * 定义安全过滤链和密码编码器
 */
@Configuration
@EnableWebSecurity
public class SecurityConfig {
    
    /**
     * 配置密码编码器
     * 使用 BCrypt 算法，其设计上的"慢"特性可以有效防止密码破解
     * @return PasswordEncoder 实例
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    /**
     * 配置安全过滤链
     * - 公开页面：登录、注册、静态资源
     * - 其他所有请求需要用户登录
     * - 使用自定义 Thymeleaf 登录页面
     * - 启用 CSRF 保护
     * @param http HttpSecurity 对象
     * @return SecurityFilterChain 实例
     * @throws Exception 配置异常
     */
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
            .authorizeHttpRequests(auth -> auth
                // 公开页面和静态资源
                .requestMatchers(
                    "/login",
                    "/login/",
                    "/register",
                    "/register/",
                    "/css/**",
                    "/js/**",
                    "/img/**",
                    "/manifest.webmanifest",
                    "/service-worker.js",
                    "/favicon.ico"
                ).permitAll()
                // 其他所有请求需要登录
                .anyRequest().authenticated()
            )
            .formLogin(form -> form
                // 使用自定义 Thymeleaf 登录页面
                .loginPage("/login")
                .defaultSuccessUrl("/", true)
                .permitAll()
            )
            .logout(logout -> logout
                .logoutUrl("/logout")
                .logoutSuccessUrl("/login?logout")
            )
            .csrf(Customizer.withDefaults());

        return http.build();
    }
}

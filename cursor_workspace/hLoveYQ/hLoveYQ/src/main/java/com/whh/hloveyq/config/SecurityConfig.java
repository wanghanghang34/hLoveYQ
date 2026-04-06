package com.whh.hloveyq.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {
	@Bean
	public PasswordEncoder passwordEncoder() {
		// BCrypt is slow-by-design, which makes password cracking much harder.
		return new BCryptPasswordEncoder();
	}

	@Bean
	public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
		http
				.authorizeHttpRequests(auth -> auth
						// Public pages and static assets.
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
						// Everything else requires a logged-in user.
						.anyRequest().authenticated()
				)
				.formLogin(form -> form
						// Use our own Thymeleaf login page.
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


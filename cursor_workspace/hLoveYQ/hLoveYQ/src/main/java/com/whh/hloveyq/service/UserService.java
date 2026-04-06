package com.whh.hloveyq.service;

import com.whh.hloveyq.domain.User;
import com.whh.hloveyq.repo.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.Locale;

@Service
public class UserService {
	private final UserRepository userRepository;
	private final PasswordEncoder passwordEncoder;

	public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
		this.userRepository = userRepository;
		this.passwordEncoder = passwordEncoder;
	}

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

	public static String normalizeEmail(String emailRaw) {
		if (emailRaw == null) return "";
		return emailRaw.trim().toLowerCase(Locale.ROOT);
	}
}


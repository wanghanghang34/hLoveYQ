package com.whh.hloveyq.security;

import com.whh.hloveyq.domain.User;
import com.whh.hloveyq.repo.UserRepository;
import com.whh.hloveyq.service.UserService;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class AuthUserDetailsService implements UserDetailsService {
	private final UserRepository userRepository;

	public AuthUserDetailsService(UserRepository userRepository) {
		this.userRepository = userRepository;
	}

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		String email = UserService.normalizeEmail(username);
		User user = userRepository.findByEmail(email)
				.orElseThrow(() -> new UsernameNotFoundException("用户不存在"));
		return new AuthUserDetails(user);
	}
}


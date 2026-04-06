package com.whh.hloveyq.security;

import com.whh.hloveyq.domain.User;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;

public class AuthUserDetails implements UserDetails {
	private final User user;

	public AuthUserDetails(User user) {
		this.user = user;
	}

	public User getUser() {
		return user;
	}

	public Long getId() {
		return user.getId();
	}

	public String getNickname() {
		return user.getNickname();
	}

	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
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


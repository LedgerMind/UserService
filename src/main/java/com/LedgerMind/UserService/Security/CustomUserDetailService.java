package com.LedgerMind.UserService.Security;

import java.util.List;

import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import com.LedgerMind.UserService.Entity.User;
import com.LedgerMind.UserService.Service.UserService;

import lombok.RequiredArgsConstructor;
@Component
@RequiredArgsConstructor
public class CustomUserDetailService implements UserDetailsService{

	private final UserService service;
	
	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		User user = service.findByEmail(username).orElseThrow();
		return UserPrincipal.builder()
				.userId(user.getId())
				.email(user.getEmail())
				.authorities(List.of(new SimpleGrantedAuthority(user.getRole())))
				.password(user.getPassword())
				.build();
	}

}